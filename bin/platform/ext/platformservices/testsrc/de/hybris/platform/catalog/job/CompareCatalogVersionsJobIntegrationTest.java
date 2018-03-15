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


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.CategoryDifferenceMode;
import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CategoryCatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.catalog.model.ProductCatalogVersionDifferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Test covering {@link CompareCatalogVersionsJobPerformable} logic.
 */
@IntegrationTest
public class CompareCatalogVersionsJobIntegrationTest extends AbstractJobIntegrationTest<CompareCatalogVersionsJobPerformable>
{
	private static final String TARGET_CATALOGVERSION = "redVille";
	private CatalogVersionModel target;

	//unit
	private UnitModel unit;

	//prepare data functions
	@Before
	public void prepare()
	{

		setUp();
		target = modelService.create(CatalogVersionModel.class);
		target.setVersion(TARGET_CATALOGVERSION);
		target.setCatalog(mainCatalog);
		mainCatalog.setCatalogVersions(new HashSet<CatalogVersionModel>(Arrays.asList(target, source)));
		modelService.saveAll(target, source, mainCatalog);

		addCategoriesAndProducts(source);
		modelService.save(source);
		prepareUnit();
	}

	private void prepareUnit()
	{
		unit = modelService.create(UnitModel.class);
		unit.setCode("specialunit");
		unit.setConversion(Double.valueOf(1.0));
		unit.setName("special Unit", Locale.ENGLISH);
		unit.setUnitType("something");

		modelService.save(unit);
	}

	private void modifyApprovalStatuses(final ProductModel[] products, final ArticleApprovalStatus... statuses)
	{
		Assert.assertEquals(products.length, statuses.length);

		if (statuses.length > 0 && products.length > 0)
		{
			products[0].setApprovalStatus(statuses[0]);
			modelService.save(products[0]);
			modelService.refresh(products[0]);
		}


		if (statuses.length > 1 && products.length > 1)
		{
			products[1].setApprovalStatus(statuses[1]);
			modelService.save(products[1]);
			modelService.refresh(products[1]);
		}

		if (statuses.length > 2 && products.length > 2)
		{
			products[2].setApprovalStatus(statuses[2]);
			modelService.save(products[2]);
			modelService.refresh(products[2]);
		}
	}

	private void addSomePriceInformations(final CatalogVersionModel catalogVersion, final ProductModel[] products,
			final Double... prices)
	{

		Assert.assertEquals(prices.length, products.length);
		if (prices.length > 0 && products.length > 0)
		{
			final PriceRowModel rootPricerow = modelService.create(PriceRowModel.class);
			//rootPricerow.setCurrency(commonI18NService.getCurrency(Locale.TAIWAN.getISO3Language()));
			rootPricerow.setCurrency(commonI18NService.getCurrentCurrency());
			rootPricerow.setMinqtd(Long.valueOf(1));
			rootPricerow.setNet(Boolean.TRUE);
			rootPricerow.setPrice(prices[0]);
			rootPricerow.setUnit(unit);
			rootPricerow.setProduct(products[0]); //root product
			rootPricerow.setCatalogVersion(catalogVersion);

			modelService.save(rootPricerow);
		}

		if (prices.length > 1 && products.length > 1)
		{

			final PriceRowModel pricerowSubOne = modelService.create(PriceRowModel.class);
			//pricerowSubOne.setCurrency(commonI18NService.getCurrency(Locale.CANADA_FRENCH.getISO3Language()));//dulary
			pricerowSubOne.setCurrency(commonI18NService.getCurrentCurrency());
			pricerowSubOne.setMinqtd(Long.valueOf(1));
			pricerowSubOne.setNet(Boolean.TRUE);
			pricerowSubOne.setPrice(prices[1]);
			pricerowSubOne.setUnit(unit);
			pricerowSubOne.setProduct(products[1]); //1st
			pricerowSubOne.setCatalogVersion(catalogVersion);

			modelService.save(pricerowSubOne);
		}

		if (prices.length > 2 && products.length > 2)
		{

			final PriceRowModel pricerowSubTwo = modelService.create(PriceRowModel.class);
			//pricerowSubTwo.setCurrency(commonI18NService.getCurrency(Locale.TAIWAN.getISO3Language()));//dukaty
			pricerowSubTwo.setCurrency(commonI18NService.getCurrentCurrency());
			pricerowSubTwo.setMinqtd(Long.valueOf(1));
			pricerowSubTwo.setNet(Boolean.TRUE);
			pricerowSubTwo.setPrice(prices[2]);
			pricerowSubTwo.setUnit(unit/* unitEvenMoreSpecial */);
			pricerowSubTwo.setProduct(products[2]); //1st
			pricerowSubTwo.setCatalogVersion(catalogVersion);

			modelService.save(pricerowSubTwo);
		}
	}

