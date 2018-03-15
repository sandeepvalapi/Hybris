//pollId for stop polling.
var pollId = null;

//How long the live-charts reach into the past in ms.
var historyLength = 300000;

//The name of the local stored cookie
var cookieName = "indexSettings";

//Indicates, how many poll-requests got an exception successive.
var timeouts = 0;

//Time between the polls in ms.
var pollInterval;

//Maximum number of ticks that are shown in chart for one line.
var maxTicks;

//Difference between local time and utc-time.
var utcOffset = (new Date()).getTimezoneOffset() * 60 * 1000;

//Different time-formats for chart.
var datetimeformat = "%d/%m/%y %H:%M:%S";
var timeformat = "%H:%M:%S";
var dateformat = "%d-%m-%y";

//Object containing the charts that are shown.
var charts = {};

hac.home = {};

$(function() {
	$('#clearHistory').click(function() {
		clearInterval(pollId);
		hac.history.clearHistory();
		$('#toplinks').html('');
		init();
	});
	
	$("#refreshCharts").click(function(){
		pollData();
	});
	
	var closeOverlay = function(){
		$("#chartContainer").empty();
		$("#labelContainer").empty();
		$("#zoomedChart").hide();
		$("#overlay").fadeOut();
	};
	
	$("#overlayCloseButton").click(closeOverlay);
	$("#overlay").click(closeOverlay);
	
	//Event listener when changing the shown time-period.
	$("#pollLast").bind("change", function() {
		var value = $(this).val();
		//Stop polling when time-period larger than 12 hours or manual.
		if(value <= 43200000 && value > 0){
			clearInterval(pollId);
			pollId = setInterval(pollData,pollInterval);
		}
		else{
			clearInterval(pollId);
		}
		historyLength = value;
		adjustDateTimeFormat();
		pollData();
		createCookie(JSON.stringify({pollLast: $(this).val()}));
	});

	init();

	function init() {
		//Initially hide the zoomed Chart stuff.
		$("#zoomedChart").hide();
		$("#overlay").hide();
		
		//Set poll-Interval (defined in project.properties).
		pollInterval = $("#content").attr("poll-interval");
		//Set maxTicks (defined in project.properties).
		maxTicks = $("#content").attr("maxTicks");
		
		//Show homecontent. Necessary to draw charts. Charts cant be drawn in invisible elements.
		$('#homeContent').fadeIn('slow');
		
		//Create charts.
		charts[$("#memoryChartContainer").attr("chartId")] = new Chart($("#memoryChartContainer"), $("#memoryChartLabels"), 3, "MB", 3, true);
		charts[$("#threadsChartContainer").attr("chartId")] = new Chart($("#threadsChartContainer"), $("#threadsChartLabels"), 0, "", 3, true);
//		charts[$("#sessionsChartContainer").attr("chartId")] = new Chart($("#sessionsChartContainer"), $("#sessionsChartLabels"), 0, "", 3, true);
		charts[$("#osChartContainer").attr("chartId")] = new Chart($("#osChartContainer"), $("#osChartLabels"), 2, "%", 3, true);
		charts[$("#taskQueueChartContainer").attr("chartId")] = new Chart($("#taskQueueChartContainer"), $("#taskQueueChartLabels"), 0, "Tasks", 3, true);
		charts[$("#taskQueuePoolingChartContainer").attr("chartId")] = new Chart($("#taskQueuePoolingChartContainer"), $("#taskQueuePoolingChartLabels"), 0, "Tasks", 3, true);
		charts[$("#dbConnectionsInUseChartContainer").attr("chartId")] = new Chart($("#dbConnectionsInUseChartContainer"), $("#dbConnectionsInUseChartLabels"), 0, "Connections", 3, true);
		//Plot charts.
		$.each(charts, function(index, value){
			value.plot();
		});
		
		buildHistory();
		
		//set pollId to be able to stop the polling later.
		pollId = setInterval(pollData,pollInterval);
		
		//Read stored settings from Cookie
		readCookieValues();
		
		adjustDateTimeFormat();
		
		//If pollInterval higher than 12 hours, dont poll.
		if(pollInterval == "manual" || pollInterval > 43200000){
			clearInterval(pollId);
		}
		
		//Fill charts initially.
		pollData();
	}
	
	//If shown history <= 24h, show time only. If bigger than 24h, show date only.
	function adjustDateTimeFormat(){
		var format = ($("#pollLast").val() <= 86400000 && $("#pollLast").val() > 0) ? timeformat : dateformat;
		$.each(charts, function(index,chart){
			chart.timeformat = format;
			chart.plot();
		});
	}
	
	//Read settings stored in cookie, for example time-period shown.
	function readCookieValues(){
		if($.cookie(cookieName) == null)
			return true;
		var settings = JSON.parse($.cookie(cookieName));
		if(settings["pollLast"] != null){
			var value = settings["pollLast"];
			$("#pollLast").val(value);
			if(value <= 43200000 && value > 0){
				clearInterval(pollId);
				pollId = setInterval(pollData,pollInterval);
			}
			else{
				clearInterval(pollId);
			}
			historyLength = value;
		}
	}
	
	//Create or update Cookie.
	function createCookie(content){
		$.cookie(cookieName, content, {	
			   expires : 365
			});
	}
	
	function buildHistory() {
		var historyArr = hac.history.getSortedTruncatedHistory(5);
		for (pos in historyArr)
		{
			for (var pos2 in historyArr[pos])
			{	
				var data = historyArr[pos][pos2];
				$('#toplinks').append("<li><a class=\"quiet\" href=\""+hac.contextPath + pos2+"\">"+data.t+"</a></li>");
			}
		}	
	}

	//Method polls new data for all available charts.
	function pollData(){
		pollRequest($("#memoryChartContainer").attr("chartId"));
		pollRequest($("#threadsChartContainer").attr("chartId"));
//		pollRequest($("#sessionsChartContainer").attr("chartId"));
		pollRequest($("#osChartContainer").attr("chartId"));
		pollRequest($("#taskQueueChartContainer").attr("chartId"));
		pollRequest($("#taskQueuePoolingChartContainer").attr("chartId"));
		pollRequest($("#dbConnectionsInUseChartContainer").attr("chartId"));
	}
});

