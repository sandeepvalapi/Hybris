ACC.minicart = {

	_autoload: [
		"bindMiniCart"
	],

	bindMiniCart: function(){

        $(document).on("click",".js-mini-cart-link", function(e){
            e.preventDefault();
            var url = $(this).data("miniCartUrl");
            var cartName = ($(this).find(".js-mini-cart-count").html() != 0) ? $(this).data("miniCartName"):$(this).data("miniCartEmptyName");

            ACC.colorbox.open(cartName,{
                href: url,
                maxWidth:"100%",
                width:"380px",
                initialWidth :"380px"
            });
        });

        $(document).on("click",".js-mini-cart-close-button", function(e){
            e.preventDefault();
            ACC.colorbox.close();
        });
    },

    updateMiniCartDisplay: function(){
        var cartItems = $(".js-mini-cart-link").data("miniCartItemsText");
        var miniCartRefreshUrl = $(".js-mini-cart-link").data("miniCartRefreshUrl");
        $.ajax({
            url: miniCartRefreshUrl,
            cache: false,
            type: 'GET',
            success: function(jsonData){
                $(".js-mini-cart-link .js-mini-cart-count").html('<span class="nav-items-total">' + jsonData.miniCartCount + '<span class="items-desktop hidden-xs hidden-sm">' + ' ' + cartItems + '</span>' + '</span>' );
                $(".js-mini-cart-link .js-mini-cart-price").html(jsonData.miniCartPrice);
            }
        });
    }

};