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
package de.hybris.platform.catalog.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SyncTimestampsForCatalogVersionRemoveInterceptorTest
{
	private SyncTimestapsForCatalogVersionRemoveInterceptor syncTimestapsForCatalogVersionRemoveInterceptor;

	@Mock
	private ItemSyncTimestampDao itemSyncTimestampDao;

	@Mock
	private ModelService modelService;

	@Mock
	private InterceptorContext context;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		syncTimestapsForCatalogVersionRemoveInterceptor = new SyncTimestapsForCatalogVersionRemoveInterceptor();

		syncTimestapsForCatalogVersionRemoveInterceptor.setItemSyncTimestampDao(itemSyncTimestampDao);

		Mockito.when(context.getModelService()).thenReturn(modelService);
	}


	@Test
	public void testRemove() throws InterceptorException
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		final Integer limit = Integer.valueOf(2);
		final ItemSyncTimestampModel itemSyncTimestampModel1 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel2 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel3 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel4 = new ItemSyncTimestampModel();

		syncTimestapsForCatalogVersionRemoveInterceptor.setLimit(limit);

		Mockito.when(itemSyncTimestampDao.findSyncTimestampsByCatalogVersion(catalogVersion, limit.intValue())).thenReturn(
				Arrays.asList(itemSyncTimestampModel1, itemSyncTimestampModel2, itemSyncTimestampModel3, itemSyncTimestampModel4));



		syncTimestapsForCatalogVersionRemoveInterceptor.onRemove(catalogVersion, context);

		Mockito.verify(modelService).remove(itemSyncTimestampModel1);
		Mockito.verify(modelService).remove(itemSyncTimestampModel2);
		Mockito.verify(modelService).remove(itemSyncTimestampModel3);
		Mockito.verify(modelService).remove(itemSyncTimestampModel4);
	}


}
