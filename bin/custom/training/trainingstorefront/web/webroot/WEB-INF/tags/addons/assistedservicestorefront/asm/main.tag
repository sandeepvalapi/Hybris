<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="asm" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/asm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<%-- JS configuration --%>
<script type="text/javascript">
    /*<![CDATA[*/
    var ASM_MESSAGES = {};
    ASM_MESSAGES.customerIdNotFound = '<spring:theme code="asm.emulate.customer.not.found" />';
    /*]]>*/
</script>
<div id="_asm" <c:if test="${not empty asm_message}"> style="display: block;"</c:if>>
    <script>
    	var body = document.getElementsByTagName("body")[0];
    	body.insertBefore(document.getElementById('_asm'), body.childNodes[0]);
    </script>
	<div class="ASM_header navigation navigation--top">
        <div class="first-top-row row">
            <asm:redirect />
                <button class="ASM_close ASM_close_all closeBtn" type="button">&times;</button>
            <c:if test="${not empty asm_message}">
                <div class="ASM_alert ${asm_alert_class}"><spring:theme code="${asm_message}" arguments="${asm_message_args}"/></div>
            </c:if>

            <cms:component uid="ASMHeaderComponent" />

            <%-- logo text --%>
            <asm:logo />
            <c:choose>
                <c:when test="${not empty agent.uid}">
                    <div id="_asmLogged" class="ASM_loggedin">
                        <cms:component uid="ASMCustomerListComponent"/>
                        <div class="ASM_timer element-separator-height">
                            <span id="sessionTimer" class="ASM_loggedin_text_name"><span class="ASM_loggedin_text_label hidden-xs hidden-sm hidden-md"><spring:theme code="asm.emulate.timer"/> </span><span class='ASM_timer_count' id="timerCount"><script>document.getElementById('timerCount').innerHTML=Math.floor(${agentTimer}/60)+":00";</script></span> <spring:theme code="asm.emulate.timer.min"/></span>
                            <button type="submit" id="resetButton" class="ASM-btn ASM-btn-reset" disabled><spring:theme code="asm.emulate.reset"/></button>
                            <script>var timer=${agentTimer};</script>
                        </div>

                        <div class="ASM_loggedin_text element-separator-height">
                            <span class="ASM_loggedin_agent_store_name">
                                <p>
                                    <span class="ASM_loggedin_text_label hidden-xs hidden-sm hidden-md"><spring:theme code="asm.login.logged"/> </span>
                                    <span class="ASM_loggedin_text_name"><c:out value="${agent.name}"/></span>
                                </p>
                                <c:if test="${not empty asm_agent_store}">
                                    <p>
                                        <span class="ASM_loggedin_text_label hidden-xs hidden-sm hidden-md"><spring:theme code="asm.emulate.at"/> </span>
                                        <span class="ASM_loggedin_text_name"><spring:theme code="${asm_agent_store}"/></span>
                                    </p>
                                </c:if>
                            </span>
                            <c:url value="/assisted-service/logoutasm" var="logoutActionUrl" />
                            <form action="${logoutActionUrl}" method="post" id="asmLogoutForm" class="asmForm">
                                <fieldset>
                                    <button type="submit" class="ASM-btn ASM-btn-logout" disabled><spring:theme code="asm.logout"/></button>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </div>
        <div class="row">
            <c:if test="${empty agent.uid}">
                <cms:component uid="ASMLoginComponent"/>
            </c:if>
            <c:if test="${not empty agent.uid}">
                <div class="ASM_session">
                    <div id="_asmCustomer">
                        <c:choose>
                            <c:when test="${empty emulatedUser}">
                                <cms:component uid="ASMEmulateUserComponent"/>
                            </c:when>
                            <c:otherwise>
                                <cms:component uid="ASMBindComponent" />
                                <div class="col-xs-12 col-sm-2 col-md-3">
                                    <button class="ASM-btn ASM-btn-customer360 js-customer360" disabled>
                                        <span class="ASM-customer360_icon"></span>
                                        <span class="hidden-sm"><spring:theme code="text.customer360.buttonLabelCustomer"/></span>
                                        <spring:theme code="text.customer360.buttonLabelView"/>
                                    </button>
                                </div>
                                <span class="ASM_end_session col-xs-12 col-sm-2 col-md-3">
                                    <c:url value="/assisted-service/personify-stop" var="sessionEndActionUrl" />
                                    <form action="${sessionEndActionUrl}" method="post" id="_asmSessionEndForm" class="asmForm">
                                        <fieldset>
                                            <button type="submit" id="stopEmulate" class="ASM-btn ASM-btn-end-session" disabled>
                                                <spring:theme code="asm.emulate.end"/>
                                                <span class="hidden-sm">&nbsp;<spring:theme code="asm.emulate.end.ending"/></span>
                                            </button>
                                        </fieldset>
                                    </form>
                                </span>
                                <input type="hidden" id="enable360View" value="${enable360View}">
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${not createDisabled}">
                            <asm:createcustomerform/>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
    
    <cms:component uid="ASMFooterComponent" />
    
    <div id="asmAutoComplete" class="asmAutoComplete"></div>
    <div id="asmAutoCompleteCartId" class="asmAutoComplete"></div>
    <div class="collapseBtn-wrapper js-ASM-collapseBtn-wrapper">
        <button class="ASM-btn collapseBtn js-ASM-collapseBtn">
            <svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 1000 1000" enable-background="new 0 0 1000 1000" xml:space="preserve">
            <g><g transform="translate(0.000000,119.000000) scale(0.100000,-0.100000)"><path d="M2675.9-1189.9L404.9-3460.9l503.7-503.7l503.7-503.7l1793.9,1829.2L5000-809.9l1793.8-1829.2l1793.8-1829.2l503.7,503.7l503.7,503.7L7315.3-1181.1C6069.2,64.9,5026.5,1090,5000,1090S3930.7,64.9,2675.9-1189.9z"/><path d="M2675.9-5431.5L404.9-7702.6l503.7-503.7l503.7-503.7l1793.9,1829.2L5000-5051.5l1793.8-1829.2L8587.7-8710l503.7,503.7l503.7,503.7L7315.3-5422.7c-1246,1246-2288.8,2271.1-2315.3,2271.1S3930.7-4176.7,2675.9-5431.5z"/></g></g>
            </svg>
        </button>
    </div>
</div>