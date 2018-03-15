<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:url value="my-account/support-ticket" var="ticketUrl" />

<div class="pageable-fragment asm-customer360-tab">
    <div class="asm-customer360-tickets-tab">
        <h3><spring:theme code="text.customer360.activity.tickets" text="Tickets"/></h3>
        <c:choose>
		 <c:when test="${ empty fragmentData}">
				<div class="clear">
		         	<spring:theme
		                 code="text.customer360.activity.tickets.no"
		                 text="There are currently no Support Ticket items" />
		       </div>
		   </c:when>
		<c:otherwise>
        <div class="pagerTickets pager hidden-xs"></div>
        <table id="asm-customer360-tickets-table" class="table techne-table">
            <thead>
                <tr class="responsive-table-head hidden-xs">
                    <th class="pointer"><spring:theme code="text.customer360.activity.general.id" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.activity.tickets.headline" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.activity.tickets.category" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.activity.general.status" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.activity.general.created" /></th>
                    <th class="headerSortUp pointer"><spring:theme code="text.customer360.activity.general.updated" /></th>
                </tr>
            </thead>

            <tbody>
                <c:forEach items="${fragmentData}" var="activityData">
                    <tr>
                        <c:url value="/${ticketUrl}/${activityData.id}" var="link"/>
                        <c:if test="${not empty activityData.url}">
                            <spring:url value="${activityData.url}" var="link" htmlEscape="false"/>
                        </c:if>
                        	
                        <td data-th="ID"><a href="${link}" class="responsive-table-link text-nowrap"><c:out value="${activityData.id}" /></a></td>
                        <td data-th="Headline" class="" title="<spring:theme code='${activityData.description}' htmlEscape='true'/>">
                          <div class="asm-customer360-table-onLineCell">
                                <spring:theme code="${activityData.description}" htmlEscape="true"/>
                          </div>
                        </td>
                        <td data-th="Category"><spring:theme code="${activityData.category}" /></td>
                        <td data-th="Status">
                            <c:choose>
                                <c:when test="${(not empty activityData.status)}">
                                    <spring:theme code="${activityData.status}" />
                                </c:when>
                                <c:otherwise>
                                    <spring:theme code="text.customer360.activity.general.status.undefined" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td data-th="Created" data-text="${activityData.created.time}" ><fmt:formatDate value="${activityData.created}" pattern="dd-MM-yy hh:mm a" /></td>
                        <td data-th="Updated" data-text="${activityData.updated.time}" ><fmt:formatDate value="${activityData.updated}" pattern="dd-MM-yy hh:mm a" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="pagerTickets pager visible-xs"></div>
         <script>
        $("#asm-customer360-tickets-table").tablesorter({headers: { 0: { sorter: "text"}, 1: {sorter: "text"},  2: { sorter: "text"}, 3: {sorter: "text"}, 4: {sorter: "text"}, 5: {sorter: "text"} }})
            .tablesorterPager({container: $(".asm-customer360-tickets-tab .pagerTickets"), size: getAifTablePageSize()});
    	</script>
    </c:otherwise>
	</c:choose>
    </div>
</div>
