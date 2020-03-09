<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
    Product list item without add to cart buttons
--%>
<spring:htmlEscape defaultHtmlEscape="true"/>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>

<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>

<li class="product__list--item">
<ycommerce:testId code="test_searchPage_wholeProduct">
    <c:set var="productName" value="fn:escapeXml(product.name)"/>

    <a class="product__list--thumb" href="${productUrl}" title="${productName}">
        <product:productPrimaryImage product="${product}" format="thumbnail"/>
    </a>
    <ycommerce:testId code="searchPage_productName_link_${product.code}">
        <a class="product__list--name" href="${productUrl}">${productName}</a>
    </ycommerce:testId>

    <div class="product__list--price-panel">
        <c:if test="${not empty product.potentialPromotions}">
            <div class="product__listing--promo">
                <c:forEach items="${product.potentialPromotions}" var="promotion">
                    ${fn:escapeXml(promotion.description)}
                </c:forEach>
            </div>
        </c:if>

        <ycommerce:testId code="searchPage_price_label_${product.code}">
            <div class="product__listing--price"><product:productListerItemPrice product="${product}"/></div>
        </ycommerce:testId>
    </div>

    <c:if test="${not empty product.summary}">
        <div class="product__listing--description">${fn:escapeXml(product.summary)}</div>
    </c:if>
</ycommerce:testId>
</li>






