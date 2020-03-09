var ASM = ASM || {}; // make sure ASM is available
var sessionSec;
var counter;
var carts;

function addASMHandlers() {
	
	revertAutocompleteNormalize();
	removeAsmAlert(3000);
	addCloseBtnHandler();
    addASMFormHandler();
    addHideBtnHandler();
    addCustomerListBtnHandler();
    customerListModalHandler();
    addCustomer360Handler();
    addGenericCustomer360Handler();

    if ($("#sessionTimer").length && $('#asmLogoutForm').length ) {
        startTimer();
    }

    if ($("#resetButton").length) {
        $("#resetButton").click(function() {
            resetSession();
        });
    }

    /* for <=IE9 */
    if (placeholderNotAvailable()) {
        $('[placeholder]').focus(function() {
            var input = $(this);
            if (input.val() === input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(function() {
                var input = $(this);
                if (input.val() === '' || input.val() === input.attr('placeholder')) {
                    input.addClass('placeholder');
                    input.val(input.attr('placeholder'));
                }
            }).blur();
    }

    $('[placeholder]').blur(function() {
        var input = $(this);
        if ((input.val() === '') && (input.attr("name"))) {
            toggleBind(false);
        }
    });

    if ($('.ASM_alert_cart').length) {
        $("input[name='cartId']").addClass('ASM-input-error');
    }
    
    if ($('.ASM_alert_customer').length) {
        $("input[name='customerName']").addClass('ASM-input-error');
    }
    
    if ($('.ASM_alert_cred').length) {
    	$("input[name='username']").addClass('ASM-input-error');
    	$("input[name='password']").addClass('ASM-input-error');
	}
    
    if ($('.ASM_alert_create_new').length) {
    	toggleCreateAccount(true);
    }

    /* form validation */
    if ($('#_asmLogin').length) {
        var loginUser = $("#asmLoginForm input[name='username']");
        var min = 1;
        if ( loginUser.val().length >= min) {
            loginUser.parent().addClass('checked');
        }
    }

    $("#asmLoginForm input[name='username'], #asmLoginForm input[name='password']").keyup(function () {
        var min = 1;
        var parentNode = $(this.parentNode);

        if (this.value.length >= (min) ) {
            parentNode.addClass('checked');
            checkSignInButton(parentNode);
        }
        else {
            parentNode.removeClass('checked');
            checkSignInButton(parentNode);
        }
    });

    /* start session validation */
    $("input[name='customerName']").keyup(function (e) {
        $("input[name='customerId']").val("");
        validateNewAccount(this);
        $(this).removeData( "hover" );
        removeAsmHover();
        toggleBind(false);
        toggleStartSessionButton(this, false);

        if ($(this).val().length < 3) {
            toggleCreateAccount(false);
        }
    });

    $("#_asmPersonifyForm input[name='cartId']").keyup(function () {
        formValidate(this, 8, true, 8);
        if (isErrorDisplayed()) {
   		    $("input[name='cartId']").removeClass('ASM-input-error');
   		    if ($('.ASM_alert')) {
   		        $('.ASM_alert').remove();
   		    }
        }
    });
    
    $("#_asmPersonifyForm input[name='customerName']").keyup(function () {
    	if (isErrorDisplayed()) { 
   		    $("input[name='customerName']").removeClass('ASM-input-error');
   		    if ($('.ASM_alert')) {
   		        $('.ASM_alert').remove();
   		    }
    	    if ($(this).val() === "") {
    		    $("input[name='cartId']").removeClass('ASM-input-error');
    		    toggleStartSessionButton ($("input[name='cartId']"), true);
    		    $("input[name='customerId']").val("");
    	    }
    	}
    	if ($(this).val() ==="") {
    		    $("input[name='cartId']").val("");
    		    $( "#asmAutoCompleteCartId" ).empty();
   	    }
    });

    $("#_asmPersonifyForm input[name='cartId']").blur(function () {
        var regEx = /^\s+$/;
        if (regEx.test($(this).val()) ) {
            $(this).val('');
            formValidate(this, 8, true, 8);
        }
    });

    $("#_asmBindForm input[name='cartId']").keyup(function (e) {
    	checkCartIdFieldAndToggleBind(this);
    });
    
    $("#_asmBindForm input[name='cartId']").bind('paste', function (e) {
    	var inputField = this;
    	setTimeout(function () {
    		checkCartIdFieldAndToggleBind(inputField);
    	}, 100);
	});
    /* end form validation */

    $("#_asmPersonifyForm input[name='customerName'], input[name='customerId']").hover(function() {
            var item = ( $(this).attr('data-hover') )? jQuery.parseJSON($(this).attr('data-hover')) : $(this).data( "hover" );
            var disabled = ( $(this).attr('data-hover') )? "disabled" : "";

            if( !(item === null || item === undefined) ) {
                $(this)
                    .after(
                        $('<div>')
                            .attr('id', 'asmHover')
                            .addClass(disabled)
                            .append(
                                $('<span>').addClass('name').text(item.name),
                                $('<span>').addClass('email').text(item.email),
                                $('<span>').addClass('date').text(item.date),
                                $('<span>').addClass('card').text(item.card)
                            )
                    );
            }
        }, function () {
            removeAsmHover();
        }
    );

    $("#_asmPersonifyForm input[name='cartId']").autocomplete({
        source: function( request, response ) {
            response(carts);
        },
        appendTo: "#asmAutoCompleteCartId",
        autoFocus: true,
        minLength: 0,
        select: function( event, ui ) {
            if (ui.item.value === "") {
                event.preventDefault();
            }
            toggleStartSessionButton (this, true);
        }
    });

    $("#_asmPersonifyForm input[name='cartId']").on('click, focus', function() {
        $("#_asmPersonifyForm input[name='cartId']").autocomplete('search', '');
    });

    if ($("input[name='customerName']").length > 0) {
        $("input[name='customerName']").autocomplete({
            source: function( request, response ) {
                $.ajax({
                    url: ACC.config.encodedContextPath + "/assisted-service/autocomplete",
                    dataType: "json",
                    data: {
                        customerId: request.term
                    },
                    success: function( data ) {
                        response( $.map( data, function( item )
                        {
                            return{
                                email: item.email,
                                date: item.date,
                                card: item.card,
                                value: item.value,
                                carts: item.carts
                            };
                        }));
                    }
                });
            },
            minLength: 3,
            appendTo: "#asmAutoComplete",
            select: function( event, ui ) {
                if (ui.item.value === null) {
                    event.preventDefault();
                    return;
                }
                toggleStartSessionButton (this, true);
                $(this).data('hover',{name:ui.item.value, email:ui.item.email, card:ui.item.card, date:ui.item.date});

                /* insert item.value in customerId hidden field */
                $("input[name='customerId']").val(ui.item.email);

                carts = ui.item.carts;
                if ($("input[name='cartId']").attr("orig_value") === null) {
                	$("input[name='cartId']").val('');
                	if (carts !== null) {
		                if (carts.length === 1) {
		                    $("input[name='cartId']").val(carts[0]);
		                } else {
		                    $("input[name='cartId']").autocomplete('search', '');
		                    $("input[name='cartId']").focus();
		                }
		            } else {
		                carts = [{label: "No Existing Carts", value: ""}];
		                $("input[name='cartId']").autocomplete('search', '');
		                $("input[name='cartId']").focus();
		            }
                }
                checkCartIdFieldAndToggleBind($("input[name='cartId']")[0]);

            }
        }).data( "ui-autocomplete" )._renderItem = function( ul, item ) {
            if (item.value === null) {
                toggleCreateAccount(true);
                return $( "<li></li>" ).data( "item.autocomplete", item ).append($('<span class=noresult>').text(ASM_MESSAGES.customerIdNotFound))
                    .appendTo( ul );
            }
            else { toggleCreateAccount(false); }

            return $( "<li></li>" ).data( "item.autocomplete", item ).append($('<span>').addClass('name').text(item.value),
                $('<span>').addClass('email').text(item.email),
                $('<span>').addClass('date').text(item.date),
                $('<span>').addClass('card').text(item.card)).appendTo( ul );
        };
    }

    if ($("#_asmBindForm").length) {
        var customerId = $("input[name='customerName']").attr('readonly');

        if(customerId === "readonly"){
            $(".ASM_icon-chain").removeClass('invisible').addClass('ASM_chain-bind');

            if ($("#_asmBindForm input[name='customerId']").val() !== undefined && $("#_asmBindForm input[name='customerId']").val() !== "") {
                $(".js-customer360").removeAttr('disabled');
            }
        }
    }
    if ($(".add_to_cart_form").length && $("#_asm input[name='cartId']").val() === "") {
    	$( ".add_to_cart_form" ).submit(function( event ) {
    		setTimeout(function () {
    			var url = ACC.config.encodedContextPath + "/assisted-service/add-to-cart";
        		$.post(url, function( data ) {
        			$("#_asm").replaceWith(data);
                    addASMHandlers();
            	});
    	    }, 400);
    	});	
    }
    enableAsmPanelButtons();
}

$( document ).ready(function() {
	var ASM = ASM || {}; // make sure ACC is available
    addASMHandlers();

    $(document).on("click",".js-select-store-label",function(e){
        $("#colorbox .js-pickup-component").addClass("show-store");
        colorboxResize();
    });

    $(document).on("click",".js-asm-store-finder-details-back",function(e){
        $("#colorbox .js-pickup-component").removeClass("show-store");
    });
});

function addASMFormHandler() {
    if (($) && ($(".asmForm").length)) {
        $(".asmForm").each(function () {
            $(this).submit(function() {
                $(this).find('[placeholder]').each(function() {
                    var input = $(this);
                    if (input.val() === input.attr('placeholder')) {
                        input.val('');
                    }
                });
                $.ajax({
                    type: "POST",
                    url: $(this).attr('action'),
                    data: $(this).serialize(),
                    success: function(data) {
                        $("#_asm").replaceWith(data);
                        addASMHandlers();
                    }
                });
                return false;
            });
        });
    }
}

function addCloseBtnHandler() {
    $("#_asm .closeBtn").click( function() {
        $("#_asm").remove();
        var url = ACC.config.encodedContextPath + "/assisted-service/quit";
    	$.post(url, function( data ) {
            var oldurl = window.location.href;
            var newurl = oldurl.replace("&asm=true", "").replace("?asm=true&", "?").replace("?asm=true", "");
            window.location.replace(newurl);
    	});
    });
}

function addHideBtnHandler() {
    $("#_asm .ASM_control_collapse").click( function() { $("#_asm").toggleClass("ASM-collapsed"); });
}

function startTimer() {
    sessionSec = timer;
    clearInterval(counter);
    counter = setInterval(timerFunc, 1000);
}

function timerFunc() {
    if (sessionSec <= 0) {
        clearInterval(counter);
        finishASMagentSession();
        return;
    }
    sessionSec = sessionSec - 1;
    var min = Math.floor(sessionSec / 60);
    var sec = sessionSec % 60;
    if (min < 10)
    {
        min = "0" + min;
    }
    if (sec < 10)
    {
        sec = "0" + sec;
    }
    $("#sessionTimer .ASM_timer_count").html(min + ":" + sec);
}

function resetSession() {
    var request = $.ajax({
        url : ACC.config.encodedContextPath + "/assisted-service/resetSession",
        type : "POST"
    });
    request.done(function(msg) {
        sessionSec = timer + 1;
    });
    request.fail(function(jqXHR, textStatus) {
        $('#errors').empty();
        $('#errors').append("Request failed: " + textStatus);
    });
}

function finishASMagentSession() {
	$.ajax({
        url : ACC.config.encodedContextPath + "/assisted-service/logoutasm",
        type : "POST",
        success: function(data) {
            $("#_asm").replaceWith(data);
            addASMHandlers();
        }
    });
}

function isStartEmulateButtonPresent() {
	return $(".ASM-btn-start-session").length === 1;
}

function enableAsmPanelButtons() {
    $('div[id="_asm"] button').not(".js-customer360, .ASM-btn-start-session, .ASM-btn-create-account, .ASM-btn-login").removeAttr('disabled');
    if (isStartEmulateButtonPresent()) {
	    if ($("#_asmPersonifyForm input[name='customerId']").val() !== "") {
	    	$("#_asmPersonifyForm input[name='customerId']").parent().addClass("checked");
	    }
        formValidate($("#_asmPersonifyForm input[name='cartId']")[0], 8, true, 8);
	}
}

function placeholderNotAvailable(){
    var i = document.createElement('input');
    return !('placeholder' in i);
}

function removeAsmHover() {
    $('#asmHover').remove();
}

function toggleCreateAccount(activate){
    var bindIcon = $(".ASM_icon-chain");
    var createButton = $("#_asmCreateAccountForm button.ASM-btn-create-account[type='submit']");
    if (activate) {
        createButton.removeClass('hidden');
        bindIcon.removeClass('invisible');
    } else {
        createButton.addClass('hidden');
        bindIcon.addClass('invisible');
    }
}
function toggleActivationState(button, activate){
    if (activate) {
        button.removeAttr('disabled');
    }
    else {
        button.attr('disabled','');
    }
}
function checkSignInButton (el) {
    var signInBtn = $("#asmLoginForm button[type='submit']");
    var checkSum = $(el).parent().find('.checked').length;
    if(checkSum > 1) {
        toggleActivationState(signInBtn, true);
    }
    else {
        toggleActivationState(signInBtn, false);
    }
}

function checkStartSessionButton (el) {
    toggleStartSessionButton (el, false);
    var checkSum = $(el.parentNode).siblings('.checked').length;
    if(checkSum > 0) {
        toggleActivationState($("button.ASM-btn-start-session"), true);
    }
}

function checkCartIdFieldAndToggleBind(cartIdField) {
	if ( !$(cartIdField).hasClass('placeholder')
	        && ($("input[name='customerName']").val().length > 0)
            && ($("input[name='customerId']").val().length > 0)
            && !isNaN(cartIdField.value)
            && (cartIdField.value.length === 8)) {
        $("#_asmBindForm button[type='submit']").removeClass('hidden');
        $(".ASM_icon-chain").removeClass('invisible');
        return;
    }
    $("#_asmBindForm button[type='submit']").addClass('hidden');
    $(".ASM_icon-chain").addClass('invisible');
}

function toggleBind (activate) {
    if ($("#_asmBindForm").length) {
        var bindIcon = $(".ASM_icon-chain");
        var bindButton = $("#_asmBindForm button.ASM-btn-bind-cart[type='submit']");
        if (activate){
            bindButton.removeClass('hidden');
            bindIcon.removeClass('invisible');
        } else {
            bindButton.addClass('hidden');
            if($('.ASM-btn-create-account').hasClass('hidden')){
                bindIcon.addClass('invisible');
            }
        }
    }
}

function toggleStartSessionButton (el, activate) {
    var checkedItem = $(el).parent();
    var button = $("button.ASM-btn-start-session");
    if (activate){
        button.removeAttr('disabled');
        checkedItem.addClass("checked");
    } else {
        button.attr('disabled','');
        checkedItem.removeClass("checked");
    }
}

function formValidate (el, min, number, max ) {
    if( !$(el).hasClass('placeholder') ) {
    	if ($(el).hasClass("ASM-input-error")) {
    		toggleStartSessionButton (el, false);
    		return false;
    	}
        if ((number !== false) && isNaN(el.value)) {
            toggleStartSessionButton (el, false);
            return false;
        }
        if (el.value.length >= (min) ) {
            toggleStartSessionButton (el, true);
            if ( max !== undefined && el.value.length > (max) ) {
                toggleStartSessionButton (el, false);
            }
        }
        else if (el.value.length === 0 ) {
            checkStartSessionButton(el);
        }
        else {
            toggleStartSessionButton (el, false);
            return false;
        }
        return true;
    }
    return false;
}

function validateEmail(mailAddress) {
    return ($('<input>').attr({ type: 'email', required:'required' }).val(mailAddress))[0].checkValidity() && (mailAddress.indexOf(".") > 0);
}

function validateName(name) {
    var split = name.trim().split(/\s+/);
    return !isBlank(split[0]) && !isBlank(split[1]);
}

function isBlank(str) {
    return (!str || 0 === str.length);
}

function validateNewAccount(el) {
    var createAccountButton = $("#_asmCreateAccountForm button.ASM-btn-create-account[type='submit']");
    var customerValues = el.value.split(', ');
    var IdInput = $("#_asmCreateAccountForm input[name='customerId']");
    var NameInput = $("#_asmCreateAccountForm input[name='customerName']");

    if (customerValues.length > 1) {
        var validName = validateName(customerValues[0]);
        var validMail = validateEmail(customerValues[1]);
        if (validName && validMail) {
            toggleActivationState(createAccountButton, true);
            /* fill hidden input fields */
            IdInput.val(customerValues[1].replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
            NameInput.val(customerValues[0]);
        }
        else {
            /* no valid customer values */
            toggleActivationState(createAccountButton, false);
            return false;
        }
    }
    else {
        /* too less customer values */
        toggleActivationState(createAccountButton, false);
        return false;
    }
}

function revertAutocompleteNormalize() {
	/* After http://bugs.jqueryui.com/ticket/9762 there was a change when for empty value label is placed instead. 
	 * But we want to send empty values for NO_FOUND case */
	$.ui.autocomplete.prototype._normalize =  function(a){
		if ( a.length && a[ 0 ].label && a[ 0 ].value ) {return a; }
		return $.map( a, function( b ) {
		    if ( typeof b === "string" ) { return {label: b,value: b};}
            return $.extend({label: b.label || b.value, value: b.value || b.label}, b );
        });
	};
}

function isErrorDisplayed() {
	return $('.ASM_alert').length;
}


function addCustomerListBtnHandler() {
    $(".js-customer-list-btn").removeClass('disabled');
    $(document).on("click", ".js-customer-list-btn", function (e) {
        e.preventDefault();
        populateCustomerListModal($(this).data('actionurl'), '.js-customer-list-modal-content', addCustomerListSelect);
    });
}

function openCustomer360Colorbox (colorboxTarget){
    colorboxTarget.colorbox({
        inline: 'true',
        className: 'ASM_customer360-modal',
        width: "100%",
        maxWidth: "1200px",
        close:'<span class="ASM_icon ASM_icon-close"></span>',
        transition: 'none',
        scrolling: false,
        opacity: 0.7,
        top: 10,
        onOpen: function() { 
            customer360Callback();
            $(window).on("resize", colorboxResize);
        },
        onClosed: function() { 
            $(window).off("resize", colorboxResize);
        }
    });
}

function colorboxResize(){
    $.colorbox.resize();
}

function addCustomer360Handler() {
    openCustomer360Colorbox($(".js-customer360"));
}

function addGenericCustomer360Handler() {
	if($( "#enable360View" ).val()) {
        openCustomer360Colorbox($);
	}
}

function customer360Callback() {
        var loader = "<div class='loader'>Loading..</div>";
        $("#cboxLoadedContent").html(loader).show();
        $.ajax({
            url: ACC.config.encodedContextPath + "/assisted-service-aif/customer360",
            type: "GET",
            success: function(data)
            {
                $("#cboxLoadedContent").append(data);
                $.colorbox.resize();
            },
            error: function(xht, textStatus, ex)
            {
                console.error("Failed to load Customer 360. %s", ex);
                document.location.reload();
            }
        });	
}

function loadCustomer360Fragment(params)
{
        return $.ajax({
	        url: ACC.config.encodedContextPath + "/assisted-service-aif/customer360Fragment",
	        timeout: params.timeout,
	        type: params.method,
	        data: params,
	        success: function(data)
	        {
	        	 $( "#"+params.fragmentId).html(data);
	        	 $.colorbox.resize();
	        	 
	        },
	        error: function(xht, textStatus, ex)
	        {
	        	if(textStatus === 'timeout')
	            {     
	        		$( "#"+params.fragmentId).html("Widget timeout!");
	                //do something. Try again perhaps?
	            }
	        	else
	        	{
	        		console.error("Failed to get widget data! %s", ex);
	        		$( "#"+params.fragmentId).html("Failed to get widget data!");
	        	}           
	        }
	    });
}

function asmAifSectionClickHandler() {
    $(document).on("click", ".asm__customer360__menu li", function (e) {
        e.preventDefault();
        if (!$(this).hasClass('nav-tabs-mobile-caret')) {
            aifSelectSection($(this).index());
        }
    });
}
function aifSelectLastSection() {
    var index = 0;
    if (sessionStorage.getItem("lastSection")) {
        var lastSection = JSON.parse(sessionStorage.getItem("lastSection"));
        if (getCurrentEmulatedCustomerId() === lastSection.userId) {
            index = lastSection.sectionId;
        }
    }
    $($(".asm__customer360__menu li[role='presentation']")[index]).addClass("active");
    aifSelectSection(index);
}

function aifSelectSection(index) {
    $("#sectionPlaceholder").hide();
    $("#longLoadExample").show();
    var sectionId= $(".asm__customer360__menu li").get(index).getAttribute("value");
    sessionStorage.setItem("lastSection", JSON.stringify({userId: getCurrentEmulatedCustomerId(), sectionId: index}));
    $.ajax({
        url: ACC.config.encodedContextPath + "/assisted-service-aif/customer360section?sectionId=" + sectionId,
        type: "GET",
        success: function(data)
        {
            $("#sectionPlaceholder").html(data);
            $("#longLoadExample").hide();
            $("#sectionPlaceholder").show();
            $.colorbox.resize();
        }
    });
    resetSession();
}

function getCurrentEmulatedCustomerId() {
    if ( $("#_asmBindForm input[name='customerId']").length ) {
        return $("#_asmBindForm input[name='customerId']").val();
    }
    return "anonymous";
}

function getCustomerListSearchUrl() {
    var targetUrl = $(".js-customer-list-sorting").data('sort-url');
    targetUrl += $(".ASM_customer-list-modal .sort-refine-bar .form-control").val();
    var query = $("#ASM_customer-list-queryInput").val();
    var uriEncodedquery = encodeURIComponent(query);
    targetUrl += '&query=' + uriEncodedquery;
    return targetUrl;
}

function customerListModalHandler() {
    $(document).on("click", ".ASM_customer-list-modal .pagination a", function (e) {
        e.preventDefault();
        populateCustomerListModal($(this).attr('href'),".asm-account-section",replaceCustomerListTable);
    });

    $(document).on("click", "#ASM_customer-list-sortOptions .sortOption", function (e) {
        e.preventDefault();
        var selectedOption = $(this).data('value');
        var previouslySelectedOption = $(".ASM_customer-list-modal .sort-refine-bar .form-control").val();
        if (selectedOption !== previouslySelectedOption)
        {
            $(".ASM_customer-list-modal .sort-refine-bar .form-control").val(selectedOption);
            var targetUrl = getCustomerListSearchUrl();
            populateCustomerListModal(targetUrl,".asm-account-section",replaceCustomerListTable);
        }
    });

    $(document).on("keypress", "#ASM_customer-list-queryInput", function(event) {
        if (event.keyCode === 13) {
            $("#ASM_customer-list-searchButton").click();
            return false;
        }
        else {
            return true;
        }
    });

    $(document).on("click", "#ASM_customer-list-searchButton", function (e) {
        e.preventDefault();
        var targetUrl = getCustomerListSearchUrl();
        populateCustomerListModal(targetUrl,".asm-account-section",replaceCustomerListTable);
    });

    $(document).on("change", ".ASM_customer-list-modal .sort-refine-bar .form-control", function (e) {
        e.preventDefault();

        var targetUrl = getCustomerListSearchUrl();
        populateCustomerListModal(targetUrl,".asm-account-section",replaceCustomerListTable);
    });

    $(document).on("change", ".js-customer-list-select", function (e) {
        e.preventDefault();
        var targetUrl = $(this).data('search-url');
        targetUrl += $(this).val();
        var request = populateCustomerListModal(targetUrl,".asm-account-section",replaceCustomerListTable);
        request.done(function(){
            $.colorbox.resize();
        });
    });
}

function addCustomerListSelect(componentToUpdate,data) {
	var selector=$(data).find('.js-customer-list-select');
    $(componentToUpdate).html(data);
    var searchUrl = $(data).find('.js-customer-list-select').data('search-url');
    if(selector[0].options.length >0)
	{
		searchUrl+= selector[0].options[0].value;
	}
    
    var request = populateCustomerListModal(searchUrl, componentToUpdate, appendCustomerListTable);
    request.done(function(){
        ACC.colorbox.open("",{
            href: ".js-customer-list-modal-content",
            inline: true,
            className: 'ASM_customer-list-modal',
            width: "100%",
            maxWidth: "1200px",
            close:'<span class="ASM_icon ASM_icon-close"></span>',
            transition: 'none',
            scrolling: false,
            opacity: 0.7,
            top: 10,
            onOpen: function() {
                $(window).on("resize", colorboxResize);
            },
            onClosed: function() {
                $(window).off("resize", colorboxResize);
            }
        });
    });
}
function appendCustomerListTable(componentToUpdate,data) {
    $(componentToUpdate).append(data);
}
function replaceCustomerListTable(componentToUpdate,data) {
    $(componentToUpdate).html(data);
}

function populateCustomerListModal(targetUrl,componentToUpdate, callFunction) {
    var method = "GET";
    return $.ajax({
        url: targetUrl,
        type: method,
        success: function(data)
        {
            callFunction(componentToUpdate,data);
        },
        error: function(xht, textStatus, ex)
        {
            console.error("Failed to get customer list. %s", ex);
            document.location.reload();
        }
    });
}
function getAifTablePageSize() {
    var pagesNumber = 5; // number
    if ($(window).width() < 668) {
        pagesNumber = 10;
    }
    return pagesNumber;
}

function copyToClipBoard(text) {
	$("#asmCopyHoldtext").val(text);
	$("#asmCopyHoldtext").show();
	$("#asmCopyHoldtext").select();
    try {
        return document.execCommand("copy");
    } catch (ex) {
        console.warn("Copy to clipboard failed.", ex);
        return false;
    } finally {
    	$("#asmCopyHoldtext").hide();
    }
}

function addRatesTableSorterParser() {
    $.tablesorter.addParser({
        // set a unique id
        id: 'rates',
        is: function(s) {
            // return false so this parser is not auto detected
            return false;
        },
        format: function(s, table, cell) {
            return Math.floor($(cell).attr('data-text')*10);
        },
        type: 'numeric'
    });
}

function removeAsmAlert(delay) {
    setTimeout(function() {$(".ASM_alert").fadeOut("slow");}, delay);
}
ACC.assistedservicestorefront = {

    buildArrayValues: function(variableArray, value) {
        variableArray.push(value);
        return variableArray;
    }

};
// collapsible
$(function(){
    if($(".js-ASM-collapseBtn").length > 0){
        var onDragging = false;
        //get saved values from cookie
        var startPosition = readASMcollapseCookie();
        if(startPosition){
            //set position of collapseBtn
            $(".js-ASM-collapseBtn").css("left",startPosition.position+"%");
            if(startPosition.state){
                $("#_asm").addClass("asm-collapsed");
            }
        }
        $("#_asm").show();
        //init drag of the button
        $(".js-ASM-collapseBtn").draggable({
            cancel : '.no-drag',
            axis:"x",
            containment: ".collapseBtn-wrapper",
            delay: 300,
            distance: 10,
            opacity: 0.8,
            start: function() {
                onDragging = true;
                $(".js-ASM-collapseBtn-wrapper").addClass("active");

            },
            stop: function() {
                onDragging = false;
                $(".js-ASM-collapseBtn-wrapper").removeClass("active");
                //save values to cookie
                saveCollapseBtn();
            }
        });

        //bind event for toogle the asm panel
        $(document).on("mouseup",".js-ASM-collapseBtn",function(){
            if(!onDragging){
               if($("#_asm").hasClass("asm-collapsed")){
                    $("#_asm").removeClass("asm-collapsed");
                }else{
                   $("#_asm").addClass("asm-collapsed");
                } 
                //save values to cookie
                saveCollapseBtn();
            }
        });

        function saveCollapseBtn(){
            var pos = $(".js-ASM-collapseBtn").offset().left;
            var parentWidth =  $(".js-ASM-collapseBtn-wrapper").width();
            var obj = {
                position: pos/(parentWidth/100),
                state: $("#_asm").hasClass("asm-collapsed")
            };
            document.cookie = "ASMcollapseBtn=" + encodeURIComponent(JSON.stringify(obj))+"; path=/";
        }

        function readASMcollapseCookie() {
            var nameEQ = "ASMcollapseBtn=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) === ' ') {c = c.substring(1, c.length);}
                if (c.indexOf(nameEQ) === 0) {return jQuery.parseJSON(decodeURIComponent(c.substring(nameEQ.length, c.length)));}
            }
            return null;
        }
    }
});

// dropdown
$(function(){
    $(document).on("click",".js-dropdown",function(e){
        e.preventDefault();
        var $e = $(this).parent();
        if($e.hasClass("open")){
            $e.removeClass("open");
        }else{
             $e.addClass("open");
        }
    });
    $(document).on("click",".js-customer-360-tab",function(e){
        e.preventDefault();
        $(this).parent().addClass("active").siblings().removeClass("active");
    });
});
