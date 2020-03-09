<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<spring:htmlEscape defaultHtmlEscape="true" />

<div id="AddFail" class="display-none">
	<spring:theme code="add.stockNotification.fail" />
</div>

<div id="RemoveFail" class="display-none">
	<spring:theme code="remove.stockNotification.fail" />
</div>


<div id="StockInfo">
	<c:if test="${not empty expiryDate}">
		<c:set var="formattedExpiryDate">
			<fmt:formatDate value="${expiryDate.date}" pattern="MM/dd/yyyy" />
		</c:set>
		<spring:theme var="stockNotifacationInfoHtml" code="text.stock.notification.info" arguments="${formattedExpiryDate}" htmlEscape="false" />
		<span> 
			${ycommerce:sanitizeHTML(stockNotifacationInfoHtml)}
		</span>
	</c:if>
</div> 
<div> 
	<span> 
		<spring:theme code="text.stock.notification.info2" />
	</span>
	<br>
</div>
<div>
	<br> 
	<span> 
		<spring:theme code="text.stock.notification.info3" />
	</span>
	<br>
	<br>
</div>
<form:form id="stockNotificationForm">
<c:forEach items="${stockNotificationForm.channels}" var="channel" varStatus="loop">
	<c:if test="${channel.visible}">
	<div class="row">
		<div class="col-sm-6">
			<div class="col-xs-6 col-sm-6 notification-channel">
				<label for="channel${fn:escapeXml(loop.index)}">
					<span>
						<spring:theme code="stocknotification.channel.${channel.channel}" arguments="${channel.value}" />
					</span>
				</label>
			</div>
			<div class="col-xs-6 col-sm-6 notification-wrapper">
				<input name="channels[${fn:escapeXml(loop.index)}].channel" value="${fn:escapeXml(channel.channel)}" type="text" class="display-none"/>
				<input id="channel${fn:escapeXml(loop.index)}" type="checkbox" name="channels[${fn:escapeXml(loop.index)}].enabled" value="${fn:escapeXml(channel.enabled)}" 
					${channel.enabled eq true? 'checked="true"' : ''} class="notification-channel-checkbox"/>
			</div>
		</div>
	</div>
	</c:if>
</c:forEach>

<div class="row">
	<div class="col-sm-6 col-sm-push-6 notification-btns">
		<div class="accountActions">
			<button id="addInterestbtn" type="button" class="btn btn-primary btn-block">
				<c:choose>
				<c:when test="${not empty removeUrl}">
				<spring:theme code="stocknotification.submit.update" />
				</c:when>
				<c:otherwise>
					<spring:theme code="stocknotification.submit.new"/>
				</c:otherwise>
				</c:choose>
			</button>
		</div>
	</div>
	<div class="col-sm-6 col-sm-pull-6 notification-btns">
		<div class="accountActions">
				<button id="cancelInterestbtn" type="button" class="btn btn-default btn-block backToHome">
					<spring:theme code="stocknotification.cancel" />
				</button>
		</div>
	</div>
</div>
</form:form>
<script type="text/javascript">
	$("div").remove("#cboxLoadingGraphic");
	$("div").remove("#cboxLoadingOverlay");
	
	$( document ).ready(checkSelectChannel);	
	$(".notification-channel-checkbox").click(function(){
		this.value = this.checked;
		checkSelectChannel();
	});

	$("#cancelInterestbtn").click(function() { 
		ACC.colorbox.close();
	});

	$("#addInterestbtn").click(function() {
		var checked = $(".notification-channel-checkbox:checked").length;
		if (checked == 0) {
			removeInterest();
		} 
		else {
			addInterest();
		}

	});
	function removeInterest() {
		var rmUrl = ACC.config.encodedContextPath +  "${removeUrl}";
		$.get(rmUrl, function(data) {
			if (data == "success") {
				ACC.colorbox.close();
				var myInterestUrl=ACC.config.encodedContextPath+"/my-account/my-interests";
				$(location).attr('href', myInterestUrl);
			} else {
				$("#RemoveFail").removeClass('display-none').toggleClass('alert-info alert-dismissable');
			}
		});
	}

	function addInterest() {

		var addInterestUrl = ACC.config.encodedContextPath + "${action}";

		$.ajax({
			type : "POST",
			url : addInterestUrl,
			async : true,
			data : $("#stockNotificationForm").serialize(),
			dataType : "text"
		}).done(function(data) {
			if (data == "success") {
				ACC.colorbox.close();
				location.reload();
			}  
			else {
				$("#AddFail").removeClass('display-none').toggleClass('alert-info alert-dismissable');
			}
		})
	}
	
	function checkSelectChannel(){
		var checked = $(".notification-channel-checkbox:checked").length;
		if (checked == 0) {
			$("#addInterestbtn").text("<spring:theme code="stocknotification.remove"/>");
		}
		else{
			$("#addInterestbtn").text("<spring:theme code="stocknotification.submit.update"/>");
		}
	}
</script>

