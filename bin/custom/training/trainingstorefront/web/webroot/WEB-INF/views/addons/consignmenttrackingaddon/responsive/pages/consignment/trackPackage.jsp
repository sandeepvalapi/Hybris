<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="consignment-track-popup">
	<div class="statusProgress">
		<c:choose>
			<c:when test="${empty consignment.carrierDetails or empty consignment.trackingID}">
				<div class="form-group row">
					<div class="col-sm-12 col-xs-12 ">
						<p><spring:message code="text.consignment.tracking.unavailable" /></p>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group row carrier-info">
					<div class="col-sm-12 col-xs-12">
						<label>
							<spring:message code="label.delivery.service.name" arguments="${consignment.carrierDetails.name}" />
						</label>
						<a href="${fn:escapeXml(trackingUrl)}" target="_black"> 
							<spring:message code="label.consignment.tracking.id" arguments="${consignment.trackingID}" />
						</a>
					</div>
				</div>
				<div class="form-group row status-info">
					<div class="col-sm-12 col-xs-12">
						<label>
							<spring:message var="statusDisplay" code="text.account.order.consignment.status.${consignment.statusDisplay}" />
							<spring:message code="label.consignment.status" arguments="${statusDisplay}" />
						</label>
					</div>
					<div class="col-sm-12 col-xs-12">
						<span> 
							<spring:message code="label.consignment.expected.time" text="Expected delivery"/>:&nbsp;
							&nbsp;<fmt:formatDate value="${consignment.targetArrivalDate}" pattern="E, MM/dd/yyyy, hh:mm a" />
						</span>
					</div>
				</div>
				 
				<div class="form-group row">
					<div class="col-sm-12 col-xs-12">
						<c:choose>
							<c:when test="${empty consignment.trackingEvents}">
								<div class="col-sm-12 col-xs-12 no-ship-detail">
									<spring:message code="text.consignment.tracking.unavailable" />
								</div>
							</c:when>
							<c:otherwise>
								<div id="events">
									<c:forEach items="${consignment.trackingEvents}" var="event" varStatus="index">
										<div class="col-sm-12 col-xs-12 ship-detail-date">
											<ul>
												<li><fmt:formatDate value="${event.eventDate}" pattern="E, MM/dd/yyyy, hh:mm a"/></li>
												<li><label>
													<spring:message code="text.account.order.consignment.status.${fn:toLowerCase(event.referenceCode)}" />
												</label></li>
												<li>[${fn:escapeXml(event.location)}] ${fn:escapeXml(event.detail)}</li>
											</ul>
										</div>
									</c:forEach>
								</div>
							</c:otherwise>
						</c:choose>
					</div>			
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>