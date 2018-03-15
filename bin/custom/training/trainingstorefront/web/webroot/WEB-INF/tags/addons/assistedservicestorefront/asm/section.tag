<%@ taglib prefix="asm" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/asm"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<c:set var="contains" value="false" />
<c:forEach var="fragment" items="${section.fragments}">
	<c:if test="${
			fragment.id == 'profileLoyaltyFragment' or 
			fragment.id == 'profileSalesStatsFragment' or 
			fragment.id == 'profileTechnologyUsedFragment'
		}">
		<c:set var="contains" value="true" />
	</c:if>
</c:forEach>


<div class="asm__section">

	<c:if test="${contains}">
		<div class="asm__customer360-overview-info">
			<div class="row">
				<c:forEach items="${section.fragments}" var="fragment">
					<c:if test="${
									fragment.id == 'profileLoyaltyFragment' or 
									fragment.id == 'profileSalesStatsFragment' or 
									fragment.id == 'profileTechnologyUsedFragment'
								}">
						<div class="col-sm-4">
							<asm:fragment id="${fragment.id}" title="${fragment.title}" sectionId="${section.id}"/>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</c:if>

	<c:forEach items="${section.fragments}" var="fragment">
		<c:if test="${
						not (
							fragment.id == 'profileLoyaltyFragment' or 
							fragment.id == 'profileSalesStatsFragment' or 
							fragment.id == 'profileTechnologyUsedFragment'
						)
					}">
			<asm:fragment id="${fragment.id}" title="${fragment.title}" sectionId="${section.id}"/>
		</c:if>
	</c:forEach>

</div>
