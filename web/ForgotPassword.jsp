<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <c:set var="currentPage" scope="session" value="ForgotPassword.jsp"/>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="fp"/>? </title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>

<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>
<div id="content">
    <a href="Authorization.jsp"><fmt:message key="login"/></a>
    <u:message/>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="forgotPassword">
        <fieldset>
            <legend><fmt:message key="settings.e-mail" />:</legend>
            <input type="email" name="email" maxlength="40">
        </fieldset>
        <br>
        <input type="submit" value="<fmt:message key="fp.remain"/>">
    </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
