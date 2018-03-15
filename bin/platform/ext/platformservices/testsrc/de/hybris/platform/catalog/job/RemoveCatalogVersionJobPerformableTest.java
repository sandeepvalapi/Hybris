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
import de.hybris.platform.catalog.job.strategy.RemoveStrategy;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test covering high level {@link RemoveCatalogVersionJobPerformable} logic.
 */
@UnitTest
public class RemoveCatalogVersionJobPerformableTest
{

	@Mock
	private RemoveStrategy<RemoveCatalogVersionCronJobModel> strategyCatalog;

	@Mock
	private RemoveStrategy<RemoveCatalogVersionCronJobModel> strategyCatalogVersion;

	private RemoveCatalogVersionJobPerformable performable;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		performable = new RemoveCatalogVersionJobPerformable()
		{
			@Override
			protected boolean isAlive(final RemoveCatalogVersionCronJobModel cronJob)
			{
				return true;
			}
		};
		performable.setRemoveCatalogStrategy(strategyCatalog);
		performable.setRemoveCatalogVersionStrategy(strategyCatalogVersion);
	}

	@Test(expected = NullPointerException.class)
	public void testPerformableNullCronJob()
	{
		performable.perform(null);
	}

	@Test
	public void testPerformableEmptyCronJob()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		final PerformResult result = performable.perform(model);

		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	@Test
	public void testPerformableEmptyCatalogVersionCronJob()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(null);
		model.setCatalog(new CatalogModel());

		final PerformResult resultAsWhole = Mockito.mock(PerformResult.class);
		Mockito.when(strategyCatalog.remove(model)).thenReturn(resultAsWhole);

		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalogVersion);
		Mockito.verify(strategyCatalog).remove(model);

		Assert.assertEquals(result, resultAsWhole);
	}

	@Test
	public void testPerformableCatalogThrowsException()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(null);
		model.setCatalog(new CatalogModel());

		Mockito.when(strategyCatalog.remove(model)).thenThrow(new SystemException(""));
		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalogVersion);
		Mockito.verify(strategyCatalog).remove(model);

		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	@Test
	public void testPerformableCatalogReturnsResult()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(null);
		model.setCatalog(new CatalogModel());

		Mockito.when(strategyCatalog.remove(model)).thenReturn(new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.PAUSED));
		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalogVersion);
		Mockito.verify(strategyCatalog).remove(model);

		Assert.assertEquals(CronJobResult.UNKNOWN, result.getResult());
		Assert.assertEquals(CronJobStatus.PAUSED, result.getStatus());
	}

	@Test
	public void testPerformableCatalogVersionThrowsException()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(new CatalogVersionModel());
		model.setCatalog(new CatalogModel());

		Mockito.when(strategyCatalogVersion.remove(model)).thenThrow(
				new SystemException("testPerformableCatalogVersionThrowsException test case"));
		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalog);
		Mockito.verify(strategyCatalogVersion).remove(model);

		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	@Test
	public void testPerformableCatalogVersionReturnsResult()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(new CatalogVersionModel());
		model.setCatalog(new CatalogModel());

		Mockito.when(strategyCatalogVersion.remove(model)).thenReturn(
				new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN));
		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalog);
		Mockito.verify(strategyCatalogVersion).remove(model);

		Assert.assertEquals(CronJobResult.UNKNOWN, result.getResult());
		Assert.assertEquals(CronJobStatus.UNKNOWN, result.getStatus());
	}



	@Test
	public void testPerformableNotEmptyCatalogVersionCronJob()
	{
		final RemoveCatalogVersionCronJobModel model = new RemoveCatalogVersionCronJobModel();
		model.setCatalogVersion(new CatalogVersionModel());
		model.setCatalog(new CatalogModel());

		final PerformResult resultAsWhole = Mockito.mock(PerformResult.class);
		Mockito.when(strategyCatalogVersion.remove(model)).thenReturn(resultAsWhole);


		final PerformResult result = performable.perform(model);

		Mockito.verifyZeroInteractions(strategyCatalog);
		Mockito.verify(strategyCatalogVersion).remove(model);

		Assert.assertEquals(result, resultAsWhole);
	}

}
