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
package de.hybris.platform.catalog.job.callback.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test covers a {@link RemoveCatalogVersionsCallback} logic.
 */
@UnitTest
public class RemoveCatalogVersionsCallbackTest
{
	private RemoveCatalogVersionsCallback callback;

	@Mock
	private CatalogVersionJobDao catalogVersionJobDao;

	@Mock
	private ModelService modelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		callback = new RemoveCatalogVersionsCallback();
		callback.setCatalogVersionJobDao(catalogVersionJobDao);
		callback.setModelService(modelService);
	}

	@Test(expected = NullPointerException.class)
	public void testBeforeNullCronJob()
	{
		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();

		callback.beforeRemove(null, Arrays.asList(catalogVersionOne, catalogVersionTwo));
	}



	@Test
	public void testBefore()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = new RemoveCatalogVersionCronJobModel();

		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();

		final List<ComposedTypeModel> composedTypeList = Mockito.mock(List.class);
		Mockito.when(catalogVersionJobDao.getOrderedComposedTypes()).thenReturn(composedTypeList);

		final List<CatalogVersionModel> catalogVersionList = Arrays.asList(catalogVersionOne, catalogVersionTwo);
		Mockito.when(Integer.valueOf(catalogVersionJobDao.getItemInstanceCount(catalogVersionList, composedTypeList))).thenReturn(
				Integer.valueOf(10));

		callback.beforeRemove(cronJobModel, catalogVersionList);
		final ArgumentMatcher<RemoveCatalogVersionCronJobModel> matcher = new ArgumentMatcher<RemoveCatalogVersionCronJobModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof RemoveCatalogVersionCronJobModel))
				{
					return false;
				}
				final RemoveCatalogVersionCronJobModel model = (RemoveCatalogVersionCronJobModel) argument;
				if (model.getTotalDeleteItemCount().intValue() != 10)
				{
					return false;
				}
				return true;
			}
		};
		Mockito.verify(modelService).save(Mockito.argThat(matcher));
	}

	@Test
	public void testAfterWithResult()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = Mockito.spy(new RemoveCatalogVersionCronJobModel());

		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();

		final ImportResult resultMock = Mockito.mock(ImportResult.class);
		final ImpExMediaModel unresolvedLinesMedia = Mockito.mock(ImpExMediaModel.class);

		Mockito.when(Boolean.valueOf(resultMock.hasUnresolvedLines())).thenReturn(Boolean.TRUE);
		Mockito.when(resultMock.getUnresolvedLines()).thenReturn(unresolvedLinesMedia);

		final List<CatalogVersionModel> catalogVersionList = Arrays.asList(catalogVersionOne, catalogVersionTwo);
		final List<ComposedTypeModel> composedTypeList = Mockito.mock(List.class);

		Mockito.when(catalogVersionJobDao.getOrderedComposedTypes()).thenReturn(composedTypeList);
		//first catalog version has 10 item instance so it wont be removed
		//second catalog version has 0 item instance so it will be removed
		Mockito.when(Integer.valueOf(catalogVersionJobDao.getItemInstanceCount(catalogVersionList, composedTypeList))).thenReturn(
				Integer.valueOf(10), Integer.valueOf(0));

		callback.afterRemoved(cronJobModel, catalogVersionList, resultMock);

		Mockito.verify(modelService, Mockito.never()).remove(catalogVersionOne);
		Mockito.verify(modelService).remove(catalogVersionTwo);
		Mockito.verify(cronJobModel).setNotRemovedItems(unresolvedLinesMedia);
		final ArgumentMatcher<RemoveCatalogVersionCronJobModel> matcher = new ArgumentMatcher<RemoveCatalogVersionCronJobModel>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof RemoveCatalogVersionCronJobModel))
				{
					return false;
				}
				final RemoveCatalogVersionCronJobModel model = (RemoveCatalogVersionCronJobModel) argument;
				if (model.getNotRemovedItems() != unresolvedLinesMedia)
				{
					return false;
				}
				return true;
			}
		};
		Mockito.verify(modelService).save(Mockito.argThat(matcher));


	}

	@Test
	public void testAfterWithNullResult()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = Mockito.spy(new RemoveCatalogVersionCronJobModel());

		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();


		final List<CatalogVersionModel> catalogVersionList = Arrays.asList(catalogVersionOne, catalogVersionTwo);
		final List<ComposedTypeModel> composedTypeList = Mockito.mock(List.class);

		Mockito.when(catalogVersionJobDao.getOrderedComposedTypes()).thenReturn(composedTypeList);
		//first catalog verion has 10 item instance so it wont be removed
		//second catalog verion has 0 item instance so it will be removed
		Mockito.when(Integer.valueOf(catalogVersionJobDao.getItemInstanceCount(catalogVersionList, composedTypeList))).thenReturn(
				Integer.valueOf(10), Integer.valueOf(0));

		callback.afterRemoved(cronJobModel, catalogVersionList, null);

		Mockito.verify(modelService, Mockito.never()).remove(catalogVersionOne);
		Mockito.verify(modelService).remove(catalogVersionTwo);

		Mockito.verify(cronJobModel, Mockito.never()).setNotRemovedItems(null);
		Mockito.verify(modelService).save(cronJobModel);


	}


	@Test
	public void testDoRemoveWithEmptyResult()
	{
		final RemoveCatalogVersionCronJobModel cronJobModel = new RemoveCatalogVersionCronJobModel();

		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();

		final ImportResult resultMock = Mockito.mock(ImportResult.class);

		callback.doRemove(cronJobModel, Arrays.asList(catalogVersionOne, catalogVersionTwo), resultMock);

		Mockito.verifyZeroInteractions(modelService);
	}

	@Test
	public void testDoRemoveWithNotEmptyResult()
	{
		final ImpExImportCronJobModel impexCronJob = Mockito.mock(ImpExImportCronJobModel.class);

		final RemoveCatalogVersionCronJobModel cronJobModel = new RemoveCatalogVersionCronJobModel();

		final CatalogVersionModel catalogVersionOne = new CatalogVersionModel();
		final CatalogVersionModel catalogVersionTwo = new CatalogVersionModel();

		final ImportResult resultMock = Mockito.mock(ImportResult.class);
		Mockito.when(resultMock.getCronJob()).thenReturn(impexCronJob);
		Mockito.when(impexCronJob.getValueCount()).thenReturn(Integer.valueOf(121));

		callback.doRemove(cronJobModel, Arrays.asList(catalogVersionOne, catalogVersionTwo), resultMock);

		Mockito.verify(modelService).refresh(impexCronJob);
		Mockito.verify(impexCronJob).getValueCount();
		Mockito.verify(modelService).save(cronJobModel);

	}

}
