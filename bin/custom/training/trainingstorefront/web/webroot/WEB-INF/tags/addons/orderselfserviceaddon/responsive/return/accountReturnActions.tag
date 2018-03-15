<%@ attribute name="returnRequest" required="true"
              type="de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="returnActionsSection" id="returnActions" data-theme="b" data-role="content">

    <!--Button begins-->
    <div class="row">
        <!--Button Back To Returns begins-->
        <div class="return-details-actions">
            <div class="col-sm-6 col-md-5 col-lg-4 pull-left">
                <a href="${request.contextPath}/my-account/returns" data-role="button">
                    <button type="submit" class="btn btn-default btn-block" id="backtoreturnsbutton">
                        <spring:theme code="text.account.return.details.back.to.returns"/>
                    </button>
                </a>
            </div>
        </div>
        <!--Button Print Return Label begins-->
        <c:if test="${not empty returnRequest.returnLabelDownloadUrl}">
            <div class="return-details-actions">
                <div class="col-sm-6 col-md-5 col-lg-4 pull-right">
                    <a href="${fn:escapeXml(returnRequest.returnLabelDownloadUrl)}"  download="ShippingLabel">
                        <button type="submit" class="btn btn-primary btn-block" id="printreturnlabel">
                            <spring:theme code="text.account.return.details.print.label"/>
                        </button>
                    </a>
                </div>
            </div>
        </c:if>
    </div>

</div>
