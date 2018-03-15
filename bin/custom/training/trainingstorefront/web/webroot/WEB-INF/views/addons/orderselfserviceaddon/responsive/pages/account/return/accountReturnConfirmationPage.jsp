<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/orderselfserviceaddon/responsive/return" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>



<ycommerce:testId code="returnOrder_section">
    <order:accountConfirmReturnOrder order="${orderData}"/>
</ycommerce:testId>
