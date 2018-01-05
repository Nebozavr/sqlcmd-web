<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='css/style.css' %>
</style>
<head>
    <title>Sqlcmd</title>
</head>
<body>

<div class="div">
    <label>After disconnect, you will be redirect to connect page.</label>
</div>

<form action="disconnect" method="POST">
    <div class="div">
        <button type="submit">disconnect</button>
    </div>
</form>

<div class="div">
        <button onclick="location.href='menu'">Menu</button>
</div>


</body>
</html>
