<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/textfieldconfiguratortemplateaddon/responsive/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="/cart/${ycommerce:encodeUrl(entryNumber)}/configuration/TEXTFIELD" var="cartPageUrl"/>
<template:page pageTitle="${pageTitle}">
    <jsp:body>
        <form:form method="post" id="textFieldConfigurationForm" action="${cartPageUrl}">
            <input id="quantity" name="quantity" type="hidden" value="1" hidden="hidden">
            <product:productConfiguratorTab configurations="${configurations}"/>
            <div class="config-action">
                <div class="row">
                    <div class="col-sm-12 col-md-6">
                        <button id="update" type="submit" class="btn btn-primary btn-block">
                            <spring:theme code="configuration.page.update"/>
                        </button>
                    </div>
                </div>
            </div>
        </form:form>
    </jsp:body>
</template:page>
