<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Progetto Tecnologie Informatiche per il Web</title>
</head>
<body>
<h1><%= "Progetto Tecnologie Informatiche per il Web"%>
</h1>
<br/>
<form action="LoginHandler" method="post">
    <label for="username">Username:</label><br>
    <input type="text" id="username" name="username" required><br>
    <label for="passwd">Last name:</label><br>
    <input type="password" id="passwd" name="passwd" required>
    <input type="submit" value="Invia" >
</form>
</body>
</html>