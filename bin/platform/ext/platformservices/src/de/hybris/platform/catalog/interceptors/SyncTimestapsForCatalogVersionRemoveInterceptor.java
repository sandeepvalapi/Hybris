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

import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Removes such {@link ItemSyncTimestampModel}s for which currently removed {@link CatalogVersionModel} is either
 * {@link ItemSyncTimestampModel#SOURCEVERSION} or {@link ItemSyncTimestampModel#TARGETVERSION}.
 */
public class SyncTimestapsForCatalogVersionRemoveInterceptor implements RemoveInterceptor
{

	private ItemSyncTimestampDao itemSyncTimestampDao;


	private Integer limit = Integer.valueOf(1000);

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogVersionModel)
		{
			List<ItemSyncTimestampModel> records = null;

			do
			{
				records = itemSyncTimestampDao.findSyncTimestampsByCatalogVersion((CatalogVersionModel) model, limit.intValue());

				for (final ItemSyncTimestampModel itemSyncTimestamp : records)
				{
					ctx.getModelService().remove(itemSyncTimestamp);
				}
			}
			while (records.size() == limit.intValue());
		}
	}

	@Required
	public void setItemSyncTimestampDao(final ItemSyncTimestampDao itemSyncTimestampDao)
	{
		this.itemSyncTimestampDao = itemSyncTimestampDao;
	}


	public void setLimit(final Integer limit)
	{
		this.limit = limit;
	}
}
