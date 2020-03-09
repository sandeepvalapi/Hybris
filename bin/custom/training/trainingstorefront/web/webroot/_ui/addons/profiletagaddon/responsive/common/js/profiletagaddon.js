(function () {
    if ( typeof window.CustomEvent === "function" ) { return false; }
    function CustomEvent ( event, params ) {
      params = params || { bubbles: false, cancelable: false, detail: undefined };
      var evt = document.createEvent( 'CustomEvent' );
      evt.initCustomEvent( event, params.bubbles, params.cancelable, params.detail );
      return evt;
     }
    CustomEvent.prototype = window.Event.prototype;
    window.CustomEvent = CustomEvent;
  })();

window.mediator.subscribe('trackAddToCart', function notifyProfileTagAddToCart(data) {
    if (data.productCode && data.quantity) {
        try {
            if (data.cartData && data.cartData.categories) {
                data.cartData.categories = JSON.parse(data.cartData.categories);
            }
            var profileTagElement = document.querySelector("body");
            var notifyProfileTag = new CustomEvent('notifyProfileTagAddToCart', {detail: data});
            profileTagElement.dispatchEvent(notifyProfileTag);
        } catch(err){
            console.error(err);
        }
    }
});

window.mediator.subscribe('trackUpdateCart', function notifyProfileTagUpdateCart(data) {
    if (data.productCode && data.initialCartQuantity && data.newCartQuantity) {
        try {
            var profileTagElement = document.querySelector("body");
            var notifyProfileTag = new CustomEvent('notifyProfileTagUpdateCart', {detail: data});
            profileTagElement.dispatchEvent(notifyProfileTag);
        } catch (err) {
            console.error(err);
        }
    }
});

window.mediator.subscribe('trackRemoveFromCart', function notifyProfileTagRemoveFromCart(data) {
    if (data.productCode && data.initialCartQuantity) {
        try {
            var profileTagElement = document.querySelector("body");
            var notifyProfileTag = new CustomEvent('notifyProfileTagRemoveFromCart', {detail: data});
            profileTagElement.dispatchEvent(notifyProfileTag);
        } catch(err) {
            console.error(err);
        }
    }
});