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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.daos.ClassificationDao;


/**
 * Value class to hold a row of the result of
 * {@link ClassificationDao#findPossibleAttributeValues(de.hybris.platform.category.model.CategoryModel, java.util.Collection, java.util.Map)}
 * .
 */
public class PossibleAttributeValue
{

	private final ClassAttributeAssignmentModel assignment;
	private final Object value;
	private final ClassificationAttributeUnitModel unit;
	private final Long count;

	/**
	 * Initializes the object with all values.
	 * 
	 * @param assignment
	 *           the class attribute assigment
	 * @param value
	 *           the actual value
	 * @param unit
	 *           the unit (may be null)
	 * @param count
	 *           the number of occurences of this value
	 */
	public PossibleAttributeValue(final ClassAttributeAssignmentModel assignment, final Object value,
			final ClassificationAttributeUnitModel unit, final Long count)
	{
		this.assignment = assignment;
		this.value = value;
		this.unit = unit;
		this.count = count;
	}

	public ClassAttributeAssignmentModel getAssignment()
	{
		return assignment;
	}

	public Object getValue()
	{
		return value;
	}

	/**
	 * Gets the unit (might be null).
	 */
	public ClassificationAttributeUnitModel getUnit()
	{
		return unit;
	}

	/**
	 * The number of occurrences of this value.
	 * 
	 * @return the number of occurrences of this value
	 */
	public Long getCount()
	{
		return count;
	}
}
