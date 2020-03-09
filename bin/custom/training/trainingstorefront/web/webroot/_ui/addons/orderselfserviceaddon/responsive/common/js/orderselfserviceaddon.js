(function(){
	var listOfActions = Array.prototype.slice.call(document.querySelectorAll('[class^="AccountOrderDetailsOverviewComponent"]'), 0);
	var firstVisibleAction = listOfActions.filter(function(element){
		return element.childElementCount > 0;
	})[0];
	if (firstVisibleAction){
		var target = firstVisibleAction.querySelectorAll('input[type="submit"], button')[0];
		target.className = target.className.replace(/btn\-default/g,'btn-primary');
	}
})();
