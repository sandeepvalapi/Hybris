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

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests whether {@link L10NService} is supporting UTF-8 encoded property files.
 */
public class L10NServiceUnicodeTest extends ServicelayerBaseTest
{
	@Resource
	I18NService i18nService;

	@Resource
	L10NService l10nService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	ModelService modelService;

	@Before
	public void setUp()
	{
		try
		{
			commonI18NService.getLanguage(Locale.GERMAN.toString());
		}
		catch (final UnknownIdentifierException e)
		{
			final LanguageModel de = new LanguageModel();
			de.setIsocode(Locale.GERMAN.toString());
			de.setActive(Boolean.TRUE);
			modelService.save(de);
		}
		i18nService.setCurrentLocale(Locale.GERMAN);
	}

	@Test
	public void testUTF8GetLocalizedString()
	{
		// test access via global loaded bundle ( from TypeLocalizations at the moment )
		assertEquals("Kardinalit\u00e4t", l10nService.getLocalizedString("type.relationendcardinalityenum.name"));
	}

	@Ignore("proves PLA-12827 - enable to test")
	@Test
	public void testUTF8GetResourceBundle()
	{
		// test loading from base name ( also using the type localization file )
		assertEquals("Kardinalit\u00e4t",
				l10nService.getResourceBundle("localization/core-locales").getString("type.relationendcardinalityenum.name"));
	}
}
