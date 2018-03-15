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
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueCondition;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueOrderBy;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueSelectField;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.catalog.jalo.classification.util.UntypedFeature;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;


@IntegrationTest
public class FeatureValueAPITest extends HybrisJUnit4Test
{
	Language german, english;
	SessionContext deCtx, enCtx;

	ClassificationSystem houseHold, other;

	ClassificationClass household_goods, toaster, microwave, someClass;

	ClassificationAttribute weight, dimensions, guarantee, date, voltage, slots, timer, grill, manufacturer, power, size,
			someAttr;

	ClassificationAttributeValue siemens, bosch, hybris;

	ClassificationAttributeUnit kg, g, cm, mm, m, v, w, m3; //NOPMD

	Product product1, product2;

	Category cat;

	/*
	 * cat +-p1
	 */
	@Before
	public void setUp() throws Exception
	{
		german = getOrCreateLanguage("de");
		deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(german);
		english = getOrCreateLanguage("en");
		enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(english);

		assertNotNull(product1 = ProductManager.getInstance().createProduct("p1"));
		assertNotNull(product2 = ProductManager.getInstance().createProduct("p2"));


		setUpClassificationSystem();

		assertNotNull(cat = CategoryManager.getInstance().createCategory("cat"));
		cat.addProduct(product1);
	}

	@After
	public void tearDown() throws Exception
	{
		if (houseHold != null)
		{
			houseHold.setActiveCatalogVersion(null);
		}
		if (other != null)
		{
			other.setActiveCatalogVersion(null);
		}
	}

	/*
	 * household_stuff: +-household_goods( weight[kg]:number, dimensions[cm]:number, guarantee{loc}:string ) +-toaster(
	 * voltage[V]:number , slots:number , timer:boolean , grill:boolean , manufacturer:[Siemens,Bosch] ) +-microwave(
	 * power[W]:number, grill:boolean , size[m3]:number ) other_system +-someClass( someAttr{loc}:string )
	 */
	protected void setUpClassificationSystem() throws ConsistencyCheckException
	{
		assertNotNull(houseHold = CatalogManager.getInstance().createClassificationSystem("household_stuff"));
		ClassificationSystemVersion classificationSystemVersion1;
		assertNotNull(classificationSystemVersion1 = houseHold.createSystemVersion("1.0", Arrays.asList(german, english)));
		classificationSystemVersion1.setActive(true);
		// units
		assertNotNull(kg = classificationSystemVersion1.createAttributeUnit("kg", "kg", "weight", 1));
		assertNotNull(g = classificationSystemVersion1.createAttributeUnit("g", "g", "weight", 1000));
		assertNotNull(cm = classificationSystemVersion1.createAttributeUnit("cm", "cm", "length", 100));
		assertNotNull(mm = classificationSystemVersion1.createAttributeUnit("mm", "mm", "length", 1000));
		assertNotNull(m = classificationSystemVersion1.createAttributeUnit("m", "m", "length", 1));
		assertNotNull(v = classificationSystemVersion1.createAttributeUnit("v", "V"));
		assertNotNull(w = classificationSystemVersion1.createAttributeUnit("w", "W"));
		assertNotNull(m3 = classificationSystemVersion1.createAttributeUnit("m3", "m³"));
		// values
		assertNotNull(siemens = classificationSystemVersion1.createClassificationAttributeValue("MAN_SIEMENS"));
		assertNotNull(bosch = classificationSystemVersion1.createClassificationAttributeValue("MAN_BOSCH"));
		assertNotNull(hybris = classificationSystemVersion1.createClassificationAttributeValue("MAN_HYBRIS"));
		// classes
		assertNotNull(household_goods = classificationSystemVersion1.createClass("household_goods"));
		assertNotNull(toaster = classificationSystemVersion1.createClass(household_goods, "toaster"));
		assertNotNull(microwave = classificationSystemVersion1.createClass(household_goods, "microwave"));
		// attributes
		int pos = 0;
		assertNotNull(date = classificationSystemVersion1.createClassificationAttribute("date"));
		household_goods.assignAttribute(date, ClassificationAttribute.TYPE_DATE, null, null, pos++);
		assertNotNull(weight = classificationSystemVersion1.createClassificationAttribute("weight"));
		household_goods.assignAttribute(weight, ClassificationAttribute.TYPE_NUMBER,
				classificationSystemVersion1.getAttributeUnit("kg"), null, pos++);
		assertNotNull(dimensions = classificationSystemVersion1.createClassificationAttribute("dimensions"));
		ClassAttributeAssignment c2a = household_goods.assignAttribute(dimensions, ClassificationAttribute.TYPE_NUMBER,
				classificationSystemVersion1.getAttributeUnit("kg"), null, pos++);
		c2a.setMultiValued(true);
		assertNotNull(guarantee = classificationSystemVersion1.createClassificationAttribute("guarantee"));
		c2a = household_goods.assignAttribute(guarantee, ClassificationAttribute.TYPE_STRING, null, null, pos++);
		c2a.setLocalized(true);
		pos = 0;
		assertNotNull(voltage = classificationSystemVersion1.createClassificationAttribute("voltage"));
		toaster.assignAttribute(voltage, ClassificationAttribute.TYPE_NUMBER, classificationSystemVersion1.getAttributeUnit("v"),
				null, pos++);
		assertNotNull(slots = classificationSystemVersion1.createClassificationAttribute("slots"));
		toaster.assignAttribute(slots, ClassificationAttribute.TYPE_NUMBER, null, null, pos++);
		assertNotNull(timer = classificationSystemVersion1.createClassificationAttribute("timer"));
		toaster.assignAttribute(timer, ClassificationAttribute.TYPE_BOOLEAN, null, null, pos++);
		assertNotNull(grill = classificationSystemVersion1.createClassificationAttribute("grill"));
		toaster.assignAttribute(grill, ClassificationAttribute.TYPE_BOOLEAN, null, null, pos++);
		assertNotNull(manufacturer = classificationSystemVersion1.createClassificationAttribute("manufacturer"));
		toaster
				.assignAttribute(manufacturer, ClassificationAttribute.TYPE_ENUM, null, Arrays.asList(siemens, bosch, hybris), pos++);
		assertNotNull(power = classificationSystemVersion1.createClassificationAttribute("power"));
		microwave.assignAttribute(power, ClassificationAttribute.TYPE_NUMBER, classificationSystemVersion1.getAttributeUnit("w"),
				null, 0);
		assertNotNull(size = classificationSystemVersion1.createClassificationAttribute("size"));
		microwave.assignAttribute(size, ClassificationAttribute.TYPE_NUMBER, classificationSystemVersion1.getAttributeUnit("m3"),
				null, 1);
		microwave.assignAttribute(grill, ClassificationAttribute.TYPE_BOOLEAN, null, null, 2);

		assertNotNull(other = CatalogManager.getInstance().createClassificationSystem("other_system"));
		assertNotNull(classificationSystemVersion1 = other.createSystemVersion("0.0.beta", Arrays.asList(english, german)));
		classificationSystemVersion1.setActive(true);
		assertNotNull(someClass = classificationSystemVersion1.createClass("someClass"));
		assertNotNull(someAttr = classificationSystemVersion1.createClassificationAttribute("someAttr"));
		c2a = someClass.assignAttribute(someAttr, ClassificationAttribute.TYPE_STRING, null, null, 0);
		c2a.setLocalized(true);
	}

