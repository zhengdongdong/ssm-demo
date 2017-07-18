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

<h3>RequestMapping路由分配</h3>
<a href="/user/zdd/test">*通配符</a>
<a href="/user/testMappingParams?val1=1&val2">RequestMapping-Params</a>
<a href="/user/testMappingMethod">RequestMapping-Method.POST</a>
<a href="/user/testMappingHeader">RequestMapping-Header</a>
<a href="/user/testMappingConsumes">RequestMapping-Consumes(请求)</a>
<a href="/user/testMappingProduces">RequestMapping-Produces(返回)</a>

<h3>传参</h3>
<a href="/user/showAll">获取所有用户</a>
<a href="/user/show/2">获取指定用户(URI模板)</a>
<a href="/user/add/ddd/123">添加用户(URI模板)</a>
<a href="/user/showByHttpServletRequest?id=1">获取指定用户(HttpServletRequest)</a>
<a href="/user/showByRequestParam?id=2">获取指定用户(RequestParam)</a>
<a href="/user/showByCookie">获取Cookie中的数据(CookieView)</a>
<a href="/user/showByRequestHeader">获取请求头中获取数据(RequestHeader)</a>
<a href="javascript:toRequestBody();">RequestBody&ResponseBody</a>


<script type="text/javascript">
    function toRequestBody() {
        var saveData = {"username": "zdd", "password": "123"};
        $.ajax({
            type: "POST",
            url: "/user/testRequestBody",
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