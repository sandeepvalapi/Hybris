<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="resetBtnId" required="false" type="java.lang.String"%>
<%@ attribute name="resetBtnClass" required="false" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<button id="${(empty resetBtnId) ? 'resetBtn' : resetBtnId}" type="button" class="${(empty resetBtnClass) ? 'reset-btn' : resetBtnClass}">
    <spring:theme code="text.quick.order.reset.form" />
</button>