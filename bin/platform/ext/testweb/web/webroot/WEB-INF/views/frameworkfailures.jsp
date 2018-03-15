<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Test failures</title>
</head>
<body>
<div class="prepend-top span-24" id="content">
	<div class="large"><strong>Test framework failure</strong></div>

	<div id="test-results">
		<c:forEach var="testResult" items="${testResults}">
			<c:forEach var="failure" items="${testResult.frameworkFailures}">
				<div class="failure-message">
					<c:out value="${failure.message}"/>
				</div>

				<div class="failure-stacktrace small code quiet">
					<c:choose>
						<c:when test="${failure.stackTraceAvailable}">
							<c:out value="${failure.exceptionStackTraceAsString}"/>
						</c:when>
						<c:otherwise>
							No stack trace available
						</c:otherwise>
					</c:choose>
				</div>
			</c:forEach>
		</c:forEach>
	</div>
</div>
</body>
</html>