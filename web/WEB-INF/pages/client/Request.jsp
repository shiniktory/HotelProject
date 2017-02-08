<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="client.request" /></title>
  <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<c:set var="currentPage" scope="session" value="controller?command=request"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>

<div id="content">
  <u:message/>
  <u:permission role='CLIENT'/>
  <br>
<form id="request" action="controller" method="post">
  <input type="hidden" name="command" value="makeRequest">
  <fieldset>
    <legend><fmt:message key="place_count" />:</legend>
  <select name="placeCount">
    <option value="1">1</option>
    <option value="2">2</option>
    <option value="3">3</option>
  </select>
    </fieldset>
  <fieldset>
    <legend><fmt:message key="apartment_class" />:</legend>
    <select name="apartmentClass">
      <c:forEach items="${requestScope.classesList}" var="ac">
      <option value="${ac.id}">${ac.name}</option>
      </c:forEach>
    </select>
  </fieldset><br>

  <jsp:useBean id="now" class="java.util.Date"/>
  <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="formatted"/>
  <fieldset>
    <legend><fmt:message key="client.time_to_stay" />:</legend>
    <input type="date" name="dateFrom" value="${formatted}"/>
    <input type="date" name="dateTo" value="${formatted}"/>
  </fieldset>
  <input type="submit" value="<fmt:message key="client.leave_request" />">

</form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
