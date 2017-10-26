## Spring初级(2017-Q3-failed 2017-Q4)

### 描述

Spring 是一个解决依赖注入(Dependency Injection, DI)或者控制反转(Inverse Of Control, IOC)的容器框架,致力于松耦合和单一职责.

> Spring core is a framework for Dependency Injection, which stems from single responsibility and loose coupling.

**概览Spring模块**

![Spring Modules](../image/spring-overview.png)

---

### Spring 工作流程

![spring mvc 工作流程](../image/springmvc-process.png)

**具体处理请求步骤:**

1. 用户发送请求, 被前端控制器拦截, 前端控制器根据请求的信息选择相应的页面控制器, 并将请求委托给此页面控制器来处理.
2. 页面控制器接收到请求后, 首先收集并绑定请求参数到一个命令对象(表单对象)中, 并进行验证转换等操作, 然后将命令对象(表单对象)委托给业务对象进行处理, 最后返回一个ModelAndView对象.
3. 前端控制器根据返回的视图名, 选择相应的视图进行渲染, 并将模型数据传入到视图中以便展示.
4. 前端控制器将响应结果返回给用户.

- Transition 1: User send request to server by submitting form/ by clicking hyperlink etc. Request is intially given to web.xml.
- Transition 2: web.xml routes request to DispatcherServlet by looking at tag.
- Transition 3: DispatcherServlet wil take help of HandlerMapping and get to know the controller class name associated with the given request.
- Transition 4: So request transfer to the controller, and then controller will process the request by executing appropriate method and returns ModelAndView object(contains Model data and View name) back to the DispatcherServlet.
- Transition 5: Now DispatcherServlet send the model object to ViewResolver to get the acutal view page.
- Transition 6: Finally DispatcherServlet will pass the Model object to View page to display the result.

**Spring通过xml配置模式装载Bean的过程**

1. 将程序内所有 XML 或 Properties 配置文件加载入内存中
2. Java类里面解析xml或properties里面的内容，得到对应实体类的字节码字符串以及相关的属性信息
3. 使用反射机制，根据这个字符串获得某个类的Class实例
4. 动态配置实例的属性

---

### Eclipse创建一个spring web项目

**新建一个web项目**

![spring-new-web-project](../image/spring-new-web-project.png)

![create-a-javaee-web-project](../image/create-a-javaee-web-project.png)

![conf-output-folder](../image/conf-output-folder.png)

![web-module](../image/web-module.png)

![maven-project-support](../image/maven-project-support.png)

![config-maven-dependency](../image/config-maven-dependency.png)

**修改pom文件**

![springmvc-pom](../image/springmvc-pom.png)

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.fmz.springweb</groupId>
      <artifactId>spring-web-demo</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <packaging>war</packaging>
      <name>spring-web-demo</name>
      <description/>
      <properties>
        <webVersion>3.0</webVersion>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <org.springframework.version>4.2.3.RELEASE</org.springframework.version>
      </properties>
      <dependencies>
        <!-- spring相关 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>
      </dependencies>
      <build>
        <sourceDirectory>src</sourceDirectory>
        <resources>
          <resource>
            <directory>src</directory>
            <excludes>
              <exclude>**/*.java</exclude>
            </excludes>
          </resource>
        </resources>
        <plugins>
          <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.3.2</version>
            <configuration>
              <source>1.7</source>
              <target>1.7</target>
            </configuration>
          </plugin>
          <plugin>
            <artifactId>maven-war-plugin</artifactId>
            <version>2.6</version>
            <configuration>
              <warSourceDirectory>${basedir}/web</warSourceDirectory>
              <failOnMissingWebXml>false</failOnMissingWebXml>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </project>

> 通常在项目中,我们不使用commons-logging依赖做日志输出,而是用slf4j.解除这种依赖关系,需要在pom中加入:

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
               <artifactId>spring-core</artifactId> 
               <version>4.3.8.RELEASE</version> 
               <exclusions>
                <exclusion> 
                    <groupId>commons-logging</groupId> 
                    <artifactId>commons-logging</artifactId>
              </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

> 增加slf4j + logback日志依赖,需要加入pom文件:

    <!-- 日志 -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.1.2</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.1.2</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>log4j-over-slf4j</artifactId>
        <version>1.6.1</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>1.6.1</version>
    </dependency>

**修改web.xml文件**

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" id="WebApp_ID" version="3.0">
        <display-name>spring-web-demo</display-name>
        <welcome-file-list>
          <welcome-file>index.html</welcome-file>
          <welcome-file>index.htm</welcome-file>
          <welcome-file>index.jsp</welcome-file>
          <welcome-file>default.html</welcome-file>
          <welcome-file>default.htm</welcome-file>
          <welcome-file>default.jsp</welcome-file>
        </welcome-file-list>
      
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </context-param>
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
        
        <!-- springmvc前端控制器 -->
        <servlet>
            <servlet-name>springmvc</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>classpath:springmvc-servlet.xml</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>
        <servlet-mapping>
            <servlet-name>springmvc</servlet-name>
            <!-- 使此前端控制器拦截所有请求 -->
            <url-pattern>/</url-pattern>
        </servlet-mapping>
    </web-app>

**添加applicationContext.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" 
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                            http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
                            http://www.springframework.org/schema/context
                            http://www.springframework.org/schema/context/spring-context-4.2.xsd">
            
    </beans>

**添加springmvc-servlet.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans 
                            http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/context 
                            http://www.springframework.org/schema/context/spring-context.xsd">
        <!-- 处理映射器 -->                   
        <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"></bean>
        <!-- 处理器适配器 -->
        <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"></bean>
        <!-- 视图解释器 -->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"></bean>
        <!-- 定义一个bean, 即处理器(或控制器), 映射"/hello"请求 -->
        <bean name="/hello" class="com.fmz.springmvc.HelloController"></bean>
        
        <!-- spring自动扫描包路径com.fmz.springmvc.controller下的所有包和类 -->
        <context:component-scan base-package="com.fmz.springmvc"></context:component-scan>   
        <!-- annotation处理映射器 -->                   
        <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"></bean>    
        <!-- annotation处理器适配器 -->
        <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"></bean>    
        <!-- 视图解释器 -->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"></bean>
    </beans>

**Bean配置的方法-HelloWorldController**

    package com.fmz.springweb.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.Controller;

    public class HelloWorldController implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            ModelAndView mv = new ModelAndView();
            mv.addObject("message", "fmz");
            mv.setViewName("/jsp/hello.jsp");;
            return mv;
        }
        
    }

> `HttpServletResponse`和`HttpServletRequest`涉及到对JavaEE API的依赖,需要将Tomcat运行时的依赖加入到项目的依赖中: `build path --> add library --> MyEclipse Server Library`

**注解的方法-HelloWorldAnnotationController**

    package com.fmz.springweb.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.servlet.ModelAndView;


    @Controller
    public class HelloWorldAnnotationController{

        private int count = 0;
        
        @RequestMapping(value="/count")
        public ModelAndView count(){
            ModelAndView mv = new ModelAndView();
            mv.addObject("message", "count...");
            mv.setViewName("/jsp/count.jsp");
            System.out.println(count++);
            return mv;
        }
        
        @RequestMapping(value="/hi")
        public ModelAndView hi(String param){
            ModelAndView mv = new ModelAndView();
            mv.addObject("message", param);
            mv.setViewName("/jsp/hi.jsp");
            return mv;
        }
    }

---

### spring项目源码

- [Spring Java项目 - HelloWorld](./springdemo-helloworld)
- [Spring web项目 - He'llWorld](./spring-web-demo)

---
