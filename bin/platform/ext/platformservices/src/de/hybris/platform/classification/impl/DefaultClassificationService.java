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
package de.hybris.platform.classification.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.classification.*;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.daos.ClassificationDao;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.filter.FilterAttribute;
import de.hybris.platform.classification.filter.FilterAttributeValue;
import de.hybris.platform.classification.filter.ProductFilter;
import de.hybris.platform.classification.filter.ProductFilterResult;
import de.hybris.platform.classification.strategy.LoadStoreFeaturesStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Required;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Implementation of the {@link ClassificationService} backed by Jalo API an flexible search.
 */
public class DefaultClassificationService extends AbstractBusinessService implements ClassificationService
{
	private ClassificationDao classificationDao;
	private ProductService productService;
	private FormatFactory formatFactory;

	private LoadStoreFeaturesStrategy loadStoreFeaturesStrategy;
	private ClassificationClassesResolverStrategy classResolverStrategy;


	/**
	 * Uses the Jalo feature API to load feature and then converts them.
	 * 
	 * @deprecated since ages
	 */
	@Deprecated
	@Override
	public Feature getFeature(final ItemModel item, final ClassAttributeAssignmentModel assignment)
	{
		if (item instanceof ProductModel)
		{
			return getFeature((ProductModel) item, assignment);
		}
		return null;
	}

	@Override
	public Feature getFeature(final ProductModel product, final ClassAttributeAssignmentModel assignment)
	{
		Preconditions.checkArgument(product != null, "product can't be null");
		Preconditions.checkArgument(assignment != null, "assignment can't be null");
		final FeatureList featureList = getFeatures(product);
		return featureList.getFeatureByAssignment(assignment);
	}


	@Override
	public FeatureList getFeatures(final ProductModel product)
	{
		Preconditions.checkArgument(product != null, "product can't be null");
		return new FeatureList(loadStoreFeaturesStrategy.loadFeatures(getAssignmentsForProduct(product), product));
	}

	private List<ClassAttributeAssignmentModel> getAssignmentsForProduct(final ProductModel product)
	{
		final Set<ClassificationClassModel> classificationClasses = classResolverStrategy.resolve(product);
		return classResolverStrategy.getAllClassAttributeAssignments(classificationClasses);
	}


	@Override
	public void setFeature(final ProductModel product, final Feature feature)
	{
		Preconditions.checkArgument(feature != null, "feature can't be null");
		Preconditions.checkArgument(product != null, "product can't be null");

		loadStoreFeaturesStrategy.storeFeatures(product, Collections.singletonList(feature));
	}

	@Override
	public void setFeatures(final ProductModel product, final FeatureList modelFeatureList)
	{
		Preconditions.checkArgument(modelFeatureList != null, "feature list can't be null");
		Preconditions.checkArgument(product != null, "product can't be null");

		loadStoreFeaturesStrategy.storeFeatures(product, modelFeatureList.getFeatures());
	}

	@Override
	public void replaceFeatures(final ProductModel product, final FeatureList modelFeatureList)
	{
		Preconditions.checkArgument(modelFeatureList != null, "feature list can't be null");
		Preconditions.checkArgument(product != null, "product can't be null");

		loadStoreFeaturesStrategy.replaceFeatures(getAssignmentsForProduct(product), product, modelFeatureList.getFeatures());
	}

