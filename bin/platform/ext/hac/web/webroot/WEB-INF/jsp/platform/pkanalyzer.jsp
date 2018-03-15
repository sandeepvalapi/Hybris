<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Pk Analyzer</title>
<link rel="stylesheet" href="<c:url value="/static/css/platform/pkanalyzer.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/platform/pkanalyzer.js"/>"></script>
</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft">
			<h2>PK Analyzer</h2>
			<c:set var="url">
				<c:url value="/platform/pkanalyzer/analyze" />
			</c:set>
			<c:set var="errorMsg">
				<spring:message code="platform.pkanalyzer.forms.pkstring.errormsg" />
			</c:set>
			<form:form method="post" action="${url}" commandName="pkAnalyzerFormData">
				<fieldset>
					<form:label path="pkString">
						<spring:message code="platform.pkanalyzer.pkString" />
					</form:label>
					<spring:bind path="pkString">
						<input type="number" min="1" value="1" name="pkString" data-errormsg="${errorMsg}" id="pkString"/>
					</spring:bind>
					<button id="buttonSubmit">
						<spring:message code="general.forms.execute" />
					</button>
				</fieldset>
			</form:form>
			<div id="analyzerResult" class="marginBottom">
				<hr/>
				<ul>
					<li id="pkString">
						<b>PK is </b><br/>
						<span id="pkStringValue"/>
					</li>
					<li id="uuidBased">
						<b>PK is </b><br/>
						<spring:message code="platform.pkanalyzer.pk.uuid.based"/>
					</li>
					<li id="counterBased">
						<b>PK is </b><br/>
						<spring:message code="platform.pkanalyzer.pk.counter.based"/>
					</li>
					<li id="typeCode">
						<b>Typecode </b><br/>
						<span id="composedType"></span>
						<nobr>
							(<span id="typecode"></span>)
						</nobr>
					</li>
					<li id="clusterId">
						<b>Cluster ID </b><br/>
						<span id="entryClusterId"></span>
					</li>
					<li id="creationTime">
						<b>Creation time </b><br/>
						<nobr>
							<span id="creationDateValue"></span>
							(<span id="creationTimeValue"></span>)
						</nobr>
					</li>
					<li id="milliCnt">
						<b>MilliCnt </b><br/>
						<span id="milliCntValue"/>
					</li>
				</ul>
				 				
				<div id="binaryRepresentationPane">
					<table id="extendedBinaryRepresentation">
						<thead id="head">
							<tr id="mainHeaders">
								<th colspan="16">Typecode</th>
								<th colspan="4">Cluster<br /> (bits 4-0)</th>
								<th colspan="1">UUID<br />/Cnt<br />
								</th>
								<th colspan="39">Timestamp</th>
								<th colspan="2">Cluster (bits 6-5)</th>
								<th colspan="2">Millicnt</th>
							</tr>
							<tr>
								<c:forEach items="${bitHeaders}" var="bitHeader">
									<th>${bitHeader}</th>
								</c:forEach>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Page description</h3>
				<div class="box">
					<div class="quiet">
						This page enables you to display information encoded in a primary key (PK).
					</div>
				</div>
		</div>
	</div>
</body>
</html>

