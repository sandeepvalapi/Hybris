<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="product.out.of.stock.notify" var="notificationBtnText"/>

<c:set var="iconClass">
	glyphicon-envelope
</c:set>

<spring:theme code="text.stock.notification.subscribe.title" var="colorBoxTitle" />

<c:if test="${isWatching}">
	<spring:theme code="product.notification.not.notify" var="notificationBtnText"/>
	<spring:theme code="text.stock.notification.cancel.title" var="colorBoxTitle" />
	<c:set var="iconClass">
		glyphicon-eye-close
	</c:set>
</c:if>

<div class="text-out-of-stock">
	<span> 
		<spring:theme code="product.out.of.stock" />
	</span>
</div>

<c:if test="${isWatching}">
	<div class="notification-subscribed">
		<span>
			<spring:theme code="text.stock.notification.subscribed" />
		</span>
	</div>
</c:if>

<button id="arrival-notification" data-box-title="${colorBoxTitle}" disabled="disabled" 
	class="btn btn-primary btn-block js-add-to-cart btn-icon ${fn:escapeXml(iconClass)} outOfStock" >
	${notificationBtnText}
</button>
