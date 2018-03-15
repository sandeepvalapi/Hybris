<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
        ~ /*
        ~  * [y] hybris Platform
        ~  *
        ~  * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
        ~  *
        ~  * This software is the confidential and proprietary information of SAP
        ~  * ("Confidential Information"). You shall not disclose such Confidential
        ~  * Information and shall use it only in accordance with the terms of the
        ~  * license agreement you entered into with SAP.
        ~ */
        --%>

<%--
  Product list without add to cart functionality. Used to pick up products for futher procesesing,
  e.g. to replace selection in particular bundle.
--%>
<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>

<div class="product__listing product__list">
    <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
        <product:productSelectorItem product="${product}"/>
    </c:forEach>
</div>

<nav:pagination top="false" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>
