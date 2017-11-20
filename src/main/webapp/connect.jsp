 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<p>Before using any command you must connect to database</p>

<form action="connect" target="_blank" method="POST">
  Database name:<br>
  <input type="text" name="dbName">
  <br>
  User name:<br>
  <input type="text" name="username">
  <br>
  Password:<br>
    <input type="password" name="password">
    <br><br>
  <input type="submit" value="connect">
</form>

<p>After connect, you will be redirect to menu.</p>

</body>
</html>
