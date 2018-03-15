<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/textfieldconfiguratortemplateaddon/responsive/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/{/documentType}/{/code}" var="returnUrl" htmlEscape="false">
    <spring:param name="documentType"  value="${returnDocumentType}"/>
    <spring:param name="code"  value="${documentCode}"/>
</spring:url>
<template:page pageTitle="${pageTitle}">
    <jsp:body>
        <div class="textFieldConfigurationForm">
            <div class="product__config">
                <product:productConfiguratorTab configurations="${configurations}" readOnly="true"/>	
            </div>            				
            <div class="config-action">
                <div class="row">
                    <div class="col-sm-12 col-md-6">
                    <form action="${returnUrl}" method="get">
                        <button id="update" type="submit" class="btn btn-primary btn-block" >
                            <spring:theme code="configuration.page.return"/>
                        </button>
                     </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</template:page>
