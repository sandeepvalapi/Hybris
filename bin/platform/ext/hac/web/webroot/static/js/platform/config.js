$(function() {
	var nrShown;
	var table, blockUser;
	var check = true;
	table = $('#props').dataTable({
		bStateSave : true,
		aaSorting : [[ 0, 'asc' ]],
		aoColumnDefs: [ { "bSortable": false, "aTargets": [ 1 ] } ],
		aLengthMenu : [[10,25,50,100,-1], [10,25,50,100,'all']],
		bSortCellsTop: true
	});
	
	enableButtons(true);

    var xssEntityMap = {
        "<": "&lt;",
        ">": "&gt;",
        "/": '&#x2F;',
        "&": "&amp;",
        '"': '&quot;',
        "'": '&#39;'
    };

	init();
	
	function rowChanged(row){
		$(row).attr("changed", "true");
		$(row).find("td").first().css("font-weight", "bold");
		$(row).find("td").first().css("font-style", "italic");
		$(row).find("input").first().css("outline", "#FFFFCE solid");
		$($(row).find("img")[1]).removeClass("applyIcon");
		$($(row).find("img")[1]).addClass("applyIconHighOpac");
	}
	
	function rowUnchanged(row){
		$(row).removeAttr("changed");
		$(row).find("td").first().css("font-weight", "normal");
		$(row).find("td").first().css("font-style", "normal");
		$(row).find("input").first().css("outline", "");
		$($(row).find("img")[1]).removeClass("applyIconHighOpac");
		$($(row).find("img")[1]).addClass("applyIcon");
	}
	
	function rowNew(row){
		$(row).attr("new", "true");
		$(row).find("td").first().css("font-weight", "bold");
		$(row).find("td").first().css("font-style", "italic");
		$(row).find("input").first().css("outline", "#c3d9ff solid");
		$($(row).find("img")[1]).removeClass("applyIcon");
		$($(row).find("img")[1]).addClass("applyIconHighOpac");
	}
	
	function rowOld(row){
		$(row).removeAttr("new");
		$(row).find("td").first().css("font-weight", "normal");
		$(row).find("td").first().css("font-style", "normal");
		$(row).find("input").first().css("outline", "");
		$($(row).find("img")[1]).removeClass("applyIconHighOpac");
		$($(row).find("img")[1]).addClass("applyIcon");
	}
	
	function enableButtons(enable){
		if(enable){
			$("#applyAllBtn").removeAttr("disabled").css("opacity","1");
			$("#resetAllBtn").removeAttr("disabled").css("opacity","1");
		}
		else{
			$("#applyAllBtn").attr("disabled", "disabled").css("opacity","0.3");
			$("#resetAllBtn").attr("disabled", "disabled").css("opacity","0.3");
		}
	}

	function getTDContentValue(configKey, configValue) {
		var td = "<div>";
		td += "<input type='text' class='configValue' name='"
				+ configKey + "' value='" + configValue + "'/>";
		td += "<div style='float:right'>";
		td += hac.global.removeImg();
		td += hac.global.applyImg();
		td += "</div>";
		td += "</div>";
		return td;
	}
	
	$("body").on("blur",'.configValue', function(e){
		var row = $(e.target).parent().parent().parent();
		var configKey = $(e.target).attr("name");
		var configValue = $(e.target).val();
		var data = valueChanged(configKey, configValue);
		if(data != null){
			if(data.changed){
				rowChanged(row);
			}
			else{
				rowUnchanged(row);
				if($(row).attr("new")){
					rowNew(row);
				}
			}
		}
	});
	
	function valueChanged(configKey, configValue){
		var url = $("#content").attr("valuechanged-url");
        var token = $("meta[name='_csrf']").attr("content");

        var responseData = null;
		$.ajax({
			url : url,
			type : 'POST',
			async: false,
			data : {
				key: configKey,
				val: configValue
			},
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				responseData = data;
			},
			error : hac.global.err
		});
		return responseData;
	}
	
	$('#toggleProperties').change(function(e) {
		$("#configTableWrapper").hide();
		table.fnFilter("");
		if(check && $('#toggleNewProps').is(":checked")){
			check = false;
			$('#toggleNewProps').click();
			check = true;
		}
		if($(e.target).is(":checked")){
			$.each(table.fnGetNodes(), function(key,value){
				if(!$(value).attr("changed") && !$(value).attr("new")){
					$(value).hide();
				}
			});
			var settings = table.fnSettings();
			nrShown = settings._iDisplayLength;
			settings._iDisplayLength = -1;
			table.fnDraw();
		}
		else{
			$.each(table.fnGetNodes(), function(key,value){
				$(value).show();
			});
			var settings = table.fnSettings();
			settings._iDisplayLength = nrShown;
			table.fnDraw();
		}
		$("#configTableWrapper").fadeIn();
	});
	
	$("#applyAllBtn").click(function(){
		var url = $("#content").attr("applyall-url");
        var token = $("meta[name='_csrf']").attr("content");

        enableButtons();
		$.ajax({
			url : url,
			type : 'POST',
			data : {},
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				
				$.each(table.fnGetNodes(),function(key,value){
					if($(value).attr("changed") || $(value).attr("new")){
						rowUnchanged(value);
						rowOld(value);
					}
				});
				if($("#toggleProperties").is(":checked")){
					$("#toggleProperties").click();
				}
				var notification = "";
				if(data.edited > 0){
					notification = "Value of " + data.edited + " key changed. ";
				}
				if(data.created > 0){
					notification += "Created " + data.created + " Property.";
				}
				hac.global.notify(notification);
			},
			error : hac.global.err
		});
	});
	
	$("#resetAllBtn").click(function(){
		var url = $("#content").attr("resetall-url");
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
			url : url,
			type : 'POST',
			data : {},
			async: false,
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				var toRemove = [];
				var props = data.edited;
				$.each(table.fnGetNodes(),function(key,value){
					if(props != null && $(value).attr("changed")){
						rowUnchanged(value);
						$(value).find("input").first().val(props[value.id]);
					}
					if($(value).attr("new")){
						toRemove.push(key);
					}
				});
				$.each(toRemove, function(arr_index, index){
					var node = table.fnGetNodes(index);
					debug.log(node);
					$(node).fadeOut('fast', function() {
						table.fnDeleteRow(node);
					});
				});
				table.fnDraw();
				if($("#toggleProperties").is(":checked")){
					$("#toggleProperties").click();
				}
				var notification = "";
				if(data.editedCount > 0){
					notification = "Value of " + data.editedCount + " properties set back. ";
				}
				if(data.createdCount > 0){
					notification += "Removed " + data.createdCount + " new Property.";
				}
				hac.global.notify(notification);
			},
			error : hac.global.err
		});
	});
			
	$("body").on("click", 'img.applyIconHighOpac', function(e) {			
		var row = $(e.target).parent().parent().parent().parent();
		var input = $(row).find("input").first();
		var url = $('#props').attr('data-configUpdUrl');
		var configKey = input.attr('name');
		var configValue = input.val();
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
			url : url,
			type : 'POST',
			data : {
				'key' : configKey,
				'val' : configValue
			},
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				
				if(data.action == "update"){
					rowUnchanged(row);
					hac.global.notify("Value of Key " + configKey + " changed.");
				}
				else{
					rowOld(row);
					hac.global.notify("Property " + configKey + " created.");
				}				
			},
			error : hac.global.err
		});		
	});
	
	$("body").on("click", 'img.removeIcon', function(e) {		
		if (blockUser)
			return;
		
		blockUser = true;
		
		var configKey = $(e.target).parent().prev('input').attr('name');
		var url = $('#props').attr('data-configDelUrl');
        var token = $("meta[name='_csrf']").attr("content");

        // tr node
		var node = this.parentNode.parentNode.parentNode.parentNode;
		debug.log(node);
		$.ajax({
			url : url,
			type : 'POST',
			data : {
				'key' : configKey
			},
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				
				$(node).fadeOut('fast', function() {
					table.fnDeleteRow(node);
					blockUser = false;
				});
				hac.global.notify("Key " + configKey + " was deleted.");
			},
			error : hac.global.err
		});
	});

	function sanitizeInput(string) {
		return String(string).replace(/[&<>"'\/]/g, function (s) {
			return xssEntityMap[s];
		});
	}

    function validateConfigKey(string) {
        var regex = new RegExp("^\\w+(\\.\\w+)*$"); // java property pattern, ex.: 'this.is.my.property.key'
        return regex.test(string);
    }

	$("body").on("click", '#addButton', function(e) {
		$(this).attr('disabled', true);
		
		var configKey = $("#configKey").val();
		var configValue = $("#configValue").val();

		configKey = sanitizeInput(configKey);
		configValue = sanitizeInput(configValue);

		if (!validateConfigKey(configKey))
		{
			hac.global.error("Please enter a valid key!");
			$(this).removeAttr('disabled');
			return;
		}
		
		$("#configKey").val("");
		$("#configValue").val("");
		
		var data = valueChanged(configKey, configValue);

		if(data.validationError)
		{
            hac.global.error("Validation error. Please enter a valid key!");
            $(this).removeAttr('disabled');
            return;
		}
		var row;
		if(data.isNew){
			var node = table.fnAddData([configKey,
			    getTDContentValue(configKey, configValue) ]);
			row = $(table.fnGetNodes(node));
			$(row).attr("new", "true");
			row.attr("id", configKey);
		}
		else{
			$.each(table.fnGetNodes(),function(key,value){
				if($(value).key == configKey){
					row = $(table.fnGetNodes(value));
				}
			});
		}
		rowNew(row);
		hac.global.notify("Key '" + configKey + "' was added, but not saved.");
		$(this).removeAttr('disabled');
	});
	
	function init(){
		$("#configTableWrapper").hide();
		var url = $("#content").attr("edited-url");
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
			url : url,
			type : 'POST',
			data : {},
			async: false,
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				enableButtons(data.hasEdited);
				
				var editedProps = data.edited;
				var newProps = data.created;
				$.each(table.fnGetNodes(),function(key,value){
					if(editedProps != null && editedProps[value.id]){
						rowChanged(value);
						$(value).find("input").first().val(editedProps[value.id]);
					}
				});
				if(newProps){
					$.each(newProps, function(key, value){
						var escapedKey = sanitizeInput(key);
						var node = table.fnAddData([escapedKey,
						   getTDContentValue(escapedKey, value) ]);
						var row = $(table.fnGetNodes(node));
						rowNew(row);
					});
				}
			},
			error : hac.global.err
		});
		
		var settings = table.fnSettings();
		settings._iDisplayLength = 10;
		$("#configTableWrapper").fadeIn();
		table.fnDraw();
	}
});