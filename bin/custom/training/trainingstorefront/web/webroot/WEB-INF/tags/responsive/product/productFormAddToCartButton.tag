<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="addToCartBtnId" required="false" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<button  id="${(empty addToCartBtnId) ? 'addToCartBtn' : addToCartBtnId}" type="button" class="btn btn-primary" disabled="disabled">
    <spring:theme code="text.addToCart" var="addToCartText"/>
    <spring:theme code="basket.add.to.basket" />
</button>







  