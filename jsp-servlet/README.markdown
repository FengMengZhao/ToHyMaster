# JSP & Servlet

## JSP

### JSP生命周期

JSP的生命周期是一个将JSP Page转化为Servlet的过程,目的是为了处理service request.

1. Translation of JSP page: container validates the syntactic correctness of the JSP page and tag files.
2. Compilation of JSP page(Compilation of JSP page into _jsp.java extends org.apache.jasper.runtime.HttpJspBase extends javax.servlet.http.HttpServlet implemetns javax.servlet.jsp.HttpJspPage).
3. Classloading (_jsp.java is converted to class file _jsp.class)
4. Instantiation(Object of generated servlet is created)
5. Initialisation(_jspinit() method is invoked by container)
6. Request Processing(_jspService() method is invoked by the container)
7. Destroy (_jspDestroy() method invoked by the container)

### JSP编译过程

- Parsing of JSP
- Turning JSP into servlet.
- Compiling the servlet.

> 当浏览器请求一个JSP page时候,JSP engine查看是否编译JSP page,如果JSP没有被编译过或者上次编译之后有所改动,则JSP engine会编译JSP page.

### JSP内置对象

- out: javax.servlet.jsp.JspWriter
    - page scope
- request: javax.servlet.http.HttpServletRequest
    - request scope
- response: javax.servlet.http.HttpServletResponse
    - page scope
- config: javax.servlet.ServletConfig
    - page scope
- application: javax.servlet.ServletContext
    - application scope
- session: javax.servlet.http.HttpSession
    - session scope
- pageContext: javax.servlet.jsp.PageContext(jsp管理者)
    - page scope
- page: java.lang.Object
    - page scope
- exception: java.lang.Trowable
    - only avaiable on error pages

> Jsp has 9 implicit objects that we can directly use in jsp. Seven of them are declared as local variable at the start of _jspService() method whereas request and response are part of _jspServie() methond arguement.

### JSP scriptlet(脚本)

- <%! code %> scriptlet of declaration tag: that are inserted into the body of the servlet class, outside of any existing methods.
- <%= Expression %> scriptlet of expression tag: that are evaluated and inserted into the output.
- <% code %> scriptlet of: thar are inserted into the servlet's service(_jspService) method.

### JSP directive(指令)

- <%@ directive{attribute=value}* %>
    - <%@ page %>
    - <%@ include file="" %>
    - <%@ taglib uri="" prefix="" %>

### JSP tag(标签/行为)

- <jsp:elements {attribute="value"}* />
    - <jsp:include />: 与<%@ include file="" %>的不同在于,后者是静态包含(.java),前者是动态包含(.class).
    - jsp bean行为
        - <jsp:useBean id="beanName" class="" scope="" />
        - <jsp:setProperty name="beanName" property="propertyName" value="" />
        - <jsp:getProperty name="beanName" propertyName="propertyName" />
    - <jsp:forward />
        - request.getRequestDispatcher("someServlet").forward(request,response)

### Expression Language(EL)

The EL simplifies the accessibility of data stored in the java bean component, and other objects like request, session, application etc.

- EL implicit object 
    - pageScope: page scope
    - requestScope: request scope
    - sessionScope: session scope
    - applicationScope: application scope
    - param: 表示一个请求参数 ${param.username} 等效 request.getParameter("username")
    - paramValues: 表示一组请求参数 ${paramValues.loves} 等效 request.getParameterValues("username")
    - header: 表示一个请求头
    - headerValues: 表示一组请求头
    - cookie: 获得cookie对象
    - initParam: web项目初始化参数, servletContext.getInitParameter("xxx")
    - pageContext: 表示jsp内置对象pageContext,能够获取request等其他jsp内置对象

### JSTL标签库(JSP Standard Tag Library)

JSTL repersents a set of tags to simplify the JSP development.

- 优点
    - 快速开发
    - 代码复用
    - 不使用<%%>标签

JSTL包含多种类型的标签: core,fmt,fn方法库,sql标签库.

标签导入: `<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>`

---

## Servlet

Servlet is a web componet that is deployed on the server to create dynamic web pages.

> CGI(Common Gateway Interface) enables the web server to call an external program and pass http request information to the external program to process the request.

### Web Service VS Web Application

Web service refers to software, that servers data in any format(xml/json ect.) through some kind of web interface. The inteface can be called API. REST and SOAP are ways to design the API.

Application is the software that is using this API produced by the web service.

In other words, web service is "server" and application is "client". Usually server serves machines and client serve the user.

### Servlet的生命周期

- Servlet class is loaded.
- Servlet instance is created(instantiation).
- init method is invoked.
- service method is invoked.
- destroy method is invoked.

> There is only one instance of the servlet on each node in multi-clustered environment or you can say there is only one instance of each servlet on each JVM machine.

> Servlet is initialized on application startup or at the first time when the servlet is invoked.

> All the servlet instances are destroyed when server is shutting down or an application disposal.

### JSP和Servlet缓存

`页面` <-- `Servlet缓存` <-- `JSP缓存`

JSP页面转换为Servlet后,使用的out对象是JspWriter类型的,所以会先将要发送的数据存入JSP的缓存中,等待JSP输出缓存满了再自动刷新到Servlet输出缓存,等Servlet输出缓存满了,或者程序结束了,就会输出到浏览器中(除非手动out.flush).


