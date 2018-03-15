<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="assistedserviceutils" uri="http://hybris.com/tld/assistedserviceutils" %>

<c:url value="/assisted-service/bind-cart" var="bindActionUrl" />
<form action="${bindActionUrl}" method="post" class="asmForm"
	id="_asmBindForm">
	<div class="ASM_input_holder customerId col-xs-12 col-sm-4 col-md-3">
		<div class="input-group">
			<span class="input-group-addon ASM_icon-user" id="customerName"></span>
			<c:choose>
				<c:when test="${emulatedUser.uid ne 'anonymous'}">
					<input name="customerName" type="text" value="${fn:escapeXml(emulatedUser.name)}"
					class="ASM-input" readonly
					data-hover='{"name":"${fn:escapeXml(emulatedUser.name)}","email":"${fn:escapeXml(emulatedUser.uid)}","card":"${assistedserviceutils:shortCardNumber(emulatedUser)}","date":"${assistedserviceutils:creationDate(emulatedUser)}"}' />
					<input name="customerId" type="hidden" value="${fn:escapeXml(emulatedUser.uid)}" />
				</c:when>
				<c:otherwise>
					<c:set var="usernamePlaceholder">
						<spring:theme code="asm.emulate.username.placeholder" />
					</c:set>
					<input name="customerId" type="hidden" value="${customerId}"
					placeholder="${usernamePlaceholder}" class="ASM-input" />
					<input name="customerName" type="text" value="${customerName}"
					placeholder="${usernamePlaceholder}" class="ASM-input" />
				</c:otherwise>
			</c:choose>
		</div>
		<span class="ASM_icon ASM_icon-chain invisible"></span>
	</div>
	<div class="ASM_input_holder cartId col-xs-12 col-sm-4 col-md-3">
		<div class="input-group">
			<span class="input-group-addon ASM_icon-cart" id="cartId"></span>
			<input type="text" value="${cart.code}" orig_value="${cart.code}" class="ASM-input" name="cartId" />
		</div>
	</div>

	<div class="assign-cart-to-member-wrapper col-xs-12 col-sm-2 col-md-3">
		<c:choose>
			<c:when test="${emulatedUser.uid ne 'anonymous'}">
				<button type="submit" class="ASM-btn ASM-btn-bind-cart hidden">
					<spring:theme code="asm.emulate.cart.bind" />
					<span class="hidden-sm"><spring:theme
							code="asm.emulate.cart.bind.ending" /></span>
				</button>
			</c:when>
			<c:otherwise>
				<button type="submit" class="ASM-btn ASM-btn-bind-cart hidden">
					<spring:theme code="asm.emulate.cart.bind" />
					<span class="hidden-sm"><spring:theme
							code="asm.emulate.customer.bind.ending" /></span>
				</button>
			</c:otherwise>
		</c:choose>
	</div>
</form>