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

import de.hybris.platform.catalog.job.CompareCatalogVersionsJobPerformable;
import de.hybris.platform.catalog.job.diff.CatalogVersionDifferenceFinder;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.catalog.model.ProductCatalogVersionDifferenceModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Difference finder for a {@link ProductModel#getApprovalStatus()}'s between two different
 * {@link CompareCatalogVersionsCronJobModel}'s <code>catalogVersions</code>.
 */
public class ProductApprovalStatusDiffFinder implements
		CatalogVersionDifferenceFinder<ProductModel, CatalogVersionDifferenceModel>
{

	private static final Logger LOG = Logger.getLogger(CompareCatalogVersionsJobPerformable.class.getName());

	private static final int DEFAULT_BLOCK_SIZE = 100;

	private final int productsBlockSize = 10 * DEFAULT_BLOCK_SIZE;

	private ModelService modelService;

	private FlexibleSearchService flexibleSearchService;

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	@Override
	public int processDifferences(final CompareCatalogVersionsCronJobModel cronJob)
	{
		Collection<ProductModel> productsToSave;
		Collection<List<ProductModel>> products;
		int start = 0;
		int processedStepsCounter = 0;
		if (shouldProcess(cronJob))
		{
			do
			{
				products = getProductsToOverwriteApprovalStatus(start, productsBlockSize, cronJob.getSourceVersion(),
						cronJob.getTargetVersion(), cronJob);
				start += productsBlockSize;
				productsToSave = new ArrayList<ProductModel>(productsBlockSize);

				for (final Iterator<List<ProductModel>> it = products.iterator(); it.hasNext();)
				{
					final List<ProductModel> productList = it.next();
					final ProductModel productFromSourceVersion = productList.get(0);
					final ProductModel productFromTargetVersion = productList.get(1);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Setting approval status of product: " + productFromTargetVersion + " from "
								+ productFromSourceVersion + " to " + productFromSourceVersion.getApprovalStatus());
					}
					productFromTargetVersion.setApprovalStatus(productFromSourceVersion.getApprovalStatus());
					productsToSave.add(productFromTargetVersion);
					processedStepsCounter++;
				}
				modelService.saveAll(productsToSave);
			}
			while (products.size() == productsBlockSize);
		}
		return processedStepsCounter;
	}


	/**
	 * Returns true if product approval status should be done.
	 */
	private boolean shouldProcess(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		return BooleanUtils.isTrue(cronJobModel.getOverwriteProductApprovalStatus());
	}

	private Collection<List<ProductModel>> getProductsToOverwriteApprovalStatus(final int start, final int count,
			final CatalogVersionModel sourceVersion, final CatalogVersionModel targetVersion, final CronJobModel cronJob)
	{

		final String queryString = "SELECT {p1:" + ItemModel.PK + "} as pk1, {p2:" + ItemModel.PK + "} as pk2 " + "FROM {"
				+ ProductModel._TYPECODE + " AS p1 JOIN " + ProductModel._TYPECODE + " AS p2 ON {p1:" + ProductModel.CODE
				+ "} = {p2:" + ProductModel.CODE + "} " + "AND {p1:" + ProductModel.CATALOGVERSION + "} = ?version1 " + "AND {p2:"
				+ ProductModel.CATALOGVERSION + "} = ?version2 } " + "WHERE not exists ( {{ select * from {"
				+ ProductCatalogVersionDifferenceModel._TYPECODE + " AS c} " + "WHERE {c:"
				+ ProductCatalogVersionDifferenceModel.SOURCEPRODUCT + "} = {p1:" + ItemModel.PK + "} " + "AND {c:"
				+ ProductCatalogVersionDifferenceModel.TARGETPRODUCT + "} = {p2:" + ItemModel.PK + "} " + "AND {c:"
				+ CatalogVersionDifferenceModel.CRONJOB + "} = ?cronjob }} )  " + "AND {p1:" + ProductModel.APPROVALSTATUS
				+ "} <> {p2:" + ProductModel.APPROVALSTATUS + "}";
		final Map params = new HashMap();
		params.put("version1", sourceVersion);
		params.put("version2", targetVersion);
		params.put("cronjob", cronJob);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, params);
		query.setCount(count);
		query.setStart(start);
		query.setResultClassList(Arrays.asList(new Class[]
		{ ProductModel.class, ProductModel.class }));

		final SearchResult<List<ProductModel>> result = flexibleSearchService.search(query);
		return result.getResult();
	}

}
