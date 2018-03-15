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
package de.hybris.platform.catalog.references.daos.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.references.daos.ProductReferencesDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;



/**
 * Default implementation of {@link ProductReferencesDao}.
 */
public class DefaultProductReferencesDao extends DefaultGenericDao<ProductReferenceModel> implements ProductReferencesDao
{
	public DefaultProductReferencesDao()
	{
		super(ProductReferenceModel._TYPECODE);
	}

	@Override
	public List<ProductReferenceModel> findAllReferences(final ProductModel product)
	{
		ServicesUtil.validateParameterNotNull(product, "product must not be null");
		final StringBuffer sql = new StringBuffer(70);

		sql.append("SELECT {").append(ProductReferenceModel.PK).append("} ");
		sql.append("FROM {").append(ProductReferenceModel._TYPECODE).append("*} ");
		sql.append("WHERE {").append(ProductReferenceModel.TARGET).append("}=?item ");

		final SearchResult<ProductReferenceModel> result = getFlexibleSearchService().search(sql.toString(),
				Collections.singletonMap("item", product));
		return result.getResult();
	}

	@Override
	public List<ProductReferenceModel> findProductReferences(final String qualifier, final ProductModel sourceProduct,
			final ProductModel targetProduct, final ProductReferenceTypeEnum type, final Boolean active)
	{
		if (qualifier == null && sourceProduct == null && targetProduct == null && type == null)
		{
			//no point in fetching all product references in the system.
			throw new IllegalArgumentException(
					"Too few search restinctions. Please provide 'target' or 'sourceProduct' or 'targetProduct' or 'type' argument");
		}

		final Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(qualifier))
		{
			params.put(ProductReferenceModel.QUALIFIER, qualifier);
		}
		if (sourceProduct != null)
		{
			params.put(ProductReferenceModel.SOURCE, sourceProduct);
		}
		if (targetProduct != null)
		{
			params.put(ProductReferenceModel.TARGET, targetProduct);
		}
		if (type != null)
		{
			params.put(ProductReferenceModel.REFERENCETYPE, type);
		}
		if (active != null)
		{
			params.put(ProductReferenceModel.ACTIVE, active);
		}
		//"sourcePOS" is a private attribute of the ProductReferenceRelation, if this attribute would not exists the test for
		//this dao would throw a FlexibleSearch "unknown field" exception
		return find(params, SortParameters.singletonAscending(ProductReferenceModel.SOURCE + "POS"));
	}
}
