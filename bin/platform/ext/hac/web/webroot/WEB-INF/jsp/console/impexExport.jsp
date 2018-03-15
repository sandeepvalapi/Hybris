<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>ImpEx Export</title>
	<link rel="stylesheet" href="<c:url value="/static/css/console/impexCommon.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/codemirror3/codemirror.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-custom.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-impex.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/show-hint.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/codemirror.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/impexhighlight.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/cmfullscreen.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/show-hint.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/impex-hint.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/console/impex.js"/>"></script>
</head>

<body>
	<div class="prepend-top span-17 colborder" id="content" typeAttr-Url="<c:url value="/console/impex/typeAndAttributes" />" allTypes-Url="<c:url value="/console/impex/allTypes" />" >
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft">
			<h2>ImpEx Export</h2>
			
			<div id="tabsNoSidebar">
				<ul>
					<li><a href="#tabs-1">Export content</a>
					</li>
					<li><a href="#tabs-2">Export script</a>
					</li>
				</ul>
				<div id="tabs-1">
					<c:set var="contentExportUrl">
						<c:url value="/console/impex/export" />
					</c:set>	
					<c:set var="contentValidateUrl">
						<c:url value="/console/impex/export/validate" />
					</c:set>	
					
					<form:form id="contentForm" method="post" action="${contentExportUrl}" data-validateUrl="${contentValidateUrl}" commandName="impexExportContent">
						<fieldset style="width:99%">
							<legend>
								<spring:message code="console.impex.forms.legends.exportContent" />
							</legend>
							
							<span><form:errors path="scriptContent" cssClass="error" /></span>
							
							<div id="textarea-container">
								<textarea id="script" form="contentForm" name="scriptContent">${script}</textarea>
							</div>
								
							<button id="clearScriptContent">Clear content</button>
							<span><form:errors path="validationEnum" cssClass="error" /></span>
							<span><form:errors path="encoding" cssClass="error" /></span>
							<p>							
								<input type="submit" value="<spring:message code="console.impex.forms.buttons.exportContent" />" />
								<input id="validate" type="submit" value="<spring:message code="console.impex.forms.buttons.validateContent" />" />
							</p>

							<div id="settings" style="width:630px">
								<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Settings</h3>
								<div>
									<table>
										<tr>
											<td>
												<spring:message code="console.impex.forms.labels.validationEnum" />
											</td>
											<td>
												<form:select path="validationEnum">
													<form:options items="${exportValidationModes}" itemLabel="code" />
												</form:select>
											</td>
										</tr>
										<tr>
											<td>
												<spring:message code="console.impex.forms.labels.encoding" />
											</td>
											<td>
												<form:input path="encoding" size="4" id="contentEncoding"/>
											</td>
										</tr>
										<tr>
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
									</table>
								</div>
							</div>
						</fieldset>
					</form:form>				
				</div>
				<div id="tabs-2">
					<c:set var="fileExportUrl">
						<c:url value="/console/impex/export/upload" />
					</c:set>	
					<form:form method="post" action="${fileExportUrl}?${_csrf.parameterName}=${_csrf.token}" commandName="impexExportFile" enctype="multipart/form-data">
						<fieldset>
							<legend>
								<spring:message code="console.impex.forms.legends.exportScript" />
							</legend>
							<div>
								<span><form:errors path="file" cssClass="error" /></span>
								<spring:bind path="file">
									<hac:input-file name="file"/>
								</spring:bind>
								<form:label path="encoding">
									<spring:message code="console.impex.forms.labels.encoding" />
								</form:label>
								<span><form:errors path="encoding" cssClass="error" /></span>
								<form:input path="encoding" size="4" />						
								<input type="submit" value="<spring:message code="console.impex.forms.buttons.exportFile" />" />								
							</div>
						</fieldset>
					</form:form>					
				</div>			
			</div>
			<div style="clear:both;margin-bottom:10px"></div>
			<!-- result -->
			<c:choose>
				<c:when test="${not empty exportResult}">
					<c:choose>
						<c:when test="${ exportResult.success eq true }">
							<span id="impexResult" data-level="notice" data-result="<spring:message code="console.impex.result.export.successfull" />" >
							</span>
							<div id="downloadExportResultData" class="marginBottom box">
								<spring:message code="console.impex.result.export.download"></spring:message>: <a href="${exportResult.downloadUrl}">${exportResult.exportDataName}</a>
							</div>
						</c:when>
						<c:otherwise>
							<span id="impexResult" data-level="error" data-result="<spring:message code="console.impex.result.export.unsuccessfull" />" >
							</span>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${not empty validationResult}">
					<div id="validationResult" class="marginBottom">
						<c:choose>
							<c:when test="${validationResult.valid eq true}">
								<span id="validationResultMsg" data-level="notice" data-result="<spring:message code="console.impex.result.validation.export.valid" />" >
							</c:when>
							<c:otherwise>
								<span id="validationResultMsg" data-level="error" data-result="<spring:message code="console.impex.result.validation.export.invalid" />" >
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
					This page provides ImpEx export functionality. You can export a script file or paste a script and validate it before the export.
				</div>
				<hr />
				<hac:info>
					<strong>Fullscreen mode</strong></br>
					Press <b>F11</b> when cursor is in the editor to toggle full screen editing. <b>Esc</b> can also be used to exit full screen editing.
				</hac:info>
			</div>
			<h3 class="caps">
				See also in the hybris Wiki
			</h3>
			<div class="box">
				<div class="quiet">
					<ul>
			 	   	<li> <a href="${wikiImpex}" target="_blank" class="quiet">impex Extension - Technical Guide</a> </li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div id="tooltip"></div>
</body>
</html>