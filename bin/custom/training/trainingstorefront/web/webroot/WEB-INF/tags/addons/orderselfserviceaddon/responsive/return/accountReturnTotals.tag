<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="returnRequest" required="true"
              type="de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="returnTotal">
    <div class="row">

        <div class="col-sm-6 col-md-5 col-lg-4 col-sm-offset-6 col-md-offset-7 col-lg-offset-8" >
            <div class="returnTotal__wrapper"> 
                <div class="row">

                    <div class="returnTotal__wrapper-total-label">
                        <div class="">
                            <spring:theme code="text.account.return.details.totals" text="Return Totals"/>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <spring:theme code="text.account.return.details.subtotal"/>
                    </div>
                    <div class="col-xs-6">
                        <div class="text-right returnTotal__wrapper-price">
                            <ycommerce:testId code="returnTotal_subTotal_label">
                                <format:price priceData="${returnRequest.subtotal}"/>
                            </ycommerce:testId>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <spring:theme code="text.account.return.details.delivery"/>
                    </div>
                    <div class="col-xs-6">
                        <div class="text-right returnTotal__wrapper-price">
                            <ycommerce:testId code="returnTotal_deliveryCost_label">
                                <c:choose>
                                    <c:when test="${returnRequest.refundDeliveryCost}">
                                        <format:price priceData="${returnRequest.deliveryCost}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span>-</span>
                                    </c:otherwise>
                                </c:choose>
                            </ycommerce:testId>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="totals">
                            <spring:theme code="text.account.return.details.total"/>
                        </div>
                    </div>

                    <div class="col-xs-6 text-right">
                        <div class="totals returnTotal__wrapper-price returnTotal__wrapper-total-price">
                            <ycommerce:testId code="returnTotal_totalPrice_label">
                                <format:price priceData="${returnRequest.total}"/>
                            </ycommerce:testId>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
