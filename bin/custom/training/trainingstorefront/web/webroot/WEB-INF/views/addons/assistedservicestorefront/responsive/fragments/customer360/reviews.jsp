<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="pageable-fragment asm-customer360-tab">
  <div class="asm-customer360-reviews-tab">
  <h3><spring:theme code="text.customer360.feedback.review" text="Product Reviews"/></h3>
  <c:choose>
    <c:when test="${empty fragmentData}">
        <div class="clear">
            <spring:theme
                 code="text.customer360.feedback.review.no"
                 text="There are currently no Product Review items" />
       </div>
    </c:when>
	<c:otherwise>
        <div class="pagerReview pager hidden-xs"></div>
        <table id="asm-customer360-reviews-table" class="table techne-table">
            <thead>
                <tr class="responsive-table-head hidden-xs">
                    <th class="pointer"><spring:theme code="text.customer360.review.item" /></th>
                    <th class="headerSortUp pointer"><spring:theme code="text.customer360.review.date_status" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.review.stars" /></th>
                    <th class="pointer"><spring:theme code="text.customer360.review.review" /></th>
                </tr>
            </thead>

            <tbody>
                <c:forEach items="${fragmentData}" var="reviewData">
                    <c:url value="${reviewData.productUrl}" var="productUrl" />
                    <tr>
                        <td data-th="Item"><a title="<c:out value='${reviewData.productName}'/>" href="${productUrl}" class="responsive-table-link text-nowrap"><c:out value="${reviewData.productName}, SKU: ${reviewData.SKUNumber}" /></a></td>
                        <td data-th="Date / Status" class="break-word"><span><fmt:formatDate value="${reviewData.created}" pattern="dd-MM-YYYY" />&nbsp;</span>/&nbsp;<spring:theme code="${reviewData.reviewStatus}" htmlEscape="true"/> </td>
                        <td data-th="Rate" data-text="${reviewData.rating}" style="min-width: 130px;">
                            <div class="rating">
                                <div class="rating-stars pull-left js-ratingCalc " data-rating='{"rating":"${reviewData.rating}","total":5}' >
                                    <div class="greyStars">
                                        <c:forEach  begin="1" end="5">
                                            <span class="glyphicon glyphicon-star"></span>
                                        </c:forEach>
                                    </div>
                                    <div class="greenStars js-greenStars">
                                        <c:forEach  begin="1" end="5">
                                            <span class="glyphicon glyphicon-star active"></span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td data-th="Review" title="<spring:theme code='${reviewData.reviewText}' htmlEscape='true'/>">
                            <div class="asm-customer360-table-onLineCell">
                                <spring:theme code="${reviewData.reviewText}" htmlEscape="true"/>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="pagerReview pager visible-xs"></div>
        <script>
            ACC.ratingstars.bindRatingStars();
            addRatesTableSorterParser();
            $("#asm-customer360-reviews-table").tablesorter({headers: { 0: { sorter: "text"}, 1: {sorter: "text"},  2: { sorter: "rates"}, 3: {sorter: "text"} }})
                .tablesorterPager({container: $(".asm-customer360-reviews-tab .pagerReview"), size: getAifTablePageSize()});
        </script>
    </c:otherwise>
	</c:choose>
    </div>
</div>