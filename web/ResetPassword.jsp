<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <c:set var="currentPage" scope="session" value="ResetPassword.jsp"/>
  <%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="user.reset_password" /></title>
  <link rel="stylesheet" type="text/css" href="/styles.css">
  <link>
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>

<div id="content">
  <u:message/>
  <br>
<form action="controller" method="post">
  <input type="hidden" name="command" value="resetPassword">
  <fieldset>
    <legend><fmt:message key="settings.new_password"/>: </legend>
    <input type="password" name="password1">
  </fieldset><br>
  <fieldset>
    <legend><fmt:message key="registr.repeat_password"/>: </legend>
    <input type="password" name="password2">
  </fieldset><br>
  <input type="submit" value="<fmt:message key="settings.change" />">

</form>
</div>
<br>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
