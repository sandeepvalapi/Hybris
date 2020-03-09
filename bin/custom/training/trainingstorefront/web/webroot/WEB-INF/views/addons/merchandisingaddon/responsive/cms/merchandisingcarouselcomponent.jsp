<%--

    Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.

--%>
<div id="merchcarouselComponent${componentID}"
	data-currency="${currency}"
	data-strategy="${strategy}"
	data-name="${name}"
	data-numbertodisplay="${numberToDisplay}"
	data-scroll="${scroll}"
	data-title="${title}"
	data-backgroundcolour="${backgroundColour}"
	data-textcolour="${textColour}"
	data-url="${serviceUrl}"
	>
</div>
<script>
	(function () {
		'use strict';
		var current = {
			el: document.querySelector('#merchcarouselComponent${componentID}')
		};
		window.__merchcarousels = window.__merchcarousels || {};
		window.__merchcarousels[current.el.id] = current;

		if (window.__merchcarousels.CarouselComponent) {
			window.__merchcarousels.CarouselComponent.init();
		}
	})();
</script>

