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

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.springframework.beans.factory.annotation.Required;


/**
 * The interceptor sets proper {@link CatalogVersionModel#ACTIVE} flags on catalog versions whenever
 * {@link CatalogModel#ACTIVECATALOGVERSION} is modified.
 * <p>
 * Prepare method throws InterceptorException in case when {@link CatalogModel#ACTIVECATALOGVERSION} was set with
 * {@link CatalogVersionModel} not belonging to the {@link CatalogModel} being modified.
 * </p>
 * 
 */
public class CatalogPrepareInterceptor implements PrepareInterceptor
{

	private CatalogService catalogService;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogModel)
		{
			final CatalogModel catalog = (CatalogModel) model;
			//if active catalog version is modified
			if (ctx.isModified(catalog, CatalogModel.ACTIVECATALOGVERSION))
			{
				final CatalogVersionModel modifiedActiveCV = catalog.getActiveCatalogVersion();
				//does it belong to the catalog?
				if (modifiedActiveCV != null && !catalog.equals(modifiedActiveCV.getCatalog()))
				{
					throw new InterceptorException("Active catalog version must belong to the catalog");
				}
				if (catalog.getCatalogVersions() != null)
				{
					for (final CatalogVersionModel catalogVersion : catalog.getCatalogVersions())
					{
						//reset all true ACTIVE flags for catalogVersions different than the modified one 
						if (Boolean.TRUE.equals(catalogVersion.getActive()) && !catalogVersion.equals(modifiedActiveCV))
						{
							catalogVersion.setActive(Boolean.FALSE);
							ctx.registerElement(catalogVersion);
						}
					}
				}
				if (modifiedActiveCV != null && Boolean.FALSE.equals(modifiedActiveCV.getActive()))
				{
					//and set only one to TRUE
					modifiedActiveCV.setActive(Boolean.TRUE);
					ctx.registerElement(modifiedActiveCV);
				}
			}

			//if DEFAULTCATALOG flag was changed to TRUE..
			if (ctx.isModified(catalog, CatalogModel.DEFAULTCATALOG) && Boolean.TRUE.equals(catalog.getDefaultCatalog()))
			{
				final CatalogModel previousCatalogModel = catalogService.getDefaultCatalog();
				//need to reset previous default catalog, but only when it is not equal to current
				if (previousCatalogModel != null && !previousCatalogModel.equals(model))
				{
					previousCatalogModel.setDefaultCatalog(Boolean.FALSE);
					ctx.registerElement(previousCatalogModel);
				}
			}
		}
	}

	@Required
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}

}
