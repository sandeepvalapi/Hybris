$(function() {
	var tabs, editor;
		
	tabs = $("#tabs").tabs();
	
	editor = CodeMirror.fromTextArea(document.getElementById("dump"), {
		mode: "text/x-dump",
		lineNumbers: true,
		styleActiveLine: true,
		readOnly: true,
		extraKeys: {
	        "F11": function(cm) {
	        	setFullScreen(cm, !isFullScreen(cm));
	        },
	        "Esc": function(cm) {
	        	if (isFullScreen(cm)) setFullScreen(cm, false);
	        }
	    }
	});
	
	editor.refresh();
	$("#textarea-container").resizable({ maxWidth: 920 }).height("500px").width("920px");
	
	$("#startForm").submit(function() {
		var interval = $("#interval").val();
		
		$.ajax({
			url : $("#startForm").attr("startUrl"),
			type : 'GET',
			data: {
				interval: interval
			},
			headers : {
				'Accept' : 'application/json'
			},
			success : function(data) {
				hac.global.notify(data.status);
			},
			error : hac.global.err
		}).done(function(data) {
			$("#stopAction").attr("value", "Stop/Download");
			$("#stopAction").removeAttr("disabled");
		});
		
		return false;
	});
	
	$("#stopForm").submit(function(){
		hac.global.notify("Collecting stopped. Downloading...");
	});
});