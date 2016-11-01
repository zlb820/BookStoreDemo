<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>top</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
body {
	background: #15B69A;
	margin: 0px;
	color: #ffffff;
}

a {
	text-transform: none;
	text-decoration: none;
	color: #ffffff;
	font-weight: 900;
}

a:hover {
	text-decoration: underline;
}
</style>
</head>

<body>
	<h1 style="text-align: center;">网上书城系统</h1>
	<div style="font-size: 10pt; line-height: 10px;">
		<!-- 判断sessionuser是否有值，有则显示没有则显示
		el表达式的使用非常严谨，不要再花括号后面加空格之类的，会影响输出
		
		 -->
		<c:choose>
			<c:when test="${empty sessionScope.sessionuser}">
		  <a href="<c:url value='/jsps/user/login.jsp'/>" target="_parent">会员登录</a> |&nbsp; 
		  <a href="<c:url value='/jsps/user/regist.jsp'/>" target="_parent">注册会员</a>
			</c:when>
			<c:otherwise>
		     用户:${sessionScope.sessionuser.loginname} &nbsp;&nbsp;|&nbsp;&nbsp;
		  <a href="<c:url value='/jsps/cart/list.jsp'/>" target="body">我的购物车</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		  <a href="<c:url value='/jsps/order/list.jsp'/>" target="body">我的订单</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		  <a href="<c:url value=''/>" target="body">修改密码</a>&nbsp;&nbsp;|&nbsp;&nbsp;
		  <a href="<c:url value='/userServlet?method=quitLogin'/>" target="_parent">退出</a>
			</c:otherwise>
		</c:choose>


	</div>
</body>
</html>
