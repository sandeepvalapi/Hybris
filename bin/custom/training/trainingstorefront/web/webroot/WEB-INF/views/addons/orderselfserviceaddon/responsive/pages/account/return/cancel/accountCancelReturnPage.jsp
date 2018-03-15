<%--[y] hybris Platform--%>

<%--Copyright (c) 2000-2017 SAP SE--%>
<%--All rights reserved.--%>

<%--This software is the confidential and proprietary information of SAP--%>
<%--Hybris ("Confidential Information"). You shall not disclose such--%>
<%--Confidential Information and shall use it only in accordance with the--%>
<%--terms of the license agreement you entered into with SAP Hybris.--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/addons/orderselfserviceaddon/responsive/return/cancel" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>



<ycommerce:testId code="cancelReturn_section">
    <return:accountCancelReturn return="${returnRequestData}"/>
</ycommerce:testId>
