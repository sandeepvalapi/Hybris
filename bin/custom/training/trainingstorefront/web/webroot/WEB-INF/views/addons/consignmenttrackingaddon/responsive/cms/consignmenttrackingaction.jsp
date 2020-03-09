<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="/consignment/{/orderCode}/{/consignmentCode}/tracking" var="consignmentTrackingUrl" htmlEscape="false">
	<spring:param name="orderCode"  value="${orderCode}"/>
    <spring:param name="consignmentCode"  value="${consignment.code}" />
</spring:url>
<div class="label-order">
	<button class="btn btn-default btn-block consignment-tracking-link" data-url="${fn:escapeXml(consignmentTrackingUrl)}"
		data-colorbox-title="<spring:theme code='text.account.order.consignment.tracking.events.title' />">
		<spring:theme code="text.account.order.consignment.tracking.button" text="Track Package"/>
	</button>
</div>
