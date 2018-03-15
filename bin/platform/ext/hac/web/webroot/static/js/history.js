if (!hac) var hac = {};

hac.history = {};

hac.history.defaultHistory = {"/platform/init":{"t":"Initialization","c":1},"/monitoring/cache":{"t":"Cache","c":1},"/console/scripting":{"t":"Scripting Languages","c":1}};

hac.history.getPageKey = function() {
	if (hac.contextPath != '')
	{
		//remove context path
		var contextLessPath = location.pathname.substring(hac.contextPath.length);
		//debug.log(contextLessPath);
		return contextLessPath;
	}
	else
		return location.pathname; // will return /hack/platform/config for
								// example. Query parameters not included!
};

hac.history.getPageTitle = function() {
	return document.title.split(' \| ')[1];
};

hac.history.updateHistory = function() {
	
	var pageKey = hac.history.getPageKey();
	var history = hac.history.getHistory();
	
	if (history[pageKey])
	{
		history[pageKey]['c']++;
		history[pageKey]['t'] = hac.history.getPageTitle();
	}
	else
		history[pageKey] = {t:hac.history.getPageTitle(), c: 1};

	
	hac.history.saveHistory(history);
};

hac.history.getHistory = function() {
	var history = hac.global.getObjectFromLocalStorage('history');
	
	if (history == null) {
		hac.global.saveToLocalStorage('history', hac.history.defaultHistory);
		return hac.global.getObjectFromLocalStorage('history');
	}
	
	return history;
};

hac.history.saveHistory = function(historyObject) {
	hac.global.saveToLocalStorage('history', historyObject);
};

hac.history.clearHistory = function() {
	hac.global.removeFromLocalStorage('history')
}

hac.history.valueOfSingleKey = function(obj)
{
	for (pos in obj)
	{
		return obj[pos]['c'];
	}
	
};

hac.history.getSortedHistory = function()
{
	var history = hac.history.getHistory(); //object
	var sortedHistory = []; //array
	
	//convert history to array
	for (pos in history)
	{
		var index = sortedHistory.length;
		sortedHistory[index] = {};
		sortedHistory[index][pos] = history[pos];
	}
	
	sortedHistory.sort(function(a,b) {
		return hac.history.valueOfSingleKey(b) - hac.history.valueOfSingleKey(a);
	});
	
	return sortedHistory;
	
};

hac.history.getSortedTruncatedHistory = function(max)
{
	var sortedHistory = hac.history.getSortedHistory();
	
	if (sortedHistory.length > max)
		return sortedHistory.slice(0, max);
	else
		return sortedHistory;
};

$(document).ready(function() {

	if (window.doNotUpdateHistory === undefined)
		hac.history.updateHistory();
});