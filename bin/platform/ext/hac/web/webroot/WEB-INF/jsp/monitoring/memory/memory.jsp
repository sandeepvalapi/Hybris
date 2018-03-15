<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Memory</title>
	
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/memory.js"/>"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script type="text/javascript">
	  if (typeof google === 'undefined') {
		  $(document).ready(displayNoInternetAccessWarning);
	  } else {
		  google.load("visualization", "1", {packages:["corechart"]});	 		  
		  $( document ).ready(function() {
			  google.setOnLoadCallback(getData);
			});

	  }
	</script>  	
  

</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft" id="charts" data-chartDataUrl="<c:url value="/monitoring/memory/data/"/>">
					<h2>Memory</h2>
					<div id="warningMessage" style="text-align:center;"></div>
					<div id="chart" style="text-align:center;"></div>
					<div id="chart2" style="text-align:center;"></div>
					<button id="gcButton" data-url="<c:url value="/monitoring/memory/gc/"/>">Run Garbage Collector</button>
				</div>				
			</div>
			<div class="span-6 last" id="sidebar">
				<div class="prepend-top" id="recent-reviews">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
						 This page provides status parameters of the Java Virtual Machine memory. You can run Garbage Collector to clear memory and refresh statistics. 
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiPerformance}" target="_blank" class="quiet" >Performance Tuning Overview</a> </li>
						</ul>
					</div>
				</div>
			</div>		
	</body>
</html>

