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
package de.hybris.platform.catalog;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.RelationQuery;
import de.hybris.platform.servicelayer.search.RelationQuery.ORDERING;
import de.hybris.platform.servicelayer.search.RelationQueryException;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;


/**
 * The Class FlexibleSearchServiceTest should be extended to serve as the test basis for the pagination logic discussed
 * in DevNet under "Improve API for Collection attributes" In particular we should implement and test
 * FlexibleSearchService's two new methods: 1) <T> SearchResult<T> searchRelation(Object model, String attribute, int
 * start, int count) throws FlexibleSearchException; Which returns a specified range of the complete search results 2)
 * <T> SearchResult<T> searchRelation(Object model, RelationQuery query) throws FlexibleSearchException; Which returns a
 * specified range of the complete search results, with one or more sorting criteria applied
 */
@Ignore("For DAV-1 See Devnet page 'Improve API for Collection attributes' + PLA-12571")
@IntegrationTest
public class FlexibleSearchServicePaginationTest extends ServicelayerTransactionalTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private CatalogService catalogService;

	/**
	 * Test for valid order specs.
	 */
	@Test
	public void testForValidOrderSpecs()
	{
		final RelationQuery rel = new RelationQuery(5, 10);
		assertTrue(rel.getStart() == 5);
		assertTrue(rel.getCount() == 10);
		assertTrue(rel.getOrderCount() == 0);
		rel.addOrder("someColumn", ORDERING.ASCENDING);
		assertTrue(rel.getOrderCount() == 1);
		assertTrue(rel.getOrderSpec(0).getAttribute().equals("someColumn"));
		assertTrue(rel.getOrderSpec(0).isAscending());
		rel.addOrder("anotherColumn", ORDERING.DESCENDING);
		assertTrue(rel.getOrderCount() == 2);
		assertTrue(rel.getOrderSpec(1).getAttribute().equals("anotherColumn"));
		assertFalse(rel.getOrderSpec(1).isAscending());
	}

	/**
	 * Test pagination.
	 */
	@Test
	public void testPagination()
	{
		final Collection<CatalogModel> catalogs = catalogService.getAllCatalogs();
		final int count = catalogs.size();
		assertTrue("We want at least 2 catalogs to test out paginating logic ", count >= 2);
		final CatalogModel catModel = catalogs.iterator().next();
		SearchResult<CatalogModel> res = flexibleSearchService.searchRelation(catModel, "catalogVersions", -1, -1);
		assertTrue(res.getCount() == count);
		res = flexibleSearchService.searchRelation(catModel, "catalogVersions", 0, count);
		assertTrue(res.getCount() == count);
		res = flexibleSearchService.searchRelation(catModel, "catalogVersions", 0, count - 1);
		assertTrue(res.getCount() == count - 1);
		res = flexibleSearchService.searchRelation(catModel, "catalogVersions", 0, count + 1);
		assertTrue(res.getCount() == count);
		res = flexibleSearchService.searchRelation(catModel, "catalogVersions", 1, count);
		assertTrue(res.getCount() == count - 1);
		res = flexibleSearchService.searchRelation(catModel, "catalogVersions", count, count);
		assertTrue(res.getCount() == 0);

		RelationQuery rel = new RelationQuery(0, count);
		rel.addOrder("catalogVersions", ORDERING.ASCENDING);
		rel.setModel(catModel);
		res = flexibleSearchService.searchRelation(rel);
		assertTrue(res.getCount() == count);

		rel = new RelationQuery(0, count);
		rel.addOrder("catalogVersions", ORDERING.DESCENDING);
		rel.setModel(catModel);
		res = flexibleSearchService.searchRelation(rel);
		assertTrue(res.getCount() == count);


		// YTODO: add further tests demonstrating correct ordering for one and more orderings
	}


	/*
	 * Tests for invalid inputs sent to searchRelation(ItemModel model, RelationQuery query) throws
	 * FlexibleSearchException; Neither parameter may be null
	 */
	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsA()
	{
		flexibleSearchService.searchRelation(null);
	}

	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsC()
	{
		flexibleSearchService.searchRelation(new RelationQuery(-1, -1));
	}

	/*
	 * Tests for invalid inputs sent to FlexibleSearchService::searchRelation(ItemModel model, String attribute, int
	 * start, int count) throws FlexibleSearchException; Parameters may not be null. The count may not be 0 Neither
	 * search nor count may be < -1
	 */
	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsE()
	{
		flexibleSearchService.searchRelation(null, null, 0, 0);
	}

	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsF()
	{
		flexibleSearchService.searchRelation(catalogService.getAllCatalogs().iterator().next(), null, 0, 0);
	}

	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsG()
	{
		flexibleSearchService.searchRelation(null, "catalogVersions", 0, 0);
	}

	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsH()
	{
		flexibleSearchService.searchRelation(catalogService.getAllCatalogs().iterator().next(), "catalogVersions", 0, 0);
	}

	@Test(expected = FlexibleSearchException.class)
	public void testForInvalidFlexibleSearchInputsI()
	{
		flexibleSearchService.searchRelation(catalogService.getAllCatalogs().iterator().next(), "catalogVersions", -2, -2);
	}

	/*
	 * Test for invalid relation queries. The relation query constructor takes a start and count parameter The start and
	 * count can be -1 in which case all results will be returned The count may never be 0 Neither the start nor count
	 * may be less than -1
	 */
	@Test(expected = RelationQueryException.class)
	public void testForInvalidRelationQueryInputsA()
	{
		new RelationQuery(-2, 2);
	}

	@Test(expected = RelationQueryException.class)
	public void testForInvalidRelationQueryInputsB()
	{
		new RelationQuery(-1, 0);
	}

	@Test(expected = RelationQueryException.class)
	public void testForInvalidRelationQueryInputsC()
	{
		new RelationQuery(0, 0);
	}

	@Test(expected = RelationQueryException.class)
	public void testForInvalidRelationQueryInputsD()
	{
		new RelationQuery(0, -2);
	}

	@Test(expected = RelationQueryException.class)
	public void testForInvalidRelationQueryInputsE()
	{
		new RelationQuery(-2, 2);
	}


	/*
	 * Tests for invalid OrderSpec inputs The attribute parameter may not be null or ""
	 */
	@Test(expected = RelationQueryException.class)
	public void testForInvalidOrderSpecA()
	{
		final RelationQuery rel = new RelationQuery(5, 10);
		rel.addOrder(null, ORDERING.ASCENDING);
	}

	@Test(expected = RelationQueryException.class)
	public void testForInvalidOrderSpecB()
	{
		final RelationQuery rel = new RelationQuery(5, 10);
		rel.addOrder("", ORDERING.ASCENDING);
	}
}
