/*
 [y] hybris Platform
 Copyright (c) 2017 SAP SE or an SAP affiliate company.
 All rights reserved.
 This software is the confidential and proprietary information of SAP
 ("Confidential Information"). You shall not disclose such Confidential
 Information and shall use it only in accordance with the terms of the
 license agreement you entered into with SAP.
 */
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