	/**
	 * configuration for a {@link CompareCatalogVersionsCronJobModel} to run only products comparation phase
	 */
	private void prepareForProductsCompare(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		cronJobModel.setSearchNewProducts(Boolean.TRUE);
		cronJobModel.setSearchMissingProducts(Boolean.TRUE);

		cronJobModel.setSearchNewCategories(Boolean.FALSE);
		cronJobModel.setSearchMissingCategories(Boolean.FALSE);

		cronJobModel.setSearchPriceDifferences(Boolean.FALSE);
		cronJobModel.setOverwriteProductApprovalStatus(Boolean.FALSE);


	}

	/**
	 * configuration for a {@link CompareCatalogVersionsCronJobModel} to run only categories comparation phase
	 */
	private void prepareForCategoriesCompare(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		cronJobModel.setSearchNewProducts(Boolean.FALSE);
		cronJobModel.setSearchMissingProducts(Boolean.FALSE);

		cronJobModel.setSearchNewCategories(Boolean.TRUE);
		cronJobModel.setSearchMissingCategories(Boolean.TRUE);

		cronJobModel.setSearchPriceDifferences(Boolean.FALSE);
		cronJobModel.setOverwriteProductApprovalStatus(Boolean.FALSE);

	}

	/**
	 * configuration for a {@link CompareCatalogVersionsCronJobModel} to run only price comparation phase
	 */
	private void prepareForPriceDifferencesCompare(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		cronJobModel.setSearchNewProducts(Boolean.FALSE);
		cronJobModel.setSearchMissingProducts(Boolean.FALSE);

		cronJobModel.setSearchNewCategories(Boolean.FALSE);
		cronJobModel.setSearchMissingCategories(Boolean.FALSE);

		cronJobModel.setSearchPriceDifferences(Boolean.TRUE);
		cronJobModel.setOverwriteProductApprovalStatus(Boolean.FALSE);

	}

	/**
	 * configuration for a {@link CompareCatalogVersionsCronJobModel} to run only approval status phase
	 */
	private void prepareForStatusCompare(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		cronJobModel.setSearchNewProducts(Boolean.FALSE);
		cronJobModel.setSearchMissingProducts(Boolean.FALSE);

		cronJobModel.setSearchNewCategories(Boolean.FALSE);
		cronJobModel.setSearchMissingCategories(Boolean.FALSE);

		cronJobModel.setSearchPriceDifferences(Boolean.FALSE);
		cronJobModel.setOverwriteProductApprovalStatus(Boolean.TRUE);

	}

	/**
	 * Common creation of {@link CronJobModel} with assigned {@link ServicelayerJobModel} instance.
	 */
	private CompareCatalogVersionsCronJobModel createCronJob(final CatalogVersionModel sourceVersion,
			final CatalogVersionModel targetVersion)
	{
		//final CompareCatalogVersionsJobModel jobModel = modelService.create(CompareCatalogVersionsJobModel.class);
		final ServicelayerJobModel jobModel = modelService.create(ServicelayerJobModel.class);
		jobModel.setCode("compareMightyCatalogsTestJob");
		jobModel.setSpringId("compareCatalogVersionsJobPerformable");

		modelService.save(jobModel);

		final CompareCatalogVersionsCronJobModel cronJobModel = modelService.create(CompareCatalogVersionsCronJobModel.class);
		cronJobModel.setJob(jobModel);
		cronJobModel.setCode("compareMightyCatalogsTestCronJob");
		cronJobModel.setSourceVersion(sourceVersion);
		cronJobModel.setTargetVersion(targetVersion);

		modelService.save(cronJobModel);
		return cronJobModel;
	}

