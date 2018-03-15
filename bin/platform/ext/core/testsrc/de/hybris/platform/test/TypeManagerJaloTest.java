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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TypeManagerJaloTest extends HybrisJUnit4TransactionalTest
{
	TypeManager typeManager;


	@Before
	public void setUp() throws Exception
	{
		typeManager = jaloSession.getTypeManager();
	}

	@Test
	public void testTypeCaching() throws Exception
	{
		final ComposedType t = typeManager.createComposedType(typeManager.getComposedType("Item"), "testeee");
		typeManager.getComposedType("testeee");
		t.remove();
		try
		{
			typeManager.getComposedType("testeee");
			fail("was able to find the removed type");
		}
		catch (final JaloItemNotFoundException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testCacheBound() throws Exception
	{
		final int CNT = 100000;
		Product p = ProductManager.getInstance().createProduct("123123");
		StopWatch w = new StopWatch("incache");
		p.getName();
		for (int i = 0; i < CNT; i++)
		{
			p.getName();
		}
		w.stop();

		Registry.getCurrentTenant().getCache().clear();
		final Product p2 = JaloSession.getCurrentSession().getItem(p.getPK());
		assertFalse(p == p2);
		assertFalse(p.isCacheBound());
		w = new StopWatch("outcache");
		p.getName();
		for (int i = 0; i < CNT; i++)
		{
			p.getName();
		}
		w.stop();

		p = (Product) p.getCacheBoundItem();
		assertTrue(p.isCacheBound());
		w = new StopWatch("afterrefresh");
		p.getName();
		for (int i = 0; i < CNT; i++)
		{
			p.getName();
		}
		w.stop();
	}


	@Test
	public void testGetComposedType() throws JaloItemNotFoundException
	{
		assertEquals(typeManager.getType("Country"), typeManager.getComposedType("Country"));
		final AtomicType at = typeManager.getRootAtomicType(String.class);
		try
		{
			final Type t = typeManager.getComposedType(at.getCode());
			fail("expected JaloItemNotFoundException, found " + t);
		}
		catch (final JaloItemNotFoundException e)
		{
			// fine..
		}
	}

	@Test
	public void testCreateAtomicType() throws JaloDuplicateCodeException, JaloInvalidParameterException,
			ConsistencyCheckException, JaloItemNotFoundException, ClassNotFoundException
	{
		AtomicType at;
		try
		{
			at = typeManager.createAtomicType(Item.class);
			at.remove();
			fail("expected exception");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		try
		{
			at = typeManager.createAtomicType(Class.forName("de.hybris.platform.persistence.ItemRemote"));
			at.remove();
			fail("expected exception");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		try
		{
			at = typeManager.createAtomicType(null, "code");
			at.remove();
			fail("expected exception");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		final AtomicType stringType = typeManager.getRootAtomicType(String.class);
		try
		{
			at = typeManager.createAtomicType(stringType, (String) null);
			at.remove();
			fail("expected exception");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
		try
		{
			at = typeManager.createAtomicType(stringType, stringType.getCode());
			at.remove();
			fail("expected exception");
		}
		catch (final JaloDuplicateCodeException e)
		{
			// fine
		}
	}

}
