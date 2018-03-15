<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<title>Scripting Languages Console</title>
    <link rel="stylesheet" href="<c:url value="/static/css/zTree/zTreeStyle.css"/>" type="text/css" media="screen, projection" />
    <link rel="stylesheet" href="<c:url value="/static/css/console/scripting.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/onoff.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/codemirror3/codemirror.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-custom.css"/>" type="text/css"
			media="screen, projection" />

    <script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/jquery.ztree.all-3.5.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/codemirror.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/groovy.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/javascript.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/clike.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/active-line.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/console/scripting.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/console/historyHandler.js"/>"></script>
</head>
	<body>
    <c:choose>
        <c:when test="${ scriptData != null }">
            <c:choose>
                <c:when test="${ scriptData.exception == null }">
                    <span id="uploadResult" data-level="notice" data-result="File save successful"></span>
                </c:when>
                <c:otherwise>
                    <span id="uploadResult" data-level="error" data-result="File save failed"></span>
                </c:otherwise>
            </c:choose>
        </c:when>
    </c:choose>
    <span hidden="hidden" id="lastActiveTab">${lastActiveTab}</span>
		<div class="prepend-top span-17 colborder" id="content" encryptUrl="<c:url value="/console/encrypt" />" decryptUrl="<c:url value="/console/decrypt" />">
			<button id="toggleSidebarButton">&gt;</button>
			<div class="marginLeft">
				<h2>Scripting Languages Console</h2>

                <div id="tabsNoSidebar">
					<ul>
						<li><a id="nav-tabs-edit" href="#tabs-edit">Edit Statement</a></li>
						<li><a id="nav-tabs-browse" href="#tabs-browse">Browse</a></li>
						<li><a id="nav-tabs-result" href="#tabs-result">Result</a></li>
						<li><a id="nav-tabs-output" href="#tabs-output">Output</a></li>
						<li><a id="nav-tabs-stacktrace" href="#tabs-stacktrace">Stack trace</a></li>
						<li><a id="nav-tabs-history" href="#tabs-history">History</a></li>
				</ul>

					<div id="tabs-edit">
						<br/>
						<form action="<c:url value="/console/scripting/save" />" method="post">
                            Script type: <select id="scriptType" name="scriptType"><c:choose><c:when test="${scriptData != null && scriptData.getContent() != null}">
                                <option value="${fn:escapeXml(scriptData.getContent().getEngineName())}"><c:out value="${scriptData.getContent().getEngineName()}"/></option>
                                <c:forEach items="${scriptingLanguages}" var="script">
                                    <c:if test="${script.name.toLowerCase() != scriptData.getContent().getEngineName().toLowerCase()}">
                                        <option value="${script.name}"><c:out value="${script.name}"/></option>
                                    </c:if>
                                </c:forEach></c:when><c:otherwise>
                                    <c:forEach items="${scriptingLanguages}" var="script">
                                        <option value="${script.name}"><c:out value="${script.name}"/></option>
                                    </c:forEach>
                                </c:otherwise></c:choose>
                            </select>
                            <input type="submit" value="Save" id="saveButton" style="float:right" data-executorUrl="<c:url value="/console/scripting/save" />"></button>
                            <div style="float: right;margin-right: 10px">code:<c:choose><c:when test="${scriptData != null && scriptData.getContent() != null}"><input type="text" id="code" name="code" value="${fn:escapeXml(scriptData.getCode())}"></c:when><c:otherwise><input type="text" id="code" name="code"></c:otherwise></c:choose>

                            </div>
                            <div id="textarea-container">
                                <div id="spinnerWrapper">
                                    <img id="spinner" src="<c:url value="/static/img/spinner.gif"/>" alt="spinner"/>
                                </div>
                                <textarea id="script" name="script"><c:choose><c:when test="${scriptData != null && scriptData.getContent() != null}"><c:out value="${scriptData.getContent().getContent()}"/></c:when><c:otherwise>
