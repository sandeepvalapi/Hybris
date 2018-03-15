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
import static de.hybris.platform.testframework.Assert.assertCollectionElements;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.flexiblesearch.TypedNull;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.link.LinkManagerEJB;
import de.hybris.platform.persistence.link.LinkRemote;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.SQLSearchResult;
import de.hybris.platform.util.Utilities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


@IntegrationTest
public class FlexibleSearchTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(FlexibleSearchTest.class.getName());

	private TypeManager tm;

	private Language de = null;
	private Language en = null;
	private Language unknownLang = null;
	private Country ctr;
	private Region reg;

	private ComposedType langType, langSubType, langSubType2, langSubSubType;

	private String LANG_TYPE;
	private List LANG_RES_CLASSES;

	private FlexibleSearch fs;
	private SessionContext ctx;
	private SearchResult res;
	private List list;

	private static Logger fsLogger = null; //NOPMD
	private static Level levelBefore = null;

	private static final String PK_ATTR = Item.PK;

	@BeforeClass
	public static void setUpGlobal()
	{
		fsLogger = Logger.getLogger(FlexibleSearch.class.getName());
		levelBefore = fsLogger.getLevel();
		fsLogger.setLevel(Level.OFF);
	}

	@AfterClass
	public static void tearDownGlobal()
	{
		try
		{
			if (fsLogger != null && levelBefore != null)
			{
				fsLogger.setLevel(levelBefore);
			}
		}
		finally
		{
			fsLogger = null;
			levelBefore = null;
		}
	}

	@Before
	public void setUp() throws Exception
	{
		// switch off FlexibleSearch logging since some test are forcing errors

		tm = jaloSession.getTypeManager();


		de = getOrCreateLanguage("de");
		de.setActive(false);
		en = getOrCreateLanguage("en");
		en.setActive(true);
		assertNotNull(unknownLang = jaloSession.getC2LManager().createLanguage("unknownLang"));

		final SessionContext deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(de);
		final SessionContext enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(en);
		final SessionContext unknownLangCtx = jaloSession.createSessionContext();
		unknownLangCtx.setLanguage(unknownLang);

		de.setName(deCtx, "Deutsch");
		de.setName(enCtx, "German");
		de.setName(unknownLangCtx, "xxx");
		de.setProperty("prop", "only de");

		en.setName(deCtx, "Englisch");
		en.setName(enCtx, "English");

		// get lang type
		langType = tm.getComposedType(Language.class);
		LANG_TYPE = langType.getCode();
		LANG_RES_CLASSES = Collections.singletonList(Language.class);

		// create subtype to test subtype inclusion
		assertNotNull(langSubType = tm.createComposedType(langType, "LanguageSubType"));
		assertNotNull(langSubType2 = tm.createComposedType(langType, "LanguageSubType2"));
		assertNotNull(langSubSubType = tm.createComposedType(langSubType, "LanguageSubSubType"));

		assertNotNull(ctr = jaloSession.getC2LManager().createCountry("ctr"));
		assertNotNull(reg = ctr.addNewRegion("reg"));

		fs = jaloSession.getFlexibleSearch();
		ctx = jaloSession.createSessionContext();
	}

	@Test
	public void testDisableExecution() throws Exception
	{

		//first, check if execution is enabled by default
		final SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
		ctx.setAttribute(FlexibleSearch.DISABLE_EXECUTION, Boolean.TRUE);

		final SearchResult rs = getFlexibleSearch().search(ctx, "SELECT {PK} FROM {Language} WHERE {isocode} LIKE ?iso",
				Collections.singletonMap("iso", "%"), Item.class);
		try
		{
			rs.getResult();
			Assert.fail("Should not able to get result list from search result in DISABLE_EXECUTION mode ");
		}
		catch (final IllegalStateException ise)
		{
			//fine here
		}
		assertTrue(((SQLSearchResult) rs).getSQLForPreparedStatement().startsWith("SELECT"));
		assertTrue(((SQLSearchResult) rs).getValuesForPreparedStatement().size() > 0);
	}




	@Test
	public void testViewTypeErrors() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final ComposedType vtMeta = tm.getComposedType(ViewType.class);
		final Map attributes = new HashMap();
		attributes.put(ViewType.CODE, "TestVT");
		attributes.put(ViewType.QUERY, "SELECT {PK} FROM {Item}");
		ViewType vt;
		assertNotNull(vt = (ViewType) vtMeta.newInstance(attributes));
		assertTrue(new HashSet(tm.getComposedType(Item.class).getSubTypes()).contains(vt));
		// test direct error
		try
		{
			fs.search("SELECT {PK} FROM {TestVT}", Collections.EMPTY_MAP, Collections.singletonList(Item.class), true, true, 0, -1);
			fail("didnt get expected error");
		}
		catch (final FlexibleSearchException fe)
		{
			// OK
		}
		catch (final Exception e)
		{
			fail("got wrong error " + e.getMessage() + " class = " + e.getClass());
		}
		// test if viewtype subtypes are savely ignored
		log.info("Got "
				+ ((Integer) fs
						.search("SELECT COUNT({PK}) FROM {Item}", Collections.EMPTY_MAP, Collections.singletonList(Integer.class),
								true, true, 0, -1).getResult().iterator().next()).intValue() + " items in the system");
	}

	@Test
	public void testNullReturnValues()
	{
		// test normal IN search
		final String LEFT_JOIN = " LEFT JOIN ";

		List items = fs.search(
				ctx,
				"SELECT {l1:" + PK_ATTR + "}, {l2:" + PK_ATTR + "} FROM {" + LANG_TYPE + " AS l1 " + LEFT_JOIN + LANG_TYPE
						+ " AS l2 ON {l1:" + C2LItem.ISOCODE + "}='blup' } " + "WHERE {l1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' )"
						+ "ORDER BY {l1:" + C2LItem.ISOCODE + "} ASC", (Map) null, Arrays.asList(new Object[]
				{ Language.class, Language.class }), true, // yes, fail on unknown fields
				true, // yes, need total
				0, -1 // range
				).getResult();

		boolean oracle9 = false;
		if (Config.isOracleUsed())
		{

			Connection conn = null;
			try
			{
				conn = Registry.getCurrentTenant().getDataSource().getConnection();
				if (conn.getMetaData().getDatabaseMajorVersion() == 9)
				{
					oracle9 = true;
				}
			}
			catch (final SQLException e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				Utilities.tryToCloseJDBC(conn, null, null);
			}
		}
		if (oracle9)
		{
			log.info("Oracle9 used, we are checking for existing bug during testNullReturnValues, see CORE-4294.");
			Assert.assertTrue(items.isEmpty());
		}
		else
		{
			assertCollection(Arrays.asList(new Object[]
			{ Arrays.asList(new Object[]
			{ de, null }), Arrays.asList(new Object[]
			{ en, null }) }), items);
		}
		items = fs.search(
				ctx,
				"SELECT {l2:" + PK_ATTR + "} FROM {" + LANG_TYPE + " AS l1 " + LEFT_JOIN + LANG_TYPE + " AS l2 ON {l1:"
						+ C2LItem.ISOCODE + "}='blup' } " + "WHERE {l1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' )", (Map) null,
				LANG_RES_CLASSES, true, // yes, fail on unknown fields
				true, // yes, need total
				0, -1 // range
				).getResult();

		if (oracle9)
		{
			log.info("Oracle9 used, we are checking for existing bug during testNullReturnValues, see CORE-4294.");
			Assert.assertTrue(items.isEmpty());
		}
		else
		{
			assertEquals(2, items.size());
			assertNull(items.get(0));
			assertNull(items.get(1));
		}

	}

	@Test
	public void testCollectionValues() throws ConsistencyCheckException
	{
		// test normal IN search
		List items = fs.search(ctx,
				"SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ( 'de', 'en' )", (Map) null,
				LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				).getResult();
		assertCollection(Arrays.asList(new Language[]
		{ de, en }), items);
		// test collection search
		final Map values = new HashMap();
		values.put("coll", Arrays.asList(new String[]
		{ "en", "de" }));
		items = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ( ?coll )",
				values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				).getResult();
		assertCollection(Arrays.asList(new Language[]
		{ de, en }), items);

		try
		{
			// test empty collection search
			values.put("coll", Collections.EMPTY_LIST);
			items = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ( ?coll )",
					values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					).getResult();

			fail("using ..IN (?coll) with empty collection should throw exception!");
		}
		catch (final FlexibleSearchException e)
		{
			//good!
		}

		//test TypedNull
		values.put("coll", new TypedNull(String.class));
		items = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ( ?coll )",
				values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				).getResult();
		assertCollection(Collections.EMPTY_LIST, items);

		try
		{
			//test TypedNull	//invalid class in TypedNull should throw exception
			values.put("coll", new TypedNull(Manager.class));
			items = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ( ?coll )",
					values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					).getResult();
			assertCollection(Collections.EMPTY_LIST, items);
		}
		catch (final FlexibleSearchException e)
		{
			//good!
		}

		/*
		 * test Principal.allGroups collection attribute
		 */
		UserGroup ug1, ug2;
		assertNotNull(ug1 = jaloSession.getUserManager().createUserGroup("flexUG1"));
		assertNotNull(ug2 = jaloSession.getUserManager().createUserGroup("flexUG2"));
		final User u = jaloSession.getUser();
		u.addToGroup(ug1);
		u.addToGroup(ug2);
		items = fs.search(
				ctx,
				"SELECT {" + PK_ATTR + "} FROM {" + jaloSession.getTypeManager().getComposedType(UserGroup.class).getCode() + "} "
						+ "WHERE {" + PK_ATTR + "} IN ( ?session.user.allGroups )", values, Collections.singletonList(UserGroup.class),
				true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				).getResult();
		assertCollection(u.getAllGroups(), items);
	}

	@Test
	public void testExternalDeploymentSearch() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final ComposedType genericItemType = jaloSession.getTypeManager().getComposedType(GenericItem.class);
		assertNotNull(genericItemType);
		final ComposedType genericTestItemType = jaloSession.getTypeManager().getComposedType("GenericTestItem");
		assertNotNull(genericTestItemType);
		assertFalse(genericItemType.getTable().equalsIgnoreCase(genericTestItemType.getTable()));

		final GenericItem genItem;
		assertNotNull(genItem = (GenericItem) genericItemType.newInstance(Collections.EMPTY_MAP));
		final GenericItem genTestItem;
		assertNotNull(genTestItem = (GenericItem) genericTestItemType.newInstance(Collections.EMPTY_MAP));

		final Map values = new HashMap();
		values.put("item", genTestItem);
		values.put("item2", genItem);
		List items = jaloSession
				.getFlexibleSearch()
				.search(
						"SELECT {" + Item.PK + "} FROM {" + genericTestItemType.getCode() + "} WHERE {" + Item.PK
								+ "} IN (  ?item, ?item2 )", values, Collections.singletonList(GenericItem.class), true, true, 0, -1)
				.getResult();
		assertCollection(Collections.singletonList(genTestItem), items);

		items = jaloSession
				.getFlexibleSearch()
				.search(
						"SELECT {" + Item.PK + "} FROM {" + genericItemType.getCode() + "} WHERE {" + Item.PK
								+ "} IN (  ?item, ?item2 )", values, Collections.singletonList(GenericItem.class), true, true, 0, -1)
				.getResult();
		assertCollection(Arrays.asList(new Object[]
		{ genItem, genTestItem }), items);

		items = jaloSession
				.getFlexibleSearch()
				.search(
						"SELECT {" + Item.PK + "} FROM {" + genericItemType.getCode() + "!} WHERE {" + Item.PK
								+ "} IN (  ?item, ?item2 )", values, Collections.singletonList(GenericItem.class), true, true, 0, -1)
				.getResult();
		assertCollection(Collections.singletonList(genItem), items);
	}

	@Test
	public void testGroupBy()
	{
		final String REGION = tm.getComposedType(Region.class).getCode();
		List rows = jaloSession
				.getFlexibleSearch()
				.search(
						"SELECT {" + Region.COUNTRY + "}, COUNT(*) FROM {" + REGION + "} WHERE 1 = 1 GROUP BY {" + Region.COUNTRY + "}",
						Collections.EMPTY_MAP, Arrays.asList(new Object[]
						{ Country.class, Integer.class }), true, true, 0, -1).getResult();
		assertCollection(Collections.singletonList(Arrays.asList(new Object[]
		{ ctr, Integer.valueOf(1) })), rows);
		rows = jaloSession
				.getFlexibleSearch()
				.search(
						"SELECT {" + Region.COUNTRY + "}, COUNT(*) FROM {" + REGION + "} WHERE 1 = 1 GROUP BY {" + Region.COUNTRY
								+ "} ORDER BY {" + Region.COUNTRY + "}", Collections.EMPTY_MAP, Arrays.asList(new Object[]
						{ Country.class, Integer.class }), true, true, 0, -1).getResult();
		assertCollection(Collections.singletonList(Arrays.asList(new Object[]
		{ ctr, Integer.valueOf(1) })), rows);
		rows = jaloSession
				.getFlexibleSearch()
				.search("SELECT {" + Region.COUNTRY + "}, COUNT(*) FROM {" + REGION + "} GROUP BY {" + Region.COUNTRY + "}",
						Collections.EMPTY_MAP, Arrays.asList(new Object[]
						{ Country.class, Integer.class }), true, true, 0, -1).getResult();
		assertCollection(Collections.singletonList(Arrays.asList(new Object[]
		{ ctr, Integer.valueOf(1) })), rows);
		/*
		 * test unions
		 */

		final String C2LITEM = tm.getComposedType(C2LItem.class).getCode();
		rows = jaloSession
				.getFlexibleSearch()
				.search("SELECT {" + Item.TYPE + "}, COUNT(*) FROM {" + C2LITEM + "} GROUP BY {" + Item.TYPE + "}",
						Collections.EMPTY_MAP, Arrays.asList(new Object[]
						{ C2LItem.class, Integer.class }), true, true, 0, -1).getResult();

		rows = jaloSession
				.getFlexibleSearch()
				.search("SELECT COUNT(*) , MIN({PK}) , max( {PK} ) FROM {" + C2LITEM + "}", Collections.EMPTY_MAP,
						Arrays.asList(new Object[]
						{ Integer.class, de.hybris.platform.core.PK.class, de.hybris.platform.core.PK.class }), true, true, 0, -1)
				.getResult();
		assertEquals(1, rows.size());
		assertEquals(TypeManager.getInstance().getComposedType(C2LItem.class).getAllInstances().size(),
				((Integer) ((List) rows.get(0)).get(0)).intValue());
	}

	@Test
	public void testRestrictedSearch() throws ConsistencyCheckException
	{
		ctx.setLanguage(en);
		/*
		 * test without restrictions
		 */
		assertEquals(langType, de.getComposedType());
		assertEquals(langType, en.getComposedType());
		Language subLang1, subLang2, subSubLang;
		assertNotNull(subLang1 = (Language) jaloSession.getC2LManager().createLanguage("sl1").setComposedType(langSubType));
		assertEquals(langSubType, subLang1.getComposedType());
		assertNotNull(subLang2 = (Language) jaloSession.getC2LManager().createLanguage("sl2").setComposedType(langSubType));
		assertEquals(langSubType, subLang2.getComposedType());
		assertNotNull(subSubLang = (Language) jaloSession.getC2LManager().createLanguage("ssl").setComposedType(langSubSubType));
		assertEquals(langSubSubType, subSubLang.getComposedType());

		final UserGroup grp = jaloSession.getUserManager().createUserGroup("grp");
		assertNotNull(grp);
		grp.addMember(ctx.getUser());
		assertTrue(grp.getMembers().contains(ctx.getUser()));

		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Arrays.asList(new Language[]
		{ de, en, subLang1, subLang2, subSubLang }), res.getResult());
		/*
		 * top level restriction
		 */
		assertNotNull(tm.createRestriction("testrestr1", ctx.getUser(), langType, "{" + C2LItem.ISOCODE + "} <> 'de'"));
		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Arrays.asList(new Language[]
		{ en, subLang1, subLang2, subSubLang }), res.getResult());
		/*
		 * subtype restriction
		 */
		// langsubtype : iso != 'en' -> should not match since langsubtype has only 'sl1' and 'sl2', and 'ssl' as subtype instance
		assertNotNull(tm.createRestriction("testrestr2", ctx.getUser(), langSubType, "{" + C2LItem.ISOCODE + "} <> 'en'"));
		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Arrays.asList(new Language[]
		{ en, subLang1, subLang2, subSubLang }), res.getResult());
		// langsubtype: NOT ( iso LIKE 'ss%' ) -> should only limit langsubsubtype instance 'ssl'
		assertNotNull(tm.createRestriction("testrestr3", grp, langSubType, "NOT ( {" + C2LItem.ISOCODE + "} LIKE 'ss%' )"));
		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Arrays.asList(new Language[]
		{ en, subLang1, subLang2 }), res.getResult());
		// langsubtype2: iso IS NULL -> should not catch since there are not langsubtype2 instances
		assertNotNull(tm.createRestriction("testrestr4", ctx.getUser(), langSubType2, "{" + C2LItem.ISOCODE + "} IS NULL"));
		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Arrays.asList(new Language[]
		{ en, subLang1, subLang2 }), res.getResult());
		// langtype: iso IS NULL -> should filter all now
		assertNotNull(tm.createRestriction("testrestr5", grp, langType, "{" + C2LItem.ISOCODE + "} IS NULL"));
		res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
				+ "} IN ( 'de', 'en', 'sl1', 'sl2', 'ssl' )", (Map) null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollection(Collections.EMPTY_LIST, res.getResult());
	}

	@Test
	public void testValuePaths()
	{
		try
		{
			ctx.setLanguage(null);
			final Map values = new HashMap();
			values.put("lll", de);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + Item.PK + "} = ?lll." + Item.PK,
					values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Collections.singletonList(de), res.getResult());
			// test session attribute
			final User u = jaloSession.getUser();
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + tm.getComposedType(User.class).getCode() + "} WHERE {"
					+ User.UID + "} = ?session." + SessionContext.USER + "." + User.UID, null, Collections.singletonList(User.class),
					true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Collections.singletonList(u), res.getResult());
			// test multiple item traversal
			values.clear();
			values.put("region", reg);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + tm.getComposedType(Country.class).getCode() + "}" + " WHERE {"
					+ Country.ISOCODE + "} = ?region." + Region.COUNTRY + "." + Country.ISOCODE, values,
					Collections.singletonList(Country.class), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Collections.singletonList(ctr), res.getResult());
			// test failure
			try
			{
				res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + tm.getComposedType(Country.class).getCode() + "}"
						+ " WHERE {" + Country.ISOCODE + "} = ?region." + Region.COUNTRY + "." + Country.ISOCODE, null,
						Collections.singletonList(Country.class), true, // yes, fail on unknown fields
						false, // yes, need total
						0, -1 // range
						);
				fail("FlexibleSearchException expected");
			}
			catch (final FlexibleSearchException e)
			{
				// fine here
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testSingleValuesFromSessionContext()
	{
		try
		{
			ctx.setLanguage(en);
			ctx.setAttribute("testprop", "de");
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
					+ "} = ?session.testprop", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Collections.singletonList(de), res.getResult());
			ctx.setAttribute("testprop", "en");
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
					+ "} = ?session.testprop", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Collections.singletonList(en), res.getResult());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}


	@Test
	public void testSubtypeRestriction()
	{
		try
		{
			ctx.setLanguage(en);
			// --- test normal search ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
					+ "} IN ( 'de', 'en' )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ de, en }), res.getResult());
			// --- test strict typed search ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "!} WHERE {" + C2LItem.ISOCODE
					+ "} IN ( 'de', 'en' )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ de, en }), res.getResult());
			// --- test subtype item searching -------
			Language subTypeLang;
			subTypeLang = jaloSession.getC2LManager().createLanguage("subtypelang");
			subTypeLang = (Language) subTypeLang.setComposedType(langSubType);
			assertNotNull(subTypeLang);
			// --- test normal search - should find subtype lang too ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
					+ "} IN ( 'de', 'en', 'subtypelang' )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ de, en, subTypeLang }), res.getResult());
			// --- test strict typed search - shouldnt find subtype lang ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "!} WHERE {" + C2LItem.ISOCODE
					+ "} IN ( 'de', 'en', 'subtypelang' )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ de, en }), res.getResult());
			// --- test search upon subtype only ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + langSubType.getCode() + "}", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ subTypeLang }), res.getResult());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testAliasSyntax()
	{
		try
		{
			ctx.setLanguage(en);
			// --- test alias search ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + " AS huhu} WHERE {huhu:" + C2LItem.ISOCODE
					+ "} IN ( 'de', 'en' )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertCollection(Arrays.asList(new Language[]
			{ de, en }), res.getResult());
			// --- test alias *and* index search ----------
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + " AS huhu} WHERE {huhu:" + C2LItem.ISOCODE
					+ "} = 'de' OR {1:" + C2LItem.ISOCODE + "} = 'en'", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertCollection(Arrays.asList(new Language[]
			{ de, en }), res.getResult());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testValueMap()
	{
		try
		{
			ctx.setLanguage(en);
			// ----------------------------------------------------------
			// --- test correct value key
			// ----------------------------------------------------------
			final Map values = new HashMap();
			values.put("name", "German");
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "} = ?name ORDER BY {"
					+ C2LItem.NAME + "}", values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertCollection(Collections.singletonList(de), res.getResult());
			// ----------------------------------------------------------
			// --- test correct value keys
			// ----------------------------------------------------------
			values.clear();
			values.put("name", "German");
			values.put("iso", "de");
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "} = ?name AND {"
					+ C2LItem.ISOCODE + "} = ?iso ORDER BY {" + C2LItem.NAME + "}", values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertNotNull(res);
			assertCollection(Collections.singletonList(de), res.getResult());

			// ----------------------------------------------------------
			// --- test missing value
			// ----------------------------------------------------------
			values.clear();
			try
			{
				res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME
						+ "} = ?name ORDER BY {" + C2LItem.NAME + "}", values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
						false, // yes, need total
						0, -1);
				fail("expected FlexibleSearchException because of missing value 'name'");
			}
			catch (final FlexibleSearchException e)
			{
				// fine here
				//log.info("got expected exception "+e.getMessage(); );
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testAbstractTypes()
	{
		try
		{
			ctx.setLanguage(null);
			final ComposedType aoType = tm.getComposedType(AbstractOrder.class);
			final Cart cart;
			final Order order;
			assertNotNull(cart = jaloSession.getOrderManager()
					.createCart("blah", ctx.getUser(), ctx.getCurrency(), new Date(), true));
			assertNotNull(order = jaloSession.getOrderManager().createOrder("fasel", ctx.getUser(), ctx.getCurrency(), new Date(),
					true));
			// ----------------------------------------------------------
			// --- test correct value key
			// ----------------------------------------------------------
			final Map values = new HashMap();
			values.put("code1", "blah");
			values.put("code2", "fasel");
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} " + "FROM {" + aoType.getCode() + "} " + "WHERE {" + AbstractOrder.CODE
					+ "} IN ( ?code1, ?code2 ) ORDER BY {" + AbstractOrder.CURRENCY + "}", values,
					Collections.singletonList(AbstractOrder.class), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1);
			assertNotNull(res);
			assertCollection(Arrays.asList(new AbstractOrder[]
			{ order, cart }), res.getResult());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testWithoutTypeIndex()
	{
		try
		{
			ctx.setLanguage(en);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "} = ?1 ORDER BY {"
					+ C2LItem.NAME + "}", Collections.singletonMap(Integer.valueOf(1), "German"), LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(1, res.getCount());
			assertEquals(1, res.getTotalCount());
			list = res.getResult();
			assertEquals(Collections.singletonList(de), list);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testMissingLanguageException()
	{
		try
		{
			ctx.setLanguage(null);
			try
			{
				fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "} = ?1",
						Collections.singletonMap(Integer.valueOf(1), "German"), LANG_RES_CLASSES, true, // yes, fail on unknown fields
						false, // yes, need total
						0, -1 // range
				);
				fail("missing language did not cause exception!");
			}
			catch (final FlexibleSearchException e)
			{
				// fine here
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testSpecificLanguageSearch()
	{
		try
		{
			// test mixed language specification
			ctx.setLanguage(null);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "["
					+ de.getPK().getLongValueAsString() + "]} = ?1 AND {" + C2LItem.NAME + "[" + en.getPK().getLongValueAsString()
					+ "]} = ?2 ", Arrays.asList(new String[]
			{ "Englisch", "English" }), LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					false, // no, dont ignore empty props
					0, -1, // range
					null // no timeout please !
					);
			assertNotNull(res);
			assertEquals(1, res.getCount());
			assertEquals(1, res.getTotalCount());
			list = res.getResult();
			assertEquals(Collections.singletonList(en), list);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testIgnoredLanguageSearch()
	{
		try
		{
			// test ignored language specification
			ctx.setLanguage(null);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME + "[ANY]} = ?1",
					Collections.singletonMap(Integer.valueOf(1), "Englisch"), LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(1, res.getCount());
			assertEquals(1, res.getTotalCount());
			list = res.getResult();
			assertEquals(Collections.singletonList(en), list);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testNonPKSelect() throws Exception
	{
		try
		{
			// test custom select field
			ctx.setLanguage(null);
			res = fs.search(ctx, "SELECT {" + C2LItem.ISOCODE + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE
					+ "} IN ( 'en', 'de' ) " + "ORDER BY {1:" + C2LItem.ISOCODE + "} ASC", null,
					Collections.singletonList(String.class), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(2, res.getCount());
			assertEquals(2, res.getTotalCount());
			list = res.getResult();
			assertEquals(Arrays.asList(new String[]
			{ "de", "en" }), list);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			throw e;
		}
	}

	@Test
	public void testUnknownFieldException() throws Exception
	{
		try
		{
			final String query = "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {uNkNoWnFiElD} IS NULL ";
			// test unknown field exception
			try
			{
				res = fs.search(ctx, query, null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
						false, // yes, need total
						0, -1 // range
						);
				fail("unknown field did not cause exception!");
			}
			catch (final FlexibleSearchException e)
			{
				// fine here
			}
			// try without check again
			try
			{
				res = fs.search(ctx, query, null, LANG_RES_CLASSES, false, // no, dont fail on unknown fields
						false, // no, dont skip total calculation
						0, -1 // range
						);
			}
			catch (final FlexibleSearchException e)
			{
				e.printStackTrace(System.err);
				fail("unknown field caused exception despite disabled check! : " + e);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			throw e;
		}
	}

	@Test
	public void testOuterJoins() throws Exception
	{
		final String LEFT_JOIN = " LEFT JOIN ";

		try
		{
			ctx.setLanguage(null);

			final String INNER_OK_QUERY = "SELECT {1:" + C2LItem.ISOCODE + "}, {2:" + C2LItem.ISOCODE + "} " + "FROM {" + LANG_TYPE
					+ " JOIN " + LANG_TYPE + " ON {1:" + PK_ATTR + "} = {2:" + PK_ATTR + "} AND {1:" + C2LItem.ISOCODE + "} = {2:"
					+ C2LItem.ISOCODE + "} } " + "WHERE {1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' ) " + "ORDER BY {1:"
					+ C2LItem.ISOCODE + "} ASC";

			final String INNER_FAIL_QUERY = "SELECT {1:" + C2LItem.ISOCODE + "}, {2:" + C2LItem.ISOCODE + "} " + "FROM {"
					+ LANG_TYPE + " JOIN " + LANG_TYPE + " ON {1:" + PK_ATTR + "} = {2:" + PK_ATTR + "} AND {2:" + C2LItem.ISOCODE
					+ "} = 'xxx' } " + "WHERE {1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' ) " + "ORDER BY {1:" + C2LItem.ISOCODE
					+ "} ASC";

			final String OUTER_OK_QUERY = "SELECT {1:" + C2LItem.ISOCODE + "}, {2:" + C2LItem.ISOCODE + "} " + "FROM {" + LANG_TYPE
					+ LEFT_JOIN + LANG_TYPE + " ON  {1:" + PK_ATTR + "} = {2:" + PK_ATTR + "} AND {2:" + C2LItem.ISOCODE
					+ "} = 'xxx' } " + "WHERE {1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' )" + "ORDER BY {1:" + C2LItem.ISOCODE
					+ "} ASC";

			String OUTER_SELECTIVE_QUERY = "SELECT {1:" + C2LItem.ISOCODE + "}, {2:prop} " + "FROM {" + LANG_TYPE + LEFT_JOIN
					+ LANG_TYPE + " ON {1:" + PK_ATTR + "} = {2:" + PK_ATTR + "} AND {2:prop} = 'only de' } " + "WHERE {1:"
					+ C2LItem.ISOCODE + "} IN ( 'de', 'en' ) " + "ORDER BY {1:" + C2LItem.ISOCODE + "} ASC";

			if (Config.isOracleUsed())
			{
				OUTER_SELECTIVE_QUERY = "SELECT {1:" + C2LItem.ISOCODE + "}, {2:prop} " + "FROM {" + LANG_TYPE + LEFT_JOIN
						+ LANG_TYPE + " ON {1:" + PK_ATTR + "} = {2:" + PK_ATTR + "} AND dbms_lob.substr({2:prop},50,1) = 'only de' } "
						+ "WHERE {1:" + C2LItem.ISOCODE + "} IN ( 'de', 'en' ) " + "ORDER BY {1:" + C2LItem.ISOCODE + "} ASC";


			}

			// test find by self join

			res = fs.search(ctx, INNER_OK_QUERY, null, Arrays.asList(new Class[]
			{ String.class, String.class }), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(2, res.getCount());
			assertEquals(2, res.getTotalCount());
			list = res.getResult();
			assertEquals(Arrays.asList(new Object[]
			{ Arrays.asList(new Object[]
			{ "de", "de" }), Arrays.asList(new Object[]
			{ "en", "en" }) }), list);

			// test not-find by self join with impossible condition

			res = fs.search(ctx, INNER_FAIL_QUERY, null, Arrays.asList(new Object[]
			{ String.class, String.class }), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(0, res.getCount());
			assertEquals(0, res.getTotalCount());
			list = res.getResult();
			assertEquals(Collections.EMPTY_LIST, list);

			// test find by self join with impossible condition and outer join
			res = fs.search(ctx, OUTER_OK_QUERY, null, Arrays.asList(new Object[]
			{ String.class, String.class }), true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(2, res.getCount());
			assertEquals(2, res.getTotalCount());
			list = res.getResult();
			assertEquals(Arrays.asList(new Object[]
			{ Arrays.asList(new Object[]
			{ "de", null }), Arrays.asList(new Object[]
			{ "en", null }) }), list);

			// test find by self join with selective outer join condition
			ctx.setLanguage(de);
			res = fs.search(ctx, OUTER_SELECTIVE_QUERY, null, Arrays.asList(new Object[]
			{ String.class, String.class }), false, // no, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(2, res.getCount());
			assertEquals(2, res.getTotalCount());
			list = res.getResult();
			assertEquals(Arrays.asList(new Object[]
			{ Arrays.asList(new Object[]
			{ "de", "only de" }), Arrays.asList(new Object[]
			{ "en", null }) }), list);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			throw e;
		}
	}

	@Test
	public void testOrderBy() throws Exception
	{
		try
		{
			ctx.setLanguage(null);
			final List allLangs = new ArrayList(jaloSession.getC2LManager().getAllLanguages());

			// ASC
			Collections.sort(allLangs, new Comparator()
			{
				@Override
				public final int compare(final Object o1, final Object o2)
				{
					return ((Language) o1).getIsoCode().compareTo(((Language) o2).getIsoCode());
				}
			});
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} ORDER BY {" + C2LItem.ISOCODE + "} ASC", null,
					LANG_RES_CLASSES, true, // yes, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(allLangs.size(), res.getCount());
			assertEquals(allLangs.size(), res.getTotalCount());
			list = res.getResult();
			for (int i = 0; i < allLangs.size(); i++)
			{
				assertEquals(allLangs.get(i), list.get(i));
			}

			// DESC
			Collections.sort(allLangs, new Comparator()
			{
				@Override
				public final int compare(final Object o1, final Object o2)
				{
					return ((Language) o2).getIsoCode().compareTo(((Language) o1).getIsoCode());
				}
			});

			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} ORDER BY {" + C2LItem.ISOCODE + "} DESC", null,
					LANG_RES_CLASSES, true, // yes, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(allLangs.size(), res.getCount());
			assertEquals(allLangs.size(), res.getTotalCount());
			list = res.getResult();
			// test equals
			for (int i = 0; i < allLangs.size(); i++)
			{
				assertEquals(allLangs.get(i), list.get(i));
			}

			// fail for control

			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} ORDER BY {" + C2LItem.ISOCODE + "} ASC", null,
					LANG_RES_CLASSES, true, // yes, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(allLangs.size(), res.getCount());
			assertEquals(allLangs.size(), res.getTotalCount());
			list = res.getResult();
			// test revert equals
			for (int i = 0; i < allLangs.size(); i++)
			{
				assertEquals(allLangs.get(i), list.get(allLangs.size() - i - 1));
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			throw e;
		}
	}

	@Test
	public void testOrderByMissingProperty() throws Exception
	{
		try
		{
			ctx.setLanguage(null);


			/*
			 * error on oracle while order by <..clobfield> can be solved with that: SELECT FROM Props prop_t0_p0 ORDER BY
			 * dbms_lob.substr(prop_t0_p0.VALUESTRING1,50,1) ASC
			 */

			/*
			 * test failure see bug 2302 -> will not fail any more since FlexibleSearch does outer joining automatically we
			 * keep the test source for historical reasons ;)
			 */
			String orderby = " {1:blahblubb-should-not-exist} ASC";
			if (Config.isOracleUsed())
			{
				orderby = "dbms_lob.substr({1:blahblubb-should-not-exist},50,1) ASC";
			}

			res = fs.search(ctx, "SELECT {1:" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME
					+ "[ANY]} = ?1 ORDER BY " + orderby, Collections.singletonMap(Integer.valueOf(1), "Englisch"), LANG_RES_CLASSES,
					false, // no, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertCollection(Collections.singletonList(en), res.getResult());

			// test find by optional table

			orderby = " {1:blahblubb-should-not-exist:o} ASC";
			if (Config.isOracleUsed())
			{
				orderby = "dbms_lob.substr({1:blahblubb-should-not-exist:o},50,1) ASC";
			}


			res = fs.search(ctx, "SELECT {1:" + PK_ATTR + "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.NAME
					+ "[ANY]} = ?1 ORDER BY " + orderby, Collections.singletonMap(Integer.valueOf(1), "Englisch"), LANG_RES_CLASSES,
					false, // no, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertCollection(Collections.singletonList(en), res.getResult());

			orderby = " {1:blahblubb-should-not-exist:o} ASC";
			if (Config.isOracleUsed())
			{
				orderby = "dbms_lob.substr({1:blahblubb-should-not-exist:o},50,1) ASC";
			}


			res = fs.search(ctx, "SELECT {1:" + PK_ATTR + "} FROM {" + LANG_TYPE + "} ORDER BY " + orderby, null, LANG_RES_CLASSES,
					false, // no, dont fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertTrue(res.getCount() + " > 0", res.getCount() > 0);
			assertTrue(res.getTotalCount() + " > 0", res.getTotalCount() > 0);
			list = res.getResult();
			assertTrue(list + " > 0 ", list != null && !list.isEmpty());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			throw e;
		}
	}

	@Test
	public void testSubselects()
	{
		try
		{
			ctx.setLanguage(de);

			res = fs.search(ctx, "SELECT {1:" + PK_ATTR + "} FROM {" + LANG_TYPE + "} " + "WHERE {" + C2LItem.NAME + "} NOT IN ( {{"
					+ "SELECT {" + C2LItem.NAME + "} FROM {" + LANG_TYPE + "} WHERE {2:" + C2LItem.NAME + "} LIKE ?1" + "}} )",
					Collections.singletonMap(Integer.valueOf(1), "Eng%"), LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertTrue(res.getCount() >= 1);
			assertTrue(res.getTotalCount() >= 1);
			list = res.getResult();
			assertTrue(list.contains(de) && !list.contains(en));

			// test referencing enclosing query

			res = fs.search(ctx, "SELECT {1:" + PK_ATTR + "} FROM {" + LANG_TYPE + "} " + "WHERE {" + C2LItem.NAME
					+ "} NOT IN ({{SELECT {l." + C2LItem.NAME + "} FROM {" + LANG_TYPE + " AS l} WHERE {1:" + C2LItem.NAME + "}={l."
					+ C2LItem.NAME + "}}} )", null, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertEquals(0, res.getCount());
			assertEquals(0, res.getTotalCount());
			list = res.getResult();
			assertEquals(Collections.EMPTY_LIST, list);

			// test nested subselects
			final Map values = new HashMap();
			values.put("active", Boolean.TRUE);
			res = fs.search(ctx, "SELECT {" + PK_ATTR + "} FROM {" + LANG_TYPE + "} " + "WHERE {" + C2LItem.NAME
					+ "} NOT IN ( {{"
					+ // should match de
					"SELECT {" + C2LItem.NAME + "} FROM {" + LANG_TYPE + "} WHERE {" + PK_ATTR + "} = ({{" + "SELECT {" + PK_ATTR
					+ "} FROM {" + LANG_TYPE + "} WHERE {" + C2LItem.ISOCODE + "} IN ('de', 'en') AND {" + Language.ACTIVE
					+ "}=?active" + // should match 'en'
					"}})" + "}} )", values, LANG_RES_CLASSES, true, // yes, fail on unknown fields
					false, // yes, need total
					0, -1 // range
					);
			assertNotNull(res);
			assertCollection(Arrays.asList(new Object[]
			{ de }), res.getResult());
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error: " + e);
		}
	}

	@Test
	public void testAmbiguousBrackets()
	{
		try
		{
			fs.search("SELECT {p.PK} FROM {Product AS p} " + "WHERE {p.code} IN ({{"
					+ "SELECT {u.code} FROM {Unit AS u JOIN Language AS l on {u.code}={l.isoCode}} " + // 1. end of FROM clashes with field -> fake sub select end
					"WHERE 'test'={l.name}}})", // 2. end of sub select clashes with field -> wrong sub select end
					Collections.EMPTY_MAP, de.hybris.platform.core.PK.class);
			fs.search("SELECT {p.PK} FROM {Product AS p} "
					+ "WHERE {p.code} IN ({{SELECT {u.code} FROM {Unit AS u JOIN Language AS l on {u.code}={l.isoCode}}}})", // everyting together ;)
					Collections.EMPTY_MAP, de.hybris.platform.core.PK.class);
		}
		catch (final FlexibleSearchException e)
		{
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	public void testJoin() throws JaloItemNotFoundException
	{
		final String key = "flexsearchtest";
		final Product product = jaloSession.getProductManager().createProduct(key);
		assertNotNull(product);
		final Unit unit = jaloSession.getProductManager().createUnit(key, key);
		assertNotNull(unit);
		unit.setName(key);

		final String t_product = tm.getRootComposedTypeForJaloClass(Product.class).getCode();
		final String t_unit = tm.getRootComposedTypeForJaloClass(Unit.class).getCode();

		final SearchResult result = jaloSession.getFlexibleSearch().search(
				"SELECT {1:PK} FROM {" + t_product + " JOIN " + t_unit + " ON {1:" + Product.CODE + "} = {2:" + Unit.NAME + "} }"
						+ "WHERE {1:" + Product.CODE + "} = ?1", Collections.singletonMap(Integer.valueOf(1), key),
				Collections.singletonList(Product.class), true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollectionElements(result.getResult(), product);
	}

	@Test
	public void testEmptyQuery()
	{
		final SearchResult result = jaloSession.getFlexibleSearch().search(ctx, "SELECT count(*) FROM {" + LANG_TYPE + "}", null,
				Collections.singletonList(Integer.class), true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		final Collection coll = result.getResult();
		assertTrue("expected [3] but got " + coll, coll != null && !coll.isEmpty()
				&& ((Integer) coll.iterator().next()).intValue() >= 3);
	}

	@Test
	public void testIgnoreMissingLocalizations()
	{
		assertNotNull(unknownLang);
		ctx.setLanguage(unknownLang);

		// a) search without outer joining property rows
		SearchResult result = jaloSession.getFlexibleSearch().search(
				ctx,
				"SELECT {1:PK} FROM {" + LANG_TYPE + "}" + "WHERE {1:" + C2LItem.NAME + "} = 'xxx' OR {1:" + C2LItem.NAME
						+ "} IS NULL", null, Collections.singletonList(Currency.class), true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		assertCollectionElements(result.getResult(), de);

		// b) search for missing property row with optional flag
		result = jaloSession.getFlexibleSearch().search(
				ctx,
				"SELECT {1:PK} FROM {" + LANG_TYPE + "}" + "WHERE {1:" + C2LItem.NAME + ":o} = 'xxx' OR {1:" + C2LItem.NAME
						+ ":o} IS NULL", null, Collections.singletonList(Currency.class), true, // yes, fail on unknown fields
				false, // yes, need total
				0, -1 // range
				);
		final Collection coll = result.getResult();
		assertTrue("expected [" + de + "," + en + "] but got " + coll, coll.contains(de) && coll.contains(en));

	}

	@Test
	public void testLanguageIsoCodeWithDot() throws ConsistencyCheckException
	{
		Language lDot;
		assertNotNull(lDot = C2LManager.getInstance().createLanguage("ch.fr"));

		final SessionContext ctx = new SessionContext(jaloSession.getSessionContext());
		ctx.setLanguage(lDot);

		de.setName(ctx, "ch.fr");

		List<Language> lst = getFlexibleSearch().search(ctx,
				"SELECT {" + Item.PK + "} FROM {" + LANG_TYPE + "} WHERE {name}='ch.fr'", Collections.EMPTY_MAP,
				Collections.singletonList(Language.class), true, true, 0, -1).getResult();
		assertEquals(Collections.singletonList(de), lst);

		lst = getFlexibleSearch().search(
				"SELECT {" + Item.PK + "} FROM {" + LANG_TYPE + "} WHERE {name[" + lDot.getPK().toString() + "]}='ch.fr'",
				Collections.EMPTY_MAP, Collections.singletonList(Language.class), true, true, 0, -1).getResult();
		assertEquals(Collections.singletonList(de), lst);

		lst = getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + LANG_TYPE + "} WHERE {name[ch.fr]}='ch.fr'",
				Collections.EMPTY_MAP, Collections.singletonList(Language.class), true, true, 0, -1).getResult();
		assertEquals(Collections.singletonList(de), lst);

		lst = getFlexibleSearch().search("SELECT {l:" + Item.PK + "} FROM {" + LANG_TYPE + " AS l} WHERE {l:name[ch.fr]}='ch.fr'",
				Collections.EMPTY_MAP, Collections.singletonList(Language.class), true, true, 0, -1).getResult();
		assertEquals(Collections.singletonList(de), lst);
	}

	@Test
	public void testOracle1000ExpressionsLimit()
	{
		if (Config.isOracleUsed())
		{
			// test failure
			final Map<String, Object> params = new HashMap<String, Object>();
			try
			{
				final Collection coll = new ArrayList(1001);
				for (int i = 0; i < 1001; i++)
				{
					coll.add(Integer.valueOf(i));
				}
				params.put("values", coll);
				getFlexibleSearch().search("SELECT {PK} FROM {Unit} WHERE {conversion} IN ( ?values )", params, Unit.class);
			}
			catch (final Exception e)
			{
				// expected this
			}
			params.clear();

			// test OR workaround
			final Collection coll1 = new ArrayList(1000);
			for (int i = 0; i < 1000; i++)
			{
				coll1.add(Integer.valueOf(i));
			}
			final Collection coll2 = new ArrayList(500);
			for (int i = 0; i < 500; i++)
			{
				coll2.add(Integer.valueOf(i));
			}
			params.put("values1", coll1);
			params.put("values2", coll2);
			getFlexibleSearch().search("SELECT {PK} FROM {Unit} WHERE {conversion} IN ( ?values1 ) OR {conversion} IN ( ?values2 )",
					params, Unit.class);

			// now test linkmanager directly

			final Language l = jaloSession.getSessionContext().getLanguage();

			final Collection linkList = new ArrayList(1500);
			for (int i = 0; i < 1500; i++)
			{
				linkList.add(l);
			}

			l.setLinkedItems(true, "fooLink", null, (List<? extends Item>) linkList);

			assertEquals(1500, l.getLinkedItemsCount(true, "fooLink", null));
			assertEquals(linkList, l.getLinkedItems(true, "fooLink", null));

			l.removeLinkedItems(true, "fooLink", null, (List) linkList);
			assertEquals(0, l.getLinkedItemsCount(true, "fooLink", null));
			assertEquals(Collections.EMPTY_LIST, l.getLinkedItems(true, "fooLink", null));

			l.addLinkedItems(true, "fooLink", null, (List) linkList);
			assertEquals(1500, l.getLinkedItemsCount(true, "fooLink", null));
			assertEquals(linkList, l.getLinkedItems(true, "fooLink", null));
		}
	}

	/**
	 * Tests bug which reduced column count when wrapping outdated PKs from select.
	 */
	@Test
	public void testMissingColumnBug() throws ConsistencyCheckException, EJBInvalidParameterException
	{
		final PK pk1 = PK.createCounterPK(Constants.TC.Product);
		final PK pk2 = PK.createCounterPK(Constants.TC.Product);

		final Link l = (Link) WrapperFactory.wrap(((LinkManagerEJB) LinkManager.getInstance().getRemote()).createLink("foo", pk1,
				pk2, 0, 0));

		assertTrue(l.isAlive());
		assertNull(l.getSource());
		assertNull(l.getTarget());
		assertEquals(pk1, ((LinkRemote) l.getImplementation().getRemote()).getSourcePK());
		assertEquals(pk2, ((LinkRemote) l.getImplementation().getRemote()).getTargetPK());

		final List<List<PK>> rows = getFlexibleSearch().search(
				"SELECT {" + Link.SOURCE + "}, {" + Link.TARGET + "} FROM {Link!} WHERE {" + Item.PK + "}=?lnk", //
				Collections.singletonMap("lnk", l), //
				Arrays.asList(PK.class, PK.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows.size());
		final List<PK> row = rows.get(0);
		assertEquals(Arrays.asList(pk1, pk2), row);

		final List<List<Item>> rows2 = getFlexibleSearch().search(
				"SELECT {" + Link.SOURCE + "}, {" + Link.TARGET + "} FROM {Link!} WHERE {" + Item.PK + "}=?lnk", //
				Collections.singletonMap("lnk", l), //
				Arrays.asList(Item.class, Item.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows2.size());
		final List<Item> row2 = rows2.get(0);
		assertEquals(Arrays.asList(null, null), row2);

		final List<List> rows3 = getFlexibleSearch().search(
				"SELECT {" + Link.SOURCE + "}, {" + Link.TARGET + "} FROM {Link!} WHERE {" + Item.PK + "}=?lnk", //
				Collections.singletonMap("lnk", l), //
				Arrays.asList(Item.class, PK.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows3.size());
		final List row3 = rows3.get(0);
		assertEquals(Arrays.asList(null, pk2), row3);
	}

	@Test
	public void testSerializableResults() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		ComposedType featureValueType = null;
		try
		{
			featureValueType = TypeManager.getInstance().getComposedType("ProductFeature");
		}
		catch (final JaloItemNotFoundException e)
		{
			log.warn("can't find ProductFeature type - skipped testSerializableResults()");
			return;
		}

		final Product p = ProductManager.getInstance().createProduct("foo");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("product", p);
		params.put("qualifier", "foo");
		params.put("value", p);

		final Item f = featureValueType.newInstance(params);

		final List<List> rows1 = getFlexibleSearch().search(
				"SELECT {rawvalue}, {PK} FROM {ProductFeature} WHERE {" + Item.PK + "}=?f", //
				Collections.singletonMap("f", f), //
				Arrays.asList(Object.class, Item.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows1.size());
		final List row1 = rows1.get(0);
		assertEquals(Arrays.asList(p, f), row1);

		final List<List> rows2 = getFlexibleSearch().search("SELECT {rawvalue} FROM {ProductFeature} WHERE {" + Item.PK + "}=?f", //
				Collections.singletonMap("f", f), //
				Arrays.asList(Object.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(Arrays.asList(p), rows2);

		// test outdated PK result column
		final PK invalidPK = PK.createCounterPK(Constants.TC.Product);
		((ExtensibleItem) f).setProperty("rawValue", new ItemPropertyValue(invalidPK));

		final List<List> rows3 = getFlexibleSearch().search(
				"SELECT {rawvalue}, {PK} FROM {ProductFeature} WHERE {" + Item.PK + "}=?f", //
				Collections.singletonMap("f", f), //
				Arrays.asList(Object.class, Item.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows3.size());
		final List row3 = rows3.get(0);
		assertEquals(Arrays.asList(null, f), row3);

		final List<List> rows4 = getFlexibleSearch().search(
				"SELECT {rawvalue}, {rawvalue} FROM {ProductFeature} WHERE {" + Item.PK + "}=?f", //
				Collections.singletonMap("f", f), //
				Arrays.asList(Object.class, Serializable.class), //
				true, true, 0, -1//
				).getResult();

		assertEquals(1, rows4.size());
		final List row4 = rows4.get(0);
		assertEquals(Arrays.asList(null, null), row4);
	}

	@Test
	public void testPLA7389() throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloItemNotFoundException,
			JaloDuplicateQualifierException
	{
		final TypeManager typeman = TypeManager.getInstance();

		final ComposedType someTestCP = typeman.createComposedType(typeman.getComposedType(GenericItem.class), "SomeTest");
		someTestCP.createAttributeDescriptor("test_from", typeman.getType(String.class.getName()), AttributeDescriptor.READ_FLAG
				| AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.SEARCH_FLAG);
		someTestCP.createAttributeDescriptor("test_from_test", typeman.getType(String.class.getName()),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.SEARCH_FLAG);
		someTestCP.createAttributeDescriptor("from_test", typeman.getType(String.class.getName()), AttributeDescriptor.READ_FLAG
				| AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.SEARCH_FLAG);

		assertNotNull(someTestCP);
		assertNotNull(someTestCP.getAttributeDescriptor("test_from"));
		assertNotNull(someTestCP.getAttributeDescriptor("test_from_test"));
		assertNotNull(someTestCP.getAttributeDescriptor("from_test"));

		final FlexibleSearch flex = getFlexibleSearch();

		String query = "select {test_from} from {SomeTest}";
		flex.search(query, GenericItem.class);

		query = "select {from_test} from {SomeTest}";
		flex.search(query, GenericItem.class);

		query = "select {test_from_test} from {SomeTest}";
		flex.search(query, GenericItem.class);

	}

	@Test
	public void testSqlServerInlining()
	{
		if (Config.isSQLServerUsed())
		{
			final User u = jaloSession.getUser();
			final Collection testColl = new ArrayList(2100);
			for (int i = 0; i < 2100; i++)
			{
				testColl.add(u);
			}
			// test if this does *not* throw a exception -> sqlserver normally
			// does not allow more than 2000 parameter but now we're trying
			// to inline values if that happens
			final List<User> res = getFlexibleSearch().search(//
					"SELECT {PK} FROM {User} WHERE {PK} IN ( ?coll )", //
					Collections.singletonMap("coll", testColl),//
					User.class).getResult();
			assertEquals(Collections.singletonList(u), res);
		}
	}

	/*
	 * Test wrong SQL query being generated in case we select at least two language versions of a single (props table)
	 * attribute.
	 */
	@Test
	public void testPLA_8330() throws JaloInvalidParameterException, JaloItemNotFoundException, JaloSecurityException,
			JaloBusinessException
	{
		final String PROP = "someprop";

		final ComposedType ct = TypeManager.getInstance().getComposedType(Title.class);
		final AttributeDescriptor ad = ct.createAttributeDescriptor(//
				"newAttr", //
				TypeManager.getInstance().getType("localized:" + String.class.getName()),//
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG
						| AttributeDescriptor.SEARCH_FLAG //
		);

		assertTrue(ad.isLocalized());
		assertTrue(ad.isProperty());
		assertNull(ad.getDatabaseColumn());

		final Language de = getOrCreateLanguage("de");
		final Language en = getOrCreateLanguage("en");

		final Title t1 = UserManager.getInstance().createTitle("t1");
		final Title t2 = UserManager.getInstance().createTitle("t2");

		final SessionContext ctx = new SessionContext(jaloSession.getSessionContext());
		ctx.setLanguage(de);
		t1.setName(ctx, "t1 de name");
		t1.setLocalizedProperty(ctx, PROP, "t1 de prop");
		t1.setAttribute(ctx, ad.getQualifier(), "t1 de attr");
		t2.setName(ctx, "t2 de name");
		t2.setLocalizedProperty(ctx, PROP, "t2 de prop");
		t2.setAttribute(ctx, ad.getQualifier(), "t2 de attr");

		ctx.setLanguage(en);
		t1.setName(ctx, "t1 en name");
		t1.setLocalizedProperty(ctx, PROP, "t1 en prop");
		t1.setAttribute(ctx, ad.getQualifier(), "t1 en attr");
		// let t2 have no EN row at all

		// test typed localized property
		List<List> rows = getFlexibleSearch().search(//
				"SELECT {PK}, {i:name[" + de.getPK().getLongValueAsString() + "]:o}, " + //
						"{i:name[" + en.getPK().getLongValueAsString() + "]:o} " + //
						"FROM {Title AS i} WHERE {PK} IN (?titles)",//
				Collections.singletonMap("titles", Arrays.asList(t1, t2)),//
				Arrays.asList(Title.class, String.class, String.class), //
				true, true, 0, -1).getResult();

		assertEquals(2, rows.size());

		for (final List row : rows)
		{
			final Title t = (Title) row.get(0);
			final String deName = (String) row.get(1);
			final String enName = (String) row.get(2);

			assertNotNull(t);
			ctx.setLanguage(de);
			assertEquals(t.getName(ctx), deName);
			ctx.setLanguage(en);
			assertEquals(t.getName(ctx), enName);
		}

		// test typed dump localized property
		rows = getFlexibleSearch().search(//
				"SELECT {PK}, {i:" + ad.getQualifier() + "[" + de.getPK().getLongValueAsString() + "]:o}, " + //
						"{i:" + ad.getQualifier() + "[" + en.getPK().getLongValueAsString() + "]:o} " + //
						"FROM {Title AS i} WHERE {PK} IN (?titles)",//
				Collections.singletonMap("titles", Arrays.asList(t1, t2)),//
				Arrays.asList(Title.class, String.class, String.class), //
				true, true, 0, -1).getResult();

		assertEquals(2, rows.size());

		for (final List row : rows)
		{
			final Title t = (Title) row.get(0);
			final String deAttr = (String) row.get(1);
			final String enAttr = (String) row.get(2);

			assertNotNull(t);
			ctx.setLanguage(de);
			assertEquals(t.getAttribute(ctx, ad.getQualifier()), deAttr);
			ctx.setLanguage(en);
			assertEquals(t.getAttribute(ctx, ad.getQualifier()), enAttr);
		}
		// test untyped dump table localized property
		rows = getFlexibleSearch().search(//
				"SELECT {PK}, {i:" + PROP + "[" + de.getPK().getLongValueAsString() + "]:o}, " + //
						"{i:" + PROP + "[" + en.getPK().getLongValueAsString() + "]:o} " + //
						"FROM {Title AS i} WHERE {PK} IN (?titles)",//
				Collections.singletonMap("titles", Arrays.asList(t1, t2)),//
				Arrays.asList(Title.class, String.class, String.class), //
				false, true, 0, -1).getResult();

		assertEquals(2, rows.size());

		for (final List row : rows)
		{
			final Title t = (Title) row.get(0);
			final String deProp = (String) row.get(1);
			final String enProp = (String) row.get(2);

			assertNotNull(t);
			ctx.setLanguage(de);
			assertEquals(t.getLocalizedProperty(ctx, PROP), deProp);
			ctx.setLanguage(en);
			assertEquals(t.getLocalizedProperty(ctx, PROP), enProp);
		}

	}

	@Test
	public void testPLA_8410()
	{
		// if this is no longer throwing a exception the bug has been fixed
		getFlexibleSearch().search(//
				"SELECT {o.code} AS code1, {o.code} AS code2, count(*) as amount " + //
						"FROM {AbstractOrder as o} " + //
						"WHERE {o.user} IS NOT NULL " + //
						"GROUP BY {o.code} " + //
						"HAVING count(*) > 1",//
				Collections.EMPTY_MAP,//
				Arrays.asList(String.class, String.class),//
				true, true, 0, -1);
	}

	@Test
	public void testPLA_9290() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		final Collection<Title> allTitle = new HashSet<Title>(UserManager.getInstance().getAllTitles());

		final ComposedType titleType = TypeManager.getInstance().getComposedType(Title.class);
		for (int i = 0; i < 1000; i++)
		{
			TypeManager.getInstance().createComposedType(titleType, "SubTitleType" + i);
		}
		assertTrue(1000 <= titleType.getAllSubTypes().size());

		// make sure we're not admin
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAnonymousCustomer());

		assertFalse(JaloSession.getCurrentSession().getUser().isAdmin());


		final Collection<Title> matches = new HashSet<Title>(getFlexibleSearch().search(//
				"SELECT {PK} FROM {Title}", Title.class).getResult());

		assertEquals(allTitle, matches);
	}

	@Test
	public void testPLA_11153() throws ConsistencyCheckException
	{
		final Customer customer = UserManager.getInstance().createCustomer("test");
		final Order order = OrderManager.getInstance().createOrder("test", customer, getOrCreateCurrency("test"), new Date(), true);
		assertEquals(customer, order.getUser());

		String query = "SELECT {e:pk} from {AbstractOrder as o join Employee as e on {o:user} = {e:pk}}";
		List<Employee> result = getFlexibleSearch().search(query, Employee.class).getResult();
		assertTrue(result.isEmpty());

		query = "SELECT {e:pk} from {Employee as e join AbstractOrder as o on {o:user} = {e:pk}}";
		result = getFlexibleSearch().search(query, Employee.class).getResult();
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSimpleLimitQuery()
	{
		//given
		final String query = "SELECT {PK} FROM {ComposedType} order by {PK}";
		final List expectedList = fs.search(query, Long.class).getResult();

		//when suddenly
		final List listFromChunks = new LinkedList<>();
		final int count = 10;
		int start = 0;
		List chunk = null;

		while ((chunk = fs.search(query, Collections.EMPTY_MAP, Collections.singletonList(Long.class), true, true, start, count)
				.getResult()).size() > 0)
		{
			listFromChunks.addAll(chunk);
			start += count;
		}

		//then
		assertThat(listFromChunks).isEqualTo(expectedList);
	}

	/*
	 * ECP-1358
	 */
	@Test
	public void testCountQueryWithOrderByIncludingParameter()
	{
		// given
		final String query = "SELECT {PK} FROM {Title AS t} ORDER BY CASE WHEN {t.modifiedtime} < ?param THEN 0 ELSE 1 END";
		final Map<String,Object> params = Collections.singletonMap("param", new Date());
		final List<Class> resultClasses = Arrays.asList(PK.class);
		final int THRESHOLD = JDBCValueMappings.getScrollableThreshold();
		createDummyItems(5,0);
		
		// when
		// 	query 1: without count query since there are too few items 
		final SearchResult srEmpty = fs.search(query,params, resultClasses,true,false/*need total -> dontNeedTotal=false*/,0,10);
		createDummyItems(THRESHOLD,5);
		// 	query 2: *with* count query since there are now more items than configured threshold  
		final SearchResult srFull = fs.search(query,params, resultClasses,true,false/*need total -> dontNeedTotal=false*/,0,10);
		
		// then 
		assertEquals(5, srEmpty.getTotalCount());
		assertEquals(THRESHOLD+5, srFull.getTotalCount());

	}
	
	void createDummyItems(int count, int offset)
	{
		for( int i = 0; i < count; i++ )
		{
			try
			{
				UserManager.getInstance().createTitle("TCQWOIP-"+(offset+i));
			}
			catch (ConsistencyCheckException e)
			{
				fail(e.getMessage());
			}
		}
	}

	
	/**
	 * 
	 */
	private FlexibleSearch getFlexibleSearch()
	{
		return FlexibleSearch.getInstance();//new NonWrappingFlexibleSearch();
	}


	class NonWrappingFlexibleSearch extends FlexibleSearch
	{
		@Override
		protected SearchResult wrapSearchResult(final SearchResult cachedSearchResult) throws Exception
		{

			return cachedSearchResult;
		}

		@Override
		public SearchResult search(final String query, final Map values, final List resultClasses,
				final boolean failOnUnknownFields, final boolean dontNeedTotal, final int start, final int count)
				throws FlexibleSearchException
		{
			return super.search(query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
		}
	}
}
