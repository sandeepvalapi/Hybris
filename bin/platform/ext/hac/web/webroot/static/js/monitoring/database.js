var tablesizes, analyzeTable, logEnabled, tracesEnabled, logUpdateIntervalId, sizeSlider, currentDownloadSize = -1;
var buttonTextAnalyze = "Analyze";
var buttonTextDownloadLog = "Download log";
var buttonTableSizes = "Calculate Table Sizes"

$(document).ready(function() {
	$( "#tabs" ).tabs({
		activate: function(event, ui) { 
			if ( ui.newPanel.attr('id') == 'tabs-3')
				refreshLoggingInfo();
			
			toggleActiveSidebar(ui.newPanel.attr('id').replace(/^.*-/, ''));
		}
	});
	
	$('#buttonTableSizes').click(reloadTableSizes);
	
	// tab 1
	tablesizes = $('#tablesizes').dataTable({
		"bStateSave": true,
		"bAutoWidth": false,
		"aLengthMenu" : [[10,25,50,100,-1], [10,25,50,100,'all']]
	});
	
	analyzeTable = $('#analyzeTable').dataTable({
		"bAutoWidth": false,
		"aaSorting": [ [3,'desc']],
	});
	
	sizeSlider = $("#slider-size").slider({
		min : 0,
		max : 100,
		change : function(event, ui) {
			$('#downloadLog').html(buttonTextDownloadLog + " (last " + ui.value + "kB)");
			currentDownloadSize = ui.value;
		}
	});		

	// tab 0
	rebuildDataSourceInfos();
	
	$("body").on('click', '.resetButton',function(e) {
        var token = $("meta[name='_csrf']").attr("content");

        var url = $('#dataSourceInfos').attr('data-url') + this.id;
		$.ajax({
			url:url,
			type:'POST',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				$('#ds_' + data.dsId).fadeOut('fast', function(e) {
					$(this).remove();
				});
				appendDataSourceInfo(data);
			},
			error: hac.global.err
		});					
		
	});
	
	$('#toggleLogging').click(function(e){
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#toggleLogging').attr('data-url') + !logEnabled;
		$.ajax({
			url:url,
			type:'POST',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				debug.log(data);
				logEnabled = !logEnabled;
				updateLoggingUI();
				if (logEnabled)
					logUpdateIntervalId = setInterval("refreshLoggingInfo()", 2000);
				else if (logUpdateIntervalId)
					clearInterval(logUpdateIntervalId);
			},
			error: hac.global.err
		});				
		
	});
	
	$('#toggleTraces').click(function(e){
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#toggleTraces').attr('data-url') + !tracesEnabled;
		$.ajax({
			url:url,
			type:'POST',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				debug.log(data);
				tracesEnabled = !tracesEnabled;
				updateLoggingUI();
			},
			error: hac.global.err
		});				
	});	
	
	$('#clearLog').click(function(e){
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#clearLog').attr('data-url');
		$.ajax({
			url:url,
			type:'POST',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(deleteResult) {
				debug.log(deleteResult);
				if (deleteResult.success)
				{
					$('#logFileSize').html('0');
					$('#downloadLog').fadeOut();
					$('#slider-size').fadeOut();
				}
				else if(deleteResult.error)
				{
					hac.global.error(data.error);
				}
			},
			error: hac.global.err
		});				
		
	});
	
	$('#downloadLog').click(function(e){
		$('#downloadSize').val(currentDownloadSize);
		$('#downloadForm').submit();
	});		
	
	$('#analyzeLog').click(function(e){
		var url = $('#analyzeLog').attr('data-url');
        var token = $("meta[name='_csrf']").attr("content");

        $('#analyzeLog').html(buttonTextAnalyze + ' ' +  hac.global.getSpinnerImg());
		$.ajax({
			url:url,
			type:'GET',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				debug.log(data);
				
				$('#totalQueries').html(data.totalQueries);
				$('#totalTime').html(data.totalTime);
				
				analyzeTable.fnClearTable();
				var pos;
				for (pos in data.queryMap)
				{
					var obj = data.queryMap[pos];
					analyzeTable.fnAddData([getTruncatedQuery(pos), obj.count, obj.time, Math.round(obj.time/data.totalTime*100)]);
				}
				
				$('#analyzeResults').fadeIn();
				$('#analyzeLog').html(buttonTextAnalyze);
			},
			error: hac.global.err
		});				
	});
});	


function getTruncatedQuery(query)
{
	var html = '<span title="' + query + '">' + query.substring(0,200) + '</span>';
	debug.log(html);
	return html;
}

function toggleActiveSidebar(num)
{
	// fadeout
	$("div[id^=sidebar]").each(function() { 
		$(this).fadeOut();
	});
	setTimeout("$('#sidebar"+num+"').fadeIn();", 500);
}

