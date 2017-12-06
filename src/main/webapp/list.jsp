<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <c:forEach items="${tables}" var="table">
            <a href="find?table=${table}">${table}</a><br>
        </c:forEach>

        <br>
        <a href="menu">Menu</a><br>
    </body>
</html>
