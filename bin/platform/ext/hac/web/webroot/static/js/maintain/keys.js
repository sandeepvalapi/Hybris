var tableKeys, tableTypes, tableCreditCards;

$(document).ready(
	function() {
        var token = $("meta[name='_csrf']").attr("content");

        $("#tabs").tabs({
        		activate : function(event, ui) {
				hac.global.toggleActiveSidebar(ui.newPanel.attr('id').replace(/^.*-/, ''));
	
				if (ui.newPanel.attr('id') == 'tabs-2') {
					loadMigrationData(token);
				} else if (ui.newPanel.attr('id') == 'tabs-3') {
					loadUnencryptedCardsData(token);
				}
			}
	
		});
	
		tableKeys = $('#tableKeys').dataTable({
			"bPaginate" : false,
			"bFilter" : false,
			"bInfo" : false,
			"aaSorting" : [ [ 0, "asc" ] ]
		});
	
		tableTypes = $('#tableTypes').dataTable({
			"bPaginate" : false,
			"bFilter" : false,
			"bInfo" : false,
			"aaSorting" : [ [ 0, "asc" ] ]
		});
		
		$.validator.addMethod("correctFileName", function(value, element) { 
				return this.optional(element) || /^([a-zA-Z0-9\-_\.]*)$/i.test(value); 
		 	}, 
		 	"Incorect file name"
		);					

	$('#buttonGenerate').click(function() {
		$('#generateKeyForm').validate({
			rules: {
				fileName: {
					required: true,
					correctFileName: true
				}
			},
			submitHandler: function(form) {
				var url = $('#buttonGenerate').attr('data-url');
				$.ajax({
					url : url,
					type : 'POST',
					headers : {
                        'Accept' : 'application/json',
                        'X-CSRF-TOKEN' : token
                    },
					data : 'fileName=' + encodeURIComponent($('#fileName').val()) + '&keySize=' + $('#keySize :selected').val(),
					success : function(data) {
						debug.log(data);
						
						if (data.success) {
							hac.global.notify("Key file created.");
							
							$('#generatedFile').html(data.generatedFile);
							$('#result').fadeIn();
						} else {
							hac.global.error("Unable to write file. Make sure the file does not exist and the file name is valid.");
						}
					},
					error : hac.global.err
				});										
			}
		})
		$('#generateKeyForm').submit(function(){
			return false;
		});								
	});

	$('#buttonMigrate').click(function() {
		// collect data
		var toMigrate = [];
		$('.migrateType').each(function() {
			toMigrate[toMigrate.length] = this.id;
			});

			debug.log(toMigrate);

			var url = $('#buttonMigrate').attr('data-url');
			$.ajax({
				url : url,
				type : 'POST',
				data : 'types=' + encodeURIComponent(toMigrate.join('|')),
				headers : {
                    'Accept' : 'application/json',
                    'X-CSRF-TOKEN' : token
                },
				success : function(data) {
					debug.log(data);
					hac.global.notify("Migrating types...");
					$('#migrationResult').html(data.htmlOutput + "<br/>DONE");
				},
				error : hac.global.err
			});
		});
	
	// Credit cards encryption
	$('#encryptCards').click(function() {
		$('#spinnerWrapper').show();
		var url = $('#encryptCards').attr('data-url');
		$.ajax({
			url: url,
			type: 'POST',
			headers: {
                'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				$('#spinnerWrapper').hide();
				// Redraw table
				tableCreditCards.fnDraw();
			},
			error: hac.global.err
		});
	});

});

function loadMigrationData(token) {
	var url = $('#tabs-2').attr('data-loadUrl');
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			debug.log(data);
			if (data.action == "showinfo") {
				$('#keys').hide();
				$('#info').fadeIn();
			} else if (data.action == "showkeys") {
				$('#info').hide();
				$('#keys').fadeIn();

				// existing keys and default key table
				tableKeys.fnClearTable();
				for ( var id in data.keys) {
					var fileName = data.keys[id];

					if (id == data.defaultKey.id)
						id = id + " (default)";

					tableKeys.fnAddData([ id, fileName ]);
				}

				// types with encryptes attributes
				tableTypes.fnClearTable();
				for ( var pos in data.encrypted) {
					var type = data.encrypted[pos];

					for ( var attrPos in type.attributes) {
						var attribute = type.attributes[attrPos];
						tableTypes.fnAddData([
								getCheckbox(type.code + "."
										+ attribute.qualifier), type.code,
								attribute.qualifier, type.count ]);
					}
				}

			}
		},
		error : hac.global.err
	});
}

function buildCardRowsForTable(data) {
	var rows = [];
	$.each(data.unencryptedCards, function(index, value) {
		var row = [];
		row.push(value.pk.longValueAsString);
		row.push(value.number);
		row.push(value.type);
		row.push(value.encrypted ? hac.global.trueIcon() : hac.global.falseIcon());
		
		rows.push(row);
	});
	return rows;
};

function loadUnencryptedCardsData(token) {
	$.ajax({
		url : $('#tabs-3').attr('data-checkEncryptionUrl'),
		type : 'GET',
		headers : {
            'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
        },
		success : function(encryptionEnabled) {
			if (encryptionEnabled) {
				tableCreditCards = $('#unenrcyptedCardsOverview').dataTable({
					"bDestroy": true,
					"bRetrieve": true,
					"bFilter": false,
			        "bProcessing": false,
			        "bServerSide": true,
			        "sAjaxSource": $('#tabs-3').attr('data-loadUrl'),
			        "fnInfoCallback": function(oSettings, iStart, iEnd, iMax, iTotal, sPre) {
						if (iTotal == 0) {
							hac.global.notify("No credit cards with unencrypted number have been found.");
							$('#encryptCards').attr("disabled", true);
							$('#tabs-3').css('opacity', '0.5');	
						}
			          }
				});										
			} else {
				hac.global.error("The attribute 'number' of the type 'CreditCardPaymentInfo' is defined as unencrypted!<br />" 
						+ "Change in core-items.xml the modifier of this attribute to 'encrypted=true' and update the system.", 15000);
				$('#encryptCards').attr("disabled", true);
				$('#tabs-3').css('opacity', '0.5');												
			}
		},
		error : hac.global.err
	});		
};

function getCheckbox(id) {
	return '<input type="checkbox" class="migrateType" id="' + id
			+ '" checked/>';
}