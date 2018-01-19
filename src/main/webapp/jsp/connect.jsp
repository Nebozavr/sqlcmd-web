<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='../css/style.css' %>
</style>
<head>
    <title>Connect</title>
    <meta charset="UTF-8"/>
</head>
<body>

<div class="div">
    <label>Hello! <br>
        Before use this program you must connect to database <br>
        Please connect to any database!
    </label>
</div>

<form:form id="loginForm" action="connect" method="POST" modelAttribute="connection">
    <div class="field">
        <label>Database name:</label>
        <div class="input"><form:input path="databaseName" id="database"/></div>
    </div>

    <div class="field">
        <label>User name:</label>
        <div class="input"><form:input path="userName" id="login"/></div>
    </div>

    <div class="field">
        <label> Password:</label>
        <div class="input"><form:password path="password" id="pass"/></div>
    </div>

    <div class="submit">
        <button type="submit">Connect</button>
    </div>

</form:form>

<div class="div">
    <label>After connect, you will be redirect to menu.</label>
</div>

</body>
</html>
