package de.hybris.platform.test

import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.core.model.user.UserGroupModel
import de.hybris.platform.servicelayer.ServicelayerBaseTest
import de.hybris.platform.servicelayer.model.ModelService

import javax.annotation.Resource;

import org.junit.Test

class GroovySampleTest extends ServicelayerBaseTest {

	@Resource
	ModelService modelService
	
	@Test
	void testCreateUser() {
		
		UserGroupModel grp1 = [uid:'Group1']
		UserGroupModel grp2 = [uid:'Group2']
		
		CustomerModel cust = [uid:'herb',name:'Herbert Schmidt',sessionLanguage:[isocode:'de'], groups:[grp1,grp2] ]
		
		modelService.save(cust)
		
		cust.groups.each {
			println "${it.uid} members: ${it.members.collect{it.uid}.join(",")}"
		}
		 
		println "User: ${cust.uid} name:${cust.name} groups:${cust.allgroups.collect{it.uid}.join(",")} lang:${cust.sessionLanguage?.isocode} pk:${cust.pk} type:${cust.itemtype}"
	}
}