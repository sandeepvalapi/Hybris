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
package de.hybris.platform.catalog.jalo.classification;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.spring.CGLibUtils;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.constants.VariantsConstants;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantType;
import de.hybris.platform.variants.jalo.VariantsManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ClassUtils;


@IntegrationTest
public class ClassificationTest extends HybrisJUnit4TransactionalTest
{
	Language german, english;
	SessionContext deCtx, enCtx;

	ClassificationSystem system1, system2;
	ClassificationSystemVersion classificationSystemVersion1, classificationSystemVersion2;
	ClassificationClass clBag, clPlasticBag, clScrew;
	ClassificationAttribute attrColor, attrWeight, attrSupplier, attrLocTxt;
	ClassificationAttributeValue vRed, vGreen, vYellow;
	ClassificationAttributeUnit uKg, uM3;

	EnumerationValue T_STRING, T_BOOLEAN, T_NUMBER, T_ENUM;


	@Before
	public void setUp() throws Exception
	{
		german = getOrCreateLanguage("de");
		deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(german);
		english = getOrCreateLanguage("en");
		enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(english);

		T_STRING = EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
		T_BOOLEAN = EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
		T_NUMBER = EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
		T_ENUM = EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
				CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM);
	}

	/*
	 * System1 +-v1(de) System2 +-v2(en)
	 */
	protected void createSystems() throws ConsistencyCheckException
	{
		assertNotNull(system1 = CatalogManager.getInstance().createClassificationSystem("System1"));
		assertEquals("System1", system1.getId());
		assertNotNull(classificationSystemVersion1 = system1.createSystemVersion("v1", german));
		assertEquals("v1", classificationSystemVersion1.getVersion());
		assertEquals(system1, classificationSystemVersion1.getClassificationSystem());
		assertCollection(Collections.singleton(classificationSystemVersion1), system1.getCatalogVersions());

		assertNotNull(system2 = CatalogManager.getInstance().createClassificationSystem("System2"));
		assertNotNull(classificationSystemVersion2 = system2.createSystemVersion("v2", english));

	}

	/*
	 * System1 +-v1(de) +-bag +-plastic bag System2 +-v2(en) +-screw
	 */
	protected void createClasses() throws ConsistencyCheckException
	{
		assertNotNull(system1);
		assertNotNull(classificationSystemVersion1);
		assertNotNull(system2);
		assertNotNull(classificationSystemVersion2);

		assertNotNull(clBag = classificationSystemVersion1.createClass("bag"));
		assertEquals("bag", clBag.getCode());
		assertEquals(classificationSystemVersion1, clBag.getSystemVersion());
		assertCollection(Collections.EMPTY_LIST, clBag.getSupercategories());

		assertNotNull(clPlasticBag = classificationSystemVersion1.createClass(clBag, "plastic bag"));
		assertEquals("plastic bag", clPlasticBag.getCode());
		assertEquals(classificationSystemVersion1, clPlasticBag.getSystemVersion());
		assertCollection(Collections.singleton(clBag), clPlasticBag.getSupercategories());

		assertCollection(Collections.singleton(clBag), classificationSystemVersion1.getRootClasses());

		assertNotNull(clScrew = classificationSystemVersion2.createClass("screw"));
		assertEquals("screw", clScrew.getCode());
		assertEquals(classificationSystemVersion2, clScrew.getSystemVersion());
		assertCollection(Collections.EMPTY_LIST, clScrew.getSupercategories());

		assertCollection(Collections.singleton(clScrew), classificationSystemVersion2.getRootClasses());
	}

	/*
	 * System1 +-v1(de) +-.color[red,green,yellow] +-.weight +-(kg,m3) System2 +-v2(en) +-.supplier +-.locTxt
	 */
	protected void createAttributes() throws ConsistencyCheckException
	{
		assertNotNull(attrColor = classificationSystemVersion1.createClassificationAttribute("color"));
		assertEquals("color", attrColor.getCode());

		assertNotNull(vRed = classificationSystemVersion1.createClassificationAttributeValue("red"));
		vRed.setName(deCtx, "Rot");
		vRed.setName(enCtx, "Red");
		assertEquals("red", vRed.getCode());
		assertEquals("Rot", vRed.getName(deCtx));
		assertEquals("Red", vRed.getName(enCtx));

		assertNotNull(vGreen = classificationSystemVersion1.createClassificationAttributeValue("green"));
		vGreen.setName(deCtx, "Gr�n");
		vGreen.setName(enCtx, "Green");
		assertEquals("green", vGreen.getCode());
		assertEquals("Gr�n", vGreen.getName(deCtx));
		assertEquals("Green", vGreen.getName(enCtx));

		assertNotNull(vYellow = classificationSystemVersion1.createClassificationAttributeValue("yellow"));
		vYellow.setName(deCtx, "Gelb");
		vYellow.setName(enCtx, "Yellow");
		assertEquals("yellow", vYellow.getCode());
		assertEquals("Gelb", vYellow.getName(deCtx));
		assertEquals("Yellow", vYellow.getName(enCtx));

		assertNotNull(attrWeight = classificationSystemVersion1.createClassificationAttribute("weight"));
		assertEquals("weight", attrWeight.getCode());

		assertNotNull(attrSupplier = classificationSystemVersion2.createClassificationAttribute("supplier"));
		assertEquals("supplier", attrSupplier.getCode());

		assertNotNull(uKg = classificationSystemVersion1.createAttributeUnit("kg", "kg"));
		assertEquals("kg", uKg.getCode());
		assertEquals("kg", uKg.getSymbol());
		assertNotNull(uM3 = classificationSystemVersion1.createAttributeUnit("m3", "m�"));
		assertEquals("m3", uM3.getCode());
		assertEquals("m�", uM3.getSymbol());
		assertEquals(uKg, classificationSystemVersion1.getAttributeUnit(uKg.getCode()));
		assertEquals(uM3, classificationSystemVersion1.getAttributeUnit(uM3.getCode()));
		assertCollection(Arrays.asList(new Object[]
		{ uKg, uM3 }), classificationSystemVersion1.getAttributeUnits());
		assertEquals(Collections.EMPTY_LIST, classificationSystemVersion2.getAttributeUnits());

		assertNotNull(attrLocTxt = classificationSystemVersion2.createClassificationAttribute("locTxt"));
		assertEquals("locTxt", attrLocTxt.getCode());
	}

	// -------------------------------------------------------------------------------------
	// --- tests 
	// -------------------------------------------------------------------------------------

	@Test
	public void testSpring() throws Exception
	{
		ClassificationSystem classificationSystem;
		ClassificationSystemVersion classificationSystemVersion;
		ClassificationClass classificationClass;
		Category category;
		assertNotNull(classificationSystem = CatalogManager.getInstance().createClassificationSystem("cs"));
		assertNotNull(classificationSystemVersion = classificationSystem.createSystemVersion("cv", (Language) null));
		assertNotNull(classificationClass = classificationSystemVersion.createClass("cc"));
		assertNotNull(category = CategoryManager.getInstance().createCategory("c"));

		if (ClassUtils.isCglibProxyClass(category.getClass()) && ClassUtils.isCglibProxyClass(classificationClass.getClass()))
		{
			assertFalse(category.getClass().isAssignableFrom(classificationClass.getClass())); //WILL FAIL IF USING CGLIB!
		}
		else
		{
			assertTrue(category.getClass().isAssignableFrom(classificationClass.getClass())); //WILL FAIL IF USING CGLIB!

		}
		assertTrue(CGLibUtils.isAssignableFrom(category.getClass(), classificationClass.getClass())); //our helper method always returns true
	}



	@Test
	public void testConstraints()
	{
		try
		{
			createSystems();
		}
		catch (final ConsistencyCheckException e1)
		{
			e1.printStackTrace();
			fail(e1.getMessage());
		}

		try
		{
			assertNotNull(CatalogManager.getInstance().createClassificationSystem("System1"));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(system1.createSystemVersion("v1", english));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		ClassificationSystemVersion v1en = null;
		try
		{
			assertNotNull(v1en = system1.createSystemVersion("v1en", english));
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertCollection(Arrays.asList(new Object[]
		{ classificationSystemVersion1, v1en }), system1.getCatalogVersions());

		Catalog catalog = null;
		CatalogVersion catalogVersion = null;
		assertNotNull(catalog = CatalogManager.getInstance().createCatalog("cl.testcat"));
		assertNotNull(catalogVersion = CatalogManager.getInstance().createCatalogVersion(catalog, "cl.testver", german));

		// test type check for assigning versions
		final Set<CatalogVersion> versions = new HashSet(system1.getSystemVersions());
		versions.add(catalogVersion);
		try
		{
			system2.setCatalogVersions(versions);
			fail("JaloInvalidParameterException expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			assertCollection(Arrays.asList(new Object[]
			{ classificationSystemVersion1, v1en }), system1.getCatalogVersions());
		}

		// test class uniqueness
		try
		{
			createClasses();
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		try
		{
			assertNotNull(classificationSystemVersion1.createClass(clBag.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion1.createClass(clPlasticBag.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createClass(clScrew.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createClass(clBag.getCode()));
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		// check attribute uniqueness
		try
		{
			createAttributes();
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		try
		{
			assertNotNull(classificationSystemVersion1.createClassificationAttribute(attrColor.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion1.createClassificationAttribute(attrWeight.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createClassificationAttribute(attrSupplier.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createClassificationAttribute(attrWeight.getCode()));
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		// check value uniqueness
		try
		{
			assertNotNull(classificationSystemVersion1.createClassificationAttributeValue(vRed.getCode()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createClassificationAttributeValue(vRed.getCode()));
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		// check unit uniqueness
		try
		{
			assertNotNull(classificationSystemVersion1.createAttributeUnit(uKg.getCode(), uKg.getSymbol()));
			fail("ConsistencyCheckException expected");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		try
		{
			assertNotNull(classificationSystemVersion2.createAttributeUnit(uKg.getCode(), uKg.getSymbol()));
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testAttributes() throws ConsistencyCheckException
	{
		try
		{
			createSystems();
			createClasses();
			createAttributes();
		}
		catch (final ConsistencyCheckException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertEquals(
				clBag,
				CatalogManager.getInstance().getClassificationClass(classificationSystemVersion1.getClassificationSystem().getId(),
						classificationSystemVersion1.getVersion(), clBag.getCode()));
		assertEquals(
				clScrew,
				CatalogManager.getInstance().getClassificationClass(classificationSystemVersion2.getClassificationSystem().getId(),
						classificationSystemVersion2.getVersion(), clScrew.getCode()));

		assertTrue(clBag.getDeclaredClassificationAttributes().isEmpty());
		assertTrue(clBag.getDeclaredClassificationAttributes().isEmpty());

		clBag.assignAttribute(attrWeight, T_NUMBER, uKg, // no unit
				null, // no values
				0 // position
		);
		try
		{
			clBag.assignAttribute(attrSupplier);
			fail("JaloInvalidParameterException expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		assertCollection(Collections.singleton(attrWeight), clBag.getDeclaredClassificationAttributes());
		assertEquals(T_NUMBER, clBag.getAttributeType(attrWeight));
		assertEquals(uKg, clBag.getAttributeUnit(attrWeight));
		assertEquals(Collections.EMPTY_LIST, clBag.getAttributeValues(attrWeight));
		assertEquals(Collections.EMPTY_LIST, attrWeight.getDefaultAttributeValues());
		assertFalse(clBag.isLocalized(attrWeight));
		assertFalse(clBag.isMultiValued(attrWeight));

		clScrew.assignAttribute(attrSupplier, T_STRING, null, null, 0);
		assertCollection(Collections.singleton(attrSupplier), clScrew.getDeclaredClassificationAttributes());
		assertEquals(T_STRING, clScrew.getAttributeType(attrSupplier));
		assertNull(clScrew.getAttributeUnit(attrSupplier));
		assertEquals(Collections.EMPTY_LIST, clScrew.getAttributeValues(attrSupplier));
		assertEquals(Collections.EMPTY_LIST, attrSupplier.getDefaultAttributeValues());
		assertFalse(clScrew.isLocalized(attrSupplier));
		assertFalse(clScrew.isMultiValued(attrSupplier));

		clPlasticBag.assignAttribute(attrColor, T_ENUM, null, // no unit
				Arrays.asList(new Object[]
				{ vGreen, vRed, vYellow }), // no values
				0 // position
				);
		clPlasticBag.setMultiValued(attrColor, true);
		assertCollection(Collections.singleton(attrColor), clPlasticBag.getDeclaredClassificationAttributes());
		assertEquals(T_ENUM, clPlasticBag.getAttributeType(attrColor));
		assertNull(clPlasticBag.getAttributeUnit(attrColor));
		assertCollection(Arrays.asList(new Object[]
		{ vGreen, vRed, vYellow }), clPlasticBag.getAttributeValues(attrColor));
		assertEquals(Collections.EMPTY_LIST, attrColor.getDefaultAttributeValues());
		assertFalse(clPlasticBag.isLocalized(attrColor));
		assertTrue(clPlasticBag.isMultiValued(attrColor));

		clScrew.assignAttribute(attrLocTxt, T_STRING, null, null, 0);
		clScrew.setLocalized(attrLocTxt, true);
		assertCollection(Arrays.asList(new Object[]
		{ attrSupplier, attrLocTxt }), clScrew.getDeclaredClassificationAttributes());
		assertEquals(T_STRING, clScrew.getAttributeType(attrLocTxt));
		assertNull(clScrew.getAttributeUnit(attrLocTxt));
		assertEquals(Collections.EMPTY_LIST, clScrew.getAttributeValues(attrLocTxt));
		assertEquals(Collections.EMPTY_LIST, attrLocTxt.getDefaultAttributeValues());
		assertTrue(clScrew.isLocalized(attrLocTxt));
		assertFalse(clScrew.isMultiValued(attrLocTxt));


		assertCollection(Collections.singletonList(attrWeight), clBag.getClassificationAttributes());
		assertCollection(Collections.singletonList(attrWeight), clBag.getDeclaredClassificationAttributes());
		assertTrue(clBag.getInheritedClassificationAttributes().isEmpty());
		assertCollection(Arrays.asList(new Object[]
		{ attrWeight, attrColor }), clPlasticBag.getClassificationAttributes());
		assertCollection(Collections.singletonList(attrColor), clPlasticBag.getDeclaredClassificationAttributes());
		assertCollection(Collections.singletonList(attrWeight), clPlasticBag.getInheritedClassificationAttributes());

		Product product;
		assertNotNull(product = ProductManager.getInstance().createProduct("testprod"));
		assertFalse(clBag.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clBag.getProducts());
		assertFalse(clPlasticBag.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clPlasticBag.getProducts());
		assertFalse(clScrew.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clScrew.getProducts());

		// assign via category
		clPlasticBag.addProduct(product);

		assertEquals(true, clBag.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clBag.getProducts());
		assertTrue(clPlasticBag.isClassifying(product));
		assertCollection(Collections.singletonList(product), clPlasticBag.getProducts());
		assertEquals(clPlasticBag, classificationSystemVersion1.getClassificationClass(product));
		assertFalse(clScrew.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clScrew.getProducts());
		assertNull(classificationSystemVersion2.getClassificationClass(product));

		// assign via system version
		classificationSystemVersion2.setClassificationClass(product, clScrew);

		assertTrue(clBag.isClassifying(product));
		assertEquals(Collections.EMPTY_LIST, clBag.getProducts());
		assertTrue(clPlasticBag.isClassifying(product));
		assertCollection(Collections.singletonList(product), clPlasticBag.getProducts());
		assertEquals(clPlasticBag, classificationSystemVersion1.getClassificationClass(product));
		assertTrue(clScrew.isClassifying(product));
		assertEquals(clScrew, classificationSystemVersion2.getClassificationClass(product));
		assertEquals(Collections.singletonList(product), clScrew.getProducts());
		assertEquals(new HashSet(Arrays.asList(new Object[]
		{ clPlasticBag, clScrew })), new HashSet(CatalogManager.getInstance().getClassificationClasses(product)));

		FeatureContainer cont = FeatureContainer.load(product);
		assertEquals(new HashSet(Arrays.asList(new Object[]
		{ clPlasticBag, clScrew, clBag })), cont.getClasses());

		assertEquals(Collections.EMPTY_LIST, cont.getFeature(attrSupplier).getValues());
		assertEquals(Collections.EMPTY_LIST, cont.getFeature(attrLocTxt).getValues(deCtx));
		assertEquals(Collections.EMPTY_LIST, cont.getFeature(attrLocTxt).getValues(enCtx));
		assertEquals(Collections.EMPTY_LIST, cont.getFeature(attrColor).getValues());
		assertEquals(Collections.EMPTY_LIST, cont.getFeature(attrWeight).getValues());

		cont.getFeature(attrColor).createValue(vRed);
		cont.getFeature(attrColor).createValue(vYellow);
		cont.getFeature(attrWeight).createValue(new BigDecimal("12.345"));

		cont.store();
		// load again
		cont = FeatureContainer.load(product);

		List<FeatureValue> featureValueList = cont.getFeature(attrColor).getValues();
		assertEquals(2, featureValueList.size());
		assertEquals(vRed, featureValueList.get(0).getValue());
		assertEquals(vYellow, featureValueList.get(1).getValue());

		featureValueList = cont.getFeature(attrWeight).getValues();
		assertEquals(1, featureValueList.size());
		assertTrue(featureValueList.get(0).getValue() instanceof Number);
		assertEquals(12.345, ((Number) featureValueList.get(0).getValue()).doubleValue(), 0.0001);

		cont.getFeature(attrSupplier).createValue("hybris");
		cont.getFeature(attrLocTxt).createValue(deCtx, "de loc text");
		cont.store();
		cont = FeatureContainer.load(product);

		featureValueList = cont.getFeature(attrSupplier).getValues();
		assertEquals(1, featureValueList.size());
		assertEquals("hybris", featureValueList.get(0).getValue());
		featureValueList = cont.getFeature(attrLocTxt).getValues(deCtx);
		assertEquals(1, featureValueList.size());
		assertEquals("de loc text", featureValueList.get(0).getValue());
		featureValueList = cont.getFeature(attrLocTxt).getValues(enCtx);
		assertTrue(featureValueList.isEmpty());

		cont.getFeature(attrLocTxt).createValue(enCtx, "en loc text");

		cont.store();
		cont = FeatureContainer.load(product);

		featureValueList = cont.getFeature(attrLocTxt).getValues(deCtx);
		assertEquals(1, featureValueList.size());
		assertEquals("de loc text", featureValueList.get(0).getValue());
		featureValueList = cont.getFeature(attrLocTxt).getValues(enCtx);
		assertEquals(1, featureValueList.size());
		assertEquals("en loc text", featureValueList.get(0).getValue());

	}

	@Test
	public void testSuperClassGetter() throws ConsistencyCheckException
	{
		ClassificationSystem clSys;
		ClassificationSystemVersion clVer;
		ClassificationClass cl1, cl21, cl22, cl23, cl3, cl4;

		assertNotNull(clSys = CatalogManager.getInstance().createClassificationSystem("xyz"));
		assertNotNull(clVer = clSys.createSystemVersion("3.11", Collections.EMPTY_LIST));

		assertNotNull(cl1 = clVer.createClass("cl1"));

		assertNotNull(cl21 = clVer.createClass("cl21"));
		assertNotNull(cl22 = clVer.createClass("cl22"));
		assertNotNull(cl23 = clVer.createClass("cl23"));

		assertNotNull(cl3 = clVer.createClass("cl3"));

		assertNotNull(cl4 = clVer.createClass("cl4"));

		cl1.setSubcategories(Arrays.asList((Category) cl21, cl22, cl23));
		cl21.setSubcategories(Arrays.asList((Category) cl3));
		cl22.setSubcategories(Arrays.asList((Category) cl3));
		cl23.setSubcategories(Arrays.asList((Category) cl3));
		cl3.setSubcategories(Arrays.asList((Category) cl4));

		assertEquals(Collections.singleton(cl3), cl4.getSuperClasses());
		assertEquals(new LinkedHashSet(Arrays.asList(cl3, cl21, cl22, cl23, cl1)), cl4.getAllSuperClasses());

		assertEquals(new LinkedHashSet(Arrays.asList(cl21, cl22, cl23)), cl3.getSuperClasses());
		assertEquals(new LinkedHashSet(Arrays.asList(cl21, cl22, cl23, cl1)), cl3.getAllSuperClasses());

		assertEquals(Collections.singleton(cl1), cl22.getSuperClasses());
		assertEquals(Collections.singleton(cl1), cl22.getAllSuperClasses());
	}


	// PLA-13578
	@Test
	public void testClassificationClassInheritanceForVariants() throws ConsistencyCheckException, JaloGenericCreationException,
			JaloAbstractTypeException
	{
		// classification system
		final ClassificationSystem clSys = CatalogManager.getInstance().createClassificationSystem("system");
		final ClassificationSystemVersion clVer = clSys.createSystemVersion("version", Collections.EMPTY_LIST);
		final ClassificationClass cl1 = clVer.createClass("clClass1");
		final ClassificationClass cl2 = clVer.createClass("clClass2");

		// products & categories
		final VariantsManager vm = VariantsManager.getInstance();
		final VariantType variantType = vm.createVariantType("TestVariantType");
		final Product base = vm.createBaseProduct("base", "TestVariantType");
		final VariantProduct variant1 = createVariantProduct(variantType, "variant-level1", base, true);
		final VariantProduct variant2 = createVariantProduct(variantType, "variant-level2", variant1, true);
		final VariantProduct variant3 = createVariantProduct(variantType, "variant-level3", variant2, false);

		assertEquals(base, variant1.getBaseProduct());
		assertEquals(variant1, variant2.getBaseProduct());
		assertEquals(variant2, variant3.getBaseProduct());

		final Category category = CategoryManager.getInstance().createCategory("category");
		category.setSupercategories(cl1);

		final CatalogManager cm = CatalogManager.getInstance();

		// sanity: no classification yet
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(base));
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(variant1));
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(variant2));
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(variant3));

		// direct assignments clClass1 -> variant3 & clClass2 -> variant2 => classes(variant3)==[clClass1,clClass2] & classes(variant2)==[clClass2]
		cl1.setProducts(Collections.singletonList((Product) variant3));
		cl2.setProducts(Collections.singletonList((Product) variant2));
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(base));
		assertEquals(Collections.EMPTY_LIST, cm.getClassificationClasses(variant1));
		assertEquals(Arrays.asList(cl2), cm.getClassificationClasses(variant2));
		assertEquals(Arrays.asList(cl1, cl2), cm.getClassificationClasses(variant3));

		cl1.setProducts(Collections.EMPTY_LIST);
		cl2.setProducts(Collections.EMPTY_LIST);

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3
		category.setProducts(Collections.singletonList(base));

		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(base));
		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(variant1));
		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(variant2));
		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(variant3));

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3 
		// AND direct assignment clClass2 -> variant2 = classes(variant2)=clClass2 & classes(variant3)=clClass2
		cl2.setProducts(Collections.singletonList((Product) variant2));

		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(base));
		assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(variant1));
		assertEquals(Arrays.asList(cl2, cl1), cm.getClassificationClasses(variant2));
		assertEquals(Arrays.asList(cl2, cl1), cm.getClassificationClasses(variant3));


		final String resolvingModeBefore = Config.getParameter("classification.resolve.classes.mode");
		try
		{
			Config.setParameter("classification.resolve.classes.mode", "closest");

			assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(base));
			assertEquals(Arrays.asList(cl1), cm.getClassificationClasses(variant1));
			assertEquals(Arrays.asList(cl2), cm.getClassificationClasses(variant2));
			assertEquals(Arrays.asList(cl2), cm.getClassificationClasses(variant3));
		}
		finally
		{
			Config.setParameter("classification.resolve.classes.mode", resolvingModeBefore);
		}

	}

	VariantProduct createVariantProduct(final VariantType type, final String code, final Product base, final boolean asBase)
			throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Map<String, Object> attributes = new HashMap<>();
		attributes.put(VariantProduct.CODE, code);
		attributes.put(VariantProduct.BASEPRODUCT, base);
		if (asBase)
		{
			attributes.put(VariantsConstants.Attributes.Product.VARIANTTYPE, type);
		}


		return (VariantProduct) type.newInstance(attributes);
	}
}
