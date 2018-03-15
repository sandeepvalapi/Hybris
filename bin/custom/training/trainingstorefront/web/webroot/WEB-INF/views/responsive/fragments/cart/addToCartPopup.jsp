<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

{"cartData": {
"total": "${cartData.totalPrice.value}",
"products": [
<c:forEach items="${cartData.entries}" var="cartEntry" varStatus="status">
	{
		"sku":		"${fn:escapeXml(cartEntry.product.code)}",
		"name": 	"<c:out value='${cartEntry.product.name}' />",
		"qty": 		"${cartEntry.quantity}",
		"price": 	"${cartEntry.basePrice.value}",
		"categories": [
		<c:forEach items="${cartEntry.product.categories}" var="category" varStatus="categoryStatus">
			"<c:out value='${category.name}' />"<c:if test="${not categoryStatus.last}">,</c:if>
		</c:forEach>
		]
	}<c:if test="${not status.last}">,</c:if>
</c:forEach>
]
},

"quickOrderErrorData": [
<c:forEach items="${quickOrderErrorData}" var="quickOrderEntry" varStatus="status">
	{
		"sku":		"${fn:escapeXml(quickOrderEntry.productData.code)}",
		"errorMsg": "<spring:theme code='${quickOrderEntry.errorMsg}' htmlEscape="true"/>"
	}<c:if test="${not status.last}">,</c:if>
</c:forEach>
],

"cartAnalyticsData":{"cartCode" : "${cartCode}","productPostPrice":"${entry.basePrice.value}","productName":"<c:out value='${product.name}' />"}
,
"addToCartLayer":"<spring:escapeBody javaScriptEscape="true" htmlEscape="false">
	<spring:htmlEscape defaultHtmlEscape="true">
	<spring:theme code="text.addToCart" var="addToCartText"/>
	<c:url value="/cart" var="cartUrl"/>
	<ycommerce:testId code="addToCartPopup">
		<div id="addToCartLayer" class="add-to-cart">
            <div class="cart_popup_error_msg">
                <c:choose>
	                <c:when test="${quickOrderErrorData ne null and not empty quickOrderErrorData}">
	                	<spring:theme code="${quickOrderErrorMsg}" arguments="${fn:length(quickOrderErrorData)}" />
                    </c:when>
                    <c:when test="${multidErrorMsgs ne null and not empty multidErrorMsgs}">
                        <c:forEach items="${multidErrorMsgs}" var="multidErrorMsg">
                            <spring:theme code="${multidErrorMsg}" />
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <spring:theme code="${errorMsg}" />
                    </c:otherwise>
                </c:choose>
            </div>

            <c:choose>
                <c:when test="${modifications ne null}">
                    <c:forEach items="${modifications}" var="modification">
                        <c:set var="product" value="${modification.entry.product}" />
                        <c:set var="entry" value="${modification.entry}" />
                        <c:set var="quantity" value="${modification.quantityAdded}" />
                        <cart:popupCartItems entry="${entry}" product="${product}" quantity="${quantity}"/>
                    </c:forEach>
                </c:when>
                <c:otherwise>

                    <cart:popupCartItems entry="${entry}" product="${product}" quantity="${quantity}"/>
                </c:otherwise>
            </c:choose>

            <ycommerce:testId code="checkoutLinkInPopup">
                <a href="${cartUrl}" class="btn btn-primary btn-block add-to-cart-button">
	                <c:choose>
		                <c:when test="${isQuote}">
		                	<spring:theme code="quote.view" />
	                    </c:when>
	                    <c:otherwise>
	                        <spring:theme code="checkout.checkout" />
	                    </c:otherwise>
                	</c:choose>
                </a>
            </ycommerce:testId>


            <a href="" class="btn btn-default btn-block js-mini-cart-close-button">
                <spring:theme code="cart.page.continue"/>
            </a>
		</div>
	</ycommerce:testId>
	</spring:htmlEscape>
</spring:escapeBody>"
}



