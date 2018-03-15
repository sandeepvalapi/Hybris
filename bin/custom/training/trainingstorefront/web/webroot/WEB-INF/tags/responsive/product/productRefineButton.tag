<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="styleClass" required="true" type="java.lang.String" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<spring:theme code="search.nav.selectRefinements.title" var="selectRefinements"/>

<button class="${styleClass}" data-select-refinements-title="${selectRefinements}">
    <spring:theme code="search.nav.refine.button"/>
</button>