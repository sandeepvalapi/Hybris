<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 

<html>
<head>
	<title>LDAP</title>
	
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
			
</head>
	<body>
		<div class="prepend-top span-17 colborder" id="content">
			<button id="toggleSidebarButton">&gt;</button>
				
			<div class="marginLeft" id="inner">
				<h2>
					LDAP
				</h2>	
				<hac:note>
					<strong>The LDAP-extension is not part of your hybris setup.</strong>
				</hac:note>
			</div>	
		</div>
			
		<div class="span-6 last" id="sidebar">
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
				
	</body>
</html>

