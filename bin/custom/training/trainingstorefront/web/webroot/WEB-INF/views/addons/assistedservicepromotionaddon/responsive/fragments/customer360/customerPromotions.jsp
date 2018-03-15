<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div role="tabpanel" class="tab-pane" id="activitySection">
    <div class="pageable-fragment asm-customer360-tab">
        
            <div class="asm-customer360-activity-tab asm-customer360-promotions">
            <div class="clearfix">
    <h3><spring:theme code="${text.customer360.customerPromotionHeading}" text="CUSTOMER PROMOTIONS"/></h3>
         </div>   
            <c:choose>
  	<c:when test="${empty fragmentData}">         
    	 <div class="asm__customer360-noCoupons">
        	<spring:message code="text.customer360.nocustomerPromos" text="There are currently no Customer Promotions available."/>
   		 </div>
   		 </c:when>
   		 <c:otherwise>
                    <c:forEach items="${fragmentData}" var="promotion">
                       
                       
                       <div class="asm-customer360-promotions-item clearfix">
                       <div class="asm-customer360-promotions-item-title"> 
                           <spring:theme code="${promotion.name}" />
                       </div>
                       <div class="asm-customer360-promotions-item-desc">
                           <spring:theme code="${promotion.description}" />
                       </div>
                       
                       </div>
                    </c:forEach>  
           </c:otherwise>
           
                    </c:choose>
        </div>
    </div>
</div>