	@Test
	public void testAPI() throws ConsistencyCheckException
	{
		assertEquals(Collections.singletonList(cat), CategoryManager.getInstance().getSupercategories(product1));
		assertEquals(Collections.EMPTY_LIST, CategoryManager.getInstance().getSupercategories(product2));

		assertEquals(Collections.EMPTY_LIST, CatalogManager.getInstance().getClassificationClasses(product1));
		assertEquals(Collections.EMPTY_LIST, CatalogManager.getInstance().getClassificationClasses(product2));

		// test untyped feature first

		FeatureContainer featureContainer = FeatureContainer.create(product1);
		assertTrue(featureContainer.isUntyped());
		assertTrue(featureContainer.isEmpty());

		writeReadUntypedFeatures(featureContainer);

		featureContainer = FeatureContainer.createUntyped(product2);
		assertTrue(featureContainer.isUntyped());
		assertTrue(featureContainer.isEmpty());

		writeReadUntypedFeatures(featureContainer);

		// test all-classifications container

		// test indirect assignment
		microwave.addToCategories(cat);
		assertEquals(Collections.singletonList(microwave), CatalogManager.getInstance().getClassificationClasses(cat));
		assertEquals(Collections.singletonList(microwave), CatalogManager.getInstance().getClassificationClasses(product1));
		// test direct assignment
		toaster.addToProducts(product1);
		microwave.addToProducts(product1);
		microwave.removeFromCategories(cat);

		assertEquals(new HashSet(Arrays.asList(toaster, microwave)), new HashSet(CatalogManager.getInstance()
				.getClassificationClasses(product1)));

		final FeatureContainer auto_typed = FeatureContainer.create(product1);
		assertFalse(auto_typed.isUntyped());
		assertTrue(auto_typed.isEmpty());
		assertEquals(new HashSet(Arrays.asList(toaster, microwave, household_goods)), auto_typed.getClasses());
		assertEquals(toaster, auto_typed.getClassificationClass(toaster.getCode()));
		assertEquals(toaster,
				auto_typed.getClassificationClass(houseHold.getActiveCatalogVersion().getFullVersionName() + "/" + toaster.getCode()));
		assertEquals(microwave, auto_typed.getClassificationClass(microwave.getCode()));
		assertEquals(
				microwave,
				auto_typed.getClassificationClass(houseHold.getActiveCatalogVersion().getFullVersionName() + "/"
						+ microwave.getCode()));
		assertEquals(household_goods, auto_typed.getClassificationClass(household_goods.getCode()));
		assertEquals(
				household_goods,
				auto_typed.getClassificationClass(houseHold.getActiveCatalogVersion().getFullVersionName() + "/"
						+ household_goods.getCode()));

		assertTrue(auto_typed.hasFeature(houseHold.getId() + "/1.0/" + household_goods.getCode() + "/" + weight.getCode()));
		assertTrue(auto_typed.hasFeature(houseHold.getId() + "/1.0/" + household_goods.getCode() + "." + weight.getCode()));
		assertTrue(auto_typed.hasFeature("1.0/" + household_goods.getCode() + "/" + weight.getCode()));
		assertTrue(auto_typed.hasFeature("1.0/" + household_goods.getCode() + "." + weight.getCode()));
		assertTrue(auto_typed.hasFeature(household_goods.getCode() + "/" + weight.getCode()));
		assertTrue(auto_typed.hasFeature(household_goods.getCode() + "." + weight.getCode()));
		assertTrue(auto_typed.hasFeature(weight.getCode()));
		final ClassAttributeAssignment weightAsgnmt = auto_typed.getClassificationAttributeAssignment(houseHold.getId() + "/1.0/"
				+ household_goods.getCode() + "/" + weight.getCode());
		assertNotNull(weightAsgnmt);
		assertEquals(
				weightAsgnmt,
				auto_typed.getClassificationAttributeAssignment(houseHold.getId() + "/1.0/" + household_goods.getCode() + "."
						+ weight.getCode()));
		assertEquals(weightAsgnmt,
				auto_typed.getClassificationAttributeAssignment("1.0/" + household_goods.getCode() + "." + weight.getCode()));
		assertEquals(weightAsgnmt,
				auto_typed.getClassificationAttributeAssignment("1.0/" + household_goods.getCode() + "/" + weight.getCode()));
		assertEquals(weightAsgnmt,
				auto_typed.getClassificationAttributeAssignment(household_goods.getCode() + "." + weight.getCode()));
		assertEquals(weightAsgnmt,
				auto_typed.getClassificationAttributeAssignment(household_goods.getCode() + "/" + weight.getCode()));
		assertEquals(weightAsgnmt, auto_typed.getClassificationAttributeAssignment(weight.getCode()));

		try
		{
			auto_typed.getClassificationClass("someClass");
			fail("JaloItemNotFoundException expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine here
		}
		assertEquals(
				new HashSet(Arrays.asList(date, weight, dimensions, guarantee, voltage, slots, timer, grill, manufacturer, power,
						size)), auto_typed.getSupportedAttributes());
		assertEquals(date, auto_typed.getClassificationAttribute(date.getCode()));
		assertEquals(weight, auto_typed.getClassificationAttribute(weight.getCode()));
		assertEquals(grill, auto_typed.getClassificationAttribute(microwave.getCode() + "/" + grill.getCode()));
		assertEquals(
				dimensions,
				auto_typed.getClassificationAttribute(houseHold.getActiveCatalogVersion().getFullVersionName() + "/"
						+ household_goods.getCode() + "/" + dimensions.getCode()));
		// test fallback to inheriting class code as well 
		assertEquals(
				dimensions,
				auto_typed.getClassificationAttribute(houseHold.getActiveCatalogVersion().getFullVersionName() + "/"
						+ microwave.getCode() + "/" + dimensions.getCode()));
		try
		{
			auto_typed.getClassificationAttribute(someAttr.getCode());
			fail("JaloInvalidParameterException expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine here
		}

		final FeatureContainer auto_typed_loaded = FeatureContainer.load(product1);
		assertFalse(auto_typed_loaded.isUntyped());
		assertEquals(new HashSet(Arrays.asList(toaster, microwave, household_goods)), auto_typed_loaded.getClasses());
		assertTrue(auto_typed_loaded.isEmpty()); // no untyped features in here

		auto_typed.reload();

		assertFalse(auto_typed_loaded.isUntyped());
		assertEquals(new HashSet(Arrays.asList(toaster, microwave, household_goods)), auto_typed_loaded.getClasses());
		assertTrue(auto_typed_loaded.isEmpty()); // no untyped features in here

		// testing bug CATALOG-449
		final FeatureContainer untyped = FeatureContainer.loadUntyped(product1);
		untyped.reload();

		// TODO test setting typed features
	}

	@Test
	public void testGenericSearch() throws ConsistencyCheckException
	{
		final String tProduct = TypeManager.getInstance().getComposedType(Product.class).getCode();
		GenericQuery genericQuery = new GenericQuery(tProduct);

		toaster.addToProducts(product1);
		microwave.addToProducts(product2);
		someClass.addToProducts(product2);

		// no condition -> p1 and p2
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// no feature 'date' -> p1 and p2
		genericQuery.addCondition(FeatureValueCondition.isNull(toaster.getAttributeAssignment(date)));
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		Date date1, date2;
		// p1.date=now
		FeatureContainer cont = FeatureContainer.loadTyped(product1);
		cont.getFeature(date).createValue(date1 = new Date());
		cont.store();
		// p2.date=now + 30*1000 ms
		cont = FeatureContainer.loadTyped(product2);
		cont.getFeature(date).createValue(date2 = new Date(date1.getTime() + (30 * 1000)));
		cont.store();

		cont = FeatureContainer.loadTyped(product1);
		final FeatureValue<Date> d1_check = cont.getFeature(date).getValue(0);
		assertEquals(date1, d1_check.getValue());

		cont = FeatureContainer.loadTyped(product2);
		final FeatureValue<Date> d2_check = cont.getFeature(date).getValue(0);
		assertEquals(date2, d2_check.getValue());

		genericQuery = new GenericQuery(tProduct);
		genericQuery.addCondition(FeatureValueCondition.equals(toaster.getAttributeAssignment(date), date1));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		genericQuery = new GenericQuery(tProduct);
		genericQuery.addConditions(
				FeatureValueCondition.greater(toaster.getAttributeAssignment(date), new Date(date1.getTime() - 2000)),
				FeatureValueCondition.less(toaster.getAttributeAssignment(date), new Date(date2.getTime() + 2000)));
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		genericQuery = new GenericQuery(tProduct);
		genericQuery.addConditions(
				FeatureValueCondition.less(toaster.getAttributeAssignment(date), new Date(date1.getTime() - 2000)),
				FeatureValueCondition.greater(toaster.getAttributeAssignment(date), new Date(date2.getTime() + 2000)));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		genericQuery = new GenericQuery(tProduct);

		// no feature 'weight' -> p1 and p2
		genericQuery.addCondition(FeatureValueCondition.isNull(toaster.getAttributeAssignment(weight)));
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// p1.weight=12.50
		cont = FeatureContainer.loadTyped(product1);
		cont.getFeature(weight).createValue(Double.valueOf(12.50));
		cont.store();

		// p2.power=2000
		cont = FeatureContainer.loadTyped(product2);
		cont.getFeature(power).createValue(Double.valueOf(2000));
		cont.store();

		// no feature 'weight' -> p2
		assertCollection(Arrays.asList(product2), jaloSession.search(genericQuery).getResult());

		// got feature 'weight' -> p1
		genericQuery = new GenericQuery(tProduct);
		genericQuery.addCondition(FeatureValueCondition.notNull(toaster.getAttributeAssignment(weight)));
		assertCollection(Arrays.asList(product1), jaloSession.search(genericQuery).getResult());

		cont = FeatureContainer.loadTyped(product2);
		cont.getFeature(weight).createValue(Double.valueOf(133.0));
		cont.store();

		// got feature 'weight' -> p1 , p2
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// weight > 50 AND weight < 100 -> empty
		genericQuery = new GenericQuery(tProduct);
		genericQuery.addCondition(FeatureValueCondition.greater(toaster.getAttributeAssignment(weight), Double.valueOf(50.0)));
		genericQuery.addCondition(FeatureValueCondition.less(toaster.getAttributeAssignment(weight), Double.valueOf(100.0)));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// weight > 10 AND weight < 150 -> p1, p2
		genericQuery = new GenericQuery(tProduct);
		genericQuery.addCondition(FeatureValueCondition.greater(toaster.getAttributeAssignment(weight), Double.valueOf(10.0)));
		genericQuery.addCondition(FeatureValueCondition.less(toaster.getAttributeAssignment(weight), Double.valueOf(150.0)));
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// p1.guarantee={de:"Garantie", "Blah", "Blubb";en:"guarantee", "wtf"}
		// p1.manufacturer=siemens
		cont = FeatureContainer.loadTyped(product1);
		cont.getFeature(guarantee).createValues(deCtx, "Garantie", "Blah", "Blubb");
		cont.getFeature(guarantee).createValues(enCtx, "guarantee", "wtf");
		cont.getFeature(manufacturer).createValue(siemens);
		cont.store();
		// p2.grill=true
		cont = FeatureContainer.loadTyped(product2);
		cont.getFeature(guarantee).createValues(deCtx, "Garantie2");
		cont.getFeature(grill).createValue(Boolean.TRUE).setDescription("really true");
		cont.store();

		// test localized
		// guarantee[de] LIKE 'Gara%' -> p1,p2
		genericQuery = new GenericQuery(tProduct);
		FeatureValueCondition cond;
		genericQuery
				.addCondition(cond = FeatureValueCondition.startsWith(household_goods.getAttributeAssignment(guarantee), "Gara"));
		cond.setLanguage(german);
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// guarantee[en] LIKE 'Gara%' -> ()
		cond.setLanguage(english);
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// guarantee[en] LIKE '%wtf%' -> p1
		genericQuery = new GenericQuery(tProduct);
		FeatureValueCondition cond1, cond2;
		genericQuery.addCondition(GenericCondition.createConditionList(Operator.OR,
				cond1 = FeatureValueCondition.contains(household_goods.getAttributeAssignment(guarantee), "wtf"),
				cond2 = cond = FeatureValueCondition.equals(household_goods.getAttributeAssignment(guarantee), "Garantie")));
		cond1.setLanguage(english);
		cond2.setLanguage(german);
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		/*
		 * test unit based searching: p1.weight = 12.5 kg p2.weight = 133 g
		 */
		final FeatureContainer p1Cont = FeatureContainer.loadTyped(product1);
		final FeatureValue<Number> weight1 = p1Cont.getFeature(weight).getValue(0);
		assertEquals(kg, weight1.getUnit());
		assertEquals(Double.valueOf(12.50), weight1.getValue());
		assertEquals(Double.valueOf(12500.0), weight1.getValue(g));

		final FeatureContainer p2Cont = FeatureContainer.loadTyped(product2);
		final FeatureValue<Number> weight2 = p2Cont.getFeature(weight).getValue(0);
		assertEquals(kg, weight2.getUnit());
		assertEquals(Double.valueOf(133.0), weight2.getValue());

		weight2.setUnit(g);
		p2Cont.store();
		p1Cont.store();

		assertEquals(g, weight2.getUnit());
		assertEquals(Double.valueOf(133.0), weight2.getValue());
		assertEquals(Double.valueOf(0.133), weight2.getValue(kg));

		assertCollection(Collections.singleton(kg), g.getConvertibleUnits());
		assertCollection(Collections.singleton(g), kg.getConvertibleUnits());

		// weight > 1kg -> p1

		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.greater(household_goods.getAttributeAssignment(weight),
				Double.valueOf(1.0)));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		// weight > 10g AND weight < 30kg -> p1,p2
		genericQuery = new GenericQuery(tProduct);
		cond1 = FeatureValueCondition.greater(household_goods.getAttributeAssignment(weight), Double.valueOf(10.0));
		cond1.setUnit(g);
		genericQuery.addConditions(cond1,
				FeatureValueCondition.less(household_goods.getAttributeAssignment(weight), Double.valueOf(30.0)));
		assertCollection(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// weight = 133 g -> p2
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.equals(household_goods.getAttributeAssignment(weight),
				Double.valueOf(133.0)));
		((FeatureValueCondition) genericQuery.getCondition()).setUnit(g);
		assertEquals(Collections.singletonList(product2), jaloSession.search(genericQuery).getResult());

		// weight = 0.133 kg -> p2
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.equals(household_goods.getAttributeAssignment(weight),
				Double.valueOf(0.133)));
		assertEquals(Collections.singletonList(product2), jaloSession.search(genericQuery).getResult());

		// test convertible error
		try
		{
			((FeatureValueCondition) genericQuery.getCondition()).setUnit(m);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}

		// test ordering 

		// order by weight[kg] (asc) : p1.weight=12.5 kg p2.weight=133g -> [ p2, p1 ]
		genericQuery = new GenericQuery(tProduct);
		genericQuery.addOrderBy(FeatureValueOrderBy.orderBy(toaster.getAttributeAssignment(weight)));
		assertEquals(Arrays.asList(product2, product1), jaloSession.search(genericQuery).getResult());

		// order by weight[kg] desc : p1.weight=12.5 kg p2.weight=133g -> [ p1, p2 ]
		genericQuery = new GenericQuery(tProduct);
		genericQuery.addOrderBy(FeatureValueOrderBy.orderBy(toaster.getAttributeAssignment(weight), false));
		assertEquals(Arrays.asList(product1, product2), jaloSession.search(genericQuery).getResult());

		// order by enum
		final Product product3;
		assertNotNull(product3 = ProductManager.getInstance().createProduct("p3"));
		toaster.addToProducts(product3);
		final TypedFeature typedFeature = TypedFeature.loadTyped(product3, toaster.getAttributeAssignment(manufacturer));
		typedFeature.setValue(hybris);
		typedFeature.getParent().store();

		// manufacturer (asc) : p1.manufacturer=siemens , p3.manufacturer=hybris -> p1,p3
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.notNull(toaster.getAttributeAssignment(manufacturer)));
		genericQuery.addOrderBy(FeatureValueOrderBy.orderBy(toaster.getAttributeAssignment(manufacturer)));
		assertEquals(Arrays.asList(product1, product3), jaloSession.search(genericQuery).getResult());

