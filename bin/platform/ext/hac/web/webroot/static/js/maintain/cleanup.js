$(function() {
    var token = $("meta[name='_csrf']").attr("content");
	var mediaNotHybrisTable, mediaHybrisTable;
	
	$('#spinnerWrapper').hide() // hide it initially
	.ajaxStart(function() {
		$('#spinnerWrapper').show();
		$('#orphansContent').hide();
        $('#orphansMedia').hide();
		$('#luceneIndexesContent').hide();
		$('#orphansContent').hide();
	}).ajaxStop(function() {
		$('#spinnerWrapper').hide();
		$('#orphansContent').show();
        $('#orphansMedia').show();
		$('#luceneIndexesContent').show();
		$('#orphansContent').show();
	});
	
	$("#tabs").tabs({
		activate : function(event, ui) {
			var selectedIndex = ui.newPanel.attr('id').replace(/^.*-/, '');

			toggleActiveSidebar(selectedIndex);
			
			if (selectedIndex == 1)
				getTypesData();	
			else if (selectedIndex == 2)
				loadCleanupLucene();
			else if (selectedIndex == 3)
				loadCleanupMedia();
		}
	});

	mediaHybrisTable = $('#mediaHybrisTable').dataTable({
		"aaSorting" : [ [ 0, "asc" ] ]
	});
	
	mediaNotHybrisTable = $('#mediaNotHybrisTable').dataTable({
		"aaSorting" : [ [ 0, "asc" ] ]
	});
	toggleRemoveTypeSystemsButton();
	$('#buttonRemoveOrphaned').click(function() {
		var url = $('#buttonRemoveOrphaned').attr("data-url");
		$.ajax({
			url : url,
			type : 'POST',
			data : 'deleteRelated='
					+ ($('#deleteRelated:checked')
							.val() ? true : false)
					+ '&deleteInstances='
					+ ($('#deleteInstances:checked')
							.val() ? true : false),
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				debug.log(data);
				showOrphanRemovalResult(data);
				$('#removalControls').hide();
				hac.global
						.notify('Orphaned types have been removed. Details below.');
	
			},
			error : hac.global.err
		});
	});

	$('#buttonRemoveLuceneIndexes').click(function() {
		var url = $('#buttonRemoveLuceneIndexes').attr("data-url");
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				if (data.success == true) {
					var resultMsg = $('#luceneRemovalResult').attr('data-successMsg');
					hac.global.notify(resultMsg);
				} else {
					var resultMsg = $('#luceneRemovalResult').attr('data-failureMsg');
					if (data.reason) {
						resultMsg = resultMsg + "<br />" + data.reason;
					}
					hac.global.error(resultMsg);
				}
			},
			error : hac.global.err
		});

	});

	$('#buttonRemoveOrphanedMedia').click(function() {
		var url = 'maintenance/cleanup/media/remove';
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				debug.log(data);
				updateMediaUI(data);
			},
			error : hac.global.err
		});

	});
	toggleRemoveTypeSystemsButton();
	$('#buttonRemoveTypeSystem').click(function() {
		var url = $('#buttonRemoveTypeSystem').attr("data-url");
		$.ajax({
			url : url,
			type : 'POST',
			data : 'typeSystemName='+ $('#typeSystem').find(":selected").val(),
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				debug.log(data);
				if(data.error!=null){
					hac.global.err(data.message);
				}else{
					hac.global.notify(data.message);
					$('#typeSystem').find(":selected").remove();
					toggleRemoveTypeSystemsButton();
				}

			},
			error : hac.global.err
		});
	});

	getTypesData();

});

function showOrphanRemovalResult(data) {
	$('#orphansUL').html('');
	var html = '';

	for ( var pos in data) {
		var typeObj = data[pos];
		html += '<li>' + typeObj.type
				+ (typeObj.removed ? ' removed' : ' could not be removed')
				+ '</li>';
	}

	$('#orphansUL').append(html);
}

function toggleActiveSidebar(num) {
	// fadeout
	$("div[id^=sidebar]").each(function() {
		$(this).fadeOut('fast');
	});

	setTimeout("$('#sidebar" + num + "').fadeIn('fast');", 500);
}

