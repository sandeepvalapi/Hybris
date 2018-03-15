/*
 [y] hybris Platform
 Copyright (c) 2017 SAP SE or an SAP affiliate company.
 All rights reserved.
 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
 */
ACC.returnorderaction = {

    _autoload: [
        ["bindToReturnCompleteOrderButton", $(".js-return-complete-order-link").length != 0],
        "bindToReturnEntryQuantityInput",
        "bindToReturnEntryQuantityFocusOut"
    ],

    bindToReturnCompleteOrderButton : function() {
        $(document).on('click', '.js-return-complete-order-link', function(event) {
            event.preventDefault();
            $.each( $('[id^="item_quantity_"]'), function(i) {
                $('[name^="returnEntryQuantityMap[' + i + ']"]').val($('#item_quantity_' + i).val())
            });
            ACC.returnorderaction.disableEnableReturnSubmit();
        });
    },

    bindToReturnEntryQuantityInput : function() {
        $('input[id^="returnEntryQuantityMap"]').keypress(function(e) {
            if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                return false;
            }
        });		
    },

    bindToReturnEntryQuantityFocusOut : function() {
        $('[name^="returnEntryQuantityMap"]').focusout(function(field) {
            var index = this.id.replace("returnEntryQuantityMap", "");
            if (parseInt($('#item_quantity_' + index).val()) < parseInt(this.value)) {
                this.value = $('#item_quantity_' + index).val();
            }
            $('[name^="returnEntryQuantityMap[' + index + ']"]').val(this.value)
            
            ACC.returnorderaction.disableEnableReturnSubmit();
        });
    },

    //Enable submit button in case some value is more than zero.
    disableEnableReturnSubmit: function() {
        var submitDisabled = true;
        $.each( $('[id^="item_quantity_"]'), function(i) {
            if (parseInt($('[name^="returnEntryQuantityMap[' + i + ']"]').val()) > 0 ) {
                submitDisabled = false;
            }
        });
        $("#returnOrderButtonConfirmation").prop("disabled", submitDisabled);
    }

};