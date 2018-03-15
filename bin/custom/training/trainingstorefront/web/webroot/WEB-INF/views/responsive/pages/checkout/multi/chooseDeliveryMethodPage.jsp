<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

<div class="row">
    <div class="col-sm-6">
        <div class="checkout-headline">
            <span class="glyphicon glyphicon-lock"></span>
            <spring:theme code="checkout.multi.secure.checkout" />
        </div>
		<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
				<ycommerce:testId code="checkoutStepTwo">
					<div class="checkout-shipping">
						<multi-checkout:shipmentItems cartData="${cartData}" showDeliveryAddress="true" />
						<div class="checkout-indent">
							<div class="headline"><spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder" /></div>
							<form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/select" method="get">
								<div class="form-group">
									<multi-checkout:deliveryMethodSelector deliveryMethods="${deliveryMethods}" selectedDeliveryMethodId="${cartData.deliveryMode.code}"/>
								</div>
							</form>
							<p><spring:theme code="checkout.multi.deliveryMethod.message" htmlEscape="false"/></p>
						</div>
					</div>
					<button id="deliveryMethodSubmit" type="button" class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue"/></button>
				</ycommerce:testId>
			</jsp:body>
		</multi-checkout:checkoutSteps>
    </div>

    <div class="col-sm-6 hidden-xs">
		<multi-checkout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
    </div>

    <div class="col-sm-12 col-lg-12">
        <cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>
</div>

</template:page>
