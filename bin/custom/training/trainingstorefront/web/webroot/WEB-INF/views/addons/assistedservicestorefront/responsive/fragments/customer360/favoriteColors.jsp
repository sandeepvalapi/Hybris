<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="favoriteColorsData" value="${fragmentData}" scope="page" />
<div class="asm__customer360-headline">
    <spring:theme code="text.customer360.colors.headline" arguments="${favoriteColorsData.name}"/>
</div>

<div class="asm__customer360-subheadline">
    <spring:theme code="text.customer360.colors.subheadline"/>
</div>
<div class="asm__customer360-overview-colors">
    <div class="asm__customer360-overview-colors-color" style="width: 50%; background: #444444;"></div>
    <div class="asm__customer360-overview-colors-color" style="width: 30%; background: #808080;"></div>
    <div class="asm__customer360-overview-colors-color" style="width: 15%; background: #ff0000;"></div>
    <div class="asm__customer360-overview-colors-color" style="width: 5%; background: #00ff00;"></div>
</div>