<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<html>
<head>
  <c:set var="currentPage" scope="session" value="controller?command=guestBook"/>
<%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="gb.title" /></title>
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>
<div id="content">
  <center><h2><fmt:message key="gb.title" /></h2></center>
  <u:message/>
  <c:forEach items="${requestScope.comments}" var="comment">
    <p>
      <fmt:formatDate value="${comment.dateCreated}" pattern="dd-MM-yyyy" var="date"/>
    <c:out value="${date}. ${comment.user.firstName}"/>
    <br>
    <c:out value="${comment.text}"/>
    </p>
    <hr>
  </c:forEach>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
