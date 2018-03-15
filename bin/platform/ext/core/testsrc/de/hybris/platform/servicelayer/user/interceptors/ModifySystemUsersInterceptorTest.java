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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ModifySystemUsersInterceptorTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;


	@Test
	public void testInterceptorAsPreparerInstalled()
	{
		final InterceptorRegistry reg = ((DefaultModelService) modelService).getInterceptorRegistry();
		final Collection<ValidateInterceptor> validaters = reg.getValidateInterceptors("Principal");
		assertThat(validaters.isEmpty()).isFalse();
		boolean found = false;
		for (final ValidateInterceptor inter : validaters)
		{
			if (inter instanceof ModifySystemUsersInterceptor)
			{
				found = true;
				break;
			}
		}
		assertThat(found).isTrue();
	}

	@Test
	public void testInterceptorAsRemoverInstalled()
	{
		final InterceptorRegistry reg = ((DefaultModelService) modelService).getInterceptorRegistry();
		final Collection<RemoveInterceptor> removers = reg.getRemoveInterceptors("Principal");
		assertThat(removers.isEmpty()).isFalse();
		boolean found = false;
		for (final RemoveInterceptor inter : removers)
		{
			if (inter instanceof ModifySystemUsersInterceptor)
			{
				found = true;
				break;
			}
		}
		assertThat(found).isTrue();
	}

	@Test
	public void testDisableLoginForAdmin()
	{
		final EmployeeModel admin = userService.getAdminUser();
		admin.setLoginDisabled(false);
		modelService.save(admin);

		admin.setLoginDisabled(true);
		runSaveModel(admin);
	}

	@Test
	public void testDeactivateAdmin()
	{
		final EmployeeModel admin = userService.getAdminUser();
		admin.setDeactivationDate(null);
		modelService.save(admin);

		admin.setDeactivationDate(new Date());

		runSaveModel(admin);
	}

	private void runSaveModel(final EmployeeModel admin)
	{
		try
		{
			modelService.save(admin);
			fail("expected ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(((InterceptorException) e.getCause()).getInterceptor()).isInstanceOf(ModifySystemUsersInterceptor.class);
		}
	}
}
