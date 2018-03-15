<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title>System</title>
<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="<c:url value="/static/css/platform/system.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/platform/system.js"/>"></script>
</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft marginBottom">
			<h2>System Information</h2>
			<p></p>

			<div id="tabsNoSidebar">
				<ul>
					<li><a href="#tabs-1">System environment</a>
					</li>
					<li><a href="#tabs-2">System properties</a>
					</li>
				</ul>
				<div id="tabs-1">
					<div class="marginBottom">
						<table id="systemEnvironment">
							<thead>
								<tr>
									<th>Key</th>
									<th>Value</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="prop" items="${systemEnvironment}">
									<tr>
										<td>${prop.key}</td>
										<td>${prop.value}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div id="tabs-2">
					<div class="marginBottom">
						<table id="systemProperties">
							<thead>
								<tr>
									<th>Key</th>
									<th>Value</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="prop" items="${systemProperties}">
									<tr>
										<td>${prop.key}</td>
										<td>${prop.value}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>

		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
		<h3 class="caps">
			Page description
		</h3>
			<div class="box">
				<div class="quiet">
					This page provides detailed information about the running system: <br><br>
						<ul>
							<li><strong>System environment</strong> tab: Information on threads and an operating system</li>
							<li><strong>System properties</strong> tab: Information about running the Java Virtual Machine and system variables</li>
						</ul>
				</div>
			</div>		
		</div>
	</div>
</body>
</html>

