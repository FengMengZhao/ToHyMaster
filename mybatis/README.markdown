# mybatis(2017-Q4)

**Mybatis是什么**

Mybatis是一个优秀的持久化框架,它支持定制的SQL,存储过程和高级映射.Mybatis避免几乎所有的JDBC代码和手动设置参数及获取结果集.Mybatis可以使用简单的XML或者注解来配置映射原生信息,将接口和Java的POJOS(Plain Old Java Object,普通的Java对象)映射承数据库中的记录.

### 通过XML的方式进行映射原生信息

**创建一个普通的Java项目并引入框架依赖的Jar包**

![引入jar包](../image/import-mybatis-jars.png)

> mybatis框架包 + JDBC连接Driver

**创建数据库表信息**

![创建表格](../image/create-table.png)

建表语句:

    DROP TABLE IF EXISTS "mybatis"."my_users";
    CREATE TABLE "mybatis"."my_users" (
    "id" int4 DEFAULT nextval('my_users_id_seq'::regclass) NOT NULL,
    "name" varchar(20) COLLATE "default",
    "age" int4
    );

    ALTER TABLE "mybatis"."my_users" ADD PRIMARY KEY ("id");

**CLASSPATH下配置conf.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
        <environments default="development">
            <environment id="development">
                <transactionManager type="JDBC" />
                <!-- 配置数据库连接信息 -->
                <dataSource type="POOLED">
                    <property name="driver" value="${driver_class}" />
                    <property name="url" value="jdbc:postgresql://${ip}:${port}/${db_name}" />
                    <property name="username" value="${username}" />
                    <property name="password" value="${password}" />
                </dataSource>
            </environment>
        </environments>
        
    </configuration>

**创建数据库表对应的JavaBean**

    package com.fmz.mybatis;

    public class User {
        
        private int id;
        private String name;
        private int age;
        
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        
        @Override
        public String toString(){
            return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
        }
        
    }

**创建mapper配置**

