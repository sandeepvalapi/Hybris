$(function() {
	$("#tabsNoSidebar").tabs();

	$('#systemEnvironment').dataTable({
		"bStateSave" : true,
		"bAutoWidth" : false,
		"iDisplayLength" : 50,
		"aLengthMenu" : [[10,25,50,100,-1], [10,25,50,100,'all']]
		
	});

	$('#systemProperties').dataTable({
		"bStateSave" : true,
		"bAutoWidth" : false,
		"iDisplayLength" : 50,
		"aLengthMenu" : [[10,25,50,100,-1], [10,25,50,100,'all']]
	});

});