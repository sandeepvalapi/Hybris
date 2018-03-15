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
package de.hybris.platform.catalog.job;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * Test covering {@link CompositeJobPerformable} usage.
 */
@UnitTest
public class CompositeJobPerformableTest
{

	@Mock
	private CronJobService cronJobService;

	@Mock
	private ModelService modelService;

	private CompositeJobPerformable performable;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		performable = new CompositeJobPerformable();

		performable.setCronJobService(cronJobService);
		performable.setModelService(modelService);
	}


	@Test
	public void testEmptyComposite()
	{
		final CompositeCronJobModel rootCronJob = new CompositeCronJobModel();
		rootCronJob.setCode("RootComposite");
		rootCronJob.setCompositeEntries(Collections.EMPTY_LIST);

		performable.perform(rootCronJob);
	}

	@Test
	public void testFewCronJobEntriesComposite()
	{

		final CronJobModel firstNestedCronJob = Mockito.spy(new CronJobModel());
		firstNestedCronJob.setCode("firstNestedCronJob");

		final CompositeEntryModel firstEntry = new CompositeEntryModel();
		firstEntry.setCode("firstEntry");
		firstEntry.setExecutableCronJob(firstNestedCronJob);

		final CronJobModel secondNestedCronJob = Mockito.spy(new CronJobModel());
		secondNestedCronJob.setCode("secondNestedCronJob");

		final CompositeEntryModel secondEntry = new CompositeEntryModel();
		secondEntry.setCode("firstEntry");
		secondEntry.setExecutableCronJob(secondNestedCronJob);

		final List<CompositeEntryModel> compositeEntries = Arrays.asList(firstEntry, secondEntry);

		Mockito.when(Boolean.valueOf(cronJobService.isRunning(firstNestedCronJob))).thenReturn(Boolean.TRUE, Boolean.TRUE,
				Boolean.FALSE);
		Mockito.when(Boolean.valueOf(cronJobService.isRunning(secondNestedCronJob))).thenReturn(Boolean.TRUE, Boolean.FALSE);

		final CompositeCronJobModel rootCronJob = new CompositeCronJobModel();
		rootCronJob.setCode("RootComposite");
		rootCronJob.setCompositeEntries(compositeEntries);

		final PerformResult result = performable.perform(rootCronJob);

		Assert.assertEquals(result.getResult(), CronJobResult.SUCCESS);
		Assert.assertEquals(result.getStatus(), CronJobStatus.FINISHED);

		Mockito.verify(cronJobService, Mockito.times(3)).isRunning(firstNestedCronJob);
		Mockito.verify(cronJobService, Mockito.times(2)).isRunning(secondNestedCronJob);

		Mockito.verify(cronJobService).performCronJob(firstNestedCronJob, true);
		Mockito.verify(cronJobService).performCronJob(secondNestedCronJob, true);

	}

	@Test
	public void tesTwoServicelayerJobsWithFactoriesAsEntries()
	{
		final CronJobModel cronJobSpy = Mockito.spy(new CronJobModel());

		final CronJobFactory factory = Mockito.mock(CronJobFactory.class);

		//one job for all
		final ServicelayerJobModel triggerableJob = new ServicelayerJobModel();
		triggerableJob.setCode("one and only servicelayerjob ");

		//first entry
		final CompositeEntryModel firstEntry = new CompositeEntryModel();
		firstEntry.setCode("firstEntry");
		firstEntry.setTriggerableJob(triggerableJob);

		//second entry
		final CompositeEntryModel secondEntry = new CompositeEntryModel();
		secondEntry.setCode("secondEntry");
		secondEntry.setTriggerableJob(triggerableJob);

		//root
		final CompositeCronJobModel rootCronJob = new CompositeCronJobModel();
		rootCronJob.setCode("RootComposite");
		rootCronJob.setCompositeEntries(Arrays.asList(firstEntry, secondEntry));

		//all jobs have a factory
		Mockito.when(cronJobService.getCronJobFactory(triggerableJob)).thenReturn(factory);

		//all factory calls return some cronjob
		Mockito.when(factory.createCronJob(triggerableJob)).thenReturn(cronJobSpy);

		final PerformResult result = performable.perform(rootCronJob);

		Assert.assertEquals(result.getResult(), CronJobResult.SUCCESS);
		Assert.assertEquals(result.getStatus(), CronJobStatus.FINISHED);


	}

	@Test
	public void tesTwoServicelayerJobsAsEntriesSecondHasNoFactory()
	{
		final CronJobModel cronJobSpy = Mockito.spy(new CronJobModel());

		final CronJobFactory factory = Mockito.mock(CronJobFactory.class);

		//one job for all
		final ServicelayerJobModel triggerableJob = new ServicelayerJobModel();
		triggerableJob.setCode("one and only servicelayerjob ");

		//first entry
		final CompositeEntryModel firstEntry = new CompositeEntryModel();
		firstEntry.setCode("firstEntry");
		firstEntry.setTriggerableJob(triggerableJob);

		//second entry
		final CompositeEntryModel secondEntry = new CompositeEntryModel();
		secondEntry.setCode("secondEntry");
		secondEntry.setTriggerableJob(triggerableJob);

		//root
		final CompositeCronJobModel rootCronJob = new CompositeCronJobModel();
		rootCronJob.setCode("RootComposite");
		rootCronJob.setCompositeEntries(Arrays.asList(firstEntry, secondEntry));


		final Stack<CronJobFactory> factoriesStack = new Stack();
		factoriesStack.add(factory);

		//all jobs have a factory
		Mockito.when(cronJobService.getCronJobFactory(triggerableJob)).thenAnswer(new Answer<CronJobFactory>()
		{
			@Override
			public CronJobFactory answer(final InvocationOnMock invocation) throws Throwable
			{
				try
				{
					return factoriesStack.pop();
				}
				catch (final EmptyStackException e)
				{
					throw new SystemException("No factory");
				}
			}
		});

		//all factory calls return some cronjob
		Mockito.when(factory.createCronJob(triggerableJob)).thenReturn(cronJobSpy);
		//creating trigger always succeeds
		final PerformResult result = performable.perform(rootCronJob);

		Assert.assertEquals(result.getResult(), CronJobResult.ERROR);
		Assert.assertEquals(result.getStatus(), CronJobStatus.FINISHED);
	}
}
