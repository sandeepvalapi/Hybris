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
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class JobPerformableGenericTypeValidatorTest
{
	private JobPerformableGenericTypeValidator validator;
	//

	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private CronJobService cronJobService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		validator = Mockito.spy(new JobPerformableGenericTypeValidator());
		validator.setCronJobService(cronJobService);
	}

	@Test
	public void testNotServicelayerJob() throws InterceptorException
	{

		final JobModel job = new JobModel();

		final CronJobModel cronJob = new CronJobModel();
		cronJob.setJob(job);

		validator.onValidate(cronJob, interceptorContext);

		Mockito.verify(cronJobService, Mockito.never()).getPerformable(Mockito.any(ServicelayerJobModel.class));
		Mockito.verifyZeroInteractions(cronJobService);

	}

	@Test
	public void testNullServicelayerJobPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		final CronJobModel cronJob = new CronJobModel();
		cronJob.setJob(job);

		//some not an Object performable generic
		Mockito.doReturn(null).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
			Assert.fail("Validation for the CronJob where assigned performable is not a compatible type should fail ");
		}
		catch (final NullPointerException e)
		{
			//fine here
		}

	}


	//class CronJobModel

	@Test
	public void testCronJobModelWithTheSameSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new CronJobModel();
		cronJob.setJob(job);

		//specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new BasePerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);

		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the CronJobModel where assigned performable BasePerformable is more specific type CronJobModel should  not fail ");
		}

	}

	@Test
	public void testCronJobModelWithMoreSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new CronJobModel();
		cronJob.setJob(job);

		//specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new AdvancedPerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
			Assert.fail("Validation for the CronJobModel where assigned performable AdvancedPerformable is more specific type AdvancedCronJobModel should  fail ");
		}
		catch (final InterceptorException e)
		{
			//fine here
		}

	}

	public void testCronJobModelWithEvenMoreSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new CronJobModel();
		cronJob.setJob(job);

		//specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new MoreAdvancedPerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
			Assert.fail("Validation for the CronJobModel where assigned performable MoreAdvancedPerformable is more specific type AdvancedCronJobModel should  fail ");
		}
		catch (final InterceptorException e)
		{
			//fine here
		}
	}


	//AdvancedCronJobModel
	@Test
	public void testAdvancedCronJobModelWithLessSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new AdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new BasePerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the AdvancedCronJobModel where assigned performable BasePerformable is less specific type CronJobModel and should not fail ");
		}

	}

	@Test
	public void testAdvancedCronJobModelWithTheSameSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new AdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new AdvancedPerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);

		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the AdvancedCronJobModel where assigned performable AdvancedPerformable is less specific type AdvancedCronJobModel and should not fail ");
		}
	}

	@Test
	public void testAdvancedCronJobModelWithMoreSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new AdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new MoreAdvancedPerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
			Assert.fail("Validation for the AdvancedCronJobModel where assigned performable MoreAdvancedPerformable is less specific type MoreAdvancedCronJobModel and should  fail ");
		}
		catch (final InterceptorException e)
		{
			//ok here
		}

	}


	//MoreAdvancedCronJobModel
	@Test
	public void testMoreAdvancedCronJobModelWithEvenLessSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new MoreAdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new BasePerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the MoreAdvancedCronJobModel where assigned performable BasePerformable is less specific type CronJobModel and should not fail ");
		}

	}

	@Test
	public void testMoreAdvancedCronJobModelWithLessSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new MoreAdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new AdvancedPerformable()).when(cronJobService).getPerformable(job);
		try
		{
			validator.onValidate(cronJob, interceptorContext);
		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the MoreAdvancedCronJobModel where assigned performable AdvancedPerformable is less specific type AdvancedCronJobModel and should not fail ");
		}

	}

	@Test
	public void testMoreAdvancedCronJobModelWithTheSameSpecificPerformable() throws InterceptorException
	{

		final ServicelayerJobModel job = new ServicelayerJobModel();
		job.setSpringId("someSpringId");

		//cronjob model
		final CronJobModel cronJob = new MoreAdvancedCronJobModel();
		cronJob.setJob(job);

		//less specific performable generic as AdvancedCronJobModel
		Mockito.doReturn(new MoreAdvancedPerformable()).when(cronJobService).getPerformable(job);

		try
		{
			validator.onValidate(cronJob, interceptorContext);
		}
		catch (final InterceptorException e)
		{
			Assert.fail("Validation for the MoreAdvancedCronJobModel where assigned performable MoreAdvancedPerformable is less specific type MoreAdvancedCronJobModel and should not fail ");
		}

	}


	/**
	 * we create a artificial performable hierarchy (top most basic, bottom most specific )
	 * <p>
	 * BasePerformable<CronJobModel>
	 * </p>
	 * <p>
	 * AdvancedPerformable<AdvancedCronJobModel>
	 * </p>
	 * <p>
	 * MoreAdvancedPerformable<MoreAdvancedCronJobModel>
	 * </p>
	 */
	class BasePerformable<T extends CronJobModel> implements JobPerformable<T>
	{

		@Override
		public boolean isAbortable()
		{
			return false;
		}

		@Override
		public boolean isPerformable()
		{
			return false;
		}

		@Override
		public PerformResult perform(final T cronJob)
		{
			// YTODO Auto-generated method stub
			return null;
		}
	}

	class AdvancedPerformable extends BasePerformable<AdvancedCronJobModel>
	{
		//
	}

	class MoreAdvancedPerformable extends BasePerformable<MoreAdvancedCronJobModel>
	{
		//
	}

	/**
	 * we create a artificial CronJobModel hierarchy (top most basic, bottom most specific )
	 * <p>
	 * CronJobModel
	 * </p>
	 * <p>
	 * AdvancedCronJobModel
	 * </p>
	 * <p>
	 * MoreAdvancedCronJobModel
	 * </p>
	 */
	class AdvancedCronJobModel extends CronJobModel
	{
		//
	}

	class MoreAdvancedCronJobModel extends AdvancedCronJobModel
	{
		//
	}

}
