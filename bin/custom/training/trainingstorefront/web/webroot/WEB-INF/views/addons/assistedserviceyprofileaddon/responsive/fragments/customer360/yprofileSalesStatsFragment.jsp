<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="name" value="${fn:split(fn:escapeXml(fragmentData.name), ' ')[0]}" scope="page"/>


<div class="asm__customer360-overview-info-is">
    <div class="asm__customer360-overview-info-headline"><spring:theme code="${name}"/> is</div>
    <div class="asm__customer360-overview-info-is-tags">
        <div class="asm__customer360-overview-info-is-tags-item">busy</div>
        <div class="asm__customer360-overview-info-is-tags-item">on-the-go</div>
        <div class="asm__customer360-overview-info-is-tags-item">early-adopting</div>
        <div class="asm__customer360-overview-info-is-tags-item">trendy</div>
        <div class="asm__customer360-overview-info-is-tags-item">frequent buyer</div>
        <div class="asm__customer360-overview-info-is-tags-item">tech savvy</div>
        <div class="asm__customer360-overview-info-is-tags-item">photographer</div>
    </div>
</div>