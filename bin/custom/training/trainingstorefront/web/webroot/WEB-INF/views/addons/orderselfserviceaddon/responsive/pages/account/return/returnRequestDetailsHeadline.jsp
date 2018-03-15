<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/returns" var="returnRequestDetailsUrl" htmlEscape="false"/>
<common:headline url="${returnRequestDetailsUrl}" labelKey="text.account.return.request.details.title" />
