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
package de.hybris.platform.servicelayer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class JaloInitDefaultValuesModelTest extends ServicelayerTransactionalBaseTest
{
	Language lang1;
	Language lang2;
	Language lang3;

	Locale locale1;
	Locale locale2;
	Locale locale3;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Before
	public void initTest() throws ConsistencyCheckException
	{
		//creating languages
		final C2LManager man = C2LManager.getInstance();
		lang1 = man.createLanguage("aaaa");
		lang2 = man.createLanguage("bbbb");
		lang3 = man.createLanguage("cccc");

		assertNotNull(i18nService.getLanguage("aaaa"));
		assertNotNull(i18nService.getLanguage("bbbb"));
		assertNotNull(i18nService.getLanguage("cccc"));

		//mapping them to test locales
		locale1 = new Locale("aaaa");
		locale2 = new Locale("bbbb");
		locale3 = new Locale("cccc");

		//fill localized attribute with default values
		final TypeManager typman = TypeManager.getInstance();
		final ComposedType countryCT = typman.getComposedType(Country.class);
		final AttributeDescriptor adName = countryCT.getAttributeDescriptor(Country.NAME);
		final Map<Language, String> defaultMap = new HashMap<Language, String>();
		defaultMap.put(lang1, "default_value_1");
		defaultMap.put(lang2, null);
		defaultMap.put(lang3, "default_value_3");

		final SessionContext localCtx = JaloSession.getCurrentSession().createLocalSessionContext();
		localCtx.setLanguage(null);
		adName.setDefaultValue(localCtx, defaultMap);

		assertEquals(defaultMap, adName.getDefaultValue(localCtx));


	}

	/**
	 * This should already be tested somewhere in the jalo layer, but nevertheless we test this here again
	 */
	@Test
	public void testJaloDefaultsExists() throws ConsistencyCheckException, JaloInvalidParameterException, JaloSecurityException
	{
		final C2LManager man = C2LManager.getInstance();
		final Country testCountry = man.createCountry("test");
		assertNotNull(testCountry);

		final SessionContext localCtx = JaloSession.getCurrentSession().createLocalSessionContext();
		localCtx.setLanguage(lang3);
		assertEquals("default_value_3", testCountry.getName(localCtx));
		localCtx.setLanguage(lang2);
		assertEquals(null, testCountry.getName(localCtx));

		localCtx.setLanguage(null);
		final Map<Language, String> names = testCountry.getAllNames(localCtx);
		assertNotNull(names);
		assertEquals(null, names.get(lang2));
		assertEquals("default_value_3", names.get(lang3));

		final Map<Language, String> names2 = (Map<Language, String>) testCountry.getAttribute(null, Country.NAME);
		assertEquals(names, names2);
	}

	@Test
	public void testLocalizedDefaultsWithModelServiceCreate()
	{
		final CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode("test");
		country.setName("value2", locale2);

		assertEquals("default_value_1", country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("default_value_3", country.getName(locale3));

		modelService.save(country);

		assertEquals("default_value_1", country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("default_value_3", country.getName(locale3));
	}

	@Test
	public void testLocalizedDefaultsWithNew()
	{
		final CountryModel country = new CountryModel();
		country.setIsocode("test");
		country.setName("value2", locale2);
		country.setName("value3", locale3);

		assertNull(country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("value3", country.getName(locale3));

		modelService.save(country);

		assertEquals("default_value_1", country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("value3", country.getName(locale3));
	}

	@Test
	public void testLocalizedDefaultsWithNewAndInitDefaults()
	{
		final CountryModel country = new CountryModel();
		country.setIsocode("test");
		country.setName("value2", locale2);
		country.setName("value3", locale3);

		modelService.initDefaults(country);

		assertEquals("default_value_1", country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("value3", country.getName(locale3));

		modelService.save(country);

		assertEquals("default_value_1", country.getName(locale1));
		assertEquals("value2", country.getName(locale2));
		assertEquals("value3", country.getName(locale3));
	}
}
