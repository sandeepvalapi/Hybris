<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%> 

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="/consignment/${ycommerce:encodeUrl(orderCode)}/${ycommerce:encodeUrl(consignment.code)}/tracking" var="consignmentTrackingUrl" htmlEscape="false"/>
<div class="label-order">
	<button class="btn btn-default btn-block consignment-tracking-link" data-url="${consignmentTrackingUrl}"
		data-colorbox-title="<spring:theme code='text.account.order.consignment.tracking.events.title' />">
		<spring:theme code="text.account.order.consignment.tracking.button" text="Track Package"/>
	</button>
</div>
