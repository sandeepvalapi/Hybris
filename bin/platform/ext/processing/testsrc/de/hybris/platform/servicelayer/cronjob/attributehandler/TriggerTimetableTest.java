/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.servicelayer.cronjob.attributehandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link TriggerTimetable}.
 */
@IntegrationTest
public class TriggerTimetableTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private TriggerTimetable triggerTimetable;

	@Resource
	private ModelService modelService;

	private TriggerModel trigger;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJob");
		job.setSpringId("cleanUpJobPerformable");
		modelService.save(job);

		// create a trigger who triggers every hour at 51 minutes
		trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 51 * * * ? *");
		trigger.setJob(job);
		trigger.setActive(Boolean.TRUE);
		// the interceptor will set the activationTime
		modelService.save(trigger);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.cronjob.attributehandler.TriggerTimetable#get(de.hybris.platform.cronjob.model.TriggerModel)}
	 * .
	 */
	@Test
	public void testGetTriggerTimetable()
	{
		final String timetableFromDynamicAttrHandler = triggerTimetable.get(trigger);
		final String timetableFromTriggerModel = trigger.getTimeTable();

		assertNotNull(timetableFromDynamicAttrHandler);
		assertNotNull(timetableFromTriggerModel);

		assertEquals(timetableFromDynamicAttrHandler, timetableFromTriggerModel);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.cronjob.attributehandler.TriggerTimetable#set(de.hybris.platform.cronjob.model.TriggerModel, java.lang.String)}
	 * .
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSetTriggerTimetableReadOnly()
	{
		triggerTimetable.set(trigger, "test");
		fail("Unsupported operation exception was expected (because timetable is read-only attribute) but not thrown!");
	}

}
