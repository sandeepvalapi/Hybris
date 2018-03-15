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
package de.hybris.platform.servicelayer.search.paginated.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.constants.SearchConstants;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link DefaultPaginatedGenericDao}. It uses testPaginatedUserGroupDao which is specified in
 * test-core-spring.xml to test its functionality. To verify sorted results, it defined two comparators. To respect
 * already existing {@link UserGroupModel} in junit tenant, existingModelCount is added.
 */
@IntegrationTest
public class DefaultPaginatedGenericDaoIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	@Resource
	private DefaultPaginatedGenericDao<UserGroupModel> testPaginatedUserGroupDao;

	private UserGroupModel[] userGroupModels;
	private int existingModelCount;
	private static final int ADDED_MODEL_COUNT = 7;
	private int expectedModelCount;


	protected Comparator<UserGroupModel> nameComparator = new NameComparator();
	protected Comparator<UserGroupModel> uidComparator = new UidComparator();

	@Before
	public void setUp() throws Exception
	{
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPagination(5, 0, true);

		final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

		existingModelCount = (int) searchResult.getPagination().getTotalNumberOfResults();
		userGroupModels = createUserGroupModels(ADDED_MODEL_COUNT);
		expectedModelCount = existingModelCount + ADDED_MODEL_COUNT;
	}

	@Test
	public void shouldSearchByPagination()
	{
		for (int pageSize = 1; pageSize <= expectedModelCount; pageSize++)
		{
			// given
			final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPagination(pageSize, 0, true);

			// when
			final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

			// then
			assertPaginationResults(pageSize, expectedModelCount, searchResult);
		}
	}

	@Test
	public void shouldSearchAndSortByName()
	{
		// given
		// Set the pagination data with pageSize of 5, currentPage at 0 and needsTotal to true with sort by name ascending
		final int pageSize = 5;
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME, SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(pageSize, 0, true,
				sortMap);

		// when
		final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

		// then
		assertPaginationResults(pageSize, expectedModelCount, searchResult);
		assertThat(searchResult.getResults()).overridingErrorMessage("Results are not sorted by specified comparator")
				.isSortedAccordingTo(nameComparator);
	}

	@Test
	public void shouldSearchAndSortByNameWithUpperCaseName()
	{
		// given
		// Set the pagination data with pageSize of 5, currentPage at 0 and needsTotal to true with sort by name ascending
		final int pageSize = 5;
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.NAME.toUpperCase(), SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(pageSize, 0, true,
				sortMap);

		// when
		final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

		// then
		assertPaginationResults(pageSize, expectedModelCount, searchResult);
		assertThat(searchResult.getResults()).overridingErrorMessage("Results are not sorted by specified comparator")
				.isSortedAccordingTo(nameComparator);
	}

	@Test
	public void shouldSearchAndSortByUid()
	{
		// given
		// Set the pagination data with pageSize of 5, currentPage at 0 and needsTotal to true with sort by uid ascending
		final int pageSize = 5;
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.UID, SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(pageSize, 0, true,
				sortMap);

		// when
		final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

		// then
		assertPaginationResults(pageSize, expectedModelCount, searchResult);
		assertThat(searchResult.getResults()).overridingErrorMessage("Results are not sorted by specified comparator")
				.isSortedAccordingTo(uidComparator);
	}

	@Test
	public void shouldSearchAndSortByUidWithUpperCaseUid()
	{
		// given
		// Set the pagination data with pageSize of 5, currentPage at 0 and needsTotal to true with sort by uid ascending
		final int pageSize = 5;
		final Map<String, String> sortMap = new LinkedHashMap();
		sortMap.put(UserGroupModel.UID.toUpperCase(), SearchConstants.ASCENDING);
		final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(pageSize, 0, true,
				sortMap);

		// when
		final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(searchPageData);

		// then
		assertPaginationResults(pageSize, expectedModelCount, searchResult);
		assertThat(searchResult.getResults()).overridingErrorMessage("Results are not sorted by specified comparator")
				.isSortedAccordingTo(uidComparator);
	}

	@Test
	public void shouldSearchByPaginationWithParam()
	{
		for (int pageSize = 1; pageSize <= ADDED_MODEL_COUNT; pageSize++)
		{
			// given
			final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPagination(pageSize, 0, true);

			final Map<String, Object> params = new HashMap<>();
			params.put(UserGroupModel.PK, Arrays.asList(userGroupModels));

			// when
			final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(params, searchPageData);

			// then
			assertPaginationResults(pageSize, ADDED_MODEL_COUNT, searchResult);
		}
	}

	@Test
	public void shouldSearchByPaginationAndSortByNameWithParam()
	{
		for (int pageSize = 1; pageSize <= ADDED_MODEL_COUNT; pageSize++)
		{
			// given
			final Map<String, String> sortMap = new LinkedHashMap();
			sortMap.put(UserGroupModel.NAME, SearchConstants.ASCENDING);
			final SearchPageData searchPageData = PaginatedSearchUtils.createSearchPageDataWithPaginationAndSorting(pageSize, 0,
					true, sortMap);

			final Map<String, Object> params = new HashMap<>();
			params.put(UserGroupModel.PK, Arrays.asList(userGroupModels));

			// when
			final SearchPageData<UserGroupModel> searchResult = testPaginatedUserGroupDao.find(params, searchPageData);

			// then
			assertPaginationResults(pageSize, ADDED_MODEL_COUNT, searchResult);
			assertThat(searchResult.getResults()).overridingErrorMessage("Results are not sorted by specified comparator")
					.isSortedAccordingTo(nameComparator);
		}
	}

	/**
	 * Returns an {@link UserGroupModel} array of given size with each of UserGroupModel's name of "test_usergroup" +
	 * sequence.
	 *
	 * @param size
	 *           the size of array
	 * @return Array of {@link UserGroupModel}
	 */
	protected UserGroupModel[] createUserGroupModels(final int size)
	{
		final UserGroupModel[] userGroupModels = new UserGroupModel[size];
		for (int i = 0; i < userGroupModels.length; i++)
		{
			userGroupModels[i] = createUserGroup("test_usergroup" + i);
		}
		return userGroupModels;
	}

	/**
	 * Returns a new {@link UserGroupModel} instance with given name and uid of name connected with current time in
	 * milliseconds.
	 *
	 * @param name
	 *           the name of the {@link UserGroupModel}
	 * @return {@link UserGroupModel}
	 */
	protected UserGroupModel createUserGroup(final String name)
	{
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);
		userGroupModel.setUid(name + System.currentTimeMillis());
		userGroupModel.setName(name);

		modelService.save(userGroupModel);
		return userGroupModel;
	}

	/**
	 * Asserts the given searchPageData results to see if it has the expected result count based on requested pagination
	 * pageSize. Then also checks for the total results count.
	 *
	 * @param expectedPaginatedResultSize
	 *           the expected result count
	 * @param expectedTotalResultSize
	 *           the expected total results of the search results
	 * @param searchResult
	 *           the searchResult
	 */
	protected void assertPaginationResults(final int expectedPaginatedResultSize, final int expectedTotalResultSize,
			final SearchPageData<UserGroupModel> searchResult)
	{
		assertThat(searchResult).isNotNull();
		assertThat(searchResult.getResults()).isNotNull();
		assertThat(searchResult.getResults().size()).isEqualTo(expectedPaginatedResultSize);
		assertThat(searchResult.getPagination()).isNotNull();
		assertThat(searchResult.getPagination().getTotalNumberOfResults()).isEqualTo(expectedTotalResultSize);
	}

	/**
	 * {@link UserGroupModel} name attribute comparator using {@link String#compareTo(String)} which handles for null
	 * value. As name attribute is defined as optional from Principal item type, this comparator return 0 for either one
	 * of given userGroupModel has null name attribute.
	 */
	public class NameComparator implements Comparator<UserGroupModel>
	{
		@Override
		public int compare(final UserGroupModel userGroup1, final UserGroupModel userGroup2)
		{
			final String name1 = userGroup1.getName();
			final String name2 = userGroup2.getName();

			// If either one of given UserGroupModel's name is null, return 0 as it can't be compared
			if (name1 == null || name2 == null)
			{
				return 0;
			}
			return name1.compareTo(name2);
		}
	}

	/**
	 * {@link UserGroupModel} uid attribute comparator using {@link String#compareTo(String)}. As uid attribute is
	 * mandatory from Principle item type, null check is omitted.
	 */
	public class UidComparator implements Comparator<UserGroupModel>
	{
		@Override
		public int compare(final UserGroupModel userGroup1, final UserGroupModel userGroup2)
		{
			return userGroup1.getUid().compareTo(userGroup2.getUid());
		}
	}
}
