<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>JMX MBeans</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />		
	<link rel="stylesheet" href="<c:url value="/static/css/onoff.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/monitoring/jmx.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/jmx.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft">
					<h2>JMX MBeans</h2>

					<div id="tableWrapper">					
						<table id="beans"  data-toggleUrl="<c:url value="/monitoring/jmx/toggle/"/>" data-url="<c:url value="/monitoring/jmx/data"/>">
							<thead>
								<tr>
									<th>MBean ID</th>
									<th>Domain</th>
									<th>Object name</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>	
					</div>				
				
				</div>
			</div>
			<div class="span-6 last" id="sidebar">
				<div class="prepend-top">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides a list of all available JMX MBeans. You can unregister them by changing their status.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiJmxBeans}" target="_blank" class="quiet" >hybris JMX MBeans Reference</a> </li>
						</ul>
					</div>
				</div>
			</div>		
	</body>
</html>

