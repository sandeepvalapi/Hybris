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

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class CatalogVersionAssert extends GenericAssert<CatalogVersionAssert, CatalogVersionModel>
{
	private final ModelService modelService;

	protected CatalogVersionAssert(final CatalogVersionModel actual)
	{
		super(CatalogVersionAssert.class, actual);
		modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	public static CatalogVersionAssert assertThat(final CatalogVersionModel catalog)
	{
		return new CatalogVersionAssert(catalog);
	}

	public CatalogVersionAssert hasNumOfProducts(final int numOfProducts)
	{
		final CatalogVersion catalog = modelService.getSource(actual);

		Assertions.assertThat(catalog.getAllProductCount()).isEqualTo(numOfProducts);

		return this;
	}

	public CatalogVersionAssert hasNumOfProductsAndReferences(final int numOfProducts, final int numOfReferences)
	{
		final CatalogVersion catalog = modelService.getSource(actual);

		final Collection<Product> allProducts = catalog.getAllProducts();

		final int productSize = allProducts.size();

		int referencesSize = 0;
		for (final Product product : allProducts)
		{
			final ProductModel productModel = modelService.get(product.getPK());
			referencesSize += CollectionUtils.size(productModel.getProductReferences());
		}

		Assertions.assertThat(productSize).isEqualTo(numOfProducts);
		Assertions.assertThat(referencesSize).isEqualTo(numOfReferences);

		return this;
	}

	public CatalogVersionAssert hasAllProductsWithPropertiesAs(final Map<String, String> values)
	{
		final CatalogVersion catalog = modelService.getSource(actual);
		final Collection<Product> allProducts = catalog.getAllProducts();

		for (final Product product : allProducts)
		{
			final ProductModel productModel = modelService.get(product.getPK());
			ProductAssert.assertThat(productModel).hasPropertyValuesAs(values);
		}

		return this;
	}

	public CatalogVersionAssert hasCounterPartProduct(final ProductModel sourceProduct)
	{
		final CatalogVersion catalogVersion = modelService.getSource(actual);
		Product tgtProduct = (Product) CatalogManager.getInstance().getCounterpartItem(modelService.getSource(sourceProduct),
				catalogVersion);
		Assertions.assertThat(tgtProduct).isNotNull();
		return this;
	}


	public CatalogVersionAssert hasCounterPartProductWithPropertiesAs(final ProductModel sourceProduct, final Map<String, String> values)
	{
		final CatalogVersion catalogVersion = modelService.getSource(actual);
		Product tgtProduct = (Product) CatalogManager.getInstance().getCounterpartItem(modelService.getSource(sourceProduct),
				catalogVersion);
		Assertions.assertThat(tgtProduct).isNotNull();
		final ProductModel productModel = modelService.get(tgtProduct);
		ProductAssert.assertThat(productModel).hasPropertyValuesAs(values);
		return this;
	}
}
