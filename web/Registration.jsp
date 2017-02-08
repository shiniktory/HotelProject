<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:set var="currentPage" scope="session" value="Registration.jsp"/>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="registration"/></title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content">
    <a href="Authorization.jsp"><fmt:message key="login"/></a>
    <u:message/>

    <form id="registration" action="controller" method="post">
        <input type="hidden" name="command" value="registration">

        <fieldset>
            <legend><fmt:message key="auth.login"/>:</legend>
            <input name="login" maxlength="25" value="${sessionScope.userTMP.login}"/>
        </fieldset>
        <br>
        <fieldset>
            <legend><fmt:message key="auth.password"/>:</legend>
            <input type="password" name="password1" maxlength="30"/>
            <legend><fmt:message key="registr.repeat_password"/>:</legend>
            <input type="password" name="password2" maxlength="30"/>
        </fieldset>
        <br>
        <fieldset>
            <legend><fmt:message key="settings.e-mail"/>:</legend>
            <input type="email" name="email" maxlength="40" value="${sessionScope.userTMP.email}"/>
        </fieldset>
        <br>
        <fieldset>
            <legend><fmt:message key="settings.first_name"/>:</legend>
            <input name="firstName" maxlength="30" value="${sessionScope.userTMP.firstName}"/>
        </fieldset>
        <br>
        <fieldset>
            <legend><fmt:message key="settings.last_name"/>:</legend>
            <input name="lastName" maxlength="30" value="${sessionScope.userTMP.lastName}"/>
        </fieldset>

        <br>
        <fieldset>
            <img src="captcha.jsp"><br>
            <fmt:message key="captcha.title"/>: <br>
            <input name="seccode" size="20">
        </fieldset>
        <br>
        <c:if test="${not empty sessionScope.userTMP}">
            <c:remove var="userTMP" scope="session"/>
        </c:if>
        <input type="submit" value="<fmt:message key="register" />">
    </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
