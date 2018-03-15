<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
    <title>ImpEx Import</title>
    <link rel="stylesheet" href="<c:url value="/static/css/console/impexCommon.css"/>" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="<c:url value="/static/css/codemirror3/codemirror.css"/>" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-custom.css"/>" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-impex.css"/>" type="text/css"
          media="screen, projection"/>
    <link rel="stylesheet" href="<c:url value="/static/css/console/show-hint.css"/>" type="text/css"
          media="screen, projection"/>

    <script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/codemirror3/codemirror.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/codemirror3/impexhighlight.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/codemirror3/cmfullscreen.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/codemirror3/show-hint.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/codemirror3/impex-hint.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/console/impex.js"/>"></script>
</head>

<body>
<div class="prepend-top span-17 colborder" id="content"
     typeAttr-Url="<c:url value="/console/impex/typeAndAttributes" />"
     allTypes-Url="<c:url value="/console/impex/allTypes" />">
<button id="toggleSidebarButton">&gt;</button>
<div class="marginLeft">
<h2>ImpEx Import</h2>

<div id="tabsNoSidebar">
    <ul>
        <li><a href="#tabs-1">Import content</a>
        </li>
        <li><a href="#tabs-2">Import script</a>
        </li>
    </ul>
    <div id="tabs-1">
        <c:set var="contentImportUrl">
            <c:url value="/console/impex/import"/>
        </c:set>
        <c:set var="contentValidateUrl">
            <c:url value="/console/impex/import/validate"/>
        </c:set>
        <form:form method="post" id="contentForm" action="${contentImportUrl}" data-validateUrl="${contentValidateUrl}"
                   commandName="impexImportContent">
            <fieldset>
                <legend>
                    <spring:message code="console.impex.forms.legends.importContent"/>
                </legend>

                <span><form:errors path="scriptContent" cssClass="error"/></span>

                <div id="textarea-container">
                    <textarea id="script" form="contentForm" name="scriptContent">${script}</textarea>
                </div>

                <button id="clearScriptContent">Clear content</button>
                <span><form:errors path="encoding" cssClass="error"/></span>
                <span><form:errors path="maxThreads" cssClass="error"/></span>
                <span><form:errors path="validationEnum" cssClass="error"/></span>

                <p>
                    <input type="submit" value="<spring:message code="console.impex.forms.buttons.importContent" />"/>
                    <input id="validate" type="submit"
                           value="<spring:message code="console.impex.forms.buttons.validateContent" />"/>
                </p>

                <div id="settings" style="width:630px">
                    <h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Settings</h3>

                    <div>
                        <table style="table-layout:fixed;width:575px;">
                            <tr>
                                <td>
                                    <spring:message code="console.impex.forms.labels.validationEnum"/>
                                </td>
                                <td>
                                    <form:select path="validationEnum">
                                        <form:options items="${importValidationModes}" itemLabel="code"/>
                                    </form:select>
                                </td>
                                <td>
                                    <spring:message code="console.impex.forms.labels.maxThread"/>
                                </td>
                                <td>
                                    <spring:bind path="maxThreads">
                                        <input type="number" min="1" id="maxThreads" name="maxThreads"
                                               style="width:50px" value="${impexImportContent.maxThreads}"/>
                                    </spring:bind>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <spring:message code="console.impex.forms.labels.encoding"/>
                                </td>
                                <td>
                                    <form:input path="encoding" size="4"/>
                                </td>
                                <td>
                                    Legacy mode
                                </td>
                                <td>
                                    <form:checkbox path="legacyMode"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Enable code execution
                                </td>
                                <td>
                                    <form:checkbox path="enableCodeExecution"/>
                                </td>
                                <td>
                                    <label>Syntax Highlighting</label>
                                </td>
                                <td>
                                    <input type="checkbox" checked="checked" id="syntaxHighlighting"/>
                                    <input type="hidden"
                                           name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Distributed mode
                                </td>
                                <td>
                                    <form:checkbox path="distributedMode"/>
                                </td>
                                <td>
                                    Direct persistence
                                </td>
                                <td>
                                    <form:checkbox path="sldEnabled"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </fieldset>
        </form:form>
    </div>
    <div id="tabs-2">
        <c:set var="fileImportUrl">
            <c:url value="/console/impex/import/upload"/>
        </c:set>
        <form:form method="post" action="${fileImportUrl}?${_csrf.parameterName}=${_csrf.token}" commandName="impexImportFile" enctype="multipart/form-data">
            <fieldset>
                <legend>
                    <spring:message code="console.impex.forms.legends.importScript"/>
                </legend>
                <div>
                    <span><form:errors path="file" cssClass="error"/></span><br/>
                    <spring:bind path="file">
                        <hac:input-file name="file"/>
                    </spring:bind>
                </div>
                <table>
                    <tr>
                        <td>
                            <spring:message code="console.impex.forms.labels.encoding"/>
                        </td>
                        <td>
                            <form:input path="encoding" size="4" id="fileEncoding"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <spring:message code="console.impex.forms.labels.maxThread"/>
                        </td>
                        <td>
                            <spring:bind path="maxThreads">
                                <input type="number" min="1" id="maxThreads" name="maxThreads"
                                       value="${impexImportFile.maxThreads}"/>
                            </spring:bind>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <spring:message code="console.impex.forms.labels.validationEnum"/>
                        </td>
                        <td>
                            <form:select path="validationEnum" id="fileValidationEnum">
                                <form:options items="${importValidationModes}" itemLabel="code"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Legacy mode
                        </td>
                        <td>
                            <form:checkbox path="legacyMode"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Enable code execution
                        </td>
                        <td>
                            <form:checkbox path="enableCodeExecution"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Distributed mode
                        </td>
                        <td>
                            <form:checkbox path="distributedMode"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Direct persistence
                        </td>
                        <td>
                            <form:checkbox path="sldEnabled"/>
                        </td>
                    </tr>
                </table>

                <span><form:errors id="encoding" path="encoding" cssClass="error"/></span>
                <span><form:errors path="validationEnum" cssClass="error"/></span>
                <span><form:errors path="maxThreads" cssClass="error"/></span>
                <input type="submit" value="<spring:message code="console.impex.forms.buttons.importFile" />"/>
            </fieldset>
        </form:form>
    </div>