![mybatis-mapper](../image/mybatis-mapper.png)

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

    <mapper namespace="com.fmz.mapping.userMapping">
        <!-- 查询操作 -->
        <select id="getUser" parameterType="int"
            resultType="com.fmz.mybatis.User">
            select * from my_users where id=#{id}		
        </select>
        
        <!-- 增加操作 -->
        <insert id="addUser" parameterType="com.fmz.mybatis.User">
            insert into my_users(name,age) values(#{name}, #{age})	
        </insert>
        
        <!-- 删除操作 -->
        <delete id="deleteUser" parameterType="int">
            delete from my_users where id = #{id}
        </delete>
        
        <!-- 更新操作 -->
        <update id="updateUser" parameterType="com.fmz.mybatis.User">
            update my_users set name = #{name}, age = #{age} where id = #{id}
        </update>
        
        <!-- 查询全部操作 参数是什么意思??? -->
        <select id="getAllUsers" resultType="com.fmz.mybatis.User">
            select * from my_users;
        </select>

    </mapper>

*parameterType表示输入的参数类型;resultType表示查询返回结果集类型.其他参数还有:*

- flushCash: ture表示当语句执行的时候,本地和二级缓存刷新
- timeout: 数据库驱动等待一个请求返回的时间,如果超出时间会发生抛出异常.默认是unset
- statementType: STATEMENT,PREPARED或者是CALLABLE
- ...

*mapper标签除了CURD操作之外,还有哪些标签:*

- sql: 可以被其他声明复用的slq片段
- cahce: 给定命名空间的缓存配置
- cache-ref: 从其他命名空间中引用

**注册mapper文件**

![注册mapper文件](../image/register-mapper.png)

	<mappers>
		<mapper resource="com/fmz/mapping/userMapping.xml"/>
	</mappers>

> 将代码段加入conf.xml中.注意文件路径的写法: com/fmz/mapping/userMapping.xml.


**创建获得SQL工厂的工具类**

    package com.fmz.util;

    import java.io.InputStream;

    import org.apache.ibatis.session.SqlSession;
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.apache.ibatis.session.SqlSessionFactoryBuilder;

    public class MyBatisUtils {

        public static SqlSessionFactory getSqlSessionFactory(){
            String resource = "conf.xml";
            InputStream is = MyBatisUtils.class.getClassLoader().getResourceAsStream(resource);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            return factory;
        }
        
        public static SqlSession getSqlSession(){
            return getSqlSessionFactory().openSession();
        }
        
        public static SqlSession getSqlSession(boolean isAutoCommit){
            return getSqlSessionFactory().openSession(isAutoCommit);
        }
    }

**无XML通过Java代码获取SQL工厂**

    package com.fmz.test;

    import javax.sql.DataSource;

    import org.apache.ibatis.mapping.Environment;
    import org.apache.ibatis.session.Configuration;
    import org.apache.ibatis.session.SqlSession;
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.apache.ibatis.session.SqlSessionFactoryBuilder;
    import org.apache.ibatis.transaction.TransactionFactory;
    import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
    import org.apache.commons.dbcp.BasicDataSource;
    import org.junit.Test;

    import com.fmz.mapping.IUserMapping;
    import com.fmz.mybatis.User;

    public class TestCURDByAnnotationMapping {
        
        @Test
        public void testAdd(){
            
            //不使用xml来获取SQLSessionFactory
            //创建数据库连接
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://172.16.192.194:5432/test");
            dataSource.setUsername("fmz");
            dataSource.setPassword("147258");
            //创建事务工厂
            TransactionFactory trxFactory = new JdbcTransactionFactory();
            Environment env = new Environment("dev", trxFactory, dataSource);
            Configuration config = new Configuration(env);
            config.addMapper(IUserMapping.class);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(config);
            SqlSession sqlSession = sessionFactory.openSession(true);
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            User user = new User();
            user.setName("XXX");
            user.setAge(37);
            //执行插入操作
            int retResult = mapper.add(user);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("插入操作执行结果: " + retResult);
        }
        
    }

**测试基于xml配置的数据库CURD操作**

    package com.fmz.test;

    import java.util.List;

    import org.apache.ibatis.session.SqlSession;
    import org.junit.Test;

    import com.fmz.mybatis.User;
    import com.fmz.util.MyBatisUtils;

    public class TestCURDByXmlMapping {
        
        @Test
        public void testAdd(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            String statement = "com.fmz.mapping.userMapping.addUser";
            User user = new User();
            user.setName("XXX");
            user.setAge(7);
            //执行插入操作
            int retResult = sqlSession.insert(statement, user);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("插入操作执行结果: " + retResult);
        }
        
        @Test
        public void testUpdate(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            String statement = "com.fmz.mapping.userMapping.updateUser";
            User user = new User();
            user.setId(3);
            user.setName("冯山林_UPDATE");
            user.setAge(7);
            //执行插入操作
            int retResult = sqlSession.update(statement, user);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("更新操作执行结果: " + retResult);
        }
        
        @Test
        public void testDelete(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            String statement = "com.fmz.mapping.userMapping.deleteUser";
            int retResult = sqlSession.delete(statement, 4);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("删除操作执行结果: " + retResult);
        }
        
        @Test
        public void testGetAll(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            String statement = "com.fmz.mapping.userMapping.getAllUsers";
            List<User> lstUsers = sqlSession.selectList(statement);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("获取全部执行结果: " + lstUsers);
        }
        
    }

---

### 基于注解(Annotation)的方式进行映射原生信息

**创建接口**

![基于注解方法](../image/based-annotation.png)

    package com.fmz.mapping;

    import java.util.List;

    import org.apache.ibatis.annotations.Delete;
    import org.apache.ibatis.annotations.Insert;
    import org.apache.ibatis.annotations.Select;
    import org.apache.ibatis.annotations.Update;

    import com.fmz.mybatis.User;

    public interface IUserMapping {

        @Insert("insert into my_users(name, age) values(#{name}, #{age})")
        public int add(User user);
        
        @Delete("delete from my_users where id = #{id}")
        public int delete(int id);
        
        @Update("update my_users set name = #{name}, age = #{age} where id = #{id}")
        public int update(User user);
        
        @Select("select * from my_users where id = #{id}")
        public User getById(int id);
        
        @Select("select * from my_users")
        public List<User> getAll();
    }

**注册接口**

	<mappers>
		<mapper class="com.fmz.mapping.IUserMapping"/>
	</mappers>

> 将代码段加入conf.xml中.注意文件路径的写法: com.fmz.mapping.IUserMapping.


**测试基于注解配置的数据库CURD操作**

    package com.fmz.test;

    import java.util.List;

    import org.apache.ibatis.session.SqlSession;
    import org.junit.Test;

    import com.fmz.mapping.IUserMapping;
    import com.fmz.mybatis.User;
    import com.fmz.util.MyBatisUtils;

    public class TestCURDByAnnotationMapping {
        
        @Test
        public void testAdd(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            User user = new User();
            user.setName("XXX");
            user.setAge(7);
            //执行插入操作
            int retResult = mapper.add(user);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("插入操作执行结果: " + retResult);
        }
        
        @Test
        public void testUpdate(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            User user = new User();
            user.setId(1);
            user.setName("XXX_UPDATE");
            user.setAge(7);
            //执行插入操作
            int retResult = mapper.update(user);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("更新操作执行结果: " + retResult);
        }
        
        @Test
        public void testDelete(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            int retResult = mapper.delete(5);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("删除操作执行结果: " + retResult);
        }
        
        @Test
        public void testGetById(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            User user = mapper.getById(3);
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("获取全部执行结果: " + user);
        }
        
        @Test
        public void testGetAll(){
            
            SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
            
            IUserMapping mapper = sqlSession.getMapper(IUserMapping.class);
            List<User> lstUsers = mapper.getAll();
            //手动提交事务
            //sqlSession.commit();
            //关闭SqlSession
            sqlSession.close();
            System.out.println("获取全部执行结果: " + lstUsers);
        }
        
    }

---

### mybatis spring整合

#### 一. 使用maven创建web项目

    mvn archetype:generate -DgroupId=me.gacl -DartifactId=spring4-mybatis3 -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

**编辑pom文件**

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.fmz.mybatis</groupId>
      <artifactId>spring4-mybatis3</artifactId>
      <packaging>war</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>spring4-mybatis3</name>
      <url>http://maven.apache.org</url>
      <dependencies>

      </dependencies>
      <build>
        <finalName>spring4-mybatis3</finalName>
      </build>
    </project>

> 修改`<name>spring4-mybatis3 Maven Webapp</name>`部分，把"Maven Webapp"这部分包含空格的内容去掉，否则Maven在编译项目时会因为空格的原因导致一些莫名其妙的错误出现，修改成:<name>spring4-mybatis3</name>.

**将生成的项目导入MyEclipse(Existing Maven Projects)中**

手动创建`src/main/java`,`src/main/resources`,`src/test/java`,`src/test/resources`:

![create source folder](../image/new-source-folder.png)

如果无法创建成功,需要删除build path下的Source:

![delete source](../image/delete-source.png)

创建成功后,如下图所示:

![create source success](../image/create-source-success.png)

#### 二. 创建数据库信息

    create table spring_users(
        user_id char(32) NOT NULL,
        user_name varchar(30) DEFAULT NULL,
        user_birthday date DEFAULT NULL,
        user_salary NUMERIC DEFAULT NULL,
        PRIMARY KEY (user_id)
    );

#### 三. Mybatis Spring的整合

`mybatis-spring`将Mybatis和Spring无缝的整合在一起.这个包允许Mybatis参与Spring的事务管理,负责将Mybatis mapper和sqlSession注入到其他bean中,把Mybatis Exception转化为Spring DataAccessException.在pom.xml中加入如下依赖,完成Mybatis Spring的整合:

    <!-- MyBatis integration -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>1.2.1</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.2.3</version>
    </dependency>

**Spring Applicaton Context配置**

Mybatis和Spring整合使用,至少要做两个配置:`sqlSessionFactory`和`mapper interface`.

> `sqlSessionFactory`需要一个dataSource.

---
