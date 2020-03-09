<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    var personalization_context = {
            "actions": [
                <c:forEach items="${personalizationActionList}" var="item" varStatus="iterator">
                {
                    "action_code": "${item.actionCode}",
                    "action_type": "${item.type}",
                    "variation_code": "${item.variationCode}",
                    "variation_name": "${item.variationName}",
                    "customization_code": "${item.customizationCode}",
                    "customization_name": "${item.customizationName}"
                }
                <c:if test="${!iterator.last}">, </c:if>
                </c:forEach>
            ],
            "segments": [
                <c:forEach items="${personalizationSegmentList}" var="item" varStatus="iterator">
                "${item.code}"
                <c:if test="${!iterator.last}">, </c:if>
                </c:forEach>
            ]
        }
    ;
</script>
