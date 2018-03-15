<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="consignment" required="true" type="de.hybris.platform.commercefacades.order.data.ConsignmentData" %>
<%@ attribute name="inProgress" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="well well-quinary well-xs">
	<ycommerce:testId code="orderDetail_itemHeader_section">			
		<div class="well-headline">
            <ycommerce:testId code="orderDetail_consignmentStatus_label">
                <spring:theme code="text.account.order.consignment.status.${consignment.statusDisplay}" />
            </ycommerce:testId>

			<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
				<span class="well-headline-sub">
                    <fmt:formatDate value="${consignment.statusDate}" dateStyle="medium" timeStyle="short" type="both"/>
                </span>
			</ycommerce:testId>
			
			<c:set var="consignment" scope="request" value="${consignment}" />
			<ycommerce:testId code="orderDetail_vendor_label">
				<c:forEach items="${actions}" var="action" varStatus="idx">
					<c:if test="${action.visible and action.uid eq 'VendorHomepageAction'}">
						<cms:component component="${action}" parentComponent="${component}" evaluateRestriction="true"/>
					</c:if>
				</c:forEach>
			</ycommerce:testId>
		</div>

        <div class="well-content col-sm-12 col-md-9">
            <div class="row">
                <div class="col-sm-12 col-md-9">
                    <c:choose>
                        <c:when test="${consignment.deliveryPointOfService ne null}">
                            <ycommerce:testId code="orderDetail_storeDetails_section">
                                <order:storeAddressItem deliveryPointOfService="${consignment.deliveryPointOfService}" inProgress="${inProgress}" statusDate="${consignment.statusDate}"/>
                            </ycommerce:testId>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-sm-6 col-md-4">
                                    <div class="order-ship-to">
                                        <ycommerce:testId code="orderDetail_deliveryAddress_section">
                                            <div class="label-order"><spring:theme code="text.account.order.shipto"/></div>
                                            <div class="value-order"><order:addressItem address="${orderData.deliveryAddress}"/></div>
                                        </ycommerce:testId>
                                    </div>
                                </div>

                                <div class="col-sm-6 col-md-4">
                                    <div class="order-shipping-method">
                                        <ycommerce:testId code="orderDetail_deliveryMethod_section">
                                            <order:deliveryMethodItem order="${orderData}"/>
                                        </ycommerce:testId>
                                    </div>
                                </div>
                                
                                <div class="col-sm-6 col-md-4">
                                    <div class="order-shipping-method">
                                        <c:set var="orderCode" scope="session" value="${order.code}" />
                                        <c:forEach items="${actions}" var="action">
											<c:if test="${action.visible and action.uid ne 'VendorHomepageAction' and consignment.statusDisplay eq 'shipped'}">
												<cms:component component="${action}" parentComponent="${component}" evaluateRestriction="true"/>
											</c:if>
										</c:forEach>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
	</ycommerce:testId>
</div>
	
<ul class="item__list">
    <li class="hidden-xs hidden-sm">
        <ul class="item__list--header">
            <li class="item__toggle"></li>
            <li class="item__image"></li>
            <li class="item__info"><spring:theme code="basket.page.item"/></li>
            <li class="item__price"><spring:theme code="basket.page.price"/></li>
            <li class="item__quantity"><spring:theme code="basket.page.qty"/></li>
            <li class="item__total--column"><spring:theme code="basket.page.total"/></li>
        </ul>
    </li>
	<ycommerce:testId code="orderDetail_itemBody_section">
		<c:forEach items="${consignment.entries}" var="entry" varStatus="loop">
			<order:orderEntryDetails orderEntry="${entry.orderEntry}" consignmentEntry="${entry}" order="${order}" itemIndex="${loop.index}"/>
		</c:forEach>
	</ycommerce:testId>
</ul>