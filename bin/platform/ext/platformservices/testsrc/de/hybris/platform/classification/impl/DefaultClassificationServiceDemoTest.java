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
package de.hybris.platform.classification.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.fest.assertions.Condition;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@DemoTest
public class DefaultClassificationServiceDemoTest extends ServicelayerTransactionalTest
{
	@Resource
	private ClassificationService classificationService;
	@Resource
	private ModelService modelService;
	@Resource
	private ClassificationSystemService classificationSystemService;
	@Resource
	private ClassificationClassesResolverStrategy classificationClassesResolverStrategy;
	@Resource
	private ProductService productService;

	private ProductModel product;
	private ClassAttributeAssignmentModel assignment;
	private ClassificationAttributeUnitModel kg, g; //NOPMD
	private final Locale english = new Locale("en");
	private final Locale german = new Locale("de");
	private final Locale currentLocale = english;

	@Before
	public void setUp() throws Exception
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		createCoreData();
		createDefaultCatalog();
		createHardwareCatalog();
		createClassificationAttributeUnits();
	}


	@Test
	public void shouldReadLocalizedValuesFromFeature()
	{
		// create localized and untyped one with map of values (locale as key and list of values as value)
		final FeatureValue value1 = new FeatureValue(Double.valueOf(777), "some description", g);
		final FeatureValue value2 = new FeatureValue(Double.valueOf(456.789), "another description", kg);

		final FeatureValue value3 = new FeatureValue(Double.valueOf(777), "einige Beschreibung", g);
		final FeatureValue value4 = new FeatureValue(Double.valueOf(456.789), "weitere Beschreibung", kg);

		final Map<Locale, List<FeatureValue>> localizedValues = new HashMap<Locale, List<FeatureValue>>();
		localizedValues.put(english, Lists.newArrayList(value1, value2));
		localizedValues.put(german, Lists.newArrayList(value3, value4));

		final LocalizedFeature feature = new LocalizedFeature("feature2", localizedValues, currentLocale);

		// check values
		assertThat(feature.getValues(english)).containsOnly(value1, value2);
		assertThat(feature.getValues(german)).containsOnly(value3, value4);
	}

	@Test
	public void shouldAddLocalizedValueToExistingListOfValuesForParticularLocale()
	{
		// create localized and untyped one with map of values (locale as key and list of values as value)
		final FeatureValue value1 = new FeatureValue(Double.valueOf(777), "some description", g);
		final FeatureValue value2 = new FeatureValue(Double.valueOf(456.789), "another description", kg);

		final FeatureValue value3 = new FeatureValue(Double.valueOf(777), "einige Beschreibung", g);
		final FeatureValue value4 = new FeatureValue(Double.valueOf(456.789), "weitere Beschreibung", kg);

		final Map<Locale, List<FeatureValue>> localizedValues = new HashMap<Locale, List<FeatureValue>>();
		localizedValues.put(english, Lists.newArrayList(value1, value2));
		localizedValues.put(german, Lists.newArrayList(value3, value4));

		final LocalizedFeature feature = new LocalizedFeature("feature2", localizedValues, currentLocale);

		// check values
		assertThat(feature.getValues(english)).containsOnly(value1, value2);
		assertThat(feature.getValues(german)).containsOnly(value3, value4);

		// add new value for locale en
		final FeatureValue value5 = new FeatureValue(Double.valueOf(666.777), "really cool description", kg);
		feature.addValue(value5, english);

		// check again
		assertThat(feature.getValues(english)).containsOnly(value1, value2, value5);
	}


	@Test
	public void shouldCreateAndStoreUntypedFeatureListWithOneFeatureOfTwoBasicValues()
	{
		product = createSampleProduct("fooBar");

		// create unlocalized and untyped feature
		final FeatureValue value1 = new FeatureValue(Double.valueOf(555));
		final FeatureValue value2 = new FeatureValue(Double.valueOf(123.456));
		final Feature feature1 = new UnlocalizedFeature("feature1", value1, value2);

		// create list of features
		final FeatureList featureList = new FeatureList(feature1);

		// store list
		classificationService.setFeatures(product, featureList);

		// load all the features from product
		final FeatureList resultList = classificationService.getFeatures(product);

		// check results
		assertThat(resultList.getFeatures()).hasSize(1);
		assertThat(resultList.getFeatureByCode("feature1").getValues()).hasSize(2);
	}

	@Test
	public void shouldCreateAndStoreUntypedFeatureList()
	{
		product = createSampleProduct("fooBar");

		// create unlocalized and untyped feature
		final FeatureValue value1 = new FeatureValue(Double.valueOf(555), "some description", g);
		final FeatureValue value2 = new FeatureValue(Double.valueOf(123.456), "another description", kg);
		final Feature feature1 = new UnlocalizedFeature("feature1", Lists.newArrayList(value1, value2));

		// create localized and untyped one with map of values (locale as key and list of values as value)
		final FeatureValue value3 = new FeatureValue(Double.valueOf(777), "some description", g);
		final FeatureValue value4 = new FeatureValue(Double.valueOf(456.789), "another description", kg);

		final FeatureValue value5 = new FeatureValue(Double.valueOf(777), "einige Beschreibung", g);
		final FeatureValue value6 = new FeatureValue(Double.valueOf(456.789), "weitere Beschreibung", kg);

		final Map<Locale, List<FeatureValue>> localizedValues = new HashMap<Locale, List<FeatureValue>>();
		localizedValues.put(english, Lists.newArrayList(value3, value4));
		localizedValues.put(german, Lists.newArrayList(value5, value6));

		final Feature feature2 = new LocalizedFeature("feature2", localizedValues, currentLocale);

		// create list of features
		final FeatureList featureList = new FeatureList(Lists.newArrayList(feature1, feature2));

		// right until now nothing has been written to the database !
		// we have to persist the whole container at once by using ClassificationService injected by Spring:
		classificationService.setFeatures(product, featureList);

		// load again all the features from product
		final FeatureList resultList = classificationService.getFeatures(product);

		// now check results
		assertThat(resultList.isEmpty()).isFalse();
		// should be 2 because we've created 2 Feature objects
		assertThat(resultList.getFeatures()).hasSize(2);

		// feature with code "feature1" should be unlocalized one
		assertThat(resultList.getFeatureByCode("feature1")).isInstanceOf(UnlocalizedFeature.class);
		// and should contain two values
		assertThat(resultList.getFeatureByCode("feature1").getValues()).hasSize(2);

		// feature with code "feature2" should be localized one
		assertThat(resultList.getFeatureByCode("feature2")).isInstanceOf(LocalizedFeature.class);
		// and should contain two values for language "en"
		assertThat(((LocalizedFeature) resultList.getFeatureByCode("feature2")).getValues(new Locale("en"))).hasSize(2);
		// and should contain two values for language "de"
		assertThat(((LocalizedFeature) resultList.getFeatureByCode("feature2")).getValues(new Locale("de"))).hasSize(2);
	}

	@Test
	public void shouldCreateAndStoreUntypedFeatureListUsingVarargsConstructor()
	{
		product = createSampleProduct("fooBar");

		// create two unlocalized and untyped features
		final UnlocalizedFeature feature1 = new UnlocalizedFeature("feature1", new FeatureValue(Double.valueOf(555)),
				new FeatureValue(Double.valueOf(123.456)));
		final UnlocalizedFeature feature2 = new UnlocalizedFeature("feature2", new FeatureValue("FooBar"), new FeatureValue(
				Boolean.TRUE));

		// create list of features
		final FeatureList featureList = new FeatureList(feature1, feature2);

		// store list of features
		classificationService.setFeatures(product, featureList);

		// load all the features from product
		final FeatureList resultList = classificationService.getFeatures(product);

		// check values

		// we can do check isNotEmpty() directly on resultList because it implements Iterable
		assertThat(resultList).isNotEmpty().hasSize(2);
	}

	@Test
	public void shouldCreateAndStoreTypedFeatureListByAddingFeatureValuesToExistingOnes()
	{
		product = productService.getProductForCode("HW2300-2356");
		assignment = getAssignment(product);

		// create unlocalized and untyped feature
		final FeatureValue value1 = new FeatureValue(Double.valueOf(555), "some description", g);
		final FeatureValue value2 = new FeatureValue(Double.valueOf(123.456), "another description", kg);
		final Feature feature1 = new UnlocalizedFeature(assignment, Lists.newArrayList(value1, value2));

		// create localized and untyped one with map of values (locale as key and list of values as value)
		final FeatureValue value3 = new FeatureValue(Double.valueOf(777), "some description", g);
		final FeatureValue value4 = new FeatureValue(Double.valueOf(456.789), "another description", kg);

		final FeatureValue value5 = new FeatureValue(Double.valueOf(777), "einige Beschreibung", g);
		final FeatureValue value6 = new FeatureValue(Double.valueOf(456.789), "weitere Beschreibung", kg);

		final Map<Locale, List<FeatureValue>> localizedValues = new HashMap<Locale, List<FeatureValue>>();
		localizedValues.put(english, Lists.newArrayList(value3, value4));
		localizedValues.put(german, Lists.newArrayList(value5, value6));

		final Feature feature2 = new LocalizedFeature(assignment, localizedValues, currentLocale);

		// create list of features
		final List<Feature> features = Lists.newArrayList(feature1, feature2);
		final FeatureList featureList = new FeatureList(features);

		// load all the features from product before we'll add new ones
		final FeatureList resultListBeforeStoring = classificationService.getFeatures(product);
		assertThat(resultListBeforeStoring.isEmpty()).isFalse();
		assertThat(resultListBeforeStoring.getFeatureByCode(feature1.getCode()).getValues()).hasSize(1);

		// right until now nothing has been written to the database !
		// we have to persist the whole container at once by using ClassificationService injected by Spring:
		classificationService.setFeatures(product, featureList);

		// load again all the features from product
		final FeatureList resultList = classificationService.getFeatures(product);

		// now check results
		assertThat(resultList.isEmpty()).isFalse();
		assertThat(resultList.getFeatures()).hasSize(13);
		assertThat(resultList.getFeatureByCode(feature1.getCode()).getValues()).hasSize(7);
	}

	@Test
	public void shouldModifyExistingValuesForFeature()
	{
		product = productService.getProductForCode("HW2300-2356");
		final FeatureList featureList = classificationService.getFeatures(product);
		final Feature feature = featureList.getFeatureByCode("SampleClassification/1.0/graphics.resolutions");

		// get first value from the list (in this test we do not care which one)
		final FeatureValue value = feature.getValue();
		// backup old value
		final Object backup = value.getValue();
		// change the value
		value.setValue("2048x1280");

		// store list
		classificationService.setFeatures(product, featureList);

		// load feature list again
		final FeatureList featureListAfter = classificationService.getFeatures(product);
		final Feature featureAfter = featureListAfter.getFeatureByCode("SampleClassification/1.0/graphics.resolutions");

		// check results (list of feature values should not contain one with old value) 
		assertThat(featureAfter.getValues()).doesNotSatisfy(new Condition<List<?>>()
		{

			@Override
			public boolean matches(final List<?> featureValues)
			{
				as("FeatureValue#value equals " + backup);

				for (final Object val : featureValues)
				{
					if (((FeatureValue) val).getValue().equals(backup))
					{
						return true;
					}
				}
				return false;
			}
		});
	}

	@Test
	public void shouldRemoveAllExistingFeatureValuesForFeature()
	{
		product = productService.getProductForCode("HW2300-2356");
		final FeatureList featureList = classificationService.getFeatures(product);
		final Feature feature = featureList.getFeatureByCode("SampleClassification/1.0/graphics.resolutions");

		// feature with code "SampleClassification/1.0/graphics.resolutions" should have 5 values
		assertThat(feature.getValues()).hasSize(5);

		// remove all values
		feature.removeAllValues();

		// store list 
		classificationService.replaceFeatures(product, featureList);

		// load feature list again
		final FeatureList featureListAfter = classificationService.getFeatures(product);
		final Feature featureAfter = featureListAfter.getFeatureByCode("SampleClassification/1.0/graphics.resolutions");

		// check results

		// both lists (before remove, and after) should have the same size 
		assertThat(featureList.getFeatures().size()).isEqualTo(featureListAfter.getFeatures().size());
		// but one feature should have empty list of values
		assertThat(featureAfter.getValues()).isEmpty();
	}

	private ProductModel createSampleProduct(final String code)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setCatalogVersion(classificationSystemService.getSystemVersion("SampleClassification", "1.0"));
		modelService.save(product);
		return product;
	}

	private ClassAttributeAssignmentModel getAssignment(final ProductModel product)
	{
		final ClassificationClassModel classificationClass = product.getClassificationClasses().iterator().next();
		final List<ClassAttributeAssignmentModel> result = classificationClassesResolverStrategy
				.getDeclaredClassAttributeAssignments(Sets.newHashSet(classificationClass));
		return result.get(0);
	}

	private void createClassificationAttributeUnits()
	{
		kg = modelService.create(ClassificationAttributeUnitModel.class);
		kg.setCode("kg");
		kg.setSymbol("kg");
		kg.setName("kg", english);
		kg.setSystemVersion(classificationSystemService.getSystemVersion("SampleClassification", "1.0"));
		modelService.save(kg);

		g = modelService.create(ClassificationAttributeUnitModel.class);
		g.setCode("g");
		g.setSymbol("g");
		g.setName("g", english);
		g.setSystemVersion(classificationSystemService.getSystemVersion("SampleClassification", "1.0"));
		modelService.save(g);
	}
}