function refreshLoggingInfo()
{
    var token = $("meta[name='_csrf']").attr("content");
    var url = $('#loggingContentWrapper').attr('data-refreshLoggingInfoUrl');
	$.ajax({
		url:url,
		type:'GET',
		headers:{
            'Accept':'application/json',
            'X-CSRF-TOKEN' : token
        },
		success: function(data) {
			debug.log(data);
			
			$('#loggingSpinnerWrapper').fadeOut('fast', function(e){
				logEnabled = data.logEnabled;
				tracesEnabled = data.tracesEnabled;
				
				updateLoggingUI();
				
				$('#logFilePath').html(data.logFilePath);
				$('#logFileSize').html(data.logFileSize);
				$('#slider-size').slider( "option", "max", data.logFileSize );

				if (parseInt(data.logFileSize, 10) > 0)
				{
					$('#downloadLog').fadeIn();
					$('#slider-size').fadeIn();
				}
				
				if (logEnabled && !logUpdateIntervalId)
					logUpdateIntervalId = setInterval("refreshLoggingInfo()", 2000);
				
				$('#loggingContentWrapper').fadeIn();
			});
		},
		error: hac.global.err
	});		
}

function updateLoggingUI()
{
	$('#toggleLogging').html(logEnabled ? 'Stop Logging ' + hac.global.getSpinnerImg() : "Start logging");
	$('#toggleTraces').html(tracesEnabled ? "Disable Traces" : "Enable traces");
}


function reloadTableSizes()
{
	$('#tableWrapper').fadeOut();
	tablesizes.fnClearTable();
	
	$('#buttonTableSizes').html(buttonTableSizes + ' ' +  hac.global.getSpinnerImg());
    var token = $("meta[name='_csrf']").attr("content");

    var url = $('#tablesizes').attr('data-reloadTableSizesUrl');
	$.ajax({
		url:url,
		type:'GET',
		headers:{
            'Accept':'application/json',
            'X-CSRF-TOKEN' : token
        },
		success: function(data) {
			debug.log(data);
		
			$('#buttonTableSizes').html(buttonTableSizes);
			
			for (pos in data)
			{
				tablesizes.fnAddData([pos, data[pos]]);
			}
			
			$("#tableWrapper").fadeIn();
			
		},
		error: hac.global.err
	});				
}

function rebuildDataSourceInfos()
{
	// clear all
	$('#dataSourceInfos').html('');
    var token = $("meta[name='_csrf']").attr("content");
    var url = $('#dataSourceInfos').attr('data-rebuildDataSourceInfosUrl');
	$.ajax({
		url:url,
		type:'GET',
		headers:{
            'Accept':'application/json',
            'X-CSRF-TOKEN' : token
        },
		success: function(data) {
			debug.log(data);
			for (pos in data)
				appendDataSourceInfo(data[pos]);
		},
		error: hac.global.err
	});			
}

function appendDataSourceInfo(datasource)
{	
	var html = '';
	
	html += '<fieldset id="ds_'+datasource.dsId+'">';
	html+= '<legend>'+datasource.dsId;
	if(datasource.active)
		html+= ' - active';
	html+= '</legend>';
	
	html += '<button style="float:right;" class="resetButton" id="'+datasource.dsId+'">Reset statistics</button>';
	html += connectionInfo(datasource);		
	
	html+= '<dl>';
 	
	if (datasource.jndi)
	{
		html+= dlEntryTextareaStyle("jndiName", datasource.pool);
	}
	else
	{
		var databaseProps= ["url", "dbName", "dbUser", "dbVersion", "dbDriverVersion"];
		
		if (datasource.tablePrefix)
			databaseProps[databaseProps.length] = "tablePrefixName";
		
		
		for (pos in databaseProps)
		{
			var value = datasource[databaseProps[pos]];
			if (value != null && value.length > 100)
				html+= dlEntryTextareaStyle(databaseProps[pos], value);
			else
				html+= dlEntryStyle(databaseProps[pos], value);
		}		
		
		
	}

 	html+= '</dl>';
 	
 	html+= '</fieldset><hr />';
	
	$('#dataSourceInfos').append(html);
}

function connectionInfo(datasource)
{
	var dsInUse = (datasource.numInUse == 1) ? " data source " : " data sources "; 
	var dsOpen = (datasource.numPhysicalOpen == 1) ? " data source " : " data sources "; 
	
	var html;
	html += '<p>';
	html = "This data source currently has <strong>" + datasource.numInUse + dsInUse + "</strong> in use and <strong>" +  datasource.numPhysicalOpen + dsOpen + "</strong> opened. ";
	html += "The maximum number of simultaneously opened connections is <strong>" + datasource.maxPhysicalOpen + "</strong>. ";
	html += "According to the configuration, the connection pool can have a maximum of <strong>" + datasource.maxAllowedOpen + "</strong> connections open. ";
	html+= "The total number of getConnection() calls is <strong>" + datasource.totalGets + "</strong> and the average time to get a connection is <strong>" + datasource.millisWaitedForConn + " ms.</strong>";
	html+= "</p>";
	
	return html;
}

function dlEntryStyle(key, value)
{
	return '<dt>'+key+'</dt><dd>'+value+'</dd>';		
}

function dlEntryTextareaStyle(key, value)
{
	return '<dt>'+key+'</dt><dd><textarea class="nobox textarea" style="min-height:50px; background:white;">'+value+'</textarea></dd>';		
}	
