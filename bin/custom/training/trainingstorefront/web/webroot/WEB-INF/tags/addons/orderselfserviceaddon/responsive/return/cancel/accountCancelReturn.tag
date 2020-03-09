 <%--[y] hybris Platform--%>

 <%--Copyright (c) 2000-2018 SAP SE--%>
 <%--All rights reserved.--%>

 <%--This software is the confidential and proprietary information of SAP--%>
 <%--Hybris ("Confidential Information"). You shall not disclose such--%>
 <%--Confidential Information and shall use it only in accordance with the--%>
 <%--terms of the license agreement you entered into with SAP Hybris.--%>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="return" required="true" type="de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<form:form action="${request.contextPath}/my-account/returns/${fn:escapeXml(returnRequestData.code)}/cancel/submit"
           id="submitcancelreturnform"
           modelAttribute="returnRequestData"
           class="account-cancel-return">
    <ul class="item__list cart__list return__list">

        <li class="hidden-xs hidden-sm">
            <ul class="item__list--header">
                <li class="item__toggle"></li>
                <li class="item__image"></li>
                <li class="item__info"><spring:theme code="basket.page.item"/></li>
                <li class="item__price"><spring:theme code="basket.page.price"/></li>
                <li class="item__quantity"><spring:theme code="text.account.return.details.qty"/></li>
                <li class="item__price"><spring:theme code="text.account.return.details.refund.amount"/></li>
            </ul>
        </li>

        <c:forEach items="${returnRequestData.returnEntries}" var="entry" varStatus="loop">
            <li class="item__list--item">

                    <%-- chevron for multi-d products --%>
                <div class="hidden-xs hidden-sm item__toggle">
                    <c:if test="${entry.orderEntry.product.multidimensional}">
                        <div class="js-show-editable-grid" data-index="${loop.index}"
                             data-read-only-multid-grid="${not entry.updateable}">
                            <ycommerce:testId code="confirmCancel_product_updateQuantity">
                                <span class="glyphicon glyphicon-chevron-down"></span>
                            </ycommerce:testId>

                        </div>
                    </c:if>
                </div>
                    <%-- product image --%>
                <div class="item__image">
                    <product:productPrimaryImage product="${entry.orderEntry.product}" format="thumbnail"/>
                </div>

                    <%-- product name, code, promotions --%>
                <div class="item__info">
                    <ycommerce:testId code="return_product_name">
                        <span class="item__name">${fn:escapeXml(entry.orderEntry.product.name)}</span>
                    </ycommerce:testId>

                    <div class="item__code">${fn:escapeXml(entry.orderEntry.product.code)}</div>

                    <c:if test="${entry.orderEntry.product.configurable}">
                        <div class="item__configurations">
                            <c:forEach var="config" items="${entry.orderEntry.configurationInfos}">
                                <c:set var="style" value=""/>
                                <c:if test="${config.status eq errorStatus}">
                                    <c:set var="style" value="color:red"/>
                                </c:if>
                                <div class="item__configuration-entry row" style="${style}">
                                    <div class="item__configuration-name col-md-4">${fn:escapeXml(config.configurationLabel)}:</div>
                                    <div class="item__configuration-value col-md-8">${fn:escapeXml(config.configurationValue)}</div>
                                </div>
                            </c:forEach>
                        </div>
                        <c:if test="${not empty entry.orderEntry.configurationInfos}">
                            <div class="item__configurations-edit">
                                <spring:theme code="basket.page.change.configuration"/>
                            </div>
                        </c:if>
                    </c:if>
                </div>
                    <%-- price --%>
                <div class="item__price">
                    <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
                    <format:price priceData="${entry.orderEntry.basePrice}" displayFreeForZero="true"/>
                </div>

                    <%-- returned quantity --%>
                <div class="item__quantity">
                    <div class="item__quantity-wrapper">
                        <label class="visible-xs visible-sm"><spring:theme code="text.account.return.details.qty"/>:</label>
                        <span class="qtyValue"><c:out value="${entry.expectedQuantity}"/></span>
                    </div>
                </div>

                    <%-- refund amount --%>
                <div class="item__price">
                    <span class="visible-xs visible-sm"><spring:theme code="text.account.return.details.refund.amount"/>: </span>
                    <format:price priceData="${entry.refundAmount}" displayFreeForZero="true"/>
                </div>
            </li>

            <li>
                <div class="add-to-cart-order-form-wrap display-none"></div>
            </li>
        </c:forEach>

    </ul>

    <!--Button begins-->
    <div class="row">
        <div class="col-md-6 col-md-offset-6">
            <div class="return-actions">
                <div class="row">

                    <div class="col-sm-6 col-sm-offset-6">
                        <button type="submit" class="btn btn-primary btn-block" id="submitcancelreturnformbutton">
                            <spring:theme code="text.account.return.cancel.submit"/>
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form:form>
