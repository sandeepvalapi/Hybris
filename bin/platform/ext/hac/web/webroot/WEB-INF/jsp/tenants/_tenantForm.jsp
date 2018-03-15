<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="tenantForm">
	<form:form method="${formMethod}" action="${url}" commandName="${commandName}">
		<fieldset>
			<legend>Tenant</legend>
			<p>
				${tenantID_disabled}
				<form:label path="tenantID">Tenant ID:</form:label>
				<form:input path="tenantID" size="8" readonly="true" />
				<form:errors path="tenantID" cssClass="error" delimiter=", "/>
			</p>

			<p>
				<form:label path="locale">Locale: </form:label>
				<form:input path="locale" size="5" readonly="true" />

				<form:label path="timezone">Timezone: </form:label>
				<form:input path="timezone" size="14" readonly="true" />
			</p>

			<p>
				<form:label path="dbUrl">DB URL: </form:label><br />
				<form:textarea path="dbUrl" rows="5" cols="50" id="dbUrl" readonly="true" />
			</p>
			
			<p>
				<form:label path="dbDriver">DB driver: </form:label>
				<form:input path="dbDriver" size="50" readonly="true" />			
			</p>

			<p>
				<form:label path="dbUser">DB user: </form:label>
				<form:input path="dbUser" size="8" readonly="true"/>

				<form:label path="dbPassword">DB password: </form:label>
				<form:input path="dbPassword" size="8" readonly="true"/>
			</p>

			<p>
				<form:label path="tablePrefix">Table prefix: </form:label>
				<form:input path="tablePrefix" size="8" readonly="true"/>
				<form:errors path="tablePrefix" cssClass="error" delimiter=", "/>
			</p>
			<p>
				<form:label path="jndiPool">JNDI: </form:label>
				<form:input path="jndiPool" size="7" readonly="true"/>
			</p>

		</fieldset>		
		<c:if test="${editMode}">					
			<fieldset>
				<legend>Configured extensions</legend>
				<div id="extensionsWrapper" class="box">
					<table id="extensions">
						<thead>
							<tr>
								<th>Extension</th>
								<th>Enabled</th>
								<th>Web Module</th>
								<th>Web Context</th>
							</tr>
						</thead>
						<tbody>						
							<c:forEach items="${extensions}" var="ext">									
									<c:set var="checked" value="${ext.value.enabled ? 'checked=\"checked\"' : ''}" />
									<c:set var="webmodule" value="${ext.value.webExtension ? 'checked=\"checked\"' : ''}" />
									<tr>	
										<td><label for="${ext.key}" style="float: left;"><c:out value="${ext.key}" /> </label></td>								
									<spring:bind path="extensions">
										<td><input class="extensions" type="checkbox" id="${ext.key}" ${checked} disabled="true" value="${ext.key}" name="extensions"></td>
										<td><input class="extensions" type="checkbox" id="${ext.key}" ${webmodule} disabled="true" value="${ext.key}"  name="extensions"></td>
										
										<c:choose>
											<c:when test="${ext.value.missingContextName}">
												<td><label for="${ext.value.contextName}" class="error" style="float: left;">Missing configuration for this context in current tenant</label></td>
											</c:when>
											<c:otherwise>								
												<td><label for="${ext.value.contextName}" style="float: left;"><c:out value="${ext.value.contextName}" /> </label></td>
											</c:otherwise>
										</c:choose>										
									</spring:bind>
									</tr> 
							</c:forEach>
						</tbody>
					</table>	
				</div>		
			</fieldset>
		</c:if>		
	</form:form>

	<c:if test="${editMode}">
		<div class="marginBottom">
			<c:if test="${!master}">
				<!-- Delete tenant -->				
			</c:if>
			<!-- Initialize tenant -->
			
			<c:if test="${empty ctxEnabled}" >				
					<form:form method="GET" action="${ctx}/platform/init/">
						<input class="actions" type="submit" value="Initialize tenant" ${ctxEnabled}  />
					</form:form>
			</c:if>
						
		</div>
	</c:if>
	
	<div class="clear marginBottom">&nbsp;</div>
	
</div>