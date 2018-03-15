if (!hac)
	var hac = {};

$(function() {
	hac.homeLink = $('#mainContainer').attr('data-homeLink');
	hac.contextPath = $('#mainContainer').attr('data-contextPath');
	
	hac.global = {};
	
	// global settings
	hac.global.timeToFade = 1000; // ms
	
	hac.global.isFullWidth = function() {
		return $('#content').hasClass('span-24');
	};
	
	hac.global.toggleSidebar = function() {
		if (hac.global.isFullWidth()) {
			$('#content').removeClass('span-24 last').addClass(
					'span-17 colborder');
			$('#content div:first').removeClass('marginRight');
			$("div[id^=sidebar]").each(
					function(pos, sidebar) {
						if ($('#tabs').length) {
							var selected = $('#tabs').tabs('option', 'selected'); 
							$('#sidebar' + (selected + 1)).show();
						} else {
							$('#sidebar').show();
						}
					});
	
			$('#toggleSidebarButton').html('>');
		} else {
			$("div[id^=sidebar]").hide(
					250,
					function() {
						$('#content').removeClass('span-17 colborder').addClass('span-24 last ');
						$('#content div:first').addClass('marginRight');
						$('#toggleSidebarButton').html('<');
					});
		}
	};
	
	hac.global.modalDialog = function(title, msg, fnc) {
		msg = msg
				|| "Unable to perform this action. Is the server running?";
	
		$("<div>" + msg + "<div>").dialog({
			title : title,
			modal : true,
			hide : 'fade',
			show : 'fade',
			buttons : {
				"OK" : function() {
					$(this).dialog("close");
					if (fnc)
						fnc();
				}
			}
		});
	};
	
	hac.global.err = function(xhr, textStatus, errorThrown) {
		hac.global.error(errorThrown);
	}
	
	/**
	 * Displays global notice message
	 */
	hac.global.notify = function(msg, duration, whenDone) {
		hac.global.msg(msg, "notify", duration, whenDone);
	};
	
	/**
	 * Displays global error message
	 */
	hac.global.error = function(msg, duration, whenDone) {
		hac.global.msg(msg, "error", duration, whenDone);
	};
	
	hac.global.msg = function(msg, type, duration, whenDone) {
		type = type || "notify";
		duration = duration || 5000;
	
		$('#msg').text(msg);
	
		if (type.toUpperCase() == 'NOTIFY' || type.toUpperCase() == 'NOTICE')
			$('#notification').css('background-color',
					'#326CA6');
		else if (type.toUpperCase() == 'ERROR')
			$('#notification').css('background-color',
					'#bc0000');
	
		$('#notification')
				.slideDown(
						'slow',
						'easeOutBounce',
						function() {
							setTimeout(
									"$('#notification').slideUp('slow', 'easeInCubic')",
									duration);
							if (whenDone)
								setTimeout(whenDone,
										duration + 1000);
						});
	};
	
	hac.global.hideSubnavigation = function() {
		$('.subnavigation').fadeOut();
		hac.global.hideTimerId = null;
		$('nav.topnavigation ul li a').removeClass('activeTab');
	};
	
	hac.global.getSpinnerImg = function() {
		return '<img class="spinner" src="' + hac.homeLink
				+ 'static/img/spinner.gif" alt="spinner"/>';
	};
	
	hac.global.removeImg = function() {
		return '<img class="removeIcon" src="' + hac.homeLink
		+ 'static/img/delete.png" alt="remove"/>';						
	};
	
	hac.global.applyImg = function() {
		return '<img class="applyIcon" src="' + hac.homeLink 
		+ 'static/img/tick.png" alt="apply"/>';
	};
	
	hac.global.trueIcon = function() {
		return '<img src="' + hac.homeLink
		+ 'static/img/tick.png" alt="true"/>';							
	};
	
	hac.global.booleanIcon = function(state) {
		return state == true ? hac.global.trueIcon() : hac.global.falseIcon();
	}
	
	hac.global.falseIcon = function() {
		return '<img src="' + hac.homeLink
		+ 'static/img/cross.png" alt="false"/>';							
	};
	
	hac.global.toggleActiveSidebar = function(num) {
		// fadeout
		$("div[id^=sidebar]").each(function() {
			$(this).fadeOut('fast');
		});
	
		if (!hac.global.isFullWidth())
			setTimeout("$('#sidebar" + num
					+ "').fadeIn('fast');", 500);
	};
	
	hac.global.messageFromTag = function(element) {
		if (element.length > 0) {
			if (element.attr("data-level") == "notice") {
				hac.global.notify(element.attr("data-result"));
			} else {
				hac.global.error(element.attr("data-result"));
			}			
		}						
	};
	
	/**
	 * Opens url defined in "data-href" attribute of "element" Jquery object.
	 */
	hac.global.openurl = function(element) {
		debug.log(element)
		var href = element.attr('data-href');
		window.location = href;
		return true;						
	};
	
	/**
	 * Displays confirmation message defined in "data-confirm-msg" attribute of "element" Jquery object 
	 * for action and if user will agree opens url defined in "data-href" attribute 
	 * of "element" Jquery object. 
	 */
	hac.global.confirm = function(element) {
		var confirmMsg = element.attr('data-confirm-msg');
		var url = element.attr('data-href');
		
		if (confirm(confirmMsg)) {
			window.location = url;
			return true;
		} else {
			return false;
		}
	}
	
	hac.global.saveToLocalStorage = function(key, object) {
		var stringified = JSON.stringify(object);
		
		if (Modernizr.localstorage) {
			localStorage[key] = stringified;
		}
		else {
			$.cookie(key, stringified, {
				expires : 100,
				path: '/'
			});	
		}		
	};
	
	hac.global.removeFromLocalStorage = function(key) {
		if (Modernizr.localstorage) {
			localStorage.removeItem(key);
		} else {
			$.cookie(key, null);
		}
	};
	
	hac.global.getObjectFromLocalStorage = function(key) {
		var data;
		
		if (Modernizr.localstorage)
		{
			data = localStorage[key];
		}
		else
		{
			data = $.cookie(key);	
		}
		
		return data == null ? null : JSON.parse(data);
	};
});
