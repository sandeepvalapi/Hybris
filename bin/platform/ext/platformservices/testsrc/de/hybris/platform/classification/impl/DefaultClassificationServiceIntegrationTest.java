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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.daos.ClassificationDao;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.classification.filter.FilterAttribute;
import de.hybris.platform.classification.filter.FilterAttributeValue;
import de.hybris.platform.classification.filter.ProductFilter;
import de.hybris.platform.classification.filter.ProductFilterResult;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultClassificationServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String WEIGHT_LVL4_VAL = "level4weightValue";
	private static final String WEIGHT_LVL3_VAL = "level3weightValue";
	private static final String WEIGHT_LVL2_VAL = "level2weightValue";
	private static final String WEIGHT_LVL1_VAL = "level1weightValue";
	private static final String FEAT_LVL4_CODE = "SampleClassification/1.0/level4.weight";
	private static final String FEAT_LVL3_CODE = "SampleClassification/1.0/level3.weight";
	private static final String FEAT_LVL2_CODE = "SampleClassification/1.0/level2.weight";
	private static final String FEAT_LVL1_CODE = "SampleClassification/1.0/level1.weight";
	@Resource
	private CategoryService categoryService;
	@Resource
	private CatalogService catalogService;
	@Resource
	private ClassificationService classificationService;
	@Resource
	private ProductService productService;
	@Resource
	private ClassificationDao classificationDao;
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ClassificationSystemService classificationSystemService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
	}

	@Test
	public void testClassificationFeaturesReadAndWrite() //NOPMD
	{
		getFeaturesTest("HW2300-2356", ""); //grafikkarte
		getFeaturesTest("HW1210-3411", ""); //prozz
		getFeaturesTest("HW2110-0012", ""); //kamera
		getFeatureTest("HW2110-0012"); //kamera		
		setFeaturesTest("HW2300-2356");
		setFeaturesTest("HW1210-3411");
		setFeaturesTest("HW2110-0012");
	}

	@Test
	public void testClassificationClasses()
	{
		final ProductModel productModel = productService.getProductForCode("HW2300-2356");
		final FeatureList modelFeatureList = classificationService.getFeatures(productModel);

		final Set<ClassificationClassModel> expectedSet = modelFeatureList.getClassificationClasses();
		int actualCounter = 0;

		for (final de.hybris.platform.classification.features.Feature feature : modelFeatureList.getFeatures())
		{
			if (feature.getClassAttributeAssignment() != null)
			{
				assertTrue("modelFeatureList.getClassificationClasses() does not contain the current ClassificationClass",
						expectedSet.contains(feature.getClassAttributeAssignment().getClassificationClass()));
				actualCounter++;
			}
		}
		assertTrue("size do not match", (actualCounter == 0 && expectedSet.isEmpty()) || expectedSet.size() < actualCounter);
	}

	private void getFeaturesTest(final String identifier, final String descprefix)
	{

		final ProductModel productModel = productService.getProductForCode(identifier);
		final Product jaloProduct = modelService.getSource(productModel);

		assertEquals("code " + identifier + " not equal", identifier, jaloProduct.getCode());

		final FeatureContainer jaloFeatureContainer = FeatureContainer.load(jaloProduct);
		final FeatureList modelFeatureList = classificationService.getFeatures(productModel);

		final List<? extends de.hybris.platform.catalog.jalo.classification.util.Feature> jaloFeatures = jaloFeatureContainer
				.getFeatures();

		assertEquals("jaloFeatureContainer/FeatureList for " + identifier + "doesn't have same size", jaloFeatures.size(),
				modelFeatureList.getFeatures().size());

		for (final de.hybris.platform.classification.features.Feature modelFeature : modelFeatureList.getFeatures())
		{
			final de.hybris.platform.catalog.jalo.classification.util.Feature jaloFeature = jaloFeatureContainer
					.getFeature(modelFeature.getCode());
			assertNotNull("Product " + identifier + " contains no jalo feature", jaloFeature);

			final List<de.hybris.platform.catalog.jalo.classification.util.FeatureValue> jaloValueList = jaloFeature.getValues();
			final List<FeatureValue> modelValueList = modelFeature.getValues();

			for (int i = 0; i < modelValueList.size(); i++)
			{
				final de.hybris.platform.classification.features.FeatureValue modelValue = modelValueList.get(i);
				final de.hybris.platform.catalog.jalo.classification.util.FeatureValue jaloValue = jaloValueList.get(i);

				assertEquals("product:" + identifier + " feature no.:" + i + " description is not equal",
						modelValue.getDescription(), jaloValue.getDescription());
				if (descprefix.length() > 0)
				{
					assertNotNull("desc is null", jaloValue.getDescription());
					assertTrue("desc didn't start with the prefix", jaloValue.getDescription().startsWith(descprefix));
				}

				final Object value = modelValue.getValue();
				if (value instanceof ClassificationAttributeValueModel)
				{
					assertEquals("product:" + identifier + " feature no.:" + i + " ClassificationAttributeValue is not equal",
							modelService.getSource(value), jaloValue.getValue());
				}
				else
				{
					assertEquals("product:" + identifier + " feature no.:" + i + " value is not equal", modelValue.getValue(),
							jaloValue.getValue());
				}

				final ClassificationAttributeUnitModel claum = modelValue.getUnit();
				final ClassificationAttributeUnit clau = jaloValue.getUnit();
				if (claum == null)
				{
					assertNull("product:" + identifier + " feature no.:" + i + " unit for jalo product is not null", clau);
				}
				else
				{
					assertEquals("product:" + identifier + " feature no.:" + i + " units are not equal",
							modelService.getSource(claum), clau);
				}
			}
		}
	}

	private void setFeaturesTest(final String identifier)
	{
		final String DESC_PREFIX = "XXX-";

		ClassificationAttributeUnitModel setUnit = null;


		//productmodel + modelfeaturelist holen
		final ProductModel productModel = productService.getProductForCode(identifier);
		assertEquals("", identifier, productModel.getCode());

		final FeatureList oldModelFeatureContainer = classificationService.getFeatures(productModel);
		final List<de.hybris.platform.classification.features.Feature> newFeatures = new ArrayList<de.hybris.platform.classification.features.Feature>();

		newFeatures.addAll(oldModelFeatureContainer.getFeatures());

		final FeatureList newModelFeatureContainer = new FeatureList(newFeatures);

		assertFalse("", oldModelFeatureContainer.equals(newModelFeatureContainer));
		assertEquals("", newFeatures.size(), oldModelFeatureContainer.getFeatures().size());

		//durchgehen, ueberall bissl was veraendern
		for (final de.hybris.platform.classification.features.Feature newModelFeature : newModelFeatureContainer.getFeatures())
		{
			for (final FeatureValue newModelFV : newModelFeature.getValues())
			{
				newModelFV.setDescription(DESC_PREFIX + newModelFV.getDescription());
				if (newModelFV.getValue() instanceof ClassificationAttributeValueModel)
				{
					newModelFV.setValue(new Double(0));
				}
				else if (newModelFV.getValue() instanceof String)
				{
					newModelFV.setValue(DESC_PREFIX + newModelFV.getValue());
				}
				else if (newModelFV.getValue() instanceof Boolean)
				{
					newModelFV.setValue(Boolean.FALSE);
				}
				else if (newModelFV.getValue() instanceof Number)
				{
					newModelFV.setValue("nix");
				}
				else
				{
					newModelFV.setValue(Integer.valueOf(10));
				}

				if (setUnit == null && newModelFV.getUnit() != null)
				{
					setUnit = newModelFV.getUnit();
				}

				newModelFV.setUnit(setUnit);
			}
		}

		//featurelist setzen
		classificationService.setFeatures(productModel, newModelFeatureContainer);

		//testGetFeatures aufrufen mit dem prod
		getFeaturesTest(identifier, DESC_PREFIX);

	}


	private void getFeatureTest(final String identifier)
	{
		final ProductModel productModel = productService.getProductForCode(identifier);
		final Product jaloProduct = modelService.getSource(productModel);

		assertEquals("code " + identifier + " not equal", identifier, jaloProduct.getCode());

		final List<ClassificationClass> classificationClasses = CatalogManager.getInstance().getClassificationClasses(jaloProduct);
		assertFalse("No classification classes found for product " + identifier, classificationClasses.isEmpty());

		ClassificationClass cpuClass = null;
		for (final ClassificationClass classificationClass : classificationClasses)
		{
			if (classificationClass.getCode().equals("cpu"))
			{
				cpuClass = classificationClass;
			}
		}
		assertNotNull("The classification class cpu could not be found", cpuClass);

		final List<ClassificationAttribute> attributes = cpuClass.getClassificationAttributes();
		assertFalse("No classicfiaction attributes found for classification class " + cpuClass.getName(), attributes.isEmpty());

		final ClassificationAttribute clockSpeedAttribute = cpuClass.getClassificationAttribute("clockSpeed");
		assertNotNull("The classicfiaction attribute clockSpeed could not be found", clockSpeedAttribute);

		// search for assignment
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("class", cpuClass.getPK());
		params.put("attribute", clockSpeedAttribute.getPK());

		final String query = "SELECT {" + Item.PK + "} FROM {" + CatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT + "} " + "WHERE {"
				+ ClassAttributeAssignment.CLASSIFICATIONCLASS + "}= ?class AND {" + ClassAttributeAssignment.CLASSIFICATIONATTRIBUTE
				+ "}= ?attribute";

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchQuery.setCount(-1);
		flexibleSearchQuery.setStart(0);
		flexibleSearchQuery.setNeedTotal(true);
		final SearchResult<ClassAttributeAssignmentModel> results = flexibleSearchService.search(flexibleSearchQuery);
		final List<ClassAttributeAssignmentModel> searchAssignments = results.getResult();
		assertFalse("No classification assignments found", searchAssignments.isEmpty());

		final de.hybris.platform.classification.features.Feature feature = classificationService.getFeature(productModel,
				searchAssignments.get(0));
		assertNotNull("Feature is null", feature);

		// check feature and it's values
		assertEquals("Feature code is wrong", "SampleClassification/1.0/cpu.clockspeed", feature.getCode());
		assertEquals("Feature value is wrong", new Double(2.53), feature.getValue().getValue());

		// check for product "HW1210-3411" with the wrong assignment
		final ProductModel productModel2 = productService.getProductForCode("HW1210-3411");
		final de.hybris.platform.classification.features.Feature feature2 = classificationService.getFeature(productModel2,
				searchAssignments.get(0));
		assertNull("Feature found for wrong assignment", feature2);
	}

	@Test
	public void testGetAttributeUnits()
	{
		final CatalogModel catalog = catalogService.getCatalog("SampleClassification");
		final Map<String, Object> paramsVersion = new HashMap<String, Object>();
		paramsVersion.put("catalog", catalog.getPk());
		paramsVersion.put("version", "1.0");

		// create second catalog + catalog version
		final ClassificationSystemModel classification2 = modelService.create(ClassificationSystemModel.class);
		classification2.setId("SampleClassification_two");
		modelService.save(classification2);
		assertNotNull("no catalog '" + classification2.getId() + "' found", catalogService.getCatalog(classification2.getId()));

		// create second catalog version
		final ClassificationSystemVersionModel classification2version = modelService.create(ClassificationSystemVersionModel.class);
		classification2version.setCatalog(classification2);
		classification2version.setVersion("version_two");
		modelService.save(classification2version);
		assertNotNull("no catalog version '" + classification2version.getVersion() + "' found",
				catalogService.getCatalogVersion(classification2.getId(), classification2version.getVersion()));

		final ClassificationAttributeUnitModel classificationUnit = modelService.create(ClassificationAttributeUnitModel.class);
		classificationUnit.setCode("Test Unit");
		classificationUnit.setSymbol("unit two");
		classificationUnit.setSystemVersion(classification2version);
		modelService.save(classificationUnit);

		String query = "SELECT {" + Item.PK + "} FROM {" + CatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION + "} " + "WHERE {"
				+ CatalogVersion.CATALOG + "}= ?catalog AND {" + CatalogVersion.VERSION + "}= ?version";
		FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(paramsVersion);
		flexibleSearchQuery.setCount(-1);
		flexibleSearchQuery.setStart(0);
		flexibleSearchQuery.setNeedTotal(true);
		final SearchResult<ClassificationSystemVersionModel> versionResults = flexibleSearchService.search(flexibleSearchQuery);
		final List<ClassificationSystemVersionModel> versions = versionResults.getResult();
		assertFalse("No classification units found", versions.isEmpty());

		final ClassificationSystemVersionModel systemVersion = versions.get(0);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", systemVersion.getPk());

		query = "SELECT {" + Item.PK + "} FROM {" + CatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} " + "WHERE {"
				+ ClassificationAttributeUnit.SYSTEMVERSION + "}= ?code ";

		flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchQuery.setCount(-1);
		flexibleSearchQuery.setStart(0);
		flexibleSearchQuery.setNeedTotal(true);
		final SearchResult<ClassificationAttributeUnitModel> results = flexibleSearchService.search(flexibleSearchQuery);
		final List<ClassificationAttributeUnitModel> units = results.getResult();
		assertEquals("Found units are not identical", units, classificationService.getAttributeUnits(systemVersion));

		boolean containsUnit = false;
		for (final ClassificationAttributeUnitModel unit : units)
		{
			if (unit.equals(classificationUnit))
			{
				containsUnit = true;
			}
		}
		//assertFalse("Unit from other classification system found", units.contains(classificationUnit));
		assertFalse("Unit from other classification system found", containsUnit);
	}

	@Test
	public void testSetFeatureValuesToZero() //NOPMD
	{
		testSetFeatureValuesToZero("HW2300-2356");
		testSetFeatureValuesToZero("HW1210-3411");
		testSetFeatureValuesToZero("HW2110-0012");
	}

	@Test
	public void testSetFeatureValuesToLessThanNormal() //NOPMD
	{
		testSetModelFVToLessThanJaloFV("HW2300-2356");
		testSetModelFVToLessThanJaloFV("HW1210-3411");
		testSetModelFVToLessThanJaloFV("HW2110-0012");
	}

	@Test
	public void testSetFeatureValuesToMoreThanNormal() //NOPMD
	{
		testSetModelFVToMoreThanJaloFV("HW2300-2356");
		testSetModelFVToMoreThanJaloFV("HW1210-3411");
		testSetModelFVToMoreThanJaloFV("HW2110-0012");

	}

	@Test
	public void testTest()
	{
		testSetFeatureValuesToZero("HW2300-2356");
	}

	public void testSetFeatureValuesToZero(final String identifier)
	{

		final ProductModel productModel = productService.getProductForCode(identifier);
		assertEquals("", identifier, productModel.getCode());

		final FeatureList oldModelFeatureContainer = classificationService.getFeatures(productModel);
		final List<de.hybris.platform.classification.features.Feature> newFeatures = new ArrayList<de.hybris.platform.classification.features.Feature>();

		newFeatures.addAll(oldModelFeatureContainer.getFeatures());

		final FeatureList newModelFeatureContainer = new FeatureList(newFeatures);

		assertFalse("", oldModelFeatureContainer.equals(newModelFeatureContainer));
		assertEquals("", newFeatures.size(), oldModelFeatureContainer.getFeatures().size());

		for (final de.hybris.platform.classification.features.Feature newModelFeature : newModelFeatureContainer.getFeatures())
		{
			newModelFeature.removeAllValues();
			assertEquals("", 0, newModelFeature.getValues().size());
		}

		classificationService.replaceFeatures(productModel, newModelFeatureContainer);

		//now the test
		final Product jaloProduct = modelService.getSource(productModel);
		final FeatureContainer jaloFeatureContainer = FeatureContainer.load(jaloProduct);
		final List<? extends de.hybris.platform.catalog.jalo.classification.util.Feature> jaloFeatures = jaloFeatureContainer
				.getFeatures();

		for (final de.hybris.platform.catalog.jalo.classification.util.Feature jaloFeature : jaloFeatures)
		{
			assertEquals("", 0, jaloFeature.getValues().size());
		}
	}

	public void testSetModelFVToLessThanJaloFV(final String identifier)
	{
		final List<Integer> oldFVCount = new ArrayList<Integer>();

		final ProductModel productModel = productService.getProductForCode(identifier);
		assertEquals("", identifier, productModel.getCode());

		final FeatureList modelFeatureContainer = classificationService.getFeatures(productModel);
		final List<de.hybris.platform.classification.features.Feature> newFeatures = new ArrayList<de.hybris.platform.classification.features.Feature>();

		newFeatures.addAll(modelFeatureContainer.getFeatures());

		final FeatureList newModelFeatureContainer = new FeatureList(newFeatures);

		assertFalse("", modelFeatureContainer.equals(newModelFeatureContainer));
		assertEquals("", newFeatures.size(), modelFeatureContainer.getFeatures().size());

		final List<de.hybris.platform.classification.features.Feature> liste = newModelFeatureContainer.getFeatures();
		final int newModelFeatureContSize = liste.size();

		for (int index = 0; index < newModelFeatureContSize; index++)
		{
			final de.hybris.platform.classification.features.Feature currentFeature = liste.get(index);
			final int currentSize = currentFeature.getValues().size();
			oldFVCount.add(index, Integer.valueOf(currentSize));

			if (currentSize > 0)
			{
				currentFeature.removeValue(currentFeature.getValue());
				//if featurevalues exists, delete first one

				assertEquals("", currentSize, currentFeature.getValues().size() + 1);
			}
			else
			{
				assertEquals("", currentSize, currentFeature.getValues().size());
			}


		}

		classificationService.replaceFeatures(productModel, newModelFeatureContainer);

		//test - we got the originaL old container


		final ProductModel testModel = productService.getProductForCode(identifier);
		final FeatureList testFeatureContainer = classificationService.getFeatures(testModel);

		for (int index = 0; index < testFeatureContainer.getFeatures().size(); index++)
		{
			final de.hybris.platform.classification.features.Feature testFeature = testFeatureContainer.getFeatures().get(index);
			if (oldFVCount.get(index).intValue() == 0)
			{
				assertEquals("", 0, testFeature.getValues().size());
			}
			else
			{
				assertEquals("", oldFVCount.get(index).intValue(), testFeature.getValues().size() + 1);
			}
		}

	}

	public void testSetModelFVToMoreThanJaloFV(final String identifier)
	{
		final List<Integer> oldFVCount = new ArrayList<Integer>();

		final ProductModel productModel = productService.getProductForCode(identifier);
		assertEquals("", identifier, productModel.getCode());

		final FeatureList oldModelFeatureContainer = classificationService.getFeatures(productModel);
		final List<de.hybris.platform.classification.features.Feature> newFeatures = new ArrayList<de.hybris.platform.classification.features.Feature>();

		newFeatures.addAll(oldModelFeatureContainer.getFeatures());

		final FeatureList newModelFeatureContainer = new FeatureList(newFeatures);

		assertFalse("", oldModelFeatureContainer.equals(newModelFeatureContainer));
		assertEquals("", newFeatures.size(), oldModelFeatureContainer.getFeatures().size());

		final List<de.hybris.platform.classification.features.Feature> liste = newModelFeatureContainer.getFeatures();
		final int newModelFeatureContSize = liste.size();

		for (int index = 0; index < newModelFeatureContSize; index++)
		{
			final de.hybris.platform.classification.features.Feature workFeature = liste.get(index);
			oldFVCount.add(index, Integer.valueOf(workFeature.getValues().size()));
			final int oldSize = workFeature.getValues().size();
			if (oldSize > 0)
			{
				workFeature.addValue(new FeatureValue("blah" + index, "foobar", workFeature.getValue().getUnit()));
				//if exists, add the first featurevalue again to the end of the fvaluelist
				assertEquals("", oldSize, workFeature.getValues().size() - 1);
			}
		}

		classificationService.setFeatures(productModel, newModelFeatureContainer);

		//test - we got the originaL old container


		final ProductModel testModel = productService.getProductForCode(identifier);
		final FeatureList testFeatureContainer = classificationService.getFeatures(testModel);

		for (int index = 0; index < testFeatureContainer.getFeatures().size(); index++)
		{
			final de.hybris.platform.classification.features.Feature testFeature = testFeatureContainer.getFeatures().get(index);
			if (oldFVCount.get(index).intValue() == 0)
			{
				assertEquals("", 0, testFeature.getValues().size());
			}
			else
			{
				assertEquals("", oldFVCount.get(index).intValue() + 1, testFeature.getValues().size());
			}
		}
	}


	@Test
	public void testGetProductsByFilter() throws Exception
	{
		final CategoryModel cat = categoryService.getCategory("HW1200");
		assertNotNull("Category", cat);

		final ProductFilter filter = new ProductFilter(cat);
		final double zoomOpt = 3.0;
		final String display = "2.5\"";
		final String type = "P_COMPACT";
		filter.setAttribute("zoomOpt", Double.valueOf(zoomOpt));
		filter.setAttribute("display", display);
		filter.setAttribute("type", type);

		final ProductFilterResult result = classificationService.getProductsByFilter(filter);
		final List<ProductModel> products = result.getProducts();
		assertFalse("Products must not be empty", products.isEmpty());
		final List<ClassificationAttributeValueModel> typeValues = classificationDao.findAttributeValuesByCode(type);
		assertNotNull("Types", typeValues);
		assertFalse("Types empty", typeValues.isEmpty());
		assertEquals("Size of types", 1, typeValues.size());
		final ClassificationAttributeValueModel typeValue = typeValues.get(0);

		for (final ProductModel product : products)
		{
			final Product productItem = modelService.getSource(product);
			final FeatureContainer featureContainer = FeatureContainer.load(productItem);
			assertTrue("Cannot find Zoom",
					containsValue(featureContainer.getFeature("zoomOpt").getValues(), Double.valueOf(zoomOpt)));
			assertTrue("Cannot find Display size", containsValue(featureContainer.getFeature("display").getValues(), display));
			assertTrue("Cannot find Type", containsValue(featureContainer.getFeature("type").getValues(), typeValue));
			assertTrue("Not in category", isInCategory(product, cat));
		}

		final List<FilterAttribute> possibleAttributes = result.getFilteredAttributes();
		assertTrue("Cannot find zoomOpt value as selected filter attributes",
				containsFilteredAttribute(possibleAttributes, "zoomOpt", Double.valueOf(zoomOpt)));
		assertTrue("Cannot find display value as selected filter attributes",
				containsFilteredAttribute(possibleAttributes, "display", display));
		assertTrue("Cannot find type value as selected filter attribute",
				containsFilteredAttribute(possibleAttributes, "type", typeValue));
		assertEquals("Count of filtered attributes", 3, countFilteredAttributes(possibleAttributes));
		assertSortedFilterAttributes(possibleAttributes);
		assertValidValueTypes(possibleAttributes);
	}

	/**
	 * @param possibleAttributes
	 */
	private void assertValidValueTypes(final List<FilterAttribute> possibleAttributes)
	{
		for (final FilterAttribute filterAttribute : possibleAttributes)
		{
			final ClassAttributeAssignmentModel assignment = filterAttribute.getAttributeAssignment();
			if (assignment == null)
			{
				continue;
			}
			final String typeCode = assignment.getAttributeType().getCode();
			final List<FilterAttributeValue> values = filterAttribute.getValues();
			for (final FilterAttributeValue attributeValue : values)
			{
				final Object value = attributeValue.getValue();
				if (ClassificationAttributeTypeEnum.BOOLEAN.equals(typeCode))
				{
					assertInstanceOf(value, Boolean.class);
				}
				else if (ClassificationAttributeTypeEnum.ENUM.equals(typeCode))
				{
					assertInstanceOf(value, ClassificationAttributeValueModel.class);
				}
				else if (ClassificationAttributeTypeEnum.NUMBER.equals(typeCode))
				{
					assertInstanceOf(value, Number.class);
				}
				else if (ClassificationAttributeTypeEnum.STRING.equals(typeCode))
				{
					assertInstanceOf(value, String.class);
				}
				else if (ClassificationAttributeTypeEnum.DATE.equals(typeCode))
				{
					assertInstanceOf(value, Date.class);
				}
				else
				{
					fail("Invalid classifcation attribute type code: " + typeCode);
				}

			}
		}
	}

	private void assertInstanceOf(final Object value, final Class clazz)
	{
		if (value != null)
		{
			assertTrue(String.format("Not an instance of %s: %s", clazz.getSimpleName(), value),
					clazz.isAssignableFrom(value.getClass()));
		}
	}

	private void assertSortedFilterAttributes(final List<FilterAttribute> possibleAttributes)
	{
		final int lastPosition = 0;
		for (final FilterAttribute filterAttribute : possibleAttributes)
		{
			int thisPosition = 0;
			if (filterAttribute.getAttributeAssignment() != null)
			{
				thisPosition = filterAttribute.getAttributeAssignment().getPosition().intValue();
			}
			assertTrue(
					String.format("FilterAttributes in wrong order position %d not less than or equal to %d",
							Integer.valueOf(lastPosition), Integer.valueOf(thisPosition)), thisPosition >= lastPosition);
			assertSortedFilterAttributeValues(filterAttribute.getValues());
		}
	}

	/**
	 * @param values
	 */
	private void assertSortedFilterAttributeValues(final List<FilterAttributeValue> values)
	{
		if (values.isEmpty())
		{
			return;
		}
		boolean compareable = false;
		for (final FilterAttributeValue attributeValue : values)
		{
			if (attributeValue.getValue() != null)
			{
				compareable = attributeValue.getValue() instanceof Comparable;
				break;
			}
		}
		for (final FilterAttributeValue attributeValue : values)
		{
			if (attributeValue.getValue() != null)
			{
				assertTrue("Not all values are " + (compareable ? "compareable" : "not compareable"),
						compareable == attributeValue.getValue() instanceof Comparable);
			}
		}
		if (!compareable)
		{
			return;
		}
		Comparable previousValue = new Comparable()
		{

			@Override
			public int compareTo(final Object obj)
			{
				return -1;
			}

			@Override
			public String toString()
			{
				return "Initial test value";
			}
		};

		for (final FilterAttributeValue attributeValue : values)
		{
			final Comparable value = (Comparable) attributeValue.getValue();
			final int index = previousValue.compareTo(value);
			assertTrue(String.format("Values in wrong order: %s, %s", previousValue, value), index <= 0);
			previousValue = value;
		}
	}

	@Test
	public void testGetProductsByFilterWithoutFilterAttributes() throws Exception
	{
		final CategoryModel cat = categoryService.getCategory("HW2200");
		assertNotNull("Category", cat);

		final ProductFilter filter = new ProductFilter(cat);

		final ProductFilterResult result = classificationService.getProductsByFilter(filter);
		final List<ProductModel> products = result.getProducts();
		assertFalse("Products must not be empty", products.isEmpty());

		for (final ProductModel product : products)
		{
			assertTrue("Not in category", isInCategory(product, cat));
		}

		assertEquals("Count of filtered attributes", 0, countFilteredAttributes(result.getFilteredAttributes()));
	}


	@Test
	public void testGetProductsByFilterWithLimit() throws Exception
	{
		final CategoryModel cat = categoryService.getCategory("HW1200");
		assertNotNull("Category", cat);
		final int count = 2;

		final ProductFilter filter = new ProductFilter(cat);
		final double zoomOpt = 3.0;
		filter.setAttribute("zoomOpt", Double.valueOf(zoomOpt));

		final ProductFilterResult resultWithoutLimit = classificationService.getProductsByFilter(filter);
		assertNotNull("Filter result", resultWithoutLimit);
		assertFalse("Products empty", resultWithoutLimit.getProducts().isEmpty());

		filter.setCount(count);
		filter.setStart(0);
		ProductFilterResult resultWithLimit = classificationService.getProductsByFilter(filter);
		assertNotNull("Filter result", resultWithLimit);
		assertFalse("Products empty", resultWithLimit.getProducts().isEmpty());
		assertEquals("Result count", count, resultWithLimit.getProducts().size());
		assertEquals("1st Product", resultWithoutLimit.getProducts().get(0), resultWithLimit.getProducts().get(0));
		assertEquals("1st Product", resultWithoutLimit.getProducts().get(1), resultWithLimit.getProducts().get(1));

		filter.setCount(count);
		filter.setStart(2);
		resultWithLimit = classificationService.getProductsByFilter(filter);
		assertNotNull("Filter result", resultWithLimit);
		assertFalse("Products empty", resultWithLimit.getProducts().isEmpty());
		assertEquals("Result count", count, resultWithLimit.getProducts().size());
		assertEquals("1st Product", resultWithoutLimit.getProducts().get(2), resultWithLimit.getProducts().get(0));
		assertEquals("1st Product", resultWithoutLimit.getProducts().get(3), resultWithLimit.getProducts().get(1));

	}


	@Test
	public void test_PLA_8083()
	{
		final ClassificationClassModel classificationClassModel = modelService.get(CatalogManager.getInstance()
				.getClassificationClass("SampleClassification", "1.0", "photography"));

		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion("hwcatalog", "Online");

		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCode("foo");
		productModel.setCatalogVersion(catalogVersionModel);
		productModel.setSupercategories(Collections.singletonList((CategoryModel) classificationClassModel));

		modelService.save(productModel);

		FeatureList features = classificationService.getFeatures(productModel);

		assertTrue(features.getClassificationClasses().contains(classificationClassModel));

		de.hybris.platform.classification.features.Feature sensorFeat = features
				.getFeatureByCode("SampleClassification/1.0/photography.sensor");
		sensorFeat.addValue(new FeatureValue("super sensor", "some description", null));

		classificationService.setFeatures(productModel, features);

		modelService.saveAll();

		features = classificationService.getFeatures(productModel);
		sensorFeat = features.getFeatureByCode("SampleClassification/1.0/photography.sensor");
		final FeatureValue featureValue = sensorFeat.getValue();
		assertEquals("super sensor", featureValue.getValue());
		assertEquals("some description", featureValue.getDescription());
		assertNull(featureValue.getUnit());
	}

	@Test
	public void testPLA_8081() throws ConsistencyCheckException
	{
		final ClassificationSystem classificationSystem = CatalogManager.getInstance().getClassificationSystem(
				"SampleClassification");
		final ClassificationSystemVersion version = classificationSystem.getSystemVersion("1.0");

		final ClassificationClass testClassificationCategory = version.createClass("testClassificationCategory");
		final ClassificationAttributeUnit testUnit = version.createAttributeUnit("testUnit", "t", "testUnit", 1);
		final ClassificationAttribute testFeature = version.createClassificationAttribute("testFeature");
		testFeature.setName("testFeature");
		final ClassAttributeAssignment caa = testClassificationCategory.assignAttribute(testFeature,
				ClassificationAttribute.TYPE_NUMBER, testUnit, null, 0);

		caa.setRange(true);

		final Collection<Category> categories = CategoryManager.getInstance().getCategoriesByCode("HW1200");
		assertNotNull(categories);
		assertFalse(categories.isEmpty());

		final Iterator iterator = categories.iterator();
		final Category cat = (Category) iterator.next();
		testClassificationCategory.addToCategories(cat);

		final String testProduct = "HW1210-0400";

		ProductModel productModel = productService.getProduct(testProduct);
		FeatureList featureList = classificationService.getFeatures(productModel);
		Feature feature = featureList.getFeatureByName(testFeature.getName());
		final FeatureValue lvalue = new FeatureValue(Integer.valueOf(1), "lower", feature.getClassAttributeAssignment().getUnit());
		feature.addValue(lvalue);
		final FeatureValue uvalue = new FeatureValue(Integer.valueOf(10), "upper", feature.getClassAttributeAssignment().getUnit());
		feature.addValue(uvalue);
		classificationService.setFeatures(productModel, featureList);

		assertEquals(feature.getValues().size(), classificationService.getFeatures(productService.getProduct(testProduct))
				.getFeatureByName(testFeature.getName()).getValues().size());

		productModel = productService.getProduct(testProduct);
		featureList = classificationService.getFeatures(productModel);
		feature = featureList.getFeatureByName(testFeature.getName());
		feature.removeValue(feature.getValues().get(0));

		classificationService.replaceFeatures(productModel, featureList);

		assertEquals(feature.getValues().size(), classificationService.getFeatures(productService.getProduct(testProduct))
				.getFeatureByName(testFeature.getName()).getValues().size());

	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToSetFeaturesForUnsavedProduct()
	{
		// given
		final ProductModel newProduct = createSampleProduct("fooBar");

		try
		{
			// when
			classificationService.setFeatures(newProduct, new FeatureList());
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("ProductModel is not persisted");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToReplaceFeaturesForUnsavedProduct()
	{
		// given
		final ProductModel newProduct = createSampleProduct("fooBar");

		try
		{
			// when
			classificationService.replaceFeatures(newProduct, new FeatureList());
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("ProductModel is not persisted");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToSetFeatureForUnsavedProduct()
	{
		// given
		final ProductModel newProduct = createSampleProduct("fooBar");

		try
		{
			// when
			classificationService.setFeature(newProduct, new UnlocalizedFeature("foo"));
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("ProductModel is not persisted");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToGetFeaturesForUnsavedProduct()
	{
		// given
		final ProductModel newProduct = createSampleProduct("fooBar");

		try
		{
			// when
			classificationService.getFeatures(newProduct);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("ProductModel is not persisted");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToGetFeatureForUnsavedProduct()
	{
		// given
		final ProductModel newProduct = createSampleProduct("fooBar");

		try
		{
			// when
			classificationService.getFeature(newProduct, new ClassAttributeAssignmentModel());
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e.getMessage()).contains("ProductModel is not persisted");
		}
	}

	@Test
	public void shouldProperlyReplaceFeaturesUsingFeatureListFromAnotherProductKeepingSourceUntouched()
	{
		// given
		final ProductModel prod1 = createSampleProduct("Prod1");
		final ProductModel prod2 = createSampleProduct("Prod2");
		modelService.saveAll(prod1, prod2);
		createProductFeatureForProduct(prod1, "fooBar11",0);
		createProductFeatureForProduct(prod1, "fooBar12",1);
		createProductFeatureForProduct(prod2, "fooBar21",0);
		createProductFeatureForProduct(prod2, "fooBar22",1);

		final FeatureList featList1 = classificationService.getFeatures(prod1);
		final FeatureList featList2 = classificationService.getFeatures(prod2);

		assertThat(featList1).isNotEmpty().hasSize(1);
		assertThat(featList2).isNotEmpty().hasSize(1);

		final Feature feat1 = featList1.getFeatures().get(0);
		final Feature feat2 = featList2.getFeatures().get(0);

		FeatureValuesAssert.assertThat(feat1.getValues()).containsOnlyValues("fooBar11", "fooBar12");
		FeatureValuesAssert.assertThat(feat2.getValues()).containsOnlyValues("fooBar21", "fooBar22");

		// when
		classificationService.replaceFeatures(prod1, featList2);

		// then
		final FeatureList featListAfterReplace1 = classificationService.getFeatures(prod1);
		final FeatureList featListAfterReplace2 = classificationService.getFeatures(prod2);

		assertThat(featListAfterReplace1).isNotEmpty().hasSize(1);
		assertThat(featListAfterReplace2).isNotEmpty().hasSize(1);

		final Feature featAfterReplace1 = featListAfterReplace1.getFeatures().get(0);
		final Feature featAfterReplace2 = featListAfterReplace2.getFeatures().get(0);

		FeatureValuesAssert.assertThat(featAfterReplace1.getValues()).containsOnlyValues("fooBar21", "fooBar22");
		FeatureValuesAssert.assertThat(featAfterReplace2.getValues()).containsOnlyValues("fooBar21", "fooBar22");
	}

	private ProductModel createSampleProduct(final String code)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setCatalogVersion(classificationSystemService.getSystemVersion("SampleClassification", "1.0"));
		return product;
	}

	private void createProductFeatureForProduct(final ProductModel product, final String value, int pos)
	{
		final ProductFeatureModel productFeatureModel = modelService.create(ProductFeatureModel.class);
		productFeatureModel.setProduct(product);
		productFeatureModel.setValue(value);
		productFeatureModel.setQualifier(product.getCode());
		productFeatureModel.setValuePosition(Integer.valueOf(pos));
		modelService.save(productFeatureModel);
	}

	private boolean isInCategory(final ProductModel product, final CategoryModel expectedCategory)
	{
		final Collection<CategoryModel> categories = new HashSet<CategoryModel>();
		categories.addAll(product.getSupercategories());
		for (final CategoryModel category : product.getSupercategories())
		{
			categories.addAll(category.getAllSupercategories());
		}
		for (final CategoryModel category : categories)
		{
			if (category.equals(expectedCategory))
			{
				return true;
			}
		}
		return false;
	}

	private int countFilteredAttributes(final List<FilterAttribute> possibleAttributes)
	{
		int count = 0;
		for (final FilterAttribute attribute : possibleAttributes)
		{
			for (final FilterAttributeValue attrValue : attribute.getValues())
			{
				if (attrValue.isFiltered())
				{
					count++;
				}
			}
		}
		return count;
	}

	private boolean containsFilteredAttribute(final List<FilterAttribute> possibleAttributes, final String code, final Object value)
	{
		for (final FilterAttribute attribute : possibleAttributes)
		{
			if (attribute.getCode().equals(code))
			{
				for (final FilterAttributeValue attrValue : attribute.getValues())
				{
					if (attrValue.getValue().equals(value))
					{
						return attrValue.isFiltered();
					}
				}
			}
		}
		return false;
	}

	private boolean containsValue(final List<de.hybris.platform.catalog.jalo.classification.util.FeatureValue<Object>> values,
			final Object expectedValue)
	{
		for (final de.hybris.platform.catalog.jalo.classification.util.FeatureValue<Object> featureValue : values)
		{
			Object value = featureValue.getValue();
			if (value instanceof Item)
			{
				value = modelService.get(value);
			}
			if (value.equals(expectedValue))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * testing the method classificationService.setFeature(prodmodel, feature) in different combinations
	 */
	@Test
	public void testSetSingleFeatureWithValues()
	{
		final ProductModel productModel = productService.getProductForCode("HW2300-2356");
		final Product jaloProduct = modelService.getSource(productModel);

		final FeatureList modelFeatureList = classificationService.getFeatures(productModel);

		//get three features
		final Feature changed1 = modelFeatureList.getFeatureByCode("SampleClassification/1.0/graphics.type");
		assertNotNull(changed1);
		assertEquals(1, changed1.getValues().size());
		final ClassAttributeAssignment caa1 = modelService.toPersistenceLayer(changed1.getClassAttributeAssignment());
		assertNotNull(caa1);

		final Feature changed2 = modelFeatureList.getFeatureByName("Speicher");
		assertNotNull(changed2);
		assertEquals(1, changed2.getValues().size());
		final ClassAttributeAssignment caa2 = modelService.toPersistenceLayer(changed2.getClassAttributeAssignment());
		assertNotNull(caa2);

		final Feature changed3 = modelFeatureList.getFeatureByCode("SampleClassification/1.0/graphics.resolutions");
		assertNotNull(changed3);
		assertEquals(5, changed3.getValues().size());
		final ClassAttributeAssignment caa3 = modelService.toPersistenceLayer(changed3.getClassAttributeAssignment());
		assertNotNull(caa3);

		//compare model feature with jalo feature
		de.hybris.platform.catalog.jalo.classification.util.Feature jaloChanged3 = FeatureContainer.loadTyped(jaloProduct, caa3)
				.getFeature(caa3);
		compareProductFeatureValues(changed3.getValues(), jaloChanged3.getValues()); // <- must be equals


		//1st test, set a Feature==null
		try
		{
			classificationService.setFeature(productModel, null);
			fail("missing an exception here");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("feature can't be null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}


		//change first feature 
		changed1.addValue(new FeatureValue("xxx", "desc", null));
		classificationService.setFeature(productModel, changed1);

		//get this first feature from jalo and the new values must be there
		de.hybris.platform.catalog.jalo.classification.util.Feature jaloChanged1 = FeatureContainer.loadTyped(jaloProduct, caa1)
				.getFeature(caa1);
		assertNotNull(jaloChanged1);
		assertEquals(2, jaloChanged1.getValues().size());
		final de.hybris.platform.catalog.jalo.classification.util.FeatureValue jaloChanged1FV = jaloChanged1.getValue(1);
		assertEquals("desc", jaloChanged1FV.getDescription());
		assertEquals("xxx", jaloChanged1FV.getValue());
		assertNull(jaloChanged1FV.getUnit());

		//second feature should be the same
		de.hybris.platform.catalog.jalo.classification.util.Feature jaloChanged2 = FeatureContainer.loadTyped(jaloProduct, caa2)
				.getFeature(caa2);
		compareProductFeatureValues(changed2.getValues(), jaloChanged2.getValues()); // <- must be equals

		//this does nothing to the features, therefore feature 3 must still be the same
		classificationService.setFeatures(productModel, new FeatureList(Collections.EMPTY_LIST));
		jaloChanged3 = FeatureContainer.loadTyped(jaloProduct, caa3).getFeature(caa3);
		compareProductFeatureValues(changed3.getValues(), jaloChanged3.getValues()); // <- must be equals


		//now remove all feature values by replacing with nothing
		classificationService.replaceFeatures(productModel, new FeatureList(Collections.EMPTY_LIST));
		//should contain only typed features and those are all empty
		jaloChanged1 = FeatureContainer.loadTyped(jaloProduct, caa1).getFeature(caa1);
		jaloChanged2 = FeatureContainer.loadTyped(jaloProduct, caa2).getFeature(caa2);
		jaloChanged3 = FeatureContainer.loadTyped(jaloProduct, caa3).getFeature(caa3);
		assertTrue(jaloChanged1.getValues().isEmpty());
		assertTrue(jaloChanged2.getValues().isEmpty());
		assertTrue(jaloChanged3.getValues().isEmpty());
	}

	/**
	 * Test for PLA-10797.
	 */
	@Test
	public void productOnEachLevelShouldContainsOnlyFeaturesWithValuesFromSelfLevel()
	{
		// given
		try
		{
			importCsv("/test/featuresInHierarchy-PLA-10797.csv", "UTF-8");
		}
		catch (final ImpExException e)
		{
			fail("Cannot import predefined classification structure from test file");
		}

		final ProductModel productLvl1 = productService.getProductForCode("TestProduct1");
		final ProductModel productLvl2 = productService.getProductForCode("TestProduct2");
		final ProductModel productLvl3 = productService.getProductForCode("TestProduct3");
		final ProductModel productLvl4 = productService.getProductForCode("TestProduct4");

		// when
		addFeatureVal(productLvl1, FEAT_LVL1_CODE, WEIGHT_LVL1_VAL);
		addFeatureVal(productLvl2, FEAT_LVL2_CODE, WEIGHT_LVL2_VAL);
		addFeatureVal(productLvl3, FEAT_LVL3_CODE, WEIGHT_LVL3_VAL);
		addFeatureVal(productLvl4, FEAT_LVL4_CODE, WEIGHT_LVL4_VAL);


		// then
		final FeatureList featuresLvl1 = classificationService.getFeatures(productLvl1);
		final FeatureList featuresLvl2 = classificationService.getFeatures(productLvl2);
		final FeatureList featuresLvl3 = classificationService.getFeatures(productLvl3);
		final FeatureList featuresLvl4 = classificationService.getFeatures(productLvl4);

		FeatureListAssert.assertThat(featuresLvl1).hasOnlyOneFeatureFromSelfLevel(FEAT_LVL1_CODE)
				.hasFeatureWithValue(FEAT_LVL1_CODE, WEIGHT_LVL1_VAL);
		FeatureListAssert.assertThat(featuresLvl2).hasOnlyOneFeatureFromSelfLevel(FEAT_LVL2_CODE)
				.hasFeatureWithValue(FEAT_LVL2_CODE, WEIGHT_LVL2_VAL);
		FeatureListAssert.assertThat(featuresLvl3).hasOnlyOneFeatureFromSelfLevel(FEAT_LVL3_CODE)
				.hasFeatureWithValue(FEAT_LVL3_CODE, WEIGHT_LVL3_VAL);
		FeatureListAssert.assertThat(featuresLvl4).hasOnlyOneFeatureFromSelfLevel(FEAT_LVL4_CODE)
				.hasFeatureWithValue(FEAT_LVL4_CODE, WEIGHT_LVL4_VAL);
	}

	/**
	 * Test for PLA-11992. It tests following (quite rare and complicated) case:<br />
	 * 
	 * <pre>
	 * Classification - Common Class (attributes: common_attribute)
	 * 	+- Classification - Special Class (attributes: special_attribute)
	 * 	+- Category 1
	 * 		+- Category 2
	 * 			+- Category 3
	 * 				+- Category 4
	 * 					+- Product prodInOneCtg
	 * 					+- Product prodInTwoCtgs
	 * 		+- Category 5
	 * 			+- prodInTwoCtgs
	 * </pre>
	 * 
	 * In above structure assumption is that <code>productInTwoCtgs</code> has both: common_class.common_attribute and
	 * special_class.special_attribute. <br />
	 * 
	 * 
	 * This structure is modelled in <code>featuresInHierarchy-PLA-11992.csv</code> file.
	 * 
	 */
	@Test
	public void productInTwoCategoriesShouldBeClassifiedByCorrectClassification()
	{
		// given
		try
		{
			importCsv("/test/featuresInHierarchy-PLA-11992.csv", "UTF-8");
		}
		catch (final ImpExException e)
		{
			fail("Cannot import predefined classification structure from test file");
		}

		// when
		final ProductModel prodInOneCtg = productService.getProductForCode("prodInOneCtg");
		final ProductModel prodInTwoCtgs = productService.getProductForCode("prodInTwoCtgs");
		final FeatureList features1 = classificationService.getFeatures(prodInOneCtg);
		final FeatureList features2 = classificationService.getFeatures(prodInTwoCtgs);

		// then
		assertThat(features1).isNotNull();
		assertThat(features1).isNotEmpty();
		assertThat(features1).hasSize(2);
		assertThat(features1.getFeatureByCode("classification/Online/common_class.common_attribute")).isNotNull();
		assertThat(features1.getFeatureByCode("classification/Online/special_class.special_attribute")).isNotNull();

		assertThat(features2).isNotNull();
		assertThat(features2).isNotEmpty();
		assertThat(features2).hasSize(2);
		assertThat(features2.getFeatureByCode("classification/Online/common_class.common_attribute")).isNotNull();
		assertThat(features2.getFeatureByCode("classification/Online/special_class.special_attribute")).isNotNull();
	}

	private void addFeatureVal(final ProductModel product, final String featureCode, final Object val)
	{
		final FeatureList features = classificationService.getFeatures(product);
		features.getFeatureByCode(featureCode).addValue(new FeatureValue(val));
		classificationService.setFeatures(product, features);
	}

	private void compareProductFeatureValues(final List<FeatureValue> modelFeatureValues,
			final List<de.hybris.platform.catalog.jalo.classification.util.FeatureValue> jaloFeatureValues)
	{
		assertEquals(modelFeatureValues.size(), jaloFeatureValues.size());
		for (int index = 0; index < modelFeatureValues.size(); index++)
		{
			final FeatureValue modelFV = modelFeatureValues.get(index);
			final de.hybris.platform.catalog.jalo.classification.util.FeatureValue jaloFV = jaloFeatureValues.get(index);
			assertEquals(modelFV.getDescription(), jaloFV.getDescription());
			assertEquals(modelService.toPersistenceLayer(modelFV.getValue()), jaloFV.getValue());
			assertEquals(modelService.toPersistenceLayer(modelFV.getUnit()), jaloFV.getUnit());
		}
	}

	/**
	 * Assertion for checking FeatureList behavior.
	 */
	private static class FeatureListAssert extends GenericAssert<FeatureListAssert, FeatureList>
	{
		public FeatureListAssert(final FeatureList list)
		{
			super(FeatureListAssert.class, list);
		}

		public static FeatureListAssert assertThat(final FeatureList actual)
		{
			return new FeatureListAssert(actual);
		}

		public FeatureListAssert hasOnlyOneFeatureFromSelfLevel(final String featureCode)
		{
			Assertions.assertThat(actual).hasSize(1);
			Assertions.assertThat(actual.getFeatureByCode(featureCode)).isNotNull();
			Assertions.assertThat(actual.getFeatureByCode(featureCode).getValues()).isNotNull();
			Assertions.assertThat(actual.getFeatureByCode(featureCode).getValues()).hasSize(1);
			return this;
		}

		public FeatureListAssert hasFeatureWithValue(final String featureCode, final Object value)
		{
			Assertions.assertThat(actual.getFeatureByCode(featureCode).getValue().getValue()).isEqualTo(value);
			return this;
		}
	}

	private static class FeatureValuesAssert extends GenericAssert<FeatureValuesAssert, List<FeatureValue>>
	{
		public FeatureValuesAssert(final List<FeatureValue> fValues)
		{
			super(FeatureValuesAssert.class, fValues);
		}

		public static FeatureValuesAssert assertThat(final List<FeatureValue> actual)
		{
			return new FeatureValuesAssert(actual);
		}

		public FeatureValuesAssert containsOnlyValues(final Object... values)
		{
			final List<Object> plainValues = transformListOfFeatureValuesIntoPlainValues(actual);
			Assertions.assertThat(plainValues).containsOnly(values);
			return this;
		}

		private List<Object> transformListOfFeatureValuesIntoPlainValues(final List<FeatureValue> fValues)
		{
			return Lists.transform(fValues, new Function<FeatureValue, Object>()
			{

				@Override
				public Object apply(final FeatureValue fValue)
				{
					return fValue.getValue();
				}

			});
		}
	}

}
