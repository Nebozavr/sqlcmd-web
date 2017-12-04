 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
     <head>
        <title>SQLCmd</title>
    </head>
    <body>
    <h3> Table - <c:out value ="${param.table}" /> </h3>
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

            <form  action="control" method="POST">
              <table>
                <tr>
                    <td> <input type="submit" name="action" value="Clear"/>
                         <input type="submit" name="action" value="Drop"/> </td>
                </tr>
                <tr>
                    <td>  Column name to update: <input type="text" name="columnWhere"> </td>
                    <td>  Field value to update: <input type="text" name="valueWhere"> </td>
                    <td>  Column name to set: <input type="text" name="columnSet"> </td>
                    <td>  Field value to set: <input type="text" name="valueSet"> </td>
                    <td> <input type="submit" name="action" value="Update"/> </td>
                </tr>
                <tr>
                     <td>  Insert <input type="text" name="insertData"> </td>
                    <td> <input type="submit" name="action" value="Insert"/> </td>
                </tr>
                <tr>
                     <td>  Delete: <input type="text" name="deleteData"> </td>
                    <td> <input type="submit" name="action" value="Delete"/> </td>
                </tr>
              </table
             </form>



        <a href="menu">Menu</a><br>
    </body>
</html>
