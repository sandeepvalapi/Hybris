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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UnlocalizedFeatureTest
{
	private final static String CODE = "Foo";
	@Mock
	private FeatureValue value1;
	@Mock
	private FeatureValue value2;
	@Mock
	private ClassAttributeAssignmentModel assignment;
	@Mock
	private ClassificationAttributeModel classificationAttribute;
	@Mock
	private ClassificationSystemVersionModel systemVersion;
	@Mock
	private ClassificationSystemModel system;
	@Mock
	private ClassificationClassModel classificationClass;
	private UnlocalizedFeature feature;
	private List<FeatureValue> values;

	@Before
	public void setUp() throws Exception
	{
		values = Lists.newArrayList(value1, value2);
	}


	@Test
	public void shouldReturnClassificationAttributeNameAsNameOfTypedFeature()
	{
		// given
		given(assignment.getClassificationAttribute()).willReturn(classificationAttribute);
		given(classificationAttribute.getName()).willReturn(CODE);

		// when
		feature = new UnlocalizedFeature(assignment, Collections.EMPTY_LIST);
		final String name = feature.getName();

		// then
		assertThat(name).isEqualTo(CODE);
		verify(classificationAttribute, times(1)).getName();
	}

	@Test
	public void shouldReturnComputedCodeFromAssignmentAsCodeOfTypedFeature()
	{
		// given
		given(assignment.getSystemVersion()).willReturn(systemVersion);
		given(assignment.getClassificationClass()).willReturn(classificationClass);
		given(assignment.getClassificationAttribute()).willReturn(classificationAttribute);
		given(systemVersion.getCatalog()).willReturn(system);
		given(systemVersion.getVersion()).willReturn("someVer");
		given(system.getId()).willReturn("myID");
		given(classificationClass.getCode()).willReturn("classificationClassCODE");
		given(classificationAttribute.getCode()).willReturn("classificationAttributeCODE");

		// when
		feature = new UnlocalizedFeature(assignment, Collections.EMPTY_LIST);
		final String code = feature.getCode();

		// then
		assertThat(code).isEqualTo("myID/someVer/classificationClassCODE.classificationattributecode");
		verify(assignment, times(3)).getSystemVersion();
		verify(assignment, times(1)).getClassificationClass();
		verify(assignment, times(1)).getClassificationAttribute();
		verify(systemVersion, times(1)).getCatalog();
		verify(systemVersion, times(1)).getVersion();
		verify(system, times(1)).getId();
		verify(classificationClass, times(1)).getCode();
		verify(classificationAttribute, times(1)).getCode();
	}

	@Test
	public void shouldReturnFixedCodeOfUntypedFeature()
	{

		// when
		feature = new UnlocalizedFeature(CODE, Collections.EMPTY_LIST);
		final String code = feature.getCode();

		// then
		assertThat(code).isEqualTo(CODE);
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenInstantiatingTypedFeatureWithoutAssignment()
	{
		// given
		final ClassAttributeAssignmentModel assignment = null;

		try
		{
			// when
			new UnlocalizedFeature(assignment, Collections.EMPTY_LIST);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Assignment is required for instantiation of typed feature");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenInstantiatingUntypedFeatureWithoutCode()
	{
		// given
		final String code = null;

		try
		{
			// when
			new UnlocalizedFeature(code, Collections.EMPTY_LIST);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("Code is required for instantiation of untyped feature");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#UnlocalizedFeature(java.lang.String, java.util.List)}
	 * .
	 */
	@Test
	public void shouldCreateFeatureWithEmptyListOfValuesWhenPassedListOfValuesIsNull()
	{
		// when
		feature = new UnlocalizedFeature(CODE);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).isEmpty();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#UnlocalizedFeature(java.lang.String, java.util.List)}
	 * .
	 */
	@Test
	public void shouldCreateFeature()
	{
		// when
		feature = new UnlocalizedFeature(CODE, values);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).isNotEmpty();
		assertThat(feature.getValues()).isEqualTo(values);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#getValues()}.
	 */
	@Test
	public void shouldReturnUnmodifableListOfValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);

		// when
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isNotEmpty();
		assertThat(result).containsOnly(value1, value2);
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
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#getValues()}.
	 */
	@Test
	public void shouldReturnEmptyUnmodifableListOfValuesWhenThereIsNoValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE);

		// when
		final List<FeatureValue> result = feature.getValues();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
		try
		{
			result.add(value1);
			fail("Should not allow modifying of collection");
		}
		catch (final UnsupportedOperationException e)
		{
			// That's OK
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#getValue()}.
	 */
	@Test
	public void shouldReturnNullValueWhenThereIsEmptyListOfValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE);

		// when
		final FeatureValue value = feature.getValue();

		// then
		assertThat(value).isNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#getValue()}.
	 */
	@Test
	public void shouldReturnFirstValueWhenThereIsListOfValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);

		// when
		final FeatureValue value = feature.getValue();

		// then
		assertThat(value).isNotNull();
		assertThat(value).isEqualTo(value1);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldAddValueToListOfValues()
	{
		// given
		final FeatureValue newValue = mock(FeatureValue.class);
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).hasSize(2);
		assertThat(feature.getValues()).containsOnly(value1, value2);

		// when
		feature.addValue(newValue);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(3);
		assertThat(feature.getValues()).containsOnly(value1, value2, newValue);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenValueIsNull()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);

		try
		{
			// when
			feature.addValue(null);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("fvalue is null");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenIndexIsGreaterThanCurrentValuesSize()
	{
		// given
		final FeatureValue newValue = mock(FeatureValue.class);
		feature = new UnlocalizedFeature(CODE, values);

		try
		{
			// when
			feature.addValue(3, newValue);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("index is not in range of: 0 and 2");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenIndexIsLowerThanZero()
	{
		// given
		final FeatureValue newValue = mock(FeatureValue.class);
		feature = new UnlocalizedFeature(CODE, values);

		try
		{
			// when
			feature.addValue(-1, newValue);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("index is not in range of: 0 and 2");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldSetNewValueOnIndexOfTwo()
	{
		// given
		final FeatureValue newValue = mock(FeatureValue.class);
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(2);

		// when
		feature.addValue(2, newValue);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(3);
		assertThat(feature.getValues().get(2)).isEqualTo(newValue);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#addValue(int, de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldSetNewValueOnIndexOfZero()
	{
		// given
		final FeatureValue newValue = mock(FeatureValue.class);
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(2);

		// when
		feature.addValue(0, newValue);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(3);
		assertThat(feature.getValues().get(0)).isEqualTo(newValue);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#removeValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldRemoveValueFromListOfValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(2);

		// when
		feature.removeValue(value1);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValues()).containsOnly(value2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.UnlocalizedFeature#removeValue(de.hybris.platform.classification.features.FeatureValue)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenValueToRemoveIsNull()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);

		try
		{
			// when
			feature.removeValue(null);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("fvalue is null");
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#removeAllValues()}.
	 */
	@Test
	public void shouldRemoveAllValuesFromCurrentListOfValues()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(2);

		// when
		feature.removeAllValues();

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(0);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#setValues(java.util.List)}.
	 */
	@Test
	public void shouldSetNewListOfValuesReplacingOldOneCompletely()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(2);
		final List<FeatureValue> newValues = Lists.newArrayList(mock(FeatureValue.class), mock(FeatureValue.class),
				mock(FeatureValue.class));

		// when
		feature.setValues(newValues);

		// then
		assertThat(feature.getValues()).isNotNull();
		assertThat(feature.getValues()).hasSize(3);
		assertThat(feature.getValues()).isNotEqualTo(values);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.UnlocalizedFeature#setValues(java.util.List)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenListOfValuesIsNull()
	{
		// given
		feature = new UnlocalizedFeature(CODE, values);

		try
		{
			// when
			feature.setValues(null);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("fvalues is null");
		}
	}
}
