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
package de.hybris.platform.category.daos.impl;

import de.hybris.platform.category.daos.CatalogTypeDao;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CatalogTypeDao}.
 */
public class DefaultCatalogTypeDao implements CatalogTypeDao
{
	private DefaultGenericDao<ComposedTypeModel> composedTModelGenericDao;

	@Override
	public List<ComposedTypeModel> findAllCatalogVersionAwareTypes()
	{
		return composedTModelGenericDao.find(Collections.singletonMap(ComposedTypeModel.CATALOGITEMTYPE, Boolean.TRUE));
	}

	@Required
	public void setComposedTModelGenericDao(final DefaultGenericDao<ComposedTypeModel> composedTModelGenericDao)
	{
		this.composedTModelGenericDao = composedTModelGenericDao;
	}

}
