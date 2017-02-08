<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="bill_page"/></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<u:permission role='CLIENT'/>
<c:set var="currentPage" scope="session" value="controller?command=bill"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content">
    <hr>
    <h3><fmt:message key="client.bill_summ"/>: ${requestScope.order.bill} <fmt:message key="bill.uah" /></h3>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="billPay">
        <input type="hidden" name="orderId" value="${requestScope.order.id}">
        <input type="submit" value="<fmt:message key="client.pay"/>">
    </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
