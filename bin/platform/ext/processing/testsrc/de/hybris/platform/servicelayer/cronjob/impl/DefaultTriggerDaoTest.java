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
package de.hybris.platform.servicelayer.cronjob.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.TriggerDao;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultTriggerDaoTest extends ServicelayerBaseTest
{
	@Resource
	private TriggerDao triggerDao;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18NService;

	private long timeFirst;
	private long timeLast;

	@Before
	public void setUp() throws ParseException
	{
		TriggerModel trigger1;
		TriggerModel trigger2;

		final ServicelayerJobModel job = modelService.create(ServicelayerJobModel.class);
		job.setCode("cleanUpJob");
		job.setSpringId("cleanUpJobPerformable");
		modelService.save(job);
		trigger1 = modelService.create(TriggerModel.class);
		trigger1.setCronExpression("5 * * * * ? *");
		trigger1.setJob(job);
		trigger1.setActive(Boolean.TRUE);
		modelService.save(trigger1);
		trigger2 = modelService.create(TriggerModel.class);
		trigger2.setCronExpression("10 * * * * ? *");
		trigger2.setJob(job);
		trigger2.setActive(Boolean.TRUE);
		modelService.save(trigger2);

		final Calendar date = Calendar.getInstance();
		date.setTime(trigger1.getActivationTime());
		Assert.assertEquals(5, date.get(Calendar.SECOND));

		date.setTime(trigger2.getActivationTime());
		Assert.assertEquals(10, date.get(Calendar.SECOND));

		timeFirst = trigger1.getActivationTime().getTime();
		timeLast = trigger2.getActivationTime().getTime();

		if (timeFirst > timeLast)
		{
			timeFirst = timeLast;
			timeLast = trigger1.getActivationTime().getTime();
		}

	}

	@Test
	public void testFindActiveTriggersBoth()
	{
		final List<TriggerModel> triggerModel = triggerDao.findActiveTriggers(getCalendar(timeLast + 10));
		assertEquals("Number of activated triggers is wrong! Should be: " + 2 + " but was: " + triggerModel.size(), 2,
				triggerModel.size());
	}

	@Test
	public void testFindActiveTriggersTrigger1()
	{
		final List<TriggerModel> triggerModel = triggerDao.findActiveTriggers(getCalendar(timeLast - 10));

		assertEquals("Number of activated triggers is wrong! Should be: " + 1 + " but was: " + triggerModel.size(), 1,
				triggerModel.size());
	}

	@Test
	public void testFindActiveTriggersNone()
	{
		final List<TriggerModel> triggerModel = triggerDao.findActiveTriggers(getCalendar(timeFirst - 10));
		assertEquals("Number of activated triggers is wrong! Should be: " + 0 + " but was: " + triggerModel.size(), 0,
				triggerModel.size());
	}

	private Calendar getCalendar(final long miliseconds)
	{
		final Calendar calendar = Calendar.getInstance(i18NService.getCurrentTimeZone(), i18NService.getCurrentLocale());
		calendar.setTimeInMillis(miliseconds);

		return calendar;
	}
}
