<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<div class="image-gallery js-gallery">
    <span class="image-gallery__zoom-icon glyphicon glyphicon-resize-full"></span>

    <c:choose>
        <c:when test="${galleryImages == null || galleryImages.size() == 0}">
            <div class="carousel image-gallery__image js-gallery-image">
                <div class="item">
                    <div>
                        <spring:theme code="img.missingProductImage.responsive.product" text="/" var="imagePath"/>
                        <c:choose>
                            <c:when test="${originalContextPath ne null}">
                                <c:url value="${imagePath}" var="imageUrl" context="${originalContextPath}"/>
                            </c:when>
                            <c:otherwise>
                                <c:url value="${imagePath}" var="imageUrl" />
                            </c:otherwise>
                        </c:choose>
                        <img class="lazyOwl" data-src="${imageUrl}"/>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>

            <div class="carousel image-gallery__image js-gallery-image">
                <c:forEach items="${galleryImages}" var="container" varStatus="varStatus">
                    <div class="item">
                        <div>
                            <img class="lazyOwl" data-src="${container.product.url}"
                                 data-zoom-image="${container.superZoom.url}"
                                 alt="${fn:escapeXml(container.thumbnail.altText)}" >
                        </div>
                    </div>
                </c:forEach>
            </div>
            <product:productGalleryThumbnail galleryImages="${galleryImages}" />
        </c:otherwise>
    </c:choose>
</div>
