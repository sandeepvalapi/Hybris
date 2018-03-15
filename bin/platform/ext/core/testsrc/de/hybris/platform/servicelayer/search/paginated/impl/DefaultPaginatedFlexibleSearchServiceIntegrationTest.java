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
package de.hybris.platform.servicelayer.search.paginated.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.constants.SearchConstants;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link DefaultPaginatedFlexibleSearchService}.
 */
@IntegrationTest
public class DefaultPaginatedFlexibleSearchServiceIntegrationTest extends ServicelayerBaseTest
{
	private static final String USER_GROUP_QUERY = "SELECT {ug:pk} FROM {UserGroup as ug} WHERE {ug:uid} LIKE 'DefaultPaginatedFlexibleSearchServiceIntegrationTest%'";
	private static final String USER_GROUP_PREFIX = "DefaultPaginatedFlexibleSearchServiceIntegrationTest-";

	@Resource
	private DefaultPaginatedFlexibleSearchService defaultPaginatedFlexibleSearchService;
	@Resource
	private ModelService modelService;

	private PaginatedFlexibleSearchParameter parameter;

	@Before
	public void setUp() throws Exception
	{
		createUserGroup("test0", "group D");
		createUserGroup("test1", "group C");
		createUserGroup("test2", "group B");
		createUserGroup("test3", "group A");
		createUserGroup("test4", "group A");
		createUserGroup("test5", "group A");

		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(UserGroupModel.UID, "ug");
		sortCodeToQueryAlias.put(UserGroupModel.NAME, "ug");
		parameter = new PaginatedFlexibleSearchParameter();
		parameter.setFlexibleSearchQuery(new FlexibleSearchQuery(USER_GROUP_QUERY));
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
	}

	@Test
	public void shouldSearchAndSortByUidAscendingWithVariousPagination()
	{
		// Set the pagination data with pageSize of 2, currentPage at 0 and needsTotal to true with sort by uid ascending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.UID, SearchConstants.ASCENDING);
		SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 0, true, sortMap);
		parameter.setSearchPageData(searchPageData);

		SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test0", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test1", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sort of results", UserGroupModel.UID, searchResult.getSorts().get(0).getCode());

