var table, statsTable, intervalId, statsIntervalId, lower = 0, upper = 50;
var statsEnabled = false;

$(document).ready(function() {

	table = $('#cache').dataTable({
		"bPaginate" : false,
		"bSort" : false,
		"bFilter" : false,
		"bInfo" : false
	});

	statsTable = $('#stats').dataTable({
		"bPaginate" : true
	});

	$("body").on('click', '#clearButton',function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#clearButton').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				updateCacheTable(data);
			},
			error : hac.global.err
		});

	});

	$("body").on('click', '#toggleStats',function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#toggleStats').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			data : "enable=" + !statsEnabled,
			success : function(data) {
				statsEnabled = !statsEnabled;
				if (statsEnabled) {
					$('#toggleStats').html('Switch Off');
					startStatsUpdates();
				} else {
					$('#toggleStats').html('Switch On');
					stopStatsUpdates();
				}
			},
			error : hac.global.err
		});

	});

	$("#slider-range").slider({
		range : true,
		min : 0,
		max : 100,
		values : [ 0, 50 ],
		slide : function(event, ui) {
			lower = ui.values[0];
			upper = ui.values[1];
			$("#range").html("Range: " + lower + "-" + upper);
		}
	});

	$("body").on("click", '#clearStats',function() {
		stopStatsUpdates();
		statsTable.fnClearTable();

	});

	intervalId = setInterval('updateData()', 2000);
});

function startStatsUpdates() {
	if (statsIntervalId)
		clearInterval(statsIntervalId);

	statsIntervalId = setInterval("updateStats()", 2000)

}

function stopStatsUpdates() {
	if (statsIntervalId)
		clearInterval(statsIntervalId);

}

function updateStats() {
	var url = $('#cache').attr('data-updateStatsUrl');
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		data : "lower=" + lower + "&upper=" + upper,
		success : function(data) {
			statsTable.fnClearTable();
			var row;
			for (pos in data) {
				row = data[pos];
				statsTable.fnAddData([ row.hitCount, row.missCount, row.factor,
						row.keyString ]);
			}

		},
		error : hac.global.err
	});
}

function updateData() {
	var url = $('#cache').attr('data-updateDataUrl');
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			updateCacheTable(data);
		},
		error : hac.global.err
	});
}

function clearUpdates() {
	if (intervalId)
		clearInterval(intervalId);
}

function updateCacheTable(d) {
	table.fnClearTable();
	table.fnAddData([ d.maxSize, d.currentSize,
			d.maxReachedSize, d.numHitsSinceStart, d.numAddsSinceStart, d.numDeletesSinceStart,
			d.numMissedSinceStart ]);
}