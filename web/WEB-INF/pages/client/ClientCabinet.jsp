<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="client.personal_cabinet"/></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<c:set var="currentPage" scope="session" value="controller?command=clientOrders"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content">
    <u:message/>
    <u:permission role='CLIENT'/>
    <c:if test="${empty sessionScope.user.orders}">
        <fmt:message key="no-orders"/>
    </c:if>
    <c:if test="${not empty sessionScope.user.orders}">
        <h3><fmt:message key="admin.orders"/></h3>
        <table>
            <thead>
            <tr>
                <th><fmt:message key="room_number"/></th>
                <th><fmt:message key="arrival_date"/></th>
                <th><fmt:message key="leaving_date"/></th>
                <th><fmt:message key="client.bill"/></th>
                <th><fmt:message key="state"/></th>
                <th></th>
            </tr>
            </thead>

            <u:orders list="${sessionScope.user.orders}"/>
        </table>
    </c:if>

    <c:if test="${not empty sessionScope.requests}">
        <h3><fmt:message key="requests"/></h3>
        <table>
            <thead>
            <tr>
                <th><fmt:message key="place_count"/></th>
                <th><fmt:message key="apartment_class"/></th>
                <th><fmt:message key="arrival_date"/></th>
                <th><fmt:message key="leaving_date"/></th>
                <th><fmt:message key="operation"/></th>
            </tr>
            </thead>
            <c:forEach items="${sessionScope.requests}" var="req">
                <tr>
                    <td><c:out value="${req.placeCount}"/></td>
                    <td><c:out value="${req.roomClass.name}"/></td>
                    <td><c:out value="${req.arrivalDate}"/></td>
                    <td><c:out value="${req.leavingDate}"/></td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="clientCancelRequest">
                            <input type="hidden" name="requestId" value="${req.id}">
                            <input type="submit" value="<fmt:message key="cancel" />">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</div>
<br>
<%@include file="/WEB-INF/pageFragments/footer.jspf"%>
</body>
</html>
