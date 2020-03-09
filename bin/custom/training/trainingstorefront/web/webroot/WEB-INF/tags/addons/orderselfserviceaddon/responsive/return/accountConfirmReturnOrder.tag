<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="orderEntryReturnForm" type="de.hybris.platform.orderselfserviceaddon.forms.OrderEntryReturnForm " %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<form:form action="${request.contextPath}/my-account/order/${fn:escapeXml(order.code)}/returns/submit"
           id="submitreturnorderform"
           modelAttribute="orderEntryReturnForm"
           class="account-confirm-return-order">
    <ul class="item__list cart__list return-order__list">
        <li class="hidden-xs hidden-sm">
            <ul class="item__list--header">
                <li class="item__toggle"></li>
                <li class="item__image"></li>
                <li class="item__info"><spring:theme code="basket.page.item"/></li>
                <li class="item__deliverymode"><spring:theme code="text.account.cancel.delivery"/></li>
                <li class="item__price"><spring:theme code="basket.page.price"/></li>
                <li class="item__quantity"><spring:theme code="text.account.return.qty"/></li>
                <li class="item__total--column"><spring:theme code="basket.page.total"/></li>
            </ul>
        </li>
        <c:forEach items="${order.entries}" var="entry" varStatus="loop">
            <c:if test="${!entry.product.multidimensional && orderEntryReturnForm.returnEntryQuantityMap[entry.entryNumber] > 0 }">
                <c:if test="${not empty entry.statusSummaryMap}">
                    <c:set var="errorCount" value="${entry.statusSummaryMap.get(errorStatus)}"/>
                    <c:if test="${not empty errorCount && errorCount > 0}">
                        <div class="notification has-error">
                            <spring:theme code="basket.error.invalid.configuration" arguments="${errorCount}"/>
                            <spring:theme code="basket.error.invalid.configuration.edit"/>
                        </div>
                    </c:if>
                </c:if>

                <li class="item__list--item">

                        <%-- chevron for multi-d products --%>
                    <div class="hidden-xs hidden-sm item__toggle">
                        <c:if test="${entry.product.multidimensional}">
                            <div class="js-show-editable-grid" data-index="${loop.index}"
                                 data-read-only-multid-grid="${not entry.updateable}">
                                <ycommerce:testId code="confirmReturn_product_updateQuantity">
                                    <span class="glyphicon glyphicon-chevron-down"></span>
                                </ycommerce:testId>

                            </div>
                        </c:if>
                    </div>
                        <%-- product image --%>
                    <div class="item__image">
                        <product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
                    </div>

                        <%-- product name, code, promotions --%>
                    <div class="item__info">
                        <ycommerce:testId code="confirmReturn_product_name">
                            <span class="item__name">${fn:escapeXml(entry.product.name)}</span>
                        </ycommerce:testId>

                        <div class="item__code">${fn:escapeXml(entry.product.code)}</div>

                        <c:if test="${entry.product.configurable}">
                            <div class="item__configurations">
                                <c:forEach var="config" items="${entry.configurationInfos}">
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
                            <c:if test="${not empty entry.configurationInfos}">
                                <div class="item__configurations-edit">
                                    <spring:theme code="basket.page.change.configuration"/>
                                </div>
                            </c:if>
                        </c:if>
                    </div>

                        <%-- delivery mode --%>
                    <div class="item__delivery">
                        <span class="visible-xs visible-sm"><spring:theme code="text.account.cancel.delivery"/>: </span>
                        <c:choose>
                            <c:when test="${entry.deliveryPointOfService eq null}">
                                <spring:theme code="text.account.cancel.standard.shipping"/>
                            </c:when>
                            <c:otherwise>
                                        <span><spring:theme
                                                code="text.account.cancel.pickup.from"/><span></span>${fn:escapeXml(entry.deliveryPointOfService.name)}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                        <%-- price --%>
                    <div class="item__price">
                        <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
                        <format:price priceData="${entry.basePrice}" displayFreeForZero="true"/>
                    </div>

                        <%-- quantity --%>
                    <div class="item__quantity hidden-xs hidden-sm">
                    <span class="qtyValue">
                        <c:set var="key" value="${entry.entryNumber}"/>
                        <form:input class="form-control js-update-entry-quantity-input" type="text" size="1"
                                    path="returnEntryQuantityMap[${key}]" readonly="true"/>
                    </span>
                    </div>

                    <div class="item__quantity visible-xs visible-sm">
                        <div class="item__quantity-wrapper">
                            <label class=""><spring:theme code="basket.page.qty"/>:</label>
                     <span class="qtyValue">
                            <c:set var="key" value="${entry.entryNumber}"/>
                            <form:input class="form-control js-update-entry-quantity-input" type="text" size="1"
                                        path="returnEntryQuantityMap[${key}]" readonly="true"/>
                        </span>
                        </div>
                        <div class="item__total--wrapper item__total">
                            <span class="item__total--label"><spring:theme code="basket.page.total"/>: </span>
                            <format:price priceData="${entry.returnedItemsPrice}" displayFreeForZero="true"/>
                        </div>
                    </div>

                        <%-- total --%>

                    <div class="item__total js-item-total hidden-xs hidden-sm">
                        <format:price priceData="${entry.returnedItemsPrice}" displayFreeForZero="true"/>
                    </div>
                        <%-- </ycommerce:testId> --%>
                </li>

                <li>
                    <div class="add-to-cart-order-form-wrap display-none"></div>
                </li>
            </c:if>

            <c:if test="${entry.product.multidimensional}">
                <c:forEach items="${entry.entries}" var="nentry" varStatus="nloop">

                    <c:if test="${orderEntryReturnForm.returnEntryQuantityMap[nentry.entryNumber] > 0}">

                        <li class="item__list--item">

                                <%-- chevron for multi-d products --%>
                            <div class="hidden-xs hidden-sm item__toggle">
                                <c:if test="${nentry.product.multidimensional}">
                                    <div class="js-show-editable-grid" data-index="${nloop.index}"
                                         data-read-only-multid-grid="${not nentry.updateable}">
                                        <ycommerce:testId code="confirmReturn_product_updateQuantity">
                                            <span class="glyphicon glyphicon-chevron-down"></span>
                                        </ycommerce:testId>

                                    </div>
                                </c:if>
                            </div>


                                <%-- product image --%>
                            <div class="item__image">
                                <product:productPrimaryImage product="${nentry.product}" format="thumbnail"/>
                            </div>

                                <%-- product name, code, promotions --%>
                            <div class="item__info">
                                <ycommerce:testId code="confirmReturn_product_name">
                                    <span class="item__name">${fn:escapeXml(nentry.product.name)}</span>
                                </ycommerce:testId>

                                <div class="item__code">${fn:escapeXml(nentry.product.code)}</div>

                                <c:if test="${nentry.product.configurable}">
                                    <div class="item__configurations">
                                        <c:forEach var="config" items="${nentry.configurationInfos}">
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
                                    <c:if test="${not empty nentry.configurationInfos}">
                                        <div class="item__configurations-edit">
                                            <spring:theme code="basket.page.change.configuration"/>
                                        </div>
                                    </c:if>
                                </c:if>
                            </div>

                                <%-- delivery mode --%>
                            <div class="item__delivery">
                                <span class="visible-xs visible-sm"><spring:theme code="text.account.cancel.delivery"/>: </span>
                                <c:choose>
                                    <c:when test="${entry.deliveryPointOfService eq null}">
                                        <spring:theme code="text.account.cancel.standard.shipping"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span><spring:theme
                                                code="text.account.cancel.pickup.from"/><span></span>${fn:escapeXml(entry.deliveryPointOfService.name)}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                                <%-- price --%>
                            <div class="item__price">
                                <span class="visible-xs visible-sm"><spring:theme code="basket.page.itemPrice"/>: </span>
                                <format:price priceData="${nentry.basePrice}" displayFreeForZero="true"/>
                            </div>

                                <%-- quantity --%>
                            <div class="item__quantity hidden-xs hidden-sm">
                    <span class="qtyValue">
                        <c:set var="key" value="${nentry.entryNumber}"/>
                        <form:input class="form-control js-update-entry-quantity-input" type="text" size="1"
                                    path="returnEntryQuantityMap[${key}]" readonly="true"/>
                    </span>
                            </div>

                            <div class="item__quantity visible-xs visible-sm">
                                <div class="item__quantity-wrapper">
                                    <label class=""><spring:theme code="basket.page.qty"/>:</label>
                           <span class="qtyValue">
                            <c:set var="key" value="${nentry.entryNumber}"/>
                            <form:input class="form-control js-update-entry-quantity-input" type="text" size="1"
                                        path="returnEntryQuantityMap[${key}]" readonly="true"/>
                        </span>
                                </div>
                                <div class="item__total--wrapper item__total">
                                    <span class="item__total--label"><spring:theme code="basket.page.total"/>: </span>
                                    <format:price priceData="${nentry.returnedItemsPrice}" displayFreeForZero="true"/>
                                </div>
                            </div>

                                <%-- total --%>
                            <div class="item__total js-item-total hidden-xs hidden-sm">
                                <format:price priceData="${nentry.returnedItemsPrice}" displayFreeForZero="true"/>
                            </div>
                                <%-- </ycommerce:testId> --%>
                        </li>

                        <li>
                            <div class="add-to-cart-order-form-wrap display-none"></div>
                        </li>
                    </c:if>
                </c:forEach>
            </c:if>
        </c:forEach>

    </ul>

    <!--Button begins-->
    <div class="row">
        <div class="col-md-6 col-md-offset-6">
            <div class="return-actions">
                <div class="row">

                    <div class="col-sm-6 col-sm-offset-6">
                        <button type="submit" class="btn btn-primary btn-block" id="submitreturnorderformvbutton">
                            <spring:theme code="text.account.return.submit"/>
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form:form>
