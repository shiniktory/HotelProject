<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="success">
    <c:if test="${not empty sessionScope.successMessage}">
        <c:out value="${sessionScope.successMessage}"/>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty requestScope.successMessage}">
        <c:out value="${requestScope.successMessage}"/>
        <c:remove var="successMessage" scope="request"/>
    </c:if>
</div>
<div id="error">
    <c:if test="${not empty sessionScope.errorMessage}">
        <c:out value="${sessionScope.errorMessage}"/>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <c:out value="${requestScope.errorMessage}"/>
        <c:remove var="errorMessage" scope="request"/>
    </c:if>
</div>