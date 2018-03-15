/** Handler for local History, saved in permanent Cookies.
 *  This Handler is used for: Beanshell, Groovyshell.
 *  @author manuel.feyrer
 */

var cookieName;
var cookieHistory = { };
var encryptionUrl;
var decryptionUrl;
var cookiePath;
var maxCookieSize = 3000;

$(function(){
    var token = $("meta[name='_csrf']").attr("content");

	encryptionUrl = $('#content').attr("encryptUrl");
	decryptionUrl = $('#content').attr("decryptUrl");
	
	cookiePath = $("#tabs-history").attr("cookiePath");
	cookieName = $("#tabs-history").attr("cookieName");
	
	$("#executeButton").click(function(){
		$("#historyLegend").show();
		
		var name = null;
		var historyItem = null;
		var statement = $.trim(editor.getValue());
		
		//Remove history statement if already available.
		$.each(cookieHistory, function(cookieName,cookie){
			if(statement == cookie.statement){
				cookie.html.hide();
				name = cookieName;
			}
		});
		
		name = (name == null) ? getNewCookieName() : name;
		historyItem = new historyEntry(statement, getDateTimeFormatted(), name);
		encryptWriteCookie(historyItem, name);
	});
	
	function isMaxCookieSizeExceeded(name, statement, ciphertext){
		if(cookieHistory.hasOwnProperty(name)){
			if(cookieHistory[name].statement == statement){
				return (getCookieSize() >= maxCookieSize);
			}
			else{
				var oldCookieSize = calcsize(cookieHistory[name].ciphertext);
				return (getCookieSize() - oldCookieSize + calcsize(ciphertext)) >= maxCookieSize;
			}
		}
		else{
			return (getCookieSize() + calcsize(ciphertext)) >= maxCookieSize;
		}
	}
	
	function getCookieSize(){
		var cookieSize = 0;
		$.each(cookieHistory, function(name, value){
			cookieSize += calcsize($.cookie(name));
		});
		return cookieSize + 64; //JSESSIONID consists of 32 characters.
	}
	
	function calcsize(content){
	    var utf8length = 0;
	    for (var n = 0; content &&  (n < content.length); n++) {
	        var c = content.charCodeAt(n);
	        if (c < 128) {
	            utf8length++;
	        }
	        else if((c > 127) && (c < 2048)) {
	            utf8length = utf8length+2;
	        }
	        else {
	            utf8length = utf8length+3;
	        }
	    }
	    return utf8length;
	}
	
	//Create or update Cookie.
	function createCookie(name, content){
		$.cookie(name, content, {	
			   expires : 365,
			   path: cookiePath
		});
	}
	
	//delete cookie
	function deleteCookie(name){
		$.cookie(name, "", {	
			   expires : -1,
			   path: cookiePath
			});
	}
	
	//Search for available cookies and get free name.
	function getNewCookieName(){
		var i = 0;
		while(true){
			var cookie = $.cookie(cookieName + i);
			if(!cookie){
				return cookieName+i;
			}
			i++;
		}
	}
	
	//Clear History View and delete all cookies.
	$("#clearHistory").click(function(){
		$("#historyLegend").hide();
		$("#historyList").html("");
		$.each(cookieHistory, function(name,cookie){
			deleteCookie(name);
		});
		cookieHistory = {};
	});
	
	//Encrypt the object and save it to cookie.
	function encryptWriteCookie(historyItem, name){
		var cookieObject = {statement: historyItem.statement, additionalDescription: historyItem.additionalDescription};
		var cookie = JSON.stringify(cookieObject);
		
		$.ajax({
			url : encryptionUrl,
			type : 'POST',
			async: false,
			data : {
				cookie: cookie
			},
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				if (data.exception) {
					deleteCookie(name);
				}
				else{
					if(!isMaxCookieSizeExceeded(name, historyItem.statement, data.ciphertext)){
						cookieHistory[name] = historyItem;
						historyItem.ciphertext = data.ciphertext;
						$("#historyList").append(historyItem.html);
						createCookie(name,data.ciphertext);
						$("#historyMax").hide();
					}
					else{
						$("#historyMax").show();
					}
				}
			},
			error : hac.global.err
		});
	}
	
	//Get all cookies starting with cookieName.
	function getAllCookies() {
		var cookies = { };
		if (document.cookie && document.cookie != '') {
			var split = document.cookie.split(';');
			for (var i = 0; i < split.length; i++) {
				var name_value = split[i].split("=");
				name_value[0] = name_value[0].replace(/^ /, '');
				if(decodeURIComponent(name_value[0]).match("^"+cookieName)){
					cookies[decodeURIComponent(name_value[0])] = decodeURIComponent(name_value[1]);
				}
			}
		}
		return cookies;
	}
	
	//Initially load cookies and show them in history tab.
	function initHistory(){
		var cookies = getAllCookies();

        $.each(cookies, function(name,cookie){
			$.ajax({
				url : decryptionUrl,
				type : 'POST',
				async: false,
				data : {
					cookie: cookie
				},
				headers : {
					'Accept' : 'application/json',
                    'X-CSRF-TOKEN' : token
                },
				success : function(data) {
					if (data.exception) {
						deleteCookie(name);
					}
					else{
						$("#historyLegend").show();
						var cookieObject = JSON.parse(data.plaintext);
						var historyItem = new historyEntry(cookieObject.statement, cookieObject.additionalDescription, name);
						historyItem.ciphertext = cookie;
						$("#historyList").append(historyItem.html);
						cookieHistory[name] = historyItem;
					}
				},
				error : hac.global.err
			});
		});
		$("#historyMax").hide();
	}

	var xssEntityMap = {
		"<": "&lt;",
		">": "&gt;",
		"/": '&#x2F;',
		"&": "&amp;",
		'"': '&quot;',
		"'": '&#39;'
	};

	function sanitizeInput(string) {
		return String(string).replace(/[&<>"'\/]/g, function (s) {
			return xssEntityMap[s];
		});
	}


	//Constructor-Method for HistoryEntry Object which contains all information for showing Statement in History.
	function historyEntry(statement, additionalDescription, name){
		//Attributes
		this.statement = statement;
		this.additionalDescription = additionalDescription;
		this.name = name;
		this.isExpanded = false;
		this.isAlive = true;
		this.linkExpand = $("<a href='#'>" + sanitizeInput(sliceStatement(this.statement)) + "<a/>");
		this.insertBtn = $("<button>Insert</button>");
		this.deleteBtn = $("<button class='deleteButton'>X</button>");
		this.leftExpandedPanel = $("<div style='float:left;width:90%;word-wrap:break-word'></div>").text(this.statement).append("</br>").append(this.insertBtn);
		this.rightExpandedPanel = $("<div style='float:right'></div>").append(this.deleteBtn);
		this.expandedDiv = $("<div class='extendedHistoryItem'></div>").append(this.leftExpandedPanel).append(this.rightExpandedPanel).append($("<div style='clear:both' />")).hide();
		this.ciphertext;
		
		this.html = $("<li></li>").append(this.linkExpand).append("<br/><em class='small'>(" + additionalDescription + ")</em>").append(this.expandedDiv);
		
		//Methods:
		//Add click-handler to Expand-link. With proxy make this context in method historyEntry-Object.
		this.linkExpand.click($.proxy(function(event){
			if(this.isExpanded){
				this.expandedDiv.fadeOut();
				this.isExpanded = false;
			}
			else{
				this.expandedDiv.fadeIn();
				this.isExpanded = true;
			}
		}, this));
		//Add click-handler to insert-Button.
		this.insertBtn.click($.proxy(function(event){
			$("#nav-tabs-edit").click();
			editor.setValue(this.statement);
		}, this));
		//Add click-handler to delete-Button.
		this.deleteBtn.click($.proxy(function(event){
			this.html.fadeOut();
			deleteCookie(this.name);
			this.isAlive = false;
			delete cookieHistory[name];
			$("#historyMax").hide();			
		}, this));
	}

	//Slices Statement to maximum length of 50 characters if needed.
	function sliceStatement(statement){
		var shortStatement = "";
		if(statement.length > 47){
			shortStatement = statement.slice(0,47);
			shortStatement += "...";
		}
		else{
			shortStatement = statement;
		}
		return shortStatement;
	}
		
	//Returns the current Date and time in format: DD/MM/YYYY HH:MM:SS as String.
	function getDateTimeFormatted(){
		var myDate = new Date();
		var day = "" + myDate.getDate();
		if(day.length < 2){ 
			day = "0"+day;
		}
		var month = myDate.getMonth();
		month = (month+1) + "";
		if(month.length < 2){
			month = "0"+month;
		}
		var year = myDate.getFullYear();
		var hours = "" + myDate.getHours();
		if(hours.length < 2){
			hours = "0"+hours;
		}
		var minutes = "" + myDate.getMinutes();
		if(minutes.length < 2){ 
			minutes = "0"+minutes;
		}
		var seconds = "" + myDate.getSeconds();
		if(seconds.length < 2){
			seconds = "0"+seconds;
		}
		return "" + day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
	}

	//Do actions when loading page.
	
	//initially hide historyLegend
	$("#historyLegend").hide();
	//Initially fill history-list, if cookie containing history is available.
	initHistory();
});