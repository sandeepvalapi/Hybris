var buttonTextRunLinpack = 'Run Linpack';
var buttonTextRunSql = 'Run SQL';
var buttonTextRunSqlMax = 'Run SQL Max';
var buttonTextRunOverall = 'Run';
$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");

    $("#tabs").tabs({
		activate : function(event, ui) {
			hac.global.toggleActiveSidebar(ui.newPanel.attr('id').replace(/^.*-/, ''));
		}
	
	});

	$('#runLinpack').click(function(e) {
		hac.global.notify("Running test...");
		$('#resultLinpack').fadeOut();
		$('#runLinpack').html(
				buttonTextRunLinpack + ' '
						+ hac.global.getSpinnerImg());
		$('#runLinpack').attr('disabled', true);
		var url = $('#runLinpack').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				debug.log(data);
	
				$('#resultLinpack').html(data.result);
				$('#runLinpack').html(buttonTextRunLinpack);
				$('#resultLinpack').fadeIn();
				$('#runLinpack').removeAttr('disabled');
			},
			error : hac.global.err
		});
	});

	$('#resultSQL').hide();
	
	$('#runSQL').click(function(e) {
		$('#runSQLForm').validate({
			rules: {
				sql: "required",
				seconds: {
					required: true,
					number: true
				},
				count: {
					required: true,
					number: true
				}
			},
			submitHandler: function(form) {
				hac.global.notify("Running test...");

				$('#runSQL').html(buttonTextRunSql + ' ' + hac.global.getSpinnerImg());
				$('#runSQL').attr('disabled', true);
				var url = $('#runSQL').attr('data-url');
				$.ajax({
					url : url,
					type : 'POST',
					data : 'count=' + $('#count').val() + '&seconds=' + $('#seconds').val() + '&sql=' + encodeURIComponent($('#sql').val()),
					headers : {
						'Accept' : 'application/json',
                        'X-CSRF-TOKEN' : token
					},
					success : function(data) {
						debug.log(data);
						
						if (!data.error) {
							$('#statementsCount').html(data.statementsCount);
							$('#statementsPerSecond').html(data.statementsPerSecond);
							$('#timePerStatement').html(data.timePerStatement);
							$('#resultSQL').fadeIn();
						} else {
							hac.global.error(data.error);
						}
						$('#runSQL').html(buttonTextRunSql);
						$('#runSQL').removeAttr('disabled');
					},
					error : hac.global.err
				});				
			}
		});
		$('#runSQLForm').submit(function(){
			return false;
		});
	});

	$('#runSQLMax').click(function(e) {
		hac.global.notify("Running test...");
		$('#resultMax').fadeOut();
		$('#runSQLMax').html(buttonTextRunSqlMax + ' ' + hac.global.getSpinnerImg());
		$('#runSQLMax').attr('disabled', true);
		var url = $('#runSQLMax').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				debug.log(data);
				
				$('#durationAdded').html(data.durationAdded);
				$('#durationAddedMax').html(data.durationAddedMax);
				$('#durationAddedMaxIndex').html(data.durationAddedMaxIndex);
				$('#runSQLMax').html(buttonTextRunSqlMax);
				$('#resultMax').fadeIn();
				$('#runSQLMax').removeAttr('disabled');
			},
			error : hac.global.err
		});
	});

	$('#runOverall').click(function(e) {
		$('#overallForm').validate({
			rules: {
				secondsPerLoop: {
					required: true,
					number: true
				}
			},
			submitHandler: function(form) {
				hac.global.notify("Running test...");
				$('#resultOverall').fadeOut();
				$('#runOverall').html(buttonTextRunOverall + ' ' + hac.global.getSpinnerImg());
				$('#runOverall').attr('disabled', true);
				var url = $('#runOverall').attr('data-url');
				$.ajax({
					url : url,
					type : 'POST',
					data : 'seconds=' + $('#secondsPerLoop').val(),
					headers : {
						'Accept' : 'application/json',
                        'X-CSRF-TOKEN' : token
					},
					success : function(data) {
						debug.log(data);

						var resultTable = $('#resultOverallTable').dataTable();
						var dataResults = [];
						$.each(data, function(index,result) {
							dataResults.push([result.name, result.count, result.perSecond ]);
						});
						resultTable.fnClearTable();
						resultTable.fnAddData(dataResults);

						$('#runOverall').html(buttonTextRunOverall);
						$('#resultOverall').fadeIn();
						$('#runOverall').removeAttr('disabled');
					},
					error : hac.global.err
				});				
			}
		});
		$('#overallForm').submit(function(){
			return false;
		});		
	});
});
