<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="/my-account/saved-carts" var="savedCartUrl" htmlEscape="false"/>

<c:set var="cart" value="${fragmentData}"  scope="request"/>
<c:choose>
  	<c:when test="${empty cart or empty cart.entries}">         
    	 <div class="asm__customer360-subheadline">
        	<spring:message code="ttext.customer360.overview.customer.savedCart " text="Latest Saved Cart"/>
   		 </div>
   		 <div class="asm__customer360-overview-saved-section">
        	<div class="row">
                <div class="col-xs-12">
                    <div class="asm__customer360-overview-divider">
                      <spring:theme
                             code="text.customer360.overview.customer.savedCart.no"
                             text="There are currently no Saved Cart items" />
                     </div>
                 </div>
        	</div>
   		 </div>
    </c:when>
	<c:otherwise>
	   	<div class="asm__customer360-headline">
	        <spring:message code="text.customer360.overview.customer.savedCart"  text="Latest Saved Cart"/>&nbsp;<a href="${savedCartUrl}/<c:out value='${cart.code}'/>"><c:out value='${cart.code}'/></a>
	        <span class="asm__customer360-overview-saved-card-info"><spring:message code="text.customer360.overview.customer.cart.total.items" text="Total No. Items" />&nbsp;${cart.totalUnitCount}<span class="asm__customer360-overview-saved-card-divider"></span><spring:message code="text.customer360.overview.customer.cart.total.price" text="Total Price" />&nbsp;${cart.totalPrice.formattedValue}</span>
	    </div>
	
	    <div class="asm__customer360-overview-saved-section">
	        <div class="row asm__customer360-overview-saved-wrap">
	            <c:forEach var="entry" items="${cart.entries}">
	                <div class="col-sm-6 col-md-4 asm__customer360-overview-saved-box">
	                    <div class="asm__customer360-overview-saved">
	                        <c:url value="${entry.product.url}" var="productUrl" />
	                        <div class="asm__customer360-overview-product-img">
	                            <a href="${productUrl}" class="responsive-table-link text-nowrap">
	                                <product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
	                            </a>
	                        </div>
	                        <div class="asm__customer360-overview-saved-text-wrap">
                                <div class="asm__customer360-overview-product-name">
                                    <a href="${productUrl}" class="responsive-table-link text-nowrap" title="<c:out value='${entry.product.name}'/>">
                                        <div class="hide_overflow"><c:out value='${entry.product.name}'/></div>
                                    </a>
                                </div>
                                <div class="asm__customer360-overview-product-sku"><c:out value='${entry.product.code}'/></div>
                                <div class="asm__customer360-overview-product-price">                                
                                    <c:choose>
	                                	<c:when test="${entry.quantity > 1}">
	                                        <span class="asm__customer360-overview-qty-divider">
	                                            <spring:message code="text.customer360.overview.customer.cart.items.price" text="Item price" />:&nbsp;${entry.basePrice.formattedValue}
	                                        </span>
	                                        <spring:message code="text.customer360.overview.customer.cart.item.qty" text="QTY" />:&nbsp;${entry.quantity}
	                                        <div class="asm__customer360-overview-saved-total">
                                        			<spring:message code="text.customer360.overview.customer.cart.total.price" text="Total Price" />:&nbsp;${entry.totalPrice.formattedValue}
                                   			 </div>
	                                    </c:when>
	                                    <c:otherwise>
	                                    	<div class="asm__customer360-overview-saved-total">
	                                            <spring:message code="text.customer360.overview.customer.cart.items.price" text="Item price" />:&nbsp;${entry.basePrice.formattedValue}
	                                        </div>
	                                    </c:otherwise>
                                    </c:choose>         
                                </div>
                            </div>
                        </div>
	                </div>
	            </c:forEach>
	        </div>
	    </div>
   </c:otherwise>
</c:choose>