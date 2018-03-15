/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
(function(){

	window.smartedit = window.smartedit || {};

	var onReprocessListeners = [];
	window.smartedit.reprocessPage = function(){
		onReprocessListeners.forEach(function( callbackFn ){
			try{
				callbackFn();
			}
			catch( e ){}
		});
	};

	window.smartedit.addOnReprocessPageListener = function( callbackFn ){
		if( !_isFunction( callbackFn ) ){
			throw new Error( 'SmartEditAddon - Cannot register page reprocessing listener. Provided callback must be a function.' );
		}

		onReprocessListeners.push(callbackFn);
	};

	function _isFunction( obj ){
		return ( obj && typeof obj === 'function');
	}

})();