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
package de.hybris.platform.catalog;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


public class ProductFeaturesAndReferencesCascadingRemovalTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;
	private CatalogVersionModel source;

	@Before
	public void setUp()
	{
		final CatalogModel catalog = createCatalog("TestCatalog");
		source = createCatalogVersion(catalog, "staged");
	}

	@Test
	public void shouldRemoveFeaturesOfRemovedProductAndReferencesToThisProduct() throws Exception
	{
		// given
		final ProductModel product1 = createProduct("MyCode-" + 1, "MyEan-" + 1, source);

		final ProductModel productToBeRemoved = createProduct("MyCode-" + 2, "MyEan-" + 2, source);
		final ProductFeatureModel feature1 = createProductFeatureForProduct(productToBeRemoved, "feature1", 0);
		final ProductFeatureModel feature2 = createProductFeatureForProduct(productToBeRemoved, "feature2", 1);
		productToBeRemoved.setFeatures(Lists.newArrayList(feature1, feature2));
		modelService.save(productToBeRemoved);

		createProductReference("battery charger", product1, productToBeRemoved);

		assertThat(product1.getProductReferences()).hasSize(1);

		// when
		modelService.remove(productToBeRemoved);

		// then
		modelService.refresh(product1);
		assertThat(product1.getProductReferences()).isEmpty();
		assertDoesNotExist(feature1);
		assertDoesNotExist(feature2);
	}

	private void assertDoesNotExist(final ProductFeatureModel feature1)
	{
		try
		{
			modelService.get(feature1.getPk());
			Assert.fail("Expected exception");
		}
		catch (final ModelLoadingException e)
		{
			// expected
		}
	}

	private ProductFeatureModel createProductFeatureForProduct(final ProductModel product, final String value,
			final int valuePosition)
	{
		final ProductFeatureModel productFeatureModel = modelService.create(ProductFeatureModel.class);
		productFeatureModel.setProduct(product);
		productFeatureModel.setValue(value);
		productFeatureModel.setQualifier(product.getCode());
		productFeatureModel.setValuePosition(Integer.valueOf(valuePosition));
		modelService.save(productFeatureModel);
		return productFeatureModel;
	}


	private CatalogModel createCatalog(final String id)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);
		modelService.save(catalog);

		return catalog;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog, final String version)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(version);
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		return catalogVersion;
	}

	private ProductModel createProduct(final String code, final String ean, final CatalogVersionModel version)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setEan(ean);
		product.setCatalogVersion(version);
		modelService.save(product);

		return product;
	}


	private ProductReferenceModel createProductReference(final String qualifier, final ProductModel source,
			final ProductModel target)
	{
		final ProductReferenceModel ref = modelService.create(ProductReferenceModel.class);
		ref.setQualifier(qualifier);
		ref.setSource(source);
		ref.setTarget(target);
		ref.setQuantity(Integer.valueOf(1));
		ref.setActive(Boolean.FALSE);
		ref.setPreselected(Boolean.FALSE);

		modelService.save(ref);

		return ref;
	}

}
