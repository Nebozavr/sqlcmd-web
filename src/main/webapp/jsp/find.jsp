<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='../css/style.css' %>
</style>
<head>
    <title>SQLCmd</title>
</head>
<body>
    <div class="div">
        <label>Table - <c:out value="${param.table}"/></label>
    </div>


    <table border="1" id="tableData">
        <c:forEach items="${tableNames}" var="columns" begin="0" end="0">
            <tr>
                <c:forEach items="${columns}" var="column">
                    <th> ${column} </th>
                </c:forEach>
            </tr>
        </c:forEach>

        <c:forEach items="${tableNames}" var="row" begin="1">
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

    <div class="div">
        <button onclick="location.href='control?table=${param.table}'">Data table management</button>
    </div>

    <div class="div">
        <button onclick="location.href='menu'">Menu</button>
    </div>
</body>
</html>
