<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    <%@include file='../css/style.css' %>
</style>
<head>
    <title>SQLCmd</title>
</head>
<body>

    <table border="1" id="tableData">

                     <tr>

                             <th> User Name </th>
                             <th> Database Name </th>
                             <th> Actions </th>
                     </tr>

        <c:forEach items="${actions}" var="userAction" >
            <tr>

                    <td> ${userAction.userName} </td>
                    <td> ${userAction.dbName} </td>
                    <td> ${userAction.actions} </td>
            </tr>
        </c:forEach>


    </table>

    <br>

    <div class="div">
        <button onclick="location.href='menu'">Menu</button>
    </div>
</body>
</html>
