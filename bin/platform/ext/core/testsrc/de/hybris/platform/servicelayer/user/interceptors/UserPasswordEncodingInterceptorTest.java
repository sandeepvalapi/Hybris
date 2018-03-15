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
package de.hybris.platform.servicelayer.user.interceptors;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderConstants;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class UserPasswordEncodingInterceptorTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	ModelService modelService;

	@Test
	public void testInterceptorAreInstalled()
	{
		boolean foundValidator = false;
		boolean foundPreparer = false;

		final InterceptorRegistry reg = ((DefaultModelService) modelService).getInterceptorRegistry();
		final Collection<ValidateInterceptor> validaters = reg.getValidateInterceptors("User");
		assertFalse(validaters.isEmpty());
		for (final ValidateInterceptor inter : validaters)
		{
			if (inter instanceof UserPasswordEncodingValidator)
			{
				foundValidator = true;
				break;
			}
		}
		final Collection<PrepareInterceptor> preparer = reg.getPrepareInterceptors("User");
		assertFalse(preparer.isEmpty());
		for (final PrepareInterceptor inter : preparer)
		{
			if (inter instanceof UserPasswordEncodingPreparer)
			{
				foundPreparer = true;
				break;
			}
		}
		assertTrue(foundValidator && foundPreparer);
	}

	@Test
	public void testPreparer()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("moo");
		modelService.save(user);

		assertEquals(PasswordEncoderConstants.DEFAULT_ENCODING, user.getPasswordEncoding());
	}

	@Test
	public void testValidator()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("moo");
		user.setPasswordEncoding("xxx");
		try
		{
			modelService.save(user);
			fail("ModelSavingException with cause InterceptorException expeected!");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof UserPasswordEncodingValidator);
		}
		catch (final Exception e)
		{
			fail("unexpected exception!");
		}
	}
}
