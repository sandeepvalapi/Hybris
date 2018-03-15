<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
    <title>Suspend system</title>
    <link rel="stylesheet" href="<c:url value="/static/css/monitoring/systeminfo.css"/>" type="text/css"
          media="screen, projection"/>
    <script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/suspendresume/systeminfo.js"/>"></script>
</head>
<body>
<div class="prepend-top span-17 colborder" id="content">
    <button id="toggleSidebarButton">&gt;</button>

    <div class="marginLeft">
        <div>
            <h2>System status: <span id="systemStatus"></span></h2>
        </div>
        <div id="suspendButtonContainer">
            <button id="suspend" value="Suspend"
                    data-suspendTokenUrl="<c:url value="/monitoring/suspendresume/suspendtoken"/>"
                    data-suspendUrl="<c:url value="/monitoring/suspendresume/suspend"/>">Suspend System
            </button>
        </div>
        <div id="forShutdownContainer">
            <label for="forShutdown">For shutdown</label>
            <input type="checkbox" name="forShutdown" id="forShutdown"/>
        </div>
        <div id="runningOperationsContainer">
            <h2>Running Operations</h2>
            <div id="runningOperations"
                 data-updateRunningOperationsUrl="<c:url value="/monitoring/suspendresume/status"/>">
            </div>
        </div>

    </div>

</div>
<div class="span-6 last" id="sidebar">
    <div class="prepend-top" id="recent-reviews">
        <h3 class="caps">Page description</h3>
        <div class="box">
            <div class="quiet">
                This page provides a list of running background operations as well as allows to suspend system.
                <hr/>
                <hac:note>
                    Refreshing the list can take up to 30 seconds.
                </hac:note>
            </div>
        </div>
    </div>
</div>
</body>
</html>

