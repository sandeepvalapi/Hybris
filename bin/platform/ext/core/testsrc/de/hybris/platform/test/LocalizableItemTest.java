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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
 * Tests the extension behavior of LocalizableItems.
 * 
 * 
 */
@IntegrationTest
public class LocalizableItemTest extends HybrisJUnit4Test
{
	static final int LANGUAGE_COUNT = 5;

	LocalizableItem localizableItem;
	Language[] languages;

	@Before
	public void setUp() throws Exception
	{
		localizableItem = createLocalizableItem();
		languages = new Language[LANGUAGE_COUNT];
		for (int i = 0; i < 5; i++)
		{
			languages[i] = jaloSession.getC2LManager().createLanguage("xitemlang" + i);
		}
	}

	@After
	public void tearDown() throws Exception
	{

		localizableItem.remove();

		for (int i = 0; languages != null && i < languages.length; i++)
		{
			languages[i].remove();
		}
	}

	protected LocalizableItem createLocalizableItem()
	{
		return jaloSession.getMediaManager().createMedia("extensibleitemtest");
	}

	@Test
	public void testLanguageFallback() throws ConsistencyCheckException
	{
		final SessionContext myCtx = jaloSession.createSessionContext();
		myCtx.setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);

		Language l1, l2, l3;

		assertNotNull(l1 = C2LManager.getInstance().createLanguage("fbTest1"));
		assertNotNull(l2 = C2LManager.getInstance().createLanguage("fbTest2"));
		assertNotNull(l3 = C2LManager.getInstance().createLanguage("fbTest3"));


		myCtx.setLanguage(l3);
		localizableItem.setLocalizedProperty(myCtx, "fbTestProp", "value");

