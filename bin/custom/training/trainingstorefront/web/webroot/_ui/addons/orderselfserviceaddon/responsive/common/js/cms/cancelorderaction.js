/*
 [y] hybris Platform
 Copyright (c) 2017 SAP SE or an SAP affiliate company.
 All rights reserved.
 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
 */
ACC.cancelorderaction = {
		
    _autoload: [
	    ["bindToCancelCompleteOrderButton", $(".js-cancel-complete-order-link").length != 0],
	    "bindToCancelEntryQuantityInput",
        "bindToCancelEntryQuantityFocusOut"
    ],

    bindToCancelCompleteOrderButton : function() {
        $(document).on('click', '.js-cancel-complete-order-link', function(event) {
            event.preventDefault();
            $.each( $('[id^="item_quantity_"]'), function(i) {
                $('[name^="cancelEntryQuantityMap[' + i + ']"]').val($('#item_quantity_' + i).val())
            });
            ACC.cancelorderaction.disableEnableCancelSubmit();
        });
    },

    bindToCancelEntryQuantityInput : function() {
        $('input[id^="cancelEntryQuantityMap"]').keypress(function(e) {
            if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                return false;
            }
        });		
    },

    bindToCancelEntryQuantityFocusOut : function() {
        $('[name^="cancelEntryQuantityMap"]').focusout(function(field) {
            var index = this.id.replace("cancelEntryQuantityMap", "");
            if (parseInt($('#item_quantity_' + index).val()) < parseInt(this.value)) {
                this.value = $('#item_quantity_' + index).val();
            }
            $('[name^="cancelEntryQuantityMap[' + index + ']"]').val(this.value)
            
            ACC.cancelorderaction.disableEnableCancelSubmit();
        });
    },

    //Enable submit button in case some value is more than zero.
    disableEnableCancelSubmit: function() {
        var submitDisabled = true;
        $.each( $('[id^="item_quantity_"]'), function(i) {
            if (parseInt($('[name^="cancelEntryQuantityMap[' + i + ']"]').val()) > 0 ) {
                submitDisabled = false;
            }
        });
        $("#cancelOrderButtonConfirmation").prop("disabled", submitDisabled);
    }

}
