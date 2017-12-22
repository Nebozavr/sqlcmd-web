<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Create Table</title>
    </head>
    <body>

        <form action="create" method="POST">
            <input type="hidden" id="countRows" name="countRows" value="1">
            <input type="submit" value="Create Table"> <br>
            <table id="tableRows">
             <tr>
                    <td>Table name:</td>
                    <td><input type="text" name="tableName"></td>
             </tr>
                 <tr>
                    <td>Column name </td>
                    <td><input type="text" name="columnName1" </td>
                    <td>Type column </td>
                    <td><input type="text" name="typeColumn1" </td>
                 </tr>

             </table>

        </form>

       <button onclick="myFunction()">Add row</button>

       <script>

        var i = 2;


             function myFunction(e) {

             document.getElementById("countRows").value = String (i);

             var x = document.createElement("tr");
             var y1 = document.createElement("td");
             var u1 = document.createTextNode("Column name:");
             y1.appendChild(u1);

             var y2 = document.createElement("td");
             var u2 = document.createElement("input");
             u2.setAttribute("type", "text");
             u2.setAttribute("name", "columnName" + i);
             y2.appendChild(u2);

             var y3 = document.createElement("td");
             var u3 = document.createTextNode("Type column:");
             y3.appendChild(u3);

             var y4 = document.createElement("td");
             var u4 = document.createElement("input");
             u4.setAttribute("type", "text");
             u4.setAttribute("name", "typeColumn" + i);
             y4.appendChild(u4);

             x.appendChild(y1);
             x.appendChild(y2);
             x.appendChild(y3);
             x.appendChild(y4);

             document.getElementById("tableRows").appendChild(x);
             i++;
       }
       </script>


        <p>
            <h2>After connect, you will be redirect to menu.</h2>
        </p>

        <a href="help">HELP</a><br>
    </body>
</html>
