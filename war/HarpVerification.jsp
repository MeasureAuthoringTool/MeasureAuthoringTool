<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Measure Authoring Tool</title>
</head>
<body>
    <p>Token Introspection: ${tokenValidation}</p>
    <p>Token: ${token}</p>
    <form action="${harpUrl}/logout" method="get" id="logoutForm">
        <input type="hidden" id="redirectUri" name="post_logout_redirect_uri" value="${loginUrl}">
        <button type="submit" id="logoutButton" name="id_token_hint" value="${token}">Logout</button>
    </form>
</body>
</html>
