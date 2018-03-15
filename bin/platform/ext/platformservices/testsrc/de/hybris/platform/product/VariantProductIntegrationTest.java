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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class VariantProductIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	private TestDataCreator testDataCreator;
	private CatalogVersionModel catalogVersion;
	private VariantTypeModel variantType;
	private final PropertyConfigSwitcher persistenceLegacySwitch = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void setUp() throws Exception
	{
		testDataCreator = new TestDataCreator(modelService);

		final CatalogModel catalog = testDataCreator.createCatalog();
		catalogVersion = testDataCreator.createCatalogVersion("test", catalog);
		variantType = createVariantType();

	}

	@After
	public void tearDown() throws Exception
	{
		persistenceLegacySwitch.switchBackToDefault();
	}

	private VariantTypeModel createVariantType()
	{
		final VariantTypeModel variantType = modelService.create(VariantTypeModel.class);
		variantType.setCode("vt");
		variantType.setSingleton(Boolean.FALSE);
		variantType.setGenerate(Boolean.TRUE);
		variantType.setCatalogItemType(Boolean.FALSE);
		modelService.save(variantType);

		return variantType;
	}

	@Test
	public void shouldSuccessfullyRemoveBaseProductAndAllItsVariantProducts_SLD_Mode() throws Exception
	{
		persistenceLegacySwitch.switchToValue("false");
		shouldSuccessfullyRemoveBaseProductAndAllItsVariants();
	}

	@Test
	public void shouldSuccessfullyRemoveBaseProductAndAllItsVariantProducts_JALO_Mode() throws Exception
	{
		persistenceLegacySwitch.switchToValue("true");
		shouldSuccessfullyRemoveBaseProductAndAllItsVariants();
	}

	private void shouldSuccessfullyRemoveBaseProductAndAllItsVariants()
	{
		// given 
		final ProductModel baseProduct = createBaseProduct(variantType);
		final VariantProductModel variantProduct1 = createVariantProduct("testVariant1", catalogVersion, baseProduct);
		final VariantProductModel variantProduct2 = createVariantProduct("testVariant3", catalogVersion, baseProduct);
		final VariantProductModel variantProduct3 = createVariantProduct("testVariant2", catalogVersion, baseProduct);

		// when
		modelService.remove(baseProduct);

		// then
		assertThat(modelService.isRemoved(baseProduct)).isTrue();
		assertThat(modelService.isRemoved(variantProduct1)).isTrue();
		assertThat(modelService.isRemoved(variantProduct2)).isTrue();
		assertThat(modelService.isRemoved(variantProduct3)).isTrue();
	}

	private ProductModel createBaseProduct(final VariantTypeModel variantType)
	{
		final ProductModel product = testDataCreator.createProduct(catalogVersion);
		product.setVariantType(variantType);
		modelService.save(product);

		return product;
	}

	private VariantProductModel createVariantProduct(final String code, final CatalogVersionModel catalogVersion,
			final ProductModel baseProduct)
	{
		final VariantProductModel variant = modelService.create(VariantProductModel.class);
		variant.setCode(code);
		variant.setCatalogVersion(catalogVersion);
		variant.setBaseProduct(baseProduct);
		modelService.save(variant);

		return variant;
	}
}
