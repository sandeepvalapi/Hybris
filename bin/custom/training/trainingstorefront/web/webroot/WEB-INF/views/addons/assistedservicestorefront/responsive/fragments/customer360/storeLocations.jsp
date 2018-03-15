<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/store" %>

<store:storeListForm />

<script>
	ASM.storefinder.autoLoad("${fragmentData}");
	<c:choose>
		<c:when test="${not empty fragmentData}">
			ASM.storefinder.getInitStoreData("${fragmentData}", "", "");
		</c:when>
		<c:otherwise>
			ASM.storefinder.getInitStoreData(null,ASM.storefinder.coords.latitude, ASM.storefinder.coords.longitude);
		</c:otherwise>
	</c:choose>
</script>