		// l1 ->{} l2->{} l3->{}
		myCtx.setLanguage(l1);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		// l1 ->{} l2->{l3} l3->{}
		l2.setFallbackLanguages(Collections.singletonList(l3));
		myCtx.setLanguage(l1);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		myCtx.setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, null);

		myCtx.setLanguage(l1);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		myCtx.setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);

		// l1 ->{l2,l3} l2->{} l3->{}
		l2.setFallbackLanguages((List) null);
		l1.setFallbackLanguages(l2, l3);
		myCtx.setLanguage(l1);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertNull(localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		myCtx.setLanguage(l2);
		localizableItem.setLocalizedProperty(myCtx, "fbTestProp", "value2");

		myCtx.setLanguage(l1);
		assertEquals("value2", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertEquals("value2", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		myCtx.setLanguage(l1);
		localizableItem.setLocalizedProperty(myCtx, "fbTestProp", "value3");

		myCtx.setLanguage(l1);
		assertEquals("value3", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l2);
		assertEquals("value2", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));
		myCtx.setLanguage(l3);
		assertEquals("value", localizableItem.getLocalizedProperty(myCtx, "fbTestProp"));

		try
		{
			l1.setFallbackLanguages(Collections.singletonList(l1));
			fail("JaloInvalidParameterException exepected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
	}

	@Test
	public void testProperties()
	{
		final int PROPERTY_COUNT = 20;
		final String valuePrefix = String.valueOf(System.currentTimeMillis());
		final String[] propertyNames = new String[PROPERTY_COUNT];
		final String[][] propertyValues = new String[PROPERTY_COUNT][LANGUAGE_COUNT + 1];
		final SessionContext context = jaloSession.createSessionContext();

		for (int i = 0; i < PROPERTY_COUNT; i++)
		{
			propertyNames[i] = "property" + i;
			if (i % 6 != 0)
			{
				propertyValues[i][0] = valuePrefix + "value" + i;
			}
			if (i % 3 == 0)
			{
				for (int j = 0; j < LANGUAGE_COUNT; j++)
				{
					propertyValues[i][j + 1] = valuePrefix + "value" + i + "(" + j + ")";
				}
			}
		}

		for (int i = 0; i < PROPERTY_COUNT; i++)
		{
			if (propertyValues[i][0] != null)
			{
				localizableItem.setProperty(propertyNames[i], propertyValues[i][0]);
			}
			for (int j = 0; j < LANGUAGE_COUNT; j++)
			{
				if (propertyValues[i][j + 1] != null)
				{
					context.setLanguage(languages[j]);
					localizableItem.setLocalizedProperty(context, propertyNames[i], propertyValues[i][j + 1]);
				}
			}
		}

		for (int i = 0; i < PROPERTY_COUNT; i++)
		{
			assertEquals(propertyValues[i][0], localizableItem.getProperty(propertyNames[i]));
			for (int j = 0; j < LANGUAGE_COUNT; j++)
			{
				context.setLanguage(languages[j]);
				assertEquals(propertyValues[i][j + 1], localizableItem.getLocalizedProperty(context, propertyNames[i]));
			}
		}
	}

	@Test
	public void testRemoveProperty() throws java.io.IOException
	{
		final SessionContext ctx = jaloSession.createSessionContext();

		// PLA-5129,  default values were already be added 
		final Map defaultProperties = localizableItem.getAllProperties();

		assertNull(localizableItem.setProperty("not_name", "value"));
		assertEquals("value", localizableItem.removeProperty("not_name"));

		assertEquals(defaultProperties, localizableItem.getAllProperties());

		ctx.setLanguage(languages[0]);
		assertNull(localizableItem.setLocalizedProperty(ctx, "not_name", "valueLoc0"));
		assertEquals("valueLoc0", localizableItem.getLocalizedProperty(ctx, "not_name"));
		ctx.setLanguage(languages[1]);
		assertNull(localizableItem.setLocalizedProperty(ctx, "not_name", "valueLoc1"));
		assertEquals("valueLoc1", localizableItem.getLocalizedProperty(ctx, "not_name"));

		ctx.setLanguage(languages[0]);
		Map properties = localizableItem.getAllLocalizedProperties(ctx);
		assertEquals(1, properties.size());
		Map.Entry entry = (Map.Entry) properties.entrySet().iterator().next();
		assertEquals("not_name", entry.getKey());
		assertEquals("valueLoc0", entry.getValue());

		ctx.setLanguage(languages[1]);
		properties = localizableItem.getAllLocalizedProperties(ctx);
		assertEquals(1, properties.size());
		entry = (Map.Entry) properties.entrySet().iterator().next();
		assertEquals("not_name", entry.getKey());
		assertEquals("valueLoc1", entry.getValue());

		ctx.setLanguage(languages[0]);
		assertEquals("valueLoc0", localizableItem.removeLocalizedProperty(ctx, "not_name"));
		ctx.setLanguage(languages[1]);
		assertEquals("valueLoc1", localizableItem.removeLocalizedProperty(ctx, "not_name"));
	}

	@Test
	public void testGetAllProperties()
	{
		final SessionContext ctx = jaloSession.createSessionContext();

		// PLA-5129,  default values were already be added 
		/*
		 * assertEquals( Collections.EMPTY_MAP, localizableItem.getAllProperties() );
		 * 
		 * for (int i=0; i<languages.length; i++) { ctx.setLanguage(languages[i]); assertEquals( Collections.EMPTY_MAP,
		 * localizableItem.getAllProperties(ctx) ); }
		 */

		assertEquals(null, localizableItem.setProperty("name0", "value0"));
		assertEquals(null, localizableItem.setProperty("name1", "value1"));
		assertEquals(null, localizableItem.setProperty("name2", "value2"));
		for (int i = 0; i < languages.length; i++)
		{
			ctx.setLanguage(languages[i]);
			assertEquals(null, localizableItem.setLocalizedProperty(ctx, "name0", "value0" + i));
			assertEquals(null, localizableItem.setLocalizedProperty(ctx, "name1", "value1" + i));
			assertEquals(null, localizableItem.setLocalizedProperty(ctx, "name2", "value2" + i));
			assertEquals("value0" + i, localizableItem.removeLocalizedProperty(ctx, "name0"));
			assertEquals("value2" + i, localizableItem.setLocalizedProperty(ctx, "name2", null));
		}

		//	 PLA-5129
		final int propertiesCount = localizableItem.getAllProperties().size();

		assertEquals("value0", localizableItem.removeProperty("name0"));
		assertEquals("value2", localizableItem.setProperty("name2", null)); // := localizableItem.removeProperty("name2")
		// -> propertiesCount - 2

		Map properties = localizableItem.getAllProperties();

		assertEquals(propertiesCount - 2, properties.size());
		assertEquals("value1", properties.get("name1"));
		for (int i = 0; i < languages.length; i++)
		{
			ctx.setLanguage(languages[i]);
			properties = localizableItem.getAllLocalizedProperties(ctx);
			assertEquals(1, properties.size());
			assertEquals("value1" + i, properties.get("name1"));
		}
	}

	// see CORE-4255 and CORE-4256
	@Test
	public void testRestrictionsBug() throws ConsistencyCheckException
	{
		final User prev = jaloSession.getUser();
		final Language prevLang = jaloSession.getSessionContext().getLanguage();
		try
		{
			Language l1, l2, l3;

			assertNotNull(l1 = C2LManager.getInstance().createLanguage("r_lang_1"));
			assertNotNull(l2 = C2LManager.getInstance().createLanguage("r_lang_2"));
			assertNotNull(l3 = C2LManager.getInstance().createLanguage("r_lang_3"));

			User u1, u2;
			assertNotNull(u1 = UserManager.getInstance().createEmployee("blah"));
			assertNotNull(u2 = UserManager.getInstance().createEmployee("blub"));
			// hide l2 from user u2
			assertNotNull(TypeManager.getInstance().createRestriction("langRestr", u2,
					TypeManager.getInstance().getComposedType(Language.class),
					"{item." + Language.ISOCODE + "} IN ( '" + l1.getIsoCode() + "','" + l3.getIsoCode() + "')"));

			jaloSession.setUser(u1);
			jaloSession.getSessionContext().setLanguage(l1);

			Map<Language, String> names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertNull(names.get(l1));
			assertNull(names.get(l2));
			assertNull(names.get(l3));

			jaloSession.getSessionContext().setLanguage(l1);
			l1.setLocalizedProperty(Language.NAME, "name_l1");
			assertEquals("name_l1", l1.getLocalizedProperty(Language.NAME));

			jaloSession.getSessionContext().setLanguage(l2);
			l1.setLocalizedProperty(Language.NAME, "name_l2");
			assertEquals("name_l2", l1.getLocalizedProperty(Language.NAME));

			jaloSession.getSessionContext().setLanguage(l3);
			l1.setLocalizedProperty(Language.NAME, "name_l3");
			assertEquals("name_l3", l1.getLocalizedProperty(Language.NAME));

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals("name_l1", names.get(l1));
			assertEquals("name_l2", names.get(l2));
			assertEquals("name_l3", names.get(l3));

			jaloSession.setUser(u2);

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals(2, names.size());
			assertEquals("name_l1", names.get(l1));
			assertNull(names.get(l2));
			assertEquals("name_l3", names.get(l3));

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("name_l1", l1.getLocalizedProperty(Language.NAME));

			// TODO should be prevent direct access ???
			//			jaloSession.getSessionContext().setLanguage( l2 );
			//			assertNull( l1.getLocalizedProperty( Language.NAME ));

			jaloSession.getSessionContext().setLanguage(l3);
			assertEquals("name_l3", l1.getLocalizedProperty(Language.NAME));

			names.clear();
			names.put(l1, "new_name_l1");
			names.put(l3, "new_name_l3");

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("name_l1", l1.getLocalizedProperty(Language.NAME));

			l1.setLocalizedProperty(null, Language.NAME, names);

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("new_name_l1", l1.getLocalizedProperty(Language.NAME));


			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals(2, names.size());
			assertEquals("new_name_l1", names.get(l1));
			assertNull(names.get(l2));
			assertEquals("new_name_l3", names.get(l3));

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("new_name_l1", l1.getLocalizedProperty(Language.NAME));

			// TODO should be prevent direct access ???
			//			jaloSession.getSessionContext().setLanguage( l2 );
			//			assertNull( l1.getLocalizedProperty( Language.NAME ));

			jaloSession.getSessionContext().setLanguage(l3);
			assertEquals("new_name_l3", l1.getLocalizedProperty(Language.NAME));

			jaloSession.setUser(u1);

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals("new_name_l1", names.get(l1));
			assertEquals("name_l2", names.get(l2));
			assertEquals("new_name_l3", names.get(l3));

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("new_name_l1", l1.getLocalizedProperty(Language.NAME));

			jaloSession.getSessionContext().setLanguage(l2);
			assertEquals("name_l2", l1.getLocalizedProperty(Language.NAME));

			jaloSession.getSessionContext().setLanguage(l3);
			assertEquals("new_name_l3", l1.getLocalizedProperty(Language.NAME));

			// remove name[l3]
			l1.setLocalizedProperty(Language.NAME, null);

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals("new_name_l1", names.get(l1));
			assertEquals("name_l2", names.get(l2));
			assertNull(names.get(l3));

			jaloSession.setUser(u2);

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertEquals(1, names.size());
			assertEquals("new_name_l1", names.get(l1));
			assertNull(names.get(l2));
			assertNull(names.get(l3));

			jaloSession.getSessionContext().setLanguage(l1);
			assertEquals("new_name_l1", l1.getLocalizedProperty(Language.NAME));

			// TODO should be prevent direct access ???
			//			jaloSession.getSessionContext().setLanguage( l2 );
			//			assertNull( l1.getLocalizedProperty( Language.NAME ));

			jaloSession.getSessionContext().setLanguage(l3);
			assertNull(l1.getLocalizedProperty(Language.NAME));

			// remove all (visible) names
			l1.removeLocalizedProperty(null, Language.NAME);

			assertEquals(Collections.EMPTY_MAP, l1.getLocalizedProperty(null, Language.NAME));

			jaloSession.setUser(u1);

			names = (Map<Language, String>) l1.getLocalizedProperty(null, Language.NAME);
			assertNull(names.get(l1));
			assertEquals("name_l2", names.get(l2));
			assertNull(names.get(l3));
		}
		finally
		{
			jaloSession.setUser(prev);
			jaloSession.getSessionContext().setLanguage(prevLang);
		}
	}
}
