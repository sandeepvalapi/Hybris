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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.JaloPropertyContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PropertyJaloTest extends HybrisJUnit4TransactionalTest
{
	private ProductManager pm;
	private Product p1;

	@Before
	public void setUp() throws Exception
	{
		pm = jaloSession.getProductManager();
		assertNotNull(p1 = pm.createProduct("testprod"));
	}

	@Test
	public void testIntegrity() throws JaloInvalidParameterException, JaloBusinessException
	{
		final ComposedType mediaType = TypeManager.getInstance().getComposedType(Media.class);
		final ComposedType titleType = TypeManager.getInstance().getComposedType(Title.class);

		final String TITLE = "theTitle";

		AttributeDescriptor ad;
		assertNotNull(ad = mediaType.createAttributeDescriptor(TITLE, titleType, AttributeDescriptor.READ_FLAG
				+ AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.REMOVE_FLAG));

		assertNotNull(ad);
		assertTrue(ad.isProperty());

		Media m;

		assertNotNull(m = MediaManager.getInstance().createMedia("trallala"));

		assertNull(m.getAttribute(TITLE));

		Title t;

		assertNotNull(t = UserManager.getInstance().createTitle("xyz"));

		m.setAttribute(TITLE, t);
		assertEquals(t, m.getAttribute(TITLE));

		t.remove();
		assertNotNull(t);
		assertFalse(t.isAlive());

		assertNull(m.getAttribute(TITLE));
	}

	@Test
	public void testNameCase()
	{
		// test typed localized prop
		final String oldName = (String) p1.setLocalizedProperty(Product.NAME, "name");
		assertEquals("name", p1.getLocalizedProperty(Product.NAME));
		assertEquals("name", p1.getLocalizedProperty(Product.NAME.toUpperCase()));
		p1.setLocalizedProperty(Product.NAME.toUpperCase(), "xxx");
		assertEquals("xxx", p1.getLocalizedProperty(Product.NAME));
		assertEquals("xxx", p1.getLocalizedProperty(Product.NAME.toUpperCase()));
		p1.setLocalizedProperty(Product.NAME, oldName);
		assertEquals(oldName, p1.getLocalizedProperty(Product.NAME));
		assertEquals(oldName, p1.getLocalizedProperty(Product.NAME.toUpperCase()));

		// test untyped prop
		p1.setProperty("blah", "fasel");
		assertEquals("fasel", p1.getProperty("blah"));
		assertEquals("fasel", p1.getProperty("BLAH"));
		assertEquals("fasel", p1.setProperty("BlAH", "xyz"));
		assertEquals("xyz", p1.getProperty("blah"));
		assertEquals("xyz", p1.getProperty("BLAH"));
		p1.removeProperty("blah");
		assertEquals(null, p1.getProperty("blah"));
		assertEquals(null, p1.getProperty("BLAH"));

		p1.setProperty("UnTyPeD", Boolean.TRUE);
		Registry.getCurrentTenant().getCache().clear();
		assertTrue(p1.getPropertyNames().contains("UnTyPeD"));
		assertFalse(p1.getPropertyNames().contains("untyped"));
	}

	@Test
	public void testSetGetProperty()
	{
		// PLA-5129
		final int defaultPropertiesCount = p1.getAllProperties().size();
		final int defaultLocalizedPropertiesCount = p1.getAllLocalizedProperties().size();

		p1.setProperty("ert", "test");

		assertEquals("test", p1.getProperty("ert"));
		// test property name case
		assertEquals("test", p1.getProperty("erT"));
		//
		assertNull(p1.getLocalizedProperty("ert"));
		assertNull(p1.getLocalizedProperty("erT"));
		assertTrue(p1.getPropertyNames().contains("ert"));
		assertFalse(p1.getLocalizedPropertyNames().contains("ert"));

		Map map = p1.getAllProperties();
		assertEquals(defaultPropertiesCount + 1, map.keySet().size());
		assertEquals("test", map.get("ert"));

		p1.removeProperty("ert");
		assertNull(p1.getProperty("ert"));
		assertNull(p1.getProperty("erT"));

		map = p1.getAllProperties();
		assertEquals(defaultPropertiesCount, map.keySet().size());

		//----

		p1.setLocalizedProperty("ert", "test");
		assertEquals("test", p1.getLocalizedProperty("ert"));
		assertEquals("test", p1.getLocalizedProperty("erT"));

		map = p1.getAllLocalizedProperties();
		assertEquals(defaultLocalizedPropertiesCount + 1, map.keySet().size());
		assertEquals("test", map.get("ert"));

		p1.removeLocalizedProperty("ert");
		assertNull(p1.getLocalizedProperty("ert"));

		map = p1.getAllLocalizedProperties();
		assertEquals(defaultLocalizedPropertiesCount, map.keySet().size());

	}

	@Test
	public void testBug282() throws Exception
	{
		Language deLan;
		assertNotNull(deLan = jaloSession.getC2LManager().createLanguage("xde"));
		Language enLan;
		assertNotNull(enLan = jaloSession.getC2LManager().createLanguage("xen"));

		//jaloSession.getSessionContext().setStagingMethod( Constants.STAGING.PRODUCTIVE );

		final SessionContext enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(enLan);

		final SessionContext deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(deLan);

		Product ppp;

		assertNotNull(ppp = jaloSession.getProductManager().createProduct(enCtx, "ppp"));
		final Set defaultProperties = ppp.getAllProperties().keySet();

		assertEquals("ppp", ppp.getCode(enCtx));
		ppp.setName(enCtx, "asdasd");
		assertEquals("asdasd", ppp.getName(enCtx));
		assertEquals(null, ppp.getName(deCtx));
		ppp.remove(enCtx);
		assertNotNull(ppp);

		assertNotNull(ppp = jaloSession.getProductManager().createProduct(enCtx, "ppp"));
		assertNull(ppp.getName(enCtx));
		assertNull(ppp.getName(deCtx));
		assertCollection(defaultProperties, ppp.getPropertyNames(deCtx));
		assertCollection(defaultProperties, ppp.getPropertyNames(enCtx));
		ppp.remove();
		assertNotNull(ppp);
	}

	@Test
	public void testPropertyContainer() throws ConsistencyCheckException
	{
		JaloPropertyContainer container = jaloSession.createPropertyContainer();
		final Language langA = jaloSession.getC2LManager().createLanguage("jalopropertyA");
		assertNotNull(langA);
		final Language langB = jaloSession.getC2LManager().createLanguage("jalopropertyB");
		assertNotNull(langB);
		final SessionContext context = jaloSession.createSessionContext();

		//assertEquals( Constants.STAGING.PRODUCTIVE, jaloSession.getSessionContext().getStagingMethod() );
		container.setProperty("a", "1");
		container.setProperty("b", "2");
		container.setLocalizedProperty("a_l", "1s");
		context.setLanguage(langA);
		container.setLocalizedProperty(context, "a_l", "1a");
		context.setLanguage(langB);
		container.setLocalizedProperty(context, "a_l", "1b");
		p1.setAllProperties(container);

		assertEquals("1", p1.getProperty("a"));
		assertEquals("2", p1.getProperty("b"));
		assertEquals("1s", p1.getLocalizedProperty("a_l"));
		context.setLanguage(langA);
		assertEquals("1a", p1.getLocalizedProperty(context, "a_l"));
		context.setLanguage(langB);
		assertEquals("1b", p1.getLocalizedProperty(context, "a_l"));

		container = jaloSession.createPropertyContainer();
		container.setProperty("a", null);
		container.setProperty("a_i", null);
		p1.setAllProperties(context, container);

		assertEquals(null, p1.getProperty("a"));
		assertEquals(null, p1.getProperty(context, "a"));
		assertEquals("2", p1.getProperty("b"));
		assertEquals("1s", p1.getLocalizedProperty("a_l"));

		assertEquals("2", p1.getProperty("b"));
		assertEquals("2", p1.getProperty(context, "b"));
		container = jaloSession.createPropertyContainer();
		container.setProperty("b", null);
		p1.setAllProperties(container);
		assertEquals(null, p1.getProperty("b"));
		assertEquals(null, p1.getProperty(context, "b"));

		container = jaloSession.createPropertyContainer();
		container.setProperty("String", "String");
		container.setProperty("Date", new Date(1234567L));
		container.setProperty("Integer", Integer.valueOf(42));
		container.setProperty("Null", null);
		container.setProperty("Boolean", Boolean.FALSE);
		p1.setAllProperties(container);
		assertEquals("String", p1.getProperty("String"));
		assertEquals(new Date(1234567L), p1.getProperty("Date"));
		assertEquals(Integer.valueOf(42), p1.getProperty("Integer"));
		assertEquals(null, p1.getProperty("Null"));
		assertEquals(Boolean.FALSE, p1.getProperty("Boolean"));
	}

	@Test
	public void testPropertyContainerOnNotStagableItems() throws ConsistencyCheckException
	{
		final Language lang = jaloSession.getC2LManager().createLanguage("propContNStI");
		assertNotNull(lang);
		final SessionContext ctx = jaloSession.createSessionContext();
		//ctx.setStagingMethod( Constants.STAGING.PRODUCTIVE );
		ctx.setLanguage(lang);
		JaloPropertyContainer container = jaloSession.createPropertyContainer();
		container.setProperty("prop", "testvalue");
		container.setLocalizedProperty(ctx, "prop_l", "locvalue");
		lang.setAllProperties(ctx, container);
		assertEquals("testvalue", lang.getProperty(ctx, "prop"));
		assertEquals("locvalue", lang.getLocalizedProperty(ctx, "prop_l"));
		container = jaloSession.createPropertyContainer();
		container.setProperty("prop", "testvalue");
		container.setLocalizedProperty(ctx, "prop_l", "locvalue");
		lang.setAllProperties(ctx, container);
		assertEquals("testvalue", lang.getProperty(ctx, "prop"));
		assertEquals("locvalue", lang.getLocalizedProperty(ctx, "prop_l"));
	}

	@Test
	public void testItemAsPropertyValue() throws ConsistencyCheckException
	{
		final Language language = jaloSession.getC2LManager().createLanguage("itemAsPropValue");
		assertNotNull(language);

		p1.setProperty("imyself", p1);
		assertEquals(null, language.setProperty("imyself", p1));
		assertEquals(p1, language.setProperty("imyself", language));
		assertEquals(null, language.setLocalizedProperty("locname_l", language));
		assertEquals(language, language.setLocalizedProperty("locname_l", p1));
		assertEquals(p1, p1.getProperty("imyself"));
		assertTrue(p1.getAllProperties().values().contains(p1));
		assertTrue(language.getAllProperties().values().contains(language));
		assertTrue(language.getAllLocalizedProperties().values().contains(p1));

		final JaloPropertyContainer container = jaloSession.createPropertyContainer();
		container.setProperty("imyself", p1);
		container.setLocalizedProperty("locname_l", language);
		language.setAllProperties(container);
		assertTrue(language.getAllProperties().values().contains(p1));
		assertTrue(language.getAllLocalizedProperties().values().contains(language));
	}

	/**
	 * will a property be automatically removed, if its value is an item and this item is deleted?
	 */
	@Test
	public void testRemovePropertyWithItem() throws ConsistencyCheckException
	{
		final SessionContext ctx = jaloSession.getSessionContext();
		Language language = null;
		assertNotNull(language = jaloSession.getC2LManager().createLanguage("delete"));
		p1.setProperty("namexx", language);
		language.remove();
		assertNotNull(language);
		language = null;
		assertNull(p1.getProperty(ctx, "namexx"));

		// test removal of single items within a collection

		Title t1, t2, t3;
		assertNotNull(t1 = UserManager.getInstance().createTitle("t1"));
		assertNotNull(t2 = UserManager.getInstance().createTitle("t2"));
		assertNotNull(t3 = UserManager.getInstance().createTitle("t3"));

		p1.setProperty(ctx, "titlesxyz", new ArrayList(Arrays.asList(t1, t2, t3)));
		assertEquals(Arrays.asList(t1, t2, t3), p1.getProperty(ctx, "titlesxyz"));
		t2.remove();
		assertNotNull(t2);
		assertEquals(Arrays.asList(t1, t3), p1.getProperty(ctx, "titlesxyz"));
		// once again ;)
		assertEquals(Arrays.asList(t1, t3), p1.getProperty(ctx, "titlesxyz"));

		// test removal within localized properties

		Language l1, l2;

		assertNotNull(l1 = C2LManager.getInstance().createLanguage("l1test"));
		assertNotNull(l2 = C2LManager.getInstance().createLanguage("l2test"));

		ctx.setLanguage(l1);
		p1.setLocalizedProperty(ctx, "titlesloc", new ArrayList(Arrays.asList(t1, t3)));
		ctx.setLanguage(l2);
		p1.setLocalizedProperty(ctx, "titlesloc", new ArrayList(Arrays.asList(t3)));

		ctx.setLanguage(l1);
		assertEquals(Arrays.asList(t1, t3), p1.getLocalizedProperty(ctx, "titlesloc"));
		ctx.setLanguage(l2);
		assertEquals(Arrays.asList(t3), p1.getLocalizedProperty(ctx, "titlesloc"));

		Map expected = new HashMap();
		expected.put(l1, Arrays.asList(t1, t3));
		expected.put(l2, Arrays.asList(t3));
		ctx.setLanguage(null);
		assertEquals(expected, p1.getLocalizedProperty(ctx, "titlesloc"));

		t3.remove();
		assertNotNull(t3);

		ctx.setLanguage(l1);
		assertEquals(Arrays.asList(t1), p1.getLocalizedProperty(ctx, "titlesloc"));
		ctx.setLanguage(l2);
		assertEquals(Collections.EMPTY_LIST, p1.getLocalizedProperty(ctx, "titlesloc"));

		expected = new HashMap();
		expected.put(l1, Arrays.asList(t1));
		expected.put(l2, Collections.EMPTY_LIST);
		ctx.setLanguage(null);
		assertEquals(expected, p1.getLocalizedProperty(ctx, "titlesloc"));
	}

	@Test
	public void testTransaction() throws Exception
	{
		final Transaction tx = Transaction.current();
		boolean done = false;
		try
		{
			// T1
			tx.begin();
			try
			{
				p1.setProperty("prop", "value");
				assertEquals("value", p1.getProperty("prop"));
				assertEquals("value", p1.setProperty("prop", null));
				assertEquals(null, p1.getProperty("prop"));

				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}
			assertEquals(null, p1.getProperty("prop"));

			// T2
			done = false;
			tx.begin();
			try
			{
				assertEquals(null, p1.getProperty("prop"));
				assertEquals(null, p1.setProperty("prop", "value"));
				assertEquals("value", p1.getProperty("prop"));
				assertEquals("value", p1.setProperty("prop", null));
				assertEquals(null, p1.getProperty("prop"));
				assertEquals(null, p1.setProperty("prop", "value2"));
				assertEquals("value2", p1.getProperty("prop"));
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("value2", p1.getProperty("prop"));

			// T3
			done = false;
			tx.begin();
			try
			{
				assertEquals("value2", p1.getProperty("prop"));
				assertEquals("value2", p1.setProperty("prop", "valueX"));
				assertEquals("valueX", p1.getProperty("prop"));
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("valueX", p1.getProperty("prop"));

			// T4
			done = false;
			tx.begin();
			try
			{
				assertEquals("valueX", p1.getProperty("prop"));
				assertEquals("valueX", p1.setProperty("prop", "value3"));
				assertEquals("value3", p1.getProperty("prop"));
				assertEquals("value3", p1.setProperty("prop", null));
				assertEquals(null, p1.getProperty("prop"));
				assertEquals(null, p1.setProperty("prop", "value4"));
				assertEquals("value4", p1.getProperty("prop")); //tests isolation

				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("value4", p1.getProperty("prop"));

			// T5
			done = false;
			tx.begin();
			try
			{
				p1.setProperty("p2", "a");
				p1.setProperty("p2", "b");
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("b", p1.getProperty("p2"));
		}
		catch (final Throwable e)
		{
			e.printStackTrace(System.err);
			fail("error " + e);
		}
	}

	@Test
	public void testTransactionWithCoreProperty() throws Exception
	{
		boolean done = false;
		final Transaction tx = Transaction.current();
		try
		{
			tx.begin();
			try
			{
				p1.setName("value");
				p1.setName(null);
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals(null, p1.getName());

			done = false;
			//Transaction.logIt=true;
			tx.begin();
			try
			{
				p1.setName("value");
				assertEquals("value", p1.getName());
				p1.setName(null);
				assertEquals(null, p1.getName());
				p1.setName("valueX");
				p1.setName("value2");
				assertEquals("value2", p1.getName());
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			p1.getName();
			p1.getName(); //again

			assertEquals("value2", p1.getName());


			done = false;
			tx.begin();
			try
			{
				p1.setName("valueX");
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}
			assertEquals("valueX", p1.getName());

			done = false;
			tx.begin();
			try
			{
				p1.setName("value3");
				p1.setName(null);
				p1.setName("value4");
				assertEquals("value4", p1.getName());
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("value4", p1.getName());

			done = false;
			tx.begin();
			try
			{
				p1.setName("a");
				p1.setName("b");
				done = true;
			}
			finally
			{
				if (done)
				{
					tx.commit();
				}
				else
				{
					tx.rollback();
				}
			}

			assertEquals("b", p1.getName());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testValueClasses()
	{
		final String qual = "prop";
		p1.setProperty(qual, java.lang.String.class);
		assertEquals(java.lang.String.class, p1.getProperty(qual));

		p1.setProperty(qual, new java.util.Date(1234567));
		assertEquals(new java.util.Date(1234567), p1.getProperty(qual));

		/*
		 * java.sql.Timestamp timestamp = new java.sql.Timestamp( 56789987 ); p1.setProperty( qual, timestamp );
		 * assertEquals( timestamp, p1.getProperty( qual ) );
		 * 
		 * does not work, because it will be converted to a java.util.Date and is loosing the microseconds
		 */

		final java.sql.Date date = new java.sql.Date(1239876543L);
		p1.setProperty(qual, date);
		assertEquals(date.getTime(), ((java.util.Date) p1.getProperty(qual)).getTime());

		//p1.setProperty( qual, java.awt.Color.black );
		//assertEquals( java.awt.Color.black, p1.getProperty( qual ) );
		//p1.setProperty( qual, java.awt.Color.magenta );
		//assertEquals( java.awt.Color.magenta, p1.getProperty( qual ) );
	}
}
