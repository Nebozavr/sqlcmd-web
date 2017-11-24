 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
     <head>
        <title>SQLCmd</title>
    </head>
    <body>
           <c:choose>
               <c:when test="${param.success eq 1}">
                   <h2> Now you connect to database </h2>
                   <br />
               </c:when>
               <c:otherwise>

                   <br />
               </c:otherwise>
           </c:choose>
               <br>

            <c:forEach items="${items}" var="item">
              <a href="${item}">${item}</a><br>
            </c:forEach>
    </body>
</html>
