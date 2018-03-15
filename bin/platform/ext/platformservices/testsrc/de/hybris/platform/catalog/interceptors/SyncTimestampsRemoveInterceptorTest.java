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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
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
public class SyncTimestampsRemoveInterceptorTest
{
	SyncTimestampsRemoveInterceptor remSyncTimestampsInt;

	@Mock
	private ItemSyncTimestampDao itemSyncTimestampDao;

	@Mock
	private ModelService modelService;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(interceptorContext.getModelService()).thenReturn(modelService);

		remSyncTimestampsInt = new SyncTimestampsRemoveInterceptor();
		remSyncTimestampsInt.setItemSyncTimestampDao(itemSyncTimestampDao);
	}


	@Test
	public void testRemove() throws InterceptorException
	{
		final ProductModel product = new ProductModel();
		final Integer limit = Integer.valueOf(2);
		final ItemSyncTimestampModel itemSyncTimestampModel1 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel2 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel3 = new ItemSyncTimestampModel();
		final ItemSyncTimestampModel itemSyncTimestampModel4 = new ItemSyncTimestampModel();

		remSyncTimestampsInt.setLimit(limit);

		Mockito.when(itemSyncTimestampDao.findSyncTimestampsByItem(product, limit.intValue())).thenReturn(
				Arrays.asList(itemSyncTimestampModel1, itemSyncTimestampModel2, itemSyncTimestampModel3, itemSyncTimestampModel4));



		remSyncTimestampsInt.onRemove(product, interceptorContext);

		Mockito.verify(modelService).remove(itemSyncTimestampModel1);
		Mockito.verify(modelService).remove(itemSyncTimestampModel2);
		Mockito.verify(modelService).remove(itemSyncTimestampModel3);
		Mockito.verify(modelService).remove(itemSyncTimestampModel4);
	}

	@Test
	public void testCannotRemove() throws InterceptorException
	{
		final CatalogModel catalogModel = new CatalogModel();
		remSyncTimestampsInt.onRemove(catalogModel, interceptorContext);

		Mockito.verify(itemSyncTimestampDao, Mockito.times(0)).findSyncTimestampsByItem((ItemModel) Mockito.anyObject(),
				Mockito.anyInt());
	}
}