		// Set the pagination data with pageSize of 2, currentPage at 1 and needsTotal to true with sort by uid ascending order
		searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 1, true, sortMap);
		parameter.setSearchPageData(searchPageData);

		searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test2", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test3", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sort of results", UserGroupModel.UID, searchResult.getSorts().get(0).getCode());

		// Set the pagination data with pageSize of 2, currentPage at 2 and needsTotal to true with sort by uid ascending order
		searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 2, true, sortMap);
		parameter.setSearchPageData(searchPageData);

		searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test4", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test5", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sort of results", UserGroupModel.UID, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByUidDescendingOrder()
	{
		// Set the pagination data with pageSize of 2, currentPage at 1 and needsTotal to true with sort by uid descending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.UID, SearchConstants.DESCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 1, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test3", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test2", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sort of results", UserGroupModel.UID, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByUidDescendingOrderWithUpperCaseUid()
	{
		// Set the pagination data with pageSize of 2, currentPage at 1 and needsTotal to true with sort by uid descending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.UID.toUpperCase(), SearchConstants.DESCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 1, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test3", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test2", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sort of results", UserGroupModel.UID, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameAscendingOrder()
	{
		// Set the pagination data with pageSize of 2, currentPage at 2 and needsTotal to true with sort by name ascending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 2, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test1", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test0", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameAscendingOrderWithUpperCaseName()
	{
		// Set the pagination data with pageSize of 2, currentPage at 2 and needsTotal to true with sort by name ascending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME.toUpperCase(), SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 2, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test1", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test0", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameDescendingOrder()
	{
		// Set the pagination data with pageSize of 2, currentPage at 0 and needsTotal to true with sort by name descending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.DESCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 0, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test0", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test1", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameAndUidAscendingOrder()
	{
		// Set the pagination data with pageSize of 3, currentPage at 0 and needsTotal to true with sort by name ascending and uid ascending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.ASCENDING);
		sortMap.put(UserGroupModel.UID, SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(3, 0, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(3, 6, 2, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test3", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test4", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test5", searchResult.getResults().get(2).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.UID, searchResult.getSorts().get(1).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameAscendingAndUidDescendingOrder()
	{
		// Set the pagination data with pageSize of 3, currentPage at 0 and needsTotal to true with sort by name ascending and uid descending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.ASCENDING);
		sortMap.put(UserGroupModel.UID, SearchConstants.DESCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(3, 0, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(3, 6, 2, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test5", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test4", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test3", searchResult.getResults().get(2).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.UID, searchResult.getSorts().get(1).getCode());
	}

	@Test
	public void shouldSearchAndSortByNameAsendingOrderNotByDate()
	{
		// Set the pagination data with pageSize of 2, currentPage at 0 and needsTotal to true with sort by name descending and date descending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.DESCENDING);
		sortMap.put("date", SearchConstants.DESCENDING); // Invalid sortCode "date" should be filtered out
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(2, 0, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(2, 6, 1, searchResult);
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test0", searchResult.getResults().get(0).getUid());
		Assert.assertEquals("Unexpected uid", USER_GROUP_PREFIX + "test1", searchResult.getResults().get(1).getUid());
		Assert.assertEquals("Unexpected sorts of results", UserGroupModel.NAME, searchResult.getSorts().get(0).getCode());
	}

	@Test
	public void shouldSearchAndFilterAllInvalidSorts()
	{
		// Set the pagination data with pageSize of 6, currentPage at 0 and needsTotal to true with sort by date ascending and price ascending order
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put("date", SearchConstants.ASCENDING); // Invalid sortCode "date" should be filtered out
		sortMap.put("price", SearchConstants.ASCENDING); // Invalid sortCode "price" should be filtered out
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(6, 0, true,
				sortMap);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(6, 6, 0, searchResult);
	}

	@Test
	public void shouldSearchWithoutSorts()
	{
		// Set the pagination data with pageSize of 6, currentPage at 0 and needsTotal to true without sort
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPagination(6, 0, true);
		parameter.setSearchPageData(searchPageData);

		final SearchPageData<UserGroupModel> searchResult = defaultPaginatedFlexibleSearchService.search(parameter);

		assertPaginationResults(6, 6, 0, searchResult);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSearchIfParameterSearchPageDataIsNull()
	{
		defaultPaginatedFlexibleSearchService.search(parameter);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSearchIfParameterFlexibleSearchQueryIsNull()
	{
		parameter.setFlexibleSearchQuery(null);
		defaultPaginatedFlexibleSearchService.search(parameter);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotSearchIfParameterPaginationDataIsNull()
	{
		final SearchPageData searchPageData = new SearchPageData();
		searchPageData.setPagination(null);
		defaultPaginatedFlexibleSearchService.search(parameter);
	}

	@Test
	public void shouldNotSearchIfParameterPaginationDataCurrentPageIsNegative()
	{
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPagination(1, -1, true);
		parameter.setSearchPageData(searchPageData);
		try
		{
			defaultPaginatedFlexibleSearchService.search(parameter);
			Assert.fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae)
		{
			// Do nothing. expected result
		}
	}

	/**
	 * Asserts the given searchPageData results to see if it has the expected result count based on requested pagination
	 * pageSize. Then also checks for the total results count and sort list count.
	 *
	 * @param expectedPaginatedResultSize
	 *           the expected result count
	 * @param expectedTotalResultSize
	 *           the expected total results of the search results
	 * @param expectedSortSize
	 *           the expected sort list size
	 * @param searchResult
	 *           the searchResult
	 */
	protected void assertPaginationResults(final int expectedPaginatedResultSize, final int expectedTotalResultSize,
			final int expectedSortSize, final SearchPageData<UserGroupModel> searchResult)
	{
		Assert.assertNotNull("Search page data is null", searchResult);
		Assert.assertNotNull("Search results are null", searchResult.getResults());
		Assert.assertEquals("Unexpected number of results", expectedPaginatedResultSize, searchResult.getResults().size());
		Assert.assertNotNull("Search page data pagination is null", searchResult.getPagination());
		Assert.assertEquals("Unexpected total number of results", expectedTotalResultSize,
				searchResult.getPagination().getTotalNumberOfResults());
		Assert.assertEquals("Unexpected number of result sorts", expectedSortSize, searchResult.getSorts().size());
	}

	/**
	 * Creates {@link UserGroupModel} with given parameters
	 *
	 * @param id
	 *           the id to be set as Uid in the model
	 * @param name
	 *           the name to be set in the model
	 */
	private void createUserGroup(final String id, final String name)
	{
		final UserGroupModel userGroup = new UserGroupModel();
		userGroup.setUid(USER_GROUP_PREFIX + id);
		userGroup.setName(name);
		modelService.save(userGroup);
	}
}
