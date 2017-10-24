## Spring初级(2017-Q3-failed 2017-Q4)

### 描述

Spring 是一个解决依赖注入(Dependency Injection, DI)或者控制反转(Inverse Of Control, IOC)的容器框架,致力于松耦合和单一职责.

> Spring core is a framework for Dependency Injection, which stems from single responsibility and loose coupling.


### Spring 工作流程

![spring mvc 工作流程](../image/springmvc.png)

**具体处理请求步骤:**

1. 用户发送请求, 被前端控制器拦截, 前端控制器根据请求的信息选择相应的页面控制器, 并将请求委托给此页面控制器来处理.
2. 页面控制器接收到请求后, 首先收集并绑定请求参数到一个命令对象(表单对象)中, 并进行验证转换等操作, 然后将命令对象(表单对象)委托给业务对象进行处理, 最后返回一个ModelAndView对象.
3. 前端控制器根据返回的视图名, 选择相应的视图进行渲染, 并将模型数据传入到视图中以便展示.
4. 前端控制器将响应结果返回给用户.

> - Transition 1: User send request to server by submitting form/ by clicking hyperlink etc. Request is intially givn to web.xml.
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

### 启动一个Spring(maven)项目需要做什么工作