	@Override
	public ProductFilterResult getProductsByFilter(final ProductFilter filter) throws UnknownIdentifierException,
			AmbiguousIdentifierException
	{
		final SearchResult<ProductModel> result;

		final Map<String, Object> attributeFilter = filter.getAttributes();
		final CategoryModel category = filter.getCategory();
		validateParameterNotNull(category, "No category in filter");

		// Use only classification attributes that are assigned as searchable
		final Collection<ClassAttributeAssignmentModel> assignments = getSearchableAssignmentsByCategory(category);
		// Re-map the filter using ClassAttributeAssignmentModels instead of attribute codes as keys
		final Map<ClassAttributeAssignmentModel, Object> attributeValues = new HashMap<ClassAttributeAssignmentModel, Object>(
				attributeFilter.size());

		// If the filter is empty simple return all products in a category
		if (attributeFilter.isEmpty())
		{
			result = productService.getProducts(category, filter.getStart(), filter.getCount());
		}
		else
		{
			// The filter map contains a map of attribute code to value. We now convert this mapping to a
			// ClassAttributeAssignmentModel/value mapping
			for (final Entry<String, Object> entry : attributeFilter.entrySet())
			{
				final String code = entry.getKey();
				Object value = entry.getValue();
				final ClassAttributeAssignmentModel assignment = findAssignmentWithCode(assignments, code);
				// If we cannot find an assignment with this code throw an exception
				if (assignment == null)
				{
					throw new UnknownIdentifierException("No attribute with code " + code + " found in category with code "
							+ category.getCode());
				}
				value = convertFilterValue(assignment, value);
				attributeValues.put(assignment, value);
			}
			// Now perform the actual search
			result = classificationDao
					.findProductsByAttributeValues(category, attributeValues, filter.getStart(), filter.getCount());
		}
		// Now find out what remaining attribute values are valid
		final List<FilterAttribute> possibleAttributes;
		if (assignments.isEmpty())
		{
			// If there are no classifcation attributes for this category there aren't any attribute values
			possibleAttributes = Collections.emptyList();
		}
		else
		{
			// Find the set of possible attributes
			possibleAttributes = getFilterAttributes(category, assignments, attributeValues);
		}
		return new ProductFilterResult(result.getResult(), possibleAttributes, result.getTotalCount());
	}

	/**
	 * Returns a list of list of valid attribute values for a given filter.
	 */
	private List<FilterAttribute> getFilterAttributes(final CategoryModel category,
			final Collection<ClassAttributeAssignmentModel> assignments,
			final Map<ClassAttributeAssignmentModel, Object> attributeValues)
	{
		// Get the database result
		final List<PossibleAttributeValue> dbResult = classificationDao.findPossibleAttributeValues(category, assignments,
				attributeValues);
		// Now map each ClassAttributeAssignmentModel to a list of FilterAttributeValues
		final Map<ClassAttributeAssignmentModel, List<FilterAttributeValue>> valuesMap = new HashMap<ClassAttributeAssignmentModel, List<FilterAttributeValue>>();
		for (final PossibleAttributeValue possibleAttributeValue : dbResult)
		{
			final ClassAttributeAssignmentModel assignment = possibleAttributeValue.getAssignment();
			final Object value = possibleAttributeValue.getValue();
			final boolean filtered = value.equals(attributeValues.get(assignment));
			final FilterAttributeValue attributeValue = new FilterAttributeValue(value, possibleAttributeValue.getCount()
					.longValue(), possibleAttributeValue.getUnit(), filtered);
			List<FilterAttributeValue> attributeValueList = valuesMap.get(assignment);
			if (attributeValueList == null)
			{
				attributeValueList = new ArrayList<FilterAttributeValue>();
				valuesMap.put(assignment, attributeValueList);
			}
			attributeValueList.add(attributeValue);
		}
		// Now create new FilterAttribute instances from the map
		final List<FilterAttribute> results = new ArrayList<FilterAttribute>(valuesMap.size());
		for (final Entry<ClassAttributeAssignmentModel, List<FilterAttributeValue>> entry : valuesMap.entrySet())
		{
			final ClassAttributeAssignmentModel assignment = entry.getKey();
			final List<FilterAttributeValue> values = entry.getValue();
			Collections.sort(values);
			final FilterAttribute filterAttribute = new FilterAttribute(assignment, values);
			results.add(filterAttribute);
		}
		Collections.sort(results);
		return results;

	}

