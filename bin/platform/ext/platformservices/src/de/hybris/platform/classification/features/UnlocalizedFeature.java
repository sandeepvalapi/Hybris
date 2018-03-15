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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * A product feature with its {@link FeatureValue}s and along with its {@link ClassAttributeAssignmentModel} if any.
 */
public class UnlocalizedFeature extends Feature
{
	private final List<FeatureValue> values;

	/**
	 * Instantiates a new unlocalized typed feature.
	 * 
	 * @param assignment
	 *           the assignment
	 * @param values
	 *           the values
	 */
	public UnlocalizedFeature(final ClassAttributeAssignmentModel assignment, final List<FeatureValue> values)
	{
		super(assignment);
		if (values == null)
		{
			this.values = new ArrayList<FeatureValue>();
		}
		else
		{
			this.values = new ArrayList<FeatureValue>(values);
		}
	}

	/**
	 * Instantiates a new unlocalized typed feature.
	 * 
	 * @param assignment
	 *           the assignment
	 * @param values
	 *           the values
	 */
	public UnlocalizedFeature(final ClassAttributeAssignmentModel assignment, final FeatureValue... values)
	{
		this(assignment, Lists.newArrayList(values));
	}

	/**
	 * Instantiates a new unlocalized untyped feature.
	 * 
	 * @param code
	 *           the code
	 * @param values
	 *           the values
	 */
	public UnlocalizedFeature(final String code, final List<FeatureValue> values)
	{
		super(code);
		if (values == null)
		{
			this.values = new ArrayList<FeatureValue>();
		}
		else
		{
			this.values = new ArrayList<FeatureValue>(values);
		}
	}

	/**
	 * Instantiates a new unlocalized untyped feature.
	 * 
	 * @param code
	 *           the code
	 * @param values
	 *           the values
	 */
	public UnlocalizedFeature(final String code, final FeatureValue... values)
	{
		this(code, Lists.newArrayList(values));
	}

	@Override
	public List<FeatureValue> getValues()
	{
		return Collections.unmodifiableList(values);
	}

	@Override
	public FeatureValue getValue()
	{
		if (values.isEmpty())
		{
			return null;
		}
		else
		{
			return values.get(0);
		}
	}

	@Override
	public void addValue(final FeatureValue fvalue)
	{
		Preconditions.checkArgument(fvalue != null, "fvalue is null");
		values.add(fvalue);
	}

	@Override
	public void addValue(final int index, final FeatureValue fvalue)
	{
		Preconditions.checkArgument(fvalue != null, "fvalue is null");
		Preconditions.checkArgument(index <= values.size() && index >= 0, "index is not in range of: 0 and " + values.size());
		values.add(index, fvalue);
	}

	@Override
	public boolean removeValue(final FeatureValue fvalue)
	{
		Preconditions.checkArgument(fvalue != null, "fvalue is null");
		return values.remove(fvalue);
	}

	@Override
	public void removeAllValues()
	{
		values.clear();
	}

	@Override
	public void setValues(final List<FeatureValue> fvalues)
	{
		Preconditions.checkArgument(fvalues != null, "fvalues is null");
		this.values.clear();
		this.values.addAll(fvalues);
	}
}
