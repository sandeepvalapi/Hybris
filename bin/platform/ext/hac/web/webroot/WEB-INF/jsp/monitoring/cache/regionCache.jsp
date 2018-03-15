<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Cache</title>

<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection">
<link rel="stylesheet" href="<c:url value="/static/css/custom-theme/jquery-ui.1.12.1.css"/>" type="text/css" media="screen, projection"/>

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/monitoring/cache/regionCache.js"/>"></script>

</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft marginBottom">
			<h2>Cache</h2>
			<hr/>
			<button id="resetCache" data-url="<c:url value="/monitoring/cache/regionCache/clear"/>">Clear cache</button>
			<br/>
			<div id="accordion" data-updateDataUrl="cache/regionCache/data">
				
				<c:forEach var="region" items="${regions}">
					<h3 id="h-${region.name}" style="padding-top:5px;padding-bottom:5px;"><b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ${region.name}</b></h3>
					<div id="div-${region.name}">
						<div style="margin-left:-15px">
							<table>
								<tr>
									<td>
										<dl class="marginTop">
								    		<dt>Max Size</dt>
								    		<dd id="${region.name}_maxEntries"></dd>
											<dt>Max Reached Size</dt>
								    		<dd id="${region.name}_maxReachedSize"></dd>	    		
											<dt>Hit/Miss Ratio</dt>
								    		<dd><span id="${region.name}_factor"></span>%</dd>	    		    		
								    	</dl>
									</td>
									<td>
										<table>
											<thead>
											<tr>
												<th>Hits</th>
												<th>Misses</th>
												<th>Invalidations</th>
												<th>Evictions</th>
												<th>Fetches</th>
												<th>InstanceCount</th>
											</tr>
											</thead>
											<tr>
												<td id="${region.name}_Hits"></td>
												<td id="${region.name}_Misses"></td>
												<td id="${region.name}_Invalidations"></td>
												<td id="${region.name}_Evictions"></td>
												<td id="${region.name}_Fetches"></td>
												<td id="${region.name}_InstanceCount"></td>
											</tr>
											<tbody>
											</tbody>
										</table>
									</td>
								</tr>
						    	</table>
									<table id="${region.name}_types" >
										<thead>
											<tr>
												<th>Type</th>
												<th>Hits</th>
												<th>Misses</th>
												<th>Ratio</th>
												<th>Invalidations</th>
												<th>Evictions</th>
												<th>Fetches</th>
												<th>InstanceCount</th>
											</tr>
										</thead>
									<tbody>
								</tbody>
							</table>						
						</div>
					</div>
				</c:forEach>				
			</div>
		</div>
	</div>

	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Useful Links</h3>
				<div class="box">
				<div class="quiet">
					Further information about the <a href="${wikiCache}" target="_blank" class="quiet">hybris Platform Cache</a> can be found in our Wiki.
				</div>
			</div>
		</div>
	</div>
	</body>
</html>

