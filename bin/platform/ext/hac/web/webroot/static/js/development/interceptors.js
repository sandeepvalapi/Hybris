$(function() {
	$('#interceptors').dataTable({
		"iDisplayLength" : 50
	});
	
	$("body").on('click', '.interceptorName',function() {	
		var url = $('#interceptors').attr('data-checkInterceptorInterfaces');
		var interceptorClassName = $(this).attr('data-interceptorName');
		
		$.ajax({
			url : url,
			data : 'interceptorClassName=' + interceptorClassName,
			type : 'GET',
			headers : {
				'Accept' : 'application/json'
			},
			success : function(data) {
				if (data.error == null) {
					var list = renderItems(data.interfaces);
					$('#dialogContainer').dialog('option', 'title', 'Interceptor ' + data.interceptorName + ' implements:');
					$('#dialogContainer').html(list);
					$('#dialogContainer').dialog({'modal': true});
					$('#dialogContainer').dialog('open');
				} else {
					hac.global.error(data.error);
				}
				return false;
			},
			error : hac.global.err
	    });
	});
	
	$('#typeCodeShow').click(function() {
		var url = $('#interceptors').attr('data-checkInterceptedTypes');
		var interceptedType = $('#typeCodeSearchField').val();
		
		$.ajax({
			url: url,
			data : 'interceptedType=' + interceptedType,
			type : 'GET',		
			headers : {
				'Accept' : 'application/json'
			},		
			success : function(data) {
				displayAllInterceptorsDialog(data, interceptedType);		
			},
			error : hac.global.err
		});
	});
	
	$('#typeCodeSearchField').bind('keypress', function(event) {
		if (event.keyCode == 13) {
			var url = $('#interceptors').attr('data-checkInterceptedTypes');
			var interceptedType = $('#typeCodeSearchField').val();
			
			$.ajax({
				url: url,
				data : 'interceptedType=' + interceptedType,
				type : 'GET',		
				headers : {
					'Accept' : 'application/json'
				},		
				success : function(data) {
					displayAllInterceptorsDialog(data, interceptedType);		
				},
				error : hac.global.err
			});
		};
	});
	
	$("body").on('click', '.interceptedType',function() {
		var url = $('#interceptors').attr('data-checkInterceptedTypes');
		var interceptedType = $(this).attr('data-interceptedType');
		
		$.ajax({
			url : url,
			data : 'interceptedType=' + interceptedType,
			type : 'GET',
			headers : {
				'Accept' : 'application/json'
			},		
			success : function(data) {
				displayAllInterceptorsDialog(data, interceptedType);		
			},
			error : hac.global.err
		});
	});
	
	displayAllInterceptorsDialog = function(data, interceptedType) {
		if (data.error == null) {
			$('#dialogContainer').html("");
			$('#dialogContainer').dialog('option', 'title', 'All interceptors for type: ' + interceptedType);
			
			$.each(data, function(key, value) {
				$('#dialogContainer').append(interceptorHeader(key + ":")).append(renderItems(value));	
			})
			
			$('#dialogContainer').dialog({'modal': true});
			$('#dialogContainer').dialog('open');
		} else {
			hac.global.error(data.error);
		}
		return false;		
	};
	
	interceptorHeader = function(header) {
		var para = $('<strong />');
		para.append(header);
		return para;
	}
	
	renderItems = function(items) {
		var para = $('<div/>');
		$.each(items, function(index, value) {
			para.append($('<p/>')).append(value);
		});
		
		return para;
	};
	
	$("body").on('click', '.overridedBy',function() {
		$('#dialogContainer').html("");
		$('#dialogContainer').dialog('option', 'title', 'Replacing interceptors');
		$('#dialogContainer').append($('span', $(this).parent()).html());
		$('#dialogContainer').dialog({'modal': true});
		$('#dialogContainer').dialog('open');		
	});
	
	$("#dialogContainer").dialog({
		height: "auto",
		width: "auto",
		autoOpen: false
	});
})