<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="asm__customer360-overview-recentsearch">
   <c:choose>
    <c:when test="${empty fragmentData}">
        <div class="asm__customer360-overview-divider">
            <spring:theme
                  code="text.customer360.yprofile.search.recent.no.results"
                   text="There are currently no Recent Search items" />
        </div>
	 </c:when>
     <c:otherwise>
     	<c:forEach items="${fragmentData}" var="keywordAffinityData">
	        <div class="asm__customer360-overview-recentsearch-item">
	            <spring:url value="/search/?text=" var="url" />
	            <a href="${url}${keywordAffinityData.searchText}" class="responsive-table-link text-nowrap">${keywordAffinityData.searchText}</a>&nbsp;(${keywordAffinityData.searchCount})
	        </div>
	    </c:forEach>
           </c:otherwise>
      </c:choose>
</div>