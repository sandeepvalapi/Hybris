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
package de.hybris.platform.servicelayer.i18n;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.MockTest;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@UnitTest
@ContextConfiguration(locations =
{ "classpath:/servicelayer/test/servicelayer-mock-base-test.xml", "classpath:/servicelayer/test/servicelayer-mock-i18n-test.xml" })
public class MockI18NTest extends MockTest
{
	@Resource(name = "i18nService")
	private I18NService i18NService;

	@Test
	public void testLanguageService()
	{
		assertNotNull(i18NService.getLanguage("en")); //en is default
		final Set<LanguageModel> allLanguages = i18NService.getAllLanguages();
		assertFalse(allLanguages.isEmpty());
	}
}
