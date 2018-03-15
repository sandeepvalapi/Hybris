<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Cluster</title>

	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/cluster.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft">
					<h2>Cluster</h2>
						<table id="clusternodes" data-updateNodesUrl="<c:url value="/monitoring/cluster/data"/>">
							<thead>
								<tr>
									<th>IP</th>
									<th>Configured ID</th>
									<th>Dynamic ID</th>
									<th>Method</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>						
					
					
					<button id="ping" data-url="<c:url value="/monitoring/cluster/ping"/>">Ping nodes</button>
					
					<br/>
					<h3>Configuration</h3>
					<dl>
					  <dt>Cluster mode enabled</dt>
					    <dd id="clusteringEnabled"></dd>
					  <dt>Cluster island ID</dt>
					    <dd id="clusterIslandId"></dd>
					  <dt>Cluster node ID</dt>
					    <dd><span id="clusterNodeId"></span> (dynamic: <span id="dynamicClusterNodeId"></span>)</dd>					    
					</dl>	
					<div id="methodWrapper"></div>				
				</div>
					
			</div>
			<div class="span-6 last" id="sidebar">
				<div class="prepend-top" id="recent-reviews">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides a list of available cluster nodes.
							<hr />
							<hac:note>
								Refreshing the list can take up to 30 seconds.
							</hac:note>
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiCluster}" target="_blank" class="quiet" >Cluster - Technical Guide</a> </li>
						</ul>
					</div>
				</div>
			</div>		
	</body>
</html>

