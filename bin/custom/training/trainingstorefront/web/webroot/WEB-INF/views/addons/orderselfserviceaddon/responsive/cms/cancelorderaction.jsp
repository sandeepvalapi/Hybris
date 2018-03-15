<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url var="cancelOrderUrl" value="${fn:escapeXml(fn:replace(url, '{orderCode}', orderCode))}" scope="page"/>

<c:if test="${orderCancellable}">
    <form:form action="${cancelOrderUrl}" id="cancelorderForm" commandName="cancelorderForm"
               class="cancelorderForm--ButtonWrapper">
            <button type="submit" class="btn btn-default btn-block" id="cancelOrderButton">
                <spring:theme code="text.order.cancelorderbutton"/>
            </button>
    </form:form>
</c:if>
