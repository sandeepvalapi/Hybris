<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="account-section-header">
	<div class="row">
		<div class="container-lg col-md-6">
			<spring:theme code="text.account.notificationPreference.notificationPreferenceForm" />
		</div>
	</div>
</div>

<div class="row">
	<div class="container-lg col-md-6">
		<div class="account-section-content">
			<div class="account-section-form">
				<form:form action="${fn:escapeXml(action)}" method="post" modelAttribute="notificationPreferenceForm">	
					<c:if test="${not empty notificationPreferenceForm}">
					    <c:forEach items="${notificationPreferenceForm.channels}" var="preference" varStatus="status">
					      <c:if test="${preference.visible}">
								<div class="checkbox">
									<label class="control-label notification_preference_channel">
			   							<form:checkbox idKey="${preference.channel}" path="channels[${status.index}].enabled" tabindex="${loop.index}"/>
			   							<input style="display:none" name= "channels[${fn:escapeXml(status.index)}].channel" value="${fn:escapeXml(preference.channel)}">
			   							<span><spring:theme code="notification.channel.${preference.channel}" arguments="${preference.value}"/></span>
				   					</label>
				   				</div>	
				   			</c:if>	     						   
					    </c:forEach>
                    </c:if>
					
					<div class="row">
						<div class="col-sm-6 col-sm-push-6">
							<div class="accountActions">
								<button type="submit" class="btn btn-primary btn-block">
									<spring:theme code="notificationPreferenceSetting.submit" text="Update Password" />
								</button>
							</div>
						</div>
						<div class="col-sm-6 col-sm-pull-6">
							<div class="accountActions">
								<button type="button" class="btn btn-default btn-block backToHome">
									<spring:theme code="notificationPreferenceSetting.cancel" text="Cancel" />
								</button>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>