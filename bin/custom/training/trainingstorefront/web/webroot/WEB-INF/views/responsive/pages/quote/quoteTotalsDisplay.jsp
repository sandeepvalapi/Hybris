<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/responsive/quote" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    ~ /*
    ~  * [y] hybris Platform
    ~  *
    ~  * Copyright (c) 2000-2017 SAP SE or an SAP affiliate company.
    ~  * All rights reserved.
    ~  *
    ~  * This software is the confidential and proprietary information of SAP
    ~  * ("Confidential Information"). You shall not disclose such Confidential
    ~  * Information and shall use it only in accordance with the terms of the
    ~  * license agreement you entered into with SAP.
    ~  *
    ~  */
--%>

<c:if test="${not empty quoteData}">
    <div class="account-orderdetail">
        <div class="account-orderdetail__footer">
            <div class="row">
                <div class="col-sm-6 col-md-7 col-lg-8">
                    <order:appliedVouchers order="${quoteData}" />
                    <order:receivedPromotions order="${quoteData}" />
                </div>
                <div class="col-sm-6 col-md-5 col-lg-4">
                    <quote:quoteTotals order="${quoteData}" />
                </div>
            </div>
        </div>
    </div>
</c:if>