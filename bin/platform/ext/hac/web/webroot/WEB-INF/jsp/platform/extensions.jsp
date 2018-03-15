<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Extensions</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
		
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/platform/extensions.js"/>"></script>	
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft" id="inner">
					<h2>
						Extensions
					</h2>	
					<table id="extensions" data-dependencyurl="<c:url value="/platform/extensions/dependency"/>">
						<thead>
							<tr>
								<th>Name</th>
								<th>Version</th>
								<th>Core</th>
								<th>HMC</th>
								<th>Web</th>
							</tr>
						</thead>
						<tbody>
					      <c:forEach var="extension" items="${extensions}">
					        <tr>
					          <td title="${extension.properties}" class="extensionName pointer" data-extensionName="${extension.name}">
					          	${extension.name} <c:if test="${extension.deprecated}"> &nbsp; (deprecated) </c:if>
					          </td>
					          <td>${extension.version}</td>
					          <td><hac:boolean state="${extension.coreModule}" /></td>
					          <td><hac:boolean state="${extension.hmcModule}" /></td>

					         			<c:choose>
											<c:when test="${extension.missingContextName}">
												<td><label for="${extension.webRoot}" class="error" style="float: left;">Missing configuration for this context in current tenant</label></td>
											</c:when>
											<c:otherwise>								
												<td><a href="${extension.webRoot}">${extension.webRoot}</a></td>
											</c:otherwise>
										</c:choose>
					          
					          
					        </tr>
					      </c:forEach>					
						</tbody>
					</table>
				</div>			
			</div>
			<div class="span-6 last" id="sidebar">
				<div class="prepend-top" id="recent-reviews">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
							This page provides the list of all installed extensions of the hybris Multichannel Suite. You can see following details about each extension:<br><br>
							<ul>
								<li>Version</li>
								<li>Contained extension module</li>
								<li>Webroot if web extension module is available</li>
								<li>After clicking extension name, a list of extensions it depends on displays.</li>
							</ul>
						</div>
					</div>
					
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
							 <li> <a href="${wikiHacExtensions}" target="_blank" class="quiet">About Extensions</a> </li>
							</ul>  
						</div>
					</div>					
				</div>
			</div>

			<div id="dependenciesDialog"></div>
	</body>
</html>

