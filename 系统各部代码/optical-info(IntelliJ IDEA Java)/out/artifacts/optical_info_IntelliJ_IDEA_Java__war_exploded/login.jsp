<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>登录</title>
    <link rel="stylesheet" href="css/login.css">
    <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/login.js"></script>
</head>
<body>
    <div class="container">
        <div class="box">
            <div class="login">
                <a href="login.jsp">
                    <input type="image" src="img/login/loginlogo.png" class="logo"/>
                </a>
                <form action="${pageContext.request.contextPath}/AdminServlet" method="post">
                    <input type="hidden" value="logon" name="method" />
                    <input type="text" value="17781855864" placeholder="账        号" class="input" maxlength="11" name="phone">
                    <input type="password" value="123456" placeholder="密        码" class="input" maxlength="20" name="password">
                    <button type="submit" id="login-button" onclick="this.disabled=true;this.parentElement.submit();">登录</button>
                    <div id="form_widget">
                        <%
                            String msg = (String) session.getAttribute("loginMSG");
                            if (msg != null) {
                                out.print("<div style='color:#f00;'>" + msg + "</div><br />");

                                session.removeAttribute("loginMSG");
                            }
                        %>
                    </div>
                    <div id="copyright">
                        Power By haveAGroup &copy;CopyRight 2017
                    </div>
                </form>
            </div>
            <div class="bubbles">
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
            </div>
        </div>
    </div>
</body>
</html>