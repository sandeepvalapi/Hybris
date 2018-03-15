<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Cron Jobs</title>

	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />	
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/cronjobs.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft">
					<h2>Cron Jobs</h2>
					
					<div id="tableWrapper">					
						<table id="cronjobs" data-cronjobsLoadUrl="<c:url value="/monitoring/cronjobs/data"/>" >
							<thead>
								<tr>
									<th>Code</th>
									<th>Job code</th>
									<th>Progress</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>	
					</div>
					
					<button id="abortJobs" style="clear:both;" data-url="<c:url value="/monitoring/cronjobs/abort"/>">Abort cron jobs</button>			
					
					<div id="abortingResult">
					</div>
				</div>
			</div>
			<div class="span-6 last" id="sidebar">
				<div class="prepend-top" id="recent-reviews">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides a list of all running cron jobs with the possibility to abort them.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiCronjob}" target="_blank" class="quiet" >cronjob Extension - Technical Guide</a> </li>
						</ul>
					</div>
				</div>
			</div>		
	</body>
</html>

