<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Connect</title>
    </head>
    <body>

        <p>
            <h2>Hello! <br>
                Before use this program you must connect to database <br>
                Please connect to any database!
            </h2>
        </p>

        <form action="connect" target="_blank" method="POST">
            Database name:<br>
            <input type="text" name="dbName"><br>
            User name:<br>
            <input type="text" name="username"><br>
            Password:<br>
            <input type="password" name="password"><br><br>
            <input type="submit" value="connect">
        </form>

        <p>
            <h2>After connect, you will be redirect to menu.</h2>
        </p>

        <a href="help">HELP</a><br>
    </body>
</html>
