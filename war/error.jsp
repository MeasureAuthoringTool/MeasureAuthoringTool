<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Error Page</title>
    <link href="css/OpenSans.css" type="text/css" rel="stylesheet"/>
    <style>
        .center {
            margin: auto;
            width: 50%;
            border: 3px solid black;
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="center">
    <div style="color: red"><%=response.getStatus() %>: Internal Server Error</div>
    <div>Please contact your system administrator.</div>
    <div>Click here to return to the <a href="Mat.html">Measure Authoring Tool</a></div>
</div>
</body>
</html>