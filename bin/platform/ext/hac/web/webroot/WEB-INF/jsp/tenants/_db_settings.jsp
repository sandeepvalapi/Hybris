<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="break-word">
	<dl>			
		<dt><spring:message code="platform.tenants.dbinfo.jndipool" /></dt>
		<dd><c:out value="${databaseInfoData.pool}"/></dd>
						
		<dt><spring:message code="platform.tenants.dbinfo.url" /></dt>
		<dd><c:out value="${databaseInfoData.url}" /></dd>				
		
		<dt><spring:message code="platform.tenants.dbinfo.name" /></dt>
		<dd><c:out value="${databaseInfoData.dbName}"/></dd>
		
		<dt><spring:message code="platform.tenants.dbinfo.user" /></dt>
		<dd><c:out value="${databaseInfoData.dbUser}" /></dd>
	</dl>
</div>