	/**
	 * Convert strings to the type expected by the attribute.
	 */
	private Object convertFilterValue(final ClassAttributeAssignmentModel assignment, final Object obj)
			throws UnknownIdentifierException, AmbiguousIdentifierException
	{
		Object returnobj = obj;
		if (obj instanceof String)
		{
			final ClassificationAttributeTypeEnum type = assignment.getAttributeType();
			switch (type)
			{
				case BOOLEAN:
					//TODO not tested yet!
					returnobj = Boolean.valueOf((String) obj);
					break;
				case ENUM:
					returnobj = findAttributeValueByCode((String) obj);
					validateParameterNotNull(returnobj, "No such attribute value: null");
					break;
				case NUMBER:
					//TODO not tested yet!
					returnobj = Double.valueOf((String) obj);
					break;
				case STRING:
					returnobj = obj;
					break;
				case DATE:
					//TODO not tested yet!
					final DateFormat dateFormat = formatFactory.createDateTimeFormat(DateFormat.DEFAULT, DateFormat.DEFAULT);
					try
					{
						returnobj = dateFormat.parse((String) obj);
					}
					catch (final ParseException e)
					{
						throw new IllegalArgumentException("Invalid date: " + obj);
					}
					break;
			}
		}
		return returnobj;
	}

	private ClassAttributeAssignmentModel findAssignmentWithCode(final Collection<ClassAttributeAssignmentModel> assignments,
			final String code)
	{
		for (final ClassAttributeAssignmentModel assignment : assignments)
		{
			if (assignment.getClassificationAttribute().getCode().equals(code))
			{
				return assignment;
			}
		}
		return null;
	}

	private ClassificationAttributeValueModel findAttributeValueByCode(final String code) throws UnknownIdentifierException,
			AmbiguousIdentifierException
	{
		final List<ClassificationAttributeValueModel> values = classificationDao.findAttributeValuesByCode(code);
		if (values.isEmpty())
		{
			throw new UnknownIdentifierException("No such attribute with code: " + code);
		}
		else if (values.size() > 1)
		{
			throw new AmbiguousIdentifierException("No unique attribute with code: " + code);
		}
		return values.get(0);
	}

	/**
	 * Uses the CatalogManager to find searchable assignments.
	 */
	private Collection<ClassAttributeAssignmentModel> getSearchableAssignmentsByCategory(final CategoryModel category)
	{
		final Category categoryItem = getModelService().getSource(category);
		final List<ClassificationClass> classes = CatalogManager.getInstance().getClassificationClasses(categoryItem);
		final Collection<ClassAttributeAssignmentModel> result = new LinkedHashSet<ClassAttributeAssignmentModel>();
		for (final ClassificationClass clazz : classes)
		{
			getModelService().getAll(clazz.getSearchableAttributeAssignments(), result);
		}
		return result;
	}

	@Override
	public Collection<ClassificationAttributeUnitModel> getAttributeUnits(final ClassificationSystemVersionModel systemVersion)
	{
		return classificationDao.findAttributeUnits(systemVersion);
	}

	@Override
	public FeatureList getFeatures(final ProductModel product, final List<ClassAttributeAssignmentModel> assignments)
	{
		Preconditions.checkArgument(product != null, "product can't be null");
		Preconditions.checkArgument(assignments != null, "assignments can't be null");
		return new FeatureList(loadStoreFeaturesStrategy.loadFeatures(assignments, product));
	}

	@Required
	public void setClassificationDao(final ClassificationDao classificationDao)
	{
		this.classificationDao = classificationDao;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Required
	public void setFormatFactory(final FormatFactory formatFactory)
	{
		this.formatFactory = formatFactory;
	}

	@Required
	public void setLoadStoreFeaturesStrategy(final LoadStoreFeaturesStrategy loadStoreFeaturesStrategy)
	{
		this.loadStoreFeaturesStrategy = loadStoreFeaturesStrategy;
	}

	@Required
	public void setClassResolverStrategy(final ClassificationClassesResolverStrategy classResolverStrategy)
	{
		this.classResolverStrategy = classResolverStrategy;
	}
}
