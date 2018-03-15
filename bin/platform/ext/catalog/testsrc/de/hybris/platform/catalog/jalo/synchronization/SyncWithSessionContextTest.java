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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.Builder;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.ConfigureSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper.SyncOperation;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class SyncWithSessionContextTest extends ServicelayerTest
{
	private static final String CATALOG = "testCatalog";
	private static final String SRC_CATALOG_VERSION = "srcCatalog";
	private static final String DST_CATALOG_VERSION = "dstCatalog";
	private static final String CATEGORY = "category";
	private static final String SUB_CATEGORY = "subcategory";
	private static final String PRODUCT1 = "product1";
	private static final String PRODUCT2 = "product2";

	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Test
	public void shouldSynchronizeAllCategoriesAndNotSettingPrincipalsRecursively() throws Exception
	{
		givenTestCatalogWithVersions();
		performSynchronization(null);

		//first sync - without principals
		assertThat(allProductsFor(dstCatalogVersion())).isNotNull().hasSize(2);
		assertThat(allCategoriesFor(dstCatalogVersion())).isNotNull().hasSize(2);
		assertThat(product1From(dstCatalogVersion())).isNotNull();
		assertThat(product2From(dstCatalogVersion())).isNotNull();
		assertThat(categoryFrom(dstCatalogVersion())).isNotNull();
		assertThat(categoryChildFrom(dstCatalogVersion())).isNotNull();
		Thread.sleep(5000L);

		assertThat(categoryFrom(dstCatalogVersion()).getAllowedPrincipals()).hasSize(0);
		assertThat(categoryChildFrom(dstCatalogVersion()).getAllowedPrincipals()).hasSize(0);

		//adding principals - only for supercategory
		final CustomerModel customerJan = modelService.create(CustomerModel.class);
		customerJan.setUid("Jan");
		final CustomerModel customerPiotr = modelService.create(CustomerModel.class);
		customerPiotr.setUid("Piotr");
		categoryFrom(srcCatalogVersion()).setAllowedPrincipals(Arrays.<PrincipalModel> asList(customerJan, customerPiotr));
		modelService.saveAll();
		categoryChildFrom(srcCatalogVersion()).setAllowedPrincipals(new ArrayList<PrincipalModel>());
		modelService.saveAll();

		assertThat(categoryFrom(srcCatalogVersion()).getAllowedPrincipals()).hasSize(2);
		assertThat(categoryChildFrom(srcCatalogVersion()).getAllowedPrincipals()).hasSize(0);
		//second sync - now check the principals
		performSynchronization(null);
		assertThat(categoryFrom(dstCatalogVersion()).getAllowedPrincipals()).hasSize(2);
		assertThat(categoryChildFrom(dstCatalogVersion()).getAllowedPrincipals()).hasSize(0);
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

		final CategoryModel category1Child = modelService.create(CategoryModel.class);
		category1Child.setCatalogVersion(sourceVersion);
		category1Child.setCode(SUB_CATEGORY);
		category1Child.setSupercategories(Arrays.asList(category1));

		final ProductModel p1 = modelService.create(ProductModel.class);
		p1.setCatalogVersion(sourceVersion);
		p1.setCode(PRODUCT1);
		p1.setSupercategories(Arrays.asList(category1));

		final ProductModel p2 = modelService.create(ProductModel.class);
		p2.setCatalogVersion(sourceVersion);
		p2.setCode(PRODUCT2);
		p2.setSupercategories(Arrays.asList(category1));

		category1.setProducts(Arrays.asList(p1, p2));
		category1.setCategories(Arrays.asList(category1Child));
		modelService.saveAll();
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

	private CategoryModel categoryChildFrom(final CatalogVersionModel catalogVersion)
	{
		final CategoryModel example = new CategoryModel();
		example.setCatalogVersion(catalogVersion);
		example.setCode(SUB_CATEGORY);
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
}
