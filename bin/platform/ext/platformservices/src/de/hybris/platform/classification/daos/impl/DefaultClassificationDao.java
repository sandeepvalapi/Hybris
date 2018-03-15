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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueCondition;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.daos.ClassificationDao;
import de.hybris.platform.classification.impl.PossibleAttributeValue;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Flexible search backed implementation.
 */
public class DefaultClassificationDao extends AbstractItemDao implements ClassificationDao
{

	@Override
	public List<PossibleAttributeValue> findPossibleAttributeValues(final CategoryModel category,
			final Collection<ClassAttributeAssignmentModel> assignments,
			final Map<ClassAttributeAssignmentModel, Object> filteredAttributeValues)
	{
		final boolean useToCharForClob = Config.isOracleUsed() || Config.isHanaUsed();
		final Map<String, Object> filterParams = new HashMap<String, Object>();

		// We select the ClassAttributeAssignment, the value of the product feature, the unit
		// and a count of all product feature value. The latter is achieved by adding a group by statement 
		// later on
		final StringBuilder stringBuilder = new StringBuilder("SELECT {pf.classificationAttributeAssignment},");
		if (useToCharForClob)
		{
			stringBuilder.append(" to_char({pf.stringValue}),");
		}
		else
		{
			stringBuilder.append(" {pf.stringValue},");
		}
		stringBuilder.append(" {pf.unit}, COUNT(*) ");
		stringBuilder.append("FROM {").append(ProductFeatureModel._TYPECODE).append(" AS pf ");
		stringBuilder.append("JOIN ").append(ProductModel._TYPECODE).append(" ON {Product.pk} = {pf.product} } ");

		// Restricting the search to the given ClassAttributeAssignments
		stringBuilder.append("WHERE {pf.classificationAttributeAssignment} IN (?assignments) ");

		// Add a subquery that select all products with the given filter
		// This is necessary to only find attribute values contained in the filtered attributes
		final String filterQuery = createQueryToFindProductsByAttributeValues(category, filteredAttributeValues, filterParams,
				false);
		stringBuilder.append(" AND {Product.pk} IN ({{").append(filterQuery).append("}}) ");

		// Group the result to count the values
		stringBuilder.append("GROUP BY {pf.classificationAttributeAssignment} ");
		if (useToCharForClob)
		{
			stringBuilder.append(", to_char({pf.stringValue})");
		}
		else
		{
			stringBuilder.append(", {pf.stringValue}");
		}
		stringBuilder.append(", {pf.unit}");

		// Create the actual flexible search query
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.setResultClassList(Arrays.asList(Item.class, String.class, Item.class, Long.class));
		query.addQueryParameter("assignments", assignments);
		query.addQueryParameters(filterParams);
		final SearchResult<List<Object>> result = search(query);
		final List<PossibleAttributeValue> possibleAttributeValues = new ArrayList<PossibleAttributeValue>(result.getCount());
		for (final List<Object> row : result.getResult())
		{
			if (row.size() != 4)
			{
				throw new IllegalStateException("Invalid size of row: " + row.size());
			}
			final ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel) row.get(0);
			final String stringValue = (String) row.get(1);
			final Object value = convertValue(assignment, stringValue);
			final ClassificationAttributeUnitModel unit = (ClassificationAttributeUnitModel) row.get(2);
			final Long count = (Long) row.get(3);
			final PossibleAttributeValue possibleValue = new PossibleAttributeValue(assignment, value, unit, count);
			possibleAttributeValues.add(possibleValue);
		}
		return possibleAttributeValues;
	}

	private Object convertValue(final ClassAttributeAssignmentModel assignment, final String stringValue)
	{
		final String typeCode = assignment.getAttributeType().getCode();
		if (ClassificationAttributeTypeEnum.BOOLEAN.getCode().equals(typeCode))
		{
			return Boolean.valueOf(stringValue);
		}
		else if (ClassificationAttributeTypeEnum.ENUM.getCode().equals(typeCode))
		{
			//YTODO
			final Item item = JaloSession.getCurrentSession().getItem(PK.parse(stringValue));
			final Object value = load(item);
			validateParameterNotNull(value, "No such value with PK: " + stringValue);
			return value;
		}
		else if (ClassificationAttributeTypeEnum.NUMBER.getCode().equals(typeCode))
		{
			return Double.valueOf(stringValue);
		}
		else if (ClassificationAttributeTypeEnum.STRING.getCode().equals(typeCode))
		{
			return stringValue;
		}
		else if (ClassificationAttributeTypeEnum.DATE.getCode().equals(typeCode))
		{
			try
			{
				return Utilities.getDateTimeInstance().parse(stringValue);
			}
			catch (final ParseException e)
			{
				throw new IllegalArgumentException(e.getMessage(), e);
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid classifcation attribute type code: " + typeCode);
		}
	}

	/**
	 * Find products by a filter.
	 */
	@Override
	public SearchResult<ProductModel> findProductsByAttributeValues(final CategoryModel category,
			final Map<ClassAttributeAssignmentModel, Object> attributeValues, final int start, final int count)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final String query = createQueryToFindProductsByAttributeValues(category, attributeValues, params, true);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchQuery.setCount(count);
		flexibleSearchQuery.setStart(start);
		flexibleSearchQuery.setNeedTotal(true);
		return search(flexibleSearchQuery);
	}

	/**
	 * Create a query to find product by filter. Utilizes GenericQuery.
	 */
	private String createQueryToFindProductsByAttributeValues(final CategoryModel category,
			final Map<ClassAttributeAssignmentModel, Object> attributeValues, final Map<String, Object> params,
			final boolean asSubselect)
	{
		final GenericQuery genericQuery = new GenericQuery("Product");
		final GenericCondition joinCondition = GenericCondition.createJoinCondition(new GenericSearchField("link", "target"),
				new GenericSearchField("Product", "pk"));
		genericQuery.addInnerJoin("CategoryProductRelation", "link", joinCondition);

		final Collection<Category> categoryItems = getAllSources(category.getAllSubcategories(), new ArrayList<Category>());
		categoryItems.add((Category) getSource(category));

		final GenericCondition categoriesConditon = GenericCondition.createConditionForValueComparison(new GenericSearchField(
				"link", "source"), Operator.IN, categoryItems);
		genericQuery.addCondition(categoriesConditon);
		for (final Entry<ClassAttributeAssignmentModel, Object> entry : attributeValues.entrySet())
		{
			final ClassAttributeAssignment attributeAssignmentItem = getSource(entry.getKey());
			Object value = entry.getValue();
			if (value instanceof AbstractItemModel)
			{
				value = getSource(value);
			}
			genericQuery.addCondition(FeatureValueCondition.equals(attributeAssignmentItem, value));
		}
		// Some SQL databases don't like orders in subselects
		if (asSubselect)
		{
			genericQuery.addOrderBy(new GenericSearchOrderBy(new GenericSearchField("Product", "name")));
		}
		final String query = genericQuery.toFlexibleSearch(params);
		return query;
	}

	@Override
	public List<ClassificationAttributeValueModel> findAttributeValuesByCode(final String code)
	{
		final SearchResult<ClassificationAttributeValueModel> results = search(//
				"SELECT {" + ClassificationAttributeValueModel.PK + "} " // 
						+ "FROM {" + ClassificationAttributeValueModel._TYPECODE + "} " // 
						+ "WHERE {" + ClassificationAttributeValueModel.CODE + "} = ?code", //
				Collections.singletonMap(ClassificationAttributeValueModel.CODE, code));
		return results.getResult();
	}

	@Override
	public List<ClassificationAttributeUnitModel> findAttributeUnits(final ClassificationSystemVersionModel systemVersion)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", systemVersion.getPk());

		final String query = "SELECT {" + Item.PK + "} FROM {" + CatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} " + "WHERE {"
				+ ClassificationAttributeUnit.SYSTEMVERSION + "}= ?code ";

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchQuery.setCount(-1);
		flexibleSearchQuery.setStart(0);
		flexibleSearchQuery.setNeedTotal(true);
		final SearchResult<ClassificationAttributeUnitModel> results = search(flexibleSearchQuery);
		return results.getResult();
	}
}
