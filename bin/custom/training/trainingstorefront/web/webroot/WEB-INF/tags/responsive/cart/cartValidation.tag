<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty validationData}">
	<c:set var="productLinkValidationTextDecoration" value="style=\"text-decoration: underline\""/>
	<c:forEach items="${validationData}" var="modification">
		<div class="alert neutral">

				<c:url value="${modification.entry.product.url}" var="entryUrl"/>
				<spring:theme code="basket.validation.${modification.statusCode}"
					arguments="${fn:escapeXml(modification.entry.product.name)}###${entryUrl}###${modification.quantity}###
							${modification.quantityAdded}###${productLinkValidationTextDecoration}" argumentSeparator="###" htmlEscape="false"/><br>

		</div>
	</c:forEach>
</c:if>
