var beanTable;

$(document).ready(
		function() {
            var token = $("meta[name='_csrf']").attr("content");

            beanTable = $('#beans').dataTable({
				"bPaginate" : false,
				"bStateSave" : true,
				"bFilter" : false,
				"aaSorting" : [ [ 0, "asc" ] ],
				"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 3 ] } ],
				"bInfo" : false
			});

			$("body").on(
					"change", "input[type=checkbox]",
					function(e) {
						var url = $('#beans').attr('data-toggleUrl') + encodeURIComponent(e.currentTarget.id);
								+ encodeURIComponent(e.currentTarget.id);
						$.ajax({
							url : url,
							type : 'POST',
							cache:false,
							headers : {
								'Accept' : 'application/json',
                                'X-CSRF-TOKEN' : token
							},
							success : function(data) {
								rebuildBeansTable(data);
							},
							error : hac.global.err
						});

					});

			getData(token);

		});

function getData(token) {
	var url = $('#beans').attr('data-url');
	$.ajax({
		url : url,
		type : 'GET',
		cache : false,
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			rebuildBeansTable(data);
		},
		error : hac.global.err
	});

}

function rebuildBeansTable(data) {
	beanTable.fnClearTable();

	for (pos in data) {
		var bean = data[pos];
		beanTable.fnAddData([ bean.key, bean.domain, bean.objectName,
				getToggleButton(bean.registered, bean.key) ]);
	}
}

function getToggleButton(registered, id) {
	return '<div class="onoffswitch">' +
    	'<input type="checkbox" class="onoffswitch-checkbox" id="'+ id + '"  '+ ((registered) ? 'checked' : '') +' />'+
    	'<label class="onoffswitch-label" for="' + id + '">'+
        '<div class="onoffswitch-inner" _on="ON" _off="OFF"></div>'+
        '<div class="onoffswitch-switch"></div>'+
        '</label>'+
        '</div>';
}