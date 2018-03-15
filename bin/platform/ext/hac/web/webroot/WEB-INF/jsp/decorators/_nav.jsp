<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${requestScope.initialized == true}">
	<hac:tabbed-menu subtabCssClass="span-24 last"/>
</c:if>