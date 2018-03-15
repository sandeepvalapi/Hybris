<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/addons/orderselfserviceaddon/responsive/return" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="well well-tertiary well-lg">
    <ycommerce:testId code="return_overview_section">
        <return:accountReturnOverview returnRequest="${returnRequestData}"/>
    </ycommerce:testId>
</div>
<br/>
<ycommerce:testId code="return_entries_section">
    <return:accountReturnEntriesOverview returnRequest="${returnRequestData}"/>
</ycommerce:testId>
<br/>
<ycommerce:testId code="return_entries_section">
    <return:accountReturnTotals returnRequest="${returnRequestData}"/>
</ycommerce:testId>
<br/>
<ycommerce:testId code="return_actions_section">
    <return:accountReturnActions returnRequest="${returnRequestData}"/>
</ycommerce:testId>

