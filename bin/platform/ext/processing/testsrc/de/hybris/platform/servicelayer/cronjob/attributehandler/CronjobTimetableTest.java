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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.MoveMediaCronJobModel;
import de.hybris.platform.cronjob.model.MoveMediaJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link CronjobTimetable}.
 */
@IntegrationTest
public class CronjobTimetableTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private CronjobTimetable cronjobTimetable;

	@Resource
	private ModelService modelService;

	private MoveMediaCronJobModel cronjob;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{

		cronjob = new MoveMediaCronJobModel();
		cronjob.setCode("test");

		final MoveMediaJobModel job = new MoveMediaJobModel();
		job.setCode("test");
		modelService.save(job);

		cronjob.setJob(job);
		modelService.save(cronjob);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.cronjob.attributehandler.CronjobTimetable#get(de.hybris.platform.cronjob.model.CronJobModel)}
	 * .
	 */
	@Test
	public void testGetCronjobTimetableWithoutTriggers()
	{
		final String timetableFromDynamicAttrHandler = cronjobTimetable.get(cronjob);
		final String timetableFromCronJobModel = cronjob.getTimeTable();

		assertNotNull(timetableFromDynamicAttrHandler);
		assertNotNull(timetableFromCronJobModel);

		assertEquals(timetableFromDynamicAttrHandler, timetableFromCronJobModel);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.cronjob.attributehandler.CronjobTimetable#get(de.hybris.platform.cronjob.model.CronJobModel)}
	 * .
	 */
	@Test
	public void testGetCronjobTimetableWithTriggers()
	{

		assertTrue(CollectionUtils.isEmpty(cronjob.getTriggers()));

		// create a trigger who triggers every hour at 51 minutes
		final TriggerModel trigger = modelService.create(TriggerModel.class);
		trigger.setCronExpression("0 51 * * * ? *");
		trigger.setActive(Boolean.TRUE);
		trigger.setCronJob(cronjob);

		modelService.save(trigger);

		modelService.refresh(cronjob);

		assertTrue(CollectionUtils.isNotEmpty(cronjob.getTriggers()));

		final String timetableFromDynamicAttrHandler = cronjobTimetable.get(cronjob);
		final String timetableFromCronJobModel = cronjob.getTimeTable();

		assertNotNull(timetableFromDynamicAttrHandler);
		assertNotNull(timetableFromCronJobModel);

		assertEquals(timetableFromDynamicAttrHandler, timetableFromCronJobModel);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.cronjob.attributehandler.CronjobTimetable#set(de.hybris.platform.cronjob.model.CronJobModel, java.lang.String)}
	 * .
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSetCronjobTimetableReadOnly()
	{
		cronjobTimetable.set(cronjob, "test");
		fail("Unsupported operation exception was expected (because timetable is read-only attribute) but not thrown!");
	}
}
