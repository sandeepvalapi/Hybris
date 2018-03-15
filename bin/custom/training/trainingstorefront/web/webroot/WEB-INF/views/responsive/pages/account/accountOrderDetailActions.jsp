<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/my-account/orders" var="orderHistoryUrl"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-md-5 col-lg-4 pull-right">
        <ycommerce:testId code="orderDetails_backToOrderHistory_button">
            <div class="orderBackBtn">
                <button type="button" class="btn btn-default btn-block" data-back-to-orders="${orderHistoryUrl}">
                    <spring:theme code="text.account.orderDetails.backToOrderHistory"/>
                </button>
            </div>
        </ycommerce:testId>
    </div>
</div>