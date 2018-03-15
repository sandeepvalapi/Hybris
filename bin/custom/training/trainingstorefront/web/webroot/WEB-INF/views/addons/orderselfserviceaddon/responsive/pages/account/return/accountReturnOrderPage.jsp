<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/addons/orderselfserviceaddon/responsive/return" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<ycommerce:testId code="returnOrder_section">
    <return:accountReturnOrder order="${orderData}"/>
</ycommerce:testId>


