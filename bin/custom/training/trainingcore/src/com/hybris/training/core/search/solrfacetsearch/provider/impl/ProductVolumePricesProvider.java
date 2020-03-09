/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Provides value for volumePrices flag. "true" if product has volume prices, "false" otherwise.
 */
public class ProductVolumePricesProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private PriceService priceService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel product = (ProductModel) model;//this provider shall only be used with products
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final CurrencyModel sessionCurrency = i18nService.getCurrentCurrency();
		try
		{
			for (final CurrencyModel currency : indexConfig.getCurrencies())
			{
				i18nService.setCurrentCurrency(currency);
				final List<PriceInformation> prices = getPriceService().getPriceInformationsForProduct(product);
				if (prices != null && !prices.isEmpty())
				{
					addFieldValues(indexedProperty, product, fieldValues, currency);
				}
			}
		}
		finally
		{
			i18nService.setCurrentCurrency(sessionCurrency);
		}
		return fieldValues;
	}

	protected void addFieldValues(final IndexedProperty indexedProperty, final ProductModel product,
			final Collection<FieldValue> fieldValues, final CurrencyModel currency) {
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				currency.getIsocode().toLowerCase());
		final Boolean hasVolumePrices = hasVolumePrices(product);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, hasVolumePrices));
		}
	}
	
	protected Boolean hasVolumePrices(final ProductModel product)
	{
		final Set<Long> volumes = new HashSet<Long>();
		final List<PriceInformation> priceInfos = getPriceService().getPriceInformationsForProduct(product);
		for (final PriceInformation priceInfo : priceInfos)
		{
			if (priceInfo.getQualifiers().containsKey(PriceRow.MINQTD))
			{
				final Long volume = (Long) priceInfo.getQualifiers().get(PriceRow.MINQTD);
				volumes.add(volume);
			}
		}
		if (volumes.size() > 1)//one volume price (probably with minqt=1) is not taken into account
		{
			return Boolean.TRUE;
		}
		else
		{
			return Boolean.FALSE;
		}
	}

	protected PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
