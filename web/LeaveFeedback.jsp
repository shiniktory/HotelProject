<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <c:set var="currentPage" scope="session" value="LeaveFeedback.jsp"/>
  <%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="feedback.leave" /></title>
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>

<div id="content">
  <u:message/>
  <u:permission role='CLIENT'/>
  <br>
  <form action="controller" method="post">
    <input type="hidden" name="command" value="leaveFeedback">
    <fmt:message key="feedback.text"/>:<br>
    <textarea cols="25" rows="10" name="feedbackText" maxlength="200"></textarea>
    <br>
    <img src="captcha.jsp"><br>
    <fmt:message key="captcha.title"/>: <br>
    <input name="seccode" size="20">
    <br>
    <br>
    <input type="submit" value="<fmt:message key="feedback.leave" />">
  </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
