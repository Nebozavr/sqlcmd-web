<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='css/style.css' %>
</style>
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

        <br>

        <form action="control" method="POST">
            <input type="hidden" name="table" value="${param.table}"/>
            <table>
                <tr>
                    <td><input type="submit" name="action" value="Clear"/>
                        <input type="submit" name="action" value="Drop"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> Column name to update: <input type="text" name="columnWhere"></td>
                </tr>
                <tr>
                    <td> Field value to update: <input type="text" name="valueWhere"></td>
                </tr>
                <tr>
                    <td> Column name to set: <input type="text" name="columnSet"></td>
                </tr>
                <tr>
                    <td> Field value to set: <input type="text" name="valueSet"></td>
                </tr>
                <tr>
                    <td><input type="submit" name="action" value="Update"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>

                <c:forEach items="${tableNames}" var="columns" begin="0" end="0">
                    <c:forEach items="${columns}" var="column">
                        <tr>
                            <td> ${column} <input type="text" name="${column}"></td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                <tr>
                    <td><input type="submit" name="action" value="Insert"/></td>
                </tr>

                <tr>
                    <td>&nbsp;</td>
                </tr>

                <tr>
                    <td> Column name to delete: <input type="text" name="columnDelete"></td>
                </tr>
                <tr>
                    <td> Field value to delete: <input type="text" name="valueDelete"></td>
                </tr>
                <td><input type="submit" name="action" value="Delete"/></td>
                </tr>
            </table>
        </form>


        <br>
        <a href="menu">Menu</a><br>
    </body>
</html>
