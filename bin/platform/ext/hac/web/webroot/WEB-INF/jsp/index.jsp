<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Home</title>

<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="<c:url value="/static/css/home/index.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/home/index.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/home/chart.js"/>"></script>
<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="/static/js/excanvas.min.js"></script><![endif]-->
<script type="text/javascript" src="<c:url value="/static/js/jquery.flot.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/jquery.flot.tooltip.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/jquery.flot.selection.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/jquery.flot.time.js"/>"></script>

</head>


<body>
	<div class="prepend-top span-17 colborder" id="content" data-url="<c:url value='/'/>" poll-interval="${pollInterval}" pollMax="${maxTicks}">
		<div class="marginLeft marginBottom" id="homeContent" style="overflow-x:hidden;overflow-y:hidden">			
			<div id="runtimeInfo" class="marginTop">
				<div id="threadStats" class="energized">
					<div>
						Uptime: <span id="prettyTime"></span>. Enjoy!
					</div>
					<br/>
				</div>
			</div>

			<div style="background-color:#dedede">
				<div style="float:right">
					<button id="refreshCharts" style="width:30px;height:25px">
						<span style="font-family:Lucida Sans Unicode;font-size:120%">
							&#8635;
						</span>
					</button>
				</div>
				
				<div style="float:right;margin-right:10px;margin-top:4px">
					<label>Show last:&nbsp&nbsp</label>
					<select id="pollLast" name="pollLast">
					 	<optgroup label="Automatic Polling">
							<option value="300000">5 minutes</option>
							<option value="3600000">1 hour</option>
			  				<option value="43200000">12 hours</option>
		  				</optgroup>
		  				<optgroup label="Manual Polling">
			  				<option value="86400000">24 hours</option>
			  				<option value="604800000">1 week</option>
			  				<option value="2419200000">1 month</option>
			  				<option value="-1">all</option>
		  				</optgroup>
					</select>
				</div>
				<div style="clear:both">
				</div>
			</div>
				
			<div>
				<div id="memoryInfo" style="float:left">
					<div class="energizedSmall marginTop">Memory overview</div>
					<br/>
					<div id="memoryChartContainer" style="width:310px;height:200px" chartId="memoryChart"></div>
					<div id="memoryChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
					<br/>
					<br/>
				</div>
				
				<div id="osInfo" style="float:right;margin-right:18px">
					<div class="energizedSmall marginTop">
						CPU Load (<span id="availableProcessors"></span> processors)
					</div>
					<br/>
					<div id="osChartContainer" style="width:310px;height:200px" chartId="osChart"></div>
					<div id="osChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
					<br/>
					<br/>
				</div>
				
				<div id="threadsInfo" style="float:left">
					<div class="energizedSmall marginTop">Threads overview</div>
					<br/>
					<div id="threadsChartContainer" style="width:310px;height:200px" chartId="threadsChart"></div>
					<div id="threadsChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
				</div>
				
				<!-- 
				<div id="sessionsInfo" style="float:right;margin-right:18px">
					<div class="energizedSmall marginTop">Sessions overview</div>
					<br/>
					<div id="sessionsChartContainer" style="width:310px;height:200px" chartId="sessionsChart"></div>
					<div id="sessionsChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
				</div>
				-->
				<div id="dbConnectionsInUseInfo" style="float:right;margin-right:18px">
					<div class="energizedSmall marginTop">Db Connections Overview</div>
					<br/>
					<div id="dbConnectionsInUseChartContainer" style="width:310px;height:200px" chartId="dbConnectionsInUseChart_${tenantId}"></div>
					<div id="dbConnectionsInUseChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
				</div>

				<div id="taskQueueInfo" style="float:left">
					<div class="energizedSmall marginTop">Task Queue Overview</div>
					<br/>
					<div id="taskQueueChartContainer" style="width:310px;height:200px" chartId="taskQueueSizeChart_${tenantId}"></div>
					<div id="taskQueueChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
				</div>

				<div id="taskQueuePoolingInfo" style="float:right;margin-right:18px">
					<div class="energizedSmall marginTop">Pending Tasks Overview</div>
					<br/>
					<div id="taskQueuePoolingChartContainer" style="width:310px;height:200px" chartId="taskQueuePoolingSizeChart_${tenantId}"></div>
					<div id="taskQueuePoolingChartLabels" style="width:250px;height:50px;padding-left:25px"></div>
				</div>
				
				<div style="clear:both"></div>
			</div>
		</div>
	</div>
	
	<!-- Content for the big chart when selecting a time-period in a chart. -->
	<div id="overlay"></div>
	<div id="zoomedChart">
		<div style="width:90%;height:85%;margin-right:5%;margin-left:5%;margin-top:5%" id="chartContainer"></div>
		<div style="width:80%;margin-left:10%" id="labelContainer"></div>
		<button id="overlayCloseButton">X</button>
	</div>
	
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top">
			<h3 class="caps">Top links</h3>
			<div class="box">
				<div class="quiet">
					<ol id="toplinks">

					</ol>
					<div>
						<button id="clearHistory">Clear history</button>
					</div>
				</div>	
			</div>
			<h3 class="caps">Statistics</h3>
			<div class="box">
				<div class="quiet">
					<ul>
						<li>
						The Charts show a limited number of datasets.
						</li>
						<li>
						In the top right you can choose how long the 
						history should reach into the past.
						</li>
						<li>
						At 5 min, 1 hour and 12 hours, data polling is 
						activated. At higher time-periods you have to 
						update the values with the refresh button.
						</li>
						<li>
						To get a more detailed view, you can mark an area 
						in the chart you want to see. In the upcoming chart, 
						more detailed data in this time-period is shown.
						</li>
					</ul>
				</div>	
			</div>
			<h3 class="caps">See also in the hybris Wiki</h3>
			<div class="box">
				<div class="quiet">
					<ul>
						<li><a href="${wikiEnduserGuide}" target="_blank" class="quiet">hybris Administration Console - End User Guide</a> </li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

