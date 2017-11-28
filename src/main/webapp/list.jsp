 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
     <head>
        <title>SQLCmd</title>
    </head>
    <body>
             <c:forEach items="${tables}" var="table">
                          <a href="${table}">${table}</a><br>
                        </c:forEach>
    </body>
</html>
