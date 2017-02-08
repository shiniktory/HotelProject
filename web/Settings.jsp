<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="settings"/></title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<c:set var="currentPage" scope="session" value="Settings.jsp"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content">
    <u:message/>
    <u:permission role="COMMON"/>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="settings">

        <div>
            <fieldset>
                <legend>
                    <fmt:message key="settings.first_name"/>:
                </legend>
                <input name="firstName" maxlength="30" value="${sessionScope.user.firstName}">
            </fieldset><br>
            <fieldset>
                <legend>
                    <fmt:message key="settings.last_name"/>:
                </legend>
                <input name="lastName" maxlength="30"  value="${sessionScope.user.lastName}"> <br>
            </fieldset><br>
            <fieldset>
                <legend><fmt:message key="settings.password_change"/></legend>
                <fmt:message key="settings.old_password"/>: <br>
                <input type="password" name="oldPassword" maxlength="30"> <br>
                <fmt:message key="settings.new_password"/>: <br>
                <input type="password" name="password1" maxlength="30"> <br>
                <fmt:message key="settings.repeat_password"/>: <br>
                <input type="password" name="password2" maxlength="30">
            </fieldset><br>
            <fieldset>
                <legend>
                    <fmt:message key="settings.e-mail"/>:
                </legend>
                <input type="email" name="email"  value="${sessionScope.user.email}"></fieldset><br>
            <input type="submit" value="<fmt:message key="settings.update"/>"/>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
