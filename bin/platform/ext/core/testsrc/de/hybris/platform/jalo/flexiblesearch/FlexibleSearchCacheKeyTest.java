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
package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.StandardSearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;


/**
 *
 */

@PerformanceTest
public class FlexibleSearchCacheKeyTest extends HybrisJUnit4Test
{
	private final static Logger LOG = Logger.getLogger(FlexibleSearchCacheKeyTest.class);

	private static final int QUERY_CALLS = 1000000;

	private FlexibleSearch getSearch()
	{
		return FlexibleSearch.getInstance();
	}


	@Before
	public void warmUp()
	{
		final String query = "select {PK} from {Product} where {PK} = ?user";

		final List signature = Collections.singletonList(Integer.class);

		LOG.info("Warm up  ....");
		for (int i = 0; i < QUERY_CALLS; i++)
		{
			final Map values = Collections.singletonMap("user", Integer.valueOf(i++));
			getSearch().search(query, values, signature, true, false, 0, i);
		}
	}


	@Test
	public void testSimpleQuery()
	{
		final Employee admin = UserManager.getInstance().getAdminEmployee();

		final String query = "select {PK} from {Product}";
		final Map values = Collections.emptyMap();
		final List signature = Collections.singletonList(Integer.class);

		final SessionContext local = JaloSession.getCurrentSession().createLocalSessionContext();
		try
		{

			local.setUser(admin);
			local.setAttribute(FlexibleSearch.DISABLE_EXECUTION, Boolean.TRUE);

			Registry.getMasterTenant().getCache().clear();

			final long start = System.currentTimeMillis();
			for (int i = 0; i < QUERY_CALLS; i++)
			{
				final StandardSearchResult result = (StandardSearchResult) getSearch().search(query, values, signature, true, false,
						0, i);
				if (!result.isFromCache())
				{
					Assert.assertFalse(result.isFromCache());
				}
			}
			final long duration = System.currentTimeMillis() - start;

			ASCIITable.getInstance().printTable(
					new String[]
					{ "operation", "count", "duration [ms]", "cache ratio missed/gets" },
					new String[][]
					{
					{
							"search query simple",
							String.valueOf(QUERY_CALLS),
							String.valueOf(duration),
							String.valueOf(Registry.getMasterTenant().getCache().getMissCount()
									/ (float) Registry.getMasterTenant().getCache().getGetCount()) } });

		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}

	}


	@Test
	public void testQueryIteration()
	{
		final Employee admin = UserManager.getInstance().getAdminEmployee();

		final String query = "select {PK} from {Product}";
		final Map values = Collections.emptyMap();
		final List signature = Collections.singletonList(Integer.class);

		final SessionContext local = JaloSession.getCurrentSession().createLocalSessionContext();
		try
		{

			local.setUser(admin);
			//local.setAttribute(FlexibleSearch.DISABLE_EXECUTION, Boolean.TRUE);

			Registry.getMasterTenant().getCache().clear();

			//final long start = System.currentTimeMillis();
			final StandardSearchResult[] resultArray = new StandardSearchResult[QUERY_CALLS];
			for (int i = 0; i < QUERY_CALLS; i++)
			{
				final StandardSearchResult result = (StandardSearchResult) getSearch().search(query, values, signature, true, false,
						0, i);
				resultArray[i] = result;
				if (!result.isFromCache())
				{
					Assert.assertFalse(result.isFromCache());
				}
			}


			for (int i = 0; i < QUERY_CALLS; i++)
			{
				for (final Object single : resultArray[i].getResult())
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug(single);
					}
				}
			}
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}

	}

	@Test
	public void testComplicatedQueryWithManyParams()
	{
		final Employee admin = UserManager.getInstance().getAdminEmployee();

		final PK catalogVersionPK = PK.createUUIDPK(601);
		final PK employeegroup = PK.createUUIDPK(5);


		final String query = "SELECT itemPK, langPK, linkedItemPK, linkPK, cnt FROM ({{ SELECT {target} as itemPK,{language} as langPK, {source} AS linkedItemPK, {pk} AS linkPK, {reverseSequenceNumber} AS cnt FROM {Principal2ReadableCatalogVersionRelation*} WHERE {target} IN ( ?items ) AND {qualifier}=?quali  AND {language} IS NULL  }} UNION ALL {{ SELECT null as itemPK, {language} as langPK, {source} AS linkedItemPK, null as linkPK, MAX({sequenceNumber}) AS cnt FROM {Principal2ReadableCatalogVersionRelation*} WHERE {source} IN ( ?toBeLinked ) AND {qualifier}=?quali  AND {language} IS NULL GROUP BY {language}, {source} }}) xyz ORDER BY cnt ASC";
		final Map values = new HashMap();
		values.put("items", catalogVersionPK);
		values.put("quali", "Principal2ReadableCatalogVersionRelation");
		values.put("toBeLinked", employeegroup);
		//[class de.hybris.platform.core.PK, class de.hybris.platform.core.PK, class de.hybris.platform.core.PK, class de.hybris.platform.core.PK, class java.lang.Integer]
		final List signature = Arrays.asList(PK.class, PK.class, PK.class, PK.class, Integer.class);
		final SessionContext local = JaloSession.getCurrentSession().createLocalSessionContext();
		try
		{
			local.setUser(admin);
			local.setAttribute(FlexibleSearch.DISABLE_EXECUTION, Boolean.TRUE);
			Registry.getMasterTenant().getCache().clear();

			final long start = System.currentTimeMillis();
			for (int i = 0; i < QUERY_CALLS; i++)
			{
				final StandardSearchResult result = (StandardSearchResult) getSearch().search(query, values, signature, true, false,
						0, i);
				if (!result.isFromCache())
				{
					Assert.assertFalse(result.isFromCache());
				}
			}
			final long duration = System.currentTimeMillis() - start;

			ASCIITable.getInstance().printTable(
					new String[]
					{ "operation", "count", "duration [ms]", "cache ratio missed/gets" },
					new String[][]
					{
					{
							"search query long",
							String.valueOf(QUERY_CALLS),
							String.valueOf(duration),
							String.valueOf(Registry.getMasterTenant().getCache().getMissCount()
									/ (float) Registry.getMasterTenant().getCache().getGetCount()) } });
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

}
