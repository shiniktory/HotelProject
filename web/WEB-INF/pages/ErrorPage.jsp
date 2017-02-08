<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:set var="currentPage" scope="session" value="/WEB-INF/pages/ErrorPage.jsp"/>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf"%>
    <title><fmt:message key="error" /></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf"%>

<div id="content">
    <u:message/>
<c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
<c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

<c:if test="${not empty code}">
    <h3>Error code: ${code}</h3>
</c:if>

<c:if test="${not empty message}">
    <h3>${message}</h3>
</c:if>

<c:if test="${not empty exception}">
    <h2><c:out value="${exception}"/></h2> <br>
    <c:forEach items="${exception.stackTrace}" var="stEl">
        <c:out value="${stEl}"/><br>
    </c:forEach>
</c:if>

<c:if test="${not empty requestScope.errorMessage}">
    <h3>${requestScope.errorMessage}</h3>
</c:if>
</div>
<%@ include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
