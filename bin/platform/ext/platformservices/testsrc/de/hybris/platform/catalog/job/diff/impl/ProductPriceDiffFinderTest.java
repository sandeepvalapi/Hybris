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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
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


@UnitTest
public class ProductPriceDiffFinderTest
{
	@Mock
	private ModelService modelService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private EnumerationService enumerationService;

	@Mock
	private PriceService priceService;

	private SessionService sessionService;
	private ProductPriceDiffFinder finder;
	private CatalogVersionModel source;
	private CatalogVersionModel target;
	private final ProductModel sourceOne = Mockito.spy(new ProductModel());
	private final ProductModel sourceTwo = Mockito.spy(new ProductModel());
	private final ProductModel targetOne = Mockito.spy(new ProductModel());
	private final ProductModel targetTwo = Mockito.spy(new ProductModel());


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		sessionService = Mockito.spy(new MockSessionService());

		finder = new ProductPriceDiffFinder();

		finder.setModelService(modelService);
		finder.setFlexibleSearchService(flexibleSearchService);
		finder.setEnumerationService(enumerationService);
		finder.setSessionService(sessionService);
		finder.setPriceService(priceService);

		source = new CatalogVersionModel();
		source.setVersion("source");

		target = new CatalogVersionModel();
		target.setVersion("target");

		final ProductCatalogVersionDifferenceModel differenceOne = new ProductCatalogVersionDifferenceModel();
		final ProductCatalogVersionDifferenceModel differenceTwo = new ProductCatalogVersionDifferenceModel();

		Mockito.when(modelService.create(ProductCatalogVersionDifferenceModel.class)).thenReturn(differenceOne, differenceTwo);

