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
package de.hybris.platform.product.daos.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.product.daos.VariantTypeDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Collections;
import java.util.List;


/**
 * Default implementation of {@link VariantTypeModel} DAO.
 */
public class DefaultVariantTypeDao extends DefaultGenericDao<VariantTypeModel> implements VariantTypeDao
{
	/**
	 * Default constructor
	 * 
	 * @param typecode
	 *           the typecode to set.
	 * 
	 */
	public DefaultVariantTypeDao(final String typecode)
	{
		super(typecode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VariantTypeModel> findAllVariantTypes()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(//
				"SELECT {" + VariantTypeModel.PK + "} " //
						+ "FROM {" + VariantTypeModel._TYPECODE + "} " //						
						+ "ORDER BY {" + VariantTypeModel.CODE + "} ASC");

		final SearchResult<VariantTypeModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBaseProductCount(final VariantTypeModel variantType)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(//
				"SELECT COUNT({" + Item.PK + "}) " //
						+ "FROM {" + ProductModel._TYPECODE + "} " //
						+ "WHERE {" + ProductModel.VARIANTTYPE + "} =?variantType");
		query.addQueryParameter(ProductModel.VARIANTTYPE, variantType);
		query.setResultClassList(Collections.singletonList(Integer.class));
		final SearchResult<Object> result = getFlexibleSearchService().search(query);
		final Integer count = result.getResult().isEmpty() ? Integer.valueOf(0) : (Integer) result.getResult().get(0);
		return count.intValue();
	}

}
