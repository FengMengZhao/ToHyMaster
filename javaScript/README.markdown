## JavaScript(2017-Q3)

`document.getElementById('ID').innerHTML=''`统一使用单引号.
`document.getElementById("ID").innerHTML=""`或者统一使用双引号.

#### JS环境

在一段程序代码中发起一个调用并等待直到调用返回结果再执行接下来的代码,这个调用对于这段程序来说是同步的;而发起一个调用后不用等待调用结果而直接执行后面的代码,这个调用对于这段程序来说就是异步的.

浏览器内核线程主要包括: GUI渲染线程; 异步http请求线程; JS引擎线程; 定时器线程; 触发事件线程等

JS引擎是ECMAScript标准的实现,用来解释(或编译)Javascript代码;JS的运行环境是包括引擎并提供一些类库让JavaScript代码能够在其上运行(Chrome等).

JavaScript语言的一大特性是单线程,主要是因为JavaScript主要用来和用户交互及进行DOM操作.

JavaScript程序的异步是由其运行环境提供的,通过event loop实现异步编程.

#### JS是如何工作的

1. JavaScript在编译时会生成堆和栈,堆存放的是程序运行过程中产生的对象,栈时JavaScript的执行栈,程序代码会根据调用关系被压入栈中执行;
2. 当遇到WegAPIs(IO或者定时器)时,浏览器会响应调用并直接返回,stack继续执行剩余JavaScript代码;
3. 当WegAPIs调用完成后,会将相应的结果与回调函数依次放入callback queue中;
4. 当执行栈中没有要执行的JavaScript代码时,则会通过event loop检查并取出callback queue中第一个回调函数,并执行.

JavaScript代码会在执行引擎的执行栈中以单线程的方式执行,而所有的IO或定时任务会通过运行环境异步执行,并将执行结果放在callback queue中等待被调用,这就是所谓的单线程异步工作原理.

#### JavaScript三大核心

1. 核心(ECMAScript): 描述了JS的语法和基本对象.
2. 文档对象模型(DOM): 处理网页内容方法和接口.
3. 浏览器对象模型(BOM): 与浏览器交互的方法和接口.

---

## JavaScript中级(2018-Q3_4)

### 作用域

JS的数据类型：

- 基本数据类型
    - `Boolean`
    - `Number`(数值)
    - `String`
    - `undefined`
    - `NULL`
- 引用数据类型
    - 对象
    - 数组
    - 函数

在`for`循环和`if`等语句中没有块级作用域。

    <script type="text/javascript">
    if(true){//if语句的花括号没有作用域的功能。
        var box = "trigkit4";
    }
    alert(box);//弹出 trigkit4
    </script>

### 内存

javascript具有自动垃圾回收机制，一旦数据不再使用，可以将其设为`null`来释放引用。

### 闭包

官方解释：「闭包」，是指拥有多个变量和绑定了这些变量的环境的表达式（通常是一个函数），因而这些变量也是该表达式的一部分。

通俗理解：闭包是个函数，而它「记住了周围发生了什么」。表现为由「一个函数」体中定义了「另个函数」。

**闭包的作用：**

- setTimeout/setInterval
- 回调函数（callback）
- 事件句柄（event handle）

---

**相关链接:**

- [JavaScript之锋利的JQuery](https://fengmengzhao.github.io/2015/10/01/JavaScript-jquery.html)
- [JavaScript和DOM编程艺术](https://fengmengzhao.github.io/2015/09/27/JavaScript-DOM-programming-art.html)
- [BootStrap基础框架](https://fengmengzhao.github.io/2015/12/23/BootStrap-frame-basic.html)
