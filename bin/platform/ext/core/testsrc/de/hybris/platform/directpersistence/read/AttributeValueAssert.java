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
package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.util.ItemPropertyValue;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class AttributeValueAssert extends GenericAssert<AttributeValueAssert, SLDDataContainer.AttributeValue>
{

	public AttributeValueAssert(final SLDDataContainer.AttributeValue actual)
	{
		super(AttributeValueAssert.class, actual);
	}

	public static AttributeValueAssert assertThat(final SLDDataContainer.AttributeValue actual)
	{
		return new AttributeValueAssert(actual);
	}

	public AttributeValueAssert withValueEqualTo(final Object value)
	{
		Assertions.assertThat(actual.getValue()).isEqualTo(value);
		return this;
	}

	public <T extends ItemModel> AttributeValueAssert withReferenceValueEqualTo(final T referenceValue)
	{
		Assertions.assertThat(actual.getValue()).isInstanceOf(ItemPropertyValue.class);
		Assertions.assertThat(((ItemPropertyValue) actual.getValue()).getPK()).isEqualTo(referenceValue.getPk());
		return this;
	}
}
