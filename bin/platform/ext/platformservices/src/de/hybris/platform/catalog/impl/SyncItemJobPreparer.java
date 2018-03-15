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

import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.apache.commons.lang.StringUtils;


/**
 * {@link PrepareInterceptor} for the {@link SyncItemJobModel}. Makes manual adjustments to code attribute to match the
 * custom creation logic inside {@link SyncItemJob}. This is necessary since unique key checks catch before the actual
 * jalo logic is being triggered!
 */
@SuppressWarnings("deprecation")
public class SyncItemJobPreparer implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		final SyncItemJobModel sijm = (SyncItemJobModel) model;
		final String code = sijm.getCode();
		if(StringUtils.isBlank(code))
		{
			final CatalogVersionModel fromCV = sijm.getSourceVersion();
			final CatalogVersionModel toCV = sijm.getTargetVersion();
			if (fromCV != null && toCV != null)
			{
				sijm.setCode("Sync " + fromCV.getCatalog().getId() + ":" + fromCV.getVersion() + " -> " + toCV.getCatalog().getId()
						+ ":" + toCV.getVersion());
			}
		}
	}

}
