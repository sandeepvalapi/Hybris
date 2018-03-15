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
package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class ProductAssert extends GenericAssert<ProductAssert, ProductModel>
{
	private final ModelService modelService;

	protected ProductAssert(final ProductModel actual)
	{
		super(ProductAssert.class, actual);
		modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	public static ProductAssert assertThat(final ProductModel protuct)
	{
		return new ProductAssert(protuct);
	}

	public ProductAssert hasPropertyValuesAs(final Map<String, String> values)
	{
		for (final Map.Entry<String, String> entry : values.entrySet())
		{
			final Object actualValue = modelService.getAttributeValue(actual, entry.getKey());
			Assertions.assertThat(actualValue).isInstanceOf(String.class);
			Assertions.assertThat((String) actualValue).contains(entry.getValue());
		}

		return this;
	}
}
