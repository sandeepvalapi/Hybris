<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Access Confirmation &middot; hybris</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Le styles -->
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>

<body>
<div class="container">
    <div class="body-content">
        <div class="row">
            <div class="col-md-6">
                <h2>Authorization not granted</h2>
                <p class="lead"><c:out value="${error.getMessage()}"/></p>
            </div>
            <div class="col-md-6">
            </div>
        </div>
    </div>
</div>

</body>
</html>
