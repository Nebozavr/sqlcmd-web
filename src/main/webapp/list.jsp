<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='css/style.css' %>
</style>
<head>
    <title>SQLCmd</title>
</head>
<body>

    <div class="div">
        <label>Actual tables:</label>
    </div>

    <div class="div">
        <c:forEach items="${tables}" var="table">
            <button onclick="location.href='find?table=${table}'">${table}</button>
        </c:forEach>
    </div>
    <br>
    <br>
    <div class="div">
        <button onclick="location.href='create'">Create Table</button>
    </div>

    <div class="div">
        <button onclick="location.href='menu'">Menu</button>
    </div>

    </body>
</html>
