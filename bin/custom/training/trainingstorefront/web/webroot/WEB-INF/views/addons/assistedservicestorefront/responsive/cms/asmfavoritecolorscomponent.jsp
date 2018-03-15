<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="canvasId" value="customerFavoriteColorCanvas"/>

<h3><spring:theme code="text.customer360.favorite.colors" text="Favorite Colors"/></h3>
<div class="row overview-tab-charts">
    <div class="col-sm-12">
        <canvas id="${canvasId}" width="auto" height="200"></canvas>
        <script type="application/javascript">
            var labels = [];
            var codes = [];
            var values = [];
            <c:forEach items="${favoriteColorsDatas}" var="color">
                ACC.assistedservicestorefront.buildArrayValues(labels, '${color.colorName}');
                ACC.assistedservicestorefront.buildArrayValues(codes, '${color.colorCode}');
                ACC.assistedservicestorefront.buildArrayValues(values, '${color.value}');
            </c:forEach>
            ACC.assistedservicestorefront.drawPolarAreaChart("${canvasId}", labels, codes, values);
        </script>
    </div>
</div>