</div>
<div style="clear:both;margin-bottom:10px"></div>
<!-- result -->

<c:choose>
    <c:when test="${not empty importResult}">
        <c:choose>
            <c:when test="${ importResult.successs eq true }">
							<span id="impexResult" data-level="notice"
                                  data-result="<spring:message code="console.impex.result.import.successfull" />">
							</span>
                <c:if test="${! empty importResult.logText }">
                    <div class="box impexResult quiet">
									<pre>
										<c:out value="${importResult.logText}"/>
									</pre>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
							<span id="impexResult" data-level="error"
                                  data-result="<spring:message code="console.impex.result.import.unsuccessfull" />">
							</span>

                <div class="box impexResult quiet">
								<pre>
									<c:out value="${importResult.unresolvedData}"/>
								</pre>
                </div>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when test="${not empty validationResult}">
        <div id="validationResult" class="marginBottom">
            <c:choose>
            <c:when test="${validationResult.valid eq true}">
								<span id="validationResultMsg" data-level="notice"
                                      data-result="<spring:message code="console.impex.result.validation.import.valid" />">
							</c:when>
							<c:otherwise>
								<span id="validationResultMsg" data-level="error"
                                      data-result="<spring:message code="console.impex.result.validation.import.invalid" arguments="${validationResult.message}"/>">
							</c:otherwise>
						</c:choose>
        </div>
    </c:when>
</c:choose>
</div>
</div>
<div class="span-6 last" id="sidebar">
    <div class="prepend-top" id="recent-reviews">
        <h3 class="caps">
            Page description
        </h3>

        <div class="box">
            <div class="quiet">
                This page provides ImpEx import functionality. You can import a script file or paste a script and
                validate it before the import.
                <hr/>
                <hac:note>
                    <strong>Legacy mode</strong><br>
                    Impex import works on Service Layer. If you select this option, then Jalo Layer is used.
                </hac:note>
                </br>
                <hac:info>
                    <strong>Fullscreen mode</strong></br>
                    Press <b>F11</b> when cursor is in the editor to toggle full screen editing.
                    <b>Esc</b> can also be used to exit full screen editing.
                </hac:info>
            </div>
        </div>
        <h3 class="caps">
            See also in the hybris Wiki
        </h3>

        <div class="box">
            <div class="quiet">
                <ul>
                    <li><a href="${wikiImpex}" target="_blank" class="quiet">impex Extension - Technical Guide</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div id="tooltip"></div>
</body>
</html>
