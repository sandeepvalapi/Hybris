<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchUrl" required="true" %>
<%@ attribute name="searchPageData" required="true"
              type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ attribute name="top" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showTopTotals" required="false" type="java.lang.Boolean" %>
<%@ attribute name="supportShowAll" required="true" type="java.lang.Boolean" %>
<%@ attribute name="supportShowPaged" required="true" type="java.lang.Boolean" %>
<%@ attribute name="additionalParams" required="false" type="java.util.HashMap" %>
<%@ attribute name="msgKey" required="false" %>
<%@ attribute name="showCurrentPageInfo" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideRefineButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="numberPagesShown" required="false" type="java.lang.Integer" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/nav/pagination" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="themeMsgKey" value="${not empty msgKey ? msgKey : 'search.page'}"/>
<c:set var="showCurrPage" value="${not empty showCurrentPageInfo ? showCurrentPageInfo : false}"/>
<c:set var="hideRefBtn" value="${hideRefineButton ? true : false}"/>
<c:set var="showTotals" value="${empty showTopTotals ? true : showTopTotals}"/>
<c:set var="queryPresent" value="${not empty query}"/>

<c:if test="${searchPageData.pagination.totalNumberOfResults == 0 && top && showTotals && !queryPresent}">
    <div class="paginationBar top clearfix">
        <ycommerce:testId code="searchResults_productsFound_label">
            <div class="totalResults"><spring:theme code="${themeMsgKey}.totalResults"
                                                    arguments="${searchPageData.pagination.totalNumberOfResults}"/></div>
        </ycommerce:testId>
    </div>
</c:if>

