<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
    <title>Deployment</title>
    <link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href="<c:url value="/static/css/maintain/deployments.css"/>" type="text/css"
          media="screen, projection"/>
    <script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/maintain/deployments.js"/>"></script>
</head>
<body>
<div class="prepend-top span-17 colborder" id="content" data-firstFree="${firstFree}">
    <button id="toggleSidebarButton">&gt;</button>
    <div class="marginLeft marginBottom">
        <h2>Deployment</h2>

        <div id="tabs">
            <ul>
                <li><a href="#tabs-1">Types with deployment</a></li>
                <li><a href="#tabs-2">Types without deployment</a></li>
                <li><a href="#tabs-3">Deployments without type</a></li>
            </ul>

            <div id="tabs-1">
                <table id="typesWith">
                    <thead>
                    <tr>
                        <th>Typecode</th>
                        <th>Table</th>
                        <th>Type</th>
                        <th>Extension</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${typesWith}">
                        <tr title="PK: ${item.typePK} | Properties Table: ${item.propsTable} | Persistent: ${item.persistent}| Abstract: ${item.abstractValue} | Final: ${item.finalValue}">
                            <td>${item.typecode}</td>
                            <td>${item.table}</td>
                            <td>${item.type}</td>
                            <td>${item.extension}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div id="tabs-2">
                <table id="typesWithout">
                    <thead>
                    <tr>
                        <th>Type</th>
                        <th>Extension</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${typesWithout}">
                        <tr title="PK ${item.typePK}">
                            <td>${item.type}</td>
                            <td>${item.extension}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div id="tabs-3" class="marginBottom">
                <table id="deplWithout">
                    <thead>
                    <tr>
                        <th>Typecode</th>
                        <th>Table</th>
                        <th>Property table</th>
                        <th>Persistent</th>
                        <th>Abstract</th>
                        <th>Final</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${deplWithout}">
                        <tr>
                            <td>${item.typecode}</td>
                            <td>${item.table}</td>
                            <td>${item.propsTable}</td>
                            <td>${item.persistent}</td>
                            <td>${item.abstractValue}</td>
                            <td>${item.finalValue}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
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
                This page provides a list of all types which have a deployment, including types with a deployment inherited from
                their supertype. Move the pointer over a row to see more details about the type.
            </div>
        </div>
        <h3 class="caps">
            See also in the hybris Wiki
        </h3>

        <div class="box">
            <ul>
                <li><a href="${wikiTypesDeployment}" target="_blank" class="quiet">Specifying a Deployment for hybris Platform
                    Types</a></li>
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
                This page provides a list of all types with no explicit or inherited deployment. Move the pointer over a row to
                see more details about the type.
            </div>
        </div>
        <h3 class="caps">
            See also in the hybris Wiki
        </h3>

        <div class="box">
            <ul>
                <li><a href="${wikiTypesDeployment}" target="_blank" class="quiet">Specifying a Deployment for hybris Platform
                    Types</a></li>
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
                This page provides a list of all deployments not used by any type.
            </div>
        </div>
        <h3 class="caps">
            See also in the hybris Wiki
        </h3>

        <div class="box">
            <ul>
                <li><a href="${wikiTypesDeployment}" target="_blank" class="quiet">Specifying a Deployment for hybris Platform
                    Types</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>

