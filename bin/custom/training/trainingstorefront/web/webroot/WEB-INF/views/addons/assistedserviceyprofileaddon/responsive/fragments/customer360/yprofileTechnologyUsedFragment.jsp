<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="name" value="${fn:split(fn:escapeXml(fragmentData.name), ' ')[0]}" scope="page"/>
<c:set var="technologyUsedDataList" value="${fragmentData.technologyUsedData}" scope="page"/>


<div class="asm__customer360-overview-info-uses">
    <div class="asm__customer360-overview-info-headline"><spring:theme code="${name}"/> uses</div>
    <c:choose>
        <c:when test="${empty technologyUsedDataList}">
            <spring:theme code="text.customer360.yprofile.devices.empty"/>
        </c:when>
        <c:otherwise>
            <table class="asm__customer360-overview-info-table">
                <tr>
                    <c:forEach items="${technologyUsedDataList}" var="index">
                        <td><div class="asm__customer360-overview-info-uses-icon asm__customer360-overview-info-uses-icon-${fn:escapeXml(index.device)}"></div></td>
                    </c:forEach>
                </tr>
                <tr>
                    <c:forEach items="${technologyUsedDataList}" var="index">
                        <td>
                            <div class="asm__customer360-overview-info-uses-name">${fn:replace(fn:escapeXml(index.device), '_',' ')}</div>
                        </td>
                    </c:forEach>
                </tr>
                <tr>
                    <c:forEach items="${technologyUsedDataList}" var="index">
                        <td>
                            <div class="asm__customer360-overview-info-uses-device">
                                <div>
                                    ${fn:replace(fn:escapeXml(index.operatingSystem), '_',' ')}
                                </div>
                                <div>
                                    ${fn:replace(fn:escapeXml(index.browser), '_',' ')}
                                </div>
                            </div>
                        </td>
                    </c:forEach>
                </tr>
                <tr>
                    <c:forEach items="${technologyUsedDataList}" var="index">
                        <td>
                            <div class="asm__customer360-overview-info-uses-browser asm__customer360-overview-info-uses-browser-${fn:escapeXml(index.browser)}"></div>
                        </td>
                    </c:forEach>
                </tr>
            </table>
        </c:otherwise>
    </c:choose>
</div>