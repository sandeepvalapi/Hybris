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
package de.hybris.platform.catalog.job;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Ignore;


/**
 * Base for integration test for a migrated catalog/catalog version related jobs.
 */
@Ignore
abstract public class AbstractJobIntegrationTest<T> extends ServicelayerTest
{
	protected static final String CATEGORY_CODE_3 = "minorGreenCategory";
	protected static final String CATEGORY_CODE_2 = "mainGreenCategory";
	protected static final String CATEGORY_CODE_1 = "rootGreenCategory";
	protected static final String PRODUCT_CODE_3 = "sampleMinorGreenProduct";
	protected static final String PRODUCT_CODE_2 = "sampleMajorGreenProduct";
	protected static final String PRODUCT_CODE_1 = "sampleRootProduct";
	protected static final String SOURCE_CATALOGVERSION = "greenVille";
	protected static final String MAIN_CATALOG = "differencesMainVilleCatalog";
	protected CatalogVersionModel source;
	protected CatalogModel mainCatalog;

	@Resource
	protected ModelService modelService;

	@Resource
	protected CronJobService cronJobService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	protected CommonI18NService commonI18NService;

	protected void setUp()
	{
		mainCatalog = modelService.create(CatalogModel.class);
		mainCatalog.setId(MAIN_CATALOG);
		modelService.save(mainCatalog);


		source = modelService.create(CatalogVersionModel.class);
		source.setVersion(SOURCE_CATALOGVERSION);
		source.setCatalog(mainCatalog);
		modelService.save(source);

	}

	protected void addCategoriesAndProducts(final CatalogVersionModel catalogVersion)
	{
		final CategoryModel rootCategory = modelService.create(CategoryModel.class);
		rootCategory.setCode(CATEGORY_CODE_1);
		rootCategory.setCatalogVersion(catalogVersion);


		final CategoryModel mainCategory = modelService.create(CategoryModel.class);
		mainCategory.setCode(CATEGORY_CODE_2);
		mainCategory.setCatalogVersion(catalogVersion);
		mainCategory.setSupercategories(Arrays.asList(rootCategory));

		final CategoryModel minorCategory = modelService.create(CategoryModel.class);
		minorCategory.setCode(CATEGORY_CODE_3);
		minorCategory.setCatalogVersion(catalogVersion);
		minorCategory.setSupercategories(Arrays.asList(rootCategory));

		modelService.saveAll(rootCategory, mainCategory, minorCategory);

		final ProductModel rootProduct = modelService.create(ProductModel.class);
		rootProduct.setCode(PRODUCT_CODE_1);
		rootCategory.setProducts(Arrays.asList(rootProduct));
		rootProduct.setCatalogVersion(catalogVersion);

		final ProductModel majorGreenProduct = modelService.create(ProductModel.class);
		majorGreenProduct.setCode(PRODUCT_CODE_2);
		mainCategory.setProducts(Arrays.asList(majorGreenProduct));
		majorGreenProduct.setCatalogVersion(catalogVersion);

		final ProductModel minorGreenProduct = modelService.create(ProductModel.class);
		minorGreenProduct.setCode(PRODUCT_CODE_3);
		minorCategory.setProducts(Arrays.asList(minorGreenProduct));
		minorGreenProduct.setCatalogVersion(catalogVersion);

		modelService.saveAll(rootProduct, majorGreenProduct, minorGreenProduct);

		catalogVersion.setRootCategories(Arrays.asList(rootCategory));
	}


	protected ProductModel getProductByCode(final String code)
	{
		final ProductModel template = new ProductModel();
		template.setCode(code);
		return flexibleSearchService.getModelByExample(template);
	}

	protected T getPerformable(final CronJobModel cronJobModel)
	{
		final T performable = (T) cronJobService.getPerformable((ServicelayerJobModel) cronJobModel.getJob());
		return performable;
	}

}
