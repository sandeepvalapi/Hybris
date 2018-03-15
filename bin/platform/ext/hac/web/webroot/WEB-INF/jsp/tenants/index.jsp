<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
<title>Tenants</title>
<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />	
<link rel="stylesheet" href="<c:url value="/static/css/tenants/index.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/tenants/index.js"/>"></script>

</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
				
		<div class="marginLeft">
			<h2>Tenants Overview</h2>
			<c:if test="${!flash.emptyMessage}">
				<div id="flash" data-severity="${flash.severity}" data-message="${flash.message}"></div>
			</c:if>
			<c:choose>
				<c:when test="${master}">
					<div id="tenantsOverview">	
						<table id="existingTenants" >
							<thead>
								<tr>
									<th>Tenant ID</th>
									<th>Active</th>
									<th>Driver</th>
									<th>Actions</th>
								</tr>
							</thead>	
							<tbody>			
						      <c:forEach var="tenant" items="${tenants}">			      
						        <tr>
									<td><c:out value="${tenant.tenantID}" /></td>
									<td>
										<hac:boolean state="${tenant.initialized}"/>
									</td>
									<td><c:out value="${tenant.dbDriver}" /></td>
									<td>
										<c:if test="${empty tenant.ctxEnabled}" >								
											<button class="activate"  ${tenant.ctxEnabled} data-href="<c:out value="${tenant.ctx}/tenants"/>">Activate</button>
										</c:if>
										<button class="edit" data-href="<c:url value="/tenants/${tenant.tenantID}/edit"/>">View</button>
									</td>							
							    </tr>						   
						      </c:forEach>
						      </tbody>
						</table>				
					</div>
					<br/>
				</c:when>							
				<c:otherwise>
					<p>Tenants are managed by master tenant only - please activate the master tenant system to show the existing slave tenants...</p>
				</c:otherwise>
			</c:choose>			
			<br/>
	
		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Page description</h3>
			<div class="box">
				<div class="quiet">
					This page provides the list of existing slave tenants and the master tenant. Here you can also activate a given tenant or view the list of active extensions for a given tenant.</br></br>
					
					Creating new tenants is possible only offline. See <a href="${wikiMultiTenantSystem}" target="_blank" class="quiet">Multi-Tenant Systems document</a> for more information.</br></br>
					
				</div>
			</div>
			<h3 class="caps">
				See also in the hybris Wiki
			</h3>
			<div class="box">
				<div class="quiet">
				
					<ul>
						 <li> <a href="${wikiSpringInHybrisCommerceSuite}" target="_blank" class="quiet">Spring in hybris Commerce Suite</a> </li> 
					</ul>
				</div>
			</div>		
		</div>
	</div>
</body>
</html>