	//Test cases
	@Test
	public void testCompareForProductsWithEmptyTargetCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(source, target);
		prepareForProductsCompare(cronJobModel);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(3, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel differencesRemoved = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		differencesRemoved.setCronJob(cronJobModel);
		differencesRemoved.setMode(ProductDifferenceMode.PRODUCT_REMOVED);

		final List<ProductCatalogVersionDifferenceModel> executionRemovedDifferences = flexibleSearchService
				.getModelsByExample(differencesRemoved);

		Assert.assertEquals(3, executionRemovedDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(source, executionRemovedDifferences.get(i).getSourceVersion());
			Assert.assertEquals(target, executionRemovedDifferences.get(i).getTargetVersion());
			Assert.assertNull(executionRemovedDifferences.get(i).getDifferenceValue());
		}

		validateContainsDifferenceText(executionRemovedDifferences,
				"Product sampleMajorGreenProduct not found in version: redVille");
		validateContainsDifferenceText(executionRemovedDifferences,
				"Product sampleMinorGreenProduct not found in version: redVille");
		validateContainsDifferenceText(executionRemovedDifferences, "Product sampleRootProduct not found in version: redVille");

		validateContainsProductPair(executionRemovedDifferences, new ProductModel[]
		{ findProduct(source, PRODUCT_CODE_3), null });
		validateContainsProductPair(executionRemovedDifferences, new ProductModel[]
		{ findProduct(source, PRODUCT_CODE_1), null });
		validateContainsProductPair(executionRemovedDifferences, new ProductModel[]
		{ findProduct(source, PRODUCT_CODE_2), null });

		final ProductCatalogVersionDifferenceModel differencesNew = modelService.create(ProductCatalogVersionDifferenceModel.class);
		differencesNew.setCronJob(cronJobModel);
		differencesNew.setMode(ProductDifferenceMode.PRODUCT_NEW);

		final List<ProductCatalogVersionDifferenceModel> executionNewDiffernces = flexibleSearchService
				.getModelsByExample(differencesNew);

		Assert.assertEquals(0, executionNewDiffernces.size());
		modelService.refresh(cronJobModel);
		Assert.assertEquals("Processed step counter for performable should be 6", 6, cronJobModel.getProcessedItemsCount());
	}

