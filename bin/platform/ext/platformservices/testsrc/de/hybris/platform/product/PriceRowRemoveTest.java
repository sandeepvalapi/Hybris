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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class PriceRowRemoveTest extends ServicelayerTransactionalTest
{
	@Resource
	private ProductService productService;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Test
	public void testPriceRowRemove() throws Exception
	{
		//creating test data
		createCoreData();
		createDefaultCatalog();

		final ProductModel product = productService.getProduct("testProduct0");
		Assert.assertNotNull(product);
		Assert.assertEquals(1, product.getEurope1Prices().size());

		final PriceRowModel productPriceRow = product.getEurope1Prices().iterator().next();

		final PriceRowModel globalPriceRow = modelService.create(PriceRowModel.class);
		globalPriceRow.setUnit(productService.getUnit("kg"));
		globalPriceRow.setCurrency(i18nService.getBaseCurrency());
		globalPriceRow.setPrice(Double.valueOf(2.3));
		modelService.save(globalPriceRow);

		final Collection<PriceRowModel> coll = new ArrayList<PriceRowModel>();
		coll.add(globalPriceRow);
		coll.add(productPriceRow);
		product.setEurope1Prices(coll);

		modelService.save(product);
		Assert.assertEquals(2, product.getEurope1Prices().size());
		Assert.assertNull(globalPriceRow.getProduct());
		Assert.assertEquals(product, productPriceRow.getProduct());

		//now the test part

		modelService.remove(product);
		Assert.assertTrue(modelService.isRemoved(productPriceRow));
		Assert.assertFalse(modelService.isRemoved(globalPriceRow));

	}
}
