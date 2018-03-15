<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/returns/" var="returnDetailsUrl" htmlEscape="false"/>
<c:set var="searchUrl" value="/my-account/returns?sort=${searchPageData.pagination.sort}"/>

<div class="account-section-header">
	<spring:theme code="text.account.returnHistory" />
</div>

<c:if test="${empty searchPageData.results}">
	<div class="account-section-content content-empty">
		<ycommerce:testId code="returnHistory_noReturns_label">
			<spring:theme code="text.account.returnHistory.noReturns" />
		</ycommerce:testId>
	</div>
</c:if>
<c:if test="${not empty searchPageData.results}">
	<div class="account-section-content	">
		<div class="account-orderhistory account-returnorder">
			<div class="account-orderhistory-pagination account-returnorder-pagination">
				<nav:pagination top="true" msgKey="text.account.returnHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>
			<div class="account-overview-table">
				<table class="orderhistory-list-table returnorder-list-table responsive-table">
					<tr class="account-orderhistory-table-head account-returnorder-table-head responsive-table-head hidden-xs">
						<th><spring:theme code="text.account.returnHistory.rma" /></th>
						<th><spring:theme code="text.account.returnHistory.orderNumber" /></th>
						<th><spring:theme code="text.account.returnHistory.creationTime"/></th>
						<th><spring:theme code="text.account.returnHistory.returnStatus"/></th>
					</tr>
					<c:forEach items="${searchPageData.results}" var="myReturn">
						<tr class="responsive-table-item">
							<ycommerce:testId code="orderHistoryItem_orderDetails_link">
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.returnHistory.rma" /></td>
								<td class="responsive-table-cell">
									<a href="${returnDetailsUrl}${fn:escapeXml(myReturn.code)}" class="responsive-table-link">
											${fn:escapeXml(myReturn.rma)}
									</a>
								</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.returnHistory.orderNumber" /></td>
								<td class="responsive-table-cell">
										${fn:escapeXml(myReturn.order.code)}
								</td>
								<td class="responsive-table-cell">
									<fmt:formatDate value="${myReturn.creationTime}" dateStyle="medium" timeStyle="short" type="both"/>
								</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.returnHistory.returnStatus"/></td>
								<td class="status">
									<spring:theme code="text.account.return.status.display.${fn:escapeXml(myReturn.status)}"/>
								</td>
							</ycommerce:testId>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="account-orderhistory-pagination account-returnorder-pagination">
			<nav:pagination top="false" msgKey="text.account.returnHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
		</div>
	</div>
</c:if>