	@Test
	public void testCompareForProductsWithEmptySourceCatalogVersion()
	{

		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(target, source);
		prepareForProductsCompare(cronJobModel);

		modelService.save(cronJobModel);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(3, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel differencesRemoved = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		differencesRemoved.setCronJob(cronJobModel);
		differencesRemoved.setMode(ProductDifferenceMode.PRODUCT_REMOVED);

		final List<ProductCatalogVersionDifferenceModel> executionRemovedDifferences = flexibleSearchService
				.getModelsByExample(differencesRemoved);

		Assert.assertEquals(0, executionRemovedDifferences.size());

		final ProductCatalogVersionDifferenceModel differencesNew = modelService.create(ProductCatalogVersionDifferenceModel.class);
		differencesNew.setCronJob(cronJobModel);
		differencesNew.setMode(ProductDifferenceMode.PRODUCT_NEW);

		final List<ProductCatalogVersionDifferenceModel> executionNewDifferences = flexibleSearchService
				.getModelsByExample(differencesNew);

		Assert.assertEquals(3, executionNewDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(target, executionNewDifferences.get(i).getSourceVersion());
			Assert.assertEquals(source, executionNewDifferences.get(i).getTargetVersion());
			Assert.assertNull(executionNewDifferences.get(i).getDifferenceValue());
		}

		validateContainsDifferenceText(executionNewDifferences, "Product sampleMinorGreenProduct new in version: greenVille");
		validateContainsDifferenceText(executionNewDifferences, "Product sampleMajorGreenProduct new in version: greenVille");
		validateContainsDifferenceText(executionNewDifferences, "Product sampleRootProduct new in version: greenVille");

		validateContainsProductPair(executionNewDifferences, new ProductModel[]
		{ null, findProduct(source, PRODUCT_CODE_3) });
		validateContainsProductPair(executionNewDifferences, new ProductModel[]
		{ null, findProduct(source, PRODUCT_CODE_1) });
		validateContainsProductPair(executionNewDifferences, new ProductModel[]
		{ null, findProduct(source, PRODUCT_CODE_2) });

		modelService.refresh(cronJobModel);

		Assert.assertEquals("Processed step counter for performable should be 3", 3, cronJobModel.getProcessedItemsCount());
	}

	@Test
	public void testCompareForCategoriesWithEmptyTargetCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(source, target);
		prepareForCategoriesCompare(cronJobModel);
		modelService.save(cronJobModel);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel anyProductDifference = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		anyProductDifference.setCronJob(cronJobModel);

		final List<ProductCatalogVersionDifferenceModel> executionProductDifferences = flexibleSearchService
				.getModelsByExample(anyProductDifference);

		Assert.assertEquals(0, executionProductDifferences.size());

		final CategoryCatalogVersionDifferenceModel catDifferencesNew = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		catDifferencesNew.setCronJob(cronJobModel);
		catDifferencesNew.setMode(CategoryDifferenceMode.CATEGORY_NEW);

		final List<CategoryCatalogVersionDifferenceModel> executionNewDifferences = flexibleSearchService
				.getModelsByExample(catDifferencesNew);

		Assert.assertEquals(3, executionNewDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(source, executionNewDifferences.get(i).getSourceVersion());
			Assert.assertEquals(target, executionNewDifferences.get(i).getTargetVersion());
			Assert.assertNull(executionNewDifferences.get(i).getDifferenceValue());
		}

		validateContainsDifferenceText(executionNewDifferences, "Category mainGreenCategory not found in version: redVille");
		validateContainsDifferenceText(executionNewDifferences, "Category minorGreenCategory not found in version: redVille");
		validateContainsDifferenceText(executionNewDifferences, "Category rootGreenCategory not found in version: redVille");

		validateContainsCatalogPair(executionNewDifferences, new CategoryModel[]
		{ findCategory(source, CATEGORY_CODE_1), null });
		validateContainsCatalogPair(executionNewDifferences, new CategoryModel[]
		{ findCategory(source, CATEGORY_CODE_2), null });
		validateContainsCatalogPair(executionNewDifferences, new CategoryModel[]
		{ findCategory(source, CATEGORY_CODE_3), null });


		final CategoryCatalogVersionDifferenceModel catDifferencesRemoved = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		catDifferencesRemoved.setCronJob(cronJobModel);
		catDifferencesRemoved.setMode(CategoryDifferenceMode.CATEGORY_REMOVED);

		final List<CategoryCatalogVersionDifferenceModel> executionRemovedDifferences = flexibleSearchService
				.getModelsByExample(catDifferencesRemoved);

		Assert.assertEquals(0, executionRemovedDifferences.size());

		modelService.refresh(cronJobModel);

		Assert.assertEquals("Processed step counter for performable should be 3", 3, cronJobModel.getProcessedItemsCount());
	}


	@Test
	public void testCompareForCategoriesWithEmptySourceCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(target, source);
		prepareForCategoriesCompare(cronJobModel);
		modelService.save(cronJobModel);

		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();
		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel anyProductDifference = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		anyProductDifference.setCronJob(cronJobModel);


		final List<ProductCatalogVersionDifferenceModel> executionProductDifferences = flexibleSearchService
				.getModelsByExample(anyProductDifference);

		Assert.assertEquals(0, executionProductDifferences.size());

		final CategoryCatalogVersionDifferenceModel catDifferencesNew = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		catDifferencesNew.setCronJob(cronJobModel);
		catDifferencesNew.setMode(CategoryDifferenceMode.CATEGORY_NEW);

		final List<CategoryCatalogVersionDifferenceModel> executionNewDifferences = flexibleSearchService
				.getModelsByExample(catDifferencesNew);

		Assert.assertEquals(0, executionNewDifferences.size());

