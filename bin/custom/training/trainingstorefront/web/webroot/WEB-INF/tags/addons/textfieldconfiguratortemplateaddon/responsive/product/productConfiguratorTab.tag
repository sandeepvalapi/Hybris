<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="configurations" required="true" type="java.util.ArrayList" %>
<%@ attribute name="readOnly" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="product__config">
    <c:forEach var="configuration" items="${configurations}">
        <div class="product__config-row">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <c:set var="configurationId" value="configurationsKeyValueMap[${fn:escapeXml(configuration.configuratorType)}][${fn:escapeXml(configuration.configurationLabel)}]"/>

                        <label for="${configurationId}">
                                ${fn:escapeXml(configuration.configurationLabel)}
                        </label>
                        <input <c:if test="${readOnly}"> readonly </c:if>
                               id="${configurationId}"
                               name="${configurationId}"
                               class="form-control" type="text" value="${fn:escapeXml(configuration.configurationValue)}">
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>