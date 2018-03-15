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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.core.PK;


/**
 * A single feature value of a {@link Feature}. It contains a value, a description and - if the jalo item is a
 * {@link de.hybris.platform.catalog.jalo.classification.util.TypedFeature} - a {@link ClassificationAttributeUnitModel}
 * . This class is the representation of {@link de.hybris.platform.catalog.jalo.classification.util.FeatureValue}.
 * 
 */
public class FeatureValue
{
	private Object value;
	private String description;
	private ClassificationAttributeUnitModel unitModel;
	private PK productFeaturePK;

	/**
	 * Initializes this featurevalue with an object.
	 * 
	 * @param value
	 *           the value
	 * 
	 * @throws IllegalArgumentException
	 *            if the value is null
	 */
	public FeatureValue(final Object value)
	{
		validateParameterNotNull(value, "value must not be null!");
		this.value = value;
		this.description = null;
		this.unitModel = null;
	}

	/**
	 * Initializes this featurevalue with an object.
	 * 
	 * @param value
	 *           the value
	 * @param description
	 *           a descrition text, can be null
	 * @param unitmodel
	 *           an unitmodel, can be null
	 * @throws IllegalArgumentException
	 *            if the value is null
	 */
	public FeatureValue(final Object value, final String description, final ClassificationAttributeUnitModel unitmodel)
	{
		validateParameterNotNull(value, "value must not be null!");
		this.value = value;
		this.description = description;
		this.unitModel = unitmodel;
	}

	/**
	 * Initializes this featurevalue with an object.
	 * 
	 * @param value
	 *           the value
	 * @param description
	 *           a descrition text, can be null
	 * @param unitmodel
	 *           an unitmodel, can be null
	 * @param productFeaturePK
	 *           the product feature pk
	 * @throws IllegalArgumentException
	 *            if the value is null
	 */
	public FeatureValue(final Object value, final String description, final ClassificationAttributeUnitModel unitmodel,
			final PK productFeaturePK)
	{
		validateParameterNotNull(value, "value must not be null!");
		this.value = value;
		this.description = description;
		this.unitModel = unitmodel;
		this.productFeaturePK = productFeaturePK;
	}

	@Override
	public String toString()
	{
		return "FeatureValue[value:" + this.value + " description:" + this.description + " unit:" + this.unitModel + "]";
	}

	public void setValue(final Object value)
	{
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public void setUnit(final ClassificationAttributeUnitModel unitmodel)
	{
		this.unitModel = unitmodel;
	}

	public ClassificationAttributeUnitModel getUnit()
	{
		return unitModel;
	}

	public PK getProductFeaturePk()
	{
		return productFeaturePK;
	}

	public void setProductFeaturePk(final PK productFeaturePK)
	{
		this.productFeaturePK = productFeaturePK;
	}
}
