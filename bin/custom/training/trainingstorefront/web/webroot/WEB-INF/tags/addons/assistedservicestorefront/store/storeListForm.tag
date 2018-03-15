<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<c:url value="/store-finder" var="storeFinderFormAction" />
<div class="pickup-component asm_store__finder js-pickup-component">
    
    <div class="store__finder js-asm-store-finder" data-url="${storeFinderFormAction}">
        <div class="row">
            <ycommerce:testId code="storeFinder">
                <div class="col-lg-12">
                    <div class="asm_store__finder--pagination">
                        <div class="pull-right">
                            <button class="btn btn-link js-asm-store-finder-pager-prev" type="button">
                                <span aria-hidden="true"><spring:theme code="text.account.customerList.page.firstPage"/></span>
                            </button>
                            <button class="btn btn-link js-asm-store-finder-pager-next" type="button">
                                <span aria-hidden="true"><spring:theme code="text.account.customerList.page.lastPage"/></span>
                            </button>
                        </div>
                        <div class="asm_store__finder--pagination-label">
                            <span class="js-asm-store-finder-pager-item-from"></span>-
                            <span class="js-asm-store-finder-pager-item-to"></span>
                            <spring:theme code="storeFinder.pagination.from" text="from"></spring:theme>
                            <span class="js-asm-store-finder-pager-item-all"></span>
                            <spring:theme code="storeFinder.pagination.stores" text="stores found"></spring:theme>
                        </div>
                    </div>
    
                    <div class="store__finder--panel clearfix">
                        <div class="store__finder--navigation">
                            <ul class="store__finder--navigation-list js-asm-store-finder-navigation-list">
                                <li class="loading"><span class="glyphicon glyphicon-repeat"></span></li>
                            </ul>
                        </div>
    
                        <div class="store__finder--details js-asm-store-finder-details">
                            <div>
                                <button class="btn btn-default store__finder--details-back js-asm-store-finder-details-back">
                                    <span class="glyphicon glyphicon-chevron-left"></span>
                                    <spring:theme code="pickup.in.store.back.to.results" text="Back"></spring:theme>
                                </button>
                            </div>
                            <div class="store__finder--details-image js-asm-store-image"></div>
                            <div class="store__finder--details-info">
                                <div class="info__name js-asm-store-name"></div>
                                <div class="info__address">
                                    <div class="js-asm-store-line1"></div>
                                    <div class="js-asm-store-line2"></div>
                                    <div class="js-asm-store-town"></div>
                                </div>
                            </div>
    
                            <hr>
                                <div class="store__finder--map js-asm-store-finder-map"></div>
                            <hr>
    
                            <div class="store__finder--details-openings">
                                <dl class="dl-horizontal js-asm-store-openings"></dl>
                                <div class="openings__title"><spring:theme code="storeDetails.table.features" /></div>
                                <ul class="js-asm-store-features"></ul>
                            </div>
                        </div>
                    </div>
    
                    <div class="asm_store__finder--pagination">
                        <div class="pull-right">
                             <button class="btn btn-link js-asm-store-finder-pager-prev" type="button">
                                <span aria-hidden="true"><spring:theme code="text.account.customerList.page.firstPage"/></span>
                            </button>
                            <button class="btn btn-link js-asm-store-finder-pager-next" type="button">
                                <span aria-hidden="true"><spring:theme code="text.account.customerList.page.lastPage"/></span>
                            </button>
                        </div>

                        <div class="asm_store__finder--pagination-label">
                            <span class="js-asm-store-finder-pager-item-from"></span>-
                            <span class="js-asm-store-finder-pager-item-to"></span>
                            <spring:theme code="storeFinder.pagination.from" text="from"></spring:theme>
                            <span class="js-asm-store-finder-pager-item-all"></span>
                            <spring:theme code="storeFinder.pagination.stores" text="stores found"></spring:theme>
                        </div>
                    </div>
                </div>
            </ycommerce:testId>
        </div>
    </div>
</div>