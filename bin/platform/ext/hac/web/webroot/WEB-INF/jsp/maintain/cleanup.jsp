<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>   
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Cleanup</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />	
	<link rel="stylesheet" href="<c:url value="/static/css/maintain/cleanup.css"/>" type="text/css" media="screen, projection" />	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/maintain/cleanup.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft marginBottom">
					<h2>Cleanup</h2>
					
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">Type system</a></li>
						<c:if test="${luceneAvailable}">
							<li><a href="#tabs-2">Lucene search indexes</a></li>
						</c:if>
						<li><a href="#tabs-3">Orphaned media files</a></li>
						<li><a href="#tabs-4">Drop Type Systems</a></li>
					</ul>		
					<div id="spinnerWrapper">
						<img id="spinner" src="<c:url value="/static/img/spinner.gif"/>" alt="spinner"/>
					</div>						
					<div id="tabs-1">
						<div id="orphansContent">
							<div id="orphans">
								<h2>
									Orphaned types
								</h2>
								<ul id="orphansUL"></ul>
							</div>
							
							<div id="removalControls">
								<hr/>
								<c:url var="cleanupTypesUrl" value="/maintain/cleanup/types/remove" />
								<input id="deleteInstances" type="checkbox" checked="checked"/> <label for="deleteInstances"><spring:message code="maintenance.cleanup.forms.delete.instances"/></label><br/>
								<input id="deleteRelated" type="checkbox" checked="checked"/> <label for="deleteRelated"><spring:message code="maintenance.cleanup.forms.delete.deployments"/></label><br/>
								<button id="buttonRemoveOrphaned" data-url="${cleanupTypesUrl}"><spring:message code="maintenance.cleanup.forms.buttons.remove.orphans" /></button>
							</div>
						</div>
					</div>					
					<c:if test="${luceneAvailable}">					
					<div id="tabs-2">
						<div id="luceneIndexesContent">
							<table id="luceneIndexes">
								<thead>
									<tr>
										<th><spring:message code="maintenance.cleanup.tables.luceneindex.code" /></th>
										<th><spring:message code="maintenance.cleanup.tables.luceneindex.pk" /></th>
										<th><spring:message code="maintenance.cleanup.tables.luceneindex.rebuildstart" /></th>
										<th><spring:message code="maintenance.cleanup.tables.luceneindex.rebuildstop" /></th>
										<th><spring:message code="maintenance.cleanup.tables.luceneindex.uptodate" /></th>
									</tr>
								</thead>
							</table>
							<c:url var="removeLuceneIndexesUrl" value="/maintain/cleanup/lucene/remove"/>
							<button id="buttonRemoveLuceneIndexes" data-url="${removeLuceneIndexesUrl}"><spring:message code="maintenance.cleanup.forms.buttons.remove.luceneindex" /></button>
							<div id="luceneRemovalResult"  
								  data-failureMsg="<spring:message code="maintenance.cleanup.messages.luceneindex.failure" arguments="${luceneIndexDir}" />" 
								  data-successMsg="<spring:message code="maintenance.cleanup.messages.luceneindex.success" arguments="${luceneIndexDir}" />">
							</div>
						</div>
					</div>
					</c:if>
					<div id="tabs-3">
						<div id="orphansMedia">
							<dl class="marginTop">
							  <dt>Files (total)</dt>
							    <dd id="filesOverall"></dd>
							  <dt>Files (used by platform)</dt>
							    <dd id="filesUsed"></dd>
							  <dt>Files (orphaned)</dt>
							    <dd id="filesOrphaned"></dd>
							  <dt>Files (not hybris media)</dt>
							    <dd id="filesNotHybrisMedia"></dd>
							  <dt>Directories scanned</dt>
							    <dd id="scannedDirs"></dd>						    
							</dl>	
							
							<div id="mediaHybrisWrapper">
								<h3>Orphaned Media Files</h3>
								<div>
									<table id="mediaHybrisTable" >
										<thead>
											<tr>
												<th>File Name</th>
												<th>Absolute Path</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								 </div>
								 <c:url var="cleanupOrphanedMediaUrl" value="/maintain/cleanup/media/remove" />
								<button id="buttonRemoveOrphanedMedia" data-url="${cleanupOrphanedMediaUrl}">Remove orphaned media</button>
							</div>
							
							
							<div id="mediaNotHybrisWrapper">
								<h3>Non-Hybris Media</h3>
								<p>The media files below were found in the hybris Multichannel Suite data directories but do not belong to it. You can remove them manually if needed.</p>
								<table id="mediaNotHybrisTable" >
									<thead>
										<tr>
											<th>File Name</th>
											<th>Absolute Path</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>	
							</div>					
						</div>
					</div>
					<div id="tabs-4"  class="marginBottom">
						<div id="typeSystemsToRemove" visibility="hidden">
							<c:url var="cleanupTypesUrl" value="/maintain/cleanup/typesystem/remove" />
							<spring:message code="maintenance.cleanup.forms.existing.typesystems" />
							<select id="typeSystem" name="typeSystem">
								<c:forEach var="typeSystem" items="${typeSystemsList}">
									<option value="${typeSystem.value}">${typeSystem.key}</option>
								</c:forEach>
							</select>
							<button id="buttonRemoveTypeSystem" data-url="${cleanupTypesUrl}"><spring:message code="maintenance.cleanup.forms.buttons.remove.typesystem" /></button>
						</div>
						<div id="noTypeSystemsToRemove">
							<spring:message code="maintenance.cleanup.forms.nothing.to.remove" />
						</div>
					</div>
				</div>
					
				</div>				
			</div>
			<div class="span-6 last" id="sidebar1">
				<div class="prepend-top">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
						This page provides a list of all system orphaned types. You can delete them, optionally including related deployment or instances from the type system.
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiCleanOprhanedTypes}" target="_blank" class="quiet" >Cleanup Type System</a> </li>
						</ul>
					</div>
				</div>
			</div>
			<div class="span-6 last" id="sidebar2">
				<div class="prepend-top">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
						This page provides a list of all related Lucene indexes. You can remove all index files from your hard disk. It is necessary when for example you make a change in the type system and create new attributes that need to be indexed.<br><br>
						<hr/>
						<hac:warning>
							<strong>Removing Lucene indexes</strong><br>
								<div class="break-word">
									<spring:message code="maintenance.cleanup.messages.luceneindex.removalcaution" arguments="${luceneIndexDir}" />
								</div>
							</hac:warning>
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiLuceneSearch}" target="_blank" class="quiet" >lucenesearch Extension - Technical Guide</a> </li>
						</ul>
					</div>
				</div>
			</div>
			<div class="span-6 last" id="sidebar3">
				<div class="prepend-top">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
						This page provides details about media files. If there are orphaned or not hybris media files in the file system, you will see a list of them with delete option.
						</div>
					</div>
				</div>
			</div>			
	</body>
</html>

