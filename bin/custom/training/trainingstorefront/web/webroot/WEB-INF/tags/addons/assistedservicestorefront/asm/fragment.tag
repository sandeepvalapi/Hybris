<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="id" required="true" type="java.lang.String" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>
<%@ attribute name="sectionId" required="false" type="java.lang.String" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${not empty title}">
    <div class="asm__customer360-subheadline">
        <spring:message code="${title}" text="${title}"/>
    </div>
</c:if>
<div class="asm__fragment" id="${id}">
   <div class='loader'>Loading..</div>
</div>

<script>
	loadCustomer360Fragment({method: 'GET', sectionId: '${sectionId}', fragmentId: '${id}', timeout: '${aif_ajax_timeout}'});
</script>
