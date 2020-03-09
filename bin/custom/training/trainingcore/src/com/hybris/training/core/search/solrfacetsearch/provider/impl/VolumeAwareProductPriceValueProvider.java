/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * {@link FieldValueProvider} for prices. Supports multi-currencies.<br>
 * The list of prices is loaded for the anonymous user and current catalog version. <br>
 */
public class VolumeAwareProductPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private static final Logger LOG = Logger.getLogger(FieldValueProvider.class.getName());

	private FieldNameProvider fieldNameProvider;
	private PriceService priceService;
	private UserService userService;
	private SessionService sessionService;
	private CommonI18NService commonI18NService;
	private Comparator<PriceInformation> priceComparator;
	private CatalogVersionService catalogVersionService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		try
		{
			checkModel(model);

			final Collection<CatalogVersionModel> filteredCatalogVersions = filterCatalogVersions(
					getCatalogVersionService().getSessionCatalogVersions());
			if (indexConfig.getCurrencies().isEmpty())
			{
				final List<PriceInformation> prices = new ArrayList<PriceInformation>();
				sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public void executeWithoutResult()
					{
						getCatalogVersionService().setSessionCatalogVersions(filteredCatalogVersions);
						prices.addAll(priceService.getPriceInformationsForProduct((ProductModel) model));
					}
				}, userService.getAnonymousUser());

				processPricesWithEmptyCurrencies(indexedProperty, fieldValues, prices);
			}
			else
			{
				for (final CurrencyModel currency : indexConfig.getCurrencies())
				{
					final List<PriceInformation> prices = new ArrayList<PriceInformation>();
					sessionService.executeInLocalView(new SessionExecutionBody()
					{
						@Override
						public void executeWithoutResult()
						{
							getCatalogVersionService().setSessionCatalogVersions(filteredCatalogVersions);
							commonI18NService.setCurrentCurrency(currency);
							prices.addAll(priceService.getPriceInformationsForProduct((ProductModel) model));
						}
					}, userService.getAnonymousUser());

					processPricesForCurrency(indexedProperty, fieldValues, currency, prices);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Failed to generate volume price", e);
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + this.getClass().getName(), e);
		}
		return fieldValues;
	}

	protected void checkModel(final Object model) throws FieldValueProviderException
	{
		if (!(model instanceof ProductModel))
		{
			throw new FieldValueProviderException("Cannot evaluate price of non-product item");
		}
	}

	protected void processPricesForCurrency(final IndexedProperty indexedProperty, final Collection<FieldValue> fieldValues,
			final CurrencyModel currency, final List<PriceInformation> prices) throws FieldValueProviderException
	{
		if (!prices.isEmpty())
		{
			List<String> rangeNameList;
			Collections.sort(prices, priceComparator);
			final Double value = Double.valueOf(prices.get(0).getPriceValue().getValue());
			rangeNameList = getRangeNameList(indexedProperty, value, currency.getIsocode());
			final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty,
					currency.getIsocode().toLowerCase());
			addFieldValues(fieldValues, rangeNameList, value, fieldNames);
		}
	}

	protected void processPricesWithEmptyCurrencies(final IndexedProperty indexedProperty,
			final Collection<FieldValue> fieldValues, final List<PriceInformation> prices) throws FieldValueProviderException
	{
		if (!prices.isEmpty())
		{
			List<String> rangeNameList;
			Collections.sort(prices, priceComparator);
			final PriceInformation price = prices.get(0);
			final Double value = Double.valueOf(price.getPriceValue().getValue());
			rangeNameList = getRangeNameList(indexedProperty, value);
			final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty,
					price.getPriceValue().getCurrencyIso());
			addFieldValues(fieldValues, rangeNameList, value, fieldNames);
		}
	}

	protected void addFieldValues(final Collection<FieldValue> fieldValues, final List<String> rangeNameList, final Double value,
			final Collection<String> fieldNames)
	{
		for (final String fieldName : fieldNames)
		{
			if (rangeNameList.isEmpty())
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
			else
			{
				for (final String rangeName : rangeNameList)
				{
					fieldValues.add(new FieldValue(fieldName, rangeName == null ? value : rangeName));
				}
			}
		}
	}

	protected Collection<CatalogVersionModel> filterCatalogVersions(final Collection<CatalogVersionModel> sessionCatalogVersions)
	{
		final List<CatalogVersionModel> result = new ArrayList<CatalogVersionModel>(sessionCatalogVersions.size());

		for (final CatalogVersionModel catalogVersion : sessionCatalogVersions)
		{
			if (!(catalogVersion instanceof ClassificationSystemVersionModel)
					&& !(catalogVersion.getCatalog() instanceof ContentCatalogModel))
			{
				result.add(catalogVersion);
			}
		}

		return result;
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

	protected PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected Comparator<PriceInformation> getPriceComparator()
	{
		return priceComparator;
	}

	@Required
	public void setPriceComparator(final Comparator<PriceInformation> priceComparator)
	{
		this.priceComparator = priceComparator;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}
