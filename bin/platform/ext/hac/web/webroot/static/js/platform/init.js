$(function() {
	var locked = Boolean.parse($('#lockInfo').attr('data-locked'));
	var unlockable = Boolean.parse($('#lockInfo').attr('data-unlockable'));
	var initialized = Boolean.parse($('#lockInfo').attr('data-initialized'));	
	var initUpdateExecuteURL = $('#initUpdateForm').attr('data-url');
	
	var buttonInitText = "Initialize";
	var buttonUpdateText = "Update";

	var updateIntervalID;	
	var type = $('#typeHeading').attr('data-initType'); 
	
	initUI();
	
	$('#toggleAll').click(toggleProjectData);
	
	$('#sqlScripts').click(function(){
		if (type == "UPDATE") {
			location.href=$('#sqlScripts').attr('data-url');
		}else{
			location.href=$('#sqlScripts').attr('data-url')+"?init=true";
		}
	});

	$('#dumpConfiguration').click(function(){
		var url = $('#dumpConfiguration').attr('data-url');
		var token = $("meta[name='_csrf']").attr("content");
		var postData = prepareInitUpdateData();

		$.ajax({
			url: url,
			type: 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json; charset=utf-8',
				'X-CSRF-TOKEN': token
			},
			data: JSON.stringify(postData),
			success: function (data) {
				var preContainer = $('<pre/>');
				preContainer.append(JSON.stringify(data, undefined, 2))

				$('#updInitConfigContainer').html("");
				$( "#updInitConfigContainer" ).dialog({
					  title: "Configuration: "
					});
				$('#updInitConfigContainer').html(preContainer);
				$('#updInitConfigContainer').dialog({'modal': true, 'height': 800, 'width': 600});
				$('#updInitConfigContainer').dialog('open');

				return false;
			},
			error: hac.global.err
		});
	});

	$('.buttonExecute').click(function(e){
		
		var needConfirmation = true;
		if (type == "UPDATE") {
			needConfirmation = false;
		}
		
		if (needConfirmation) {
			if (confirm("Are you sure you want to initialize system? It will destroy all current data.")) {
				prepareAndExecuteInitUpdate(e);
				return true;
			} else {
				return false;
			}			
		} else {
			prepareAndExecuteInitUpdate(e);
		}
	});

	var prepareInitUpdateData = function() {
		var postData = {};

		postData.dropTables = $('#dropTables').is(':checked') ? true : false;
		postData.clearHMC = $('#clearHMC').is(':checked') ? true : false;
		postData.createEssentialData = $('#createEssentialData').is(':checked') ? true : false;
		postData.localizeTypes = $('#localizeTypes').is(':checked') ? true : false;
		postData.allParameters = {};
		postData.patches = {}
		postData.createProjectData = $('#toggleAll').is(':checked') ? true: false;

		$('#projectData input[type=checkbox]').each(function () {
			if ($(this).is(':checked'))
				postData.allParameters[this.id] = "true";
		});

		$('#projectData select').each(function (e) {
			var values = [];

			$(this).find('option:selected').each(function () {
				values[values.length] = $(this).val();
			});

			postData.allParameters[this.id] = values;
		});

		$('#projectData input[type=text]').each(function () {
			postData.allParameters[this.id] = this.value;
		});

        var patches = {};
        $('#patches input[type=checkbox]').each(function () {

            if ($(this).is(':checked')) {
                var res = this.id.split('_');
                var extName = res[0];
                var hash = res[1];

                debug.log(extName);
                debug.log(hash);

                if (!patches[extName])
                {
                    patches[extName] = [];
                }

                patches[extName].push(hash);
            }

        })
        postData.patches = patches;

		if (type == "UPDATE" && !$('#initMethod').is(':checked')) {
			// in case when someone do not want to update system but trigger other
			// options like "clear hmc config" or "localize types" only
			postData.initMethod = null;
		} else {
			postData.initMethod = type;
		}

		return postData;
	}

	var prepareAndExecuteInitUpdate = function() {
		// register unload event handler only if this session triggered the init/update
		$(window).bind('beforeunload', null, beforeUnloadFunc);

		// the data to send to the server
		var postData = prepareInitUpdateData()

		// prevent double post
		$('.buttonExecute').attr('disabled', true);
		$('.buttonExecute').html(getButtonText() + ' ' + hac.global.getSpinnerImg());

		hac.global.notify("Please wait...");
		startLogUpdate();

		// ajax init/update call
		executeInitUpdate(initUpdateExecuteURL, postData);
	}
	
	$("body").on('click', '#rerunUpdate',function() {
		var url = $('#initUpdateForm').attr('data-url');
		var postData = hac.global.getObjectFromLocalStorage('rerunUpdate');
		startLogUpdate();
		if (!$.isEmptyObject(postData)) {
			executeInitUpdate(initUpdateExecuteURL, postData);
		}
	})

	var executeInitUpdate = function (url, postData) {
		var token = $("meta[name='_csrf']").attr("content");

		$.ajax({
			url: url,
			type: 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json; charset=utf-8',
				'X-CSRF-TOKEN': token
			},
			data: JSON.stringify(postData),
			success: function (data) {
				if (data.success) {
					var logContainer = $('#inner').html(data.log);

					if (data.initUpdateData.initMethod == null) {
						logContainer.append('<button id="rerunUpdate">Execute again</button>')
						hac.global.saveToLocalStorage('rerunUpdate', data.initUpdateData);
					}
				}
				else {
					hac.global.error("Trouble... check logs!");
				}
				stopLogUpdate();
			},
			error: hac.global.err
		});
	};
	
	$("body").on('click', '.extensionBox input[type="checkbox"]',function(){
		var checked = $(this).is(':checked') ? true : false;
		$(this).closest('.extensionBox').removeClass('active inactive').addClass(checked ? 'active' : 'inactive');
	});
	
	$("#lockButton").click(function(e) {
		
		var lock = (!locked) ? true : false;
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#lockButton').attr('data-url');
		debug.log(url);
		$.ajax({
			url : url,
			type : 'POST',
			data : "lock=" + lock,
            headers: { 'X-CSRF-TOKEN' : token },
			success : function(data) {
				locked = lock;
				initLock();
			},
			error : function(xhr, textStatus, errorThrown) {
				hac.global.modalDialog(errorThrown);
			}
		});
	});	
	
	
	// Functions
	function initLock() {
		debug.log("Locked: " + locked);
		
		if (locked) {
			$('#lockInfoWrapper').show();
			$("#initUpdateForm").hide();
		} else {
			$('#lockInfoWrapper').hide();
			$("#initUpdateForm").show();
		}
	
		if (locked && unlockable) {
			$("#lockButton").html("Unlock");
			$("#lockAdditionalInfo").html("Click <strong>Unlock</strong> button to enable these functions.");
		} else if (locked && !unlockable) {
			$("#lockButton").html("Locked, cannot be unlocked");
			$("#lockButton").attr("disabled", "disabled");
			$("#lockButton").attr("disabled", "disabled");
			$("#lockAdditionalInfo").html("Change <strong>system.unlocking.disabled</strong> key to <strong>false</strong> <a href=\"" + hac.contextPath +  "/platform/config\">here</a> on the fly or in <strong>local.properties</strong> or <strong>project.properties</strong> file and rebuild the system to change it permanently.");
		} else {
			$("#lockButton").html("Lock");
			$("#lockInfo").html("");
		}
	}	
	
	function initUI() {
		$('.buttonExecute').attr("disabled", "disabled");
		initLock();
		
		$('.buttonExecute').html(getButtonText());
		
		if (type == "INIT") {
			// Remove "initMethod" checkbox when method is init
			$('#initMethodWrapper').remove();
			$('#initOptions').hide();
			$('#requiredForInit').hide();
			$('#dropTables').attr('checked', true);
		}
		else if (type == "UPDATE") {
			$('#typeHeading').html('Update');
			$('#initOptions').hide();
			$('#dropTables').attr('checked', false);
			$('#requiredForInit').show();
		}
		
		loadData();

        loadSystemPatches();
	}

	function toggleProjectData() {
		var checked = $('#toggleAll').is(':checked') ? true : false;
		
		$('#projectData input[type=checkbox]').each(function (e) {
			$(this).attr('checked', checked);
		});
		
		$('#projectData .extensionBox').removeClass('active inactive').addClass(checked ? 'active' : 'inactive');
	}

	function loadData() {
		var url = $('#initOptions').attr('data-initUiDataUrl');
        var token = $("meta[name='_csrf']").attr("content");

		$.ajax({
			url:url,
			type:'GET',
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				if (data.isInitializing) {
					hac.global.error('Current system is initializing: ' + data.initInfo, 10000);
					$('.buttonExecute').attr('disabled', true);
					startLogUpdate();					
				}
				else {	
					$('#tenantID').html(data.tenantID);
					$('#master').html(data.master ? "true" : "false");
					
					$.each(data.projectDatas, function(key, projectData) {
						$('#projectData').append(renderProjectDataUI(projectData));						
					});
					
					// if it's an update, disable allcheckboxes in project data by default
					if (type === "UPDATE") {
						$('#toggleAll').attr('checked', false);
						toggleProjectData();
					}
				}
				
				$('.buttonExecute').removeAttr("disabled");
				
			},
			error: hac.global.err
		});
	}

	function loadSystemPatches() {
        var url = $('#initOptions').attr('data-systemPatchesDataUrl');
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
            url:url,
            type:'GET',
            headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
            success: function(data) {
                if (data.isInitializing) {
                    hac.global.error('Current system is initializing: ' + data.initInfo, 10000);
                    $('.buttonExecute').attr('disabled', true);
                    startLogUpdate();
                }
                else {

                    if (!$.isEmptyObject(data)) {
                        $.each(data, function (extName, patches) {
                            $('#patches').append(renderPatchDataUI(extName, patches));
                        });
                    }
                }

                $('.buttonExecute').removeAttr("disabled");

            },
            error: hac.global.err
        });
    }

	function getButtonText() {
		if (type == "INIT")
			return buttonInitText;
		else if (type == "UPDATE")
			return buttonUpdateText;
		else
			return "MISSING";
	}

	function beforeUnloadFunc() {
		return "Leaving this page during initialization is an unwise thing to do.";
	}

	function startLogUpdate() {
		$('#inner').html('');
		updateIntervalID = setInterval(updateLog, 2000);	
	}

	function stopLogUpdate() {
		$(window).unbind('beforeunload', beforeUnloadFunc);
		
		if (updateIntervalID)
			clearInterval(updateIntervalID);
	}

	function updateLog()
	{
		var url = $('#inner').attr('data-updateLogUrl');
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
			url:url,
			type:'GET',
			cache: false,
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				if (data.log || data.log === "")
					$('#inner').html(data.log);
				else if (data.indexOf("redirect_detection") != -1) {
						// redirect back home, login screen, if 302 is sent
						location.href= hac.homeLink + "/platform/" + type.toLowerCase();
					}
			},			
			error: hac.global.err
		});		
	}

    function renderPatchDataUI(extName, patches) {
        var html = $('<div class="extensionBox" />');

        if (!$.isEmptyObject(patches)) {
            $.each(patches, function (key, patchData) {
                var elementId = jQuery('<div />').text(extName + '_' + patchData.hash).html();
                var description = jQuery('<div />').text(patchData.description).html();
                var checkbox;
                var label;

                if (patchData.process == 'ALL' || patchData.process == type) {
                    if (patchData.required) {
                        checkbox = $('<input type="checkbox" id="' + elementId + '" checked="checked" onclick="return false;" />')
                        label = $('<label for="' + elementId + '" class="required" title="' + description + '"/>').text(extName + " - " + patchData.name);
                    } else {
                        checkbox = $('<input type="checkbox" id="' + elementId + '" checked="checked" />');
                        label = $('<label for="' + elementId + '" title="' + description + '"/>').text(extName + " - " + patchData.name);
                    }

                    html.append(checkbox);

                    html.append(label).append('<br />');
                }
            });
        }

        return html;
    }

	function renderProjectDataUI(projectData) {
		var html = $('<div class="extensionBox" />');
		html.append($('<input type="checkbox" id="' + projectData.name + '_sample" checked="checked" />'))
		var label = $('<label for="' + projectData.name + '_sample" />').append(projectData.name);
		html.append(label).append('<br />');
		html.append(renderProjectOptionsUI(projectData.parameter, projectData.name));
		
		return html;
	}
	
	function renderProjectOptionsUI(projectDataParam, projectDataName) {
		var html;
		
		if (!$.isEmptyObject(projectDataParam)) {
			html = $('<dl/>');
			$.each(projectDataParam, function(key, parameter) {
				var parameterId = projectDataName + '_' + parameter.name;
				var dt = $('<dt/>').append(parameter.label);
				var dd = $('<dd/>');
				
				if (parameter.values) {
					var select = $('<select id="' + parameterId + '" ' + ((parameter.multiSelect) ? 'multiple="multiple" size="' + parameter.values.length + '"' : '') + '/>');
					
					$.each(parameter.values, function(key, selected) {
						select.append($('<option value="' + key + '" ' + ((selected) ? 'selected="selected"' : '') + '/>').append(key));
					});
					
					dd.append(select);
				} else {
					dd.append($('<input type="text" id="' + parameterId + '" value="' + parameter["default"] + '"/>'));
				}
				
				html.append(dt);
				html.append(dd);
			});
		}
		
		return html
	}
});