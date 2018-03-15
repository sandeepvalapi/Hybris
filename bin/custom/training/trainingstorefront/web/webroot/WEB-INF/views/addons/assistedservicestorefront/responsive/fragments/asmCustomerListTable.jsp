<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/nav" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:set var="baseUrl" value="/assisted-service-querying/listCustomers"/>
<c:set var="frowardUrl" value="/my-account/orders"/>
<c:set var="invalidAddtionalColumnIndexes" value="" />
<c:set var="queryPresent" value="${not empty query}"/>
<spring:url value="/assisted-service/emulate/?customerId=" var="emulateCustomerUrl" htmlEscape="false"/>
<spring:url value="${baseUrl}" var="sortUrl" htmlEscape="false"><spring:param name="customerListUId" value="${defaultList}"/><spring:param name="sort" value=""/></spring:url>
<c:choose>
    <c:when test="${customerListData.searchBoxEnabled}">
        <c:set var="searchUrl" value="/assisted-service-querying/listCustomers?sort=${searchPageData.pagination.sort}&customerListUId=${defaultList}&query=${ycommerce:encodeUrl(query)}"/>
    </c:when>
    <c:otherwise>
        <c:set var="searchUrl" value="/assisted-service-querying/listCustomers?sort=${searchPageData.pagination.sort}&customerListUId=${defaultList}"/>
    </c:otherwise>
</c:choose>

