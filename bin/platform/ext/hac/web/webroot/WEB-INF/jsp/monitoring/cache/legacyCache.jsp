<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
<title>Cache</title>

<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="<c:url value="/static/css/monitoring/cache.css"/>" type="text/css" media="screen, projection">

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/monitoring/cache/legacyCache.js"/>"></script>

</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft">
			<h2>Legacy Cache</h2>
			<hac:note>
				Default cache for the hybris Platform is the hybris Region Cache. You are currently using previous cache. Activate the hybris Region Cache by adding property definition <strong>cache.legacymode=false</strong> to the	
				<span class="filecol">local.properties</span> file. Afterwards restart your application server.
			</hac:note>
			    			
			<h3>Overview</h3>
			
			<table id="cache" data-updateStatsUrl="<c:url value="/monitoring/cache/legacyCache/stats"/>" data-updateDataUrl="<c:url value="/monitoring/cache/legacyCache/data"/>">
				<thead>
					<tr>
						<th>Max. cache size</th>
						<th>Current size</th>
						<th>Max. reached cache size</th>
						<th>Gets</th>
						<th>Adds</th>
						<th>Removes</th>
						<th>Misses</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<button id="clearButton" data-url="<c:url value="/monitoring/cache/legacyCache/clear"/>">Clear cache</button>
			<h3 style="clear: both; margin-top: 4.5em;">Statistics</h3>
			
			<button id="toggleStats" data-url="<c:url value="/monitoring/cache/legacyCache/stats/toggle"/>">Switch on</button>
			<button id="clearStats">Clear table</button>
			
			<div style="float:right; text-align:center; width:200px;">
				<div id="range">Range: 0-50</div>
				<div id="slider-range"></div>
			</div>
			
			<table id="stats">
				<thead>
					<tr>
						<th>Hits</th>
						<th>Misses</th>
						<th>Ratio</th>
						<th>Key</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			</div>
			</div>
			<div class="span-6 last" id="sidebar">
			<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides details about the cache. <br><br>
								<ul>
									<li><strong>Overview</strong>: Information about cache parameters status. You can clear cache  without reconfirmation.</li>
									<li><strong>Statistics</strong>: You can create caching statistics.</li>
								</ul>
							<hr/>
							<hac:note>
							<strong>Statistics</strong><br>
							Calculating cache statistics should be used over short periods only, because it is very memory intensive
							</hac:note>
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiCache}" target="_blank" class="quiet" >hybris Platform Cache</a> </li>
						</ul>
					</div>
			</div>
		</div>
	</body>
</html>

