<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Welcome to processing</title>
    <link rel="stylesheet" href="<c:url value="/static/processing-webapp.css"/>" type="text/css"
          media="screen, projection"/>
</head>
<div class="container">
    <h2>Welcome to "processing" extension</h2>

    <div>
        <strong>Task engine status: ${processEngineStatus}</strong>
    </div>
</div>
</html>