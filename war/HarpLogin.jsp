<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Login</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">

    <!-- Google Analytics -->
    <script>
      (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
          (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
        a = s.createElement(o),
          m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
      })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

      ga('create', 'UA-31005711-1', 'auto');
      ga('send', 'pageview');
    </script>
    <!-- End Google Analytics -->


    <link href="https://global.oktacdn.com/okta-signin-widget/3.8.2/css/okta-sign-in.min.css" type="text/css" rel="stylesheet"/>
    <style>
        noscript {
            background-color: white;
            border: 2px solid red;
            color: red;
            font-family: sans-serif;
            left: 50%;
            margin-left: -11em;
            margin-top: 5ex;
            padding: 12px;
            position: absolute;
            width: 22em;
        }
    </style>
</head>
​
<body data-harpUrl="${harpBaseUrl}" data-clientId="${clientId}">
<noscript>
    Your web browser must have JavaScript enabled
    in order for this application to display correctly.
</noscript>

<div id="okta-login-container"></div>

<form action="" method="post" id="loginForm">
    <input type="hidden" id="loginPost" name="loginPost" value="">
</form>

<script src="https://global.oktacdn.com/okta-signin-widget/3.8.2/js/okta-sign-in.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"></script>
<script src="HarpLogin.js"></script>
</body>
</html>