<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] />
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"] />
<html>
<head>
    <title>多语言国际化测试</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

    <fmt:bundle basename="messages">
           数据库驱动程序名:<fmt:message key="name1"/><br>
           连接字符串:<fmt:message key="name2"/><br>
           用户名:<fmt:message key="name3"/><br>
           名字:<fmt:message key="name4"/><br>
           动态提示信息:<fmt:message key="messageTemp"/><br>
           密码:<fmt:message key="password" var="password"/> <br>
    </fmt:bundle>

</body>
</html>