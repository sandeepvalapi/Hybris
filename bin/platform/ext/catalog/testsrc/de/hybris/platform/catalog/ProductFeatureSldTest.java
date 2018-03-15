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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.dynamic.ProductFeatureValueAttributeHandler;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.directpersistence.impl.DefaultJaloAccessorsService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Direct persistence tests for ProductFeature related scenarios
 */
@IntegrationTest
public class ProductFeatureSldTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	private ProductModel testProduct;

	private final PropertyConfigSwitcher allSafeConfig = new PropertyConfigSwitcher(DefaultJaloAccessorsService.CFG_ALL_SAFE_4_WRITING);

	@Before
	public void setUp() throws Exception
	{
        final TestDataCreator dataCreator = new TestDataCreator(modelService);

        dataCreator.createLanguage("de", "German");
        final CatalogModel ctg = dataCreator.createCatalog("catalog1");
        final CatalogVersionModel ctgv = dataCreator.createCatalogVersion("v1.0", ctg);
        testProduct = dataCreator.createProduct(ctgv);

		allSafeConfig.switchToValue("true");
	}

	@After
	public void TearDown() throws Exception
	{
		allSafeConfig.switchBackToDefault();
	}


	@Test
	public void shouldCreateProductFeatureSuccessfullyWithNumberValue()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductFeatureModel productFeature = createProductFeature("testProdFeature", BigDecimal.TEN, testProduct);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productFeature);

			modelService.refresh(productFeature);
			assertThat(productFeature).isNotNull();
			assertThat(productFeature.getProduct()).isEqualTo(testProduct);
			assertThat(productFeature.getValue()).isEqualTo(BigDecimal.TEN);
			assertThat(productFeature.getQualifier()).isEqualTo("testProdFeature");

			assertThat((Integer) productFeature.getProperty(ProductFeatureModel.VALUETYPE))
					.isEqualTo(ProductFeatureValueAttributeHandler.TYPE_NUMBER);
			checkSearchFields(productFeature, "10", Boolean.TRUE, new BigDecimal(10));
			assertThat(productFeature.getValuePosition()).isEqualTo(0);
			assertThat(productFeature.getFeaturePosition()).isEqualTo(0);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(productFeature);
			return null;
		});
	}

	@Test
	public void shouldCreateProductFeatureSuccessfullyWithStringValue()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductFeatureModel productFeature = createProductFeature("testProdFeature", "20", testProduct);
			modelService.save(productFeature);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productFeature);

			modelService.refresh(productFeature);
			assertThat(productFeature).isNotNull();
			assertThat(productFeature.getProduct()).isEqualTo(testProduct);
			assertThat(productFeature.getValue()).isEqualTo("20");
			assertThat(productFeature.getQualifier()).isEqualTo("testProdFeature");

			assertThat((Integer) productFeature.getProperty(ProductFeatureModel.VALUETYPE))
					.isEqualTo(ProductFeatureValueAttributeHandler.TYPE_STRING);
			checkSearchFields(productFeature, "20", Boolean.FALSE, new BigDecimal(20));
			assertThat(productFeature.getValuePosition()).isEqualTo(0);
			assertThat(productFeature.getFeaturePosition()).isEqualTo(0);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(productFeature);
			return null;
		});
	}

	@Test
	public void shouldCreateProductFeatureSuccessfullyWithBooleanValue()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductFeatureModel productFeature = createProductFeature("testProdFeature", Boolean.TRUE, testProduct);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productFeature);

			modelService.refresh(productFeature);
			assertThat(productFeature).isNotNull();
			assertThat(productFeature.getProduct()).isEqualTo(testProduct);
			assertThat(productFeature.getValue()).isEqualTo(Boolean.TRUE);
			assertThat(productFeature.getQualifier()).isEqualTo("testProdFeature");

			assertThat((Integer) productFeature.getProperty(ProductFeatureModel.VALUETYPE))
					.isEqualTo(ProductFeatureValueAttributeHandler.TYPE_BOOLEAN);
			checkSearchFields(productFeature, "true", Boolean.TRUE, new BigDecimal(1.0));
			assertThat(productFeature.getValuePosition()).isEqualTo(0);
			assertThat(productFeature.getFeaturePosition()).isEqualTo(0);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(productFeature);
			return null;
		});
	}

	private void checkSearchFields(final ProductFeatureModel productFeature, final String expectedStringValue,
			final Boolean expectedBooleanValue, final BigDecimal expectedBigDecimalValue)
	{
		final String stringValue = productFeature.getProperty(ProductFeatureModel.STRINGVALUE);
		assertThat(stringValue).isEqualTo(expectedStringValue);
		final Boolean booleanValue = productFeature.getProperty(ProductFeatureModel.BOOLEANVALUE);
		assertThat(booleanValue).isEqualTo(expectedBooleanValue);
		final BigDecimal numberValue = productFeature.getProperty(ProductFeatureModel.NUMBERVALUE);
		assertThat(numberValue.compareTo(expectedBigDecimalValue)).isZero();
	}

	private ProductFeatureModel createProductFeature(final String qualifier, final Object value,
			final ProductModel productForFeature)
	{
		final ProductFeatureModel model = modelService.create(ProductFeatureModel.class);
		model.setProduct(productForFeature);
		model.setValue(value);
		model.setQualifier(qualifier);
		return model;
	}
}
