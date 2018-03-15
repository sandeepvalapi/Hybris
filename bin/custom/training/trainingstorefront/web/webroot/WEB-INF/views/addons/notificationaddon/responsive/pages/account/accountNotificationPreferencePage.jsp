<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
				<form:form action="${action}" method="post" commandName="notificationPreferenceForm">
					<c:forTokens var="channel" items="email,sms" delims="," varStatus="loop">
   						<c:if test="${not empty notificationPreferenceForm.emailAddress && channel == 'email'}">
							<div class="checkbox">
								<label class="control-label notification_preference_channel">
		   							<form:checkbox idKey="${channel}NotificationChannel" path="${channel}Enabled" tabindex="${loop.index}"/>
		   							<span><spring:theme code="notification.channel.${channel}" arguments="${notificationPreferenceForm.emailAddress}"/></span>	   							
			   					</label>
			   				</div>
   						</c:if>
   						<c:if test="${not empty notificationPreferenceForm.mobileNumber && channel == 'sms'}">
			   				<div class="checkbox">
			   					<label class="control-label notification_preference_channel">
		   							<form:checkbox idKey="${channel}NotificationChannel" path="${channel}Enabled" tabindex="${loop.index}"/>
		   							<span><spring:theme code="notification.channel.${channel}" arguments="${notificationPreferenceForm.mobileNumber}"/></span>
			   					</label>
			   				</div>
   						</c:if>
					</c:forTokens>
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