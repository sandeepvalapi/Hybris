<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<template:page pageTitle="${pageTitle}">
	
	<c:url value="/" var="homePageUrl" />


	<cms:pageSlot position="MiddleContent" var="comp" element="div" class="errorNotFoundPageMiddle">
		<cms:component component="${comp}" element="div" class="errorNotFoundPageMiddle-component" />
	</cms:pageSlot>
	<cms:pageSlot position="BottomContent" var="comp" element="div" class="errorNotFoundPageBottom">
		<cms:component component="${comp}" element="div" class="errorNotFoundPageBottom-component"/>
	</cms:pageSlot>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="errorNotFoundPageSide">
		<cms:component component="${feature}" element="div" class="errorNotFoundPageSide-component"/>
	</cms:pageSlot>
	
	<div class="error-page">
		<a class="btn btn-default js-shopping-button" href="${homePageUrl}">
			<spring:theme text="Continue Shopping" code="general.continue.shopping"/>
		</a>
	</div>

</template:page>