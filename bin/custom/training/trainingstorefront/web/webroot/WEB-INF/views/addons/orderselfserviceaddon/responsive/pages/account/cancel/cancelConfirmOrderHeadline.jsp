<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/order/${fn:escapeXml(orderData.code)}/cancel" var="orderCancelUrl" htmlEscape="false"/>
<common:headline url="${orderCancelUrl}" labelKey="text.account.cancel.confirm.order.title" labelArguments="${fn:escapeXml(orderData.code)}" />
