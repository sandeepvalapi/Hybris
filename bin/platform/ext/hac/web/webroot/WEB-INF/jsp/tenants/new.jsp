<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
<title>Tenants - Create</title>
<link rel="stylesheet" href="<c:url value="/static/css/tenants/new_edit.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/tenants/new_edit.js"/>"></script>
</head>

<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft">
			<c:if test="${!flash.emptyMessage}">
				<div id="flash" data-severity="${flash.severity}" data-message="${flash.message}"></div>
			</c:if>
			
			<h2>Create New Tenant</h2>
			<!-- tenant form -->
			<c:url value="/tenants/create" var="url" scope="request"/>
			<c:set value="createNewTenantForm" var="commandName" scope="request"/>
			<c:set value="Create" var="tenantFormExecute" scope="request"/>
			<c:set value="false" var="tenantID_readonly" scope="request"/>
			<c:set value="false" var="Locale_readonly" scope="request"/>
			<c:set value="false" var="Timezone_readonly" scope="request"/>		
			<c:set value="post" var="formMethod" scope="request"/>	
			
			<jsp:include page="_tenantForm.jsp" />
		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Current database settings</h3>
			<div class="box">
				<div class="quiet">		
					<jsp:include page="_db_settings.jsp" />
				</div>
			</div>
			<h3 class="caps">Page description</h3>
			<div class="box">
				<div class="quiet">
					This page enables you to create a new slave tenant.
					<hr />
					<hac:note>
						<p>
							If you leave the DB URL empty, then all DB settings will use the system database. You can still overwrite this.
						</p>
						<p>
							If you will use <strong>{tenantID}</strong> placeholder as Table prefix, then Table prefix will use Tenant ID value.
						</p> 
					</hac:note>
				</div>
			</div>
		</div>
	</div>
</body>
</html>