		// manufacturer desc : p1.manufacturer=siemens , p3.manufacturer=hybris -> p3,p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.notNull(toaster.getAttributeAssignment(manufacturer)));
		genericQuery.addOrderBy(FeatureValueOrderBy.orderBy(toaster.getAttributeAssignment(manufacturer), false));
		assertEquals(jaloSession.search(genericQuery).getResult(), Arrays.asList(product3, product1));

		product3.remove();
		assertNotNull(product3);

		/*
		 * test enum ordering: p1.manufacturer = bosch
		 */
		cont = FeatureContainer.loadTyped(product1);
		cont.getFeature(manufacturer).setSelectableValue(bosch.getCode());
		cont.store();

		assertEquals(bosch, TypedFeature.loadTyped(product1, manufacturer).getValue(0).getValue());

		// assure ordering siemems,bosch,hybris
		assertEquals(Arrays.asList(siemens, bosch, hybris), toaster.getAttributeValues(manufacturer));

		// manufacturer = bosch -> p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.equals(toaster.getAttributeAssignment(manufacturer), bosch));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		// manufacturer > siemens -> p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.greater(toaster.getAttributeAssignment(manufacturer),
				siemens));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		// manufacturer < siemens -> ()
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.less(toaster.getAttributeAssignment(manufacturer), siemens));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// manufacturer < hybris -> p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.less(toaster.getAttributeAssignment(manufacturer), hybris));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		// manufacturer > hybris -> ()
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.greater(toaster.getAttributeAssignment(manufacturer),
				hybris));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// manufacturer < bosch -> ()
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.less(toaster.getAttributeAssignment(manufacturer), bosch));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// reorder bosch,hybris,siemens
		toaster.getAttributeAssignment(manufacturer).setAttributeValues(Arrays.asList(bosch, hybris, siemens));

		// manufacturer > siemens -> ()
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.greater(toaster.getAttributeAssignment(manufacturer),
				siemens));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());

		// manufacturer < siemens -> p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.less(toaster.getAttributeAssignment(manufacturer), siemens));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		// manufacturer IN ( siemens,hybris ) -> ()
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.in(toaster.getAttributeAssignment(manufacturer), siemens,
				hybris));
		assertEquals(Collections.EMPTY_LIST, jaloSession.search(genericQuery).getResult());
		// manufacturer IN ( bosch ) -> p1
		genericQuery = new GenericQuery(tProduct, FeatureValueCondition.in(toaster.getAttributeAssignment(manufacturer), bosch));
		assertEquals(Collections.singletonList(product1), jaloSession.search(genericQuery).getResult());

		/*
		 * test SELECT
		 */

		genericQuery = new GenericQuery(tProduct);
		// 1. product itself
		genericQuery.addSelectField(new GenericSelectField(Product.PK, Item.class));
		// 2. its weight [kg]
		genericQuery.addSelectField(FeatureValueSelectField.select(toaster.getAttributeAssignment(weight)));
		// order by weight p1.weight=12.5 kg p2.weight=133g -> p2,p1
		genericQuery.addOrderBy(FeatureValueOrderBy.orderBy(toaster.getAttributeAssignment(weight)));

		List<List> rows = jaloSession.search(genericQuery).getResult();
		assertEquals(2, rows.size());
		assertEquals(product2, rows.get(0).get(0));
		assertEquals(product1, rows.get(1).get(0));
		// selected values cannot be compared qith equals since they're db dependent !
		assertEquals(.133, ((BigDecimal) rows.get(0).get(1)).doubleValue(), 0.0001);
		assertEquals(12.5, ((BigDecimal) rows.get(1).get(1)).doubleValue(), 0.0001);

		// now try different unit
		((FeatureValueSelectField) genericQuery.getSelectFields().get(1)).setUnit(g);
		rows = jaloSession.search(genericQuery).getResult();
		assertEquals(2, rows.size());
		assertEquals(product2, rows.get(0).get(0));
		assertEquals(product1, rows.get(1).get(0));
		// selected values cannot be compared qith equals since they're db dependent !
		assertEquals(133.0, ((BigDecimal) rows.get(0).get(1)).doubleValue(), 0.0001);
		assertEquals(12500.0, ((BigDecimal) rows.get(1).get(1)).doubleValue(), 0.0001);

		/*
		 * test date
		 */


	}

	protected void writeReadUntypedFeatures(final FeatureContainer featureContainer) throws ConsistencyCheckException
	{
		/*
		 * blah:String = text(some comment),text2(some comment2),text3(some comment3) bool:boolean = true nr:number =
		 * 1.234 locTxt:String(loc) = { de = de txt(de comment) , en = en txt(en comment) }
		 */
		final UntypedFeature<String> untypedFeature1 = featureContainer.createFeature("blah", false);
		FeatureValue<String> featureValue1 = untypedFeature1.createValue("text");
		featureValue1.setDescription("some comment");
		featureValue1 = untypedFeature1.createValue("text2");
		featureValue1.setDescription("some comment2");
		featureValue1 = untypedFeature1.createValue("text3");
		featureValue1.setDescription("some comment3");

		final UntypedFeature<Boolean> untypedFeature2 = featureContainer.createFeature("bool", false);
		final FeatureValue<Boolean> featureValue2 = untypedFeature2.createValue(Boolean.TRUE);
		featureValue2.setDescription("comment");

		final UntypedFeature<Number> untypedFeature3 = featureContainer.createFeature("nr", false);
		final FeatureValue<Number> featureValue3 = untypedFeature3.createValue(new Double(1.234));
		featureValue3.setDescription("comment");

		final UntypedFeature<String> untypedFeature4loc = featureContainer.createFeature("locTxt", true);
		final FeatureValue<String> featureValue4_de = untypedFeature4loc.createValue(deCtx, "de txt");
		featureValue4_de.setDescription("de comment");
		final FeatureValue<String> featureValue4_en = untypedFeature4loc.createValue(enCtx, "en txt");
		featureValue4_en.setDescription("en comment");

		assertEquals(untypedFeature1, featureContainer.getFeature("blah"));
		assertEquals(untypedFeature2, featureContainer.getFeature("bool"));
		assertEquals(untypedFeature3, featureContainer.getFeature("nr"));
		assertEquals(untypedFeature4loc, featureContainer.getFeature("locTxt"));

		featureContainer.store();

		final FeatureContainer pf_loaded = FeatureContainer.load(product1);
		final Feature<String> fv_loaded = pf_loaded.getFeature("blah");
		assertEquals(untypedFeature1.getValues(), fv_loaded.getValues());
		final Feature<Boolean> fv2_loaded = pf_loaded.getFeature("bool");
		assertEquals(untypedFeature2.getValues(), fv2_loaded.getValues());
		final Feature<Number> fv3_loaded = pf_loaded.getFeature("nr");
		assertEquals(untypedFeature3.getValues(), fv3_loaded.getValues());
		final Feature<String> fv4_loaded = pf_loaded.getFeature("locTxt");
		assertEquals(untypedFeature4loc.getValues(deCtx), fv4_loaded.getValues(deCtx));
		assertEquals(untypedFeature4loc.getValues(enCtx), fv4_loaded.getValues(enCtx));

		pf_loaded.clearFeatures();

		assertTrue(pf_loaded.isEmpty());
	}

	/**
	 * Tests bug PLA-5267
	 */
	@Test
	public void testClassificationAssignment() throws ConsistencyCheckException
	{
		ClassificationSystem sys1, sys2;
		ClassificationSystemVersion sv1, sv2;
		assertNotNull(sys1 = CatalogManager.getInstance().createClassificationSystem("sys1"));
		assertNotNull(sys2 = CatalogManager.getInstance().createClassificationSystem("sys2"));
		assertNotNull(sv1 = sys1.createSystemVersion("sv1", Collections.EMPTY_SET));
		assertNotNull(sv2 = sys2.createSystemVersion("sv2", Collections.EMPTY_SET));

		ClassificationClass cl1, cl2;
		assertNotNull(cl1 = sv1.createClass("cl1"));
		assertNotNull(cl2 = sv2.createClass("cl2"));

		assertEquals(sys1, sv1.getClassificationSystem());
		assertEquals(sys2, sv2.getCatalog());
		assertEquals(sv1, cl1.getSystemVersion());
		assertEquals(sv2, CatalogManager.getInstance().getCatalogVersion(cl2));

		Category category1, category2;

		assertNotNull(category1 = CategoryManager.getInstance().createCategory("c"));
		assertNotNull(category2 = CategoryManager.getInstance().createCategory("cc"));

		category2.setSupercategories(category1);

		Product product;

		assertNotNull(product = ProductManager.getInstance().createProduct("xyz"));

		category2.addToProducts(product);

		assertEquals(Collections.singletonList(product), category2.getProducts());
		assertEquals(Collections.singletonList(category2), CategoryManager.getInstance().getSupercategories(product));

		category1.addToSupercategories(cl1);
		category2.addToSupercategories(cl2);

		assertEquals(Collections.singletonList(cl1), category1.getSupercategories());
		assertEquals(Arrays.asList(category1, cl2), category2.getSupercategories());

		assertEquals(Collections.singletonList(cl1), CatalogManager.getInstance().getClassificationClasses(category1));

		assertEquals(Collections.singletonList(cl1), CatalogManager.getInstance().getClassificationClasses(sv1, category2));
		assertEquals(Collections.singletonList(cl2), CatalogManager.getInstance().getClassificationClasses(sv2, category2));
		assertEquals(Arrays.asList(cl2, cl1), CatalogManager.getInstance().getClassificationClasses(category2));

		assertEquals(Collections.singletonList(cl1), CatalogManager.getInstance().getClassificationClasses(sv1, product));
		assertEquals(Collections.singletonList(cl2), CatalogManager.getInstance().getClassificationClasses(sv2, product));
		assertEquals(Arrays.asList(cl2, cl1), CatalogManager.getInstance().getClassificationClasses(product));
	}

	@Test
	public void testPLA6634() throws ConsistencyCheckException
	{
		final Product product;
		ClassificationSystem sys;
		ClassificationSystemVersion ver;
		ClassificationClass cl1, cl2;
		ClassificationAttribute attr;

		assertNotNull(product = ProductManager.getInstance().createProduct("pPp"));
		assertNotNull(sys = CatalogManager.getInstance().createClassificationSystem("foo"));
		assertNotNull(ver = sys.createSystemVersion("ver", jaloSession.getSessionContext().getLanguage()));
		assertNotNull(cl1 = ver.createClass("cl1"));
		assertNotNull(cl2 = ver.createClass("cl2"));
		assertNotNull(attr = ver.createClassificationAttribute("attr"));
		// assign attr to second class
		cl2.assignAttribute(attr);

		CategoryManager.getInstance().setSupercategories(product, (List) Arrays.asList(cl1, cl2));

		assertEquals(Arrays.asList(cl1, cl2), CatalogManager.getInstance().getClassificationClasses(product));

		// test for exception as described in PLA-6634
		final TypedFeature feat = Feature.loadTyped(product, attr);

		assertNotNull(feat);
		assertEquals(attr, feat.getClassificationAttribute());
		assertEquals(cl2, feat.getClassificationClass());
	}

	@Test
	public void testUniqueQualifierCache()
	{
		final ClassAttributeAssignment toaster_voltage = toaster.getAttributeAssignment(voltage);

		final String expected = toaster_voltage.getSystemVersion().getFullVersionName() + //
				"/" + //
				toaster_voltage.getClassificationClass().getCode() + //
				"." + // 
				toaster_voltage.getClassificationAttribute().getCode().toLowerCase();

		assertEquals(expected, FeatureContainer.createUniqueKey(toaster_voltage));

		final Method method = ReflectionUtils.findMethod(FeatureContainer.class, "getUniqueKeyCache");
		method.setAccessible(true);
		final Map<PK, String> cache = (Map<PK, String>) ReflectionUtils.invokeMethod(method, null);

		assertEquals(expected, cache.get(toaster_voltage.getPK()));

		// invalidate by cl.attribute
		voltage.setName("foo"); // (actually name does not make any difference but invalidation should catch) 
		assertNull(cache.get(toaster_voltage.getPK()));
		assertEquals(expected, FeatureContainer.createUniqueKey(toaster_voltage));
		assertEquals(expected, cache.get(toaster_voltage.getPK())); // cache filled again

		// invalidate by class
		toaster.setName("foo"); // (actually name does not make any difference but invalidation should catch)
		assertNull(cache.get(toaster_voltage.getPK()));
		assertEquals(expected, FeatureContainer.createUniqueKey(toaster_voltage));
		assertEquals(expected, cache.get(toaster_voltage.getPK())); // cache filled again

		// invalidate by cl.sys.version
		houseHold.getSystemVersion("1.0").setCategorySystemName("foo"); // (actually system name does not make any difference but invalidation should catch)
		assertNull(cache.get(toaster_voltage.getPK()));
		assertEquals(expected, FeatureContainer.createUniqueKey(toaster_voltage));
		assertEquals(expected, cache.get(toaster_voltage.getPK())); // cache filled again

		// invalidate by cl.system
		houseHold.setName("foo"); // (actually name does not make any difference but invalidation should catch)
		assertNull(cache.get(toaster_voltage.getPK()));
		assertEquals(expected, FeatureContainer.createUniqueKey(toaster_voltage));
		assertEquals(expected, cache.get(toaster_voltage.getPK())); // cache filled again
	}
}
