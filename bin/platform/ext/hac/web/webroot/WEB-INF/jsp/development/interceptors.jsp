<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %> 
<html>
<head>
	<title>Platform Interceptors</title>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/development/interceptors.css"/>" type="text/css" media="screen, projection" />
		
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/development/interceptors.js"/>"></script>	
</head>
	<body>
			<div class="prepend-top span-24" id="content">
				<div class="marginLeft">
					<h2>Interceptors</h2>
					<p>This page provides an overview of the registered interceptors.</p>
					
					<div>
						<input type="text" name="typeCode" id="typeCodeSearchField" placeholder="Search for TypeCode..." />
						<input type="button" value="Show interceptors" id="typeCodeShow" />
					</div>
					
					<table id="interceptors" data-checkInterceptorInterfaces="<c:url value="/development/interceptors/interfaces"/>" 
													 data-checkInterceptedTypes="<c:url value="/development/interceptors/typeInterceptors"/>">
						<thead>
							<tr>
								<th>Name</th>
								<th>Replace</th>
								<th>Item type</th>
								<th>Order</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${interceptors}" var="interceptor">
								<tr>
									<td class="interceptorName pointer" data-interceptorName="${interceptor.name}">${interceptor.name}</td>
									<td> 
										<c:if test="${!empty interceptor.replacedByInterceptors}">
											<span class="replacingInterceptors">
												<c:forEach items="${interceptor.replacedByInterceptors}" var="replacingInterceptor">
													<c:out value="${replacingInterceptor}" /><br />
												</c:forEach>
											</span>
											<img class="overridedBy pointer" src="<c:url value="/static/img/override.png"/>" alt="override"/>
										</c:if>
									</td>
									<td class="interceptedType pointer" data-interceptedType="${interceptor.type}">${interceptor.type}</td>
									<td>${interceptor.order}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			
			<div id="dialogContainer"></div>
	</body>
</html>

