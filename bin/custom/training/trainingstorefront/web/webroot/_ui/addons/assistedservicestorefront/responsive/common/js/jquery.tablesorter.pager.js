(function($) {
	$.extend({
		tablesorterPager: new function() {

			function updatePageDisplay(c) {
				var s = $(c.cssPageDisplay,c.container).val((c.page+1) + c.seperator + c.totalPages);
			}

			function moveToPage(table) {
				var c = table.config;
				if(c.page < 0 || c.page > (c.totalPages-1)) {
					c.page = 0;
				}

				renderTable(table,c.rowsCopy);
			}

			function renderTable(table,rows) {

				var c = table.config;
				var l = rows.length;
				var s = (c.page * c.size);
				var e = (s + c.size);
				if(e > rows.length ) {
					e = rows.length;
				}


				var tableBody = $(table.tBodies[0]);

				// clear the table body

				$.tablesorter.clearTableBody(table);

				for(var i = s; i < e; i++) {

					//tableBody.append(rows[i]);

					var o = rows[i];
					var l = o.length;
					for(var j=0; j < l; j++) {

						tableBody[0].appendChild(o[j]);

					}
				}

				$(table).trigger("applyWidgets");

				if( c.page >= c.totalPages ) {
        			moveToLastPage(table);
				}

				updatePageDisplay(c);
			}

			this.appender = function(table,rows) {

				var c = table.config;

				c.rowsCopy = rows;
				c.totalRows = rows.length;
				c.totalPages = Math.ceil(c.totalRows / c.size);

				renderTable(table,rows);
			};

			this.defaults = {
				size: 5,
				offset: 0,
				page: 0,
				totalRows: 0,
				totalPages: 0,
				container: null,
				seperator: "/",
				appender: this.appender
			};

			this.construct = function(settings) {

				return this.each(function() {

                    config = $.extend(this.config, $.tablesorterPager.defaults, settings);

					var table = this, pager = config.container;

                    var curr = 0;
                    var numPages = Math.ceil($(table).find("tbody tr").length/config.size);
                    if (numPages > 1) {
                        while(numPages > curr){
                            $('<li><a href="#" class="page_link">'+(curr+1)+'</a></li>').appendTo(pager);
                            curr++;
                        }

                        $(this).trigger("appendCache");
                        $(pager).find('.page_link:first').addClass('active');
                        $(pager).find(".page_link").click(function() {
                            var clickedPage = $(this).html().valueOf()-1;
                            table.config.page = clickedPage;
                            moveToPage(table);
                            pager.find(".page_link").removeClass("active");
                            pager.find(".page_link").eq(clickedPage).addClass("active");
                            return false;
                        });
					}
				});
			};

		}
	});
	// extend plugin scope
	$.fn.extend({
        tablesorterPager: $.tablesorterPager.construct
	});

})(jQuery);