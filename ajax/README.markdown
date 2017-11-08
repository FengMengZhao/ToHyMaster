# AJAX异步加载(2017-Q4)

ajax的全称是: Asynchronous JavaScript and XML.

ajax可以不刷新页面的情况下实现异步的与服务器进行交互,更新页面信息.

- 创建ajax对象
- 设置回调函数(完成与服务器的交互后锁触发的函数)
- 打开请求并发送
- 客户端获取反馈数据,更新页面

获取ajax XmlHttpRequest对象:

- IE5,IE6
    - new ActiveXObject("Microsoft.XMLHTTP")
    - new XmlHttpRequest()

XmlHttpRequest对象属性:

- onreadystatechange
    - 每次状态改变所触发事件的事件处理程序(回调函数)
- responseText
    - 从服务器进程返回数据的字符串形式
- responseXML
    - 从服务器进程返回的DOM兼容文档对象模型
- status
    - 从数据库返回的数字代码
        - 200: OK
        - 302: 在其他地址中发现请求数据
        - 400: 错误请求
        - 403: 请求不允许
        - 404: 未找到此页面,查询或者URL
        - 500: 服务器产生内部错误
        - 501: 服务器不支持请求函数
        - 502: 服务器暂时不可用
- statusText
    - 伴随状态码的字符串信息
- readyState
    - 对象状态值
        - 0(未初始化)对象已建立,但是尚未初始化(尚未调用open方法)
        - 1(初始化)对象已建立,尚未调用send方法
        - 2(发送数据)send方法已调用,但是当前状态及http头未知
        - 3(数据传送中)已经接受部分数据,因为响应及http头不全,这时通过responseBody和responseText获取部分数据会出现错误
        - 4(完成)数据接收完毕,此时可以通过responseXml和responseText获取完整的回应数据

**原生ajax使用:**

HTML通过ajax请求Servlet

    package one;

    import java.io.IOException;
    import java.io.PrintWriter;

    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    /**
     * Servlet implementation class AjaxServlet
     */
    @WebServlet("/AjaxServlet")
    public class AjaxServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        /**
         * @see HttpServlet#HttpServlet()
         */
        public AjaxServlet() {
            super();
            // TODO Auto-generated constructor stub
        }

        /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
         *      response)
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // TODO Auto-generated method stub
            //response.getWriter().append("Served at: ").append(request.getContextPath());
            String userinput = request.getParameter("userinput");
            System.out.println("客户端连接！");
            System.out.println("请求信息为：" + userinput);
            PrintWriter out = response.getWriter();
            if(userinput.equals("") || userinput.length()<6) {
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=utf-8");
                out.write("<h3>the length of input string must be more than 6!</h3>");
            }else{
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "text/html;charset=utf-8");
                out.println("<h3>Correct!</h3>");
            }
            out.close();
        }

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
         *      response)
         */
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // TODO Auto-generated method stub
            doGet(request, response);
        }

    }

> servlet file

    <web-app version="3.0"
      xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">

      <!-- To save as <CATALINA_HOME>\webapps\helloservlet\WEB-INF\web.xml -->

       <servlet>
          <servlet-name>AjaxServlet</servlet-name>
          <servlet-class>one.AjaxServlet</servlet-class>
          <load-on-startup>1</load-on-startup>
       </servlet>

       <!-- Note: All <servlet> elements MUST be grouped together and
             placed IN FRONT of the <servlet-mapping> elements -->

       <servlet-mapping>
          <servlet-name>AjaxServlet</servlet-name>
          <url-pattern>/servlet/AjaxServlet</url-pattern>
       </servlet-mapping>
    </web-app>

> web.xml file

    <!DOCTYPE html>
    <html>
    <head>
    <meta charset="UTF-8">
    <title>Ajax测试</title>
    </head>
    <body>
    <div>
        <h2>AJAX Test</h2>
        <input type="text" name="userinput" placeholder="用户输入，Ajax方式获得数据" onblur="getResult(this)">
        <br>
        <span id="ajax_result">hello</span>
        <script>
        getResult = function(str){
            var httpxml;
            if(0 == str.value.length) {
                document.getElementById("ajax_result").innerHTML = "Nothing";
            }
            if (window.XMLHttpRequest) {
                xmlhttp = new XMLHttpRequest();
            }else{
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange = function(message){
                if(4 == xmlhttp.readyState && 200 == xmlhttp.status) {
                    document.getElementById("ajax_result").innerHTML = xmlhttp.responseText;
                }
            }
            xmlhttp.open("GET","/ajax/servlet/AjaxServlet?userinput="+str.value,true);
            xmlhttp.send();

            }
        </script>
    </div>
    </body>
    </html>

> html file

HTML通过ajax请求JSP

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%
            //接收参数
            String userinput = request.getParameter("userinput");
            //控制台输出表单数据看看
            System.out.println("userinput =" + userinput);
            //检查code的合法性
            if (userinput == null || userinput.trim().length() == 0) {
                    out.println("code can't be null or empty");
            } else if (userinput != null && userinput.equals("admin")) {
                    out.println("code can't be admin");
            } else {
                    out.println("OK");
            }
    %>

> JSP文件,HTML ajax请求要变为: `xmlhttp.open("GET","receiveParams.jsp?userinput="+str.value,true);`

jQuery方式ajax请求

方法:

*$.ajax():*

    $("button").click(function(){
        $.ajax({url: "demo_test.txt", success: function(result){
            $("#div1").html(result);
        }});
    });

> perform an async ajax request

*$.get():*

    $("button").click(function(){
        $.get("demo_test.asp", function(data, status){
            alert("Data: " + data + "\nStatus: " + status);
        });
    });

> load data from a server using an ajax http get request

*$.post():*

    $("button").click(function(){
        $.post("demo_test.asp", function(data, status){
            alert("Data: " + data + "\nStatus: " + status);
        });
    });

> load data from a server using an ajax http post request

*$.getJSON():*

    $("button").click(function(){
        $.getJSON("demo_ajax_json.js", function(result){
            $.each(result, function(i, field){
                $("div").append(field + " ");
            });
        });
    });

> Loads JSON-encoded data from a server using a HTTP GET request

---
