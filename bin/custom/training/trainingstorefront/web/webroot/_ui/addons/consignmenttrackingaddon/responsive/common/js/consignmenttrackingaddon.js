ACC.tracking = {
    _autoload: ["trackPackage"],
    trackPackage: function() {
        $(".consignment-tracking-link").click(function() {
            var title = $(this).attr("data-colorbox-title");
            var href = $(this).attr("data-url");
            ACC.common.checkAuthenticationStatusBeforeAction(function() {
                ACC.colorbox.open(ACC.common.encodeHtml(title), {
                    href: href,
                    maxWidth: "100%",
                    width: "700px",
                    initialWidth: "700px"
                });
            });
        });
    }
};