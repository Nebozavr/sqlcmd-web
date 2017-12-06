<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <h3> Table -
            <c:out value="${param.table}"/>
        </h3>
        <table border="1">
            <c:forEach items="${tableNames}" var="row">
                <tr>
                    <c:forEach items="${row}" var="element">
                        <td>
                            ${element}
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>

        <br>

        <a href="control?table=${param.table}">Data table management</a><br>
        <a href="menu">Menu</a><br>
    </body>
</html>
