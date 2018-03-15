<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<c:set var="customer" value="${fragmentData}" scope="request"/>
<div class="asm__customer360-profile">
    <div class="row">
        <div class="asm__customer360-profile-col">
            <div class="asm__customer360-profile-headline"><spring:theme code="text.account.paymentDetails.billingAddress"/></div>
            <c:choose>
                <c:when test="${not empty customer.billingAddress}">
                        <spring:theme code="${customer.billingAddress.line1}" htmlEscape="true" /><br>
                        <spring:theme code="${customer.billingAddress.line2}" htmlEscape="true" /><br>
                        <spring:theme code="${customer.billingAddress.town}"  htmlEscape="true" />,
                        <c:if test="${not empty customer.billingAddress.region}"><spring:theme code="${customer.billingAddress.region.name}" htmlEscape="true"/></c:if><br>
                        <c:if test="${not empty customer.billingAddress.country}"><spring:theme code="${customer.billingAddress.country.name}" htmlEscape="true"/></c:if>
                </c:when>
                <c:otherwise>-</c:otherwise>
            </c:choose>
        </div>
        <div class="asm__customer360-profile-col">
            <div class="asm__customer360-profile-headline"><spring:theme code="checkout.summary.deliveryAddress"/></div>
            <c:choose>
                <c:when test="${not empty customer.deliveryAddress}">
                   <spring:theme code="${customer.deliveryAddress.line1}" htmlEscape="true" /><br>
                   <spring:theme code="${customer.deliveryAddress.line2}" htmlEscape="true" /><br>
                   <spring:theme code="${customer.deliveryAddress.town}" htmlEscape="true" />,
                   <c:if test="${not empty customer.deliveryAddress.region}"><spring:theme code="${customer.deliveryAddress.region.name}" htmlEscape="true"/></c:if><br>
                   <c:if test="${not empty customer.deliveryAddress.country}"><spring:theme code="${customer.deliveryAddress.country.name}" htmlEscape="true"/></c:if>
                </c:when>
                <c:otherwise>-</c:otherwise>
            </c:choose>
        </div>

        <div class="asm__customer360-profile-col">
            <div class="asm__customer360-profile-headline"><spring:theme code="text.customer360.profile.phone1"/></div>
            <c:choose>
                <c:when test="${not empty customer.phone1}">
                    <spring:theme code="${customer.phone1}" htmlEscape="true"/>
                </c:when>
                <c:otherwise>-</c:otherwise>
            </c:choose>
        </div>
        <div class="asm__customer360-profile-col">
            <div class="asm__customer360-profile-headline"><spring:theme code="text.customer360.profile.phone2"/></div>
            <c:choose>
                <c:when test="${not empty customer.phone2}">
                    <spring:theme code="${customer.phone2}" htmlEscape="true"/>
                </c:when>
                <c:otherwise>-</c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="asm__customer360-profile-headline"><spring:theme code="text.customer360.profile.payment_methods"/></div>
    <div class="row">
    <c:choose>
        <c:when test="${not empty customer.paymentInfoList}">
            <c:forEach items="${customer.paymentInfoList}" var="paymentInfo">
                <div class="col-md-4">
                    <c:choose>
                        <c:when test="${paymentInfo.defaultPaymentInfo}">
                             <span class="asm__customer360-profile-payment asm__customer360-profile-payment-active">
                        </c:when>
                        <c:otherwise>
                             <span class="asm__customer360-profile-payment">
                        </c:otherwise>
                    </c:choose>
                        <div class="asm__customer360-profile-payment-img asm__customer360-profile-payment-${paymentInfo.cardType}"></div>
                        <div class="asm__customer360-profile-payment-nr"><spring:theme code="${paymentInfo.cardNumber}, ${paymentInfo.expiryMonth}-${paymentInfo.expiryYear}" htmlEscape="true"/></div>
                        <div class="asm__customer360-profile-payment-type">
                            <c:choose>
                                <c:when test="${paymentInfo.defaultPaymentInfo}">
                                    <span class="asm__customer360-profile-payment-default"><spring:theme code="text.customer360.profile.default"/></span><spring:theme code="${paymentInfo.cardType}" />
                                </c:when>
                                <c:otherwise>
                                    <spring:theme code="${paymentInfo.cardType}" />
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </span>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="col-md-4">
                <div><spring:theme code="text.customer360.profile.nosavedpayments" /></div>
            </div>
        </c:otherwise>
    </c:choose>
    </div>
</div>