<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:set var="currentPage" scope="session" value="controller?command=adminOrders"/>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="admin.orders_and_requests"/></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content">
    <u:message/>
    <u:permission role='ADMIN'/>
    <c:if test="${empty requestScope.orders}">
        <fmt:message key="no-orders"/> <br>
    </c:if>
    <c:if test="${not empty requestScope.orders}">
        <h3><fmt:message key="admin.orders"/></h3>
        <table>
            <thead>
            <tr>
                <th><fmt:message key="room_number"/></th>
                <th><fmt:message key="date_creation"/></th>
                <th><fmt:message key="arrival_date"/></th>
                <th><fmt:message key="leaving_date"/></th>
                <th><fmt:message key="user" /></th>
                <th><fmt:message key="state"/></th>
                <th><fmt:message key="admin.change_order_state"/></th>
            </tr>
            </thead>
            <u:orders list="${requestScope.orders}"/>
        </table>
    </c:if>

    <c:if test="${empty requestScope.requestsAndNumbers}">
        <fmt:message key="no-requests"/> <br>
    </c:if>
    <c:if test="${not empty requestScope.requestsAndNumbers}">
        <h3><fmt:message key="requests"/></h3>

        <form action="controller" method="post">
            <input type="hidden" name="command" value="orderFromRequest">
            <table>
                <thead>
                <tr>
                    <th><fmt:message key="place_count"/></th>
                    <th><fmt:message key="apartment_class"/></th>
                    <th><fmt:message key="arrival_date"/></th>
                    <th><fmt:message key="leaving_date"/></th>
                    <th><fmt:message key="room_number"/></th>
                    <th><fmt:message key="user" /></th>
                    <th><fmt:message key="operation"/></th>
                </tr>
                </thead>
                <c:forEach items="${requestScope.requestsAndNumbers}" var="req">
                    <tr>
                        <td><c:out value="${req.key.placeCount}"/></td>
                        <td><c:out value="${req.key.roomClass.name}"/></td>
                        <td><c:out value="${req.key.arrivalDate}"/></td>
                        <td><c:out value="${req.key.leavingDate}"/></td>
                        <td><select name="roomNumber">
                            <c:forEach items="${req.value}" var="room">
                                <option value="${room.roomNumber}">${room.roomNumber}</option>
                            </c:forEach>
                        </select></td>
                        <c:set var="reqUser" scope="page" value="${requestScope.requestUsers[req.key]}"/>
                        <td><c:out value="${reqUser.firstName} ${reqUser.lastName}"/></td>
                        <td>
                            <select name="operationName">
                                <option value="create"><fmt:message key="admin.create_order"/></option>
                                <option value="cancel"><fmt:message key="admin.cancel_request"/></option>
                            </select>
                            <input type="hidden" name="requestId" value="${req.key.id}">
                            <input type="submit" value="<fmt:message key="confirm" />">
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </form>
    </c:if>
</div>
<br>
<%@include file="/WEB-INF/pageFragments/footer.jspf" %>
</body>
</html>
