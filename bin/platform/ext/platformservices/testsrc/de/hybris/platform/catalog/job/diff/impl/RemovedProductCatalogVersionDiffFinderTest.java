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
import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.catalog.model.ProductCatalogVersionDifferenceModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
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


/**
 * Test covering {@link RemovedProductCatalogVersionDiffFinder} logic.
 */
@UnitTest
public class RemovedProductCatalogVersionDiffFinderTest
{
	@Mock
	private ModelService modelService;

	@Mock
	private EnumerationService enumerationService;

	@Mock
	private FlexibleSearchService flexibleSearchService;


	private RemovedProductCatalogVersionDiffFinder finder;

	private CatalogVersionModel source;

	private CatalogVersionModel target;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		finder = new RemovedProductCatalogVersionDiffFinder();

		finder.setModelService(modelService);
		finder.setEnumerationService(enumerationService);
		finder.setFlexibleSearchService(flexibleSearchService);

		final ProductCatalogVersionDifferenceModel differenceOne = new ProductCatalogVersionDifferenceModel();
		final ProductCatalogVersionDifferenceModel differenceTwo = new ProductCatalogVersionDifferenceModel();

		Mockito.when(modelService.create(ProductCatalogVersionDifferenceModel.class)).thenReturn(differenceOne, differenceTwo);

