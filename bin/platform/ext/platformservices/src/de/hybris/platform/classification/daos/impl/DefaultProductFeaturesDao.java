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
package de.hybris.platform.classification.daos.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.daos.ProductFeaturesDao;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ProductFeaturesDao}.
 */
public class DefaultProductFeaturesDao implements ProductFeaturesDao
{
	private static final Logger LOG = Logger.getLogger(DefaultProductFeaturesDao.class);
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<List<ItemModel>> findProductFeaturesByProductAndAssignment(final ProductModel product,
			final List<ClassAttributeAssignmentModel> assignments, final List<ProductFeatureModel> excludes)
	{
		final SearchResult<List<ItemModel>> searchResult = flexibleSearchService.search(getQuery(product, assignments, excludes));
		return searchResult.getResult();
	}

	@Override
	public List<List<ItemModel>> findProductFeaturesByProductAndAssignment(final ProductModel product,
			final List<ClassAttributeAssignmentModel> assignments)
	{
		final SearchResult<List<ItemModel>> searchResult = flexibleSearchService.search(getQuery(product, assignments, null));
		return searchResult.getResult();
	}

	@Override
	public List<Integer> getProductFeatureMaxValuePosition(final ProductModel product,
			final ClassAttributeAssignmentModel assignment)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT max({").append(ProductFeatureModel.VALUEPOSITION).append("}) ");
		builder.append("FROM {").append(ProductFeatureModel._TYPECODE).append("} WHERE {");
		builder.append(ProductFeatureModel.PRODUCT).append("}=?product").append(" AND {");
		if (assignment == null)
		{
			builder.append(ProductFeatureModel.CLASSIFICATIONATTRIBUTEASSIGNMENT).append("} IS NULL");
		}
		else
		{
			builder.append(ProductFeatureModel.CLASSIFICATIONATTRIBUTEASSIGNMENT).append("}=?assignment");
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(builder.toString());
		fQuery.setResultClassList(Collections.singletonList(Integer.class));
		fQuery.addQueryParameter("product", product);
		if (assignment != null)
		{
			fQuery.addQueryParameter("assignment", assignment);
		}

		final SearchResult<Integer> result = flexibleSearchService.search(fQuery);

		return result.getResult();
	}

	private FlexibleSearchQuery getQuery(final ProductModel product, final List<ClassAttributeAssignmentModel> assignments,
			final List<ProductFeatureModel> excludes)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final StringBuilder builder = new StringBuilder();

		builder.append("SELECT {").append(ProductFeatureModel.PK)
				.append("},{" + ProductFeatureModel.CLASSIFICATIONATTRIBUTEASSIGNMENT + "} ");
		builder.append("FROM {").append(ProductFeatureModel._TYPECODE).append("} ");
		builder.append("WHERE {").append(ProductFeatureModel.PRODUCT).append("}=?product AND ");
		builder.append("{").append(ProductFeatureModel.CLASSIFICATIONATTRIBUTEASSIGNMENT).append("} ");

		params.put("product", product.getPk());

		if (isUntyped(assignments))
		{
			builder.append(" IS NULL ");
		}
		else
		{
			if (assignments.size() == 1)
			{
				builder.append(" = ?assignment ");
				params.put("assignment", assignments.iterator().next().getPk());
			}
			else
			{
				builder.append(" IN ( ?assignments ) ");
				final List<PK> _assignments = new ArrayList<PK>();
				for (final ClassAttributeAssignmentModel assignment : assignments)
				{
					_assignments.add(assignment.getPk());
				}
				params.put("assignments", _assignments);
			}
		}

		if (excludes != null && !excludes.isEmpty())
		{
			builder.append(" AND {").append(ProductFeatureModel.PK).append("} ");
			if (excludes.size() == 1)
			{
				builder.append("<> ?excludes ");
				params.put("excludes", excludes.iterator().next().getPk());
			}
			else
			{
				builder.append(" NOT IN ( ?excludes )");
				final ArrayList _excludes = new ArrayList<PK>();
				for (final ProductFeatureModel exclude : excludes)
				{
					_excludes.add(exclude.getPk());
				}
				params.put("excludes", _excludes);
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Sql query: [" + builder.toString() + "], Params: [" + params + "]");
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(builder.toString());
		fQuery.addQueryParameters(params);
		fQuery.setResultClassList(Arrays.asList(ProductFeatureModel.class, ClassAttributeAssignmentModel.class));


		return fQuery;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	private boolean isUntyped(final List<ClassAttributeAssignmentModel> assignments)
	{
		return CollectionUtils.isEmpty(assignments);
	}

}
