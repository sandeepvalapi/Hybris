<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title>Support</title>
<link rel="stylesheet" href="<c:url value="/static/css/platform/support.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/platform/support.js"/>"></script>

<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection">
</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<div class="marginLeft" id="inner">
			<h2>Create ZIP File</h2>

			<input type="checkbox" id="checkSelectAll"> <label for="checkSelectAll"><strong>Select all</strong></label>
			<div class="box" id="supportFiles" data-url="<c:url value="/platform/support/data"/>"></div>

			<button id="buttonCreateZIP" data-url="<c:url value="/platform/support/zip"/>">Create</button>
			<button id="buttonDownloadZIP">Download</button>
			<button id="buttonSendZIP" data-url="<c:url value="/platform/support/zip/send"/>">Send in email</button>



		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<h3 class="caps">Page description</h3>
			<div class="box">
				<div class="quiet">
					This page enables you to create a ZIP file with information about the running system, which you can send to the hybris Support.<p/>
				</div>
				<div class="quiet">
					The ZIP file includes the type system definition (items.xml files), the properties files and the installed extensions. Additionally
					you can add the console~ and access logfiles or other files from the log folder to this zip.
				</div>
			</div>
			<h3 class="caps">See also in the hybris Wiki</h3>
				<div class="box">
					<ul>
						<li> <a href="${wikiSupport}" target="_blank" class="quiet" >Support Home</a> </li>
					</ul>
				</div>
		</div>
	</div>

	<form id="downloadForm" method="GET" action="<c:url value="/platform/support/zip/download"/>"></form>
</body>
</html>

