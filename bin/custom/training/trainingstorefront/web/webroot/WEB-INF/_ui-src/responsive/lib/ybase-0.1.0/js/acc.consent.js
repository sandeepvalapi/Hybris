ACC.consent = {
    _autoload: [
        ["bindSendConsent", $("#consent-management-form").length != 0],
        ["bindToggleConsentTemplateDescription", $("#consent-management-form").length != 0],
        "bindConsentClick",
        "bindConsentManagementAlertBar"
    ],
   
    bindSendConsent: function ()
    {
        var consentCheckbox = $('#consent-management-form').find('input.toggle-button__input');

        consentCheckbox.click(function ()
        {
            var consentId = $(this).prop('id');
            var isConsentGiven = $(this).is(':checked');
            var buttonId = (isConsentGiven ? '#give-consent-button-' : '#withdraw-consent-button-') + consentId;
            $(buttonId).trigger('click');

            $(buttonId).on('keydown', function(event) {
                if (event.keyCode === 13 || event.keyCode === 32) {
                    event.preventDefault();
                    $(buttonId).trigger('click');
                }
            });

        });
    },


    bindToggleConsentTemplateDescription: function () {

        var accordion = $('#consent-management-form').find('[data-behavior="accordion"]');
        var expandedClass = 'is-expanded';

        $.each(accordion, function() {

            var accordionItems = $(this).find('[data-binding="expand-accordion-item"]');

            $.each(accordionItems, function() {
                var $this = $(this);
                var triggerBtn = $this.find('[data-binding="expand-accordion-trigger"]');

                var setHeight = function(nV) {

                    var innerContent = nV.find('.consent-management-list__content-inner')[0],
                        maxHeight = $(innerContent).outerHeight(),
                        content = nV.find('.consent-management-list__content')[0];

                    if (!content.style.height || content.style.height === '0px') {
                        $(content).css('height', maxHeight);
                    } else {
                        $(content).css('height', '0px');
                    }
                };

                var toggleClasses = function(event) {
                    var clickedItem = event.currentTarget;
                    var currentItem = $(clickedItem).parent();
                    var clickedContent = $(currentItem).find('.consent-management-list__content')
                    $(currentItem).toggleClass(expandedClass);
                    setHeight(currentItem);

                    if ($(currentItem).hasClass('is-expanded')) {
                        $(clickedItem).attr('aria-selected', 'true');
                        $(clickedItem).attr('aria-expanded', 'true');
                        $(clickedContent).attr('aria-hidden', 'false');

                    } else {
                        $(clickedItem).attr('aria-selected', 'false');
                        $(clickedItem).attr('aria-expanded', 'false');
                        $(clickedContent).attr('aria-hidden', 'true');
                    }
                }

                triggerBtn.on('click', function(event) {
                    event.preventDefault();
                    toggleClasses(event);
                });

                // keyboard navigation
                $(triggerBtn).on('keydown', function(event) {
                    if (event.keyCode === 13 || event.keyCode === 32) {
                        event.preventDefault();
                        toggleClasses(event);
                    }
                });
            });

        });

    },

    bindConsentClick:function(){
        $('.consent-accept').on("click",function(){
            ACC.consent.updateConsent(this, "GIVEN");
        });

        $('.consent-reject').on("click",function(){
            ACC.consent.updateConsent(this, "WITHDRAWN");
        });
    },

    changeConsentState:function(anonymousConsentCookie,consentCode, consentState){
        anonymousConsentCookie.forEach(function(consent) {
            if(consent.templateCode === consentCode){
                consent.consentState=consentState;
            }
        });
    },

    updateConsent:function(element, state){
        var anonymousConsentCookie=  JSON.parse( decodeURIComponent($.cookie("anonymous-consents")));
        $(element).closest('.consentmanagement-bar').hide();
        var consentCode = ($(element).closest('.consentmanagement-bar').data('code'));
        ACC.consent.changeConsentState(anonymousConsentCookie,consentCode,state)
        $.cookie("anonymous-consents",JSON.stringify(anonymousConsentCookie),{json:true, path:'/'});
    },


    bindConsentManagementAlertBar: function () {

        //accordion behaviour
        var accordion = $('#consent-management-alert').find('[data-behavior="accordion"]');
        var expandedClass = 'is-expanded';

        $.each(accordion, function() {

            var accordionItems = $(this).find('[data-binding="expand-accordion-item"]');

            $.each(accordionItems, function() {
                var $this = $(this);
                var triggerBtn = $this.find('[data-binding="expand-accordion-trigger"]');

                var setHeight = function(nV) {

                    var innerContent = nV.find('.consent-management-list__content-inner')[0],
                        maxHeight = $(innerContent).outerHeight(),
                        content = nV.find('.consent-management-list__content')[0];

                    if (!content.style.height || content.style.height === '0px') {
                        $(content).css('height', maxHeight);
                    } else {
                        $(content).css('height', '0px');
                    }
                };

                var toggleClasses = function(event) {
                    var clickedItem = event.currentTarget;
                    var currentItem = $(clickedItem).parent();
                    var clickedContent = $(currentItem).find('.consent-management-list__content')
                    $(currentItem).toggleClass(expandedClass);
                    setHeight(currentItem);

                    if ($(currentItem).hasClass('is-expanded')) {
                        $(clickedItem).attr('aria-selected', 'true');
                        $(clickedItem).attr('aria-expanded', 'true');
                        $(clickedContent).attr('aria-hidden', 'false');

                    } else {
                        $(clickedItem).attr('aria-selected', 'false');
                        $(clickedItem).attr('aria-expanded', 'false');
                        $(clickedContent).attr('aria-hidden', 'true');
                    }
                }

                triggerBtn.on('click', function(event) {
                    event.preventDefault();
                    toggleClasses(event);
                });

                // keyboard navigation
                $(triggerBtn).on('keydown', function(event) {
                    if (event.keyCode === 13 || event.keyCode === 32) {
                        event.preventDefault();
                        toggleClasses(event);
                    }
                });
            });

        });

    }



};




