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

import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;


/**
 * A possible value for a classifcation attribute. Part of a {@link FilterAttribute}. Comes with information on how many
 * products with this values exist within the set of filtered products. If this value was part of the product filter the
 * method {@link #isFiltered()} will return true.
 */
public class FilterAttributeValue implements Comparable<FilterAttributeValue>
{

	private final Object value;
	private final long count;
	private final ClassificationAttributeUnitModel unit;
	boolean filtered;

	/**
	 * Initialize this instance.
	 * 
	 * @param value
	 *           the value of the attribute
	 * @param count
	 *           the number of filtered products with this attribute value
	 * @param unit
	 *           the unit of the value
	 * @param filtered
	 *           true if this attribute value was part of the filter
	 */
	public FilterAttributeValue(final Object value, final long count, final ClassificationAttributeUnitModel unit,
			final boolean filtered)
	{
		super();
		this.value = value;
		this.count = count;
		this.unit = unit;
		this.filtered = filtered;
	}

	/**
	 * The actual value.
	 * 
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * The number of filtered products with this value.
	 * 
	 * @return the number of filtered products with this value
	 */
	public long getCount()
	{
		return count;
	}

	/**
	 * The unit of this value.
	 * 
	 * @return the unit of this value
	 */
	public ClassificationAttributeUnitModel getUnit()
	{
		return unit;
	}

	/**
	 * Returns true if this value was part of the product filter.
	 * 
	 * @return true if this value was part of the product filter
	 */
	public boolean isFiltered()
	{
		return filtered;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [" + value + " ( " + count + ")]";
	}

	@Override
	public int compareTo(final FilterAttributeValue filterAttributeValue)
	{
		if (value == null && filterAttributeValue.value == null)
		{
			return 0;
		}
		else if (value == null && filterAttributeValue.value != null)
		{
			return -1;
		}
		else if (value != null && filterAttributeValue.value == null)
		{
			return 1;
		}
		else if (value instanceof Comparable)
		{
			return ((Comparable) value).compareTo(filterAttributeValue.value);
		}
		else
		{
			return 0;
		}
	}
}
