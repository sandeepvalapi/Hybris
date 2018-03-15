<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/order/${orderData.code}/returns" var="orderReturnUrl" htmlEscape="false"/>
<common:headline url="${orderReturnUrl}" labelKey="text.account.return.confirm.order.title" labelArguments="${fn:escapeXml(orderData.code)}" />
