/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
(function(){

	// Fix flickering in storefront when image.src=undefined
	if (window.Imager) {
         Imager.prototype.replaceImagesBasedOnScreenDimensions = function(image) {
            var availableWidths = [], srcARRAY, cwidth;

            if ($(image).attr("data-media") !== undefined) {
                var eMedia = $(image).attr("data-media");
                $(image).removeAttr("data-media");
                eMedia = $.parseJSON(eMedia);
                $.each(eMedia, function(key, value) {
                    availableWidths.push(parseInt(key));
                });
                $(image).data("width", availableWidths);
                $(image).data("media", eMedia);
            }

            srcARRAY = $(image).data("media");
            cwidth = Imager.getClosestValue(window.innerWidth, $.extend([], $(image).data("width")));

            if (!srcARRAY[cwidth] || image.src == srcARRAY[cwidth]) {
                return;
            }
            image.src = srcARRAY[cwidth];
        };
	}

	window.smartedit.addOnReprocessPageListener(function() {

		// reinitializing the list of images processed through the Imager object
		ACC.global.initImager();

		// replacing images according to screen size, when required
		ACC.global.reprocessImages();

		// replaying the my account nav only if it has been swapped by new component
		if ($(".navigation--top .myAccountLinksHeader").length === 0) {
			$('.js-myAccount-root').empty();
			$(".js-secondaryNavAccount > ul").empty();
			ACC.navigation.myAccountNavigation();
		}

		// Make sure the carousel component is reloaded properly after re-rendering.
		ACC.carousel.bindCarousel();
		
		// Make sure SearchBox Component is reloaded properly after re-rendering.
		ACC.autocomplete.bindSearchAutocomplete();
	});

})();