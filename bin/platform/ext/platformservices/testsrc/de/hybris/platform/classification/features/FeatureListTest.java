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
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class FeatureListTest
{

	@Mock
	private ClassAttributeAssignmentModel assignment1, assignment2, assignment3;
	@Mock
	private ClassificationClassModel classificationClassModel;
	private FeatureList featureList;

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#FeatureList(java.util.List)}.
	 */
	@Test
	public void shouldInstantiateFeatureListWithListOfFeatures()
	{
		// given
		final Feature feature1 = new UnlocalizedFeature("feature1", new FeatureValue(Boolean.TRUE), new FeatureValue("Foo"));
		final Feature feature2 = new UnlocalizedFeature("feature2", new FeatureValue(Boolean.FALSE), new FeatureValue("Bar"));

		// when
		featureList = new FeatureList(Lists.newArrayList(feature1, feature2));

		// then
		assertThat(featureList).isNotNull();
		assertThat(featureList).isNotEmpty();
		assertThat(featureList).hasSize(2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#FeatureList(java.util.List)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenInstantiatingFeatureListWithNullList()
	{
		// given
		final List<Feature> features = null;

		try
		{
			// when
			new FeatureList(features);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).isEqualTo("features list must not be null!");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.FeatureList#FeatureList(de.hybris.platform.classification.features.Feature[])}
	 * .
	 */
	@Test
	public void shouldInstantiateFeatureListWithVarargs()
	{
		// given
		final Feature feature1 = new UnlocalizedFeature("feature1", new FeatureValue(Boolean.TRUE), new FeatureValue("Foo"));
		final Feature feature2 = new UnlocalizedFeature("feature2", new FeatureValue(Boolean.FALSE), new FeatureValue("Bar"));

		// when
		featureList = new FeatureList(feature1, feature2);

		// then
		assertThat(featureList).isNotNull();
		assertThat(featureList).isNotEmpty();
		assertThat(featureList).hasSize(2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getFeatures()}.
	 */
	@Test
	public void shouldReturnListOfFeatures()
	{
		// given
		final Feature feature1 = new UnlocalizedFeature("feature1", new FeatureValue(Boolean.TRUE), new FeatureValue("Foo"));
		final Feature feature2 = new UnlocalizedFeature("feature2", new FeatureValue(Boolean.FALSE), new FeatureValue("Bar"));
		featureList = new FeatureList(Lists.newArrayList(feature1, feature2));

		// when
		final List<Feature> features = featureList.getFeatures();

		// then
		assertThat(features).isNotNull();
		assertThat(features).isNotEmpty().hasSize(2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#isEmpty()}.
	 */
	@Test
	public void shouldCheckWhetherListIsEmptyAndReturnTrueIfListIsEmpty()
	{
		// given
		featureList = new FeatureList(Collections.EMPTY_LIST);

		// when
		final boolean isEmpty = featureList.isEmpty();

		// then
		assertThat(isEmpty).isTrue();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#isEmpty()}.
	 */
	@Test
	public void shouldCheckWhetherListIsEmptyAndReturnFalseIfListIsNotEmpty()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature("foo"));

		// when
		final boolean isEmpty = featureList.isEmpty();

		// then
		assertThat(isEmpty).isFalse();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getFeatureByName(java.lang.String)}.
	 */
	@Test
	public void shouldReturnExistingFeatureByName()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature("bar"));

		// when
		final Feature fooFeature = featureList.getFeatureByName("foo");
		final Feature bazFeature = featureList.getFeatureByName("baz");

		// then
		assertThat(fooFeature).isNotNull();
		assertThat(fooFeature.getName()).isEqualTo("foo");
		assertThat(bazFeature).isNull();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.features.FeatureList#getFeatureByAssignment(de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel)}
	 * .
	 */
	@Test
	public void shouldReturnExistingFeatureByAssignment()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature(assignment1), new UnlocalizedFeature(assignment2));

		// when
		final Feature fooFeature = featureList.getFeatureByAssignment(assignment1);
		final Feature bazFeature = featureList.getFeatureByAssignment(assignment3);

		// then
		assertThat(fooFeature).isNotNull();
		assertThat(fooFeature.getClassAttributeAssignment()).isEqualTo(assignment1);
		assertThat(bazFeature).isNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getFeatureByCode(java.lang.String)}.
	 */
	@Test
	public void shouldReturnExistingFeatureByCode()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature("bar"));

		// when
		final Feature fooFeature = featureList.getFeatureByCode("foo");
		final Feature bazFeature = featureList.getFeatureByCode("baz");

		// then
		assertThat(fooFeature).isNotNull();
		assertThat(fooFeature.getCode()).isEqualTo("foo");
		assertThat(bazFeature).isNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#iterator()}.
	 */
	@Test
	public void shouldBehavesLikeNormalListWithIterator()
	{
		//	given
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature("bar"));

		// when
		final Iterator<Feature> iterator = featureList.iterator();

		// then
		assertThat(iterator).isNotNull();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getClassificationClasses()}.
	 */
	@Test
	public void shouldReturnListOfClassificationClassesForTypedFeatures()
	{
		// given
		given(assignment2.getClassificationClass()).willReturn(classificationClassModel);
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature(assignment2));

		// when
		final Set<ClassificationClassModel> classificationClasses = featureList.getClassificationClasses();

		// then
		assertThat(classificationClasses).isNotNull().containsOnly(classificationClassModel);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getClassificationClasses()}.
	 */
	@Test
	public void shouldReturnEmptyListOfClassificationClassesWhenThereIsEmptyListOfFeatures()
	{
		// given
		featureList = new FeatureList(Collections.EMPTY_LIST);

		// when
		final Set<ClassificationClassModel> classificationClasses = featureList.getClassificationClasses();

		// then
		assertThat(classificationClasses).isNotNull().isEmpty();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getClassificationClasses()}.
	 */
	@Test
	public void shouldReturnEmptyListOfClassificationClassesWhenListOfFeaturesContainsOnlyUntypedFeatures()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature("bar"));

		// when
		final Set<ClassificationClassModel> classificationClasses = featureList.getClassificationClasses();

		// then
		assertThat(classificationClasses).isNotNull().isEmpty();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#getClassAttributeAssignments()}.
	 */
	@Test
	public void shouldReturnClassAttributeAssignmentsForTypedFeatures()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature(assignment1), new UnlocalizedFeature(assignment2));

		// when
		final Set<ClassAttributeAssignmentModel> classAttributeAssignments = featureList.getClassAttributeAssignments();

		// then
		assertThat(classAttributeAssignments).isNotNull().isNotEmpty();
		assertThat(classAttributeAssignments).containsOnly(assignment1, assignment2);
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#hasUntypedFeatures()}.
	 */
	@Test
	public void shouldCheckWhetherListContainsUntypedFeaturesAndReturnTrueIfListContainUntypedFeatures()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature("foo"), new UnlocalizedFeature(assignment2));

		// when
		final boolean hasUntypedFeatures = featureList.hasUntypedFeatures();

		// then
		assertThat(hasUntypedFeatures).isTrue();
	}

	/**
	 * Test method for {@link de.hybris.platform.classification.features.FeatureList#hasUntypedFeatures()}.
	 */
	@Test
	public void shouldCheckWhetherListContainsUntypedFeaturesAndReturnFalseIfListDoesNotContainUntypedFeatures()
	{
		// given
		featureList = new FeatureList(new UnlocalizedFeature(assignment1), new UnlocalizedFeature(assignment2));

		// when
		final boolean hasUntypedFeatures = featureList.hasUntypedFeatures();

		// then
		assertThat(hasUntypedFeatures).isFalse();
	}
}
