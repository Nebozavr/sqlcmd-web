<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='css/style.css' %>
</style>
<head>
    <title>Create Table</title>
</head>
<body>

<div class="div">
    <label>After create, you will be redirect to menu.</label>
</div>

<form action="create" method="POST" id="create">
    <input type="hidden" id="countRows" name="countRows" value="1">

    <br>
    <table id="tableRows">
        <tr>

            <td>
                <label>Table name</label>
                <div class="input"><input type="text" name="tableName"/></div>
            </td>
        </tr>
        <tr>
            <td>
                <label>Column name</label>
                <div class="input"><input type="text" name="columnName1"></div>
            </td>
            <td>
                <label>Type column</label>
                <div class="input"><input type="text" name="typeColumn1"></div>
            </td>
        </tr>

    </table>

</form>
<div class="divCreate">
    <button onclick="myFunction()">Add row</button>
    <button type="submit" form="create">Create Table</button>
</div>


<script>

    var i = 2;


    function myFunction() {

        document.getElementById("countRows").value = String(i);

        var x = document.createElement("tr");

        var y1 = document.createElement("td");
        var l1 = document.createElement("label");
        var u1 = document.createTextNode("Column name");
        l1.appendChild(u1);

        var d1 = document.createElement("div");
        d1.setAttribute("class", "input");
        var u2 = document.createElement("input");
        u2.setAttribute("type", "text");
        u2.setAttribute("name", "columnName" + i);
        d1.appendChild(u2);

        y1.appendChild(l1);
        y1.appendChild(d1);


        var y2 = document.createElement("td");
        var l2 = document.createElement("label");
        var u3 = document.createTextNode("Type column:");
        l2.appendChild(u3);

        var d2 = document.createElement("div");
        d2.setAttribute("class", "input");
        var u4 = document.createElement("input");
        u4.setAttribute("type", "text");
        u4.setAttribute("name", "typeColumn" + i);
        d2.appendChild(u4);

        y2.appendChild(l2);
        y2.appendChild(d2);

        x.appendChild(y1);
        x.appendChild(y2);


        document.getElementById("tableRows").appendChild(x);
        i++;
    }
</script>

</body>
</html>
