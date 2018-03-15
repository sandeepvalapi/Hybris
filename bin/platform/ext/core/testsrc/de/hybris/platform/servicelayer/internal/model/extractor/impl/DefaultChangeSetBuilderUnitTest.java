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
package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.impl.LocalizedAttributesProcessor;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableSet;

/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
@UnitTest
public class DefaultChangeSetBuilderUnitTest
{
	private static final Logger LOG = Logger.getLogger(DefaultChangeSetBuilderUnitTest.class);

	private final Locale ROOT_LOCALE_DE = Locale.GERMAN;
	private final Locale MID_LOCALE_DE = new Locale(ROOT_LOCALE_DE.getLanguage(), "au");
	private final Locale LEAF_LOCALE_DE = new Locale(ROOT_LOCALE_DE.getLanguage(), "de", "by"); //bayern

	private final Locale ROOT_LOCALE_EN = Locale.ENGLISH;
	private final Locale MID_LOCALE_EN = new Locale(ROOT_LOCALE_EN.getLanguage(), "au");
	private final Locale OTHER_LEAF_LOCALE_EN = new Locale(ROOT_LOCALE_EN.getLanguage(), "en", "ch");
	private final Locale LEAF_LOCALE_EN = new Locale(ROOT_LOCALE_EN.getLanguage(), "en", "by"); //bayern in england !!
	@Mock
	private I18NService i18NService;
	private LocalizedAttributesProcessor processor;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		BDDMockito.when(i18NService.getBestMatchingLocale(ROOT_LOCALE_DE)).thenReturn(ROOT_LOCALE_DE);
		BDDMockito.when(i18NService.getBestMatchingLocale(MID_LOCALE_DE)).thenReturn(ROOT_LOCALE_DE);
		BDDMockito.when(i18NService.getBestMatchingLocale(LEAF_LOCALE_DE)).thenReturn(ROOT_LOCALE_DE);


		BDDMockito.when(i18NService.getBestMatchingLocale(ROOT_LOCALE_EN)).thenReturn(ROOT_LOCALE_EN);
		BDDMockito.when(i18NService.getBestMatchingLocale(MID_LOCALE_EN)).thenReturn(ROOT_LOCALE_EN);
		BDDMockito.when(i18NService.getBestMatchingLocale(LEAF_LOCALE_EN)).thenReturn(ROOT_LOCALE_EN);
		BDDMockito.when(i18NService.getBestMatchingLocale(OTHER_LEAF_LOCALE_EN)).thenReturn(ROOT_LOCALE_EN);


		processor = new LocalizedAttributesProcessor(i18NService);
	}

	@Test
	public void testMergingIntoMostSpecific()
	{
		final DefaultChangeSetBuilder builder = new DefaultChangeSetBuilder();

		builder.setLocalizedAttributesProcessor(processor);

		final Map<Locale, Set<String>> dirtyLocalizedAttributes = new LinkedHashMap<>();

		fillAttributes(dirtyLocalizedAttributes, ROOT_LOCALE_DE, "foo", "bar", "baz");
		fillAttributes(dirtyLocalizedAttributes, MID_LOCALE_DE, "mi-foo", "bar", "mi-baz");
		fillAttributes(dirtyLocalizedAttributes, LEAF_LOCALE_DE, "li-foo", "bar", "mi-baz", "baz");

		final Map<Locale, Set<String>> resultLocalizedAttributes = builder.preProcessLocalizedAttributes(dirtyLocalizedAttributes);

		Assertions.assertThat(resultLocalizedAttributes.get(ROOT_LOCALE_DE)).containsOnly("foo");
		Assertions.assertThat(resultLocalizedAttributes.get(MID_LOCALE_DE)).containsOnly("mi-foo");
		Assertions.assertThat(resultLocalizedAttributes.get(LEAF_LOCALE_DE)).containsOnly("li-foo", "bar", "mi-baz", "baz");

	}


	@Test
	public void testMergingIntoMostSpecificWithMoreLanguages()
	{
		final DefaultChangeSetBuilder builder = new DefaultChangeSetBuilder();

		builder.setLocalizedAttributesProcessor(processor);

		final Map<Locale, Set<String>> dirtyLocalizedAttributes = new LinkedHashMap<>();

		fillAttributes(dirtyLocalizedAttributes, ROOT_LOCALE_DE, "foo", "bar", "baz");
		fillAttributes(dirtyLocalizedAttributes, LEAF_LOCALE_EN, "roo", "rar", "raz");
		fillAttributes(dirtyLocalizedAttributes, MID_LOCALE_DE, "mi-foo", "bar", "mi-baz");
		fillAttributes(dirtyLocalizedAttributes, OTHER_LEAF_LOCALE_EN, "other-foo", "other-bar", "baz", "razr");
		fillAttributes(dirtyLocalizedAttributes, LEAF_LOCALE_DE, "li-foo", "bar", "mi-baz", "baz");
		fillAttributes(dirtyLocalizedAttributes, MID_LOCALE_EN, "mi-roo", "rar");
		fillAttributes(dirtyLocalizedAttributes, ROOT_LOCALE_EN, "roo", "rar", "raz" , "razr");

		final Map<Locale, Set<String>> resultLocalizedAttributes = builder.preProcessLocalizedAttributes(dirtyLocalizedAttributes);

		Assertions.assertThat(resultLocalizedAttributes.get(ROOT_LOCALE_DE)).containsOnly("foo");
		Assertions.assertThat(resultLocalizedAttributes.get(MID_LOCALE_DE)).containsOnly("mi-foo");
		Assertions.assertThat(resultLocalizedAttributes.get(LEAF_LOCALE_DE)).containsOnly("li-foo", "bar", "mi-baz", "baz");


		Assertions.assertThat(resultLocalizedAttributes.get(ROOT_LOCALE_EN)).isNullOrEmpty();
		Assertions.assertThat(resultLocalizedAttributes.get(MID_LOCALE_EN)).containsOnly("mi-roo");
		Assertions.assertThat(resultLocalizedAttributes.get(LEAF_LOCALE_EN)).containsOnly("roo", "rar", "raz");
		Assertions.assertThat(resultLocalizedAttributes.get(OTHER_LEAF_LOCALE_EN)).containsOnly("other-foo", "other-bar", "baz","razr");

	}

	private void fillAttributes(final Map<Locale, Set<String>> dirtyLocalizedAttributes, final Locale locale,
										 final String... qualifiers)
	{
		dirtyLocalizedAttributes.put(locale, ImmutableSet.copyOf(qualifiers));
	}

}