function loadCleanupLucene() {
    var token = $("meta[name='_csrf']").attr("content");
	var url = 'maintenance/cleanup/lucene/data';
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			debug.log(data);
			if (data.length == 0) {
				hac.global.notify('There are no Lucene search indexes.');
				$('#buttonRemoveLuceneIndexes').attr("disabled", true);
				$('#tabs-2').css('opacity', '0.5');
			} else {
				$('#buttonRemoveLuceneIndexes').attr("disabled", false);
				$('#tabs-2').css('opacity', '1');

				$('#indexes').html('');

				var indexes = [];
				$.each(data, function(i, indexData) {
					var row = [indexData.code, indexData.pk, indexData.rebuildStartDate, indexData.rebuildEndDate, hac.global.booleanIcon(indexData.upToDate)];
					indexes.push(row);
				});
				var luceneIndexesTable = $('#luceneIndexes').dataTable();
				luceneIndexesTable.fnClearTable();
				luceneIndexesTable.fnAddData(indexes);
			}

		},
		error : hac.global.err
	});
}

function loadCleanupMedia() {
    var token = $("meta[name='_csrf']").attr("content");
	var url = 'maintenance/cleanup/media/data';
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			debug.log(data);
			updateMediaUI(data);
		},
		error : hac.global.err
	});

}

function updateMediaUI(data) {
	$('#filesOverall').html(data.filesOverall);
	$('#filesUsed').html(data.filesUsed);
	$('#filesOrphaned').html(data.filesOrphaned);
	$('#filesNotHybrisMedia').html(data.filesNotHybrisMedia);
	$('#scannedDirs').html(data.scannedDirs.join(', '));

	if (data.filesNotHybrisMedia == 0) {
		$('#mediaNotHybrisWrapper').fadeOut();
	} else {
		$('#mediaNotHybrisWrapper').fadeIn();
		$('#mediaNotHybrisTable').dataTable({
			"aaSorting" : [ [ 0, "asc" ] ],
			"bRetrieve" : true
		}).fnClearTable();		
		for ( var pos in data.notHybrisMedia) {
			var file = data.notHybrisMedia[pos];
			$('#mediaNotHybrisTable').dataTable({
				"aaSorting" : [ [ 0, "asc" ] ],
				"bRetrieve" : true
			}).fnAddData([ file.name, file.absolutePath ]);
		}
	}

	debug.log("Files orphaned: " + data.filesOrphaned);
	if (data.filesOrphaned == 0) {
		$('#mediaHybrisWrapper').fadeOut();
	} else {
		$('#mediaHybrisWrapper').fadeIn();
		$('#mediaHybrisTable').dataTable({
			"aaSorting" : [ [ 0, "asc" ] ],
			"bRetrieve" : true
		}).fnClearTable();		
		for ( var pos in data.orphanedMedia) {
			var file = data.orphanedMedia[pos];
			$('#mediaHybrisTable').dataTable({
				"aaSorting" : [ [ 0, "asc" ] ],
				"bRetrieve" : true
			}).fnAddData([ file.name, file.absolutePath ]);
		}
	}
}

function getTypesData() {
    var token = $("meta[name='_csrf']").attr("content");
	var url = 'maintenance/cleanup/types/data';
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			debug.log(data);
			if (data.totalOrphanedTypes == 0) {
				hac.global.notify('There are no orphaned types.');
				$('#buttonRemoveOrphaned').attr("disabled", true);
				$('#deleteInstances').attr('disabled', true);
				$('#deleteRelated').attr('disabled', true);
				$('#tabs-1').css('opacity', '0.5');
			} else {
				rebuildOrphansInfo(data);
				$('#buttonRemoveOrphaned').attr("disabled", false);
				$('#deleteInstances').attr('disabled', false);
				$('#deleteRelated').attr('disabled', false);
				$('#tabs-1').css('opacity', '1');
			}
		},
		error : hac.global.err
	});
}

function rebuildOrphansInfo(data) {
	$('#orphansUL').html('');
	for ( var pos in data.extensions) {
		var extObj = data.extensions[pos];

		var html = '';
		html += '<li>Extension: ' + extObj.extension;

		html += '<ul>';
		for ( var pos2 in extObj.orphanedTypes) {
			var orphan = extObj.orphanedTypes[pos2];
			html += '<li>' + orphan.composedTypeCode + ' <strong>'
					+ orphan.code + '</strong> [' + orphan.pk + ']';

			html += '</li>';
		}
		html += '</ul>';

		html += '</li>';

		$('#orphansUL').append(html);
	}

	$('#orphans').fadeIn();

}

function toggleRemoveTypeSystemsButton(){
	if( $('#typeSystem option').length==0 )
	{
		$('#typeSystemsToRemove').css('display','none')
		$('#noTypeSystemsToRemove').css('display','block');
	}
	else
	{
		$('#typeSystemsToRemove').css('display','block');
		$('#noTypeSystemsToRemove').css('display','none');
	}

}