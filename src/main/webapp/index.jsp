<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style type="text/css">
        a {
            display: block;
        }
    </style>
    <script src="/js/jquery-1.11.3.min.js" type="text/javascript"></script>
</head>
<body>
<h1>Hello World!</h1>

<a href="/user/showAll">获取所有用户</a>

<h3>RequestMapping路由分配</h3>
<a href="/user/zdd/test">*通配符</a>
<a href="/user/testMappingParams?val1=1&val2">RequestMapping-Params</a>
<a href="/user/testMappingMethod">RequestMapping-Method.POST</a>
<a href="/user/testMappingHeader">RequestMapping-Header</a>
<a href="/user/testMappingConsumes">RequestMapping-Consumes(请求)</a>
<a href="/user/testMappingProduces">RequestMapping-Produces(返回)</a>

<h3>RequestMapping可用参数</h3>
<a href="/user/testParams/1?aa=bb">HttpServletRequest/HttpServletResponse/HttpSession</a>
<a href="/user/testParams/2?aa=cc">WebRequest</a>
<form action="/user/testParams/3" method="post" enctype="multipart/form-data">
    <input type="file" name="file"/>
    <button type="submit">MultipartFile</button>
</form>

<h3>RequestMapping返回值类型</h3>
<a href="/user/return/modelAndView">ModelAndView</a>
<a href="/user/return/viewName">ViewName</a>
<a href="/user/return/void">Void</a>
<a href="/user/model">Model</a>
<a href="javascript:toRequestBody('/user/return/json')">ResponseBody(Json)</a>


<h3>传入/传出Json对象</h3>
<a href="javascript:toRequestBody('/user/testRequestBody');">RequestBody&ResponseBody</a>

<h3>传参</h3>
<a href="/user/show/2?aa=bb">获取指定用户(URI模板)</a>
<a href="/user/add/ddd/123">添加用户(URI模板)</a>
<a href="/user/testRegExp/value.123">正则表达式(URI模板)</a>
<a href="/user/showByHttpServletRequest?id=1">获取指定用户(HttpServletRequest)</a>
<a href="/user/showByRequestParam?id=2">获取指定用户(RequestParam)</a>
<a href="/user/showByCookie">获取Cookie中的数据(CookieView)</a>
<a href="/user/showByRequestHeader">获取请求头中获取数据(RequestHeader)</a>

<h3>重定向传值</h3>
<a href="/user/redirect/str">重定向传入String</a>
<a href="/user/redirect/model">重定向传入Model</a>

<h3>模型/Controller共享数据</h3>
<a href="/user/share/modelAttribute">模型间共享数据(ModelAttribute)</a>

<script type="text/javascript">
    function toRequestBody(url) {
        var saveData = {"username": "zdd", "password": "123"};
        $.ajax({
            type: "POST",
            url: url,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(saveData), // 转为字符串
            success: function (data) {
                console.log(data);
            }
        });
    }
</script>

</body>
</html>