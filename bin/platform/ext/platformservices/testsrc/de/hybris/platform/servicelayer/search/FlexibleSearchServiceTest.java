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
package de.hybris.platform.servicelayer.search;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.servicelayer.search.impl.DefaultFlexibleSearchService;
import de.hybris.platform.servicelayer.search.impl.LazyLoadMultiColumnModelList;
import de.hybris.platform.servicelayer.search.internal.preprocessor.QueryPreprocessorRegistry;
import de.hybris.platform.servicelayer.session.MockSession;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class FlexibleSearchServiceTest extends ServicelayerTransactionalTest
{
	private final static Logger LOG = Logger.getLogger(FlexibleSearchServiceTest.class);

	@Resource
	@Spy
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private CatalogService catalogService;

	@Resource
	@Spy
	private UserService userService;

	@Resource
	private SessionService sessionService;
	@Resource
	TypeService typeService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testSearchTypeInstancesWithoutParams()
	{
		areSearchesForTypeInstancesEqual("Product", "Product search was not equal");
		areSearchesForTypeInstancesEqual("Category", "Category search was not equal");
		areSearchesForTypeInstancesEqual("User", "User search was not equal");
		areSearchesForTypeInstancesEqual("VariantProduct", "VariantProduct search was not equal");
	}

	private void areSearchesForTypeInstancesEqual(final String typecode, final String errormessage)
	{
		final String query = "SELECT {PK} FROM {" + typecode + "} order by {pk}";
		final Type jaloType = TypeManager.getInstance().getType(typecode);

		final List<Product> jaloResultList = FlexibleSearch.getInstance().search(query, jaloType.getClass()).getResult();


		final de.hybris.platform.servicelayer.search.SearchResult<ItemModel> modelSearchResult = flexibleSearchService
				.search(query);
		final List<ItemModel> modelResultList = modelSearchResult.getResult();

		checkResultLists(errormessage, jaloResultList, modelResultList);
	}

	private void checkResultLists(final String errormessage, final List jaloResultList, final List<ItemModel> modelResultList)
	{
		final int jalosize = jaloResultList.size();
		assertEquals(errormessage, jalosize, modelResultList.size());

		for (int index = 0; index < jalosize; index++)
		{
			final Item jaloitem = (Item) jaloResultList.get(index);
			final Item modelitem = modelService.getSource(modelResultList.get(index));
			assertEquals(errormessage, jaloitem, modelitem);
		}
	}


	@Test
	public void testSearchWithSingleParamsForLanguage() throws ConsistencyCheckException
	{
		Language language1 = null;
		try
		{
			language1 = C2LManager.getInstance().getLanguageByIsoCode("falseLang");
		}
		catch (final JaloItemNotFoundException jile)
		{
			language1 = C2LManager.getInstance().createLanguage("falseLang");
		}
		language1.setActive(false);

		// boolean
		final String query1 = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value order by {pk}";
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("value", Boolean.TRUE);

		List<Product> jaloResultList = FlexibleSearch.getInstance().search(query1, values, Language.class).getResult();

		de.hybris.platform.servicelayer.search.SearchResult<ItemModel> modelSearchResult = flexibleSearchService.search(query1,
				values);
		List<ItemModel> modelResultList = modelSearchResult.getResult();

		final int sizeTrue = jaloResultList.size();
		assertTrue("No active languages found (" + sizeTrue + ")", sizeTrue > 0);
		checkResultLists("search for Language with one params was not equals", jaloResultList, modelResultList);

		values.put("value", Boolean.FALSE);
		jaloResultList = FlexibleSearch.getInstance().search(query1, values, Language.class).getResult();
		modelSearchResult = flexibleSearchService.search(query1, values);
		modelResultList = modelSearchResult.getResult();
		final int sizeFalse = jaloResultList.size();
		assertTrue(sizeFalse > 0);
		checkResultLists("search for Language with one params was not equals", jaloResultList, modelResultList);

		final String query2 = "SELECT count({pk}) FROM {Language}";
		final List<Integer> jaloResultList2 = FlexibleSearch.getInstance().search(query2, Integer.class).getResult();

		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query2);
		fsq.setResultClassList(Collections.singletonList(Integer.class));
		final de.hybris.platform.servicelayer.search.SearchResult<Integer> modelSearchResult2 = flexibleSearchService.search(fsq);
		final List<Integer> modelResultList2 = modelSearchResult2.getResult();
		assertEquals(1, jaloResultList2.size());
		assertEquals(jaloResultList2.size(), modelResultList2.size());
		final Integer sum = jaloResultList2.iterator().next();
		assertEquals(sum.intValue(), modelResultList2.iterator().next().intValue());
		assertTrue(sum.intValue() == sizeFalse + sizeTrue);
	}

	@Test
	public void testSearchWithSingleParams()
	{
		final Map<String, Object> values = new HashMap<String, Object>();

		final String query3 = "SELECT {pk} FROM {Product} WHERE {code} LIKE ?value";
		values.put("value", "testProduct3");
		final de.hybris.platform.servicelayer.search.SearchResult<ItemModel> modelSearchResult = flexibleSearchService.search(
				query3, values);
		final List<ItemModel> modelResultList = modelSearchResult.getResult();
		assertEquals(1, modelResultList.size());
		assertEquals("testProduct3", ((ProductModel) modelResultList.iterator().next()).getCode());

		final CatalogModel hwCatalog = flexibleSearchService
				.<CatalogModel> search("SELECT {PK} FROM {Catalog} WHERE {id}='testCatalog'").getResult().get(0);
		Assert.assertNotNull(hwCatalog);

		final CatalogVersionModel hwVersion = flexibleSearchService
				.<CatalogVersionModel> search("SELECT {PK} FROM {CatalogVersion} WHERE {version}='Online' AND {catalog}=?catalog",
						Collections.singletonMap("catalog", hwCatalog)).getResult().get(0);
		Assert.assertNotNull(hwVersion);
	}


	@Test
	public void testComplexSearch()
	{
		final String query = "SELECT {pk}, {code}, {catalogversion}, {name[en]} FROM {Product} WHERE {code} = ?code order by {pk}";
		final Map params = new HashMap<String, Object>(2);
		params.put("code", "testProduct2");

		final List<Product> jaloResultList = FlexibleSearch.getInstance().search(query, params, Product.class).getResult();
		final de.hybris.platform.servicelayer.search.SearchResult<ProductModel> modelSearchResult = flexibleSearchService.search(
				query, params);
		final List<ProductModel> modelResultList = modelSearchResult.getResult();

		assertEquals(1, jaloResultList.size());
		assertEquals(1, modelResultList.size());
		assertEquals(jaloResultList.iterator().next(), modelService.getSource(modelResultList.iterator().next()));

	}

	@Test
	public void testComplexSearch2()
	{
		final String query = "SELECT {pk}, {code}, {name[en]} FROM {Product} WHERE {code} = ?code order by {pk}";
		final Map params = new HashMap<String, Object>(2);
		params.put("code", "testProduct1");

		final List jaloResultList = FlexibleSearch.getInstance()
				.search(query, params, Arrays.asList(Product.class, String.class, String.class), true, false, 0, -1).getResult();

		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query);
		fsq.setResultClassList(Arrays.asList(ProductModel.class, String.class, String.class));
		fsq.addQueryParameters(params);

		final de.hybris.platform.servicelayer.search.SearchResult<ProductModel> modelSearchResult = flexibleSearchService
				.search(fsq);
		final List modelResultList = modelSearchResult.getResult();

		assertEquals(1, jaloResultList.size());
		assertEquals(1, modelResultList.size());

		final List internalJaloSearchList = (List) jaloResultList.iterator().next();
		final List internalModelSearchList = (List) modelResultList.iterator().next();

		assertEquals(3, internalJaloSearchList.size());
		assertEquals(3, internalModelSearchList.size());

		assertEquals(internalJaloSearchList.get(0), modelService.getSource(internalModelSearchList.get(0)));
		assertEquals(internalJaloSearchList.get(1), internalModelSearchList.get(1));
		assertEquals(internalJaloSearchList.get(2), internalModelSearchList.get(2));

	}

	@Test
	public void testFailOnUnknownFields()
	{
		final String query = "SELECT {unknown} FROM {Product} order by {pk}";
		final FlexibleSearchQuery fsq1 = new FlexibleSearchQuery(query);
		fsq1.setFailOnUnknownFields(true);
		final FlexibleSearchQuery fsq2 = new FlexibleSearchQuery(query);
		fsq2.setFailOnUnknownFields(false);

		try
		{
			flexibleSearchService.search(fsq1);
			fail();
		}
		catch (final FlexibleSearchException e1)
		{
			assertThat(e1.getCause()).isInstanceOf(de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException.class);
			assertThat(e1.getMessage()).startsWith("cannot search unknown field 'TableField(name='unknown'");
		}
		catch (final Exception e1)
		{
			fail();
		}

		try
		{
			flexibleSearchService.search(fsq2);
		}
		catch (final Exception e1)
		{
			fail();
		}

	}


	@Test
	public void testSearchResultInfos()
	{
		final String query = "SELECT {pk} FROM {Product} order by {code}";
		final de.hybris.platform.jalo.SearchResult<Product> jaloSearchResult = FlexibleSearch.getInstance().search(query,
				Collections.EMPTY_MAP, Collections.singletonList(Product.class), true, false, 5, 3);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.setCount(3);
		searchQuery.setStart(5);
		searchQuery.setNeedTotal(true);
		final SearchResult<ProductModel> modelSearchResult = flexibleSearchService.search(searchQuery);

		assertEquals(jaloSearchResult.getCount(), modelSearchResult.getCount());
		assertEquals(jaloSearchResult.getRequestedCount(), modelSearchResult.getRequestedCount());
		assertEquals(jaloSearchResult.getRequestedStart(), modelSearchResult.getRequestedStart());
		assertEquals(jaloSearchResult.getTotalCount(), modelSearchResult.getTotalCount());
	}

	/**
	 * This test checks the differtent params for the searchranges for the servicelayer search against the jalo search.
	 * There are (currently) 36 products in the db. The testcases should cover all possibilities: search with/without
	 * unlimited range, get a resultcount which fits/fits not in the overal result count (different starting points by a
	 * fixed result count).
	 */
	@Test
	public void testSearchRangeCombos()
	{
		for (int index = 5; index < 50; index++)
		{
			final ProductModel prod = modelService.create(ProductModel.class);
			prod.setCode("testProduct" + index);
		}
		modelService.saveAll();

		final de.hybris.platform.jalo.SearchResult<Integer> countSR = FlexibleSearch.getInstance().search(
				"SELECT COUNT({pk}) FROM {Product}", Integer.class);
		assertEquals(1, countSR.getTotalCount());
		final int maxProdCount = countSR.getResult().get(0).intValue();
		assertTrue(maxProdCount >= 36);

		final int len1 = maxProdCount / 2;
		final int start1 = maxProdCount / 4;


		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", start1, -1, true);
		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", start1, -1, false);
		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", start1, len1, true);
		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", start1, len1, false);
		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", maxProdCount - start1, len1, true);
		compareSearches(maxProdCount, "SELECT {pk} FROM {Product} order by {pk}", maxProdCount - start1, len1, false);
	}

	private void compareSearches(final int maxProdCount, final String query, final int start, final int count,
			final boolean needTotal)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.setCount(count);
		searchQuery.setStart(start);
		searchQuery.setNeedTotal(needTotal);
		final SearchResult<ProductModel> modelSearchResult = flexibleSearchService.search(searchQuery);

		final de.hybris.platform.jalo.SearchResult<Product> seres = FlexibleSearch.getInstance().search(query,
				Collections.EMPTY_MAP, Collections.singletonList(Product.class), false, !needTotal, start, count);

		assertEquals(seres.getCount(), modelSearchResult.getCount());
		assertEquals(seres.getRequestedCount(), modelSearchResult.getRequestedCount());
		assertEquals(seres.getRequestedStart(), modelSearchResult.getRequestedStart());
		assertEquals(seres.getTotalCount(), modelSearchResult.getTotalCount());
		if (needTotal)
		{
			assertEquals(maxProdCount, modelSearchResult.getTotalCount());
		}
	}


	@Test
	public void testMultiColumnSearch()
	{
		final String oldPageSize = Config.getParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY);
		Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, "10");
		try
		{
			final int max = 30;

			final TitleModel[] titleModel = new TitleModel[max];

			for (int i = 0; i < max; i++)
			{
				titleModel[i] = new TitleModel();
				titleModel[i].setCode(("t" + (i / 10)) + (i % 10));
				titleModel[i].setName("t" + i + "name", i18nService.getCurrentLocale());
			}

			modelService.saveAll(Arrays.asList(titleModel));

			final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(//
					"SELECT {code},{PK},{name} FROM {Title} WHERE {PK} IN (?titles) ORDER BY {code} ASC");
			flexibleSearchQuery.addQueryParameter("titles", Arrays.asList(titleModel));
			flexibleSearchQuery.setResultClassList(Arrays.asList(String.class, Title.class, String.class));

			final SearchResult<List<?>> searchResult = flexibleSearchService.search(flexibleSearchQuery);
			assertEquals(max, searchResult.getTotalCount());
			final List list = (List) ModelUtils.getFieldValue(searchResult, "resultList");
			assertNotNull(list);
			assertTrue(list instanceof LazyLoadMultiColumnModelList);
			for (int i = 0; i < max; i++)
			{
				final List row = (List) list.get(i);
				assertEquals(3, row.size());
				assertEquals(titleModel[i].getCode(), row.get(0));
				assertEquals(titleModel[i], row.get(1));
				assertEquals(titleModel[i].getName(), row.get(2));
			}
		}
		finally
		{
			if (oldPageSize != null)
			{
				Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, oldPageSize);
			}
		}
	}


	/**
	 * This method tests the usage of jalo classes vs. service layer model classes (and default java classes) for the
	 * search result class list. The same flexiblesearch statement is used for both searches and multicolumn.
	 */
	@Test
	public void testPLA8496()
	{
		final String searchquery = "SELECT {pk}, {code}, {unit} FROM {Product}";

		final FlexibleSearchQuery query1 = new FlexibleSearchQuery(searchquery);
		query1.setResultClassList(Arrays.asList(Long.class, String.class, UnitModel.class));

		final SearchResult<List<?>> sr1 = flexibleSearchService.search(query1);
		assertTrue(sr1.getTotalCount() > 0);
		final List list1 = (List) ModelUtils.getFieldValue(sr1, "resultList");
		assertNotNull(list1);
		assertTrue(list1 instanceof LazyLoadMultiColumnModelList);
		final List row1 = (List) list1.get(0);
		assertEquals(3, row1.size());

		final FlexibleSearchQuery query2 = new FlexibleSearchQuery(searchquery);
		query2.setResultClassList(Arrays.asList(Product.class, String.class, Unit.class));
		final SearchResult<List<?>> sr2 = flexibleSearchService.search(query2);
		assertTrue(sr2.getTotalCount() > 0);
		final List list2 = (List) ModelUtils.getFieldValue(sr1, "resultList");
		assertNotNull(list2);
		assertTrue(list2 instanceof LazyLoadMultiColumnModelList);
		final List row2 = (List) list2.get(0);
		assertEquals(3, row2.size());

		assertEquals(sr1.getTotalCount(), sr2.getTotalCount());
		for (int i = 0; i < row1.size(); i++)
		{
			assertEquals(row1.get(i), row2.get(i));
		}
	}

	/**
	 * This method tests the search for enumeration values but with different result classes for a single column list.
	 */
	@Test
	public void testPLA8827_single()
	{
		randomizeStatuses();
		// setup test
		final String searchQuery = "SELECT {approvalStatus} FROM {Product}";
		final List<EnumerationValue> jaloResultList = FlexibleSearch.getInstance().search(searchQuery, EnumerationValue.class)
				.getResult();
		assertFalse(jaloResultList.isEmpty());

		final FlexibleSearchQuery fsq1 = new FlexibleSearchQuery(searchQuery);
		fsq1.setResultClassList(Arrays.asList(EnumerationValue.class));

		final FlexibleSearchQuery fsq2 = new FlexibleSearchQuery(searchQuery);
		fsq2.setResultClassList(Arrays.asList(EnumerationValueModel.class));

		final FlexibleSearchQuery fsq3 = new FlexibleSearchQuery(searchQuery);
		fsq3.setResultClassList(Arrays.asList(ArticleApprovalStatus.class));

		// execute test
		final SearchResult<EnumerationValueModel> sr1 = flexibleSearchService.search(fsq1);
		final SearchResult<EnumerationValueModel> sr2 = flexibleSearchService.search(fsq2);
		final SearchResult<HybrisEnumValue> sr3 = flexibleSearchService.search(fsq3);

		// compare result
		assertEquals(jaloResultList.size(), sr1.getTotalCount());
		assertEquals(jaloResultList.size(), sr2.getTotalCount());
		assertEquals(jaloResultList.size(), sr3.getTotalCount());

		for (int index = 0; index < jaloResultList.size(); index++)
		{
			final String sr1Code = sr1.getResult().get(index).getCode();
			assertEquals(jaloResultList.get(index).getCode(), sr1Code);
			final String sr2Code = sr2.getResult().get(index).getCode();
			assertEquals(jaloResultList.get(index).getCode(), sr2Code);
			assertTrue(sr3.getResult().get(index) instanceof ArticleApprovalStatus);
			final String sr3Code = sr3.getResult().get(index).getCode();
			assertEquals(jaloResultList.get(index).getCode(), sr3Code);
		}
	}

	/**
	 * Generally if no result class provided search result should contain elements extending ItemModel In result for this
	 * test might not only Items but also enumeration values be returned, anyway it should not throw an exception and
	 * return all object as {@link ItemModel},{@link EnumerationValueModel} instances.
	 */
	@Test
	public void testPLA8827_items()
	{
		final String searchQuery = "SELECT {PK} FROM {Item}";

		final FlexibleSearchQuery fsq1 = new FlexibleSearchQuery(searchQuery);

		final SearchResult<?> sr1 = flexibleSearchService.search(fsq1);

		LOG.info("Total items :" + sr1.getTotalCount());

		for (final Object o : sr1.getResult())
		{
			assertTrue(o instanceof ItemModel);
		}
	}

	/**
	 * If explicitly declared expected class as implementation of {@link de.hybris.platform.core.HybrisEnumValue}
	 * instances of this type should be returned.
	 */
	@Test
	public void testPLA8827_hybris_enum()
	{
		randomizeStatuses();

		final String searchQuery = "SELECT {approvalStatus} FROM {Product}";
		final List<EnumerationValue> jaloResultList = FlexibleSearch.getInstance().search(searchQuery, EnumerationValue.class)
				.getResult();
		assertFalse(jaloResultList.isEmpty());

		final FlexibleSearchQuery fsq1 = new FlexibleSearchQuery(searchQuery);
		fsq1.setResultClassList(Arrays.asList(ArticleApprovalStatus.class));

		final SearchResult<ArticleApprovalStatus> sr1 = flexibleSearchService.search(fsq1);

		for (int index = 0; index < jaloResultList.size(); index++)
		{
			assertEquals(jaloResultList.get(index).getCode(), sr1.getResult().get(index).getCode());
		}
	}

	/**
	 * This method tests the search for enumeration values but with different result classes for a multiple column list.
	 */
	@Test
	public void testPLA8827_multiple()
	{
		// setup test checkmulitline
		randomizeStatuses();

		final String searchQueryJalo = "SELECT {approvalStatus} FROM {Product} order by {PK}";
		final List<EnumerationValue> jaloResultList = FlexibleSearch.getInstance().search(searchQueryJalo, EnumerationValue.class)
				.getResult();
		assertFalse(jaloResultList.isEmpty());

		final String searchQuery = "SELECT {PK},{approvalStatus},{code} FROM {Product} order by {PK}";

		final FlexibleSearchQuery fsq1 = new FlexibleSearchQuery(searchQuery);
		fsq1.setResultClassList(Arrays.asList(ProductModel.class, EnumerationValue.class, String.class));

		final FlexibleSearchQuery fsq2 = new FlexibleSearchQuery(searchQuery);
		fsq2.setResultClassList(Arrays.asList(ProductModel.class, EnumerationValueModel.class, String.class));

		final FlexibleSearchQuery fsq3 = new FlexibleSearchQuery(searchQuery);
		fsq3.setResultClassList(Arrays.asList(ProductModel.class, ArticleApprovalStatus.class, String.class));

		// execute test
		final SearchResult<List<?>> sr1 = flexibleSearchService.search(fsq1);
		final SearchResult<List<?>> sr2 = flexibleSearchService.search(fsq2);
		final SearchResult<List<?>> sr3 = flexibleSearchService.search(fsq3);

		// compare result
		assertEquals(jaloResultList.size(), sr2.getTotalCount());
		assertEquals(jaloResultList.size(), sr2.getTotalCount());
		assertEquals(jaloResultList.size(), sr2.getTotalCount());

		for (int index = 0; index < jaloResultList.size(); index++)
		{
			assertTrue(sr1.getResult().get(index).get(1) instanceof EnumerationValueModel);
			assertEquals(jaloResultList.get(index).getCode(), ((EnumerationValueModel) sr1.getResult().get(index).get(1)).getCode());
			assertTrue(sr2.getResult().get(index).get(1) instanceof EnumerationValueModel);
			assertEquals(jaloResultList.get(index).getCode(), ((EnumerationValueModel) sr2.getResult().get(index).get(1)).getCode());
			assertTrue(sr3.getResult().get(index).get(1) instanceof HybrisEnumValue);
			assertEquals(jaloResultList.get(index).getCode(), ((HybrisEnumValue) sr3.getResult().get(index).get(1)).getCode());
		}
	}

	/**
	 * randomize the approval statuses for products
	 */
	protected void randomizeStatuses(final ArticleApprovalStatus... statuses)
	{
		final String searchQueryProduct = "SELECT {PK} FROM {Product} order by {PK}";
		final List<ProductModel> resultListProduct = flexibleSearchService.<ProductModel> search(searchQueryProduct).getResult();
		assertFalse(resultListProduct.isEmpty());
		List<ArticleApprovalStatus> enumVals = null;
		if (statuses == null || statuses.length == 0)
		{
			enumVals = Arrays.asList(ArticleApprovalStatus.APPROVED, ArticleApprovalStatus.CHECK, ArticleApprovalStatus.UNAPPROVED);
		}
		else
		{
			enumVals = Arrays.asList(statuses);
		}

		int index = 0;
		for (final ProductModel product : resultListProduct)
		{
			try
			{
				product.setApprovalStatus(enumVals.get(index++ % enumVals.size()));
				modelService.save(product);
			}
			catch (final SystemException e)
			{
				fail(e.getMessage());
			}
		}
	}

	@Test
	public void testPLA9192_ResultIsNullSearch() // see PLA-9192
	{
		final String query = "select distinct({passwordanswer}) from {customer}"; // this query (should) returns a single
																											// row with NULL

		// first jalo search - so should it be
		final List<?> jaloResultList = FlexibleSearch.getInstance().search(query, String.class).getResult();
		assertEquals(1, jaloResultList.size());
		assertNull(jaloResultList.get(0));

		// now the flexible search service
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query);
		final SearchResult<List<?>> searchresult = flexibleSearchService.search(fsq);
		assertEquals("model search result has no size of 1", jaloResultList.size(), searchresult.getTotalCount());

		// searchresult.getResult().get(0) will get the NPE exception which will be fixed with PLA-9192
		assertEquals("model search result does not contains null", jaloResultList.get(0), searchresult.getResult().get(0));
	}

	@Test
	public void testTranslate()
	{
		final String expectedTranslatedSqlQuery = "SELECT  item_t0.PK  FROM " + (getTablePrefix())
				+ "languages item_t0 WHERE ( item_t0.p_active  = ?) AND (item_t0.TypePkString=? ) order by  item_t0.PK ";
		final Boolean expectedFlag = Boolean.TRUE;

		final String query = "SELECT {PK} FROM {Language AS l} WHERE {l.active} = ?value order by {pk}";
		final Map<String, ?> values = ImmutableMap.of("value", expectedFlag);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query, values);
		final TranslationResult translationResult = flexibleSearchService.translate(searchQuery);

		Assertions.assertThat(translationResult).isNotNull();
		Assertions.assertThat(translationResult.getSQLQuery()).isEqualToIgnoringCase(expectedTranslatedSqlQuery);// intentional
																																					// case
																																					// insensitive

		Assertions.assertThat(translationResult.getSQLQueryParameters()).isNotNull();

		Assertions.assertThat(translationResult.getSQLQueryParameters().size()).isEqualTo(2);
		Assertions.assertThat(translationResult.getSQLQueryParameters().get(0)).isEqualTo(expectedFlag);
	}


	@Test
	public void shouldThrowFlexibleSearchExceptionFromSlayerPackageWhenTranslationWillThrowJaloException()
	{
		// given
		final String query = "SELECT {PK} FROM {FooBar AS l}";
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		try
		{
			// when
			flexibleSearchService.translate(searchQuery);
			fail("Should throw FlexibleSearchException from servicelayer package");
		}
		catch (final FlexibleSearchException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException.class);
		}
	}

	/**
	 * PLA-10781 test checks if the during flexibleSearchService.translate all needed attributes has been set up to local
	 * context
	 */
	@Test
	public void testTranslateWithCustomSession()
	{
		// local session attributtes
		// admin user
		final UserModel adminUser = userService.getAdminUser();
		final LanguageModel langEn = commonI18NService.getLanguage("en");

		MockitoAnnotations.initMocks(this);

		// mock session
		final MockSession mockSession = Mockito.spy(new MockSession());
		Mockito.when(mockSession.getAttribute(SessionContext.USER)).thenReturn(adminUser);
		Mockito.when(mockSession.getAttribute(SessionContext.LANGUAGE)).thenReturn(langEn);

		// mock QueryPreprocessorEngine
		final QueryPreprocessorRegistry queryPreprocessorEngine = Mockito.mock(QueryPreprocessorRegistry.class);
		Mockito.when(((DefaultFlexibleSearchService) flexibleSearchService).getQueryPreprocessorRegistry()).thenReturn(
				queryPreprocessorEngine);

		// mock sessionService
		final SessionService mockSessionService = Mockito.spy(sessionService);
		Mockito.when(mockSessionService.getCurrentSession()).thenReturn(mockSession);


		((DefaultFlexibleSearchService) flexibleSearchService).setSessionService(mockSessionService);

		// fillin query

		final String sqlQuery = "SELECT  item_t0.PK  FROM " + (getTablePrefix())
				+ "languages item_t0 WHERE (item_t0.TypePkString=? ) order by  item_t0.PK ";
		final String query = "SELECT {PK} FROM {Language AS l} order by {PK}";

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query, Collections.EMPTY_MAP);
		searchQuery.setUser(adminUser);
		searchQuery.setLanguage(langEn); // this one from basic.csv
		final Set<PK> catalogVersionPKSet = new HashSet<>();
		for (final CatalogModel cmodel : catalogService.getAllCatalogs())
		{
			final List<CatalogVersionModel> cvModelList = searchQuery.getCatalogVersions() == null ? new ArrayList<CatalogVersionModel>()
					: new ArrayList<>(searchQuery.getCatalogVersions());
			cvModelList.addAll(cmodel.getCatalogVersions());
			searchQuery.setCatalogVersions(cvModelList);
		}
		for (final CatalogVersionModel model : searchQuery.getCatalogVersions())
		{
			catalogVersionPKSet.add(model.getPk());
		}
		// reset spy
		Mockito.reset(userService);

		// tested call
		final TranslationResult translationResult = flexibleSearchService.translate(searchQuery);

		// main assert
		assertNotNull(translationResult);
		assertEquals(sqlQuery, translationResult.getSQLQuery());
		assertNotNull(translationResult.getSQLQueryParameters());
		assertEquals(1, translationResult.getSQLQueryParameters().size());
		// verify triggering all preprocessors
		verify(queryPreprocessorEngine, times(1)).executeAllPreprocessors(searchQuery);
	}

	@Test
	public void testSearchUnique()
	{
		final String query = "SELECT {pk} FROM {Product} WHERE {code} = ?code order by {pk}";
		final Map params = new HashMap<String, Object>(1);
		params.put("code", "testProduct4");
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		assertEquals("testProduct4", ((ProductModel) flexibleSearchService.searchUnique(flexibleSearchQuery)).getCode());

	}

	@Test(expected = de.hybris.platform.servicelayer.exceptions.ModelNotFoundException.class)
	public void testSearchUniqueFailsModelNotFound()
	{
		final String query = "SELECT {pk} FROM {Product} WHERE {code} = ?code order by {pk}";
		final Map params = new HashMap<String, Object>(1);
		params.put("code", "test");
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(params);
		flexibleSearchService.searchUnique(flexibleSearchQuery);
	}

	@Test(expected = de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException.class)
	public void testSearchUniqueFailsAmbiguousIdentifier()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Product}");
		flexibleSearchService.searchUnique(query);

	}

	@Test
	public void testFindByApprovalStatus()
	{
		randomizeStatuses(ArticleApprovalStatus.APPROVED);

		final Map params = new HashMap<String, Object>(1);
		params.put("status", ArticleApprovalStatus.APPROVED);
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Product} where {"
				+ ProductModel.APPROVALSTATUS + "} = ?status");
		query.addQueryParameters(params);
		final SearchResult<ProductModel> result = flexibleSearchService.search(query);
		Assert.assertNotNull(result.getResult());
		Assert.assertFalse(result.getResult().isEmpty());
		for (final ProductModel model : result.getResult())
		{
			Assert.assertEquals(ArticleApprovalStatus.APPROVED, model.getApprovalStatus());
		}
	}

	@Test
	public void shouldFindLanguageByItsLocalizedNames()
	{
		// given
		final LanguageModel languageModel_en = commonI18NService.getLanguage("en");
		final LanguageModel languageModel_de = commonI18NService.getLanguage("de");
		final String query = "SELECT {" + LanguageModel.PK + "} FROM {" + LanguageModel._TYPECODE + "} WHERE {name["
				+ languageModel_en.getPk().getLongValueAsString() + "]} = ?en AND {name["
				+ languageModel_de.getPk().getLongValueAsString() + "]} = ?de";
		final Map params = new HashMap<String, LanguageModel>();
		params.put("en", "English");
		params.put("de", "Englisch");
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, params);

		// when
		final SearchResult<LanguageModel> search = flexibleSearchService.search(fQuery);
		final List<LanguageModel> result = search.getResult();

		// then
		assertThat(result).isNotNull();
		assertThat(search.getResult()).hasSize(1);
		assertThat(result.get(0).getIsocode()).isEqualTo("en");
	}

	@Test
	public void shouldRespectSearchRestrictionsInTranslateMethod() throws Exception
	{
		// given
		createDefaultUsers();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		final PrincipalModel principal = userService.getUserForUID("ahertz");
		createRestriction(principal, composedTypeModel, "FooBarInActive", "foo != 'bar'", Boolean.TRUE);
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {Trigger} ORDER BY {PK}");
		query.setUser((UserModel) principal);
		query.setSessionSearchRestrictions(new SessionSearchRestriction("Foo", "baz > 'boom'", composedTypeModel));

		// when
		final TranslationResult translationResult = flexibleSearchService.translate(query);

		// then
		assertThat(translationResult.getSQLQuery()).isNotNull();
		assertThat(translationResult.getSQLQuery()).contains("foo != 'bar'");
	}

	private SearchRestrictionModel createRestriction(final PrincipalModel principal, final ComposedTypeModel type,
			final String code, final String query, final Boolean active)
	{
		final SearchRestrictionModel model = (SearchRestrictionModel) modelService.create(SearchRestrictionModel.class);
		model.setCode(code);
		model.setActive(active);
		model.setQuery(query);
		model.setRestrictedType(type);
		model.setPrincipal(principal);
		model.setGenerate(Boolean.TRUE);
		modelService.save(model);
		return model;
	}

	private String getTablePrefix()
	{
		return Registry.getCurrentTenantNoFallback().getDataSource().getTablePrefix();
	}
}
