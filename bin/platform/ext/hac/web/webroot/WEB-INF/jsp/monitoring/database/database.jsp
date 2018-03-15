<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Database</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/monitoring/database.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/database.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft marginBottom"> 
					<h2>Database</h2>
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1">Data sources</a></li>
							<li><a href="#tabs-2">Table size</a></li>
							<li><a href="#tabs-3">JDBC logging</a></li>
							<li><a href="#tabs-4">JDBC log analysis</a></li>
						</ul>		
						<div id="tabs-1">
							<div id="dataSourceInfos" data-url="<c:url value="/monitoring/database/reset/"/>" data-rebuildDataSourceInfosUrl="<c:url value="/monitoring/database/allinfo/"/>"></div>
						</div>
						<div id="tabs-2">
							<button id="buttonTableSizes">Calculate Table Sizes</button>
	
							<div id="tableWrapper">
								<table id="tablesizes" data-reloadTableSizesUrl="<c:url value="/monitoring/database/tablesizes"/>">
									<thead>
										<tr>
											<th>Table</th>
											<th>Rows</th>
										</tr>
									</thead>
									<tbody>
								      				
									</tbody>
								</table>	
							</div>
						</div>
						<div id="tabs-3">
							<div id="loggingSpinnerWrapper">
								<img id="spinner" src="<c:url value="/static/img/spinner.gif"/>" alt="spinner"/>
							</div>
							<div id="loggingContentWrapper" data-refreshLoggingInfoUrl="<c:url value="/monitoring/database/logs/"/>">
								<p>
									Log file path: <span id="logFilePath"></span> (<span id="logFileSize"></span> kB)<br/>
									<button id="clearLog" class="floatLeft" data-url="<c:url value="/monitoring/database/clearlog"/>">Clear log</button>
									<button id="toggleLogging" class="floatLeft" data-url="<c:url value="/monitoring/database/logs/"/>"></button>
									<button id="toggleTraces" class="floatLeft" data-url="<c:url value="/monitoring/database/stacktrace/"/>"></button>
									<button id="downloadLog" class="floatLeft">Download log</button>	
								</p>
								<div id="slider-size"></div>	
							</div>					
						</div>
						
						<div id="tabs-4">
							<p>
							 <button id="analyzeLog" data-url="<c:url value="/monitoring/database/analyzeLog"/>">Analyze log</button>
							</p>
							
							<div id="analyzeResults"> 
								<dl>
									<dt>Total Queries Analyzed</dt>
									<dd id="totalQueries"></dd>
									<dt>Total Query Time in ms</dt>
									<dd id="totalTime"></dd>
								</dl>
								
								<table id="analyzeTable">
									<thead>
										<tr>
											<th>Query</th>
											<th>Count</th>
											<th>Time</th>
											<th>%</th>
										</tr>
									</thead>
									<tbody>
								      				
									</tbody>
								</table>							
							</div>			
						</div>					
					</div>			
				</div>
			</div>
			<div class="span-6 last" id="sidebar1">
				<div class="prepend-top">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page shows all configured data sources with status parameters.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiDatabase}" target="_blank" class="quiet" >Third-Party Databases</a> </li>
						</ul>
					</div>
				</div>
			</div>	
			
			<div class="span-6 last" id="sidebar2">
				<div class="prepend-top">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides a list of all database tables for the configured data source with their current size.
						</div>
						
						<hac:note>
							Calculating all table sizes might be an expensive operation for a big system. Please consider this before starting this calculation!
						</hac:note>						
					</div>
				</div>
			</div>
			
			<div class="span-6 last" id="sidebar3">
				<div class="prepend-top">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page enables you to start logging to the configured log file and download it. If traces are enabled, then a SQL comment is added to each statement with the Java stack trace from where the statement is being called. <br><br>
							<hr />
							<hac:note>
								Do not forget to stop logging when it is not needed, to avoid performance issues. Remember also to clear the log file when it is not needed any more.
							</hac:note>
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiJdbcLogging}" target="_blank" class="quiet" >Logging Database Statements</a> </li>
						</ul>
					</div>
				</div>
			</div>		
			
			<div class="span-6 last" id="sidebar4">
				<div class="prepend-top">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page enables you to identify JDBC statements that are executed either very often or take particularly long to execute. This allows you to optimize your system. It analyzes the log created in <strong>JDBC Logging</strong> tab.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiJdbcLogging}" target="_blank" class="quiet" >Logging Database Statements</a> </li>
						</ul>
					</div>
				</div>
			</div>						
			
			<form id="downloadForm" method="GET" action="<c:url value="/monitoring/database/logs/download"/>">
				<input type="text" id="downloadSize" name="downloadSize" value="-1"/>
			</form>



	</body>
</html>

