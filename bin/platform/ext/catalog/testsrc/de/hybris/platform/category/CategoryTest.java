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
package de.hybris.platform.category;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.tx.Transaction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Category
 *
 * @version ExtGen v0.3
 */
@IntegrationTest
public class CategoryTest extends HybrisJUnit4TransactionalTest
{

	private static final Logger LOG = Logger.getLogger(CategoryTest.class);
	private CategoryManager manager;


	@Before
	public void setUp() throws Exception
	{
		this.manager = CategoryManager.getInstance();
	}


	@Test
	public void testBugPLA81() throws Exception
	{
		Product product = null;
		Category category = null;

		assertNotNull(product = ProductManager.getInstance().createProduct(PK.createUUIDPK(0).getHex()));
		assertNotNull(category = CategoryManager.getInstance().createCategory(PK.createUUIDPK(0).getHex()));

		final Transaction tx = Transaction.current();
		tx.begin();

		boolean success = false;
		try
		{

			final CategoryManager categoryManager = CategoryManager.getInstance();
			Collection<Category> categories;
			categories = categoryManager.getCategoriesByProduct(product);
			assertFalse(categories.contains(category));

			category.addProduct(product);
			//LOG.info("done");
			//Transaction.current().pushToDBTest();

			categories = categoryManager.getCategoriesByProduct(product);
			assertTrue(categories.contains(category));


			success = true;
		}
		catch (final RuntimeException re)
		{
			LOG.error("Rolling back transaction");
			throw re;
		}
		finally
		{
			if (success)
			{
				tx.commit();
			}
			else
			{
				tx.rollback();
			}
		}
	}

	@Test
	public void testBugCORE4084()
	{
		Category category1, category2, category3;
		assertNotNull(category1 = manager.createCategory("c1"));
		assertNotNull(category3 = manager.createCategory("c3"));
		assertNotNull(category2 = manager.createCategory("c2"));

		assertEquals(Collections.EMPTY_LIST, category1.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category1.getSubcategories());

		assertEquals(Collections.EMPTY_LIST, category2.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category2.getSubcategories());

		assertEquals(Collections.EMPTY_LIST, category3.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category3.getSubcategories());

		// c1 -> c2

		category2.setSupercategories(Collections.singletonList(category1));

		assertEquals(1, category2.getSupercategoriesCount());

		assertEquals(Collections.EMPTY_LIST, category1.getSupercategories());
		assertEquals(Collections.singletonList(category2), category1.getSubcategories());

		assertEquals(Collections.singletonList(category1), category2.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category2.getSubcategories());

		assertEquals(Collections.EMPTY_LIST, category3.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category3.getSubcategories());

		Collection<Link> links = LinkManager.getInstance().getLinks(CategoryConstants.Relations.CATEGORYCATEGORYRELATION,
				Link.ANYITEM, category2);
		assertEquals(1, links.size());
		Link link = links.iterator().next();
		assertEquals(category1, link.getSource());

		// c3 -> c2
		category2.setSupercategories(Collections.singletonList(category3));

		assertEquals(Collections.EMPTY_LIST, category1.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category1.getSubcategories());

		assertEquals(Collections.singletonList(category3), category2.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category2.getSubcategories());

		assertEquals(Collections.EMPTY_LIST, category3.getSupercategories());
		assertEquals(Collections.singletonList(category2), category3.getSubcategories());

		links = LinkManager.getInstance().getLinks(CategoryConstants.Relations.CATEGORYCATEGORYRELATION, Link.ANYITEM, category2);
		assertEquals(1, links.size());
		link = links.iterator().next();
		assertEquals(category3, link.getSource());

		// c3 -> c2 [0] , c1 -> c2 [0] , c3 -> c1 [1]
		category2.setSupercategories(Arrays.asList(category3, category1));
		category1.setSupercategories(Arrays.asList(category3));

		assertCollection(Collections.singletonList(category3), category1.getSupercategories());
		assertCollection(Collections.singletonList(category2), category1.getSubcategories());

		assertCollection(Arrays.asList(category3, category1), category2.getSupercategories());
		assertEquals(Collections.EMPTY_LIST, category2.getSubcategories());

		assertEquals(Collections.EMPTY_LIST, category3.getSupercategories());
		assertEquals(Arrays.asList(category2, category1), category3.getSubcategories());

		// c3 -> c2 [0], c1 -> c2 [0]
		links = LinkManager.getInstance().getLinks(CategoryConstants.Relations.CATEGORYCATEGORYRELATION, Link.ANYITEM, category2);
		assertEquals(2, links.size());
		for (final Link lnk : links)
		{
			if (category3.equals(lnk.getSource()))
			{
				assertEquals(category2, lnk.getTarget());
			}
			else if (category1.equals(lnk.getSource()))
			{
				assertEquals(category2, lnk.getTarget());
			}
		}
		// c3 -> c1 [1]
		links = LinkManager.getInstance().getLinks(CategoryConstants.Relations.CATEGORYCATEGORYRELATION, Link.ANYITEM, category1);
		assertEquals(1, links.size());
		link = links.iterator().next();
		assertEquals(category3, link.getSource());
	}

