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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;


@IntegrationTest
public class PriceRowPrepareInterceptorTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Test
	public void testPriceRowFallbackUnit()
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("sl_" + System.currentTimeMillis());
		modelService.save(catalogModel);
		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(catalogModel);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);


		final UnitModel unitModel = modelService.create(UnitModel.class);
		unitModel.setCode("unit_code");
		unitModel.setUnitType("unit_type");

		final ProductModel prodModel = modelService.create(ProductModel.class);
		prodModel.setCode("some-product-key");
		prodModel.setUnit(unitModel);
		prodModel.setCatalogVersion(cmv1);
		modelService.save(prodModel);

		final CurrencyModel currencyModel = modelService.create(CurrencyModel.class);
		currencyModel.setIsocode("isoCode");
		currencyModel.setSymbol("dollar$$");

		final PriceRowModel priceRowModel = modelService.create(PriceRowModel.class);
		priceRowModel.setCurrency(currencyModel);
		priceRowModel.setPrice(Double.valueOf(1.0));
		priceRowModel.setCatalogVersion(cmv1);
		priceRowModel.setProduct(prodModel);

		modelService.save(priceRowModel);

		Assert.assertEquals("Price Row must have the same Unit as the Product  ", priceRowModel.getUnit(), prodModel.getUnit());

	}

}
