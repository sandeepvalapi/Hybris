<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>   
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
<title><c:out value="${title}" /></title>
<link rel="stylesheet" href="<c:url value="/static/css/platform/dryrun.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/platform/dryrun.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/codemirror3/codemirror.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/codemirror3/sql.js"/>"></script>

</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		
		<h2 class="marginLeft"><span id="typeHeading">Schema initialization/update scripts</span></h2>
		
		<div class="marginLeft marginBottom" id="inner" data-updateLogUrl="<c:url value="/initlog/log"/>">
		
			<div id="generateScriptsForm" data-url="<c:url value="/platform/dryrun/execute"/>">		
					<div id="requiredForInit">
						<input type="radio" id="initialize" name="scriptsFor" ${initialization}/>
						<label for="initialize">Generate scripts for initialization</label><br/>
						
						<input type="radio" id="update" name="scriptsFor" ${update} ${notinitialized}/>
						<label for="update">Generate scripts for update</label><br/>

					</div>		
					
					<button class="buttonExecute">Generate scripts</button>
			</div>
			<div id="generatingScripts" hidden="hidden">
				<spring:message code="platform.dryrun.pleasewait" />
			</div>
			
			<div id="generatedScriptsArea" hidden="hidden">
			
			<div id="previewScriptsForm" data-url="<c:url value="/platform/dryrun/preview"/>" >		
				<dl>			
					<span id="previewDropDDLFileDiv">
						<dt><spring:message code="platform.dryrun.preview.dropDDLFile" /></dt>
						<dd><span id="previewDropDDLFile"></span><button class="preview" data-id="1"/>Preview</button></dd>
					</span>
					<dt><spring:message code="platform.dryrun.preview.DDLFile" /></dt>
					<dd><span id="previewDDLFile"></span><button class="preview" data-id="2"/>Preview</button></dd>
				
					<dt><spring:message code="platform.dryrun.preview.DMLFile" /></dt>
					<dd><span id="previewDMLFile"></span><button class="preview" data-id="3"/>Preview</button></dd>
				</dl>
			</div>
					
			<div id="textarea-container" class="border" hidden="hidden">
				<textarea id="file"></textarea>
			</div>

			<form id="downloadScriptsForm" method="GET" action="<c:url value="/platform/dryrun/download"/>">
					<button id="downloadButton">Download all scripts in zip file</button>
					
			</form>
			</div>			
		</div>
	</div>
		<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">
				Page description
			</h3>
			<div class="box">
				<div class="quiet">
					<!-- Init page sidebar -->
					This page enables you to generate SQL Scripts for:
					<ul>
						<li>schema clean up</li>
						<li>schema init/update</li>
						<li>typesystem initialization</li>
					</ul>
					<hr />
					<hac:note>
					Generating scripts DOES NOT remove any data from the database.
					</hac:note> 
				</div>
			</div>
		</div>
	</div>				
</body>
</html>

