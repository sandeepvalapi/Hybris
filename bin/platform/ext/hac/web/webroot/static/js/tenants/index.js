$(function() {
	$('.activate').click(function() {
		hac.global.openurl($(this));
	})
	
	$('.edit').click(function() {
		hac.global.openurl($(this));
	});
	
	$('#createTenant').click(function() {
		hac.global.openurl($(this));
	});	
	
	$('#existingTenants').dataTable({
		 "bSort": false,
		 "iDisplayLength" : 25
	});
});