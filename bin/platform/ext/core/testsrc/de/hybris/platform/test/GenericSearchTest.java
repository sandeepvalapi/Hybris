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
package de.hybris.platform.test;

import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericFunctionSelectField;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchFieldType;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.StandardSearchContext;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class GenericSearchTest extends HybrisJUnit4TransactionalTest
{
	static final Logger LOG = Logger.getLogger(GenericSearchTest.class.getName());

	ProductManager productManager;
	C2LManager c2lManager;

	Language german, english;
	Product product1, product2, product3, product4;
	Unit unit1;

	SessionContext ctxDe;
	SessionContext ctxEn;

	ComposedType productType;
	ComposedType unitType;
	ComposedType composedType;

	//--
	@Before
	public void setUp() throws Exception
	{
		productManager = jaloSession.getProductManager();
		c2lManager = jaloSession.getC2LManager();

		productType = jaloSession.getTypeManager().getRootComposedType(Constants.TC.Product);
		unitType = jaloSession.getTypeManager().getRootComposedType(Constants.TC.Unit);
		composedType = jaloSession.getTypeManager().getRootComposedType(Constants.TC.ComposedType);

		english = getOrCreateLanguage("en");
		german = getOrCreateLanguage("de");

		ctxDe = jaloSession.createSessionContext();
		ctxDe.setLanguage(german);
		ctxEn = jaloSession.createSessionContext();
		ctxEn.setLanguage(english);

		assertNotNull(product1 = makeProduct(1));
		assertNotNull(product2 = makeProduct(2));
		assertNotNull(product3 = makeProduct(3));
		assertNotNull(product4 = makeProduct(4));

		assertNotNull(unit1 = productManager.createUnit("piece", "unit1"));
		product1.setUnit(unit1);
	}


	//--
	private final Product makeProduct(final int number) throws Exception
	{
		final Product result = productManager.createProduct("product" + number);
		result.setName(ctxDe, result.getCode());
		result.setName(ctxEn, result.getCode());
		return result;
	}

	@Test
	public void testGenericConditions() throws Exception
	{
		final GenericSearchField codeField = new GenericSearchField(productType.getCode(), Product.CODE);
		final GenericSearchField nameField = new GenericSearchField(productType.getCode(), Product.NAME);
		final GenericSelectField codeSelectField = new GenericSelectField(productType.getCode(), Product.CODE, String.class);
		final GenericSelectField nameSelectField = new GenericSelectField(productType.getCode(), Product.NAME, String.class);

		//---- test value condition /  condition list
		GenericCondition condition = GenericCondition.createConditionForValueComparison(codeField, Operator.EQUAL,
				product1.getCode());
		final GenericConditionList conditionList = GenericCondition.createConditionList(condition);
		conditionList.addToConditionList(GenericCondition.createConditionForValueComparison(nameField, Operator.STARTS_WITH,
				"pRoduCt", /* upper */true));
		GenericQuery query = new GenericQuery(productType.getCode());
		query.addCondition(conditionList);
		query.addSelectField(codeSelectField);
		query.addSelectField(nameSelectField);

		Collection result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(1, result.size());
		assertEquals(0, product1.getCode().compareTo((String) ((Collection) result.iterator().next()).iterator().next()));
		// wrong operator
		try
		{
			condition.setOperator(Operator.IS_NULL);
			fail("Should have thrown an IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{/* expected */
		}

		//---- test literal condition 

		try
		// wrong operator
		{
			condition = GenericCondition.createConditionForLiteralComparison(codeField, Operator.GREATER);
			fail("Should have thrown an IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{/* expected */
		}

		//	code field not all (should find all)
		condition = GenericCondition.createConditionForLiteralComparison(codeField, Operator.IS_NOT_NULL);
		query = new GenericQuery(productType.getCode());
		query.addCondition(condition);
		result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(4, result.size());

		//---- test field comparison condition

		try
		// wrong operator
		{
			condition = GenericCondition.createConditionForFieldComparison(codeField, Operator.IS_NOT_NULL, nameField);
			fail("Should have thrown an IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{/* expected */
		}

		//	compare name field with code field (should find all)
		condition = GenericCondition.createConditionForFieldComparison(codeField, Operator.EQUAL, nameField);
		query = new GenericQuery(productType.getCode());
		query.addCondition(condition);
		result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(4, result.size());

		// test function select MAX
		query = new GenericQuery(productType.getCode());
		query.addSelectField(new GenericFunctionSelectField(Product.CODE, String.class, "MAX"));
		result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(Collections.singletonList(product4.getCode()), result);

		// test function select MIN
		query = new GenericQuery(productType.getCode());
		query.addSelectField(new GenericFunctionSelectField(Product.CODE, String.class, "MIN"));
		result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(Collections.singletonList(product1.getCode()), result);

		// test function select COUNT
		query = new GenericQuery(productType.getCode());
		query.addSelectField(new GenericFunctionSelectField(Product.PK, Integer.class, "COUNT"));
		result = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(Collections.singletonList(Integer.valueOf(4)), result);
	}

	@Test
	public void testGenericTypeJoin() throws Exception
	{
		final GenericSearchField productUnitField = new GenericSearchField(productType.getCode(), Product.UNIT);
		final GenericSearchField unitPkField = new GenericSearchField(unitType.getCode(), Item.PK);
		final GenericSearchField unitCodeField = new GenericSearchField(unitType.getCode(), Unit.CODE);
		final GenericSearchField unitComposedTypeField = new GenericSearchField(unitType.getCode(), Item.TYPE);
		final GenericSearchField composedTypePkField = new GenericSearchField(composedType.getCode(), Item.PK);
		final GenericSearchField dumbOrderByField = new GenericSearchField(Constants.TYPES.Customer, Principal.NAME);

		assertEquals(unit1, product1.getUnit());

		//test inner join
		GenericQuery query = new GenericQuery(productType.getCode());
		query.addInnerJoin(unitType.getCode(), GenericCondition.createJoinCondition(productUnitField, unitPkField));
		Collection products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(1, products.size());

		//	test double inner join
		query = new GenericQuery(productType.getCode());
		query.addInnerJoin(unitType.getCode(), GenericCondition.createJoinCondition(productUnitField, unitPkField));
		query.addInnerJoin(composedType.getCode(), GenericCondition.createJoinCondition(unitComposedTypeField, composedTypePkField));
		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(1, products.size());

		//	test outer join
		query = new GenericQuery(productType.getCode());
		query.addOuterJoin(unitType.getCode(), GenericCondition.createJoinCondition(productUnitField, unitPkField));
		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(4, products.size());

		//	test outer join with order by condition
		query = new GenericQuery(productType.getCode());
		query.addOuterJoin(unitType.getCode(), GenericCondition.createJoinCondition(productUnitField, unitPkField));
		query.addOrderBy(new GenericSearchOrderBy(unitCodeField, false));
		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(4, products.size());

		//	test outer join with inappropriate order by condition
		query = new GenericQuery(productType.getCode());
		query.addOuterJoin(unitType.getCode(), GenericCondition.createJoinCondition(productUnitField, unitPkField));
		query.addOrderBy(new GenericSearchOrderBy(dumbOrderByField, false));
		try
		{
			jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
			fail("Should have thrown RuntimeException here...");
		}
		catch (final Exception e)
		{
			// fine here
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
		}
	}

	@Test
	public void testNullValues() throws Exception
	{
		/*
		 * final GenericSearchField unitCodeField = new GenericSearchField( unitType.getCode(), Unit.CODE );
		 * 
		 * //test exists subquery GenericQuery query = new GenericQuery( productType.getCode() );
		 * query.addCondition(GenericCondition.equals(new GenericSearchField( productType.getCode(), Product.CODE),
		 * null));
		 * 
		 * Collection products = jaloSession.search( query, new StandardSearchContext( ctxDe ) ).getResult();
		 */

	}





	@Test
	public void testSubQuery() throws Exception
	{
		final GenericSearchField unitCodeField = new GenericSearchField(unitType.getCode(), Unit.CODE);

		//test exists subquery 
		GenericQuery query = new GenericQuery(productType.getCode(), GenericCondition.getComparison(Product.CODE, Operator.EQUAL,
				product1.getCode()));

		query.addSubQuery(Product.UNIT, Operator.IN, unitType.getCode()).addCondition(
				GenericCondition.getComparison(unitCodeField, Operator.EQUAL, "unit1"));

		Collection products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(1, products.size());

		//test is_in subquery
		query = new GenericQuery(productType.getCode(), GenericCondition.getComparison(Product.CODE, Operator.EQUAL,
				product1.getCode()));

		query.addSubQuery(Operator.EXISTS, unitType.getCode()).addCondition(
				GenericCondition.getComparison(unitCodeField, Operator.EQUAL, "unit1"));

		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(1, products.size());
		assertEquals(0, query.getCondition().getResettableValues().size());

		//test not_exists 
		query = new GenericQuery(productType.getCode(), GenericCondition.getComparison(Product.CODE, Operator.EQUAL,
				product1.getCode()));

		query.addSubQuery(Operator.NOT_EXISTS, unitType.getCode()).addCondition(
				GenericCondition.getComparison(unitCodeField, Operator.EQUAL, "unit1"));

		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(0, products.size());

		/*
		 * test subtypes upon same type:
		 * 
		 * SELECT {PK} FROM {Product} WHERE {code} IN ( {{ SELECT {code} FROM {Product} WHERE {unit}=unit1 }}) -> must
		 * return product1 only
		 */

		query = new GenericQuery(productType.getCode());
		final GenericQuery subquery = query.addSubQuery(Product.CODE, // means outer query type.code !!!
				Operator.IN, productType.getCode() // initial type of the subquery 
				);
		subquery.addSelectField(new GenericSelectField(Product.CODE)); // means subquery type.code !!!
		subquery.addCondition(GenericCondition.equals(new GenericSearchField(Product.UNIT), unit1));
		products = jaloSession.search(query).getResult();
		assertEquals(Collections.singletonList(product1), products);

		/*
		 * test self-join
		 * 
		 * SELECT {PK}, {other:pk} FROM {Product JOIN Product AS other ON {code} = {other:code} } WHERE {code} IN (
		 * 'product2', 'product4' )
		 */
		query = new GenericQuery(productType.getCode());
		query.addOuterJoin(productType.getCode(), "other", GenericCondition.getComparison(new GenericSearchField(Product.CODE),
				Operator.EQUAL, new GenericSearchField("other", Product.CODE)));
		query.addCondition(GenericCondition.getComparison(Product.CODE, Operator.IN,
				Arrays.asList(product2.getCode(), product4.getCode())));
		products = jaloSession.search(query).getResult();
		assertEquals(new HashSet(Arrays.asList(product2, product4)), new HashSet(products));

		// test duplicate alias error
		query = new GenericQuery(productType.getCode());
		query.addOuterJoin(productType.getCode(), // this must throw an error upon flexiblesearch generation 
				GenericCondition.getComparison(new GenericSearchField(Product.CODE), Operator.EQUAL, new GenericSearchField("other",
						Product.CODE)));
		try
		{
			query.toFlexibleSearch(new HashMap());
			fail("InvalidParameterException expected");
		}
		catch (final InvalidParameterException e)
		{
			// fine
		}
	}

	@Test
	public void testResettableValues() throws Exception
	{
		final GenericSearchField codeField = new GenericSearchField(productType.getCode(), Product.CODE);
		final GenericSearchField nameField = new GenericSearchField(productType.getCode(), Product.NAME);

		//build query 
		final String codeQualifier = "code_qual";
		final String nameQualifier = "name_qual";
		final GenericQuery query = new GenericQuery(productType.getCode());
		query.addCondition(GenericCondition.createConditionForValueComparison(codeField, Operator.EQUAL, product1.getCode(),
				codeQualifier));
		query.addCondition(GenericCondition.createConditionForValueComparison(nameField, Operator.EQUAL, product1.getName(ctxDe),
				nameQualifier));

		Collection products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(product1, products.iterator().next());

		assertEquals(2, query.getCondition().getResettableValues().size());
		assertTrue(query.getCondition().getResettableValues().containsKey(codeQualifier));
		assertTrue(query.getCondition().getResettableValues().containsKey(nameQualifier));

		//reset values
		query.getCondition().setResettableValue(codeQualifier, product2.getCode());
		query.getCondition().setResettableValue(nameQualifier, product2.getName(ctxDe));
		products = jaloSession.search(query, new StandardSearchContext(ctxDe)).getResult();
		assertEquals(product2, products.iterator().next());
	}


	@Test
	public void testLocalized() throws Exception
	{
		final String qualifier = "name";

		final Product product5 = productManager.createProduct("product5");
		assertNotNull(product5);

		product1.setLocalizedProperty(ctxDe, qualifier, "locvalueA");
		product1.setLocalizedProperty(ctxEn, qualifier, "locvalueA");
		product2.setLocalizedProperty(ctxDe, qualifier, "locvalueB");
		product2.setLocalizedProperty(ctxEn, qualifier, "locvalueB");
		product3.setLocalizedProperty(ctxDe, qualifier, "locvalueA");
		product3.setLocalizedProperty(ctxEn, qualifier, "locvalueB");
		product4.setLocalizedProperty(ctxDe, qualifier, "locvalueB");
		product4.setLocalizedProperty(ctxEn, qualifier, "locvalueA");
		assertNull(product5.getLocalizedProperty(ctxDe, qualifier));
		product5.setLocalizedProperty(ctxEn, qualifier, "trallala");


		final GenericSearchField field = new GenericSearchField(productType.getCode(), qualifier);
		field.setFieldTypes(Arrays.asList(new Object[]
		{ GenericSearchFieldType.LOCALIZED, GenericSearchFieldType.PROPERTY }));
		GenericCondition genericCondition = GenericCondition.createConditionForLiteralComparison(field, Operator.IS_NULL);
		GenericQuery query = new GenericQuery(productType.getCode(), genericCondition);
		SearchResult searchResultDe = jaloSession.search(query, new StandardSearchContext(ctxDe));
		assertCollectionElements(searchResultDe.getResult(), product5);

		genericCondition = GenericCondition.createConditionForValueComparison(field, Operator.EQUAL, "locvalueA");
		query = new GenericQuery(productType.getCode(), genericCondition);
		searchResultDe = jaloSession.search(query, new StandardSearchContext(ctxDe));
		assertCollectionElements(searchResultDe.getResult(), product1, product3);

		final SearchResult searchResultEn = jaloSession.search(query, new StandardSearchContext(ctxEn));
		assertCollectionElements(searchResultEn.getResult(), product1, product4);
	}
}
