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
package de.hybris.platform.classification.features;

import de.hybris.platform.catalog.ClassificationUtils;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.List;

import com.google.common.base.Preconditions;


/**
 * A product feature with its {@link FeatureValue}s and along with its {@link ClassAttributeAssignmentModel} if any.
 * This class is the service representation of {@link de.hybris.platform.catalog.jalo.classification.util.Feature}.
 */
public abstract class Feature // NOPMD
{
	protected String code;
	protected String name;
	protected final ClassAttributeAssignmentModel assignment;

	public Feature(final String code)
	{
		Preconditions.checkArgument(code != null, "Code is required for instantiation of untyped feature");
		this.code = code;
		this.assignment = null;
	}

	public Feature(final ClassAttributeAssignmentModel assignment)
	{
		Preconditions.checkArgument(assignment != null, "Assignment is required for instantiation of typed feature");
		this.assignment = assignment;
		this.code = null;
	}


	/**
	 * Gets the code of the feature computed from assignment for typed feature or direct code for untyped feature.
	 * 
	 * @see ClassificationUtils#createFeatureQualifier(ClassAttributeAssignmentModel)
	 * @return the code
	 */
	public String getCode()
	{
		if (this.code == null)
		{
			this.code = ClassificationUtils.createFeatureQualifier(this.assignment);
		}
		return this.code;
	}

	/**
	 * Returns the list of feature values. The list is unmodifiable.
	 * 
	 * @return the list of feature values
	 */
	public abstract List<FeatureValue> getValues();

	/**
	 * Convenience method to return a single value. If the value list is empty this method will return null. If the
	 * list's size is greater that one it will return the first element.
	 * 
	 * @return method to return a single value
	 */
	public abstract FeatureValue getValue();

	/**
	 * Add to this feature a {@link FeatureValue}
	 * 
	 * @param fvalue
	 *           the feature value
	 */
	public abstract void addValue(final FeatureValue fvalue);

	/**
	 * Inserts the {@link FeatureValue}at the specified index position in this list (optional operation). Shifts the
	 * element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
	 * 
	 * @param index
	 *           the index value
	 * @param fvalue
	 *           the feature value
	 */
	public abstract void addValue(final int index, final FeatureValue fvalue);

	/**
	 * Removes the first occurrence of the given {@link FeatureValue} from the feature, if it is present (optional
	 * operation). If this {@link UnlocalizedFeature} does not contain this {@link FeatureValue}, it is unchanged. More
	 * formally, removes the element with the lowest index <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt> (if such an element exists). Returns
	 * <tt>true</tt> if the feature contained the specified feature value.
	 * 
	 * @param fvalue
	 *           the FeatureValue to be removed
	 * @return true if the feature value was removed successfully
	 */
	public abstract boolean removeValue(final FeatureValue fvalue);

	/**
	 * Remove all containing {@link FeatureValue}s from the current feature.
	 */
	public abstract void removeAllValues();

	/**
	 * Clears always all {@link FeatureValue} of this Feature and add the given List with FeatureValues to this Feature.
	 * If the parameter is null the internal FeatureValue List will only be cleared (same behaviour as
	 * {@link #removeAllValues()}
	 * 
	 * @param fvalues
	 *           the list with feature values or null
	 */
	public abstract void setValues(final List<FeatureValue> fvalues);

	/**
	 * Returns a human readable description.
	 */
	@Override
	public String toString()
	{
		return String.format("%s <%s: %s>", getClass().getSimpleName(), getCode(), getValues().toString());
	}

	/**
	 * Returns the classification attribute assignment or null if untyped feature.
	 * 
	 * @return the classification attribute assignment or null if untyped feature
	 */
	public ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		return assignment;
	}

	/**
	 * Returns name of the feature. If this feature has no {@link ClassAttributeAssignmentModel} the code is returned
	 * instead.
	 * 
	 * @return the localized name of the feature
	 */
	public String getName()
	{
		if (this.name == null)
		{
			this.name = assignment == null ? this.code : assignment.getClassificationAttribute().getName();
		}
		return this.name;
	}
}
