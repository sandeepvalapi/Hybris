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
package de.hybris.platform.misc;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.search.internal.resolver.impl.DefaultModelResolver;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;




@IntegrationTest
public class LazyListTest extends ServicelayerTransactionalTest
{
	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	ModelService modelService;

	@Resource
	UserService userService;

	@Before
	public void setUp() throws Exception
	{
		final Language language1 = C2LManager.getInstance().createLanguage("falseLang");
		language1.setActive(false);
	}

	/**
	 * PLA-8723, HOR-538
	 */
	@Test
	public void testLazyListContains() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		final String query = "SELECT {pk} FROM {Product} where {code} like ?code order by {pk}";

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter("code", "testProduct_");

		final SearchResult<ProductModel> modelSearchResult = flexibleSearchService.search(searchQuery);

		// Following step is to get a list with unique products
		final List<ProductModel> productsFromCategory = new ArrayList(new HashSet(
				productService.getProductsForCategory(categoryService.getCategory("testCategory0"))));

		de.hybris.platform.testframework.Assert.assertCollection("Found products are not identical", modelSearchResult.getResult(),
				productsFromCategory);

		//compare different collection with the same size 

		final String query2 = "SELECT {pk} FROM {Product} where {code} like ?code order by {pk}";

		final FlexibleSearchQuery searchQuery2 = new FlexibleSearchQuery(query2);
		searchQuery2.addQueryParameter("code", "testProduct__");
		searchQuery2.setCount(productsFromCategory.size());

		final SearchResult<ProductModel> modelSearchResult2 = flexibleSearchService.search(searchQuery2);

		//productsFromCategory.add(modelSearchResult2.getResult().get(0)); //add some other item 

		assertFalse("After removing shouldn't containsAll be true ",
				modelSearchResult2.getResult().containsAll(productsFromCategory));
	}

	@Test
	public void testPaging()
	{
		final UserModel user = userService.getCurrentUser();
		final PK uPK = user.getPk();

		final int PAGE_SIZE = 10;
		final int listSize = (PAGE_SIZE * 10) + 5;

		final List<PK> pkList = new ArrayList<PK>(listSize);
		for (int i = 0; i < listSize; i++)
		{
			pkList.add(uPK);
		}

		final DefaultModelResolver modelResolver = new DefaultModelResolver();
		modelResolver.setModelService(modelService);

		final TestLazyModelList lazyLoadModelList = new TestLazyModelList(new LazyLoadItemList<User>(null, pkList, PAGE_SIZE),
				PAGE_SIZE, (List) Collections.singletonList(UserModel.class), modelResolver);

		Object buffer = null;
		for (int page = 0; page < 10; page++)
		{
			final int start = page * PAGE_SIZE;
			final int end = Math.min(listSize, start + PAGE_SIZE);

			final Object previousRunBuffer = buffer;
			buffer = null;

			for (int index = start; index < end; index++)
			{
				assertEquals(user, lazyLoadModelList.get(index));
				if (buffer == null)
				{
					buffer = lazyLoadModelList.getBuffer();
					if (previousRunBuffer != null)
					{
						assertNotSame(previousRunBuffer, buffer);
					}
				}
				else
				{
					assertSame(buffer, lazyLoadModelList.getBuffer());
				}
			}
		}
	}

	static class TestLazyModelList extends LazyLoadModelList
	{
		public TestLazyModelList(final LazyLoadItemList llitemlist, final int prefetchsize, final List<Class> expectedClassList,
				final ItemObjectResolver itemResolver)
		{
			super(llitemlist, prefetchsize, expectedClassList, itemResolver);
		}

		// for accessing buffer directly
		Object getBuffer()
		{
			return getCurrentBufferedPage();
		}
	}
}
