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
package de.hybris.platform.servicelayer.i18n.interceptors;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Integration test checking remove interceptors for a {@link LanguageModel}.
 */
@IntegrationTest
public class RemoveLanguageIntercetorTest extends ServicelayerTest
{
	@Resource
	private SessionService sessionService;

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	/**
	 * Test covering the scenario for removing session language
	 */
	@Test
	public void testDeleteCurrentSessionLanguage()
	{
		final LanguageModel lModel0 = modelService.create(LanguageModel.class);
		lModel0.setIsocode("tinyRed");

		modelService.save(lModel0);
		sessionService.setAttribute("language", lModel0);
		try
		{
			modelService.remove(lModel0);
			Assert.fail("Should not be able to remove " + lModel0 + " the current language for the session.");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof ModelRemovalException);
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof RemoveSessionLanguageInterceptor);
		}

	}

	/**
	 * Test covering the scenario for removing last language in the tenant (actually all languages from tenant)
	 */
	@Test
	public void testDeleteAllLanguages()
	{
		final List<LanguageModel> allLang = commonI18NService.getAllLanguages();
		if (allLang.size() > 1)
		{
			//remove all other languages except one
			for (int i = 1; i < allLang.size(); i++)
			{
				modelService.remove(allLang.get(i));
			}
		}
		try
		{
			//delete last language
			modelService.remove(allLang.get(0));
			Assert.fail("Should not be able to remove all languages from the tenant.");
		}
		catch (final Exception e)
		{
			assertTrue(e instanceof ModelRemovalException);
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof RemoveLastLanguageInterceptor);
		}



	}

}
