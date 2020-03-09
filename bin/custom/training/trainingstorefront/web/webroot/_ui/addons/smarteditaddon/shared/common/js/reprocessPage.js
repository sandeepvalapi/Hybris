/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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