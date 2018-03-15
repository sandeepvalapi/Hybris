<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="searchPageData" required="true" type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

{"data":[
	<c:forEach items="${searchPageData.results}" var="pickupStore" varStatus="pickupEntryNumber">
		<c:set value="${ycommerce:storeImage(pickupStore, 'store')}"  var="storeImage"/>
		<c:set var="stockPickup"><storepickup:pickupStoreStockLevel stockData="${pickupStore.stockData}"/></c:set>
		{
			"name" : "${fn:escapeXml(pickupStore.name)}",
			"displayName" : "${fn:escapeXml(pickupStore.displayName)}",
			"town" : "${fn:escapeXml(pickupStore.address.town)}",
			"line1" : "${fn:escapeXml(pickupStore.address.line1)}",
			"line2" : "${fn:escapeXml(pickupStore.address.line2)}",
			"country" : "${fn:escapeXml(pickupStore.address.country.name)}",
			"postalCode" : "${fn:escapeXml(pickupStore.address.postalCode)}",
			"formattedDistance" : "${pickupStore.formattedDistance}",
			"url" : "${storeImage.url}",
			"stockPickup" : "${stockPickup}",
			<storepickup:pickupStoreOpeningSchedule store="${pickupStore}"/>
			"productcode":"${fn:escapeXml(searchPageData.product.code)}",
			"storeLatitude":"${pickupStore.geoPoint.latitude}",
			"storeLongitude":"${pickupStore.geoPoint.longitude}",
			"stockLevel": "${pickupStore.stockData.stockLevel}"
		}<c:if test="${!pickupEntryNumber.last}">,</c:if>
	</c:forEach>
]}
