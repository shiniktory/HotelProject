<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="http://nure.ua/SummaryTask4/user" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/pageFragments/Locale.jspf" %>
<html>
<head>
    <title><fmt:message key="authorization"/></title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<c:set var="currentPage" scope="session" value="Authorization.jsp"/>
<%@ include file="/WEB-INF/pageFragments/UserMenu.jspf" %>

<c:if test="${not empty sessionScope.user}">
    <c:if test="${sessionScope.user.userRole eq 'ADMIN'}">
        <c:redirect url="controller?command=adminOrders"/>
    </c:if>
    <c:if test="${sessionScope.user.userRole eq 'CLIENT'}">
        <c:redirect url="controller?command=clientOrders"/>
    </c:if>
</c:if>


<div id="start-page">
    <h1><fmt:message key="auth.greetings"/>! </h1>
    <h4><fmt:message key="auth.info_message"/></h4>

    <u:message/>
    <center>
        <form id="login" action="controller" method="post">
            <input type="hidden" name="command" value="login">

            <div>
                <fieldset>
                    <legend><fmt:message key="auth.login"/>:</legend>
                    <input name="login"/>
                </fieldset>
            </div>
            <div>
                <fieldset>
                    <legend><fmt:message key="auth.password"/>:</legend>
                    <input type="password" name="password">
                </fieldset>
            </div>
            <br>
            <input type="submit" value="<fmt:message key="login" />"><br>
            <br>
            <a href="ForgotPassword.jsp"><fmt:message key="fp"/>?</a><br>
            <a href="Registration.jsp"><fmt:message key="registration"/></a>
        </form>

        <div>
        <c:import url="controller?command=rooms" var="rooms" scope="page"/>
        <c:if test="${not empty sessionScope.aps}">
            <h3><fmt:message key="client.rooms_list"/>:</h3>
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
                            <td><c:out value="${ap.roomNumber}"/></td>
                            <td><c:out value="${ap.placeCount}"/></td>
                            <td><c:out value="${ap.price}"/></td>
                            <td><fmt:message key="${ap.state.localized}"/></td>
                        </tr>
                    </c:forEach>
                </c:forEach>
            </table>
        </c:if>
        </div>
    </center>
</div>

<%@include file="/WEB-INF/pageFragments/footer.jspf" %>
</body>
</html>