<c:if test="${searchPageData.pagination.totalNumberOfResults > 0 || queryPresent}">
    <div class="pagination-bar ${(top)?"top":"bottom"}">
        <div class="pagination-toolbar">
            <c:if test="${not empty searchPageData.sorts}">
                <div class="helper clearfix hidden-md hidden-lg"></div>
                <div class="sort-refine-bar y-toolbar__wrapper">
                    <div class="y-toolbar">
                        <div class="form-group">
                            <form id="sortForm${top ? '1' : '2'}" name="sortForm${top ? '1' : '2'}" method="get"
                                  action="#" class="ASM-sort__wrapper">
                                <div class="ASM-toolbar__search">
                                    <c:if test="${customerListData.searchBoxEnabled}">
                                        <spring:theme code="text.customerlist.${defaultList}.searchbox.label" var="customerListSearchboxLabel"/>
                                        <div class="input-group ASM-toolbar__input">
                                            <input id="ASM_customer-list-queryInput" type="text" name="query" placeholder="${customerListSearchboxLabel}" value="${fn:escapeXml(query)}" />
                                            <span class="input-group-btn">
                                               <button id="ASM_customer-list-searchButton" class="btn btn-link ASM_customer-list-searchButton" type="submit" >
                                                   <span class="glyphicon glyphicon-search"></span>
                                               </button>
                                            </span>
                                        </div>
                                    </c:if>
                                    <c:catch var="errorException">
                                        <spring:eval expression="searchPageData.currentQuery.query"
                                                     var="dummyVar"/><%-- This will throw an exception is it is not supported --%>
                                        <input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>
                                    </c:catch>

                                    <c:if test="${supportShowAll}">
                                        <ycommerce:testId code="searchResults_showAll_link">
                                            <input type="hidden" name="show" value="Page"/>
                                        </ycommerce:testId>
                                    </c:if>
                                    <c:if test="${supportShowPaged}">
                                        <ycommerce:testId code="searchResults_showPage_link">
                                            <input type="hidden" name="show" value="All"/>
                                        </ycommerce:testId>
                                    </c:if>
                                    <c:if test="${not empty additionalParams}">
                                        <c:forEach items="${additionalParams}" var="entry">
                                            <input type="hidden" name="${entry.key}" value="${entry.value}"/>
                                        </c:forEach>
                                    </c:if>
                                </div>
                                <div class="y-toolbar__item y-toolbar__item-sort y-toolbar__dropdown">
                                    <c:forEach items="${searchPageData.sorts}" var="sort">
                                        <c:if test="${sort.selected}">
                                            <c:set var="selectedSort" value="${sort.code}" />
                                            <c:choose>
                                                <c:when test="${not empty sort.name}">
                                                    <c:set var="outputEncodedselectedSortLabel" value="${fn:escapeXml(sort.name)}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:theme var="outputEncodedselectedSortLabel" code="${themeMsgKey}.sort.${sort.code}" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:forEach>
                                    <input type="hidden" id="sortOptions${top ? '1' : '2'}" name="sort" class="form-control" value="${selectedSort}">
                                    <a href="#" class="y-toolbar__btn y-toolbar__btn--caption js-dropdown" role="button" id="ASM-dropdown">
                                        <span class="glyphicon glyphicon-sort-by-attributes-alt" aria-hidden="true" title="sort"></span><strong>${outputEncodedselectedSortLabel}</strong>
                                        <span class="glyphicon glyphicon-chevron-down y-toolbar__dropdown__icon" aria-hidden="true"></span>
                                    </a>
                                    <ul id="ASM_customer-list-sortOptions" class="dropdown-menu" aria-labelledby="ASM-dropdown">
                                        <c:forEach items="${searchPageData.sorts}" var="sort">
                                            <li data-value="${sort.code}" class="sortOption ${sort.selected?'selected' : ''}">
                                                <a href="#">
                                                    <c:choose>
                                                        <c:when test="${not empty sort.name}">
                                                            ${fn:escapeXml(sort.name)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <spring:theme code="${themeMsgKey}.sort.${sort.code}"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </form>
                        </div>

                        <div class="y-toolbar__right">
                            <nav class="y-toolbar__item">
                                <c:if test="${top && showTotals}">
                            <span class="pagination-bar-results">
                                <ycommerce:testId code="searchResults_productsFound_label">
                                    <c:choose>
                                        <c:when test="${showCurrPage}">
                                            <c:choose>
                                                <c:when test="${searchPageData.pagination.totalNumberOfResults == 1}">
                                                    <spring:theme code="${themeMsgKey}.totalResultsSingleCustomer"/>
                                                </c:when>
                                                <c:when test="${searchPageData.pagination.numberOfPages <= 1}">
                                                    <spring:theme code="${themeMsgKey}.totalResultsSinglePage"
                                                                  arguments="${searchPageData.pagination.totalNumberOfResults}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="currentPageItems"
                                                           value="${(searchPageData.pagination.currentPage + 1) * searchPageData.pagination.pageSize}"/>
                                                    <c:set var="upTo"
                                                           value="${(currentPageItems > searchPageData.pagination.totalNumberOfResults ? searchPageData.pagination.totalNumberOfResults : currentPageItems)}"/>
                                                    <c:set var="currentPage"
                                                           value="${searchPageData.pagination.currentPage * searchPageData.pagination.pageSize + 1} - ${upTo}"/>
                                                    <spring:theme code="${themeMsgKey}.totalResultsCurrPage"
                                                                  arguments="${currentPage},${searchPageData.pagination.totalNumberOfResults}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${searchPageData.pagination.totalNumberOfResults == 1}">
                                                    <spring:theme code="${themeMsgKey}.totalResultsSingleCustomer"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:theme code="${themeMsgKey}.totalResultsSinglePage"
                                                                  arguments="${searchPageData.pagination.totalNumberOfResults}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </ycommerce:testId>
                            </span>
                                </c:if>
                                <pagination:pageSelectionPagination searchUrl="${searchUrl}" searchPageData="${searchPageData}"
                                                                    numberPagesShown="${numberPagesShown}"
                                                                    themeMsgKey="${themeMsgKey}"/>
                            </nav>
                        </div>

                    </div>
                    <div class="row">
                        <c:if test="${not hideRefBtn}">
                            <div class="col-xs-12 col-sm-2 col-md-4 hidden-md hidden-lg">
                                <ycommerce:testId code="searchResults_refine_button">
                                    <product:productRefineButton styleClass="btn btn-default js-show-facets"/>
                                </ycommerce:testId>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</c:if>