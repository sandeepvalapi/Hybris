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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;


@IntegrationTest
public class FlexibleSearchServiceSpecialCasesTest extends ServicelayerTransactionalTest
{
	private final static Logger LOG = Logger.getLogger(FlexibleSearchServiceSpecialCasesTest.class);

	@Resource
	@Spy
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
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


	}

	/**
	 * This test works only (and will only executed) when no attribute is preloaded
	 */
	@Test
	public void testSwitchingBufferPage()
	{

		if (isPrefetchModeNone())
		{
			LOG.warn("Could not execute test testSwitchingBufferPage() because no preloading of the model attributes is enabled!");
		}
		else
		{
			final int modelPrefetchSize = 10;
			final String oldPageSize = Config.getParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY);
			Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, String.valueOf(modelPrefetchSize));
			try
			{
				final String query = "SELECT {pk} FROM {Product} order by {pk}";
				final List<Product> jaloResultList = FlexibleSearch.getInstance().search(query, Product.class).getResult();
				assertTrue(modelPrefetchSize + 2 < jaloResultList.size()); //the result list must be greater than the prefetch size - for testing the page switch

				final Product bufferedZeroProduct = jaloResultList.get(0); //filling the prefetchedList
				final Product bufferedWillBeChangedProduct = jaloResultList.get(3); //NOPMD this will be modified
				final Product notYetBufferedProduct = jaloResultList.get(modelPrefetchSize + 2); //this is outside the buffered model size

				final SearchResult<ProductModel> modelSearchResult = flexibleSearchService.search(query);
				final List<ProductModel> modelResultList = modelSearchResult.getResult();

				assertEquals(bufferedZeroProduct, modelService.getSource(modelResultList.get(0))); //this line loads the prefetched modelbuffer

				bufferedWillBeChangedProduct.setCode("XXX");
				notYetBufferedProduct.setCode("YYY");

				final ProductModel inTheBuffer = modelResultList.get(3);
				final ProductModel outTheBuffer = modelResultList.get(modelPrefetchSize + 2);

				assertNotSame(bufferedWillBeChangedProduct.getCode(), inTheBuffer.getCode());
				assertEquals("YYY", outTheBuffer.getCode());
			}
			finally
			{
				if (oldPageSize != null)
				{
					Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, oldPageSize);
				}
			}
		}
	}

	/**
	 * This method tests the search for special characters, specifically '\u00DF' - for PLA-11184
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchForSpecialCharacters() throws Exception
	{
		final String query = "SELECT {pk} FROM {Product} WHERE {code} = ?code order by {pk}";
		final Map params = new HashMap<String, Object>(2);
		params.put("code", "HW1100-0024");

		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query);
		fsq.addQueryParameters(params);

		final de.hybris.platform.servicelayer.search.SearchResult<ProductModel> modelSearchResult = flexibleSearchService
				.search(fsq);
		final List<ProductModel> modelResultList = modelSearchResult.getResult();

		assertEquals(1, modelResultList.size());

		final ProductModel productModel = modelResultList.iterator().next();

		productModel.setCode("\u00DF");

		modelService.save(productModel);

		final String searchQueryProduct = "SELECT {PK} FROM {Product} WHERE {code} LIKE '\u00DF%' GROUP BY {PK} ORDER BY {PK} DESC";

		final List<ProductModel> resultListProduct = flexibleSearchService.<ProductModel> search(searchQueryProduct).getResult();

		assertEquals(1, resultListProduct.size());

	}
}
