<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<c:choose>
	<c:when test="${not empty product.bundleTemplates}">
		<div class="carousel__component carousel-bundles__component">
			<div class="carousel__component--headline">
				<spring:theme code="product.bundles" />
			</div>
			<div class="carousel__component--carousel js-owl-carousel js-owl-default">
				
				<c:forEach items="${product.bundleTemplates}" var="bundleTemplate">

					<c:url value="${product.url}" var="productUrl"/>

					<div class="carousel__item">
						
						<div class="carousel__item--name">${fn:escapeXml(bundleTemplate.rootBundleTemplateName)}</div>
						<div class="carousel__item--name">${fn:escapeXml(bundleTemplate.name)}</div>
						
						<!-- Add bundle -->
						<c:url value="/cart/addBundle" var="addBundleToCartUrl"/>
						<form:form method="post" id="addBundleToCartForm" class="add_to_cart_form" action="${addBundleToCartUrl}">
							<input type="hidden" name="productCodeForBundle" value="${fn:escapeXml(product.code)}"/>
							<input type="hidden" name="bundleTemplate" value="${fn:escapeXml(bundleTemplate.id)}"/>
	
							<c:set var="buttonType">button</c:set>
							<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
								<c:set var="buttonType">submit</c:set>
							</c:if>
							
							<c:choose>
								<c:when test="${fn:contains(buttonType, 'button')}">
									<button type="${buttonType}" class="btn btn-primary btn-block js-add-to-cart outOfStock" disabled="disabled">
										<spring:theme code="product.start.bundle"/>
									</button>
								</c:when>
								<c:otherwise>
	
									<ycommerce:testId code="addBundleToCartButton">
										<button id="addBundleToCartButton" type="${buttonType}" class="btn btn-primary btn-block js-add-to-cart js-enable-btn" disabled="disabled">
											<spring:theme code="product.start.bundle" />
										</button>
									</ycommerce:testId>
							
								</c:otherwise>
							</c:choose>
						
						</form:form>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>
				
