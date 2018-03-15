<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="store" required="true" type="de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<div class="detailSection">
	<div class="detailSectionHeadline">${fn:escapeXml(store.name)}</div>
	<c:if test="${not empty store.address.line1 || not empty store.address.line2 ||
				  not empty store.address.town || not empty store.address.country.name ||
				  not empty store.address.postalCode || not empty store.address.phone }">
	<ul>
		<li>${fn:escapeXml(store.address.line1)}</li>
		<li>${fn:escapeXml(store.address.line2)}</li>
		<li>${fn:escapeXml(store.address.town)}</li>
		<li>${fn:escapeXml(store.address.postalCode)}</li>
		<li>${fn:escapeXml(store.address.country.name)}</li>
		<li>${fn:escapeXml(store.address.phone)}</li>
	</ul>
	</c:if>
</div>


<c:if test="${not empty store.formattedDistance}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.distance" /></div>
		<c:choose>
			<c:when test="${not empty locationQuery}">
				<spring:theme code="storeDetails.table.distanceFromSource" argumentSeparator="^" arguments="${store.formattedDistance}^${fn:escapeXml(locationQuery)}"/>
			</c:when>
			<c:otherwise>
				<spring:theme code="storeDetails.table.distanceFromCurrentLocation" argumentSeparator="^" arguments="${store.formattedDistance}"/>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>


<c:if test="${not empty store.address.phone}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.telephone" /></div>
			${fn:escapeXml(store.address.phone)}
	</div>
</c:if>

<c:if test="${not empty store.address.email}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.email" /></div>
		<c:set var="storeEmail" value="${fn:escapeXml(store.address.email)}" />
		<a href="mailto:${storeEmail}">${storeEmail}</a>
	</div>
</c:if>

<c:if test="${not empty store.openingHours}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.opening" /></div>
		<store:openingSchedule openingSchedule="${store.openingHours}" />
	</div>
</c:if>


<c:if test="${not empty store.openingHours.specialDayOpeningList}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.openingSpecialDays" /></div>
		<store:openingSpecialDays openingSchedule="${store.openingHours}" />
	</div>
</c:if>


<c:if test="${not empty store.features}">
	<div class="detailSection">
		<div class="detailSectionHeadline"><spring:theme code="storeDetails.table.features" /></div>
		<ul>
			<c:forEach items="${store.features}" var="feature">
				<li>${fn:escapeXml(feature.value)}</li>
			</c:forEach>
		</ul>
	</div>
</c:if>

