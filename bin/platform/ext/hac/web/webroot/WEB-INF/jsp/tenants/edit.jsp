<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
<title>Tenants - Edit - <c:out value="111${tenantID}" /></title>
<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />	
<link rel="stylesheet" href="<c:url value="/static/css/tenants/new_edit.css"/>" type="text/css" media="screen, projection" />
	
<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/tenants/new_edit.js"/>"></script>
</head>

<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft">
			<h2>Edit Tenant: <c:out value="${tenantID}" /></h2>
			<c:if test="${!flash.emptyMessage}">
				<div id="flash" data-severity="${flash.severity}" data-message="${flash.message}"></div>
			</c:if>
			
			<!-- tenant form -->
			<c:url value="/tenants/${tenantID}/update" var="url" scope="request"/>
			<c:set value="editTenantForm" var="commandName" scope="request"/>
			<c:set value="Update" var="tenantFormExecute" scope="request"/>			
			<c:set value="true" var="editMode" scope="request"/>
			<c:set value="put" var="formMethod" scope="request"/>
			<c:set value="${editTenantForm.master}" var="master" scope="request"/>
			
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
					This page enables you to perform the following administrative tasks on slave tenant:<br><br>
						<ul>		
							<li>Browsing</li>
							<li>Initializing</li>
						</ul>
					<hr/>
					<hac:note>
						<strong>Master tenant</strong><br/>
						If you are editing the master tenant, then all text boxes on the page are in read-only mode and you can only initialize the tenant.
					</hac:note>
					<hr/>
					<hac:note>
						<strong>Configured extensions</strong><br/>
						
						 To find out how to modify the offline extension configuration for given tenant, see <a href="${wikiMultiTenantSystem}">Multi-Tenant Systems</a>
						
					</hac:note>
					<hr/>
					<hac:note>
						<strong>Configured web contexts</strong><br/>
						
						 To find out how to configure a web context name for the web application in a specific tenant, see <a href="${wikiMultiTenantSystem}">Multi-Tenant Systems</a>
						
					</hac:note>
					
				</div>
			</div>
		</div>
	</div>
</body>
</html>