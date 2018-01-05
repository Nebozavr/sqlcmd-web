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
    <label>
        <c:choose>
            <c:when test="${param.success eq 1}">
                You connected to database
            </c:when>
            <c:when test="${param.success eq 2}">
                You disconnected
            </c:when>
            <c:otherwise>
                Menu:
            </c:otherwise>
        </c:choose>
    </label>
</div>
<br>

<div class="div">
    <c:forEach items="${items}" var="item">
        <button onclick="location.href='${item}'">${item}</button>
    </c:forEach>
</div>
</body>
</html>
