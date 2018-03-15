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
package de.hybris.platform.jalo.type;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.ItemAttributeMap;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SearchRestrictionTest extends HybrisJUnit4Test //do NOT use TransactionalTest, the testC2LStuff() will then always SUCCEED even if something is wrong, TH
{

	private ComposedType titleType;
	private ComposedType unitType;
	private ComposedType languageType;
	private UserGroup adminGroup;
	private UserGroup customerGroup;


	@Before
	public void setUp() throws Exception
	{
		titleType = jaloSession.getTypeManager().getComposedType(Title.class);
		unitType = jaloSession.getTypeManager().getComposedType(Unit.class);
		languageType = jaloSession.getTypeManager().getComposedType(Language.class);
		adminGroup = jaloSession.getUserManager().getAdminUserGroup();
		customerGroup = jaloSession.getUserManager().createUserGroup("customergroup");
	}

	@Override
	public void finish() throws JaloSecurityException
	{
		// switch back session user before removing it
		jaloSession.setUser(UserManager.getInstance().getAnonymousCustomer());
		//
		super.finish();
	}

	@Test
	public void testQueryCheck()
	{
		/*
		 * test check during creation
		 */
		try
		{
			jaloSession.getTypeManager().createRestriction("tetsres5", adminGroup, titleType,
					" {blahfaselnotintypeforsure} IS NOT NULL");
			fail("JaloInvalidParameterException expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
		/*
		 * test check for setQuery()
		 */
		SearchRestriction res = null;
		try
		{
			res = jaloSession.getTypeManager().createRestriction("tetsres6", adminGroup, titleType, "{" + Item.PK + "} IS NOT NULL");
		}
		catch (final JaloInvalidParameterException e)
		{
			fail("got unexpected exception " + e + " : " + e.getMessage());
		}
		try
		{
			res.setQuery("sdhkfldhjas {{{ jasfhdkas} ");
		}
		catch (final JaloInvalidParameterException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
	}


	@Test
	public void testC2LStuff() throws Exception
	{
		final C2LManager c2lman = C2LManager.getInstance();
		final int currentlanguageCount = c2lman.getAllLanguages().size();

		Customer cust;
		cust = UserManager.getInstance().createCustomer("cust");
		cust.addToGroup(customerGroup);

		final String query = "{" + C2LItem.ISOCODE + "}='4'";
		final SearchRestriction res = jaloSession.getTypeManager()
				.createRestriction("tetsres1", customerGroup, languageType, query);

		final Language four = c2lman.createLanguage("4");
		final Language three = c2lman.createLanguage("3");

		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());

		final Collection adminlangcoll = c2lman.getAllLanguages();
		assertEquals(currentlanguageCount + 2, adminlangcoll.size());
		assertTrue(adminlangcoll.contains(four));
		assertTrue(adminlangcoll.contains(three));

		JaloSession.getCurrentSession().setUser(cust);
		final Collection custlangcoll = c2lman.getAllLanguages();
		assertEquals(1, custlangcoll.size());
		assertTrue(custlangcoll.contains(four));

		res.remove();
		final Collection removeRestLangColl = c2lman.getAllLanguages();
		assertEquals(currentlanguageCount + 2, removeRestLangColl.size());
		assertTrue(removeRestLangColl.contains(four));
		assertTrue(removeRestLangColl.contains(three));
	}



	@Test
	public void testCreation()
	{
		SearchRestriction res;
		final String query = "{" + Item.PK + "} IS NOT NULL";
		res = jaloSession.getTypeManager().createRestriction("code1", adminGroup, titleType, query);
		assertNotNull(res);
		assertEquals(adminGroup, res.getPrincipal());
		assertEquals(titleType, res.getRestrictionType());
		assertEquals(query, res.getQuery());
		assertEquals("code1", res.getCode());
		assertNull(res.getName());
		res.setCode("code3");
		res.setName("name");
		assertEquals("code3", res.getCode());
		assertEquals("name", res.getName());

		res = jaloSession.getTypeManager().createRestriction("code1", adminGroup, titleType, query);
		try
		{
			res = jaloSession.getTypeManager().createRestriction("code1", adminGroup, titleType, query);
			fail("Should throw JaloInvalidParameterException");
		}
		catch (final JaloInvalidParameterException e)
		{
			assertTrue(e.getMessage().contains("SearchRestriction not unique:"));
		}
		res = jaloSession.getTypeManager().createRestriction("code4", adminGroup, titleType, query);
		assertEquals("code4", res.getCode());

		final ItemAttributeMap attributes = new ItemAttributeMap();
		attributes.put(SearchRestriction.PRINCIPAL, customerGroup);
		attributes.put(SearchRestriction.RESTRICTEDTYPE, unitType);
		attributes.put(SearchRestriction.QUERY, query);
		attributes.put(SearchRestriction.CODE, "myCode");


		try
		{
			res.createItem(jaloSession.getSessionContext(), titleType, attributes);
			res.createItem(jaloSession.getSessionContext(), titleType, attributes);
			fail("Should throw JaloInvalidParameterException");
		}
		catch (final JaloInvalidParameterException e)
		{
			assertTrue(e.getMessage().contains("SearchRestriction not unique:"));
		}
		catch (final JaloBusinessException e)
		{
			fail(e.getMessage());
		}

	}

	@Test
	public void testCreationViaType() throws JaloItemNotFoundException, JaloBusinessException
	{
		final Map attributes = new HashMap();
		final String query = "{" + Item.PK + "} IS NULL";
		attributes.put(SearchRestriction.PRINCIPAL, customerGroup);
		attributes.put(SearchRestriction.RESTRICTEDTYPE, unitType);
		attributes.put(SearchRestriction.QUERY, query);
		attributes.put(SearchRestriction.CODE, "myCode");
		SearchRestriction res;
		res = (SearchRestriction) jaloSession.getTypeManager().getComposedType(SearchRestriction.class).newInstance(attributes);
		assertNotNull(res);
		assertEquals(customerGroup, res.getPrincipal());
		assertEquals(unitType, res.getRestrictionType());
		assertEquals(query, res.getQuery());
		assertEquals("myCode", res.getCode());
		assertNull(res.getName());
	}

	@Test
	public void testGetters()
	{
		SearchRestriction res1, res2;
		final String query1 = "{" + Item.PK + "} IS NULL";
		final String query2 = "{" + Item.PK + "} IS NOT NULL";
		res1 = jaloSession.getTypeManager().createRestriction("tetsres2", adminGroup, titleType, query1);
		res2 = jaloSession.getTypeManager().createRestriction("tetsres3", customerGroup, unitType, query2);
		assertCollection(Collections.singleton(res1), jaloSession.getTypeManager().getSearchRestrictions(adminGroup, titleType));
		assertCollection(Collections.singleton(res2), jaloSession.getTypeManager().getSearchRestrictions(customerGroup, unitType));
		assertCollection(Collections.EMPTY_LIST, jaloSession.getTypeManager().getSearchRestrictions(adminGroup, unitType));
		assertCollection(Collections.EMPTY_LIST, jaloSession.getTypeManager().getSearchRestrictions(customerGroup, titleType));
	}

	@Test
	public void testSearch() throws Exception
	{
		SearchRestriction res1;
		final String query1 = "{" + Title.CODE + "} LIKE '%rita'";
		res1 = jaloSession.getTypeManager().createRestriction("tetsres4", JaloSession.getCurrentSession().getUser(), titleType,
				query1);

		Title title1, title2;
		title1 = jaloSession.getUserManager().createTitle("senor");
		title2 = jaloSession.getUserManager().createTitle("senorita");

		SearchResult result = JaloSession
				.getCurrentSession()
				.getFlexibleSearch()
				.search("SELECT {" + Item.PK + "} FROM {" + titleType.getCode() + "}", null, Collections.singletonList(Title.class),
						true, // fail on unknown fields
						true, // don't need total
						0, -1 // range
				);
		assertCollection(Collections.singletonList(title2), result.getResult());

		// change the principal of the restriction and search again 
		res1.setPrincipal(adminGroup);
		result = JaloSession
				.getCurrentSession()
				.getFlexibleSearch()
				.search("SELECT {" + Item.PK + "} FROM {" + titleType.getCode() + "}", null, Collections.singletonList(Title.class),
						true, // fail on unknown fields
						true, // don't need total
						0, -1 // range
				);
		assertCollection(Arrays.asList(new Title[]
		{ title1, title2 }), result.getResult());

		// change the principal back to current user, change the query of the restriction and search again 
		res1.setPrincipal(JaloSession.getCurrentSession().getUser());
		res1.setQuery("{" + Title.CODE + "} = 'senor'");
		result = JaloSession
				.getCurrentSession()
				.getFlexibleSearch()
				.search("SELECT {" + Item.PK + "} FROM {" + titleType.getCode() + "}", null, Collections.singletonList(Title.class),
						true, // fail on unknown fields
						true, // don't need total
						0, -1 // range
				);
		assertCollection(Collections.singletonList(title1), result.getResult());
	}

	@Test
	public void testOverloading() throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloGenericCreationException,
			JaloAbstractTypeException, JaloItemNotFoundException, ConsistencyCheckException
	{
		Customer cust;
		cust = UserManager.getInstance().createCustomer("cust");
		cust.addToGroup(customerGroup);

		ComposedType subTitleType;
		subTitleType = TypeManager.getInstance().createComposedType(titleType, "SubTitle");

		final SearchRestriction titleSr = TypeManager.getInstance().createRestriction("sr", // code must be same for overloading
				customerGroup, titleType, "{" + Title.CODE + "} LIKE 'blah%'");
		final SearchRestriction subTitleSr = TypeManager.getInstance().createRestriction("sr", // code must be same for overloading
				customerGroup, subTitleType, "{" + Title.CODE + "} <> 'jaja'");

		Title t1, t2, subT1, subT2, subT3;
		t1 = ComposedType.newInstance(jaloSession.getSessionContext(), Title.class, Title.CODE, "blah1");
		t2 = ComposedType.newInstance(jaloSession.getSessionContext(), Title.class, Title.CODE, "blubb1");
		subT1 = ComposedType.newInstance(jaloSession.getSessionContext(), subTitleType.getCode(), Title.CODE, "sub1");
		subT2 = ComposedType.newInstance(jaloSession.getSessionContext(), subTitleType.getCode(), Title.CODE, "blah2");
		subT3 = ComposedType.newInstance(jaloSession.getSessionContext(), subTitleType.getCode(), Title.CODE, "jaja");

		final User prev = jaloSession.getUser();
		try
		{
			jaloSession.setUser(cust);
			Set<Title> rs = new HashSet<Title>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {Title}", Title.class)
					.getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(t1, subT1, subT2)), rs);

			rs = new HashSet<Title>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {Title!}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(t1)), rs);

			rs = new HashSet<Title>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {Title*}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(t1, t2, subT1, subT2, subT3)), rs);

			rs = new HashSet<Title>(FlexibleSearch.getInstance()
					.search("SELECT {PK} FROM {" + subTitleType.getCode() + "}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(subT1, subT2)), rs);

			// switch off sub-restriction -> super restriction should be included now
			subTitleSr.setActive(false);
			rs = new HashSet<Title>(FlexibleSearch.getInstance()
					.search("SELECT {PK} FROM {" + subTitleType.getCode() + "}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(subT2)), rs);

			rs = new HashSet<Title>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {Title}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(t1, subT2)), rs);

			// disable super restriction, enable sub restriction
			subTitleSr.setActive(true);
			titleSr.setActive(false);

			rs = new HashSet<Title>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {Title}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(t1, t2, subT1, subT2)), rs);

			rs = new HashSet<Title>(FlexibleSearch.getInstance()
					.search("SELECT {PK} FROM {" + subTitleType.getCode() + "}", Title.class).getResult());
			assertEquals(new HashSet<Title>(Arrays.asList(subT1, subT2)), rs);

		}
		finally
		{
			jaloSession.setUser(prev);
		}
	}

	@Test
	public void testDynamicRestrictions() throws ConsistencyCheckException
	{
		final ComposedType ct = TypeManager.getInstance().getComposedType(Title.class);

		final User u = UserManager.getInstance().createEmployee("heinz");
		final PrincipalGroup ug1 = UserManager.getInstance().createUserGroup("ug1");
		final PrincipalGroup ug2 = UserManager.getInstance().createUserGroup("ug2");

		ug1.setGroups(Collections.singleton(ug2));
		u.setGroups(Collections.singleton(ug1));

		final Title t1 = UserManager.getInstance().createTitle("t1");
		final Title t2 = UserManager.getInstance().createTitle("t2");
		final Title t3 = UserManager.getInstance().createTitle("t3");
		final Title t4 = UserManager.getInstance().createTitle("t4");
		final Title x1 = UserManager.getInstance().createTitle("x1");
		final Title x2 = UserManager.getInstance().createTitle("x2");

		create("sr1", ug1, ct, "{item.code} IN ( 't1', 't2', 't3', 't4' )");
		//create("sr1", ug2, ct, "{item.code} IS NULL");
		create("sr2", ug2, ct, "{item.code} LIKE 't%'");
		// test wrong overloading -> same code but different type 
		create("sr1", u, TypeManager.getInstance().getComposedType(Product.class), "1 = 0");

		final User current = jaloSession.getUser();
		try
		{
			jaloSession.setUser(u);

			// normal restriction -> limited to t1, t2, t3, t4
			assertEquals(//
					new HashSet(Arrays.asList(t1, t2, t3, t4)), //
					new HashSet(FlexibleSearch.getInstance().search(//
							"SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class).getResult()//
					)//
			);

			// disable restriction -> all
			final SessionContext myCtx = jaloSession.createSessionContext();
			myCtx.setAttribute(FlexibleSearch.DISABLE_RESTRICTIONS, Boolean.TRUE);

			assertEquals(//
					new HashSet(Arrays.asList(t1, t2, t3, t4, x1, x2)), //
					new HashSet(FlexibleSearch.getInstance().search(//
							myCtx, "SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class).getResult()//
					)//
			);

			myCtx.removeAttribute(FlexibleSearch.DISABLE_RESTRICTIONS);

			// normal restriction + dynamic -> limited to t1 and t3 
			FlexibleSearch.getInstance().addContextQueryFilter(myCtx,
					new ContextQueryFilter("foo", ct, "{item.code} NOT IN ('t2', 't4') "));

			assertEquals(//
					new HashSet(Arrays.asList(t1, t3)), //
					new HashSet(FlexibleSearch.getInstance().search(//
							myCtx, "SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class).getResult()//
					)//
			);

			// normal + dynamic + overload sr1 -> all but t2 and t4
			FlexibleSearch.getInstance().addContextQueryFilters(myCtx, //
					Arrays.asList( //
							new ContextQueryFilter("sr2", ct, "1 = 1"), // switch off sr2
							new ContextQueryFilter("sr1", ct, "1 = 1")));// switch off sr1

			assertEquals(//
					new HashSet(Arrays.asList(t1, t3, x1, x2)), //
					new HashSet(//
							FlexibleSearch.getInstance().search(myCtx, "SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class)
									.getResult()//
					)//
			);

			// add dynamic restriction for different type -> result should not be affected
			FlexibleSearch.getInstance().setContextQueryFilters(myCtx, //
					Arrays.asList(//
							new ContextQueryFilter("foo", ct, "{item.code} NOT IN ('t2', 't4') "),//
							new ContextQueryFilter("sr2", ct, "1 = 1"), // switch off sr2
							new ContextQueryFilter("sr1", ct, "1 = 1"),// switch off sr1
							new ContextQueryFilter("sr1", TypeManager.getInstance().getComposedType(Product.class), "1 = 0")));

			assertEquals(//
					new HashSet(Arrays.asList(t1, t3, x1, x2)), //
					new HashSet(FlexibleSearch.getInstance().search(//
							myCtx, "SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class).getResult()//
					)//
			);

			// test disabling both user and ctx restrictions again
			myCtx.setAttribute(FlexibleSearch.DISABLE_RESTRICTIONS, Boolean.TRUE);

			assertEquals(//
					new HashSet(Arrays.asList(t1, t2, t3, t4, x1, x2)), //
					new HashSet(FlexibleSearch.getInstance().search(//
							myCtx, "SELECT {PK} FROM {Title}", Collections.EMPTY_MAP, Title.class).getResult()//
					)//
			);
		}
		finally
		{
			jaloSession.setUser(current);
			FlexibleSearch.getInstance().clearContextQueryFilters();
		}
	}

	protected SearchRestriction create(final String code, final Principal principal, final ComposedType type, final String query)
	{
		//return TypeManager.getInstance().createRestriction(code, principal, type, query);
		try
		{
			return ComposedType.newInstance(null, SearchRestriction.class, //
					SearchRestriction.CODE, code,//
					SearchRestriction.PRINCIPAL, principal,//
					SearchRestriction.RESTRICTEDTYPE, type,//
					SearchRestriction.QUERY, query);
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
			return null;
		}
	}
}
