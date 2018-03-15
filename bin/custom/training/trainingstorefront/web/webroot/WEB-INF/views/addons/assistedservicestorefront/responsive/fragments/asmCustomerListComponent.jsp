<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>
<%@ taglib prefix="asm" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/asm"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:set var="baseUrl" value="/assisted-service-querying/listCustomers"/>
<spring:url value="${baseUrl}" var="customerListUrl" htmlEscape="false"><spring:param name="customerListUId" value=""/></spring:url>

<div class="customer-list-select-wrapper">
    <select id="sortOptions${top ? '1' : '2'}" name="sort" class="customer-list-select js-customer-list-select" data-search-url="${customerListUrl}">
        <c:forEach items="${availableLists}" var="availableCustomerLists">
            <option value="${availableCustomerLists.uid}"><c:out value="${availableCustomerLists.name}"/></option>
        </c:forEach>
    </select>
</div>