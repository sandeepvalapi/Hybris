<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="testweb" uri="/WEB-INF/testweb-custom.tld" %>
<html>
<head>
	<title>Test results</title>
</head>
<body>
<div class="span-24" id="content">
	<div id="test-results">
		<table id="test-results-table">
			<thead>
			<tr>
				<th colspan="4">JUnit Test Results</th>
			</tr>
			<tr>
				<th></th>
				<th>Test suite</th>
				<th>Overall time</th>
				<th>Result</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="overallResult" value="true"/>
			<c:forEach var="testResult" items="${testResults}">
				<c:forEach var="tesSuiteResult" items="${testResult.testResults}">
					<c:set var="testSuiteData" value="${tesSuiteResult.value}"/>
					<c:if test="${testSuiteData.overallResult == false}">
						<c:set var="overallResult" value="false"/>
					</c:if>
					<tr>
						<td><img src="<c:url value="/static/img/expand-small-silver.png" />"
									class="toggleTestCases"
									data-testCasesClass="${testSuiteData.testSuiteClass.simpleName}_testCases"/></td>
						<td>
							<a class="re-run-test"
								title="re-run test suite"
								href="<c:url value="/run/testsuites?testSuites=${testSuiteData.testSuiteClass.name}"/>">
								${testSuiteData.testSuiteClass.name}
							</a>
						</td>
						<td><testweb:duration value="${testSuiteData.elapsedTime}" format="m 'm' s 's' SS 'ms'"/></td>
						<td><testweb:boolean state="${testSuiteData.overallResult}" cssClass="overallResult"/></td>
					</tr>
					<c:forEach var="testCaseData" items="${testSuiteData.results}">
						<tr class="${testSuiteData.testSuiteClass.simpleName}_testCases testCasesContainer">
							<td colspan="2">
								<spring:eval
										expression="T(de.hybris.platform.util.SafeURLEncoder).encode(testCaseData.fullTestCaseMethod)"
										var="urlEncodedTestCaseMethod" />
								<a class="re-run-test"
									title="re-run test case"
									href="<c:url value="/run/testscases?testSuite=${testCaseData.testClass.name}&testCaseName=${urlEncodedTestCaseMethod}"  />">
								${testCaseData.testCaseMethod}
								</a>
							</td>
							<td><testweb:duration value="${testCaseData.elapsedTime}" format="m 'm' s 's' SS 'ms'"/></td>
							<td data-error="${testCaseData.failure}"
								 data-stackTrace="${testCaseData.failure.trace}">
								<c:choose>
									<c:when test="${testCaseData.ignored == true}">
										<c:out value="ignored"/>
									</c:when>
									<c:otherwise>
										<c:set var="testCaseCssClass" value="testCaseIcon_${testCaseData.successful}"/>
										<testweb:boolean state="${testCaseData.successful}" cssClass="${testCaseCssClass}"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</c:forEach>
			</tbody>
		</table>

		<span id="overallResult" data-result="${overallResult}"></span>

		<div id="errorsContainer"></div>
	</div>
</div>
</body>
</html>