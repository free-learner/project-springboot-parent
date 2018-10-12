<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>多语言国际化测试</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

   <!--  <fmt:bundle basename="messages">
           数据库驱动程序名:<fmt:message key="name1"/><br>
           连接字符串:<fmt:message key="name2"/><br>
           用户名:<fmt:message key="name3"/><br>
           名字:<fmt:message key="name4"/><br>
           动态提示信息:<fmt:message key="messageTemp"/><br>
           密码:<fmt:message key="password" var="password"/> <br>
    </fmt:bundle> -->
    
    <!-- 修改.properties文件中某个键的动态值 -->
    <%-- <c:set var="todayTemp" value="<%=new Date() %>"/>
    <fmt:setBundle basename="dbconn"/>
    动态提示信息:
          <fmt:message key="messageTemp">
                 <fmt:param>张三</fmt:param>
                 <fmt:param value="${todayTemp}"></fmt:param>
           </fmt:message> --%>
           

</body>
</html>