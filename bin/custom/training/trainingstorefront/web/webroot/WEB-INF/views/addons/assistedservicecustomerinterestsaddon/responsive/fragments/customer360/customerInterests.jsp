<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<c:url var="custInterestsUrl" value="/my-account/my-interests" />

<c:set var="custInterests" value="${fragmentData}"  scope="request"/>

<c:choose>
 	 <c:when test="${empty custInterests}">
	    <div class="asm__customer360-subheadline">
	        	 <spring:message code="text.customer360.overview.customer.interests" text="Interests" />
	   	 </div>
	   	  <div class="asm__customer360-overview-divider">
	     	<spring:theme
	                  code="text.customer360.overview.customer.interests.no.results"
	                  text="There are currently no Interest items" />
	      </div>
	 </c:when>
	 <c:otherwise>
		<div class="asm__customer360-headline asm__customer360-overview-interests-headline">
	        <a href="${custInterestsUrl}"><spring:message code="text.customer360.overview.customer.interests" text="Interests" /></a>
	    </div>
	    <div class="asm__customer360-overview-interests-section">
	        <div class="row">
	            <c:forEach var="entry" items="${custInterests}">
	                <div class="col-sm-2">
	                    <div class="asm__customer360-overview-interests">
	                    <c:url value="${entry.product.url}" var="productUrl" />
	                    <div class="asm__customer360-overview-interests-img">
	                        <a href="${productUrl}"  class="responsive-table-link text-nowrap">
	                            <product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
	                        </a>
	                    </div>
	                    <div class="asm__customer360-overview-interests-name">${fn:escapeXml(entry.product.name)}</div>
	                    <div class="asm__customer360-overview-interests-stock">
	                        <c:choose>
	                            <c:when test="${not empty entry.product.stock and entry.product.stock.stockLevelStatus.code eq 'inStock'}">
	                                <span class="stock instock"><spring:theme code="product.variants.in.stock"/></span>
	                            </c:when>
	                            <c:when test="${not empty entry.product.stock and entry.product.stock.stockLevelStatus.code eq 'lowStock'}">
	                                <span class="stock lowstock"><spring:theme code="product.variants.low.stock"/></span>
	                            </c:when>
	                            <c:otherwise>
	                                <span class="out-of-stock">
	                                    <spring:theme code="product.variants.out.of.stock"/>
	                                </span>
	                            </c:otherwise>
	                        </c:choose>
	                    </div>
	
	                    <c:if test="${empty entry.product.stock.stockLevelStatus.code or entry.product.stock.stockLevelStatus.code eq 'outOfStock' and not empty entry.product.futureStocks}">
	                        <div class="asm__customer360-overview-interests-price">
	                            <span class="stockSpan">
	                                <spring:theme code="text.customer360.overview.customer.interests.availability" /><br/>
	                                <fmt:formatDate value="${entry.product.futureStocks[0].date}" pattern="dd-MM-YYYY" />
	                            </span>
	                        </div>
	                    </c:if>
	                    </div>
	                </div>
	            </c:forEach>
	        </div>
	     </div>
	  </c:otherwise>
</c:choose>