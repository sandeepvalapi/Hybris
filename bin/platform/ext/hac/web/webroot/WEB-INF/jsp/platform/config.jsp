<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
	<title>Configuration</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/platform/config.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/onoff.css"/>" type="text/css" media="screen, projection" />
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/platform/config.js"/>"></script>

</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content" edited-url="<c:url value="/platform/config/edited/"/>" applyall-url="<c:url value="/platform/config/applyedited/"/>" resetall-url="<c:url value="/platform/config/resetedited/"/>" valuechanged-url="<c:url value="/platform/config/valuechanged/"/>">
				<button id="toggleSidebarButton">&gt;</button>
			 	<div class="marginLeft">
					<h2>
						Configuration Properties
					</h2>
					
					<div style="margin-top:30px;">
							<button style="float:left" id="applyAllBtn">apply all</button>
							<button style="float:left" id="resetAllBtn">reset all</button>
							
							<div class="onoffswitch" style="float:right">
							    <input type="checkbox" class="onoffswitch-checkbox" id="toggleProperties"/>
							    <label class="onoffswitch-label" for="toggleProperties">
							        <div class="onoffswitch-inner" _on="ON" _off="OFF"></div>
							        <div class="onoffswitch-switch"></div>
							    </label>
							</div>
							<label style="float:right;margin-right:1em">Show unsaved only:</label>
							<br/>
							<hr/>
					</div>			
					<div id="configTableWrapper" style="margin-top: 10px;">
						<table id="props" data-configUpdUrl="<c:url value="/platform/configstore/"/>" data-configDelUrl="<c:url value="/platform/configdelete/"/>">
							<thead>
								<tr>
									<th>Key</th>
									<th>Value</th>
								</tr>
								<tr>
									<th style="background:#dedede !important;">
										<div style="width:300px">
											<input type="text" class="addInput" id="configKey" placeholder="New key..."/>
										</div>
									</th>
									<th style="background:#dedede !important;border-left-style:solid;border-left-width:1px;border-left-color:#bcbcbc;">
										<div style="width:330px">
											<input type="text" id="configValue" placeholder="New value..." style="width:265px;"/>
							          	<div style="float:right;">
							          		<button id="addButton" style="margin:10px 0 0 0">add</button>
							          	</div>
										</div>
									</th>
								</tr>
							</thead>
							
							<tbody>								
						      <c:forEach var="entry" items="${props}">
						        <tr id="${fn:escapeXml(entry.key)}">
						          <td>
									${fn:escapeXml(entry.key)}
						          </td>
						          <td>
										<div>
						          		<input type="text" class="configValue" name="${fn:escapeXml(entry.key)}" value="${fn:escapeXml(entry.value)}"/>
						          		<div style="float:right">
						          			<img class="removeIcon" alt="remove" title="remove" src="<c:url value="/static/img/delete.png" />"/>
						          			<img class="applyIcon" alt="apply" title="apply" src="<c:url value="/static/img/tick.png" />"/>
						          		</div>
						          	</div>
						          </td>
						        </tr>
						      </c:forEach>					
							</tbody>
						</table>
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
							This page provides the list of all currently set hybris properties and their values. You can modify these properties or add a new one.
							<br />
							<br />
							<ul>
								<li><b>apply all:</b> Saves all changed but not saved values at once.
								</li>
								<li><b>reset all:</b> Resets all changed and not saved values.
								</li>
							</ul>
							<hr />
							<hac:note>
							Changes entered on this page are stored only in the memory. That means all values are reloaded from the <span class="filecol">local.properties</span> or <span class="filecol">project.properties</span> file when restarting the application server. To make them persistent, enter changes in <span class="filecol">local.properties</span> or <span class="filecol">project.properties</span> file directly.
							</hac:note>
						</div>
					</div>
					
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
							 <li> <a href="${wikiProjectProperties}" target="_blank" class="quiet">Configuring the Behavior of the hybris Multichannel Suite</a> </li> 
							 <li> <a href="${wikiConfiguration}" target="_blank" class="quiet">Configuration Properties Reference</a> </li> 
							</ul>
						</div>
					</div>			
				</div>
			</div>
	</body>
</html>

