<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8"/>
    <sec:csrfMetaTags />
	<title>hybris administration console | <sitemesh:write property='title'/></title>
	<link rel="stylesheet" href="<c:url value="/static/css/blueprint.css"/>" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/plugins/fancy-type/screen.css"/>" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/plugins/buttons/screen.css"/>" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/custom-theme/jquery-ui.1.12.1.css"/>" type="text/css"
			media="screen, projection"/>

	<script type="text/javascript" src="<c:url value="/static/js/ba-debug.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/modernizr.custom.19066.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-3.0.0.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery-ui-1.12-1.custom.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery.validate.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/global.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/application.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery.cookie.js"/>"></script>

	<script type="text/javascript" src="<c:url value="/static/js/searchsuggest.js"/>"></script>

	<c:if test="${not empty statisticsPayload}">
		<script type="text/javascript" src="<c:url value="/static/js/ms.js"/>"></script>
	</c:if>

	<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" type="text/css" media="screen, projection"/>
	<link rel="shortcut icon" href="<c:url value="/static/img/favicon.png"/>"/>
	<link rel="icon" href="<c:url value="/static/img/favicon.png"/>" type="image/x-icon"/>
	<sitemesh:write property='head'/>
</head>
<body>
<!-- tenant badge, only shown for non-master tenants -->
<hac:tenantBadge/>
<div id="mainContainer" class="container" data-homeLink="<c:url value="/"/>" data-contextPath="<%= request.getContextPath() %>">
	<header class="span-24 last">
		<c:if test="${requestScope.initialized == true}">
			<div id="loginInfo">
                <spring:message
                        code="general.credentials.welcome"/> ${jalosession.user.name != null ? jalosession.user.name : jalosession.user.uid}
                <form method="post" action="<c:url value="/j_spring_security_logout"/>">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <input type="submit" value="logout">
                </form>
			</div>
		</c:if>
		<a href="<c:url value="/"/>"><img id="logo" src="<c:url value="/static/img/logo.png"/>" alt="logo"></a>

		<input id="searchsuggest" autofocus type="text" placeholder="Type here..."/>

		<div class="searchicon"></div>

		<jsp:include page="_nav.jsp"/>

	</header>
	<div id="notification" class="span-24 last" data-hurl="${statisticsPayload.homeURL}"
		  data-pl="${statisticsPayload.data}" data-lid="${statisticsPayload.password}">
		<div id="msg" class="msg">This is a beautiful notification message.</div>
	</div>

	<!-- begin page content -->
	<sitemesh:write property='body'/>
	<!-- end page content -->
</div>

<footer>
	<a id="about" data-aboutUrl="<c:url value="/platform/about"/>" href="#">&copy; SAP SE, 2016</a>
</footer>

<div id="aboutContainer"></div>
</body>
</html>