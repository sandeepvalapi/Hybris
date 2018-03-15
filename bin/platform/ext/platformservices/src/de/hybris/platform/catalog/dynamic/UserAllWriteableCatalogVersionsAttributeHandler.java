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
package de.hybris.platform.catalog.dynamic;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ImmutableSet;


public class UserAllWriteableCatalogVersionsAttributeHandler
		implements DynamicAttributeHandler<Collection<CatalogVersionModel>, UserModel>
{
	private CatalogVersionService catalogVersionService;
	private ModelService modelService;

	@Override
	public Collection<CatalogVersionModel> get(final UserModel model)
	{
		if (modelService.isNew(model))
		{
			final ImmutableSet.Builder<CatalogVersionModel> resultBuilder = ImmutableSet.builder();
			resultBuilder.addAll(emptyIfNull(model.getWritableCatalogVersions()));
			emptyIfNull(model.getAllGroups()).forEach(g -> resultBuilder.addAll(emptyIfNull(g.getWritableCatalogVersions())));
			return resultBuilder.build();
		}
		return catalogVersionService.getAllWritableCatalogVersions(model);
	}

	@Override
	public void set(final UserModel model, final Collection<CatalogVersionModel> catalogVersionModels)
	{
		throw new UnsupportedOperationException();
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	private static <T> Collection<T> emptyIfNull(final Collection<T> versions)
	{
		if (versions == null)
		{
			return Collections.emptyList();
		}
		return versions;
	}
}
