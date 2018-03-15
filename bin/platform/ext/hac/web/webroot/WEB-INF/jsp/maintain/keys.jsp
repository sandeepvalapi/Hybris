<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Encryption Keys</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />	
	<link rel="stylesheet" href="<c:url value="/static/css/maintain/keys.css"/>" type="text/css" media="screen, projection" />	
	
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/maintain/keys.js"/>"></script>
</head>
	<body>
			<div class="prepend-top span-17 colborder" id="content">
				<button id="toggleSidebarButton">&gt;</button>
				<div class="marginLeft marginBottom">
					<h2>Encryption Keys</h2>
					
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1">Generation</a></li>
							<li><a href="#tabs-2">Migration</a></li>
							<li><a href="#tabs-3">Credit cards encryption</a></li>
						</ul>		
						<div id="tabs-1">
							<form id="generateKeyForm">
								<dl>
									<dt>Key size</dt>
									<dd>
										<select id="keySize">
					    					<option value="128" selected>128</option>
					    					<option value="192">192</option>
					    					<option value="256">256</option>
										</select>								
									</dd>
									
									<dt>Output file</dt>
									<dd>
										<input type="text" name="fileName" id="fileName" class="wide" value="Generated-KEYSIZE-Bit-AES-Key.hybris"  placeholder="Enter a valid file name..."/>
									</dd>
								</dl>
								
								<button id="buttonGenerate" data-url="<c:url value="/maintain/keys/generate"/>">Generate</button>
							</form>
							<div id="result">
								<dl>
									<dt>Generated file</dt>
									<dd id="generatedFile"></dd>
								</dl>
							</div>
							
						</div>
						<div id="tabs-2" data-loadUrl="<c:url value="/maintain/keys/migration/data"/>">
							<div id="info">
								<div>The current hybris properties are not in the expected format. Add to the <span class="filecol">local.properties</span> file the following:<br/>	<br/>	
									<ol>
										<li>Add <strong>symmetric.key.file.1=$name_of_the_old_key_file</strong>  property definition.</li>
										<li>Add <strong>symmetric.key.file.2=$name_of_the_new_key_file</strong>  property definition.</li>
										<li>Add <strong>symmetric.key.file.default=2</strong>  property definition.</li>
									</ol>
								</div>
	
								<br/>						
								<div>The default key file referred in <strong>symmetric.key.file.default</strong> property is used for all new encryption steps. This means that with the sample setting below, you will initiate a migration from key <strong>1</strong> to key <strong>2</strong>:<br/>							
<pre>symmetric.key.file.1=weak-symmetric.key
symmetric.key.file.2=256bit-symmetric.key
symmetric.key.file.default=2</pre>

After introducing all changes restart your application server and perform encryption keys migration.	
								</div>
							</div>
							
							<div id="keys">
								<h3>Encryption keys</h3>
									<table id="tableKeys">
										<thead>
											<tr>
												<th>ID</th>
												<th>Key file</th>
											</tr>
										</thead>
										<tbody>
									      					
										</tbody>
									</table>
								<br/><br/>
								<h3>Encrypted attributes</h3>
										<table id="tableTypes">
											<thead>
												<tr>
													<th>Selection</th>
													<th>Type</th>
													<th>Attribute</th>
													<th>Instances</th>												
												</tr>
											</thead>
											<tbody>
										      					
											</tbody>
										</table>	
									
									<button id="buttonMigrate" data-url="<c:url value="/maintain/keys/migration/migrate"/>">Migrate</button>		
									
									<div id="migrationResult"></div>					
							</div>
						</div>
						<div id="tabs-3" data-loadUrl="<c:url value="/maintain/keys/creditcards/unencrypted" />" data-checkEncryptionUrl="<c:url value="/maintain/keys/creditcards/checkencryption" />">
							<div id="spinnerWrapper">
								<img id="spinner" src="<c:url value="/static/img/spinner.gif"/>" alt="spinner"/>
							</div>
							<div id="unenrcyptedCardsWrapper">
								<table id="unenrcyptedCardsOverview">
									<thead>
										<tr>
											<th>PK</th>
											<th>Number</th>
											<th>Type</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div class="marginBottom">
								<button id="encryptCards" data-url="<c:url value="/maintain/keys/creditcards/encrypt" />" >Encrypt all</button>
							</div>
						</div>
					</div>						
				</div>				
			</div>
			<div class="span-6 last" id="sidebar1">
				<div class="prepend-top" id="recent-reviews">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
						 This page enables you to generate encryption keys of different sizes. <br /><br />
						 <hr />
						 <hac:note>
						 <strong>Output file</strong><br />
						 The name of the output file can consist of the following characters only: <br />
						 	<ul>
						 		<li>Latin alphabet characters in uppercase: <strong>A-Z</strong></li>
						 		<li>Latin alphabet characters in lowercase: <strong>a-z</strong></li>
						 		<li>Numeric characters: <strong>0-9</strong></li>
						 		<li>Nonalphabetical characters: <strong>.</strong>  <strong>-</strong>  <strong>_</strong></li>
						 	</ul>
						 </hac:note>
						 <br />
						 <hr />
						 <hac:note>
							The Java Runtime Environment (JRE) must support the level of encryption required by the key length that you select. 
							For example, you cannot use a 256 bit encryption key with a JRE that supports only 128 bit encryption. By default, 
							the hybris Platform is configured with restricted or limited strength ciphers. To use less restricted encryption algorithms, 
							you must download and apply the unlimited jurisdiction policy files, local_policy.jar and US_export_policy.jar.						 
						 </hac:note>
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiEncryptionKeys}" target="_blank" class="quiet" >Transparent Attribute Encryption (TAE)</a> </li>
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
						This page enables you to migrate encryption keys.
						</div>
					</div>
					<h3 class="caps">See also in the hybris Wiki</h3>
					<div class="box">
						<ul>
							<li> <a href="${wikiEncryptionKeys}" target="_blank" class="quiet" >Transparent Attribute Encryption (TAE)</a> </li>
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
						This page provides a list of credit cards with unencrypted numbers.
						</div>
					</div>
				</div>
			</div>	
	</body>
</html>

