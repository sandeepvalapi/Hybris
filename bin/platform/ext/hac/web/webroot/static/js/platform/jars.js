var loadedJarsTable, loadedClassesTable;
var buttonText = "Show results";

$(document).ready(
		function() {
            var token = $("meta[name='_csrf']").attr("content");
			$("#tabs").tabs({
				activate: function(event, ui) {
					hac.global.toggleActiveSidebar(ui.newPanel.attr('id').replace(/^.*-/, ''));
				}
			});

			loadedJarsTable = $('#loadedJars').dataTable({ "aLengthMenu" : [[10,25,50,100,-1], [10,25,50,100,'all']] });
			loadedClassesTable = $('#loadedClasses').dataTable({ "aLengthMenu" : [[10,25,50,100,-1], [10,25,50,100,'all']] });

			var url = $('#tabs-1').attr("data-url");
			$.ajax({
				url : url,
				type : 'GET',
				headers : {
					'Accept' : 'application/json',
                    'X-CSRF-TOKEN' : token
				},
				success : function(data) {
					debug.log(data);

					var pos;
					for (pos in data.scopes) {
						var entry = data.scopes[pos];
						var option = '<option value="' + entry.key + '">'
								+ entry.value + '</option>';
						$('#scopeJars').append(option);
						$('#scopeClasses').append(option);
					}
				},
				error : hac.global.err
			});

			$('#buttonResultsJars').click(fetchJarsResults);
			$('#buttonResultsClasses').click(fetchClassesResults);

		});

function getResults(requestType) {
	var dataObj = {};

	dataObj.type = requestType;

	if (requestType == "jars") {
		$('#loadedJars_wrapper').fadeOut();
		$('#buttonResultsJars').html(
				buttonText + ' ' + hac.global.getSpinnerImg());
		$('#buttonResultsJars').attr('disabled', 'true');
		dataObj.extension = $('#scopeJars option:selected').val();
		dataObj.filter = $('#filterJars').val();
		dataObj.option = $('#duplicatesOnly').attr('checked') ? true : false;

	} else {
		$('#loadedClasses_wrapper').fadeOut();
		$('#buttonResultsClasses').html(
				buttonText + ' ' + hac.global.getSpinnerImg());
		$('#buttonResultsClasses').attr('disabled', 'true');
		dataObj.extension = $('#scopeClasses option:selected').val();
		dataObj.filter = $('#filterClasses').val();

		if (dataObj.filter === "")
			hac.global
					.notify("Not using a filter pattern might freeze the browser due to data overload. Please be patient...");

		dataObj.option = $('#allClasses').attr('checked') ? true : false;
	}

	debug.log(dataObj);

	var url = $('#tabs-1').attr("data-analyzeUrl");
    var token = $("meta[name='_csrf']").attr("content");
	$.ajax({
		url : url,
		type : 'POST',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		data : dataObj,
		success : function(data) {

			if (data.error) {
				hac.global.error('Check your filter: ' + data.error);
			} else {
				if (requestType == 'jars')
					refreshJarsUI(data.jars);
				else
					refreshClassesUI(data.classes);
			}

			$('#buttonResultsClasses').removeAttr('disabled');
			$('#buttonResultsJars').removeAttr('disabled');

		},
		error : hac.global.err
	});

}

function fetchJarsResults() {
	getResults("jars");
}

function fetchClassesResults() {
	getResults("classes");
}

function refreshJarsUI(jars) {
	loadedJarsTable.fnClearTable();

	var pos;
	for (pos in jars) {

		var info = jars[pos];

		// first column content
		var name = info.jarName;
		if (info.duplicated && !info.folder)
			name += " (" + info.occurrences + ")";
		name += '<br/>' + info.path;

		// second column
		var scope = info.classLoaderInfo;

		// third column
		var type = (info.folder) ? "folder" : "jar";

		loadedJarsTable.fnAddData([ name, scope, type ]);
	}

	// switch to full screen as we otherwise got a table width issue
	if (!hac.global.isFullWidth())
		hac.global.toggleSidebar();

	$('#buttonResultsJars').html(buttonText);
	$('#loadedJars_wrapper').fadeIn();

}

function refreshClassesUI(classes) {
	loadedClassesTable.fnClearTable();

	var className;
	for (className in classes) {

		var jars = classes[className]
		loadedClassesTable.fnAddData([ className, jars.join('<br/>') ]);
	}

	// switch to full screen as we otherwise got a table width issue
	if (!hac.global.isFullWidth())
		hac.global.toggleSidebar();

	$('#buttonResultsClasses').html(buttonText);
	$('#loadedClasses_wrapper').fadeIn();
}
