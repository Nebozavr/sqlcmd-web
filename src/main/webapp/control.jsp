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


<form class="control1" action="control" method="POST">
    <input type="hidden" name="table" value="${param.table}"/>
    <div class="divCreate">
        <button type="submit" name="action" value="Clear">Clear</button>
        <button type="submit" name="action" value="Drop">Drop</button>
    </div>
</form>

<div class="controlDiv">
    <form action="control" method="POST">
        <input type="hidden" name="table" value="${param.table}"/>
        <div class="field">
            <label>Column name to update:</label>
            <div class="input"><input type="text" name="columnWhere"/></div>
        </div>

        <div class="field">
            <label>Field value to update:</label>
            <div class="input"><input type="text" name="valueWhere"/></div>
        </div>

        <div class="field">
            <label>Column name to set:</label>
            <div class="input"><input type="text" name="columnSet"/></div>
        </div>

        <div class="field">
            <label>Field value to set:</label>
            <div class="input"><input type="text" name="valueSet"/></div>
        </div>

        <div class="divCreate">
            <button type="submit" name="action" value="Update">Update</button>
        </div>

    </form>

    <form action="control" method="POST">
        <input type="hidden" name="table" value="${param.table}"/>

        <c:forEach items="${tableNames}" var="columns" begin="0" end="0">
            <c:forEach items="${columns}" var="column">
                <div class="field">
                    <label>${column}</label>
                    <div class="input"><input type="text" name="${column}"/></div>
                </div>
            </c:forEach>
        </c:forEach>

        <div class="divCreate">
            <button type="submit" name="action" value="Insert">Insert</button>
        </div>

    </form>

    <form action="control" method="POST">
        <input type="hidden" name="table" value="${param.table}"/>
        <div class="field">
            <label>Column name to delete:</label>
            <div class="input"><input type="text" name="columnDelete"/></div>
        </div>

        <div class="field">
            <label>Field value to delete:</label>
            <div class="input"><input type="text" name="valueDelete"/></div>
        </div>

        <div class="divCreate">
            <button type="submit" name="action" value="Delete">Delete</button>
        </div>

    </form>
</div>

<div class="divCreate">
    <button onclick="location.href='menu'">Menu</button>
</div>
</body>
</html>
