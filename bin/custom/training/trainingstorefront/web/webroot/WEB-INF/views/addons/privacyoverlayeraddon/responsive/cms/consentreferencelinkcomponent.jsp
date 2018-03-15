<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:set var="consentReferenceUrl" value="${fn:replace(url, '{tenant}', TENANT)}" />
<c:set var="consentReferenceUrl" value="${fn:replace(consentReferenceUrl, '{consent-reference}', CONSENT_REFERENCE_ID)}" />
<c:set var="consentReferenceUrl" value="${fn:replace(consentReferenceUrl, '{consent-reference-token}', CONSENT_REFERENCE_TOKEN)}" />

<a href="${consentReferenceUrl}" target="_blank">${linkName}</a>