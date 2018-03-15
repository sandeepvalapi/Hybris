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
package de.hybris.platform.product.interceptors;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * test-class for VariantProductValidateInterceptor.
 */
@IntegrationTest
public class VariantProductValidateInterceptorTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private ProductDao productDao;

	private VariantTypeModel testVariantTypeModel;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);
	}

	@Test
	public void testOnValidateCorrect() throws Exception
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("id");
		final CatalogVersionModel catver = modelService.create(CatalogVersionModel.class);
		catver.setActive(Boolean.TRUE);
		catver.setCatalog(cat);
		catver.setVersion("xxx");
		modelService.save(cat);
		modelService.save(catver);

		final ProductModel baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseprod");
		baseProduct.setCatalogVersion(catver);
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

		final VariantProductModel variant = modelService.create(VariantProductModel.class);
		variant.setCode("aaa");
		variant.setCatalogVersion(catver);
		variant.setBaseProduct(baseProduct);
		modelService.save(variant);
		assertNotNull(variant);
		//no exception will be thrown
	}

	@Test
	public void testOnValidateMissingBaseVariantType()
	{
		final VariantProductModel variantProductModel = modelService.create(VariantProductModel.class);
		variantProductModel.setCode("testVariantProduct0");
		variantProductModel.setVariantType(testVariantTypeModel);
		final ProductModel base = productDao.findProductsByCode("testProduct0").get(0);
		assertNull(base.getVariantType());
		variantProductModel.setBaseProduct(base);
		try
		{
			modelService.save(variantProductModel);
			fail("InterceptorException expected but not thrown");
		}
		catch (final ModelSavingException e)
		{
			assertTrue("Get " + e.getCause().getClass() + " instead of InterceptorException",
					e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof VariantProductValidateInterceptor);
		}
	}
}
