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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.TriggerableJob;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class CompositeEntryJobValidateInterceptorTest
{
	private CompositeEntryJobValidateInterceptor validator;

	@Before
	public void setUp()
	{
		validator = new CompositeEntryJobValidateInterceptor();
	}


	@Test
	public void testAssignInvalidJobModel()
	{
		final CompositeEntryModel model = new CompositeEntryModel();
		model.setTriggerableJob(new JobModel());

		try
		{
			validator.onValidate(model, null);
			Assert.fail("Should be not able to assign not TriggerableJob or ServicelayerJobModel");
		}
		catch (final InterceptorException e)
		{
			//ok
		}
	}

	@Test
	public void testAssignNullModel()
	{
		final CompositeEntryModel model = new CompositeEntryModel();
		model.setTriggerableJob(null);

		try
		{
			validator.onValidate(model, null);

		}
		catch (final InterceptorException e)
		{
			Assert.fail("Should be able to assign  ServicelayerJobModel");
		}
	}


	@Test
	public void testAssignValidServicelayerJobModel()
	{
		final CompositeEntryModel model = new CompositeEntryModel();
		model.setTriggerableJob(new ServicelayerJobModel());

		try
		{
			validator.onValidate(model, null);

		}
		catch (final InterceptorException e)
		{
			Assert.fail("Should be able to assign  ServicelayerJobModel");
		}
	}


	@Test
	public void testAssignValidTriggerableJob()
	{
		final CompositeEntryModel model = new CompositeEntryModel();
		model.setTriggerableJob(new TestTriggerableJobModel());

		try
		{
			validator.onValidate(model, null);

		}
		catch (final InterceptorException e)
		{
			Assert.fail("Should be able to assign  TriggerableJob");
		}
	}

	class TestTriggerableJobModel extends JobModel implements TriggerableJob
	{

		@Override
		public CronJob newExecution()
		{
			// YTODO Auto-generated method stub
			return null;
		}

	}
}
