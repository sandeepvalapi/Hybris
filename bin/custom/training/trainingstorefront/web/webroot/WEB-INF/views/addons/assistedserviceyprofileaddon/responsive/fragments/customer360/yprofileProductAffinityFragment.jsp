<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="assistedserviceutils" uri="http://hybris.com/tld/assistedserviceutils" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="asm__customer360-overview-saved-section">
    <div class="row">
        <c:choose>
            <c:when test="${empty fragmentData}"> 
                <div class="col-xs-12">
                    <div class="asm__customer360-overview-divider">
                        <spring:theme
                            code="text.customer360.yprofile.product.recent.no.results"
                            text="There are currently no Recent Product items" />
                    </div>
                </div>
            </c:when>
            <c:otherwise>
            	<c:forEach items="${fragmentData}" var="productAffinityData">
                    <spring:url value="${productAffinityData.productData.url}" var="productUrl" htmlEscape="false"/>
                    <div class="col-sm-6 col-md-4">
                        <div class="asm__customer360-overview-product-view">
                            <div class="asm__customer360-overview-product-img">
                                <a href="${productUrl}" class="responsive-table-link text-nowrap">
                                    <product:productPrimaryImage product="${productAffinityData.productData}" format="thumbnail" />
                                </a>
                            </div>
                            <div class="asm__customer360-overview-product-name">
                            	 <c:set var="productName" value="${fn:escapeXml(productAffinityData.productData.name)}" scope="page"/>
                                <a href="${productUrl}" class="responsive-table-link text-nowrap" title="${productName}">
                                    <div class="hide_overflow">${productName}</div>
                                </a>
                            </div>
                            <div class="asm__customer360-overview-product-sku">${fn:escapeXml(productAffinityData.productData.code)}</div>
                            <div class="asm__customer360-overview-product-price">${fn:escapeXml(productAffinityData.productData.price.formattedValue)}</div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>