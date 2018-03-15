<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>   
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Classpath Analyzer</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/platform/jars.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/platform/jars.js"/>"></script>	

</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft marginBottom" id="inner">
					<h2>
						Classpath Analyzer
					</h2>	
					
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">Order of loaded resources</a></li>
						<li><a href="#tabs-2">Loaded classes</a></li>
					</ul>
					<c:url value="/platform/jars/analyze" var="buttonAnalyzeUrl"/>
					<c:url value="/platform/jars/data" var="buttonDataUrl"/>
					<div id="tabs-1" data-analyzeUrl="${buttonAnalyzeUrl}" data-url="${buttonDataUrl}">
						<div class="marginBottom marginTop">
						
						<dl>
							<dt>Classloader scope</dt>
							<dd><select id="scopeJars"></select></dd>
							<dt>Regular expression filter</dt>
							<dd><input id="filterJars" type="text" placeholder="regular expression"/></dd>
							<dt>Duplicate JARs</dt>
							<dd><input id="duplicatesOnly" type="checkbox"/><label for="duplicatesOnly">Only duplicated JAR file names</label></dd>																			
						</dl>
						
						<button id="buttonResultsJars">Show results</button>
						
						<table id="loadedJars">
							<thead>
								<tr>
									<th>Resource name</th>
									<th>Scope of classloader</th>
									<th>Type</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						</div>
					</div>
					<div id="tabs-2">
						<div class="marginBottom marginTop">
						
						<dl>
							<dt>Classloader scope</dt>
							<dd><select id="scopeClasses"></select></dd>
							<dt>Regular expression filter</dt>
							<dd><input id="filterClasses" type="text" placeholder="regular expression" value=".*BMP.*"/></dd>
							<dt>Show all</dt>
							<dd><input id="allClasses" type="checkbox"/><label for="allClasses">Show all loaded classes</label></dd>																			
						</dl>
						
						<button id="buttonResultsClasses">Show results</button>						
						
						
						<table id="loadedClasses">
							<thead>
								<tr>
									<th>Class</th>
									<th>JAR(s)</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						</div>		
					</div>
				</div>						
				
					
				</div>			
			</div>
			<div class="span-6 last" id="sidebar1">
				<div class="prepend-top" id="recent-reviews1">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides a list of resources like JAR files and class folders in the hybris Platform or specified web application. Results are displayed in the order of loading.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiClassLoaderMonitor}" target="_blank" class="quiet" >Classpath Analyzer</a> </li>
						</ul>
					</div>
				</div>
			</div>	
			<div class="span-6 last" id="sidebar2">
				<div class="prepend-top" id="recent-reviews2">
					<h3 class="caps">Page description</h3>
					<div class="box">
						<div class="quiet">
							This page provides resources (JAR files) location of loaded classes.<br><br>
							<hr />
							<hac:note>
								Displaying results from all loaded classes may force a long loading time.
							</hac:note>
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiClassLoaderMonitor}" target="_blank" class="quiet" >Classpath Analyzer</a> </li>
						</ul>
					</div>
				</div>
			</div>	
	</body>
</html>

