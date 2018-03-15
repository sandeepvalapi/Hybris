<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${searchPageData ne null and !empty searchPageData.results}">
	{"total":${searchPageData.pagination.totalNumberOfResults},"data":[
	<c:forEach items="${searchPageData.results}" var="pos" varStatus="loopStatus">
		<c:set value="${ycommerce:storeImage(pos, 'cartIcon')}" var="storeImage"/>
		<c:url value="${pos.url}" var="storeUrl" scope="request"/>
		{
			"displayName" : "${fn:escapeXml(pos.displayName)}",
			"name" : "${fn:escapeXml(pos.name)}",
			"url" : "${fn:escapeXml(pos.url)}",
			"phone" : "${fn:escapeXml(pos.address.phone)}",
			"formattedDistance" : "${fn:escapeXml(pos.formattedDistance)}",
			"line1" : "${fn:escapeXml(pos.address.line1)}",
			"line2" : "${fn:escapeXml(pos.address.line2)}",
			"town" : "${fn:escapeXml(pos.address.town)}",
			"postalCode" : "${fn:escapeXml(pos.address.postalCode)}",
			"latitude" : "${pos.geoPoint.latitude}",
			"longitude" : "${pos.geoPoint.longitude}",
			<c:if test="${not empty pos.openingHours}">
				"openings":<store:openingSchedule openingSchedule="${pos.openingHours}" />
			</c:if>
			<c:if test="${not empty pos.features}">
				"features" :[
						<c:forEach items="${pos.features}" var="feature" varStatus="featureNumber">
							"${fn:escapeXml(feature.value)}"<c:if test="${!featureNumber.last}">,</c:if>
						</c:forEach>
						],
					
			</c:if>
			"image" : "${storeImage.url}"
		}<c:if test="${!loopStatus.last}">,</c:if>
	</c:forEach>
	]}
</c:if>

<c:if test="${empty searchPageData.results}">

<template:page pageTitle="${pageTitle}">
		
		<cms:pageSlot position="TopContent" var="feature">
			<cms:component component="${feature}"  element="div" class="top-content-slot cms_disp-img_slot"  />
		</cms:pageSlot>

		<div id="storeFinder">
			<cms:pageSlot position="MiddleContent" var="feature">
				<cms:component component="${feature}"  element="div"/>
			</cms:pageSlot>
		</div>

</template:page>

</c:if>