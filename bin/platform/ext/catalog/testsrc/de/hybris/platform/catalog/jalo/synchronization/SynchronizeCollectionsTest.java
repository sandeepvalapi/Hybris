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
package de.hybris.platform.catalog.jalo.synchronization;

import static de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.create;
import static de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.remove;
import static de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.update;
import static java.util.Objects.requireNonNull;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.Builder;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.ConfigureSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.SyncOperation;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class SynchronizeCollectionsTest extends ServicelayerTest
{
	private static final String CATALOG = "testCatalog";
	private static final String SRC_CATALOG_VERSION = "srcCatalog";
	private static final String DST_CATALOG_VERSION = "dstCatalog";
	private static final String CATEGORY = "category";
	private static final String PRODUCT1 = "product1";
	private static final String PRODUCT2 = "product2";

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private TypeService typeService;

	@Test
	public void shouldSynchronizeAllProductsAndCategories() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization();

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(2);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(product2From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(product2From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(2)
				.containsOnly(product1From(dstCatalogVersion()), product2From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeSingleCategoryWithoutProducts() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization(create(categoryFrom(srcCatalogVersion())));

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().isEmpty();
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().isEmpty();
	}

	@Test
	public void shouldSynchronizeCategoryWithAllProducts() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization( //
				create(categoryFrom(srcCatalogVersion())), //
				create(product1From(srcCatalogVersion())), //
				create(product2From(srcCatalogVersion())) //
		);

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(2);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(product2From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(product2From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(2)
				.containsOnly(product1From(dstCatalogVersion()), product2From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeCategoryWithSingleProduct() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization( //
				create(categoryFrom(srcCatalogVersion())), //
				create(product1From(srcCatalogVersion())) //
		);

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(1)
				.containsOnly(product1From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeProductWithCategoryAndThenCategory() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization( //
				create(categoryFrom(srcCatalogVersion())), //
				create(product1From(srcCatalogVersion())) //
		);
		performSynchronization(update(categoryFrom(srcCatalogVersion()), categoryFrom(dstCatalogVersion())));

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(1)
				.containsOnly(product1From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeProductAndThenCategoryWhenPartialTranslationIsEnabled() throws Exception
	{
		runWithPartialTranslationEnabled(new Runnable()
		{
			@Override
			public void run()
			{
				givenTestCatalogWithVersions();

				performSynchronization(create(product1From(srcCatalogVersion())));
				performSynchronization(create(categoryFrom(srcCatalogVersion())));

				assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(product1From(dstCatalogVersion())).isNotNull();
				assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
				assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
						.containsOnly(categoryFrom(dstCatalogVersion()));
				assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(1)
						.containsOnly(product1From(dstCatalogVersion()));
			}
		});
	}

	@Test
	public void shouldSynchronizeProductAndThenCategoryWhenPartialTranslationIsDisabled() throws Exception
	{
		runWithPartialTranslationDisabled(new Runnable()
		{
			@Override
			public void run()
			{
				givenTestCatalogWithVersions();

				performSynchronization(create(product1From(srcCatalogVersion())));
				performSynchronization(create(categoryFrom(srcCatalogVersion())));

				assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(product1From(dstCatalogVersion())).isNotNull();
				assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
				assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().isEmpty();
				assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().isEmpty();
			}
		});
	}

	@Test
	public void shouldSynchronizeCategoryWithProductAndThenCategoryWithOtherProduct() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization( //
				create(categoryFrom(srcCatalogVersion())), //
				create(product1From(srcCatalogVersion())) //
		);

		performSynchronization( //
				update(categoryFrom(srcCatalogVersion()), categoryFrom(dstCatalogVersion())), //
				create(product2From(srcCatalogVersion())) //
		);

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(2);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(product2From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(product2From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(2)
				.containsOnly(product1From(dstCatalogVersion()), product2From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeCategoryAndProductsAndThenRemoveProduct() throws Exception
	{
		givenTestCatalogWithVersions();

		performSynchronization( //
				create(categoryFrom(srcCatalogVersion())), //
				create(product1From(srcCatalogVersion())), //
				create(product2From(srcCatalogVersion())) //
		);
		performSynchronization(remove(product2From(dstCatalogVersion())));

		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
				.containsOnly(categoryFrom(dstCatalogVersion()));
		assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(1)
				.containsOnly(product1From(dstCatalogVersion()));
	}

	@Test
	public void shouldSynchronizeCategoryAndPartialProductsWhenGlobalPartialTranslationIsDisabledButIsEnabledForProductsAttribute()
			throws Exception
	{
		runWithPartialTranslationDisabled(new Runnable()
		{
			@Override
			public void run()
			{
				givenTestCatalogWithVersions();

				performSynchronization(create(product1From(srcCatalogVersion())));
				performSynchronization( //
						configureAttributeToBePartiallyTranslated(CategoryModel._TYPECODE, CategoryModel.PRODUCTS), //
						create(categoryFrom(srcCatalogVersion())));

				assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(product1From(dstCatalogVersion())).isNotNull();
				assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
				assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().hasSize(1)
						.containsOnly(categoryFrom(dstCatalogVersion()));
				assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().hasSize(1)
						.containsOnly(product1From(dstCatalogVersion()));
			}
		});
	}

	@Test
	public void shouldSynchronizeCategoryWithoutPartialProductsWhenGlobalPartialTranslationIsEnabledButIsDisabledForProductsAttribute()
			throws Exception
	{
		runWithPartialTranslationEnabled(new Runnable()
		{
			@Override
			public void run()
			{
				givenTestCatalogWithVersions();

				performSynchronization(create(product1From(srcCatalogVersion())));
				performSynchronization( //
						configureAttributeToNotBePartiallyTranslated(CategoryModel._TYPECODE, CategoryModel.PRODUCTS), //
						create(categoryFrom(srcCatalogVersion())));

				assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(1);
				assertThat(product1From(dstCatalogVersion())).isNotNull();
				assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
				assertThat(product1From(dstCatalogVersion()).getSupercategories()).isNotNull().isEmpty();
				assertThat(categoryFrom(dstCatalogVersion()).getProducts()).isNotNull().isEmpty();
			}
		});
	}

	private void runWithPartialTranslationEnabled(final Runnable action)
	{
		runWithPartialTranslationFlagSetTo(true, action);
	}

	private void runWithPartialTranslationDisabled(final Runnable action)
	{
		runWithPartialTranslationFlagSetTo(false, action);
	}

	private void runWithPartialTranslationFlagSetTo(final boolean value, final Runnable action)
	{
		final String key = "synchronization.allow.partial.collection.translation";
		final String valueToResotre = Config.getParameter(key);
		Config.setParameter(key, Boolean.toString(value));
		try
		{
			action.run();
		}
		finally
		{
			Config.setParameter(key, valueToResotre);
		}
	}

	private void givenTestCatalogWithVersions()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(CATALOG);

		final CatalogVersionModel sourceVersion = modelService.create(CatalogVersionModel.class);
		sourceVersion.setCatalog(catalog);
		sourceVersion.setVersion(SRC_CATALOG_VERSION);

		final CatalogVersionModel targetVersion = modelService.create(CatalogVersionModel.class);
		targetVersion.setCatalog(catalog);
		targetVersion.setVersion(DST_CATALOG_VERSION);

		final CategoryModel category1 = modelService.create(CategoryModel.class);
		category1.setCatalogVersion(sourceVersion);
		category1.setCode(CATEGORY);

		final ProductModel p1 = modelService.create(ProductModel.class);
		p1.setCatalogVersion(sourceVersion);
		p1.setCode(PRODUCT1);
		p1.setSupercategories(Arrays.asList(category1));

		final ProductModel p2 = modelService.create(ProductModel.class);
		p2.setCatalogVersion(sourceVersion);
		p2.setCode(PRODUCT2);
		p2.setSupercategories(Arrays.asList(category1));

		category1.setProducts(Arrays.asList(p1, p2));

		modelService.saveAll();
	}

	private void performSynchronization(final SyncOperation... operations)
	{
		performSynchronization(null, operations);
	}

	private void performSynchronization(final ConfigureSyncCronJob configure, final SyncOperation... operations)
	{
		final Builder builder = SynchronizationTestHelper.builder(srcCatalogVersion(), dstCatalogVersion());

		final SynchronizationTestHelper helper = builder.configure(configure).add(operations).build();

		helper.performSynchronization();
	}

	private CatalogModel testCatalog()
	{
		final CatalogModel example = new CatalogModel();
		example.setId(CATALOG);
		return flexibleSearchService.getModelByExample(example);
	}

	private CatalogVersionModel srcCatalogVersion()
	{
		final CatalogVersionModel example = new CatalogVersionModel();
		example.setCatalog(testCatalog());
		example.setVersion(SRC_CATALOG_VERSION);
		return flexibleSearchService.getModelByExample(example);
	}

	private CatalogVersionModel dstCatalogVersion()
	{
		final CatalogVersionModel example = new CatalogVersionModel();
		example.setCatalog(testCatalog());
		example.setVersion(DST_CATALOG_VERSION);
		return flexibleSearchService.getModelByExample(example);
	}

	private Collection<ProductModel> allProductsFor(final CatalogVersionModel catalogVersion)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(catalogVersion);
		return flexibleSearchService.getModelsByExample(example);
	}

	private Collection<CategoryModel> allCategoriesFor(final CatalogVersionModel catalogVersion)
	{
		final CategoryModel example = new CategoryModel();
		example.setCatalogVersion(catalogVersion);
		return flexibleSearchService.getModelsByExample(example);
	}

	private CategoryModel categoryFrom(final CatalogVersionModel catalogVersion)
	{
		final CategoryModel example = new CategoryModel();
		example.setCatalogVersion(catalogVersion);
		example.setCode(CATEGORY);
		return flexibleSearchService.getModelByExample(example);
	}

	private ProductModel product1From(final CatalogVersionModel catalogVersion)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(catalogVersion);
		example.setCode(PRODUCT1);
		return flexibleSearchService.getModelByExample(example);
	}

	private ProductModel product2From(final CatalogVersionModel catalogVersion)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(catalogVersion);
		example.setCode(PRODUCT2);
		return flexibleSearchService.getModelByExample(example);
	}

	private ConfigureSyncCronJob configureAttributeToBePartiallyTranslated(final String typeCode, final String qualifier)
	{
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(typeCode, qualifier);
		return new ConfigurePartialTranslationForAttribute(attributeDescriptor, Boolean.TRUE);
	}

	private ConfigureSyncCronJob configureAttributeToNotBePartiallyTranslated(final String typeCode, final String qualifier)
	{
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(typeCode, qualifier);
		return new ConfigurePartialTranslationForAttribute(attributeDescriptor, Boolean.FALSE);
	}

	private class ConfigurePartialTranslationForAttribute implements ConfigureSyncCronJob
	{
		private final AttributeDescriptorModel attributeDescriptor;
		private final Boolean partialTranslationValue;

		public ConfigurePartialTranslationForAttribute(final AttributeDescriptorModel attributeDescriptor,
				final Boolean partialTranslationValue)
		{
			this.attributeDescriptor = requireNonNull(attributeDescriptor);
			this.partialTranslationValue = partialTranslationValue;
		}

		@Override
		public void configure(final CatalogVersionSyncCronJob job)
		{
			final SyncAttributeDescriptorConfigModel attributeConfig = modelService.create(SyncAttributeDescriptorConfigModel.class);
			attributeConfig.setAttributeDescriptor(attributeDescriptor);
			attributeConfig.setSyncJob(findSyncJobItemModel(requireNonNull(job)));
			attributeConfig.setPartiallyTranslatable(partialTranslationValue);
			modelService.save(attributeConfig);
		}

		private SyncItemJobModel findSyncJobItemModel(final CatalogVersionSyncCronJob job)
		{
			final SyncItemJobModel syncItemJob = new SyncItemJobModel();
			syncItemJob.setCode(job.getJob().getCode());
			return flexibleSearchService.getModelByExample(syncItemJob);
		}

	}
}
