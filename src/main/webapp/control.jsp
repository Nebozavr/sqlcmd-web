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


<div class="divCreate">
    <button type="submit" name="action" form="control" value="Clear">Clear</button>
    <button type="submit" name="action" form="control" value="Drop">Drop</button>
</div>

<form action="control" method="POST" id="control">
    <input type="hidden" name="table" value="${param.table}"/>
        <table>
        <tr>

            <tr>
                <td>
                    <label>Column name to update:</label>
                    <div class="input"><input type="text" name="columnWhere"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Field value to update:</label>
                    <div class="input"><input type="text" name="valueWhere"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Column name to set:</label>
                    <div class="input"><input type="text" name="columnSet"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Field value to set:</label>
                    <div class="input"><input type="text" name="valueSet"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="divCreate">
                        <button type="submit" name="action" form="control" value="Update">Update</button>
                    </div>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>

            <c:forEach items="${tableNames}" var="columns" begin="0" end="0">
                <c:forEach items="${columns}" var="column">
                    <tr>
                        <td>
                            <label>${column}</label>
                            <div class="input"><input type="text" name="${column}"/></div>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
            <tr>
                <td>
                    <div class="divCreate">
                        <button type="submit" name="action" value="Insert">Insert</button>
                    </div>
                </td>
            </tr>

            <tr>
                <td>&nbsp;</td>
            </tr>

            <tr>
                <td>
                    <label>Column name to delete:</label>
                    <div class="input"><input type="text" name="columnDelete"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Field value to delete:</label>
                    <div class="input"><input type="text" name="valueDelete"/></div>
                </td>
            </tr>
            <td>
                <div class="divCreate">
                    <button type="submit" name="action" form="control" value="Delete">Delete</button>
                </div>
            </td>
            </tr>

        </tr>
    </table>
</form>


<div class="divCreate">
    <button onclick="location.href='menu'">Menu</button>
</div>
</body>
</html>
