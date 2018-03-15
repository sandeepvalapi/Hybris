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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.Collections;
import java.util.List;


/**
 * A classification attribute along with possible values. It is a part of {@link ProductFilterResult}.
 */
public class FilterAttribute implements Comparable<FilterAttribute>
{
	private final ClassAttributeAssignmentModel attributeAssignment;
	private final List<FilterAttributeValue> values;

	/**
	 * Initializes the object with an attribute assignment and possible values.
	 * 
	 * @param attributeAssignment
	 *           the attribute assignment
	 * @param possibleValues
	 *           the possible values
	 */
	public FilterAttribute(final ClassAttributeAssignmentModel attributeAssignment, final List<FilterAttributeValue> possibleValues)
	{
		super();
		this.attributeAssignment = attributeAssignment;
		this.values = possibleValues;
	}

	/**
	 * Returns the code of the attribute.
	 * 
	 * @return the code of the attribute
	 */
	public String getCode()
	{
		return attributeAssignment.getClassificationAttribute().getCode();
	}

	/**
	 * Returns the (localized) name of the attribute.
	 * 
	 * @return the name of the attribute
	 */
	public String getName()
	{
		return attributeAssignment.getClassificationAttribute().getName();
	}

	/**
	 * Returns the attribute assignment.
	 * 
	 * @return the attribute assignment
	 */
	public ClassAttributeAssignmentModel getAttributeAssignment()
	{
		return attributeAssignment;
	}

	/**
	 * Returns a list of possible attribute values.
	 * 
	 * @return the list of attribute values
	 */
	public List<FilterAttributeValue> getValues()
	{
		return Collections.unmodifiableList(values);
	}

	@Override
	public String toString()
	{
		if (attributeAssignment != null)
		{
			return getClass().getSimpleName() + " [" + attributeAssignment.getClassificationAttribute().getCode() + "]";
		}
		else
		{
			return getClass().getSimpleName() + " [<untyped>]";
		}

	}

	@Override
	public int compareTo(final FilterAttribute filterAttribute)
	{
		if (attributeAssignment != null && filterAttribute.attributeAssignment != null)
		{
			int myPosition = 0;
			int otherPosition = 0;
			if (attributeAssignment.getPosition() != null)
			{
				myPosition = attributeAssignment.getPosition().intValue();
			}
			if (filterAttribute.attributeAssignment.getPosition() != null)
			{
				otherPosition = filterAttribute.attributeAssignment.getPosition().intValue();
			}
			return (myPosition < otherPosition ? -1 : (myPosition == otherPosition ? 0 : 1));
		}
		else if (attributeAssignment == null && filterAttribute.attributeAssignment != null)
		{
			return -1;
		}
		else if (attributeAssignment != null && filterAttribute.attributeAssignment == null)
		{
			return 1;
		}
		else
		{
			return 0;
		}

	}
}
