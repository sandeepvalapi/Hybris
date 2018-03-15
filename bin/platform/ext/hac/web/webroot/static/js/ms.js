$(window).load(function() {
	var url = $('#notification').attr('data-hurl');
	var data = $('#notification').attr('data-pl');
	var lid = $('#notification').attr('data-lid');
	
	$.ajax({
		url: url,
		data: {
			ed: data,
			lid: lid, 
		},
		type:'POST'
	});	
});	