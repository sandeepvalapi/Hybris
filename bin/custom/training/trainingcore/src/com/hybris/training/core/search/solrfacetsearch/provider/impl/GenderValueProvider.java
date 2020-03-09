/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import com.hybris.training.core.model.ApparelProductModel;
import com.hybris.training.core.model.ApparelSizeVariantProductModel;
import com.hybris.training.core.model.ApparelStyleVariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class GenderValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ApparelProductModel apparelModel = getApparelProductModel(model);
		if (apparelModel == null)
		{
			return Collections.emptyList();
		}

		final List<Gender> genders = apparelModel.getGenders();

		if (genders != null && !genders.isEmpty())
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			for (final Gender gender : genders)
			{
				fieldValues.addAll(createFieldValue(gender, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(final Gender gender, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = gender.getCode();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	protected ApparelProductModel getApparelProductModel(final Object model)
	{
		Object finalModel = model;
		if (model instanceof ApparelSizeVariantProductModel)
		{
			finalModel = ((ApparelSizeVariantProductModel) finalModel).getBaseProduct();
		}

		if (finalModel instanceof ApparelStyleVariantProductModel)
		{
			finalModel = ((ApparelStyleVariantProductModel) finalModel).getBaseProduct();
		}

		if (finalModel instanceof ApparelProductModel)
		{
			return (ApparelProductModel) finalModel;
		}
		else
		{
			return null;
		}
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
