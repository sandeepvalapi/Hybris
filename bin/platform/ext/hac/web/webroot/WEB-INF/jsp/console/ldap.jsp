<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>LDAP</title>
	
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/console/ldap.js"/>"></script>
			
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft marginBottom">
					<h2>
						LDAP
					</h2>
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1">Configuration check</a></li>
							<li><a href="#tabs-2">LDIF import</a></li>
							<li><a href="#tabs-3">LDAP search</a></li>
						</ul>		
						<div id="tabs-1" data-loadUrl="<c:url value="/console/ldap/data"/>">
							<dl>
								<dt>Username</dt>
								<dd><input id="username" type="text" placeholder="LDAP Username"/></dd>
							
								<dt>Password</dt>
								<dd><input id="password" type="password" placeholder="LDAP Password"/></dd>
								
								<dt>Server available</dt>
								<dd id="serverAvailable"></dd>
							</dl>
							
							<button id="buttonLDAPCheck" data-url="<c:url value="/console/ldap/check"/>">Check</button>
							
						</div>
						<div id="tabs-2">
							<dl>
								<dt>LDIF file</dt>
								<dd><input id="ldifFile" class="wide" type="text" placeholder="LDIF File"/></dd>
							
								<dt>Configuration</dt>
								<dd><input id="confFile" class="wide" type="text" placeholder="Configuration"/></dd>
								
							</dl>
							
							<button id="buttonImpex" data-url="<c:url value="/console/ldap/impex"/>">Generate ImpEx</button>
							
							
							<div id="impexResult" class="marginRight">
								<textarea id="impex"></textarea>
							</div>
						</div>
						<div id="tabs-3">
							<dl>
								<dt>Search base</dt>
								<dd><input id="searchBase" class="wide" type="text" placeholder="Search base" value="cn=development,ou=hybris groups,ou=hybris,dc=hybris,dc=de"/></dd>
							
								<dt>Select</dt>
								<dd><input id="searchSelect" class="wide" type="text" placeholder="Selection" value="distinguishedName, objectClass, displayName, member"/></dd>
								
								<dt>Filter</dt>
								<dd><input id="searchFilter" class="wide" type="text" placeholder="Filter" value="(&amp;(objectclass=*)(cn=*))"/></dd>
								
								<dt>Limit</dt>
								<dd><input id="searchLimit" type="number" value="-1"/></dd>
							</dl>
							
							<button id="buttonSearch" data-url="<c:url value="/console/ldap/search"/>">Search</button>
							
							<div id="searchResult" class="marginRight">
								<textarea id="search"></textarea>
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
						 	 This page enables you to check the LDAP server configuration.
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
						 		<li> <a href="${wikiLdap}" target="_blank" class="quiet">ldap Extension</a> </li>
						 	</ul>
						</div>
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
						 	 This page enables you to transform a LDIF file into an ImpEx file.
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
								<li> <a href="${wikiLdap}" target="_blank" class="quiet">ldap Extension</a> </li>
							</ul>
						</div>
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
						 	 This page enables you to query your configured LDAP server.
						</div>
					</div>
					<h3 class="caps">
						See also in the hybris Wiki
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
 					 	   	<li> <a href="${wikiLdap}" target="_blank" class="quiet">ldap Extension</a> </li>
							</ul>
						</div>
					</div>
				</div>
			</div>		
	</body>
</html>

