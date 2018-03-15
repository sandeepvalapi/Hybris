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

import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.catalog.job.diff.CatalogVersionDifferenceFinder;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.catalog.model.ProductCatalogVersionDifferenceModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * General difference finder for a different {@link ProductModel}s between {@link CompareCatalogVersionsCronJobModel}s
 * source and target {@link CatalogVersionModel}.
 */
abstract public class AbstractProductCatalogVersionDiffFinder implements
		CatalogVersionDifferenceFinder<ProductModel, ProductCatalogVersionDifferenceModel>
{

	protected ModelService modelService;

	private FlexibleSearchService flexibleSearchService;

	private final static int DEFAULT_RANGE = 1000;

	private int range = DEFAULT_RANGE;

	protected EnumerationService enumerationService;

	public void setRange(final int range)
	{
		this.range = range;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	@Override
	public int processDifferences(final CompareCatalogVersionsCronJobModel model)
	{
		Collection<ProductModel> differencedProducts;
		int differencedProductsCount = 0;
		int processedStepsCounter = 0;
		int start = 0;
		if (shouldProcess(model))
		{
			do
			{

				differencedProducts = findDifferences(start, range, model);

				differencedProductsCount += differencedProducts.size();
				start += range;

				for (final ProductModel productModel : differencedProducts)
				{
					final ProductCatalogVersionDifferenceModel difference = populateDifferenceModel(productModel, null, model);
					modelService.save(difference);
					processedStepsCounter++; // see CompareCatalogVersionWizard#pollStatus
				}
			}
			while (differencedProducts.size() == range);
			setDifferencesCount(model, differencedProductsCount);
		}
		return processedStepsCounter;
	}

	/**
	 * Method decides if to process differences or not depending on the model's flag
	 * {@link CompareCatalogVersionsCronJobModel#getMissingProducts()},
	 * {@link CompareCatalogVersionsCronJobModel#getSearchNewProducts()}
	 */
	abstract protected boolean shouldProcess(CompareCatalogVersionsCronJobModel model);

	/**
	 * Method to reflect differences into {@link CronJobModel}.
	 */
	abstract protected void setDifferencesCount(final CompareCatalogVersionsCronJobModel model, int differencedProductsCount);


	/**
	 * Abstraction for a getting collection of different {@link ProductModel} between
	 * {@link CompareCatalogVersionsCronJobModel}s source and target {@link CatalogVersionModel}.
	 */
	abstract protected Collection<ProductModel> findDifferences(final int start, final int count,
			final CompareCatalogVersionsCronJobModel model);

	/**
	 * Provides a {@link ProductDifferenceMode} instance to be specialized in any subtype.
	 */
	abstract protected ProductDifferenceMode getProductDifferenceMode();


	abstract protected ProductCatalogVersionDifferenceModel populateDifferenceModel(final ProductModel srcProduct,
			final ProductModel targetProduct, final CompareCatalogVersionsCronJobModel model);


	/**
	 * Searches for a difference between two catalog versions.
	 * <p>
	 * For looking new products use source/target as version1/version2 catalog version.
	 * <p>
	 * For looking removed products use target/source as version1/version2 catalog version
	 */
	protected Collection<ProductModel> search4Diffs(final int start, final int count, final CatalogVersionModel version1,
			final CatalogVersionModel version2)
	{

		final Map values = new HashMap();
		values.put("version1", version1);
		values.put("version2", version2);

		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {p1:" + ItemModel.PK + "} FROM {"
				+ ProductModel._TYPECODE + " AS p1} " //
				+ "WHERE NOT EXISTS ({{" //
				+ "SELECT {p2:" + ItemModel.PK + "} FROM {" + ProductModel._TYPECODE + " AS p2} " //
				+ "WHERE {p2:" + ProductModel.CATALOGVERSION + "} = ?version1 " //
				+ "AND {p2:" + ProductModel.CODE + "} = {p1:" + ProductModel.CODE + "} " //
				+ "}}) " //
				+ "AND {p1:" + ProductModel.CATALOGVERSION + "} = ?version2 "//
		, values);
		query.setCount(count);
		query.setStart(start);

		final SearchResult<ProductModel> result = flexibleSearchService.search(query);

		return result.getResult();
	}



}
