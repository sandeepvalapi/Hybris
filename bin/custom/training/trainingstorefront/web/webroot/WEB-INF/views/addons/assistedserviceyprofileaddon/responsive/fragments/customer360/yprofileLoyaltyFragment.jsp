<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="loyaltyData" value="${fragmentData}" scope="page"/>

<c:set var="silverBadgeStatus" value="${loyaltyData.silverBadgeActive ? 'asm__customer360-overview-info-points-medals-silver-on' : 'asm__customer360-overview-info-points-medals-silver-off'}"/>
<c:set var="goldBadgeStatus" value="${loyaltyData.goldBadgeActive ? 'asm__customer360-overview-info-points-medals-gold-on' : 'asm__customer360-overview-info-points-medals-gold-off'}"/>
<c:set var="platinumBadgeStatus" value="${loyaltyData.platinumBadgeActive ? 'asm__customer360-overview-info-points-medals-platinum-on' : 'asm__customer360-overview-info-points-medals-platinum-off'}"/>
<c:set var="currentLoyaltyBalance" value="${fn:escapeXml(loyaltyData.currentLoyaltyBalance)}" scope="page"/>


<div class="asm__customer360-overview-info-points">
     <div class="asm__customer360-overview-info-headline">Loyalty Points</div>
                       <c:choose>
             <c:when test="${empty loyaltyData}">
                 <spring:theme code="text.customer360.yprofile.loyalty.empty" text="There are currently no Loyalty data"/>
             </c:when>
         <c:otherwise>
             <div class="asm__customer360-overview-info-points-pts">
                <c:choose>
                 <c:when test="${not empty currentLoyaltyBalance}">
                     ${currentLoyaltyBalance}&nbsp <spring:theme code="text.customer360.yprofile.pts" text="PTS"/>
                 </c:when>
                 <c:otherwise>
                     <spring:theme code="text.customer360.yprofile.loyaltyBalance.missing" text="000000"/>
                  </c:otherwise>
              </c:choose>
             </div>
             <div class="asm__customer360-overview-info-points-ptsinfo">
                 <c:set var="activityScoreValue" value="${fn:escapeXml(loyaltyData.activityScore)}" scope="page"/>
                 <c:set var="activityScore" value="${not empty activityScoreValue ? activityScoreValue : 'text.customer360.yprofile.activityScore.missing'}" />
                 <spring:theme code="${activityScore}" text="No Activity Score"/>
             </div>
             <div class="asm__customer360-overview-info-points-medals">

                <span class="asm__customer360-overview-info-points-medals-item ${silverBadgeStatus}" title="<spring:theme code="text.customer360.yprofile.medal.silver.alt" text ="Silver"/>" />
                     <span class="asm__customer360-overview-info-points-medals-item ${goldBadgeStatus}" title="<spring:theme code="text.customer360.yprofile.medal.gold.alt" text ="Gold"/>" />
                 <span class="asm__customer360-overview-info-points-medals-item ${platinumBadgeStatus}" title="<spring:theme code="text.customer360.yprofile.medal.platinum.alt" text ="Platinum"/>" />
                            </div>
      </c:otherwise>
     </c:choose>
</div>