		Mockito.doAnswer(new Answer<ProductDifferenceMode>()
		{

			@Override
			public ProductDifferenceMode answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				if (ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.getCode().equals(args[1]))
				{
					return ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE;
				}
				return null;
			}
		}).when(enumerationService).getEnumerationValue(Mockito.anyString(), Mockito.anyString());

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
				de.hybris.platform.catalog.enums.ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.getType());
		Assert.assertEquals(diff.getMode().getCode(),
				de.hybris.platform.catalog.enums.ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.getCode());
		Assert.assertEquals(diff.getSourceProduct(), srcProduct);
		Assert.assertNull(diff.getDifferenceText());
		Assert.assertEquals(diff.getTargetProduct(), tgtProduct);
		Assert.assertEquals(diff.getCronJob(), cronJob);

	}


	@Test
	public void testSkipSearches()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.FALSE);

		Assert.assertEquals(0, finder.processDifferences(cronJob));

		Mockito.verifyZeroInteractions(modelService);
		Mockito.verifyZeroInteractions(flexibleSearchService);

	}


	@Test
	public void testProcessSearchesWithEmptyProductsDifferenceCount()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.TRUE);

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

		Mockito.verifyNoMoreInteractions(sessionService);
		Mockito.verifyNoMoreInteractions(priceService);


	}

	@Test
	public void testProcessSearchesWithNullPriceInformation()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();
		cronJob.setSearchPriceDifferences(Boolean.TRUE);

		Mockito.when(sessionService.getAttribute("user")).thenReturn(new UserModel());
		Mockito.when(priceService.getPriceInformationsForProduct(Mockito.any(ProductModel.class))).thenReturn(null); //null price information

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
		try
		{
			finder.processDifferences(cronJob);
			Assert.fail("Null price information should thrown a NPE");
		}
		catch (final NullPointerException npe)
		{
			// okhere 
		}
		catch (final Exception e)
		{
			Assert.fail("Got unexpected exception: " + e);
		}

	}

	@Test
	public void testProcessSearchesWithEmptyPriceInformation()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.TRUE);
		cronJob.setPriceCompareCustomer(new UserModel());

		Mockito.when(priceService.getPriceInformationsForProduct(Mockito.any(ProductModel.class))).thenReturn(
				new ArrayList<PriceInformation>());

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
		Assert.assertEquals(0, finder.processDifferences(cronJob));

		Mockito.verifyNoMoreInteractions(modelService);

	}


	@Test
	public void testProcessSearchesWithDifferentSizePriceTheSamePriceValueInformation()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.TRUE);
		cronJob.setPriceCompareCustomer(new UserModel());

		//price information 1.0
		final List<PriceInformation> listSourceOne = createPriceInformations(1.0);
		Mockito.when(priceService.getPriceInformationsForProduct(sourceOne)).thenReturn(listSourceOne);

		//price information 1.0, 2.2
		final List<PriceInformation> listTargetOne = createPriceInformations(2.0);
		Mockito.when(priceService.getPriceInformationsForProduct(targetOne)).thenReturn(listTargetOne);


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
		Assert.assertEquals(1, finder.processDifferences(cronJob));

		final ArgumentMatcher<ProductCatalogVersionDifferenceModel> matcher = new ArgumentMatcher<ProductCatalogVersionDifferenceModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ProductCatalogVersionDifferenceModel))
				{
					return false;
				}
				final ProductCatalogVersionDifferenceModel difference = (ProductCatalogVersionDifferenceModel) argument;
				if (!"Difference above max tolerance ( 0.0 ) in price: (new: 1.0 old: 2.0 for PriceInfo: {} <PV<EUR#1.0#true>VP>."
						.equalsIgnoreCase(difference.getDifferenceText()))
				{
					return false;
				}
				if (!ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.equals(difference.getMode()))
				{
					return false;
				}
				if (!sourceOne.equals(difference.getSourceProduct()))
				{
					return false;
				}
				if (!targetOne.equals(difference.getTargetProduct()))
				{
					return false;
				}

				return true;
			}

		};


		Mockito.verify(modelService).save(Mockito.argThat(matcher));
		//
		//		Mockito.verify(sessionService, Mockito.times(2)).getAttribute("user");

	}


	@Test
	public void testProcessSearchesWithTheSameSizePriceDifferentPriceValueInformation()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.TRUE);
		cronJob.setPriceCompareCustomer(new UserModel());

		//price information 1.0
		final List<PriceInformation> listSourceOne = createPriceInformations(1.0);
		Mockito.when(priceService.getPriceInformationsForProduct(sourceOne)).thenReturn(listSourceOne);
		//price information 2.0
		final List<PriceInformation> listTargetOne = createPriceInformations(2.0);
		Mockito.when(priceService.getPriceInformationsForProduct(targetOne)).thenReturn(listTargetOne); //different price value


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
		Assert.assertEquals(1, finder.processDifferences(cronJob));

		final ArgumentMatcher<ProductCatalogVersionDifferenceModel> matcher = new ArgumentMatcher<ProductCatalogVersionDifferenceModel>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ProductCatalogVersionDifferenceModel))
				{
					return false;
				}
				final ProductCatalogVersionDifferenceModel difference = (ProductCatalogVersionDifferenceModel) argument;
				if (!"Difference above max tolerance ( 0.0 ) in price: (new: 1.0 old: 2.0 for PriceInfo: {} <PV<EUR#1.0#true>VP>."
						.equalsIgnoreCase(difference.getDifferenceText()))
				{
					return false;
				}
				if (!difference.getDifferenceValue().equals(Double.valueOf(50.0)))
				{
					return false;
				}
				if (!ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.equals(difference.getMode()))
				{
					return false;
				}
				if (!sourceOne.equals(difference.getSourceProduct()))
				{
					return false;
				}
				if (!targetOne.equals(difference.getTargetProduct()))
				{
					return false;
				}

				return true;
			}

		};
		Mockito.verify(modelService).save(Mockito.argThat(matcher));
		//
		//		Mockito.verify(sessionService, Mockito.times(2)).getAttribute("user");

	}

	/**
	 * price tolarence is measured between from old = 5 and new = 5.1 to old =1 and new =1.1 as (old - new ) *100 / old
	 * all tolerance above 5 are used, which means only one difference entry.
	 */
	@Test
	public void testProcessSearchesWithTheSameSizePriceDifferentPriceValueAndToleranceInformation()
	{
		final CompareCatalogVersionsCronJobModel cronJob = new CompareCatalogVersionsCronJobModel();

		cronJob.setSearchPriceDifferences(Boolean.TRUE);
		cronJob.setPriceCompareCustomer(new UserModel());

		cronJob.setMaxPriceTolerance(Double.valueOf(5));

		//price information 5.0 ,4.0  3.0  2.0. 1.0
		final List<PriceInformation> listSourceOne = createPriceInformations(5.0, 4.0, 3.0, 2.0, 1.0);

		Mockito.when(priceService.getPriceInformationsForProduct(sourceOne)).thenReturn(listSourceOne);
		//price information 5.1 , 4.1, 3.1 ,2.1 , 1.1
		final List<PriceInformation> listTargetOne = createPriceInformations(5.1, 4.1, 3.1, 2.1, 1.1);

		Mockito.when(priceService.getPriceInformationsForProduct(targetOne)).thenReturn(listTargetOne);


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
		Assert.assertEquals(1, finder.processDifferences(cronJob));

		final ArgumentMatcher<ProductCatalogVersionDifferenceModel> matcher = new ArgumentMatcher<ProductCatalogVersionDifferenceModel>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ProductCatalogVersionDifferenceModel))
				{
					return false;
				}
				final ProductCatalogVersionDifferenceModel difference = (ProductCatalogVersionDifferenceModel) argument;
				if (!"Difference above max tolerance ( 5.0 ) in price: (new: 1.0 old: 1.1 for PriceInfo: {} <PV<EUR#1.0#true>VP>."
						.equalsIgnoreCase(difference.getDifferenceText()))
				{
					return false;
				}
				if (difference.getDifferenceValue().doubleValue() != Math.abs(1.1 - 1) * 100 / 1.1)
				{
					return false;
				}
				if (!ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.equals(difference.getMode()))
				{
					return false;
				}
				if (!sourceOne.equals(difference.getSourceProduct()))
				{
					return false;
				}
				if (!targetOne.equals(difference.getTargetProduct()))
				{
					return false;
				}
				return true;
			}

		};
		Mockito.verify(modelService).save(Mockito.argThat(matcher));

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


	/**
	 * Creates price information list every successive entry gets its value and succeeding qualifier count
	 */
	private List<PriceInformation> createPriceInformations(final double... prices)
	{
		int index = 0;
		final List<PriceInformation> list = new ArrayList<PriceInformation>(prices.length);
		for (final double price : prices)
		{
			final PriceInformation priceInformation = Mockito.spy(new PriceInformation(new PriceValue("EUR", price, true)));
			Mockito.when(Integer.valueOf(priceInformation.getQualifierCount())).thenReturn(Integer.valueOf(index++));

			list.add(priceInformation);
		}
		return list;

	}

}