spring.beanDefinitionNames.each {
    println it
}
return "Groovy Rocks!"</c:otherwise></c:choose></textarea>
                                <input type="hidden"
                                       name="${_csrf.parameterName}"
                                       value="${_csrf.token}"/>
                            </div>
                        </form>

                        <div id="modeWarningMsg">Rollback Mode: script results <strong>won't be persisted</strong> in database!</div>
						<div id="buttons">
							<div class="onoffswitch-large" style="float:left;margin-top:7px">
							    <input type="checkbox" class="onoffswitch-checkbox" id="commitCheckbox"/>
							    <label class="onoffswitch-label" for="commitCheckbox">
							        <div class="onoffswitch-inner" _on="COMMIT" _off="ROLLBACK"></div>
							        <div class="onoffswitch-switch-large"></div>
							    </label>
							</div>
							<button id="executeButton" style="float:right" data-executorUrl="<c:url value="/console/scripting/execute" />">Execute</button>
						</div>
						<div style="clear:both"></div>
					</div>

					<div id="tabs-result">
                        <div class="executedModeMsg"></div>
                        <pre id="result" class="hidden scrollable"></pre>
					</div>

					<div id="tabs-output">
                        <div class="executedModeMsg"></div>
                        <pre id="output" class="hidden scrollable"></pre>
					</div>

					<div id="tabs-stacktrace">
                        <div class="executedModeMsg"></div>
                        <pre id="stacktrace" class="hidden scrollable"></pre>
					</div>

					<div id="tabs-history" cookieName="ScriptingShellHistory" historyMax="${historyMax}" cookiePath="/console/scripting/">
			      	<legend style="padding-left:5px;padding-top:10px;padding-bottom:10px" id="historyLegend">
							<span>History</span>
							<a href="#" id="clearHistory" style="font-size:75%">(clear)</a></br>
							<span id="historyMax" style="color:red;font-weight:bold;font-size:12px">Maximum size of history achieved!</span>
						</legend>
						<ul id="historyList" style="margin:0"></ul>
					</div>
                    <div id="tabs-browse" >
                        <script language="javascript">var zNodes = ${scripts};</script>
                        <ul id="treeDemo" class="ztree"></ul>
                        <br/><hr/>
                        Import Script File<br/>
                        <c:set var="fileImportUrl">
                            <c:url value="/console/scripting/upload"/>
                        </c:set>
                        <form:form method="post" action="${fileImportUrl}" commandName="scriptFileFormData"
                                   enctype="multipart/form-data">
                            <spring:bind path="file">
                                <hac:input-file name="file"/>
                            </spring:bind>
                            <table>
                                <tr>
                                    <td>Name:</td>
                                    <td><form:input path="name" size="25" id="name"/></td>
                                </tr>
                                <tr>
                                    <td>Type:</td>
                                    <td><select id="type" name="type">
                                        <c:forEach items="${scriptingLanguages}" var="script">
                                            <option value="${script.name}"><c:out value="${script.name}"/></option>
                                        </c:forEach>
                                    </select></td>
                                </tr>
                            </table>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="submit" value="Import file"/>
                        </form:form>
                    </div>
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
					 This page provides the Scripting Languages console. It comes with implicit variables:<br><br>
						<ul>
							<li><strong>spring</strong> - Spring ApplicationContext</li>
							<li><strong>out</strong> - Standard Output Stream</li>
						</ul>
						<hr/>
						<strong>Browse</strong> tab: Allows you to browse, upload or delete scripts stored in the script repository.<br><br>
						<strong>Result</strong> tab: Displays results for the executed script. You can return a result using the <strong>return</strong> keyword. If the result is in a <strong>String</strong>, then you can read it directly. For all other objects the <strong>toString()</strong> method is called and the result is displayed.<br><br>
						<strong>Output</strong> tab: Displays a string if the script uses a method to display text. During script execution all output to <strong>System.out</strong> is redirected and shows up in this tab. The implicit variable <strong>out</strong> is available and is of <strong>PrintStream</strong> type.<br><br>
						<strong>Stacktrace</strong> tab: Displays the stack trace for the corrupted script.  <br><br>
						<strong>History</strong> tab: Displays the local script history and the timestamp of the last execution.  <br><br>
					</div>
				</div>
                <h3 class="caps">Hybris Help</h3>
                <div class="box">
                    <ul>
                        <li><a href="${scriptingHelpUrl}" target="_blank" class="quiet">Scripting</a></li>
                    </ul>
                </div>
			</div>
		</div>
        <div id="rMenu">
            <ul>
                <li id="reloadButton">Reload Scripts</li>
                <li id="loadButton" data-executorUrl="<c:url value="/console/scripting/load" />">Load Script</li>
                <li id="deleteButton" data-executorUrl="<c:url value="/console/scripting/delete" />">Delete Script</li>
            </ul>

        </div>
	</body>
</html>

