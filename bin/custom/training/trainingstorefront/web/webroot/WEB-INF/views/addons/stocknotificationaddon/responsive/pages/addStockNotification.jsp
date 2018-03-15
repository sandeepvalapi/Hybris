<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="${action}" var="addInterestUrl" />

<div id="AddFail" class="display-none">
	<spring:theme code="add.stockNotification.fail" />
</div>

<div id="RemoveFail" class="display-none">
	<spring:theme code="remove.stockNotification.fail" />
</div>


<c:choose>
	<c:when test="${empty removeUrl}">
		
		<c:set var="formattedExpiryDate">
			<fmt:formatDate value="${expiryDate.date}" pattern="MM/dd/yyyy"/>
		</c:set>
		<div>
			<span><b><spring:theme code="text.stock.notification.subscribt.subtitle" /></b></span>
		</div>
		<div id="StockInfo">
			<c:if test="${not empty expiryDate}">
				<span> <spring:theme code="text.stock.notification.info"
						argumentSeparator=","
						arguments="${formattedExpiryDate},${expiryDay}" />
				</span>
			</c:if>
		</div>
		<div>
			<span> <spring:theme code="text.stock.notification.info2" /></span>
			<br>
		</div>
		<div>
			<br> <span> <spring:theme
					code="text.stock.notification.info3" /></span> <br> <br>
		</div>

		<c:forTokens var="channel" items="email,sms" delims="," varStatus="loop">
			<c:if test="${not empty stockNotificationForm.emailAddress && channel == 'email'}">
				<div class="row">
					<div class="col-sm-6">
						<div class="col-xs-6 col-sm-6 notification-channel">
							<label for="channel${loop.index}">
								<span><spring:theme code="stocknotification.channel.${channel}" arguments="${stockNotificationForm.emailAddress}" /></span>
							</label>
						</div>
						<div class="col-xs-6 col-sm-6 notification-wrapper">
							<input id="channel${loop.index}" type="checkbox" name="notificationChannel" value="${channel}NotificationEnabled"
								${stockNotificationForm.emailNotificationEnabled eq true? 'checked="true"' : ''} />
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${not empty stockNotificationForm.mobileNumber && channel == 'sms'}">
				<div class="row">
					<div class="col-sm-6">
						<div class="col-xs-6 col-sm-6 notification-channel">
							<label for="channel${loop.index}">
								<span><spring:theme code="stocknotification.channel.${channel}" arguments="${stockNotificationForm.mobileNumber}" /></span>
							</label>
						</div>
						<div class="col-xs-6 col-sm-6 notification-wrapper">
							<input id="channel${loop.index}" type="checkbox" name="notificationChannel" value="${channel}NotificationEnabled"
								${stockNotificationForm.smsNotificationEnabled eq true? 'checked="true"' : ''} />
						</div>
					</div>
				</div>
			</c:if>
		</c:forTokens>

		<div class="row">
			<div class="col-sm-6 col-sm-push-6 notification-btns">
				<div class="accountActions">
					<button id="addInterestbtn" type="button" class="btn btn-primary btn-block">
						<spring:theme code="stocknotification.submit.new" text="Add"/>
					</button>
				</div>
			</div>
			<div class="col-sm-6 col-sm-pull-6 notification-btns">
				<div class="accountActions">
					<button id="cancelInterestbtn" type="button" class="btn btn-default btn-block backToHome">
						<spring:theme code="stocknotification.cancel" text="Cancel" />
					</button>
				</div>
			</div>
		</div>
	</c:when>
	
	<c:otherwise>
		<div id="StockInfo">
			<span><spring:theme code="text.stock.notification.subscribed.cancel"/></span>
		</div>
		
		<div class="row">
			<div class="col-sm-6 col-sm-push-6 notification-btns">
				<div class="accountActions">
					<button id="removeInterestbtn" type="button" class="btn btn-primary btn-block">
						<spring:theme code="stocknotification.remove" text="Yes, Unsubscribe Me" />
					</button>
				</div>
			</div>
			<div class="col-sm-6 col-sm-pull-6 notification-btns">
				<div class="accountActions">
					<button id="cancelInterestbtn" type="button" class="btn btn-default btn-block backToHome">
						<spring:theme code="stocknotification.cancel" text="Cancel"/>
					</button>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	$("div").remove("#cboxLoadingGraphic");
	$("div").remove("#cboxLoadingOverlay");
	
	$( document ).ready(checkSelectChannel);
	$("input[type=checkbox][name=notificationChannel]").click(checkSelectChannel);
	
	$("#cancelInterestbtn").click(function(){
		ACC.colorbox.close();
	});
	
	$("#addInterestbtn").click(function(){
		var stockNotificationForm={emailNotificationEnabled:false,smsNotificationEnabled:false};
		$("input[type=checkbox][name=notificationChannel]:checked").each(function(){
			stockNotificationForm[$(this).val()]=true;
		});
		
		var addInterestUrl=ACC.config.encodedContextPath+"${action}";
		
		$.ajax({
			type: "POST",
			url: addInterestUrl,
			async: true,
			data: stockNotificationForm,
			dataType: "text"
		}).done(function(data) {
		    if(data=="success"){
		    	ACC.colorbox.close();
		    	location.reload();
		    }
		    else{
		    	$("#AddFail").removeClass('display-none').toggleClass('alert-info alert-dismissable');
		    }
		})
	});
	$("#removeInterestbtn").click(function(){
		var rmUrl=ACC.config.encodedContextPath+"${removeUrl}";
		
		$.ajax({
			type: "POST",
			url: rmUrl
		}).done(function(data) {
		    if(data=="success"){
		    	ACC.colorbox.close();
		    	location.reload();
		    }
		    else{
		    	$("#RemoveFail").removeClass('display-none').toggleClass('alert-info alert-dismissable');
		    }
		})

	});
	
	function checkSelectChannel(){
		var checked = $("input[type=checkbox][name=notificationChannel]:checked").length;
		if (checked == 0) {
			$("#addInterestbtn").attr('disabled','disabled');
		}
		else{
			$("#addInterestbtn").removeAttr('disabled');
		}
	}

</script>