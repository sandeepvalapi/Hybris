<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="qty">
	<c:if test="${product.purchasable}">
		<label for="qtyInput">
			<spring:theme code="basket.page.quantity"/>
		</label>
		<input type="text" maxlength="3" size="1" id="qtyInput" name="qtyInput" class="qty" value="1">
	</c:if>
	
	<c:if test="${product.stock.stockLevel gt 0}">
		<c:set var="productStockLevel">${product.stock.stockLevel}&nbsp;
			<spring:theme code="product.variants.in.stock"/>
		</c:set>
	</c:if>
	<c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">
		<c:set var="productStockLevel">
			<spring:theme code="product.variants.only.left" arguments="${product.stock.stockLevel}"/>
		</c:set>
	</c:if>
	<c:if test="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}">
		<c:set var="productStockLevel">
			<spring:theme code="product.variants.available"/>
		</c:set>
	</c:if>

	<ycommerce:testId code="productDetails_productInStock_label">
		<p class="stock_message">${productStockLevel}</p>
	</ycommerce:testId>
	
	<product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}" />
	
</div>



<div id="actions-container-for-${fn:escapeXml(component.uid)}" class="productAddToCartPanelContainer clearfix">
	<ul class="productAddToCartPanel clearfix">
		<c:if test="${multiDimensionalProduct}" >
			<c:url value="${product.url}/orderForm" var="productOrderFormUrl"/>
			<a href="${productOrderFormUrl}" class="button negative" id="productOrderButton" ><spring:theme code="order.form" /></a>
		</c:if>
		<action:actions element="li" styleClass="productAddToCartPanelItem" parentComponent="${component}"/>
	</ul>
</div>

