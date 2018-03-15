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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class LocalizedAttributeTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(LocalizedAttributeTest.class.getName());

	Tax tax;
	Language lang1;
	Language lang2;
	SessionContext ctx;
	SessionContext ctx2;

	@Before
	public void setUp() throws Exception
	{
		assertNotNull(tax = jaloSession.getOrderManager().createTax("testTax"));
		assertNotNull(lang1 = jaloSession.getC2LManager().createLanguage("lang1"));
		assertNotNull(lang2 = jaloSession.getC2LManager().createLanguage("lang2"));
		ctx = jaloSession.createSessionContext();

		ctx.setLanguage(lang1);
		tax.setAttribute(ctx, "name", "name1");

		ctx.setLanguage(lang2);
		tax.setAttribute(ctx, "name", "name2");

		ctx2 = jaloSession.createSessionContext();
		ctx2.setLanguage(null);
	}

	@Test
	public void testSearchForLocalizedAttribute() throws Exception
	{
		// test getting the value
		final Map map = (Map) tax.getAttribute(ctx2, "name");
		assertFalse("should not be empty!", map.isEmpty());
		ctx.setLanguage(lang1);
		assertEquals("name1", tax.getAttribute(ctx, "name"));
		assertEquals("name1", tax.getLocalizedProperty(ctx, "name"));
		ctx.setLanguage(lang2);
		assertEquals("name2", tax.getAttribute(ctx, "name"));
		assertEquals("name2", tax.getLocalizedProperty(ctx, "name"));
		// test searching

		final ComposedType taxType = tax.getComposedType();
		final SearchContext ctx = jaloSession.createSearchContext();
		ctx.setLanguage(lang1);
		final GenericCondition gc = GenericCondition
				.createLikeCondition(new GenericSearchField(taxType.getCode(), "name"), "name%");
		/* conv-log */log.debug("---------------------------------------");
		final SearchResult result = jaloSession.search(new GenericQuery(taxType.getCode(), gc), ctx);
		/* conv-log */log.debug("---------------------------------------");
		assertEquals(1, result.getTotalCount());
		assertCollection(new HashSet(Arrays.asList(new Object[]
		{ tax })), new HashSet(result.getResult()));
	}
}
