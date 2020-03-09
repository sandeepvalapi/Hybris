/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;
import com.hybris.training.core.model.ApparelProductModel;
import com.hybris.training.facades.product.data.GenderData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link ProductData} with genders
 */
public class ApparelProductPopulator implements Populator<ProductModel, ProductData>
{
	private Converter<Gender, GenderData> genderConverter;

	protected Converter<Gender, GenderData> getGenderConverter()
	{
		return genderConverter;
	}

	@Required
	public void setGenderConverter(final Converter<Gender, GenderData> genderConverter)
	{
		this.genderConverter = genderConverter;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target) throws ConversionException
	{
		final ProductModel baseProduct = getBaseProduct(source);

		if (baseProduct instanceof ApparelProductModel)
		{
			final ApparelProductModel apparelProductModel = (ApparelProductModel) baseProduct;
			if (CollectionUtils.isNotEmpty(apparelProductModel.getGenders()))
			{
				final List<GenderData> genders = new ArrayList<GenderData>();
				for (final Gender gender : apparelProductModel.getGenders())
				{
					genders.add(getGenderConverter().convert(gender));
				}
				target.setGenders(genders);
			}
		}
	}

	protected ProductModel getBaseProduct(final ProductModel productModel)
	{
		ProductModel currentProduct = productModel;
		while (currentProduct instanceof VariantProductModel)
		{
			final VariantProductModel variant = (VariantProductModel) currentProduct;
			currentProduct = variant.getBaseProduct();
		}
		return currentProduct;
	}
}
