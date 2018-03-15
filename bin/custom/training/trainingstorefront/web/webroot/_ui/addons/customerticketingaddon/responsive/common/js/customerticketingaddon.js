ACC.customerticketingaddon = {

	_autoload: [
		"onStatusChange",
		"bindMessageArea",
		"toggleAllMessages",
		"postNewMessage",
		"onFileChosen",
		"bindTicketAddActions",
		"bindTicketUpdateActions"
	],


	disableMessage: function(_this){
		var currentTicketStatus = $('input[id="currentTicketStatus"]').val();
		var selectedStatus = $(_this).val();

		if((currentTicketStatus === 'COMPLETED' && selectedStatus === 'COMPLETED') || (currentTicketStatus === 'CLOSED' && selectedStatus === 'CLOSED')) {
			$('textarea[id="message"]').attr('disabled','disabled');
			$('button[id="updateTicket"]').attr('disabled','disabled');
		} else {
			$('textarea[id="message"]').removeAttr('disabled');
		}
	},

	onStatusChange: function () {
        $(document).on('change', '.js-add-message-status', function () {
            ACC.customerticketingaddon.disableMessage(this);
		});
	},

    onFileChosen: function () {
        $(document).on('change', '#supportTicketForm input[name=files]', function () {
            ACC.customerticketingaddon.clearAlerts();
            var selectedFile = document.getElementById('attachmentFiles');
            if (!ACC.customerticketingaddon.isSelectedFilesValid(selectedFile))
            {
                var message = "<span style='color:red'>" + $('#file-too-large-message').text() + "</span>";
                $("#supportTicketForm").find(".js-file-upload__file-name").html(message);
            }
        });
    },

    bindMessageArea: function () {
        $(document).on('keyup', '.js-add-message', function () {
            if($(this).val().length > 0) {
                $('button[id="updateTicket"]').removeAttr('disabled');
                $('#NotEmpty-supportTicketForm-message').hide();
            } else {
                $('button[id="updateTicket"]').attr('disabled','disabled');
            }
        });
    },

	toggleAllMessages: function() {
		$('#ct-toggle-all-messages').on('click touchstart', function() {
			$('.cts-msg-history-item:not(.ct-msg-visible)').show();
			$(this).hide();
		});
	},

	postNewMessage: function () {
		var title = $('#ct-overlay-title').html();
		$('.ct-add-new-msg-btn').on('click touchstart', function(e) {
			e.preventDefault();
			$.colorbox({
				href: "#ct-add-new-msg",
				maxWidth:"100%",
				width: 525,
				opacity:0.7,
				title: title,
				inline: true,
                close: '<span class="glyphicon glyphicon-remove"></span>',
                onOpen: function () {
                    $('#ct-add-new-msg').fadeIn();                   
                },
                onComplete: function () {                	
                    ACC.customerticketingaddon.disableMessage($('.js-add-message-status'));
                    
                    if (!$.trim($("#message").val())) {
                    	  $('button[id="updateTicket"]').attr('disabled', 'disabled');
                    }                                    
                  
                    ACC.csvimport.changeFileUploadAppearance();
                },
                onCleanup: function () {       	
                  $('#ct-add-new-msg').hide();
                }
            });
        })
    },

    isSelectedFilesValid: function (selectedFiles) {
        if (window.File && window.Blob) {
            var fileMaxSize = $('.js-file-upload__input').data('max-upload-size');
            var totalSize = 0;

            for (var i = 0; i < selectedFiles.files.length; ++i){
                totalSize += selectedFiles.files[i].size;
            }

            if ($.isNumeric(fileMaxSize) && totalSize > parseFloat(fileMaxSize)) {
                return false;
            }
        }

        return true;
    },

    displayCustomerTicketingAlert: function (options) {
        var alertTemplateSelector;

        switch (options.type) {
            case 'error':
                alertTemplateSelector = '#global-alert-danger-template';
                break;
            case 'warning':
                alertTemplateSelector = '#global-alert-warning-template';
                break;
            default:
                alertTemplateSelector = '#global-alert-info-template';
        }

        if (typeof options.message !== 'undefined') {
            $('#customer-ticketing-alerts').append($(alertTemplateSelector).tmpl({message: options.message}));
        }

        if (typeof options.messageId !== 'undefined') {
            $('#customer-ticketing-alerts').append($(alertTemplateSelector).tmpl({message: $('#' + options.messageId).text()}));
        }
    },

    displayGlobalAlert: function (options) {
        var alertTemplateSelector;

        switch (options.type) {
            case 'error':
                alertTemplateSelector = '#global-alert-danger-template';
                break;
            case 'warning':
                alertTemplateSelector = '#global-alert-warning-template';
                break;
            default:
                alertTemplateSelector = '#global-alert-info-template';
        }

        if (typeof options.message !== 'undefined') {
            $('#global-alerts').append($(alertTemplateSelector).tmpl({message: options.message}));
        }

        if (typeof options.messageId !== 'undefined') {
            $('#global-alerts').append($(alertTemplateSelector).tmpl({message: $('#' + options.messageId).text()}));
        }
    },

    bindTicketAddActions: function () {
        $(document).on('click', '#addTicket',
            function (event) {
                event.preventDefault();

                ACC.customerticketingaddon.formPostAction("support-tickets?ticketAdded=true");
            });
    },

    bindTicketUpdateActions: function () {
        $(document).on('click', '#updateTicket',
            function (event) {        	
                event.preventDefault();

                ACC.customerticketingaddon.formPostAction('?ticketUpdated=true');
            });
    },

    formPostAction: function (successRedirectUrl) {

        ACC.customerticketingaddon.clearAlerts();

        var form = document.getElementById("supportTicketForm");
        var formData = new window.FormData(form);

        var selectedFile = document.getElementById('attachmentFiles');
        if (!ACC.customerticketingaddon.isSelectedFilesValid(selectedFile)) {
            ACC.customerticketingaddon.displayCustomerTicketingAlert({
                type: 'error',
                messageId: 'attachment-file-max-size-exceeded-error-message'
            });
            return;
        }

        $.ajax({
            url: form.action,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function () {
                window.location.replace(successRedirectUrl);
            },
            error: function (jqXHR) {
                ACC.customerticketingaddon.processErrorResponse(jqXHR);
            }
        });
    },

    processErrorResponse: function (jqXHR) {
        ACC.customerticketingaddon.clearAlerts();
        if (jqXHR.status === 400 && jqXHR.responseJSON) {

            $.each(jqXHR.responseJSON, function() {
                $.each(this, function(k, v) {
                    var target = '#' + k;
                    $(target).show();
                    $(target).text(v);
                    if (k === 'NotEmpty-supportTicketForm-subject'
                        || k === 'Size-supportTicketForm-subject'
                        || k === 'NotEmpty-supportTicketForm-message'
                        || k === 'Size-supportTicketForm-message') {
                        ACC.customerticketingaddon.addHasErrorClass();
                    }
                    else {
                        ACC.customerticketingaddon.displayGlobalAlert({type: 'error', message: v});
                    }
                });
            });

            return;
        }

        ACC.customerticketingaddon.displayCustomerTicketingAlert({type: 'error', messageId: 'supporttickets-tryLater'});
    },

    addHasErrorClass: function () {
        $('#createTicket-message').parent().addClass('has-error');
    },

    clearAlerts: function () {
        $('#customer-ticketing-alerts').empty();
        $('#global-alerts').empty();
        $('#NotEmpty-supportTicketForm-subject').hide();
        $('#Size-supportTicketForm-message').hide();
        $('#Size-supportTicketForm-subject').hide();
        $('#createTicket-subject').parent().removeClass('has-error');
        $('#NotEmpty-supportTicketForm-message').hide();
        $('#createTicket-message').parent().removeClass('has-error');
    }
};