		final CategoryCatalogVersionDifferenceModel catDifferencesRemoved = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		catDifferencesRemoved.setCronJob(cronJobModel);
		catDifferencesRemoved.setMode(CategoryDifferenceMode.CATEGORY_REMOVED);

		final List<CategoryCatalogVersionDifferenceModel> executionRemovedDifferences = flexibleSearchService
				.getModelsByExample(catDifferencesRemoved);

		Assert.assertEquals(3, executionRemovedDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(target, executionRemovedDifferences.get(i).getSourceVersion());
			Assert.assertEquals(source, executionRemovedDifferences.get(i).getTargetVersion());
			Assert.assertNull(executionRemovedDifferences.get(i).getDifferenceValue());
		}

		validateContainsDifferenceText(executionRemovedDifferences, "Category mainGreenCategory new in version: greenVille");
		validateContainsDifferenceText(executionRemovedDifferences, "Category minorGreenCategory new in version: greenVille");
		validateContainsDifferenceText(executionRemovedDifferences, "Category rootGreenCategory new in version: greenVille");

		validateContainsCatalogPair(executionRemovedDifferences, new CategoryModel[]
		{ null, findCategory(source, CATEGORY_CODE_1) });
		validateContainsCatalogPair(executionRemovedDifferences, new CategoryModel[]
		{ null, findCategory(source, CATEGORY_CODE_2) });
		validateContainsCatalogPair(executionRemovedDifferences, new CategoryModel[]
		{ null, findCategory(source, CATEGORY_CODE_3) });

		modelService.refresh(cronJobModel);

