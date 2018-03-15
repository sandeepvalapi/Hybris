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
package de.hybris.platform.catalog.job.diff.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class ProductApprovalStatusDiffFinderTest
{
	@Mock
	private ModelService modelService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	private ProductApprovalStatusDiffFinder finder;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		finder = new ProductApprovalStatusDiffFinder();

		finder.setModelService(modelService);
		finder.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testSkipSearches()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setOverwriteProductApprovalStatus(Boolean.FALSE);

		Assert.assertEquals(0, finder.processDifferences(cronJob));

		Mockito.verifyZeroInteractions(modelService);
		Mockito.verifyZeroInteractions(flexibleSearchService);

	}

	@Test
	public void testProcessSearchesWithEmptyApprovalDifferenceCount()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();
		cronJob.setOverwriteProductApprovalStatus(Boolean.TRUE);

		final SearchResult<List<ProductModel>> result = createSearchResultMock();

		final Stack<SearchResult<List<ProductModel>>> stackOfResults = new Stack<SearchResult<List<ProductModel>>>();
		stackOfResults.add(result);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(
				new Answer<SearchResult<List<ProductModel>>>()
				{
					@Override
					public SearchResult<List<ProductModel>> answer(final InvocationOnMock invocation) throws Throwable
					{
						try
						{
							return stackOfResults.pop();
						}
						catch (final EmptyStackException ese)
						{
							return Mockito.mock(SearchResult.class);
						}

					}
				});
		Assert.assertEquals(0, finder.processDifferences(cronJob));

		Mockito.verify(modelService).saveAll(Mockito.anyCollection());
	}

	@Test
	public void testProcessSearchesWithNontEmptyApprovalDifferenceCount()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setOverwriteProductApprovalStatus(Boolean.TRUE);

		final ProductModel sourceOne = Mockito.spy(new ProductModel());
		sourceOne.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		sourceOne.setCode("sourceOne");

		final ProductModel sourceTwo = Mockito.spy(new ProductModel());
		sourceTwo.setApprovalStatus(ArticleApprovalStatus.CHECK);
		sourceTwo.setCode("sourceTwo");

		final ProductModel targetOne = Mockito.spy(new ProductModel());
		targetOne.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
		targetOne.setCode("targetOne");

		final ProductModel targetTwo = Mockito.spy(new ProductModel());
		targetTwo.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
		targetTwo.setCode("targetTwo");

		final List<ProductModel> sourceProducts = Arrays.asList(sourceOne, targetOne);

		final List<ProductModel> targetProducts = Arrays.asList(sourceTwo, targetTwo);

		final SearchResult<List<ProductModel>> result = createSearchResultMock(sourceProducts, targetProducts);

		final Stack<SearchResult<List<ProductModel>>> stackOfResults = new Stack<SearchResult<List<ProductModel>>>();
		stackOfResults.add(result);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(
				new Answer<SearchResult<List<ProductModel>>>()
				{
					@Override
					public SearchResult<List<ProductModel>> answer(final InvocationOnMock invocation) throws Throwable
					{
						if (invocation.getArguments()[0] instanceof FlexibleSearchQuery)
						{
							try
							{
								final FlexibleSearchQuery query = (FlexibleSearchQuery) invocation.getArguments()[0];
								if (query.getResultClassList() != null)
								{
									return stackOfResults.pop();
								}
								else
								{
									Assert.fail("Flexible search query should be called with custom result class list");
								}
							}
							catch (final EmptyStackException ese)
							{
								return Mockito.mock(SearchResult.class);
							}
						}
						else
						{
							Assert.fail("Flexible search should be called with flexible search query");
						}
						return null;

					}
				});
		Assert.assertEquals(2, finder.processDifferences(cronJob));

		Mockito.verify(sourceOne, Mockito.atLeastOnce()).getApprovalStatus();
		Mockito.verify(sourceTwo, Mockito.atLeastOnce()).getApprovalStatus();

		Mockito.verify(targetOne).setApprovalStatus(sourceOne.getApprovalStatus());
		Mockito.verify(targetTwo).setApprovalStatus(sourceTwo.getApprovalStatus());

		final ArgumentMatcher<Collection<ProductModel>> matcher = new ArgumentMatcher<Collection<ProductModel>>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof Collection))
				{
					return false;
				}
				final Collection<ProductModel> collection = (Collection<ProductModel>) argument;
				if (collection.size() == 2 && collection.contains(targetOne) && collection.contains(targetTwo))
				{
					return true;
				}
				return false;
			}
		};

		Mockito.verify(modelService).saveAll(Mockito.argThat(matcher));
	}

	/**
	 * Creates a mock of result returning one rows containing <code>models</code>
	 */
	private SearchResult<List<ProductModel>> createSearchResultMock(final List<ProductModel>... models)
	{
		final List<List<ProductModel>> underlyingResult = Arrays.asList(models);

		final SearchResult<List<ProductModel>> result = Mockito.mock(SearchResult.class);
		Mockito.when(Integer.valueOf(result.getCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(Integer.valueOf(result.getTotalCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(result.getResult()).thenReturn(underlyingResult);
		return result;
	}

}
