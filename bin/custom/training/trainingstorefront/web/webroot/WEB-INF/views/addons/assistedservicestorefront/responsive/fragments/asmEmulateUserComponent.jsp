<%@ taglib prefix="asm" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/asm"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- emulate --%>
<c:url value="/assisted-service/personify-customer" var="personifyActionUrl" />
<asm:emulateform actionNameKey="asm.emulate.start" actionNameKeyEnding="asm.emulate.start.ending" action="${personifyActionUrl}" disabledButton="true"/>