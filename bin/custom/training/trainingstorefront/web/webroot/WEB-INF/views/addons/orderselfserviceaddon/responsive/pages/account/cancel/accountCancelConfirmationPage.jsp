<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/orderselfserviceaddon/responsive/order/cancel" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>



    <ycommerce:testId code="cancelOrder_section">
        <order:accountConfirmCancelOrder order="${orderData}"/>
    </ycommerce:testId>
