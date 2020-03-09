<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${not empty PROFILETAG_URL and not empty PROFILETAG_CONFIG_URL}">
    <script>

        var siteId = "${SITE_ID}";
        var profileTagUrl = "${PROFILETAG_URL}";
        var tenant = "${TENANT}";
        var profileTagConfigUrl = "${PROFILETAG_CONFIG_URL}";

        (function () {
            window.Y_TRACKING = window.Y_TRACKING || function () {
                (window.Y_TRACKING.q = window.Y_TRACKING.q || []).push(arguments);
            };
            var s = document.createElement('script');
            s.type = 'text/javascript';
            s.async = true;
            s.src = profileTagUrl;
            var x = document.getElementsByTagName('script')[0];
            x.parentNode.insertBefore(s, x);
        })();


        window.Y_TRACKING({siteId:siteId, tenant: tenant, configUrl: profileTagConfigUrl});

    </script>
</c:if>




