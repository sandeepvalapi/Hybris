<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty supportTicketForm}">
    <div id="global-alerts" class="global-alerts"></div>
    <div class="back-link border">
        <div class="row">
            <div class="container-lg col-md-6">
                <a href="support-tickets">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                </a>
                <span class="label"><spring:theme code="text.account.supporttickets" text="Support Tickets" /></span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="container-lg col-md-6">
            <div class="account-section-content">
                <div class="account-section-form">
                    <div id="customer-ticketing-alerts"></div>
                    <form:form method="post" commandName="supportTicketForm" enctype="multipart/form-data">
                        <formElement:formInputBox idKey="createTicket-subject" labelKey="text.account.supporttickets.createTicket.subject" path="subject" inputCSS="text" mandatory="true" />
                        <div id="NotEmpty-supportTicketForm-subject" class="help-block" style="display: none;">
                            <span id="subject-errors"></span>
                        </div>
                        <div id="Size-supportTicketForm-subject" class="help-block" style="display: none;"></div>
                        <formElement:formTextArea idKey="createTicket-message" labelKey="text.account.supporttickets.createTicket.message" path="message" mandatory="true" areaCSS="form-control" labelCSS="control-label"/>
                        <div id="NotEmpty-supportTicketForm-message" class="help-block" style="display: none;"></div>
                        <div id="Size-supportTicketForm-message" class="help-block" style="display: none;"></div>
                        <div class="form-group file-upload js-file-upload">
                            <label class="control-label file-upload__label" for="files"> <spring:theme code="text.account.supporttickets.createTicket.selectFile" text="Select a file"/> </label>
                            <div class="file-upload__wrapper btn btn-default btn-small">
                                <span> <spring:theme code="text.account.supporttickets.createTicket.chooseFile" text="Choose file"/> </span>
                                <input type="file" name="files" id="attachmentFiles" multiple size="60" class="file-upload__input js-file-upload__input" data-max-upload-size="${maxUploadSize}"/>
                            </div>
                            <span class="file-upload__file-name js-file-upload__file-name">
                                <spring:theme code="text.account.supporttickets.createTicket.noFileChosen"/>
                            </span>
                        </div>

                        <%--Associated Objects--%>
                        <c:if test="${not empty associatedObjects}">
                            <div class="form-group">
                            <label class="control-label" for="text.account.supporttickets.createTicket.associatedTo.option1"> <spring:theme code="text.account.supporttickets.createTicket.associatedTo" text="Associated To"/></label>

                            <form:select path="associatedTo" cssClass="form-control">
                                <option><spring:theme code="text.account.supporttickets.createTicket.associatedTo.option1" text="Please select"></spring:theme></option>
                                <c:forEach var="associatedMap" items="${associatedObjects}">
                                    <c:forEach var="associatedItem" items="${associatedMap.value}">
                                        <form:option value="${associatedMap.key}=${associatedItem.code}">
                                            <c:choose>
                                                <c:when test="${'SavedCart' eq associatedItem.type }"><spring:message code="text.account.supporttickets.createTicket.${associatedItem.type}"/>: ${associatedItem.code}; <spring:message code="text.account.supporttickets.createTicket.updated"/>: <fmt:formatDate pattern="dd/MM/yy" value="${associatedItem.modifiedtime}"/></c:when>
                                                <c:otherwise><spring:message code="text.account.supporttickets.createTicket.${associatedMap.key}"/>: ${associatedItem.code}; <spring:message code="text.account.supporttickets.createTicket.updated"/>: <fmt:formatDate pattern="dd/MM/yy" value="${associatedItem.modifiedtime}"/></c:otherwise>
                                            </c:choose>
                                        </form:option>
                                    </c:forEach>
                                </c:forEach>
                            </form:select>
                            </div>
                        </c:if>

                       <%--Ticket Categories--%>
                        <c:if test="${not empty categories}">
                            <div class="form-group">
                                <label class="control-label" for="text.account.supporttickets.createTicket.ticketCategory"> <spring:theme code="text.account.supporttickets.createTicket.ticketCategory" text="Category"/></label>

                                <form:select path="ticketCategory" cssClass="form-control">
                                    <c:forEach var="category" items="${categories}">
                                        <form:option value="${category}"><spring:message code="text.account.supporttickets.createTicket.ticketCategory.${category}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </c:if>

                        <div id="customer-ticketing-buttons" class="form-actions">
                            <div class="accountActions">
                                <div class="row">
                                    <div class="col-sm-6 col-sm-push-6 accountButtons">
                                        <ycommerce:testId code="supportTicket_create_button">
                                            <button class="btn btn-primary btn-block" type="submit" id="addTicket">
                                            <spring:theme code="text.account.supporttickets.createTicket.submit" text="Submit"/>
                                            </button>
                                        </ycommerce:testId>
                                    </div>

                                    <div class="col-sm-6 col-sm-pull-6 accountButtons">
                                        <a href="support-tickets" class="btn btn-default btn-block">
                                            <spring:theme code="text.account.supporttickets.createTicket.back" text="Cancel" />
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <div style="display: none">
        <span id="supporttickets-tryLater"><spring:theme code="text.account.supporttickets.tryLater"/></span>
        <span id="attachment-file-max-size-exceeded-error-message"><spring:theme code="text.account.supporttickets.fileMaxSizeExceeded"/></span>
        <span id="file-too-large-message"><spring:theme code="text.account.supporttickets.file.is.large.than" arguments="${maxUploadSizeMB}"/></span>
    </div>
    <common:globalMessagesTemplates/>
</c:if>