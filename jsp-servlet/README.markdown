# JSP & Servlet

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
- pageContext: javax.servlet.jsp.PageContext
    - page scope
- page: java.lang.Object
    - page scope
- exception: java.lang.Trowable
    - only avaiable on error pages

> Jsp has 9 implicit objects that we can directly use in jsp. Seven of them are declared as local variable at the start of _jspService() method whereas request and response are part of _jspServie() methond arguement.

### JSP tag

- <%! code %> scriptlet of declaration tag: that are inserted into the body of the servlet class, outside of any existing methods.
- <%= Expression %> scriptlet of expression tag: that are evaluated and inserted into the output.
- <% code %> scriptlet of: thar are inserted into the servlet's service(_jspService) method.

### JSP directive

- <%@ page %>
- <%@ include file="" %>
- <%@ taglib uri="" prefix="" %>
