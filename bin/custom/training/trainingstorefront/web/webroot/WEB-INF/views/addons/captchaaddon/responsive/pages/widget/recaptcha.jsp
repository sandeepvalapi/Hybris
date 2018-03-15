<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${requestScope.captchaEnabledForCurrentStore}">
	<div id="g-recaptcha_incorrect"><spring:theme code="recaptcha.challenge.field.invalid"/></div>
	<div id="g-recaptcha_widget" class="g-recaptcha" data-sitekey="${requestScope.recaptchaPublicKey}"></div>
</c:if>