		Assert.assertEquals("Processed step counter for performable should be 3", 3, cronJobModel.getProcessedItemsCount());
	}

	@Test
	public void testCompareForPricesWithEmptyTargetCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(source, target);

		addCategoriesAndProducts(target);//move some products /categories into target instance scope products gets overwritten
		final ProductModel[] products = new ProductModel[]
		{ findProduct(source, PRODUCT_CODE_1), findProduct(source, PRODUCT_CODE_2), findProduct(source, PRODUCT_CODE_3) };

		addSomePriceInformations(source, products, Double.valueOf(1.234), Double.valueOf(12.34), Double.valueOf(123.4));
		prepareForPriceDifferencesCompare(cronJobModel);

		modelService.save(cronJobModel);
		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel anyProductDifference = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		anyProductDifference.setCronJob(cronJobModel);
		anyProductDifference.setMode(ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE);

		final List<ProductCatalogVersionDifferenceModel> executionDifferences = flexibleSearchService
				.getModelsByExample(anyProductDifference);

		Assert.assertEquals(3, executionDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(source, executionDifferences.get(i).getSourceVersion());
			Assert.assertEquals(target, executionDifferences.get(i).getTargetVersion());
			Assert.assertNull(executionDifferences.get(i).getDifferenceValue());
			validateContainsDifferenceText(executionDifferences, "Difference in price info count! oldPrices: 1 newPrices: 0");
		}
		modelService.refresh(cronJobModel);
		Assert.assertEquals("Processed step counter for performable should be 3", 3, cronJobModel.getProcessedItemsCount());
	}

	@Test
	public void testCompareForPricesWithNotEmptySourceAndTargetCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(target, source);

		final String message = "Difference above max tolerance ( %s ) in price: (new: %s old: %s for PriceInfo:";

		final ProductModel[] productsSource = new ProductModel[]
		{ findProduct(source, PRODUCT_CODE_1), findProduct(source, PRODUCT_CODE_2), findProduct(source, PRODUCT_CODE_3) };

		final Map<PK, Object[]> validateMapping = new HashMap<PK, Object[]>(3);
		validateMapping.put(productsSource[0].getPk(), new Object[]
		{ String.format(message, Double.valueOf(0.0), Double.valueOf(50), Double.valueOf(100)), Double.valueOf(50) });//
		validateMapping.put(productsSource[1].getPk(), new Object[]
		{ String.format(message, Double.valueOf(0.0), Double.valueOf(10), Double.valueOf(5)), Double.valueOf(100) });//
		validateMapping.put(productsSource[2].getPk(), new Object[]
		{ String.format(message, Double.valueOf(0.0), Double.valueOf(1), Double.valueOf(2)), Double.valueOf(50) });//

		addCategoriesAndProducts(target);//move some products /categories into target instance scope products gets overwritten
		addSomePriceInformations(source, productsSource,//
				Double.valueOf(50), Double.valueOf(10), Double.valueOf(1));//prices

		addSomePriceInformations(target, new ProductModel[]
		{ findProduct(target, PRODUCT_CODE_1), findProduct(target, PRODUCT_CODE_2), findProduct(target, PRODUCT_CODE_3) },//
				Double.valueOf(100), Double.valueOf(5), Double.valueOf(2)); //prices

		prepareForPriceDifferencesCompare(cronJobModel);

		modelService.save(cronJobModel);
		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());
		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel anyProductDifference = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		anyProductDifference.setCronJob(cronJobModel);
		anyProductDifference.setMode(ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE);

		final List<ProductCatalogVersionDifferenceModel> executionDifferences = flexibleSearchService
				.getModelsByExample(anyProductDifference);

		Assert.assertEquals(3, executionDifferences.size());

		for (int i = 0; i < 3; i++)
		{
			Assert.assertEquals(target, executionDifferences.get(i).getSourceVersion());
			Assert.assertEquals(source, executionDifferences.get(i).getTargetVersion());
			Assert.assertNotNull(executionDifferences.get(i).getSourceProduct());
			Assert.assertNotNull(executionDifferences.get(i).getTargetProduct());
			final PK pkSource = executionDifferences.get(i).getSourceProduct().getPk();
			final Object[] dataToValidate = validateMapping.get(pkSource); //get text ,difference value
			Assert.assertEquals(dataToValidate[1], executionDifferences.get(i).getDifferenceValue());
			validateContainsDifferenceText(executionDifferences, dataToValidate[0].toString());
		}
		modelService.refresh(cronJobModel);

		Assert.assertEquals("Processed step counter for performable should be 3", 3, cronJobModel.getProcessedItemsCount());
	}

	@Test
	public void testCompareForStatusesWithNotEmptySourceAndTargetCatalogVersion()
	{
		final CompareCatalogVersionsCronJobModel cronJobModel = createCronJob(source, target);

		final ProductModel sourceProduct1 = findProduct(source, PRODUCT_CODE_1);
		final ProductModel sourceProduct2 = findProduct(source, PRODUCT_CODE_2);
		final ProductModel sourceProduct3 = findProduct(source, PRODUCT_CODE_3);

		addCategoriesAndProducts(target);//move some products /categories into target instance scope products gets overwritten

		modifyApprovalStatuses(new ProductModel[]
		{ sourceProduct1, sourceProduct2, sourceProduct3 }, ArticleApprovalStatus.APPROVED, ArticleApprovalStatus.CHECK,
				ArticleApprovalStatus.UNAPPROVED);

		final ProductModel targetProduct1 = findProduct(target, PRODUCT_CODE_1);
		final ProductModel targetProduct2 = findProduct(target, PRODUCT_CODE_2);
		final ProductModel targetProduct3 = findProduct(target, PRODUCT_CODE_3);

		modifyApprovalStatuses(new ProductModel[]
		{ targetProduct1, targetProduct2, targetProduct3 }, ArticleApprovalStatus.CHECK, ArticleApprovalStatus.UNAPPROVED,
				ArticleApprovalStatus.APPROVED);

		prepareForStatusCompare(cronJobModel);

		modelService.save(cronJobModel);
		cronJobService.performCronJob(cronJobModel, true);

		final CronJobResult result = cronJobModel.getResult();

		modelService.refresh(cronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result);
		Assert.assertEquals(CronJobStatus.FINISHED, cronJobModel.getStatus());

		Assert.assertEquals(0, cronJobModel.getNewProducts());
		Assert.assertEquals(0, cronJobModel.getMissingProducts());

		final ProductCatalogVersionDifferenceModel anyProductDifference = modelService
				.create(ProductCatalogVersionDifferenceModel.class);
		anyProductDifference.setCronJob(cronJobModel);

		final List<ProductCatalogVersionDifferenceModel> executionProductDifferences = flexibleSearchService
				.getModelsByExample(anyProductDifference);

		Assert.assertEquals(0, executionProductDifferences.size());

		final CategoryCatalogVersionDifferenceModel anyCategoryDifference = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		anyCategoryDifference.setCronJob(cronJobModel);

		final List<CategoryCatalogVersionDifferenceModel> executionCataegoryDifferences = flexibleSearchService
				.getModelsByExample(anyCategoryDifference);

		Assert.assertEquals(0, executionCataegoryDifferences.size());
		Assert.assertEquals(findProduct(source, PRODUCT_CODE_1).getApprovalStatus(), ArticleApprovalStatus.APPROVED);
		Assert.assertEquals(findProduct(source, PRODUCT_CODE_2).getApprovalStatus(), ArticleApprovalStatus.CHECK);
		Assert.assertEquals(findProduct(source, PRODUCT_CODE_3).getApprovalStatus(), ArticleApprovalStatus.UNAPPROVED);

	}

	private CategoryModel findCategory(final CatalogVersionModel version, final String code)
	{
		final CategoryModel example = new CategoryModel();
		example.setCatalogVersion(version);
		example.setCode(code);

		final CategoryModel result = flexibleSearchService.getModelByExample(example);
		Assert.assertNotNull("Should found " + code + " for cv " + version, result);
		return result;
	}

	private ProductModel findProduct(final CatalogVersionModel version, final String code)
	{
		final ProductModel example = new ProductModel();
		example.setCatalogVersion(version);
		example.setCode(code);

		final ProductModel result = flexibleSearchService.getModelByExample(example);
		Assert.assertNotNull("Should found " + code + " for cv " + version, result);
		return result;
	}


	//validator functions
	private void validateContainsProductPair(final List<ProductCatalogVersionDifferenceModel> differences,
			final ProductModel[] expectedProductPair)
	{
		for (final ProductCatalogVersionDifferenceModel difference : differences)
		{
			final Object originalSource = difference.getSourceProduct() == null ? "" : difference.getSourceProduct();
			final Object expectedSource = expectedProductPair[0] == null ? "" : expectedProductPair[0];

			final Object originalTarget = difference.getTargetProduct() == null ? "" : difference.getTargetProduct();
			final Object expectedTarget = expectedProductPair[1] == null ? "" : expectedProductPair[1];


			if (originalSource.equals(expectedSource) && originalTarget.equals(expectedTarget))
			{
				return;
			}
		}
		Assert.fail("There should be found product pair (source,target) = (" + expectedProductPair[0] + ","
				+ expectedProductPair[1] + ") in a differences " + differences);

	}

	private void validateContainsDifferenceText(final List<? extends CatalogVersionDifferenceModel> differences,
			final String expectedText)
	{
		for (final CatalogVersionDifferenceModel difference : differences)
		{
			final String originalSource = difference.getDifferenceText() == null ? "" : difference.getDifferenceText();
			final String expectedSource = expectedText == null ? "" : expectedText;

			if (originalSource.contains(expectedSource))
			{
				return;
			}
		}
		Assert.fail("There should be difference text (" + expectedText + ") in a differences " + differences);

	}

	private void validateContainsCatalogPair(final List<CategoryCatalogVersionDifferenceModel> differences,
			final CategoryModel[] expectedCategoryPair)
	{
		for (final CategoryCatalogVersionDifferenceModel difference : differences)
		{
			final Object originalSource = difference.getSourceCategory() == null ? "" : difference.getSourceCategory();
			final Object expectedSource = expectedCategoryPair[0] == null ? "" : expectedCategoryPair[0];

			final Object originalTarget = difference.getTargetCategory() == null ? "" : difference.getTargetCategory();
			final Object expectedTarget = expectedCategoryPair[1] == null ? "" : expectedCategoryPair[1];


			if (originalSource.equals(expectedSource) && originalTarget.equals(expectedTarget))
			{
				return;
			}
		}
		Assert.fail("There should be found category pair (source,target) = (" + expectedCategoryPair[0] + ","
				+ expectedCategoryPair[1] + ") in a differences " + differences);
	}


}
