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

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.product.daos.VariantTypeDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantTypeModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * test-class for VariantTypeRemoveInterceptor.
 */
@IntegrationTest
public class VariantTypeRemoveInterceptorTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private ProductDao productDao;

	@Resource
	private VariantTypeDao variantTypeDao;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testOnRemove()
	{
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("testProduct0");
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(variantTypeModel);
		assertTrue(variantTypeDao.getBaseProductCount(variantTypeModel) == 0);
		modelService.remove(variantTypeModel);//no problem with remove, no base products assigned.

		//next attempt, with base products assigned to the variantType, which is to be removed
		final VariantTypeModel variantTypeModel2 = modelService.create(VariantTypeModel.class);
		variantTypeModel2.setCode("testProduct0");
		variantTypeModel2.setSingleton(Boolean.FALSE);
		variantTypeModel2.setGenerate(Boolean.TRUE);
		variantTypeModel2.setCatalogItemType(Boolean.FALSE);
		modelService.save(variantTypeModel2);

		final ProductModel base = productDao.findProductsByCode("testProduct0").get(0);
		base.setVariantType(variantTypeModel2);
		modelService.save(base);
		assertTrue(variantTypeDao.getBaseProductCount(variantTypeModel2) == 1);

		try
		{
			modelService.remove(variantTypeModel2);//This time an appropriate Exception is expected
			fail("InterceptorException was expected but not thrown");
		}
		catch (final ModelRemovalException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof VariantTypeRemoveInterceptor);
		}

	}

}
