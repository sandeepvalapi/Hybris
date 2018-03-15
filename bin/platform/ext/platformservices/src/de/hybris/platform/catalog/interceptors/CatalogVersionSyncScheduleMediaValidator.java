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

import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncScheduleMediaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


public class CatalogVersionSyncScheduleMediaValidator implements ValidateInterceptor<CatalogVersionSyncScheduleMediaModel>
{
	@Override
	public void onValidate(final CatalogVersionSyncScheduleMediaModel model, final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model != null && model.getCronjob() == null)
		{
			throw new InterceptorException("mssing sync cronjob for creating a new "
					+ CatalogVersionSyncScheduleMediaModel._TYPECODE);
		}
	}
}
