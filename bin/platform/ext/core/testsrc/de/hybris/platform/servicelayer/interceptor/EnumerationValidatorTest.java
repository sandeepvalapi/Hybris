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
package de.hybris.platform.servicelayer.interceptor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.impl.EnumerationValidator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * 
 * test-class for EnumerationValidator.
 */
@IntegrationTest
public class EnumerationValidatorTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;


	@Test
	public void testOnValidateEnumOk()
	{
		final EnumerationValueModel evModel = modelService.create(OrderStatus.CREATED.getType());
		final EnumerationMetaTypeModel enumMetaTypeModel = typeService.getEnumerationTypeForCode(evModel.getItemtype());
		assertEquals(Boolean.TRUE, enumMetaTypeModel.getDynamic());
		assertNotNull(evModel);
		evModel.setCode("TestValueCode");

		modelService.save(evModel);
		assertEquals("TestValueCode", evModel.getCode());

	}

	@Test
	public void testOnValidateEnumNotDynamic()
	{
		final EnumerationValueModel evModelNotDynamic = modelService.create(GenderEnumStub.MALE.getType());
		final EnumerationMetaTypeModel enumMetaTypeModel = typeService.getEnumerationTypeForCode(evModelNotDynamic.getItemtype());
		assertEquals(Boolean.FALSE, enumMetaTypeModel.getDynamic());

		evModelNotDynamic.setCode("TestValueCodeDynamic");

		try
		{
			modelService.save(evModelNotDynamic);
			fail("Exception was expected but not thrown");
		}
		catch (final ModelSavingException e)
		{
			final Throwable cause = e.getCause();
			assertNotNull(cause);
			assertTrue(cause instanceof InterceptorException);
			assertTrue(((InterceptorException) cause).getInterceptor() instanceof EnumerationValidator);
		}

	}


}
