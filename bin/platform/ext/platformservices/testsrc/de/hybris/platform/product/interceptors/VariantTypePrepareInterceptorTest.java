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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * test-class for VariantTypePrepareInterceptor.
 */
@IntegrationTest
public class VariantTypePrepareInterceptorTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;


	@Test
	public void testOnPrepareDefaultSupertype()
	{
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("testProduct0");
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);

		modelService.save(variantTypeModel);
		assertNotNull(variantTypeModel);
		assertTrue(variantTypeModel.getSuperType().equals(typeService.getComposedTypeForCode(VariantProductModel._TYPECODE)));
	}

	@Test
	public void testOnPrepareWrongSupertype()
	{
		final ComposedTypeModel wrongSuperType = modelService.create(ComposedTypeModel.class);
		wrongSuperType.setCode("wrongSuperType");
		wrongSuperType.setSingleton(Boolean.FALSE);
		wrongSuperType.setGenerate(Boolean.TRUE);
		wrongSuperType.setCatalogItemType(Boolean.FALSE);
		wrongSuperType.setSuperType(typeService.getComposedTypeForCode(ProductModel._TYPECODE));
		modelService.save(wrongSuperType);

		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("testProduct0");
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);
		variantTypeModel.setSuperType(wrongSuperType);

		try
		{
			modelService.save(variantTypeModel);
			fail("InterceptorException expected but not thrown");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof VariantTypePrepareInterceptor);
		}

	}

	@Test
	public void testOnPrepareCorrectSupertype()
	{
		final VariantTypeModel correctSuperType = modelService.create(VariantTypeModel.class);
		correctSuperType.setCode("correctSuperType");
		correctSuperType.setSingleton(Boolean.FALSE);
		correctSuperType.setGenerate(Boolean.TRUE);
		correctSuperType.setCatalogItemType(Boolean.FALSE);
		modelService.save(correctSuperType);

		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("testProduct0");
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);
		variantTypeModel.setSuperType(correctSuperType);

		modelService.save(variantTypeModel);
		assertNotNull(variantTypeModel);
		assertTrue(variantTypeModel.getSuperType().equals(correctSuperType));

	}

}
