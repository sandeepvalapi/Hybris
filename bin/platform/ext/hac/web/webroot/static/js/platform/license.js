$(document).ready(function() {
	$('#licenseInfos').dataTable({
		"bPaginate" : false,
		"bFilter" : false,
		"bInfo" : false,
		"aaSorting" : [ [ 0, "asc" ] ]

	});

	$('#licenseProperties').dataTable({
		"bPaginate" : false,
		"bFilter" : false,
		"bInfo" : false,
		"aaSorting" : [ [ 0, "asc" ] ]
	});

	$('#demoLicence').dataTable({
		"bPaginate" : false,
		"bFilter" : false,
		"bInfo" : false,
		"aaSorting" : [ [ 0, "asc" ] ]
	});
});