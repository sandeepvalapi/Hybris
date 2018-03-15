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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.directpersistence.impl.DefaultJaloAccessorsService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductReferenceSldTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	private CatalogVersionModel testCatalogVersion;

	private ProductModel testSourceProduct;

	private ProductModel testTargetProduct;
	private static final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");
	private static final PropertyConfigSwitcher cfgAllSafe4Writing = new PropertyConfigSwitcher(
			DefaultJaloAccessorsService.CFG_ALL_SAFE_4_WRITING);


	@Before
	public void setUp() throws Exception
	{
		final CatalogModel catalog1 = modelService.create(CatalogModel.class);
		catalog1.setId("catalog1");
		modelService.save(catalog1);

		testCatalogVersion = modelService.create(CatalogVersionModel.class);
		testCatalogVersion.setCatalog(catalog1);
		testCatalogVersion.setVersion("v1.0");

		testSourceProduct = createProduct("source123");

		testTargetProduct = createProduct("target123");


		modelService.saveAll();

		persistenceLegacyMode.switchToValue("false");
		cfgAllSafe4Writing.switchToValue("true");
	}

	@After
	public void tearDown()
	{
		persistenceLegacyMode.switchBackToDefault();
		cfgAllSafe4Writing.switchBackToDefault();
	}


	@Test
	public void shouldNotChangeModificationTimeIfProductReferenceDidNotChange()
	{
		final ProductReferenceModel productReference = createProductReference();

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productReference);
		modelService.refresh(testSourceProduct);

		final Date originalSourceModifyTime = testSourceProduct.getModifiedtime();

		productReference.setQualifier("123");

		modelService.save(productReference);

		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(productReference);

		modelService.refresh(testSourceProduct);

		assertThat(originalSourceModifyTime).isEqualTo(testSourceProduct.getModifiedtime());
	}


	@Test
	public void shouldChangeModificationTimeIfProductReferenceDidChange()
	{
		final ProductReferenceModel productReference = createProductReference();

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productReference);
		modelService.refresh(testSourceProduct);

		final Date originalSourceModifyTime = testSourceProduct.getModifiedtime();

		productReference.setQualifier("456");

		modelService.save(productReference);

		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(productReference);

		modelService.refresh(testSourceProduct);

		assertThat(originalSourceModifyTime).isNotEqualTo(testSourceProduct.getModifiedtime());
	}


	@Test
	public void shouldChangeModificationTimeOnSourceIfProductReferenceRemoved()
	{
		final ProductReferenceModel productReference = createProductReference();

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, productReference);
		modelService.refresh(testSourceProduct);

		final Date originalSourceModifyTime = testSourceProduct.getModifiedtime();

		modelService.remove(productReference);
		modelService.refresh(testSourceProduct);

		assertThat(originalSourceModifyTime).isNotEqualTo(testSourceProduct.getModifiedtime());
	}


	private ProductReferenceModel createProductReference()
	{
		final ProductReferenceModel productReference = modelService.create(ProductReferenceModel.class);
		productReference.setSource(testSourceProduct);
		productReference.setTarget(testTargetProduct);
		productReference.setPreselected(Boolean.FALSE);
		productReference.setActive(Boolean.TRUE);
		productReference.setQualifier("123");

		return productReference;
	}

	private ProductModel createProduct(final String code)
	{
		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCatalogVersion(testCatalogVersion);
		productModel.setCode(code);

		return productModel;
	}
}
