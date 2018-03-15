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
package de.hybris.platform.jalo.link;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.test.RunnerCreator;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@PerformanceTest
public class LinkManagerTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(LinkManagerTest.class);

	@Resource
	private ModelService modelService;
	private CategoryModel category;
	private Category categoryItem;
	private final LinkManager linkManager = LinkManager.getInstance();

	@Before
	public void setUp() throws Exception
	{
		final TestDataCreator creator = new TestDataCreator(modelService);
		final CatalogVersionModel catalogVersion = creator.createCatalogVersion("test", creator.createCatalog());
		category = creator.createCategory(catalogVersion);
		categoryItem = modelService.getSource(category);

		IntStream.range(0, 500).forEach(i -> {
			ProductModel product = creator.createProduct(catalogVersion);
			product.setSupercategories(Lists.newArrayList(category));
		});

		modelService.saveAll();
	}

	@Test
	public void readLinkedItemsOldWay() throws Exception
	{
		final RunnerCreator<Runnable> runnerCreator = threadNumber -> () -> IntStream.range(0, 10000).forEach(
				i -> linkManager.getLinkedItems(JaloSession.getCurrentSession().getSessionContext(), categoryItem, true,
						"CategoryProductRelation", null, true, 0, -1, true, false));

		final TestThreadsHolder<Runnable> holder = new TestThreadsHolder<>(100, runnerCreator, true);
		holder.startAll();
		holder.waitAndDestroy(100);

		LOG.info("Old Way -- Score: " + holder.getStartToFinishMillis());
	}



	@Test
	public void readLinkedItemsNewWay() throws Exception
	{
		final RunnerCreator<Runnable> runnerCreator = threadNumber -> () -> IntStream.range(0, 10000).forEach(
				i -> linkManager.getLinkedItems(JaloSession.getCurrentSession().getSessionContext(), category.getPk(), true, null,
						"CategoryProductRelation", "Product", 0, -1, true, false));

		final TestThreadsHolder<Runnable> holder = new TestThreadsHolder<>(100, runnerCreator, true);
		holder.startAll();
		holder.waitAndDestroy(100);

		LOG.info("New Way -- Score: " + holder.getStartToFinishMillis());
	}

	public class TestDataCreator
	{
		private final ModelService modelService;

		public TestDataCreator(final ModelService modelService)
		{
			this.modelService = modelService;
		}

		public CatalogModel createCatalog()
		{
			return createCatalog(uniqueId());
		}

		public CatalogModel createCatalog(final String id)
		{
			final CatalogModel catalog = modelService.create(CatalogModel.class);
			catalog.setId(id);

			modelService.save(catalog);

			return catalog;
		}

		public CatalogVersionModel createCatalogVersion(final String version, final CatalogModel catalog)
		{
			final CatalogVersionModel ctgVer = modelService.create(CatalogVersionModel.class);
			ctgVer.setVersion(version);
			ctgVer.setCatalog(catalog);

			modelService.save(ctgVer);

			return ctgVer;
		}

		public ProductModel createProduct(final CatalogVersionModel catalogVersion)
		{
			return createProduct(uniqueId(), randomStringOfLength(8), randomStringOfLength(30), catalogVersion);
		}

		public ProductModel createProduct(final String code, final String name, final String description,
				final CatalogVersionModel catalogVersion)
		{
			final ProductModel product = modelService.create(ProductModel.class);
			product.setCode(code);
			product.setName(name);
			product.setCatalogVersion(catalogVersion);
			product.setDescription(description);

			modelService.save(product);

			return product;
		}

		public CategoryModel createCategory(final CatalogVersionModel catalogVersion, final ProductModel... products)
		{
			return createCategory(uniqueId(), catalogVersion, Arrays.asList(products));
		}

		public CategoryModel createCategory(final String code, final CatalogVersionModel catalogVersion,
				final List<ProductModel> products)
		{
			final CategoryModel category = modelService.create(CategoryModel.class);
			category.setCode(code);
			category.setCatalogVersion(catalogVersion);
			category.setProducts(products);

			modelService.save(category);

			return category;
		}

		private String uniqueId()
		{
			return UUID.randomUUID().toString();
		}

		private String randomStringOfLength(final int lenght)
		{
			return RandomStringUtils.randomAlphabetic(lenght);
		}
	}
}
