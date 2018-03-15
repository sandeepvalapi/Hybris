<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="returnRequest" required="true"
              type="de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<div class="return-overview">
    <div class="row">
        <div class="col-sm-2 col-md-2">
            <div class="item-group">
                <ycommerce:testId code="return_overviewReturnRequestID_label">
                    <span class="item-label"><spring:theme code="text.account.return.details.rma"/></span>
                    <span class="item-value">${fn:escapeXml(returnRequest.rma)}</span>
                </ycommerce:testId>
            </div>
        </div>
        <div class="col-sm-2 col-md-2">
            <div class="item-group">
                <ycommerce:testId code="return_overviewReturnRequestStatus_label">
                    <span class="item-label"><spring:theme code="text.account.return.details.status"/></span>
                        <span class="item-value"><spring:theme
                                code="text.account.return.status.display.${fn:toUpperCase(fn:escapeXml(returnRequest.status))}"/></span>
                </ycommerce:testId>
            </div>
        </div>
        <div class="col-sm-2 col-md-2">
            <div class="item-group">
                <ycommerce:testId code="return_overviewReturnRequestOrder_label">
                    <span class="item-label"><spring:theme code="text.account.return.details.order.number"/></span>
                    <span class="item-value">${fn:escapeXml(returnRequest.order.code)}</span>
                </ycommerce:testId>
            </div>
        </div>
        <!--Button Print Return Label begins-->
        <c:if test="${not empty returnRequest.returnLabelDownloadUrl && returnRequest.cancellable}">
            <div class="return-details-actions">
                <div class="col-sm-4 col-md-3 col-lg-3 pull-right">
                    <a href="${fn:escapeXml(returnRequest.returnLabelDownloadUrl)}" download="ShippingLabel">
                        <button type="submit" class="btn btn-default btn-block" id="printreturnlabel">
                            <spring:theme code="text.account.return.details.print.label"/>
                        </button>
                    </a>
                </div>
            </div>
        </c:if>
        <div class="col-sm-4 col-md-3 col-md-offset-3 col-sm-offset-2" style="${style}">
            <div class="item-action">
                <c:set var="returnCode" value="${fn:escapeXml(returnRequest.code)}" scope="request"/>
                <c:set var="returnCancellable" value="${returnRequest.cancellable}" scope="request"/>
                <action:actions element="div" parentComponent="${component}"/>
            </div>
        </div>
    </div>
</div>
