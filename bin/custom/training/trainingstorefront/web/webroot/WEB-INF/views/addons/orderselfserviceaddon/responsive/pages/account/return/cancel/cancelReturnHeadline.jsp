<%--[y] hybris Platform--%>

<%--Copyright (c) 2000-2018 SAP SE--%>
<%--All rights reserved.--%>

<%--This software is the confidential and proprietary information of SAP--%>
<%--Hybris ("Confidential Information"). You shall not disclose such--%>
<%--Confidential Information and shall use it only in accordance with the--%>
<%--terms of the license agreement you entered into with SAP Hybris.--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/returns/${fn:escapeXml(returnRequestData.code)}" var="cancelReturnUrl" htmlEscape="false"/>
<common:headline url="${cancelReturnUrl}" labelKey="text.account.return.cancel.title" labelArguments="${fn:escapeXml(returnRequestData.code)}" />