	@Test
	public void testBugPLA7005a()
	{
		//test without attribute set in ctx
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.removeAttribute(Category.DISABLE_SUBCATEGORY_REMOVALCHECK);

		Category category1, category2, category21, category3, category4, category5;
		category1 = manager.createCategory("c1");
		assertEquals(Collections.EMPTY_LIST, category1.getSubcategories());

		category2 = manager.createCategory("c2");
		category21 = manager.createCategory("c21");
		category2.addSubcategory(localctx, category21);
		assertEquals(1, category2.getSubcategories().size());

		category3 = manager.createCategory("c3");
		category4 = manager.createCategory("c4");
		category5 = manager.createCategory("c5");
		category3.addSubcategory(localctx, category5);
		category4.addSubcategory(localctx, category5);
		assertEquals(1, category3.getSubcategoryCount());
		assertEquals(1, category4.getSubcategoryCount());
		assertEquals(0, category5.getSubcategoryCount());
		assertEquals(2, category5.getSupercategoryCount());

		try
		{
			category1.remove(localctx);
			assertFalse(category1.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue("category c1 is still alive!", category1.isAlive());
		}

		try
		{
			category2.remove(localctx);
			assertFalse("c2 was removed but still got a subcategory", category2.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue(category2.isAlive());
		}

		try
		{
			category3.remove(localctx);
			assertFalse("c3 was removed but still got a subcategory", category3.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue(category3.isAlive());
		}

		JaloSession.getCurrentSession().removeLocalSessionContext();
	}


	@Test
	public void testBugPLA7005b()
	{

		//test with attribute set in ctx and set to true
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.setAttribute(Category.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.TRUE);

		Category category1, category2, category21, category3, category4, category5;
		category1 = manager.createCategory("c1");
		assertEquals(Collections.EMPTY_LIST, category1.getSubcategories());

		category2 = manager.createCategory("c2");
		category21 = manager.createCategory("c21");
		category2.addSubcategory(localctx, category21);
		assertEquals(1, category2.getSubcategories().size());

		category3 = manager.createCategory("c3");
		category4 = manager.createCategory("c4");
		category5 = manager.createCategory("c5");
		category3.addSubcategory(localctx, category5);
		category4.addSubcategory(localctx, category5);
		assertEquals(1, category3.getSubcategoryCount());
		assertEquals(1, category4.getSubcategoryCount());
		assertEquals(0, category5.getSubcategoryCount());
		assertEquals(2, category5.getSupercategoryCount());

		try
		{
			category1.remove(localctx);
			assertFalse(category1.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue("category c1 is still alive!", category1.isAlive());
		}

		try
		{
			category2.remove(localctx);
			assertFalse(category2.isAlive());
			assertTrue(category21.isAlive());
			assertEquals(0, category21.getSubcategoryCount());
			assertEquals(0, category21.getSupercategoryCount());
		}
		catch (final ConsistencyCheckException e)
		{
			fail("There should be no ConsistencyCheckException");
		}

		try
		{
			category3.remove(localctx);
			assertFalse(category3.isAlive());
			assertTrue(category4.isAlive());
			assertTrue(category5.isAlive());
			assertEquals(1, category4.getSubcategoryCount());
			assertEquals(1, category5.getSupercategoriesCount());
			assertEquals(0, category5.getSubcategoryCount());
		}
		catch (final ConsistencyCheckException e)
		{
			fail("There should be no ConsistencyCheckException");
		}

		JaloSession.getCurrentSession().removeLocalSessionContext();
	}

	@Test
	public void testBugPLA7005c()
	{
		//test with attribute set in ctx and set to false
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.setAttribute(Category.DISABLE_SUBCATEGORY_REMOVALCHECK, Boolean.FALSE);


		Category category1, category2, category21, category3, category4, category5;
		category1 = manager.createCategory("c1");
		assertEquals(Collections.EMPTY_LIST, category1.getSubcategories());

		category2 = manager.createCategory("c2");
		category21 = manager.createCategory("c21");
		category2.addSubcategory(localctx, category21);
		assertEquals(1, category2.getSubcategories().size());

		category3 = manager.createCategory("c3");
		category4 = manager.createCategory("c4");
		category5 = manager.createCategory("c5");
		category3.addSubcategory(localctx, category5);
		category4.addSubcategory(localctx, category5);
		assertEquals(1, category3.getSubcategoryCount());
		assertEquals(1, category4.getSubcategoryCount());
		assertEquals(0, category5.getSubcategoryCount());
		assertEquals(2, category5.getSupercategoryCount());

		try
		{
			category1.remove(localctx);
			assertFalse(category1.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue("category c1 is still alive!", category1.isAlive());
		}

		try
		{
			category2.remove(localctx);
			assertFalse("c2 was removed but still got a subcategory", category2.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue(category2.isAlive());
		}

		try
		{
			category3.remove(localctx);
			assertFalse("c3 was removed but still got a subcategory", category3.isAlive());
		}
		catch (final ConsistencyCheckException e)
		{
			assertTrue(category3.isAlive());
		}

		JaloSession.getCurrentSession().removeLocalSessionContext();
	}
}
