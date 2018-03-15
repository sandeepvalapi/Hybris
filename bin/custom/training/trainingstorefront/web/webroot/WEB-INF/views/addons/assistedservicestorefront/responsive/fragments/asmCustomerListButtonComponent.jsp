<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/assisted-service-querying/availableCustomerLists" var="listCustomersActionUrl" />

<a href="#"
       class="ASM-btn ASM-btn-customer-list js-customer-list-btn element-separator-height disabled"
       id="product_1"
       data-actionurl="${listCustomersActionUrl}">
        <span class="ASM_icon ASM_icon-customer-list"></span>
        <span class="hidden-sm ASM-btn ASM-btn-customers"><spring:theme code="text.asm.customerList.component" /></span>
</a>
<div class="hidden js-customer-list-modal-content-wrap"><div class="js-customer-list-modal-content"></div></div>