//Request new data from server.
function pollRequest(chartId){
	$.ajax({
		url : $("#content").attr("data-url") + "poll",
		type : 'GET',
		cache : false,
		headers : {
			'Accept' : 'application/json'
		},
		data: {
			chartId: chartId,
			timePeriod: historyLength,
			utcOffset: utcOffset
		},
		success : function(data){
			if (typeof(data) === 'string' && data.indexOf("redirect_detection") != -1)  {
				// Redirect when response is login page where "redirect_detection" marker is placed
				// Login page could be as response in case of redirection from spring security (session is dead)
				clearInterval(pollId);
				top.location.href = "/";
				return true;
			}
			if(data.exception != null){
				hac.global.error(data.exception);
				charts[data.chartId].failed();
			}
			timeouts = 0;
			charts[data.chartId].update(data);
			updateRuntime(data.runtime, data.os.availableProcessors);
		},
		error: function(){
			timeouts++;
			if(timeouts >= 8){
				clearInterval(pollId);
				hac.global.error("No Server Response. Polling stopped.");
			}
			else{
				hac.global.error("No Server Response");
			}
		}
	});
}

//Function handles Ajax-call to get a range of data from start till end-timestamp in ms.
function fetchSelection(start, end, chart){
	$.ajax({
		url : $("#content").attr("data-url") + "fetch",
		type : 'GET',
		cache : false,
		headers : {
			'Accept' : 'application/json'
		},
		data: {
			chartId: chart.chartId,
			start: Math.round(start + utcOffset),
			end: Math.round(end + utcOffset),
			utcOffset: utcOffset
		},
		success : function(data){
			if (typeof(data) === 'string' && data.indexOf("redirect_detection") != -1)  {
				// Redirect when response is login page where "redirect_detection" marker is placed
				// Login page could be as response in case of redirection from spring security (session is dead)
				clearInterval(pollId);
				top.location.href = "/";
				return true;
			}
			if(data.exception){
				hac.global.error("Internal Error.");
				return;
			}
			var plotData = new Array();
			var range = 0;
			
			for(var line in data.lines){
				var newLine = {
					label: data.lines[line].label,
					data: data.lines[line].data,
					color: data.lines[line].color
				};
				
				var len = $.map(newLine.data, function(n, i) { return i; }).length;
				range = newLine.data[len-1][0] - newLine.data[0][0];
				
				//Chrome Bugfix: If showing a line with very much datapoints, and the line only consists of the same values, problems occur.
				if(window.chrome && newLine.data[0][1] == newLine.data[len-1][1] && newLine.data.length > 3000){
					newLine.data[len-1][1] = newLine.data[len-1][1] + 0.1;
				}
				plotData.push(newLine);
			}
			//If time-period shown in zoomed-chart is longer than 24 hours, show time only, otherwise show date.
			var format = (range < 172800000) ? timeformat : dateformat;
			$.plot($("#chartContainer"),plotData,createChartOptions($("#labelContainer"), chart.roundNrs, chart.unit, chart.tickCount, format, false, true));
		},
		error: hac.global.err
	});
}

function updateRuntime(runtime, availableProcessors)
{
	$('#prettyTime').html(runtime.prettyTime);	
	$("#availableProcessors").html(availableProcessors);
}
