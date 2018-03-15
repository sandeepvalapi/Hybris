<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Hybris junit runner</title>
</head>
<body>
<div id="mainTitle">
	Junit testweb frontend
</div>
<div id="init-system">
	<h3>Initialize System for unit tests</h3>

	<form method="post" action="<c:url value="/initialize" />">
		<fieldset>
			<input type="submit" value="Initialize"/>
			(This will create new tables with prefix junit_ and will NOT destroy your data.)
			<c:if test="${not testSystemInitialized}">
				<br />
				<strong class="highlight">Test system is not initialized! All tests which are dependent on Database will
					fail.</strong>
			</c:if>
		</fieldset>
	</form>

	<c:out value="${msg}"/>
</div>
<div class="prepend-top span-24" id="content">
	<div id="run-by-filter">
		<div class="runner-form">
			<c:url value="/run/filter" var="testFilterRunUrl"/>
			<form:form commandName="testFilter" action="${testFilterRunUrl}" method="get">
				<fieldset>
					<table>
						<thead>
						<tr>
							<th class="sectionTitle" colspan="3">Test Execution Filter Settings</th>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td colspan="3"><form:button id="filter-form-run">Run</form:button></td>
						</tr>
						<tr>
							<td>Annotations</td>
							<td>
								<form:checkbox path="testAnnotations" id="filter_unittests"
													value="de.hybris.bootstrap.annotations.UnitTest"/>
								<form:label path="testAnnotations" for="filter_unittests">Unit Tests</form:label>

								<form:checkbox path="testAnnotations" id="filter_integrationtests"
													value="de.hybris.bootstrap.annotations.IntegrationTest"/>
								<form:label path="testAnnotations" for="filter_integrationtests">Integration Tests</form:label>

								<form:checkbox path="testAnnotations" id="filter_demotests"
													value="de.hybris.bootstrap.annotations.DemoTest"/>
								<form:label path="testAnnotations" for="filter_demotests">Demo Tests</form:label>

								<form:checkbox path="testAnnotations" id="filter_performancetests"
													value="de.hybris.bootstrap.annotations.PerformanceTest"/>
								<form:label path="testAnnotations" for="filter_performancetests">Performance Tests</form:label>

								<form:checkbox path="testAnnotations" id="filter_bugprooftests"
													value="de.hybris.bootstrap.annotations.BugProofTest"/>
								<form:label path="testAnnotations" for="filter_bugprooftests">Bugproof Tests</form:label>

								<form:checkbox path="testAnnotations" id="filter_manualtests"
													value="de.hybris.bootstrap.annotations.ManualTest"/>
								<form:label path="testAnnotations" for="filter_manualtests">Manual Tests</form:label>
							</td>
						</tr>
						<tr>
							<td><form:label path="replaced">Do not execute replaced tests</form:label></td>
							<td><form:checkbox path="replaced" id="filter_replaced" value="${replaced}"/></td>
						</tr>
						<tr>
							<td><form:label path="packageName">Package filter</form:label></td>
							<td><form:input path="packageName"/></td>
						</tr>
						<tr>
							<td>Extensions</td>
							<td>
								<span class="selectAll" id="selectAllExtensions">select/deselect all</span>
								<form:checkboxes path="extensionNames" items="${extensions}" delimiter="<br />" class="extensions"/>
							</td>
						</tr>
						</tbody>
					</table>
				</fieldset>
			</form:form>
		</div>
	</div>
	<hr/>
	<input type="checkbox" value="true" id="replacedSwitch"/>Hide replaced testSuites
	<span class="selectAll" id="selectAllSuites">select/deselect all</span>
	<div id="unfiltered">
		<div id="run-by-testsuite" data-testCasesUrl="<c:url value="/loadTestCases"/>">
			<div class="runner-form">
				<c:url value="/run/testsuites" var="testCaseRunUrl"/>
				<form:form commandName="testSuite" action="${testCaseRunUrl}" method="get">
					<fieldset>
						<c:url value="/static/img/collapse-small-silver.png" var="collapse-img-url"/>
						<c:url value="/static/img/expand-small-silver.png" var="expand-img-url"/>
						<form:button id="testcase-form-run">Run</form:button>
						<table id="allTestSuites">
							<thead>
							<tr>
								<th class="sectionTitle">
									Select Test Suite(S) To Run
								</th>
							</tr>
							</thead>
							<c:forEach items="${testSuites}" var="testClass">
								<tr>
									<td>
										<img src="<c:url value="/static/img/expand-small-silver.png"  />"
											 id="test-cases-toggle_${testClass.name}"
											 class="test-cases-toggle" data-testSuite="${testClass.name}"
											 data-contentContainerId="testCasesFor_${testClass.simpleName}"/>

										<form:checkbox path="testSuites" class="testSuite" id="testClass_${testClass.name}"
													   value="${testClass.name}"
													   data-contentContainerId="testCasesFor_${testClass.simpleName}"/>
										<form:label path="testSuites" for="testClass_${testClass.name}"
													data-contentContainerId="testCasesFor_${testClass.simpleName}">
											<c:out value="${testClass.name}" />
										</form:label>
							<span id="testCasesFor_${testClass.simpleName}">
								<!-- Ajax loaded content -->
							</span>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</fieldset>
				</form:form>
			</div>
		</div>
	</div>
	<div id="filtered">
		<div id="run-by-testsuite" data-testCasesUrl="<c:url value="/loadTestCases"/>">
			<div class="runner-form">
				<c:url value="/run/testsuites" var="testCaseRunUrl"/>
				<form:form commandName="testSuite" action="${testCaseRunUrl}" method="get">
					<fieldset>
						<c:url value="/static/img/collapse-small-silver.png" var="collapse-img-url"/>
						<c:url value="/static/img/expand-small-silver.png" var="expand-img-url"/>
						<form:button id="testcase-form-run">Run</form:button>
						<table id="filteredTestSuites">
							<thead>
							<tr>
								<th class="sectionTitle">
									Select Test Suite(S) To Run
								</th>
							</tr>
							</thead>
							<c:forEach items="${filteredTestSuites}" var="testClass">
								<tr>
									<td>
										<img src="<c:url value="/static/img/expand-small-silver.png"  />"
											 id="filtered-test-cases-toggle_${testClass.name}"
											 class="test-cases-toggle" data-testSuite="${testClass.name}"
											 data-contentContainerId="testCasesFor_${testClass.simpleName}"/>

										<form:checkbox path="testSuites" class="filteredTestSuite" id="filtered-testClass_${testClass.name}"
													   value="${testClass.name}"
													   data-contentContainerId="testCasesFor_${testClass.simpleName}"/>
										<form:label path="testSuites" for="filtered-testClass_${testClass.name}"
													data-contentContainerId="testCasesFor_${testClass.simpleName}">
											<c:out value="${testClass.name}" />
										</form:label>
							<span id="filteredtestCasesFor_${testClass.simpleName}">
								<!-- Ajax loaded content -->
							</span>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</fieldset>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>