		Mockito.doAnswer(new Answer<ProductDifferenceMode>()
		{

			@Override
			public ProductDifferenceMode answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				if (ProductDifferenceMode.PRODUCT_NEW.getCode().equals(args[1]))
				{
					return ProductDifferenceMode.PRODUCT_NEW;
				}
				if (ProductDifferenceMode.PRODUCT_REMOVED.getCode().equals(args[1]))
				{
					return ProductDifferenceMode.PRODUCT_REMOVED;
				}
				return null;
			}
		}).when(enumerationService).getEnumerationValue(Mockito.anyString(), Mockito.anyString());

		source = new CatalogVersionModel();
		source.setVersion("source");

		target = new CatalogVersionModel();
		target.setVersion("target");

	}

	@Test
	public void testPopulateDifferenceModel()
	{
		final CatalogVersionModel source = new CatalogVersionModel();
		source.setVersion("source");

		final CatalogVersionModel target = new CatalogVersionModel();
		target.setVersion("target");

		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();
		cronJob.setTargetVersion(target);
		cronJob.setSourceVersion(source);


		final ProductModel srcProduct = new ProductModel();
		srcProduct.setCode("sourceProduct");

		final ProductModel tgtProduct = new ProductModel();
		tgtProduct.setCode("tgtProduct");

		final ProductCatalogVersionDifferenceModel diff = finder.populateDifferenceModel(srcProduct, tgtProduct, cronJob);


		Assert.assertEquals(diff.getSourceVersion(), cronJob.getSourceVersion());
		Assert.assertEquals(diff.getTargetVersion(), cronJob.getTargetVersion());
		Assert.assertEquals(diff.getMode().getType(),
				de.hybris.platform.catalog.enums.ProductDifferenceMode.PRODUCT_REMOVED.getType());
		Assert.assertEquals(diff.getMode().getCode(),
				de.hybris.platform.catalog.enums.ProductDifferenceMode.PRODUCT_REMOVED.getCode());
		Assert.assertEquals(diff.getSourceProduct(), srcProduct);
		Assert.assertEquals(diff.getDifferenceText(),
				"Product " + srcProduct.getCode() + " not found in version: " + target.getVersion());
		Assert.assertEquals(diff.getTargetProduct(), null);
		Assert.assertEquals(diff.getCronJob(), cronJob);

	}


	@Test
	public void testSkipSearches()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchMissingProducts(Boolean.FALSE);
		Assert.assertEquals(0, finder.processDifferences(cronJob));

		Mockito.verifyZeroInteractions(modelService);
		Mockito.verifyZeroInteractions(enumerationService);

	}

	@Test
	public void testProcessSearchesWithEmptyCategoryCount()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();
		cronJob.setSearchMissingProducts(Boolean.TRUE);
		final SearchResult<ProductModel> result = createSearchResultMock();

		final Stack<SearchResult<ProductModel>> stackOfResults = new Stack<SearchResult<ProductModel>>();
		stackOfResults.add(result);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(
				new Answer<SearchResult<ProductModel>>()
				{

					@Override
					public SearchResult<ProductModel> answer(final InvocationOnMock invocation) throws Throwable
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

		Mockito.verifyZeroInteractions(enumerationService);
	}


	@Test
	public void testProcessSearchesWithFewProducts()
	{

		final ProductModel productOne = new ProductModel();
		productOne.setCode("prodOne");

		final ProductModel productTwo = new ProductModel();
		productTwo.setCode("prodTwo");

		final SearchResult<ProductModel> firstResult = createSearchResultMock(productOne, productTwo);

		//record here result for a somePublicfieldTwo
		final SearchResult<ProductModel> secondResult = createSearchResultMock(productOne);


		final Stack<SearchResult<ProductModel>> stackOfResults = new Stack<SearchResult<ProductModel>>();
		stackOfResults.add(secondResult);
		stackOfResults.add(firstResult);

		Mockito.when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenAnswer(
				new Answer<SearchResult<ProductModel>>()
				{

					@Override
					public SearchResult<ProductModel> answer(final InvocationOnMock invocation) throws Throwable
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
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();
		cronJob.setSourceVersion(source);
		cronJob.setTargetVersion(target);
		cronJob.setSearchMissingProducts(Boolean.TRUE);

		Assert.assertEquals(2, finder.processDifferences(cronJob));

		final ArgumentMatcher<ProductCatalogVersionDifferenceModel> matcherOne = new ArgumentMatcher<ProductCatalogVersionDifferenceModel>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ProductCatalogVersionDifferenceModel))
				{
					return false;
				}
				final ProductCatalogVersionDifferenceModel modelLocal = (ProductCatalogVersionDifferenceModel) argument;
				if (!("Product " + productOne.getCode() + " not found in version: " + target.getVersion())
						.equalsIgnoreCase(modelLocal.getDifferenceText()))
				{
					return false;
				}
				if (modelLocal.getTargetProduct() != null)
				{
					return false;
				}
				if (!modelLocal.getSourceProduct().equals(productOne))
				{
					return false;
				}
				if (!modelLocal.getMode().getCode().equals(ProductDifferenceMode.PRODUCT_REMOVED.getCode()))
				{
					return false;
				}
				if (!modelLocal.getTargetVersion().equals(target))
				{
					return false;
				}
				if (!modelLocal.getSourceVersion().equals(source))
				{
					return false;
				}
				return true;
			}
		};

		final ArgumentMatcher<ProductCatalogVersionDifferenceModel> matcherTwo = new ArgumentMatcher<ProductCatalogVersionDifferenceModel>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ProductCatalogVersionDifferenceModel))
				{
					return false;
				}
				final ProductCatalogVersionDifferenceModel modelLocal = (ProductCatalogVersionDifferenceModel) argument;
				if (!("Product " + productTwo.getCode() + " not found in version: " + target.getVersion())
						.equalsIgnoreCase(modelLocal.getDifferenceText()))
				{
					return false;
				}
				if (modelLocal.getTargetProduct() != null)
				{
					return false;
				}
				if (!modelLocal.getSourceProduct().equals(productTwo))
				{
					return false;
				}
				if (!modelLocal.getMode().getCode().equals(ProductDifferenceMode.PRODUCT_REMOVED.getCode()))
				{
					return false;
				}
				if (!modelLocal.getTargetVersion().equals(target))
				{
					return false;
				}
				if (!modelLocal.getSourceVersion().equals(source))
				{
					return false;
				}
				return true;
			}
		};

		Mockito.verify(modelService, Mockito.times(1)).save(Mockito.argThat(matcherOne));
		Mockito.verify(modelService, Mockito.times(1)).save(Mockito.argThat(matcherTwo));
	}


	/**
	 * Creates a mock of result returning one rows containing <code>models</code>
	 */
	private SearchResult<ProductModel> createSearchResultMock(final ProductModel... models)
	{
		final List<ProductModel> underlyingResult = Arrays.asList(models);

		final SearchResult<ProductModel> result = Mockito.mock(SearchResult.class);
		Mockito.when(Integer.valueOf(result.getCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(Integer.valueOf(result.getTotalCount())).thenReturn(Integer.valueOf(models.length));
		Mockito.when(result.getResult()).thenReturn(underlyingResult);
		return result;
	}


}
