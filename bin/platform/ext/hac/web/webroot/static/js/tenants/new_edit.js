$(function() {
	
	$('#initialize').click(function() {
		hac.global.openurl($(this));
	});
	
	$('#configureExt').click(function() {
		hac.global.openurl($(this));
	});
	
	$('#delete').click(function() {
		var confirmMsg = $('#delete').attr('data-confirm-msg');
		if (confirm(confirmMsg)) {
			$('#deleteTenantForm').submit();
		} else {
			return false;
		}
	});
	
	$('#unInitialize').click(function() {
		var confirmMsg = $('#unInitialize').attr('data-confirm-msg');
		if (confirm(confirmMsg)) {
			$('#unInitializeForm').submit();
		} else {
			return false;
		}
	});	
	
	$('#extensions').dataTable({
		"bPaginate" : false,
		"bStateSave" : true,
		"bFilter" : false,
		"aaSorting" : [ [ 0, "asc" ] ],
		"bInfo" : false
	});
});