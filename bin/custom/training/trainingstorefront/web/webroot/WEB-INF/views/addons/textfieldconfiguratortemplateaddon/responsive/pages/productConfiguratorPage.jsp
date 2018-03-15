<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/textfieldconfiguratortemplateaddon/responsive/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="/p/${ycommerce:encodeUrl(product.code)}/configure/TEXTFIELD" var="addToCartUrl"/>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}">
    <jsp:body>
        <form:form method="post" id="textFieldConfigurationForm" action="${addToCartUrl}">
            <input id="quantity" name="quantity" type="hidden" value="${qty}" hidden="hidden">
            <product:productConfiguratorTab configurations="${configurations}"/>
            <cms:pageSlot position="Section1" var="comp" element="div" class="productConfiguratorPageSection1">
                <cms:component component="${comp}"/>
            </cms:pageSlot>
            <div class="row">
                <div class="col-sm-12 col-md-6">
                    <button id="addToCartButton" type="${buttonType}" class="btn btn-primary btn-block js-add-to-cart js-enable-btn btn-icon glyphicon-shopping-cart" disabled="disabled">
                        <spring:theme code="basket.add.to.basket"/>
                    </button>
                </div>
            </div>
        </form:form>
    </jsp:body>
</template:page>
