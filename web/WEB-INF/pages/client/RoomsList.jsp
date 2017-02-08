<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
    <title><fmt:message key="client.rooms_list"/></title>
    <link rel="stylesheet" type="text/css" href="/styles.css">
</head>
<body>
<c:set var="currentPage" scope="session" value="controller?command=rooms"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>

<div id="content">
    <a href="controller?command=request"><fmt:message key="client.leave_request"/></a>
    <br>

    <p>
        <fmt:message key="client.info_about_ordering"/>
    </p>
    <br>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="sort">
        <select name="sortType">
            <option></option>
            <option value="price" ${sessionScope.sort == "price" ? 'selected' : ''}>
                <fmt:message key="sort.price"/></option>
            <option value="placeCount" ${sessionScope.sort == "placeCount" ? 'selected' : ''}>
                <fmt:message key="sort.place_count"/></option>
            <option value="status" ${sessionScope.sort == "status" ? 'selected' : ''}>
                <fmt:message key="sort.status"/></option>
            <c:remove var="sort" scope="session"/>
        </select>
        <input type="submit" value="<fmt:message key="sort"/>">
    </form>

    <u:message/>
    <u:permission role='CLIENT'/>
    <br>

    <form action="controller" method="post">
        <input type="hidden" name="command" value="order">
        <table>
            <thead>
            <tr>
                <th><fmt:message key="room_number"/></th>
                <th><fmt:message key="place_count"/></th>
                <th><fmt:message key="price"/>, <fmt:message key="bill.uah"/></th>
                <th><fmt:message key="status"/></th>
            </tr>
            </thead>
            <c:forEach items="${sessionScope.aps}" var="apEntry">
                <tr>
                    <td colspan="4"><b>${apEntry.key.name}</b></td>
                </tr>
                <c:forEach items="${apEntry.value}" var="ap">
                    <tr>
                        <td><input type="checkbox" name="roomNumber" value="${ap.roomNumber}">
                            <c:out value="${ap.roomNumber}"/></td>
                        <td><c:out value="${ap.placeCount}"/></td>
                        <td><c:out value="${ap.price}"/></td>
                        <td><fmt:message key="${ap.state.localized}"/></td>
                    </tr>
                </c:forEach>
            </c:forEach>
        </table>
        <jsp:useBean id="now" class="java.util.Date"/>
        <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="formatted"/>
        <fieldset>
            <legend><fmt:message key="order.reserv_dates"/>:</legend>
            <input type="date" name="dateFrom" value="${formatted}"/>
            <input type="date" name="dateTo" value="${formatted}"/>
            <input type="submit" value="<fmt:message key="client.reserve"/>">
        </fieldset>
    </form>
</div>
<%@include file="/WEB-INF/pageFragments/footer.jspf" %>
</body>
</html>
