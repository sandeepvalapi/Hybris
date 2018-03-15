<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 

<html>
<head>
	<title>Thread Dump</title>
	<link rel="stylesheet" href="<c:url value="/static/css/monitoring/threaddump.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/codemirror3/codemirror.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-custom.css"/>" type="text/css"
			media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/console/codemirror3-threaddump.css"/>" type="text/css"
			media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/codemirror.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/threaddumphighlight.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/codemirror3/cmfullscreen.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/monitoring/threaddump.js"/>"></script>

</head>
	<body>
			<div class="prepend-top span-24" id="content">
				<div>
					<h2>Thread Dump</h2>
					<div id="tabs">
						<ul>
					   	<li><a href="#tabs-1">Current Dump</a></li>
					    	<li><a href="#tabs-2">Record Dumps</a></li>
					  	</ul>
						<div id="tabs-1">
							<br/>
							<h3>Current Dump</h3>
					   	<p>This page provides a thread dump of the current VM-threads.</p>
							<div>
								<form id="downloadForm" method="GET" action="<c:url value="/monitoring/threaddump/download"/>">
									<input type="submit" value="Download">
								</form>
							</div>
							<div id="textarea-container" class="border">
								<textarea id="dump">${dump}</textarea>
							</div>
						</div>
						<div id="tabs-2">
							<br/>
							<h3>Record Dumps</h3>
							<hac:note>
							This page provides the possibility to select thread dumps over a period of time. You can 
							select in which time periods the thread dumps are being collected. When you click stop, 
							the thread dumps are being downloaded as zip File.
							</hac:note>
							<br/>
							<form id="startForm" startUrl="<c:url value="/monitoring/threaddump/start"/>" updateUrl="<c:url value="/monitoring/threaddump/update"/>" style="margin-bottom:5px">
								<select id="interval">
									<option value="5000">5 sec</option>
									<option value="10000">10 sec</option>
									<option value="30000">30 sec</option>
									<option value="60000">60 sec</option>
									<option value="300000">5 min</option>
									<option value="900000">15 min</option>
									<option value="1800000">30 min</option>
								</select>
								<input type="submit" value="Start"/>
							</form>
							<hr/>
							
							<form id="stopForm" method="GET" action="<c:url value="/monitoring/threaddump/stop"/>"   >
							<c:choose>
      							<c:when test="${dumpAvailable}">
									<input id="stopAction" type="submit" value="Stop/Download" <c:out value="${dumpAvailable}"/> />
								</c:when>
								<c:otherwise>
									<input id="stopAction" type="submit" value="No dump available" disabled />
								</c:otherwise>
							</c:choose>
								
							</form>
						</div>
					</div>
					<label id="debug"></label>
				</div>
			</div>
	</body>
</html>