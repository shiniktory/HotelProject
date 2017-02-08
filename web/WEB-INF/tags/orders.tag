<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
<%@ attribute name="list" required="true" type="java.util.List" %>

<c:forEach items="${list}" var="order">
    <tr>
        <td><c:out value="${order.roomNumber}"/></td>

        <c:if test="${sessionScope.user.userRole eq 'ADMIN'}">
            <td><c:out value="${order.dateCreation}"/></td>
        </c:if>

        <td><c:out value="${order.arrivalDate}"/></td>
        <td><c:out value="${order.leavingDate}"/></td>

        <c:if test="${sessionScope.user.userRole eq 'CLIENT'}">
            <td><c:out value="${order.bill}"/></td>
            <td><fmt:message key="${order.state.localized}"/>
            <td><c:if test="${order.state eq 'NEW'}">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="confirmOrder">
                    <input type="hidden" name="roomNumber" value="${order.roomNumber}">
                    <select name="operationName">
                        <option value="confirm"><fmt:message key="confirm"/></option>
                        <option value="cancel"><fmt:message key="cancel"/></option>
                    </select>
                    <input type="submit" value="<fmt:message key="confirm" />">
                </form>
            </c:if>
                <c:if test="${order.state eq 'CONFIRMED'}">
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="confirmOrder">
                        <input type="hidden" name="roomNumber" value="${order.roomNumber}">
                        <input type="submit" value="<fmt:message key="client.pay" />">
                    </form>
                </c:if>
            </td>
        </c:if>

        <c:if test="${sessionScope.user.userRole eq 'ADMIN'}">
            <c:set var="index" scope="page" value="${list.indexOf(order)}"/>
            <td><c:out value="${requestScope.orderUsers[index].firstName} ${requestScope.orderUsers[index].lastName}"/></td>
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
        </c:if>
    </tr>
</c:forEach>
