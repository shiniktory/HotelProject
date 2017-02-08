<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:set var="currentPage" scope="session" value="controller?command=allOrders"/>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="admin.orders"/></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>
<div id="content" >
    <u:message/>
    <u:permission role='ADMIN'/>
    <br>
    <c:if test="${empty requestScope.orders}">
        <fmt:message key="no-orders"/> <br>
    </c:if>
    <c:if test="${not empty requestScope.orders}">
        <h3><fmt:message key="admin.orders"/></h3>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th><fmt:message key="user_id"/></th>
                <th><fmt:message key="room_number"/></th>
                <th><fmt:message key="client.bill"/>, <fmt:message key="bill.uah"/></th>
                <th><fmt:message key="date_creation"/></th>
                <th><fmt:message key="arrival_date"/></th>
                <th><fmt:message key="leaving_date"/></th>
                <th><fmt:message key="state"/></th>
                <th><fmt:message key="admin.change_order_state"/></th>
            </tr>
            </thead>
            <c:forEach items="${requestScope.orders}" var="order">
                <tr>
                    <td><c:out value="${order.id}"/></td>
                    <td><c:out value="${order.userId}"/></td>
                    <td><c:out value="${order.roomNumber}"/></td>
                    <td><c:out value="${order.bill}"/></td>
                    <td><c:out value="${order.dateCreation}"/></td>
                    <td><c:out value="${order.arrivalDate}"/></td>
                    <td><c:out value="${order.leavingDate}"/></td>
                    <td><fmt:message key="${order.state.localized}"/></td>
                    <td>
                        <form action="controller" method="post">
                            <input type="hidden" name="command" value="changeOrderState">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <select name="newOrderState">
                                <c:forEach items="${requestScope.states}" var="state">
                                    <option value="${state}"><fmt:message key="${state.localized}"/></option>
                                </c:forEach>
                            </select>
                            <input type="submit" value="<fmt:message key="settings.change" />">
                        </form>
                    </td>
                </tr>

            </c:forEach>
        </table>
    </c:if>

</div>
<br>
<%@include file="/WEB-INF/pageFragments/footer.jspf" %>
</body>
</html>
