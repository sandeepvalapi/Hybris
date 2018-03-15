$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");

    $("body").on('click', '#gcButton',function() {
		var url = $('#gcButton').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				drawCircleChart(data);
				drawColumnChart(data);
			},
			error : hac.global.err
		});

	});
});

function getData() {
    var token = $("meta[name='_csrf']").attr("content");
    
	debug.log("Get Data...");
	var url = $('#charts').attr('data-chartDataUrl');
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			drawCircleChart(data);
			drawColumnChart(data);
		},
		error : hac.global.err
	});

}

function drawCircleChart(d) {
	debug.log(d.freeMemory + " / " + d.usedMemory + " / " + d.totalMemory);

	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Memory');
	data.addColumn('number', 'Size');
	data.addRows(2);
	data.setValue(0, 0, 'Free');
	data.setValue(0, 1, d.freeMemory);
	data.setValue(1, 0, 'Used');
	data.setValue(1, 1, d.usedMemory);

	var chart = new google.visualization.PieChart(document
			.getElementById('chart'));
	chart.draw(data, {
		width : 450,
		height : 300,
		backgroundColor : '#EDEDED'
	});
	debug.log(document.getElementById('chart'));
}

function drawColumnChart(d) {
	var data2 = new google.visualization.DataTable();
	data2.addColumn('string', 'type of memory');
	data2.addColumn('number', 'init');
	data2.addColumn('number', 'used');
	data2.addColumn('number', 'comitted');
	data2.addColumn('number', 'max');
	data2.addRows(2);
	data2.setValue(0, 0, 'heap');
	data2.setValue(0, 1, d.heap_init);
	data2.setValue(0, 2, d.heap_used);
	data2.setValue(0, 3, d.heap_comitted);
	data2.setValue(0, 4, d.heap_max);
	data2.setValue(1, 0, 'non-heap');
	data2.setValue(1, 1, d.nonheap_init);
	data2.setValue(1, 2, d.nonheap_used);
	data2.setValue(1, 3, d.nonheap_comitted);
	data2.setValue(1, 4, d.nonheap_max);
	var columnchart = new google.visualization.ColumnChart(document
			.getElementById('chart2'));
	columnchart.draw(data2, {
		width : 450,
		height : 300,
		backgroundColor : '#EDEDED'
	});
}

function displayNoInternetAccessWarning() {
	$('#warningMessage').html('<h1 style="color:red;">To see memory charts you have to be connected to the internet.</h1>');
}
