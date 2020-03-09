<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:url value="${urlLink}" var="simpleBannerUrl" />

<div class="banner__component simple-banner">
	<c:choose>
		<c:when test="${empty simpleBannerUrl || simpleBannerUrl eq '#'}">
			<img title="${fn:escapeXml(media.altText)}" alt="${fn:escapeXml(media.altText)}"
				src="${fn:escapeXml(media.url)}">
		</c:when>
		<c:otherwise>
			<a href="${fn:escapeXml(simpleBannerUrl)}"><img title="${fn:escapeXml(media.altText)}"
				alt="${fn:escapeXml(media.altText)}" src="${fn:escapeXml(media.url)}"></a>
		</c:otherwise>
	</c:choose>
</div>