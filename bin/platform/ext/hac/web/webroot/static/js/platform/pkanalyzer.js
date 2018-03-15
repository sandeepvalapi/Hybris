/**
 * Script responsible for getting all data from PK analyze and for rendering informations on the page
 * 
 * @author Piotr 'piter' Hlawski
 */
$(function() {
	// Hide result divs and messages at the beginning
	$("#possibleException").hide();
	$("#analyzerResult").hide();
	$("#uuidBased").hide();
	$("#counterBased").hide();
	
	// We do not want to submit form using standard http
	$("#pkAnalyzerFormData").submit(function(){
		return false;
	});
	
	$("#buttonSubmit").click(function() {
		var pkString = $("#pkString").val();
		
		// Validate form
		if (pkString == '') {
			hac.global.error($("#pkString").attr("data-errormsg"));
			return false;
		}
		
		// Setup AJAX request
		var url = $("#pkAnalyzerFormData").attr("action");
        var token = $("meta[name='_csrf']").attr("content");

		$.ajax({
			url: url,
			type:'POST',
			data: "pkString=" + pkString,
			headers:{
                'Accept':'application/json',
                'X-CSRF-TOKEN' : token
            },
			success: function(data) {
				if (data.possibleException != null) {
					$("#analyzerResult").hide();
					hac.global.error(data.possibleException.message);
					return;
				}				
				
				$("#possibleException").hide();
				
				$("#pkStringValue").text(data.pkString);
				$("#composedType").text(data.pkComposedTypeCode);
				$("#typecode").text(data.pkTypeCode);
				$("#milliCntValue").text(data.pkMilliCnt);
				
				if (data.counterBased) {
					setCounterBasedPk(data);
				} else {
					setUuidBasedPk(data);
				}
				
				// Initialize PK extended bit representation table
				table = $('#extendedBinaryRepresentation').dataTable({
					"bFilter": false,
					"bSearchable": false,
					"bInfo": false,
					"bLengthChange": false,
					"bRetrieve": true,
					"bPaginate" : false
				});
				table.fnClearTable();
				table.fnAddData(data.bits);
				
				$("#analyzerResult").fadeIn();

			},
			error: hac.global.err
		});	
	});
	
	function setCounterBasedPk(data){
		$("#counterBased").show();
		$("#uuidBased").hide();
		
		$("#clusterId").hide();
		$("#creationTime").hide();
	}
	
	function setUuidBasedPk(data){
		$("#uuidBased").show();
		$("#counterBased").hide();
		$("#creationTime").show();
		
		$("#creationTimeValue").text(data.pkCreationTime);
		$("#creationDateValue").text(data.pkCreationDate);

		$("#creationDate").text(data.pkCreationDate);
		if(data.pkClusterId != null && data.pkClusterId != ""){
			$("#clusterId").show();
			$("#entryClusterId").text(data.pkClusterId);
		}
		else{
			$("#clusterId").hide();
		}
	}
});