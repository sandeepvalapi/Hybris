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
package de.hybris.platform.catalog.job.util.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.job.sort.impl.ComposedTypeSorter;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Set;
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
 * Test covering {@link DefaultCatalogVersionJobDao} implementation.
 */
@UnitTest
public class DefaultCatalogVersionJobDaoTest
{
	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private ModelService modelService;

	@Mock
	private ComposedTypeSorter composedTypeSorter;

	private DefaultCatalogVersionJobDao counter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		counter = Mockito.spy(new DefaultCatalogVersionJobDao());
		counter.setFlexibleSearchService(flexibleSearchService);
		counter.setModelService(modelService);
		counter.setComposedTypeSorter(composedTypeSorter);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyCatalogeVersionForComposedType()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();

		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		composedTypeModel.setCatalogVersionAttribute(null);

		counter.getItemInstanceCount(catalogVersion, composedTypeModel);
	}

	@Test(expected = NullPointerException.class)
	public void testNullResultForASingleComposedTypeForCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");

		//record here result for a field one
		final SearchResult<Integer> firstResult = createSearchResultMock(0);

		final Stack<SearchResult<Integer>> stackOfResults = new Stack<SearchResult<Integer>>();
		stackOfResults.add(firstResult);

		Mockito.doReturn(null).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {SomeCustomType!} WHERE {fieldOne}  = ?version");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Assert.assertEquals(0, counter.getItemInstanceCount(catalogVersion, composedTypeModel));

		Mockito.verify(flexibleSearchService).search(Mockito.argThat(flexibleSearchMatcher));
		Mockito.verifyZeroInteractions(composedTypeSorter);
	}

	@Test
	public void testEmptyResultForASingleComposedTypeForCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");

		//record here result for a field one
		final SearchResult<Integer> firstResult = createSearchResultMock(0);

		final Stack<SearchResult<Integer>> stackOfResults = new Stack<SearchResult<Integer>>();
		stackOfResults.add(firstResult);
		Mockito.doAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				Assert.assertTrue(args[0] instanceof FlexibleSearchQuery);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {SomeCustomType!} WHERE {fieldOne}  = ?version");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Assert.assertEquals(0, counter.getItemInstanceCount(catalogVersion, composedTypeModel));

		Mockito.verify(flexibleSearchService).search(Mockito.argThat(flexibleSearchMatcher));
		Mockito.verifyZeroInteractions(composedTypeSorter);
	}

	@Test
	public void testSingleComposedTypeForCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");

		//record here result for a field one
		final SearchResult<Integer> firstResult = createSearchResultMock(122);

		final Stack<SearchResult<Integer>> stackOfResults = new Stack<SearchResult<Integer>>();
		stackOfResults.add(firstResult);

		Mockito.doAnswer(new Answer<SearchResult<Integer>>()
		{
			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				Assert.assertTrue(args[0] instanceof FlexibleSearchQuery);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {SomeCustomType!} WHERE {fieldOne}  = ?version");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Assert.assertEquals(122, counter.getItemInstanceCount(catalogVersion, composedTypeModel));

		Mockito.verify(flexibleSearchService).search(Mockito.argThat(flexibleSearchMatcher));
		Mockito.verifyZeroInteractions(composedTypeSorter);
		Mockito.verifyZeroInteractions(modelService);
	}

	@Test
	public void testFewComposedTypeForCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		final ComposedTypeModel composedTypeModel1 = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		final ComposedTypeModel composedTypeModel2 = createComposedTypeModel("java.lang.Integer", "fieldTwo", "OtherSomeCustomType");
		final ComposedTypeModel composedTypeModel3 = createComposedTypeModel("CustomType", "unexpectedfield", "StrangeType");

		//record here result for a field one
		final Stack<SearchResult<Integer>> stackOfResults = new Stack<SearchResult<Integer>>();
		stackOfResults.add(createSearchResultMock(10));
		stackOfResults.add(createSearchResultMock(0));
		stackOfResults.add(createSearchResultMock(20));

		Mockito.doAnswer(new Answer<SearchResult<Integer>>()
		{

			@Override
			public SearchResult<Integer> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				Assert.assertTrue(args[0] instanceof FlexibleSearchQuery);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {SomeCustomType!} WHERE {fieldOne}  = ?version");
		setOfQueries.add("SELECT count({pk}) FROM {OtherSomeCustomType!} WHERE {fieldTwo}  = ?version");
		setOfQueries.add("SELECT count({pk}) FROM {StrangeType!} WHERE {unexpectedfield}  = ?version");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Assert.assertEquals(
				10 + 0 + 20,
				counter.getItemInstanceCount(catalogVersion,
						Arrays.asList(composedTypeModel1, composedTypeModel2, composedTypeModel3)));

		Mockito.verify(flexibleSearchService, Mockito.times(3)).search(Mockito.argThat(flexibleSearchMatcher));
		Mockito.verifyZeroInteractions(composedTypeSorter);
		Mockito.verifyZeroInteractions(modelService);
	}

	@Test
	public void testGetPKs()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();


		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		//composedTypeModel.setCatalogVersionAttribute(null);

		//one PK in result
		final PK one = PK.createFixedCounterPK(1000, 0);
		final Stack<SearchResult<PK>> stackOfResults = new Stack<SearchResult<PK>>();
		stackOfResults.add(createSearchResultMock(one));

		Mockito.doAnswer(new Answer<SearchResult<PK>>()
		{

			@Override
			public SearchResult<PK> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				Assert.assertTrue(args[0] instanceof FlexibleSearchQuery);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final List<PK> result = counter.getPKList(composedTypeModel, catalogVersion);

		Assert.assertEquals(1, result.size());

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT {pk} FROM {SomeCustomType!} WHERE {fieldOne}  = ?version ORDER BY {PK} DESC");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Mockito.verify(flexibleSearchService).search(Mockito.argThat(flexibleSearchMatcher));
	}


	@Test(expected = NullPointerException.class)
	public void testGetPKsWithInvalidComposedType()
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();


		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		//clear the catalogVersion stuff
		composedTypeModel.setCatalogVersionAttribute(null);

		//one PK in result
		final PK one = PK.createFixedCounterPK(1000, 0);
		final Stack<SearchResult<PK>> stackOfResults = new Stack<SearchResult<PK>>();
		stackOfResults.add(createSearchResultMock(one));

		Mockito.doAnswer(new Answer<SearchResult<PK>>()
		{

			@Override
			public SearchResult<PK> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();
				Assert.assertTrue(args[0] instanceof FlexibleSearchQuery);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(SearchResult.class);
				}
			}

		}).when(flexibleSearchService).search(Mockito.any(FlexibleSearchQuery.class));

		final List<PK> result = counter.getPKList(composedTypeModel, catalogVersion);

		Assert.assertEquals(1, result.size());

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {SomeCustomType!} WHERE {fieldOne}  = ?version");

		final ArgumentMatcher<FlexibleSearchQuery> flexibleSearchMatcher = new ArgumentMatcher<FlexibleSearchQuery>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof FlexibleSearchQuery))
				{
					return false;
				}
				final FlexibleSearchQuery query = (FlexibleSearchQuery) argument;
				if (!setOfQueries.contains(query.getQuery()))
				{
					return false;
				}
				return true;
			}
		};

		Mockito.verify(flexibleSearchService).search(Mockito.argThat(flexibleSearchMatcher));
	}



	@Test
	public void testOrderedComposedTypes()
	{

		final ComposedTypeModel composedTypeModel = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		composedTypeModel.setCatalogVersionAttribute(null);

		final List<ComposedTypeModel> unOrderedList = Mockito.mock(List.class);

		Mockito.doReturn(unOrderedList).when(counter).getUnorderedNonAbstractComposedTypes();
		Mockito.when(composedTypeSorter.sort(unOrderedList)).thenReturn(unOrderedList);

		Assert.assertEquals(unOrderedList, counter.getOrderedComposedTypes());

		Mockito.verify(composedTypeSorter).sort(unOrderedList);
	}

	private ComposedTypeModel createComposedTypeModel(final String attributeTypeName, final String attribiteQualifier,
			final String composedTypeCode)
	{
		final TypeModel type = new TypeModel();
		type.setCode(attributeTypeName);

		final AttributeDescriptorModel descriptor = new AttributeDescriptorModel();
		descriptor.setQualifier(attribiteQualifier);
		descriptor.setAttributeType(type);

		final ComposedTypeModel composedType = new ComposedTypeModel();
		composedType.setCode(composedTypeCode);
		composedType.setCatalogVersionAttribute(descriptor);

		return composedType;
	}

	/**
	 * Creates a mock of result returning one row as integer value of <code>resultCount</code>
	 */
	private SearchResult<Integer> createSearchResultMock(final int resultCount) // NOPMD
	{
		final List<Integer> underlyingResult = Arrays.asList(Integer.valueOf(resultCount));

		final SearchResult<Integer> result = Mockito.mock(SearchResult.class);
		Mockito.when(Integer.valueOf(result.getCount())).thenReturn(Integer.valueOf(1));
		Mockito.when(Integer.valueOf(result.getTotalCount())).thenReturn(Integer.valueOf(1));
		Mockito.when(result.getResult()).thenReturn(underlyingResult);
		return result;
	}

	/**
	 * Creates a mock of result returning one row as integer value of <code>resultCount</code>
	 */
	private SearchResult<PK> createSearchResultMock(final PK... pks)
	{
		final List<PK> underlyingResult = Arrays.asList(pks);

		final SearchResult<PK> result = Mockito.mock(SearchResult.class);
		Mockito.when(Integer.valueOf(result.getCount())).thenReturn(Integer.valueOf(pks.length));
		Mockito.when(Integer.valueOf(result.getTotalCount())).thenReturn(Integer.valueOf(pks.length));
		Mockito.when(result.getResult()).thenReturn(underlyingResult);
		return result;
	}
}
