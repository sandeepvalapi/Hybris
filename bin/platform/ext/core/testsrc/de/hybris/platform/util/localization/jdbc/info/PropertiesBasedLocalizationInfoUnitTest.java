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
package de.hybris.platform.util.localization.jdbc.info;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.c2l.Language;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PropertiesBasedLocalizationInfoUnitTest
{
	private static final PK PL_LANGUAGE_PK = PK.fromLong(123);
	private static final PK EN_LANGUAGE_PK = PK.fromLong(456);
	private static final PK UNKNOWN_LANGUAGE_PK = PK.fromLong(789);

	private static final String KNOWN_KEY = "known.key";
	private static final String UNKNOWN_KEY = "unknown.key";
	private static final String EMPTY_KEY = "empty.key";
	private static final String KNOWN_VALUE_PL = "known.value.pl";
	private static final String KNOWN_VALUE_EN = "known.value.en";

	@Mock
	private Language plLanguage;

	@Mock
	private Language enLanguage;

	@Mock
	private Properties plProperties;

	@Mock
	private Properties enProperties;

	private Map<Language, Properties> localization;

	@Before
	public void setUp()
	{
		when(plLanguage.getPK()).thenReturn(PL_LANGUAGE_PK);
		when(enLanguage.getPK()).thenReturn(EN_LANGUAGE_PK);

		when(plProperties.getProperty(KNOWN_KEY)).thenReturn(KNOWN_VALUE_PL);
		when(plProperties.getProperty(UNKNOWN_KEY)).thenReturn(null);
		when(plProperties.getProperty(EMPTY_KEY)).thenReturn("");

		when(enProperties.getProperty(KNOWN_KEY)).thenReturn(KNOWN_VALUE_EN);
		when(enProperties.getProperty(UNKNOWN_KEY)).thenReturn(null);
		when(enProperties.getProperty(EMPTY_KEY)).thenReturn("");

		localization = ImmutableMap.of(plLanguage, plProperties, enLanguage, enProperties);
	}

	@Test
	public void shouldReturnLocalizedValueForGivenLanguage()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final String localizedValue = localizationInfo.getLocalizedProperty(PL_LANGUAGE_PK, KNOWN_KEY);

		//then
		assertThat(localizedValue).isNotNull().isEqualTo(KNOWN_VALUE_PL);
	}

	@Test
	public void shouldReturnNullForUnknownLanguage()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final String localizedValue = localizationInfo.getLocalizedProperty(UNKNOWN_LANGUAGE_PK, KNOWN_KEY);

		//then
		assertThat(localizedValue).isNull();
	}

	@Test
	public void shouldReturnNullForUnknownKey()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final String localizedValue = localizationInfo.getLocalizedProperty(EN_LANGUAGE_PK, UNKNOWN_KEY);

		//then
		assertThat(localizedValue).isNull();
	}

	@Test
	public void shouldReturnNullForUnknownKeyAndUnknownLanguage()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final String localizedValue = localizationInfo.getLocalizedProperty(UNKNOWN_LANGUAGE_PK, UNKNOWN_KEY);

		//then
		assertThat(localizedValue).isNull();
	}

	@Test
	public void shouldReturnNullForEmpytValue()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final String localizedValue = localizationInfo.getLocalizedProperty(EN_LANGUAGE_PK, EMPTY_KEY);

		//then
		assertThat(localizedValue).isNull();
	}

	@Test
	public void shouldReturnKnownLangagePKs()
	{
		//given
		final PropertiesBasedLocalizationInfo localizationInfo = new PropertiesBasedLocalizationInfo(localization,
				Collections.emptyMap());

		//when
		final Collection<PK> languagePKs = localizationInfo.getLanguagePKs();

		//then
		assertThat(languagePKs).isNotNull().containsOnly(PL_LANGUAGE_PK, EN_LANGUAGE_PK);
	}
}
