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
package de.hybris.platform.classification.filter;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A product filter defined in terms of a category and a number classification attribute code/value pairs. Also you can
 * provide a limit and offset. A filter must have a category but may have zero attribute/value pairs.
 * 
 * 
 * @see de.hybris.platform.classification.ClassificationService#getProductsByFilter(ProductFilter)
 */
public class ProductFilter
{

	private final CategoryModel category;
	private final Map<String, Object> attributes;
	private int start = 0;
	private int count = -1;

	/**
	 * Initialize the filter with a category. The category must not be null.
	 * 
	 * @param category
	 *           the filtered category
	 * @throws IllegalArgumentException
	 *            when the category is null
	 */
	public ProductFilter(final CategoryModel category)
	{
		super();
		validateParameterNotNull(category, "category must not be null");
		this.category = category;
		this.attributes = new HashMap<String, Object>();
	}

	/**
	 * Convenience method to set a filtered attribute by {@link ClassAttributeAssignmentModel}.
	 * 
	 * @see #setAttribute(String, Object)
	 * @param assignment
	 *           the assigment of the attribute
	 * @param value
	 *           the attribute value
	 */
	public void setAttribute(final ClassAttributeAssignmentModel assignment, final Object value)
	{
		setAttribute(assignment.getClassificationAttribute(), value);
	}

	/**
	 * Convenience method to set a filtered attribute by {@link ClassificationAttributeModel}.
	 * 
	 * @see #setAttribute(String, Object)
	 * @param attribute
	 *           the attribute
	 * @param value
	 *           the attribute value
	 */
	public void setAttribute(final ClassificationAttributeModel attribute, final Object value)
	{
		setAttribute(attribute.getCode(), value);
	}

	/**
	 * Sets a filtered attribute. The attribute is given by its code. The value must either be of the attribute's type or
	 * may be a string. See the ClassificationService's
	 * {@link de.hybris.platform.classification.ClassificationService#getProductsByFilter(ProductFilter)
	 * getProductsByFilter} method for more information on how value are converted
	 * 
	 * @see de.hybris.platform.classification.ClassificationService#getProductsByFilter(ProductFilter)
	 * @param attributeCode
	 *           the code of the filter attribute
	 * @param value
	 *           the value of the filter attribute
	 */
	public void setAttribute(final String attributeCode, final Object value)
	{
		attributes.put(attributeCode, value);
	}

	/**
	 * Returns all filter attributes. Keys are the attribute's code, value are the attribute values
	 * 
	 * @return the filter attributes
	 */
	public Map<String, Object> getAttributes()
	{
		return Collections.unmodifiableMap(attributes);
	}

	/**
	 * Returns the category of this filter
	 * 
	 * @return the category of this filter
	 */
	public CategoryModel getCategory()
	{
		return category;
	}

	/**
	 * Return the start index of the filter result. 0 is the first index number.
	 * 
	 * @return the start index of the filter result
	 */
	public int getStart()
	{
		return start;
	}

	/**
	 * Set the start index of the filter result. 0 is the first index number.
	 * 
	 * @param start
	 *           the start index of the filter result
	 */
	public void setStart(final int start)
	{
		this.start = start;
	}

	/**
	 * Returns the maximum amount of items in the filter result. -1 denotes no maximum.
	 * 
	 * @return the maximum amount of filered items
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Sets the maximum amount of items in the filter result. -1 denotes no maximum.
	 * 
	 * @param count
	 *           the maximum amount of filered items
	 */
	public void setCount(final int count)
	{
		this.count = count;
	}

}
