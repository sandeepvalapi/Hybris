<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
        ~ /*
        ~  * [y] hybris Platform
        ~  *
        ~  * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
        ~  *
        ~  * This software is the confidential and proprietary information of SAP
        ~  * ("Confidential Information"). You shall not disclose such Confidential
        ~  * Information and shall use it only in accordance with the terms of the
        ~  * license agreement you entered into with SAP.
        ~ */
--%>

<div class="bundle-dashboard">
    <ul>
        <c:forEach var="leaf" items="${bundleNavigation}">
            <c:choose>
                <c:when test="${leaf.groupNumber eq entryGroupNumber}">
                    <li class="bundle-dashboard active-item">${fn:escapeXml(leaf.label)}</li>
                </c:when>
                <c:otherwise>
                    <spring:url value="/entrygroups/CONFIGURABLEBUNDLE/{/entryGroupNumber}" var="bundleComponentUrl"
                                htmlEscape="false">
                        <spring:param name="entryGroupNumber" value="${leaf.groupNumber}"/>
                    </spring:url>
                    <li class="bundle-dashboard"><a href="${bundleComponentUrl}">${fn:escapeXml(leaf.label)}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</div>
