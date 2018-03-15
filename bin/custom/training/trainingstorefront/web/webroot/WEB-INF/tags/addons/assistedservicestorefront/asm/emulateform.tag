<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String" %>
<%@ attribute name="actionNameKeyEnding" required="true" type="java.lang.String" %>
<%@ attribute name="action" required="true" type="java.lang.String" %>
<%@ attribute name="disabledButton" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="usernamePlaceholder"><spring:theme code="asm.emulate.username.placeholder"/></c:set>
<c:set var="cartPlaceholder">
	<c:choose>
	    <c:when test="${not emulateByOrder}">
			<spring:theme code="asm.emulate.cart.placeholder"/>
		</c:when>
		<c:otherwise>
			<spring:theme code="asm.emulate.cart-order.placeholder"/>	
		</c:otherwise>
	</c:choose>
</c:set>
	
<form action="${action}" method="post" id="_asmPersonifyForm" class="asmForm">
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="ASM_input_holder customerId">
			<input name="customerId" type="hidden" value="${fn:escapeXml(customerId)}" placeholder="${usernamePlaceholder}" class="ASM-input"/>
			<div class="input-group">
				<span class="input-group-addon ASM_icon-user" id="customerName"></span>
				<input name="customerName" type="text" value="${customerName}" placeholder="${usernamePlaceholder}" class="ASM-input form-control" aria-describedby="customerName"/>
			</div>
		</div>
	</div>
	<div class="col-md-3 col-sm-4 col-xs-12">
		<div class="ASM_input_holder cartId">
			<div class="input-group">
				<span class="input-group-addon ASM_icon-cart" id="cartId"></span>
				<c:choose>
					<c:when test="${not emulateByOrder}">
						<input name="cartId" type="text" value="${fn:escapeXml(cartId)}" placeholder="${cartPlaceholder}" class="ASM-input form-control" aria-describedby="cartId"/>
					</c:when>
					<c:otherwise>
						<input name="cartId" type="text" value="${fn:escapeXml(cartId)}" placeholder="${cartPlaceholder}" class="ASM-input form-control" aria-describedby="cartId"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<div class=" col-md-3 col-sm-2 col-xs-12">
		<button type="submit" class="ASM-btn ASM-btn-start-session" disabled><spring:theme code="${actionNameKey}"/>
			<span class="hidden-sm"><spring:theme code="${actionNameKeyEnding}"/></span>
		</button>
	</div>
</form>