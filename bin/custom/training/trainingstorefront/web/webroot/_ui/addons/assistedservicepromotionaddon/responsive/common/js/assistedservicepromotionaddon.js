ACC.assistedservicepromotion = {
	bindAll: function ()
	{
		$(document).on("click",".asm-customer360-promotions-addToCart",function(e){
			ACC.assistedservicepromotion.handleCouponToCartAction("apply",this,"Failed to Apply to cart");
		});
		$(document).on("click",".asm-customer360-promotions-removefromCart-remove",function(e){
			ACC.assistedservicepromotion.handleCouponToCartAction("remove",this,"Failed to remove from cart");
		});
	},
	
	handleCouponToCartAction: function (couponToCartAction, element, errorMsg )
	{
		var index = $(element).attr('data-index');
		var params ={
				CSRFToken:ACC.config.CSRFToken,
				voucherCode:"csa_coupon_"+$(element).attr('data-coupon')
		};
	    var couponUrl = ACC.config.encodedContextPath + "/cart/voucher/" + couponToCartAction;
	    
	    return $.ajax({
			url : couponUrl,
			type : "POST",
			data : params,
			success : function(data) {
				var cssClassToToggle = 'hidden';
				$('.asm-customer360-promotions-addToCart[data-index='+index+']').toggleClass(cssClassToToggle);
				$('.asm-customer360-promotions-removefromCart[data-index='+index+']').toggleClass(cssClassToToggle);
			},
			error : function(xht, textStatus, ex) {
				$(element).html(errorMsg);
			}
		});
	}
};

$(document).ready(function ()
{
	ACC.assistedservicepromotion.bindAll();
});
