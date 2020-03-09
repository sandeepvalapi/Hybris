<%--[y] hybris Platform--%>

<%--Copyright (c) 2000-2018 SAP SE--%>
<%--All rights reserved.--%>

<%--This software is the confidential and proprietary information of SAP--%>
<%--Hybris ("Confidential Information"). You shall not disclose such--%>
<%--Confidential Information and shall use it only in accordance with the--%>
<%--terms of the license agreement you entered into with SAP Hybris.--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url var="cancelReturnUrl" value="${fn:escapeXml(fn:replace(url, '{returnCode}', returnCode))}" scope="page"/>

<c:if test="${returnCancellable}">
    <form:form action="${cancelReturnUrl}" id="cancelreturnForm" modelAttribute="cancelreturnForm"
               class="cancelreturnForm--ButtonWrapper">
            <button type="submit" class="btn btn-primary btn-block pull-right" id="cancelReturnButton">
                <spring:theme code="text.return.cancelreturnbutton"/>
            </button>
    </form:form>
</c:if>
