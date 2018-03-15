<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url var="returnOrderUrl" value="${fn:escapeXml(fn:replace(url, '{orderCode}', orderCode))}" scope="page"/>

<c:if test="${orderReturnable}">
    <form:form action="${returnOrderUrl}" id="returnorderForm" commandName="returnorderForm"
               class="returnorderForm--ButtonWrapper">
            <button type="submit" class="btn btn-default btn-block" id="returnOrderButton">
                <spring:theme code="text.order.returnorderbutton"/>
            </button>
    </form:form>
</c:if>
