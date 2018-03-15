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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import org.springframework.beans.factory.annotation.Required;


/**
 * When removing {@link CatalogModel} not allow to remove one with is marked as default (
 * {@link CatalogModel#DEFAULTCATALOG}).
 */
public class DefaultCatalogRemoveInterceptor implements RemoveInterceptor
{

	private L10NService l10nService;

	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogModel)
		{
			final CatalogModel catalog = (CatalogModel) model;
			if ((catalog.getDefaultCatalog() != null) && (catalog.getDefaultCatalog().booleanValue()))
			{
				throw new InterceptorException(l10nService.getLocalizedString("error.catalog.removing_default_catalog"));
			}
		}

	}

	@Required
	public void setL10nService(final L10NService l10nService)
	{
		this.l10nService = l10nService;
	}

}
