ACC.stocknotification = {
    _autoload: ["bindStockNotification", "enableNotificationButton"],
    bindStockNotification: function() {
        $("#arrival-notification").click(function(e) {
            var title = ACC.common.encodeHtml($(this).attr("data-box-title"));
            var productCode = $("span.code").text();
            var url = encodeURI(ACC.config.encodedContextPath + "/my-account/my-stocknotification/open/" + productCode + "?channel=pdp");
            
            ACC.common.checkAuthenticationStatusBeforeAction(function() {
                ACC.colorbox.open(title, {
                    href: url,
                    maxWidth: "100%",
                    width: "550px",
                    initialWidth: "550px"
                });
            });
        });
    },
    enableNotificationButton: function() {
        $("#arrival-notification").removeAttr("disabled");
    }
};