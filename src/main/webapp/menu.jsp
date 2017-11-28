 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
     <head>
        <title>SQLCmd</title>
    </head>
    <body>
           <c:choose>
               <c:when test="${param.success eq 1}">
                   <h3> You connected to database </h3>
                   <br />
               </c:when>
               <c:when test="${param.success eq 2}">
                  <h3> You disconnected </h3>
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
