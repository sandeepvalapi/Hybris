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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.ItemAttributeMap;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.SingletonCreator.SingletonCreateException;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Testcase for the Utilities class.
 */
@IntegrationTest
public class UtilitiesIntegrationTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(UtilitiesIntegrationTest.class.getName());


	private static final String TEST1 = "bla";
	private static final String TEST2 = "bla";


	@Test
	public void testInternString()
	{
		assertSame(TEST1, "bla");
		assertSame("bla", "bla");
		assertSame(TEST1, TEST2);
		assertSame(TEST1, "bla".substring(0, 3));
		assertNotSame(TEST1, "blam".substring(0, 3));
		assertSame(TEST1, "bl" + "a");
	}


	@Test
	public void testResourceBundles()
	{
		final Language english = getOrCreateLanguage("en");
		final Language german = getOrCreateLanguage("de");
		final Language de_de = getOrCreateLanguage("de_DE");
		final Language spanish = getOrCreateLanguage("es");
		final Language es_es = getOrCreateLanguage("es_ES");

		spanish.setFallbackLanguages(german, english);
		es_es.setFallbackLanguages(spanish, german, english);
		de_de.setFallbackLanguages(german);

		final Locale enLoc = english.getLocale();
		assertEquals("en", enLoc.getLanguage());
		assertEquals("", enLoc.getCountry());
		assertEquals("", enLoc.getDisplayVariant());

		final Locale deLoc = german.getLocale();
		assertEquals("de", deLoc.getLanguage());
		assertEquals("", deLoc.getCountry());
		assertEquals("", deLoc.getDisplayVariant());

		final Locale dedeLoc = de_de.getLocale();
		assertEquals("de", dedeLoc.getLanguage());
		assertEquals("DE", dedeLoc.getCountry());
		assertEquals("", dedeLoc.getDisplayVariant());

		final Locale esLoc = spanish.getLocale();
		assertEquals("es", esLoc.getLanguage());
		assertEquals("", esLoc.getCountry());
		assertEquals("", esLoc.getDisplayVariant());

		final String baseName = "core.unittest.locales";

		final ResourceBundle enBundle = Utilities.getResourceBundle(english, baseName);
		assertNotNull(enBundle);
		assertEquals("en", enBundle.getObject("testkey"));

		final ResourceBundle deBundle = Utilities.getResourceBundle(german, baseName);
		assertNotNull(deBundle);
		assertEquals("de", deBundle.getObject("testkey"));

		final ResourceBundle dedeBundle = Utilities.getResourceBundle(de_de, baseName);
		assertNotNull(dedeBundle);
		assertEquals("de", dedeBundle.getObject("testkey"));

		ResourceBundle esBundle = Utilities.getResourceBundle(spanish, baseName);
		assertNotNull(esBundle);
		assertEquals("es", esBundle.getObject("testkey"));

		ResourceBundle esesBundle = Utilities.getResourceBundle(es_es, baseName);
		assertNotNull(esesBundle);
		assertEquals("es", esesBundle.getObject("testkey"));

		//en-> englishonly, englisAndGermanonly
		//de-> englisAndGermanonly
		assertEquals("de", esesBundle.getObject("englisAndGermanonly"));
		assertEquals("en", esesBundle.getObject("englishonly"));


		spanish.setFallbackLanguages(english);

		es_es.setFallbackLanguages(spanish, english);

		esBundle = Utilities.getResourceBundle(spanish, baseName);
		assertNotNull(esBundle);
		assertEquals("es", esBundle.getObject("testkey"));

		esesBundle = Utilities.getResourceBundle(es_es, baseName);
		assertNotNull(esesBundle);
		assertEquals("es", esesBundle.getObject("testkey"));

		//en-> englishonly, englisAndGermanonly
		assertEquals("en", esesBundle.getObject("englisAndGermanonly"));
		assertEquals("en", esesBundle.getObject("englishonly"));
	}

	@Test
	public void testBuildRelativePath() throws Exception
	{
		//			result				from										to

		testRel("..", "/opt/hybris/platform", "/opt/hybris/");
		testRel("..", "/opt/hybris/platform/", "/opt/hybris");
		testRel("../..", "/opt/hybris/platform/test", "/opt/hybris");

		testRel("platform", "/opt/hybris/", "/opt/hybris/platform");
		testRel("platform/test", "/opt/hybris", "/opt/hybris/platform/test");

		//	 in an unix environment using ':' is critical, because this character will be used as a path delimiter, too!
		// unix uses mount points and has no windows like character representation of physical or shared drives!

		if (Utilities.isWindows())
		{
			testRel("..", "c:\\1\\2\\3", "c:\\1\\2");
			testRel("../..", "c:\\1\\2\\3", "c:\\1");
			testRel("../../..", "c:\\1\\2\\3", "c:\\");
			//testRel( "../../..", 		"c:\\1\\2\\3",							"c:" );
		}
		else if (Utilities.isUnix())
		{
			testRel("..", "\\C\\1\\2\\3", "\\C\\1\\2");
			testRel("../..", "\\C\\1\\2\\3", "\\C\\1");
			testRel("../../..", "\\C\\1\\2\\3", "\\C\\");
		}

		testRel("../3/4", "/1//2/33/", "/1/2/3/4");
		testRel("4", "/1/2/3////", "/1/2/3/4");
		testRel("../4/6", "/1/2/3/", "/1/2/4/6");
		testRel("", "/1/2/3", "/1/2/3");
		testRel("", "/1/2/3", "/1/2/3/../3");
		testRel("", "/1/2/3/../../2//3", "/1/2/3/../3");

		//	in an unix environment using ':' is critical, because this character will be used as a path delimiter, too!
		// unix uses mount points and has no windows like character representation of physical or shared drives!

		if (Utilities.isWindows())
		{

			testRelIgnoreCase("D:/data", "c:\\data", "d:\\data");
			testRelIgnoreCase("X:/data", "c:\\data", "X:\\data");
		}
		else if (Utilities.isUnix())
		{
			testRelIgnoreCase("../../D/data", "\\C\\data", "\\D\\data");
			testRelIgnoreCase("../../X/data", "\\C\\data", "\\X\\data");
		}

		testRel("", "/1/2 2/3/../../2 2//3", "/1/2 2/3/../3");

		testRel("../4/6/test.txt", "/1/2/3/", "/1/2/4/6/test.txt");


	}


	private static void testRel(final String should, final String relfrom, final String relto) throws IOException
	{
		final String was = Utilities.buildRelativePath(new File(relfrom), new File(relto));
		assertEquals("should=" + should + ", was=" + was, should, was);
	}

	private static void testRelIgnoreCase(final String should, final String relfrom, final String relto) throws IOException
	{
		final String was = Utilities.buildRelativePath(new File(relfrom), new File(relto));
		assertTrue("should=" + should + ", was=" + was, should.equalsIgnoreCase(was));
	}



	Throwable throwable = null;

	@Test
	public void testSingletonCreator() throws Throwable
	{
		final SingletonCreator.Creator<String> creator = new SingletonCreator.Creator<String>()
		{
			@Override
			protected String create() throws Exception
			{
				Thread.sleep(5);
				return "";
			}

			@Override
			protected String getID()
			{
				return "key";
			}
		};

		checkSingleton(100, 5, creator);
		checkSingleton(100, 5, Object.class);
	}

	private void checkSingleton(final int count, final int threadCount, final Object key) throws Throwable
	{
		long ges = 0;
		for (int zz = 0; zz < count; zz++)
		{
			final SingletonCreator singletonCreator = new SingletonCreator();
			final Thread[] threads = new Thread[threadCount];
			for (int i = 0; i < threadCount; i++)
			{
				threads[i] = new RegistrableThread()
				{
					@Override
					public void internalRun()
					{
						if (key instanceof Class)
						{
							singletonCreator.getSingleton((Class) key); //test with class
						}
						else
						{
							singletonCreator.getSingleton((SingletonCreator.Creator) key);
						}
					}
				};
				threads[i].setUncaughtExceptionHandler(new UncaughtExceptionHandler()
				{
					@Override
					public void uncaughtException(final Thread thread, final Throwable exception)
					{
						throwable = exception;
					}
				}

				);

			}
			final long start = System.currentTimeMillis();
			for (int i = 0; i < threadCount; i++)
			{
				threads[i].start();
			}
			for (int i = 0; i < threadCount; i++)
			{
				threads[i].join();
				if (throwable != null)
				{
					throw throwable;
				}
			}
			final long end = System.currentTimeMillis();
			ges += end - start;
		}

		LOG.info("time for creating singletons: " + (ges));
	}


	private static final SingletonCreator rectestcreator = new SingletonCreator();

	@Test
	public void testRecursiveSingletonCreation() throws Throwable
	{
		try
		{
			rectestcreator.getSingleton(TestRec.class);
			fail("should throw SingletonCreateException");
		}
		catch (final SingletonCreateException e)
		{
			//ok!
		}

		try
		{
			rectestcreator.getSingleton(TestRec.class);
			fail("should throw SingletonCreateException");
		}
		catch (final SingletonCreateException e)
		{
			//ok!
		}
	}


	public static class TestRec
	{
		public TestRec()
		{
			rectestcreator.getSingleton(TestRec.class);
		}
	}

	public static class TestRec1
	{
		public TestRec1()
		{
			rectestcreator.getSingleton(TestRec2.class);
		}
	}

	public static class TestRec2
	{
		public TestRec2()
		{
			rectestcreator.getSingleton(TestRec1.class);

		}
	}

	@Test
	public void testSingletonDestroy()
	{
		SingletonCreator singletonCreator = new SingletonCreator();

		final TestDestroyCreator cre1 = new TestDestroyCreator(1);
		final TestDestroyCreator cre2 = new TestDestroyCreator(2);
		final TestDestroyCreator cre3 = new TestDestroyCreator(3);


		createdCreators.clear();
		assertTrue(createdCreators.isEmpty());
		singletonCreator.getSingleton(cre1);
		assertTrue(createdCreators.size() == 1);
		assertTrue(createdCreators.contains(cre1));
		singletonCreator.getSingleton(cre2);
		assertTrue(createdCreators.size() == 2);
		assertTrue(createdCreators.contains(cre1));
		assertTrue(createdCreators.contains(cre2));
		singletonCreator.getSingleton(cre3);
		assertTrue(createdCreators.size() == 3);
		assertTrue(createdCreators.contains(cre1));
		assertTrue(createdCreators.contains(cre2));
		assertTrue(createdCreators.contains(cre3));

		singletonCreator.destroy();
		assertTrue(createdCreators.isEmpty());

		try
		{
			singletonCreator.getSingleton(Object.class);
			fail("exception should be raised.");
		}
		catch (final SingletonCreateException e)
		{
			// DOCTODO Document reason, why this block is empty
		}


		singletonCreator = new SingletonCreator();
		final int MAX = 100;
		final TestDestroyCreator[] testDestroyCreator = new TestDestroyCreator[MAX];
		for (int i = 0; i < MAX; i++)
		{
			testDestroyCreator[i] = new TestDestroyCreator(i);
			singletonCreator.getSingleton(testDestroyCreator[i]);
		}

		singletonCreator.destroy();


	}


	/**
	 * Testing ItemLink creation and resolving
	 */
	@Test
	public void testCreateItemLink()
	{
		final String productTypeCode = "Product";
		final String code = "LinkTest-TestProduct-Code";
		final String name = "LinkTest-TestProduct-Name";
		final String catalogID = "LinkTest-TestCatalog-ID";
		final String catalogVersionID = "LinkTest-TestCatalogVersion-ID";
		final String linkToProductPattern = "code, name, catalogVersion(version,catalog(id))";

		Config.setParameter("linkTo.Product", linkToProductPattern);

		final ComposedType catalogType = TypeManager.getInstance().getComposedType("Catalog");
		final Map attributeValues = new ItemAttributeMap();
		attributeValues.put("id", catalogID);
		Item catalog = null;
		try
		{
			catalog = catalogType.newInstance(attributeValues);
		}
		catch (final Exception e)
		{
			fail("Error while creating a test Catalog: " + Utilities.getStackTraceAsString(e));
		}
		assertNotNull(catalog);

		final ComposedType catalogVersionType = TypeManager.getInstance().getComposedType("CatalogVersion");
		attributeValues.clear();
		attributeValues.put("catalog", catalog);
		attributeValues.put("version", catalogVersionID);
		Item catalogVersion = null;
		try
		{
			catalogVersion = catalogVersionType.newInstance(attributeValues);
		}
		catch (final Exception e)
		{
			fail("Error while creating a test CatalogVersion: " + Utilities.getStackTraceAsString(e));
		}
		assertNotNull(catalogVersion);

		final Product product = jaloSession.getProductManager().createProduct(code);
		assertNotNull(product);

		product.setName(name);
		try
		{
			product.setAttribute("catalogVersion", catalogVersion);
		}
		catch (final Exception e)
		{
			fail("Error while setting CatalogVersion for test product: " + Utilities.getStackTraceAsString(e));
		}

		String createdLink = null;
		try
		{
			final String expectedLink = Utilities.ITEMLINK_TYPE_PREFIX + URLEncoder.encode(productTypeCode, "UTF-8")
					+ Utilities.ITEMLINK_ELEMENT_SEPARATOR + "code" + Utilities.ITEMLINK_VALUE_SEPARATOR
					+ URLEncoder.encode(code, "UTF-8") + Utilities.ITEMLINK_ELEMENT_SEPARATOR + "name"
					+ Utilities.ITEMLINK_VALUE_SEPARATOR + URLEncoder.encode(name, "UTF-8") + Utilities.ITEMLINK_ELEMENT_SEPARATOR
					+ "catalogVersion" + Utilities.ITEMLINK_VALUE_SEPARATOR + Utilities.ITEMLINK_ITEMATTRIBUTE_START + "version"
					+ Utilities.ITEMLINK_VALUE_SEPARATOR + URLEncoder.encode(catalogVersionID, "UTF-8")
					+ Utilities.ITEMLINK_ELEMENT_SEPARATOR + "catalog" + Utilities.ITEMLINK_VALUE_SEPARATOR
					+ Utilities.ITEMLINK_ITEMATTRIBUTE_START + "id" + Utilities.ITEMLINK_VALUE_SEPARATOR
					+ URLEncoder.encode(catalogID, "UTF-8") + Utilities.ITEMLINK_ITEMATTRIBUTE_END
					+ Utilities.ITEMLINK_ITEMATTRIBUTE_END + Utilities.ITEMLINK_ELEMENT_SEPARATOR + Utilities.ITEMLINK_LINK_ID
					+ Utilities.ITEMLINK_VALUE_SEPARATOR;

			createdLink = Utilities.createLink(JaloSession.getCurrentSession().getSessionContext(), product);

			assertEquals(expectedLink, createdLink.substring(0, expectedLink.length()));
		}
		catch (final Exception e)
		{
			fail("Error while testing ItemLink creation: " + e);
		}

		// Testig Item resolving
		if (createdLink == null)
		{
			fail("Could not test ItemLink resolving, link was not created.");
		}
		try
		{
			final Item resolvedProduct = Utilities.getItemFromLink(JaloSession.getCurrentSession().getSessionContext(), createdLink);
			assertEquals(product, resolvedProduct);
		}
		catch (final Exception e)
		{
			fail("Error while testing ItemLink resolving: " + e);
		}
	}


	static List<TestDestroyCreator> createdCreators = Collections.synchronizedList(new LinkedList());

	@SuppressWarnings("boxing")
	static class TestDestroyCreator extends SingletonCreator.Creator
	{
		private final Integer key;

		TestDestroyCreator(final int key)
		{
			super();
			this.key = Integer.valueOf(key);
		}

		@Override
		protected Object create()
		{
			createdCreators.add(this);
			return key;
		}

		@Override
		protected String getID()
		{
			return key.toString();
		}

		@Override
		protected void destroy(final Object object)
		{
			int biggest = 0;
			for (final TestDestroyCreator creator : createdCreators)
			{
				biggest = Math.max(creator.key.intValue(), biggest);
			}
			if (biggest > key.intValue())
			{
				throw new RuntimeException("destroy was not in reverse order!!");
			}
			createdCreators.remove(this);
		}
	}

	/*
	 * public void testFastLRUMap() { Object ONE = "one"; Object TWO = "two"; Object THREE = "three"; FastLRUMap p = new
	 * FastLRUMap(2); p.put( ONE, ONE ); p.put( TWO, TWO ); assertEquals( p.head().getNext().getValue(), ONE );
	 * p.get(ONE); assertEquals( p.head().getNext().getValue(), TWO ); p.put( THREE, THREE ); assertEquals( p.size(),2 );
	 * assertEquals( p.head().getNext().getValue(), ONE ); p.put( TWO, TWO ); assertEquals(
	 * p.head().getNext().getValue(),THREE ); p.get( ONE ); //NOT IN THERE
	 * assertEquals(p.head().getNext().getValue(),THREE ); p.get( THREE ); assertEquals(p.head().getNext().getValue(),TWO
	 * ); p.containsKey( THREE ); assertEquals(p.head().getNext().getValue(),TWO ); assertEquals( p.size(),2); p.remove(
	 * THREE); assertEquals( p.size(),1); }
	 */

	@Test
	public void testLozalizationGetLocalizedString()
	{
		//constants
		final Float floatValue = new Float(1234.56);
		final Object[] arguments = new Float[]
		{ floatValue };
		final String text = "value = {0}";

		//save previous locale
		final Locale prevLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();

		//English session locale
		final String englishExpectedValue = "value = 1,234.56";
		JaloSession.getCurrentSession().getSessionContext().setLocale(new Locale("en"));
		String actualValue = Localization.getLocalizedString(text, arguments);
		assertEquals(englishExpectedValue, actualValue);

		//German session locale
		final String germanExpectedValue = "value = 1.234,56";
		JaloSession.getCurrentSession().getSessionContext().setLocale(new Locale("de"));
		actualValue = Localization.getLocalizedString(text, arguments);
		assertEquals(germanExpectedValue, actualValue);

		//Swiss session locale
		final String swissExpectedValue = "value = 1'234.56";
		JaloSession.getCurrentSession().getSessionContext().setLocale(new Locale("de", "ch"));
		actualValue = Localization.getLocalizedString(text, arguments);
		assertEquals(swissExpectedValue, actualValue);

		//German or Polish VM, English session locale
		JaloSession.getCurrentSession().getSessionContext().setLocale(new Locale("en"));
		actualValue = Localization.getLocalizedString(text, arguments);
		assertFalse(germanExpectedValue.equals(actualValue));

		//roll back to previous locale
		JaloSession.getCurrentSession().getSessionContext().setLocale(prevLocale);
	}

}
