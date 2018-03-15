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
package de.hybris.platform.classification.features;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.core.PK;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizedFeatureTest
{
	private static final String FOO_DESCR2 = "fooDescr2";
	private static final String FOO_VAL2 = "fooVal2";
	private static final String FOO_VAL1 = "fooVal1";
	private static final String FOO_DESCR1 = "fooDescr1";
	private LocalizedFeature feature;
	@Mock
	private ClassAttributeAssignmentModel assignment;
	@Mock
	private Map<Locale, List<FeatureValue>> values;
	@Mock
	private ClassificationAttributeUnitModel unit;
	private PK pk;
	private Locale currentLocale;

	@Before
	public void setUp()
	{
		currentLocale = new Locale("en");
		pk = PK.createFixedUUIDPK(1, 10);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValues()}.
	 */
	@Test
	public void shouldReturnEmptyListWhenThereIsEmptyListOfValuesForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, values, currentLocale);
		given(values.get(currentLocale)).willReturn(Collections.EMPTY_LIST);

		// when
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValues()}.
	 */
	@Test
	public void shouldReturnEmptyListWhenThereIsNullListOfValuesForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, values, currentLocale);
		given(values.get(currentLocale)).willReturn(null);

		// when
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValues()}.
	 */
	@Test
	public void shouldReturnUnmodifableListOfValuesWhenThereIsListOfValuesForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		values.put(currentLocale, Lists.newArrayList(new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk)));
		feature = new LocalizedFeature(assignment, values, currentLocale);

		// when
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isNotEmpty();
		assertThat(result).isEqualTo(values.get(currentLocale));
		try
		{
			result.remove(0);
			fail("Should not allow modifying of collection");
		}
		catch (final UnsupportedOperationException e)
		{
			// That's OK
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValue()}.
	 */
	@Test
	public void shouldReturnNullValueWhenThereIsEmptyListOfValuesForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, values, currentLocale);
		given(values.get(currentLocale)).willReturn(Collections.EMPTY_LIST);

		// when
		final FeatureValue value = feature.getValue();

		// then
		assertThat(value).isNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValue()}.
	 */
	@Test
	public void shouldReturnNullValueWhenThereIsNullListOfValuesForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, values, currentLocale);
		given(values.get(currentLocale)).willReturn(null);

		// when
		final FeatureValue value = feature.getValue();

		// then
		assertThat(value).isNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValue()}.
	 */
	@Test
	public void shouldReturnFirstValueForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		values.put(currentLocale, Lists.newArrayList(new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk), new FeatureValue(FOO_VAL2,
				FOO_DESCR2, unit, pk)));
		feature = new LocalizedFeature(assignment, values, currentLocale);

		// when
		final FeatureValue value = feature.getValue();

		// then
		assertThat(value).isNotNull();
		assertThat(value.getValue()).isEqualTo(FOO_VAL1);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#addValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldAddValueForCurrentLocaleWhenThereIsNoValueForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, null, currentLocale);
		final FeatureValue featureValue = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);

		// when
		feature.addValue(featureValue);

		// then
		assertThat(feature.getValue()).isNotNull();
		assertThat(feature.getValue()).isEqualTo(featureValue);
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldAddValueForCurrentLocaleOnIndex0WhenThereIsNoValueForCurrentLocale()
	{
		// given
		feature = new LocalizedFeature(assignment, null, currentLocale);
		final FeatureValue featureValue = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);

		// when
		feature.addValue(0, featureValue);

		// then
		assertThat(feature.getValue()).isNotNull();
		assertThat(feature.getValue()).isEqualTo(featureValue);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldAddValueForCurrentLocaleOnIndex0WhenThereIsValueForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		values.put(currentLocale, Lists.newArrayList(new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk), new FeatureValue(FOO_VAL2,
				FOO_DESCR2, unit, pk)));
		feature = new LocalizedFeature("foo", values, currentLocale);
		final FeatureValue featureValue = new FeatureValue("new value", "new descr", unit, pk);

		// when
		feature.addValue(0, featureValue);
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result.get(0).getValue()).isEqualTo(featureValue.getValue());
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenSettingValueOnIndexGreaterThanValuesSize()
	{
		// given
		feature = new LocalizedFeature(assignment, null, currentLocale);
		final FeatureValue featureValue = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);

		try
		{
			// when (try to set value on index 10 when values for current locale is array of size 0)
			feature.addValue(10, featureValue);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("index is not in range of: 0 and 0");
		}

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenSettingValueOnIndexLowerThanZero()
	{
		// given
		feature = new LocalizedFeature(assignment, null, currentLocale);
		final FeatureValue featureValue = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);

		try
		{
			// when (try to set value on index -1 when values for current locale is array of size 0)
			feature.addValue(-1, featureValue);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("index is not in range of: 0 and 0");
		}

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#removeValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldRemoveLocalizedValueForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		values.put(currentLocale, Lists.newArrayList(featureValue1, featureValue2));
		feature = new LocalizedFeature(assignment, values, currentLocale);

		// when
		feature.removeValue(featureValue1);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValues()).containsOnly(featureValue2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#removeAllValues()}.
	 */
	@Test
	public void shouldRemoveAllValuesForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		values.put(currentLocale, Lists.newArrayList(featureValue1, featureValue2));
		feature = new LocalizedFeature(assignment, values, currentLocale);

		// when
		feature.removeAllValues();

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(0);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#setValues(java.util.List)}.
	 */
	@Test
	public void shouldSetListOfValuesForCurrentLocaleIfThereIsNoValuesForCurrentLocale()
	{
		// given
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		final List<FeatureValue> values = Lists.newArrayList(featureValue1, featureValue2);
		feature = new LocalizedFeature(assignment, null, currentLocale);
		assertThat(feature.getValues()).isEmpty();

		// when
		feature.setValues(values);

		// then
		assertThat(feature.getValues()).isNotEmpty();
		assertThat(feature.getValues()).containsOnly(featureValue1, featureValue2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#setValues(java.util.List)}.
	 */
	@Test
	public void shouldClearAndSetListOfValuesForCurrentLocaleIfThereIsListOfValuesForCurrentLocale()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		values.put(currentLocale, Lists.newArrayList(featureValue1, featureValue2));
		feature = new LocalizedFeature(assignment, values, currentLocale);

		final FeatureValue featureValue3 = new FeatureValue("new value 1", "new descr 1", unit, pk);
		final FeatureValue featureValue4 = new FeatureValue("new value 2", "new descr 2", unit, pk);
		final List<FeatureValue> newValues = Lists.newArrayList(featureValue3, featureValue4);

		// when
		feature.setValues(newValues);

		// then
		assertThat(feature.getValues()).isNotEmpty();
		assertThat(feature.getValues()).containsOnly(featureValue3, featureValue4);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#LocalizedFeature(ClassAttributeAssignmentModel, Map, Locale)}
	 * .
	 */
	@Test
	public void shouldCreateLocalizeFeatureWithEmptyValuesMapWhenValuesIsNull()
	{
		// when
		final LocalizedFeature localizedFeature = new LocalizedFeature(assignment, null, currentLocale);

		// then
		assertThat(localizedFeature).isNotNull();
		assertThat(localizedFeature.getValuesForAllLocales()).isEmpty();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.LocalizedFeature#LocalizedFeature(ClassAttributeAssignmentModel, Map, Locale)}
	 * .
	 */
	@Test
	public void shouldCreateLocalizeFeatureWithCopyOfValuesMapWhenValuesIsNotNull()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		values.put(currentLocale, Lists.newArrayList(featureValue1, featureValue2));

		// when
		final LocalizedFeature localizedFeature = new LocalizedFeature(assignment, values, currentLocale);

		// then
		assertThat(localizedFeature).isNotNull();
		assertThat(localizedFeature.getValuesForAllLocales()).isNotEmpty();
		assertThat(localizedFeature.getValuesForAllLocales()).isNotSameAs(values);
		assertThat(localizedFeature.getValuesForAllLocales()).hasSize(1);
		assertThat(localizedFeature.getValues()).isNotEmpty();
		assertThat(localizedFeature.getValues()).isNotSameAs(values.get(currentLocale));
		assertThat(localizedFeature.getValues()).containsOnly(featureValue1, featureValue2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.LocalizedFeature#getValuesForAllLocales()}.
	 */
	@Test
	public void shouldReturnFullMapOfValuesForAllLocales()
	{
		// given
		values = new HashMap<Locale, List<FeatureValue>>();
		final FeatureValue featureValue1 = new FeatureValue(FOO_VAL1, FOO_DESCR1, unit, pk);
		final FeatureValue featureValue2 = new FeatureValue(FOO_VAL2, FOO_DESCR2, unit, pk);
		values.put(currentLocale, Lists.newArrayList(featureValue1, featureValue2));
		final LocalizedFeature localizedFeature = new LocalizedFeature(assignment, values, currentLocale);

		// when
		final Map<Locale, List<FeatureValue>> valuesForAllLocales = localizedFeature.getValuesForAllLocales();

		// then
		assertThat(valuesForAllLocales).isNotNull();
		assertThat(valuesForAllLocales).isNotEmpty();
		assertThat(valuesForAllLocales).hasSize(1);
		assertThat(valuesForAllLocales.get(currentLocale)).isNotEmpty();
		assertThat(valuesForAllLocales.get(currentLocale)).hasSize(2);
		assertThat(localizedFeature.getValues()).containsOnly(featureValue1, featureValue2);
	}
}
