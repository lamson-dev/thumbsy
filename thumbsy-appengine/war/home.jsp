<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="me.lamson.thumbsy.appengine.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- &lt;html> -->                    ≤

<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Thumsy CloudSMS</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
        <link rel='icon' href='favicon.png' />

        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/thumbsy.css">
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user != null) {
                pageContext.setAttribute("user", user);
        %>
        <p>
            Hello, ${fn:escapeXml(user.nickname)}! (You can <a
                href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
                out</a>.)
        </p>
        <%
            } else {
        %>
        <p>
            Hello! <a
                href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
                in</a> to include your name with greetings you post.
        </p>
        <%
            }
            String status = (String) request.getAttribute("status");
            if (status != null) {
                out.print(status);
            }
            int total = Datastore.getTotalDevices();
            if (total == 0) {
                out.print("<h2>No devices registered!</h2>");
            } else {
                out.print("<h2>" + total + " device(s) registered!</h2>");
                out.print("<form name='form' method='POST' action='sendAll'>");
                out.print("<input type='submit' value='Send Message' />");
                out.print("</form>");
            }
            response.setStatus(HttpServletResponse.SC_OK);
        %>

        <!--[if lt IE 7]>
                <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
            <![endif]-->

        <!-- Add your site or application content here -->
        <div id="page">
            <div class="container">
                <div class="wrapper">

                    <section id="main">

                        <div id="message-box">

                            <p class="message receiver">Hello</p>

                            <p class="message sender">Yo</p>

                        </div>

                        <form action="sendAll"
                            method="POST">
                            <div id="user-input">
                                <textarea name="new-message" rows="3" cols="40"></textarea>
                                <input type="submit" name="send-message" value="Send"
                                    style="margin: 5px; height: 60px; width: 100px;" />
                            </div>
                    </section>

                </div>
            </div>
        </div>


        <script
            src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script>
            window.jQuery
                    || document
                            .write('<script src="js/vendor/jquery-1.9.1.min.js"><\/script>')
        </script>

        <script src="js/plugins.js"></script>
        <script src="js/main.js"></script>

        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
        <script>
            var _gaq = [ [ '_setAccount', 'UA-XXXXX-X' ], [ '_trackPageview' ] ];
            (function(d, t) {
                var g = d.createElement(t), s = d.getElementsByTagName(t)[0];
                g.src = ('https:' == location.protocol ? '//ssl' : '//www')
                        + '.google-analytics.com/ga.js';
                s.parentNode.insertBefore(g, s)
            }(document, 'script'));
        </script>
    </body>
</html>
