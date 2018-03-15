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
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


/**
 * Validates the {@link ItemSyncTimestampModel}. Throws the InterceptorException if the source item, target item or the
 * sync job is <code>null</code>.
 */
public class ItemSyncTimeStampValidator implements ValidateInterceptor
{
	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof ItemSyncTimestampModel)
		{
			final ItemSyncTimestampModel modelImpl = (ItemSyncTimestampModel) model;
			if (modelImpl.getSourceItem() == null)
			{
				throw new InterceptorException("missing source item");
			}
			if (modelImpl.getSyncJob() == null)
			{
				if (modelImpl.getSourceVersion() == null || modelImpl.getTargetVersion() == null)
				{
					throw new InterceptorException("missing source or target verison (sync job was null)");
				}
			}
			if (modelImpl.getTargetItem() == null)
			{
				throw new InterceptorException("missing target item");
			}
		}
	}
}
