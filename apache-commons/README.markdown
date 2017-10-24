# Apache Commons库(2017-Q4)

Apache Commons库致力于可复用的各种Java组件的开发.

> Apache Commons is an Apache project focused on all aspects of reusable Java components.

- The Commons Proper - 可复用Java组件仓库
    - 开发并维护可复用的Java组件
        - 对其他库最小的依赖性(minimal dependencies)
        - 接口稳定性(as stable as possible)
- The Commons Sandbox - Java组件开发工作空间
- The Commons Dormant - 不活跃组件仓库

**Apache Commons常用组件**

- Lang
    - java.lang的扩展
- BeanUtils
    - 易用的Java反射(reflection)和自我检查(introspection)封装
- Collections
    - Java Collections的扩展
- DbUtils
    - JDBC帮手
- IO
    - I/O工具集合
- Logging
    - 各类日志API实现的封装
- Math
    - 轻量级算数和统计组件

---

### BeanUtils

Java语言提供的反射(java.lang.reflect)和自我检查(java.beans)机制的API.这些API相当难理解和应用,BeanUtils组件提供了易用的封装.

Bean自我检查组件提供了底层的工具来getting和setting符合JavaBean规范类的属性,并且提供了一种动态定义和获取JavaBean属性的机制.

> The Bean Introspection Utilities component of the Apache Commons subproject offers low-level utility classes that assist in getting and setting property values on Java classes that follow the naming design patterns outlined in the JavaBeans Specification, as well as mechanisms for dynamically defining and accessing bean properties.

**JavaBean规范**

- 序列化(实现`java.io.Serialization`接口)
- 无参构造(`Class.newInstance()`方法会调用无参构造函数)
- setter & getter pattern命名规范

- API
    - `org.apache.commons.beanutils.BeanUtils`
        - `getProperty(bean, name): String`
        - `setProperty(bean, name, value): void`, value类型转化为目标属性类型
            - Simple Property
            - Indexed Property
            - Mapped Property
            - Nested Property
    - `org.apache.commons.beanutils.PropertyUtils`
        - `getProperty(bean, name): Object`
        - `setProperty(bean, name, value): void`, value类型不进行转化
    - `org.apache.commons.beanutils.MethodUtils`
        - `invokeMethod(object, methodName, arg)`
    - `org.apache.commons.beanutils.copyProperties(bean, anotherBean)`

---

### Lang