<div class="asm-account-section">
    <c:if test="${(empty searchPageData || empty searchPageData.results ) && !queryPresent}">
        <div class="account-section-content	col-md-6 col-md-push-3 content-empty">
            <ycommerce:testId code="orderHistory_noOrders_label">
                <spring:theme code="text.asm.customerList.noCustomers" />
            </ycommerce:testId>
        </div>
    </c:if>
    <c:if test="${not empty searchPageData.results || queryPresent}">
        <div class="account-section-content	">
            <div class="account-orderhistory">
                <div class="account-orderhistory-pagination js-customer-list-sorting" data-sort-url="${sortUrl}">
                    <nav:pagination top="true" msgKey="text.account.customerList.page" showCurrentPageInfo="false" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
                </div>
                <div class="account-overview-table">
                    <table class="table techne-table">
                        <thead>
                            <tr>
                                <th></th>
                                <th><spring:theme code="text.asm.customerList.name" /></th>
                                <th><spring:theme code="text.asm.customerList.email" /></th>
                                <th><spring:theme code="text.asm.customerList.phone" /></th>
                                 <%-- Dynamically add headers for additional columns of customerList and handle exceptions. The invalidAddtionalColumnIndexes records the column indexes that have exception if there is any.--%>
                                <c:if test="${not empty customerListData}">
	                                <c:forEach var="columnKey" items="${customerListData.additionalColumnsKeys}" varStatus="loop">
	                                	<c:if test="${not empty columnKey}">
										    <c:set var="isAnyAdditionalColumnsException" value="${false}" />
											<c:set var="columnValue" value="${searchPageData.results[0]}" />
										       <c:forTokens items="${columnKey}" delims="." var="item">   
											       <c:if test="${!isAnyAdditionalColumnsException}">
														<c:catch var="fieldMissing">
															<c:set var="columnValue" value="${columnValue[item]}" />
														</c:catch>
														<c:if test="${not empty fieldMissing}">
															<c:set var="isAnyAdditionalColumnsException" value="${true}" />
															<c:set var="invalidAddtionalColumnIndexes" value="${invalidAddtionalColumnIndexes};${loop.index}" />
														</c:if>
													</c:if>
												</c:forTokens>
											<c:if test="${!isAnyAdditionalColumnsException}">
												<th><spring:theme code="text.customerList.header.${columnKey}" /></th>
											</c:if>
										</c:if>
									</c:forEach>
								</c:if>
                                <th><spring:theme code="text.asm.customerList.cart" /></th>
                                <th><spring:theme code="text.asm.customerList.orders" /></th>
                                <th><spring:theme code="text.asm.customerList.360view" text="360 View"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${searchPageData.results}" var="customer">
                                <tr class="techne-table-xs-left">
                                    <ycommerce:testId code="orderHistoryItem_orderDetails_link">
                                        <td class="techne-table-xs-left-slot">
                                            <c:choose>
                                                <c:when test="${not empty customer.profilePicture}">
                                                    <a href="<c:out value='${emulateCustomerUrl}${customer.uid}'/>" class="responsive-table-link">
                                                        <img src="<c:out value='${customer.profilePicture.url}'/>" class="img-circle img-profile-thumbnail" title="<spring:theme code="text.asm.customerList.picture.alt" />"/>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:out value='${emulateCustomerUrl}${customer.uid}'/>" class="responsive-table-link default-pic" title="<spring:theme code="text.asm.customerList.picture.alt"/>">
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td data-th="NAME" >
                                            <a href="<c:out value='${emulateCustomerUrl}${customer.uid}'/>" class="responsive-table-link">
                                                <c:out value="${customer.name}"/>
                                            </a>
                                        </td>
                                        <td data-th="EMAIL/ID" >
                                           <c:out value="${customer.displayUid}"/>
                                        </td>
                                        <td data-th="PHONE #" >
                                            <c:choose>
                                                <c:when test="${(not empty customer.defaultAddress) && (not empty customer.defaultAddress.phone)}">
                                                    <c:out value="${customer.defaultAddress.phone}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:theme code="text.asm.customerList.phone.empty" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- Dynamically add values for additional columns of customerList. Skip invalidAddtionalColumnIndexes items if there is any.--%>
                                        <c:if test="${not empty customerListData}">
	                                        <c:forEach var="columnKey" items="${customerListData.additionalColumnsKeys}" varStatus="loop">
	                                        	<c:if test="${not empty columnKey && !ycommerce:arrayContainsInstance(fn:split(invalidAddtionalColumnIndexes, ';'), loop.index)}" >
													<c:set var="columnValue" value="${customer}" />
												    <c:forTokens items="${columnKey}" delims="." var="item">   
														<c:set var="columnValue" value="${columnValue[item]}" />
													</c:forTokens>
													<td data-th="<spring:theme code="text.asm.customerList.additionalColumns.${columnKey}" />" ><c:out value="${columnValue}"/></td>
												</c:if>
											</c:forEach>
										</c:if>
                                        <td>
                                            <c:choose>
                                                <c:when test="${null != customer.latestCartId}">
                                                    <spring:url value="/assisted-service/emulate/" var="cartEmulationUrl" htmlEscape="false">
                                                        <spring:param name="customerId" value="${customer.uid}"/>
                                                        <spring:param name="cartId" value="${customer.latestCartId}"/>
                                                    </spring:url>
                                                    <a href="${cartEmulationUrl}" class="responsive-table-link" title="<spring:theme code="text.asm.customerList.viewCarts"/>">
                                                        <div class="asm-full-card-icon asm-card-icon">
                                                            <span class="glyphicon glyphicon-shopping-cart"></span>
                                                        </div>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="asm-empty-card-icon asm-card-icon">
                                                        <span class="glyphicon glyphicon-shopping-cart" title="<spring:theme code="text.asm.customerList.carts.empty" />"></span>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="${emulateCustomerUrl}${ycommerce:encodeUrl(customer.uid)}&fwd=${frowardUrl}" class="responsive-table-link">
                                                <div class="nav-order-tools" title="<spring:theme code="text.asm.customerList.viewOrders" />"></div>
                                            </a>
                                        </td>
                                        <td>
                                             <a href="${emulateCustomerUrl}${ycommerce:encodeUrl(customer.uid)}&enable360View=true" title="<spring:theme code="text.asm.customerList.360view.alt" text="360 View of Customer" />">
                                                <div class="ASM-customer360_icon ASM-customer360_customer-list_icon nav-order-tools"></div>
                                             </a>
                                        </td>
                                    </ycommerce:testId>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</div>