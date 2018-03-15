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
package de.hybris.platform.catalog.jalo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ItemRemoveTest extends HybrisJUnit4TransactionalTest
{
	private final CategoryManager catman = CategoryManager.getInstance();
	private final ProductManager prodman = ProductManager.getInstance();
	private final CatalogManager cman = CatalogManager.getInstance();
	private Category root, sub;
	private Product prod;
	private Catalog catalog;
	private CatalogVersion catver;

	@Before
	public void setUp() throws Exception
	{
		root = catman.createCategory("root");
		sub = catman.createCategory("sub");
		sub.setSupercategories(root);
		prod = prodman.createProduct("prod");
		sub.setProducts(Collections.singletonList(prod));

		catalog = cman.createCatalog("catalog");
		catver = cman.createCatalogVersion(catalog, "catver", null);
		cman.setCatalogVersion(prod, catver);
		cman.setCatalogVersion(root, catver);
		cman.setCatalogVersion(sub, catver);
		catver.setActive(true);
	}


	@Test
	public void testDefaultBehaviour() throws Exception
	{
		try
		{
			root.remove(); //exception
			fail("root contains a subcat. Therfore a ConsistencyCheckException should be thrown");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO document reason why this is empty

		}
		catch (final Exception e)
		{
			fail("unknown exception. Shouldn't happen");
			throw e;
		}

	}

	@Test
	public void testDisableFlagForCategory() throws Exception
	{
		final SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
		ctx.setAttribute(Category.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.TRUE);
		ctx.setAttribute(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.FALSE);
		try
		{
			root.remove(); //exception
			assertTrue("subcat isn't alive, but only root cat was deleted", sub.isAlive());
			assertFalse("rootcat is still alive but should be deleted", root.isAlive());
		}
		catch (final Exception e)
		{
			fail("unknown exception. Shouldn't happen");
			throw e;
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	@Test
	public void testDisableFlagForItem() throws Exception
	{
		final SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
		ctx.setAttribute(Category.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.FALSE);
		ctx.setAttribute(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);
		try
		{
			root.remove();
			assertTrue("subcat isn't alive, but only root cat was deleted", sub.isAlive());
			assertFalse("rootcat is still alive but should be deleted", root.isAlive());
		}
		catch (final Exception e)
		{
			fail("unknown exception. Shouldn't happen");
			throw e;
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	@Test
	public void testFlagSetToFalse() throws Exception
	{
		final SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
		ctx.setAttribute(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.FALSE);
		try
		{
			root.remove(); //exception
			fail("root contains a subcat. Therfore a ConsistencyCheckException should be thrown");
		}
		catch (final ConsistencyCheckException e)
		{
			// document why this is empty
		}
		catch (final Exception e)
		{
			fail("unknown exception. Shouldn't happen");
			throw e;
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	@Test
	public void testDisableFlagForOtherItem() throws Exception
	{
		assertTrue("catalog does not exist", catalog.isAlive());
		assertTrue("catalogversion does not exist", catver.isAlive());
		try
		{
			catalog.remove(); //exception
			fail("catalog contains active catalogversion. Therfore a ConsistencyCheckException should be thrown");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine
		}
		catch (final Exception e)
		{
			fail("unknown exception " + e + ". Shouldn't happen");
		}
	}

	@Test
	public void testDisableFlagForOtherItem2() throws Exception
	{
		assertTrue("catalog does not exist", catalog.isAlive());
		assertTrue("catalogversion does not exist", catver.isAlive());
		assertTrue("category does not exist", root.isAlive());
		assertTrue("product does not exist", prod.isAlive());

		// disable consistency checks
		final SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
		ctx.setAttribute(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);
		try
		{
			catalog.remove(); //exception
			assertFalse("catalog was not deleted", catalog.isAlive());
			assertFalse("catver was not deleted", catver.isAlive()); // deleted recursively
			assertFalse("category was not deleted", root.isAlive()); // deleted recursively
			assertTrue("product was deleted", prod.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			fail("consistency exception shouldn't happen here");
		}
		catch (final Exception e)
		{
			fail("unknown exception " + e + ". Shouldn't happen");
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}

	}
}
