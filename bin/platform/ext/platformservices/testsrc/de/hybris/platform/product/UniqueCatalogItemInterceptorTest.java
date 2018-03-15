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
package de.hybris.platform.product;


import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class UniqueCatalogItemInterceptorTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	UnitModel unit;
	CurrencyModel currency;
	CatalogModel catalog;
	CatalogVersionModel catalogVersion;

	@Before
	public void setupCatalog()
	{
		unit = modelService.create(UnitModel.class);
		unit.setCode(UUID.randomUUID().toString());
		unit.setConversion(Double.valueOf(1.0));
		unit.setUnitType(UUID.randomUUID().toString());

		currency = modelService.create(CurrencyModel.class);
		currency.setSymbol(UUID.randomUUID().toString());
		currency.setIsocode(UUID.randomUUID().toString());

		catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());
		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		catalogVersion.setCatalog(catalog);

		modelService.saveAll();
	}


	@Test
	public void dontTriggerConstraintChecksForTypesWithDisabledInterceptorRegisteredForSave()
	{
		final ProductModel product = givenProductThatTriggerUniqueCatalogItemInterceptor();
		final PriceRowModel priceRow = givenPriceRowFor(product);
		final PriceRowModel identicalPriceRow = givenPriceRowFor(product);

		// when
		modelService.saveAll(product, priceRow, identicalPriceRow);

		// then
		modelService.detachAll();
		final PriceRowModel fetchedPriceRow = modelService.get(priceRow.getPk());
		final PriceRowModel fetchedPriceRow2 = modelService.get(identicalPriceRow.getPk());

		assertThat(fetchedPriceRow.getProductMatchQualifier()).isEqualTo(product.getPk().getLong());
		assertThat(fetchedPriceRow2.getProductMatchQualifier()).isEqualTo(product.getPk().getLong());
	}

	private ProductModel givenProductThatTriggerUniqueCatalogItemInterceptor()
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCatalogVersion(catalogVersion);
		product.setCode(UUID.randomUUID().toString());
		return product;
	}

	private PriceRowModel givenPriceRowFor(final ProductModel product)
	{
		final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setCurrency(currency);
		priceRow.setMinqtd(Long.valueOf(1));
		priceRow.setNet(Boolean.TRUE);
		priceRow.setPrice(Double.valueOf(2.34));
		priceRow.setUnit(unit);
		priceRow.setProduct(product);

		return priceRow;
	}
}
