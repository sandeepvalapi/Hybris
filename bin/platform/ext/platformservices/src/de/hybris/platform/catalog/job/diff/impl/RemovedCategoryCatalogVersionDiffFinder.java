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

import de.hybris.platform.catalog.enums.CategoryDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CategoryCatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.category.model.CategoryModel;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;


/**
 * Implementation for a finding removed {@link CategoryModel} between two {@link CatalogVersionModel}s of given
 * {@link CompareCatalogVersionsCronJobModel}.
 */
public class RemovedCategoryCatalogVersionDiffFinder extends AbstractCategoryCatalogVersionDiffFinder
{

	private static final Logger LOG = Logger.getLogger(RemovedCategoryCatalogVersionDiffFinder.class.getName());

	@Override
	protected CatalogVersionModel getSourceCatalogVersion(final CompareCatalogVersionsCronJobModel model)
	{
		return model.getTargetVersion();
	}

	@Override
	protected CategoryDifferenceMode getCategoryDifferenceMode()
	{
		final CategoryDifferenceMode categoryRemoved = (CategoryDifferenceMode) enumerationService.getEnumerationValue(
				CategoryDifferenceMode.CATEGORY_REMOVED.getType(), CategoryDifferenceMode.CATEGORY_REMOVED.getCode());

		return categoryRemoved;
	}

	@Override
	protected CatalogVersionModel getTargetCatalogVersion(final CompareCatalogVersionsCronJobModel model)
	{
		return model.getSourceVersion();
	}


	@Override
	public CategoryCatalogVersionDifferenceModel populateDifferenceModel(final CategoryModel srcCategory,
			final CategoryModel targetCategory, final CompareCatalogVersionsCronJobModel model)
	{
		LOG.info("Category " + srcCategory.getCode() + " new in version: " + model.getTargetVersion().getVersion());

		final CategoryCatalogVersionDifferenceModel differenceModel = modelService
				.create(CategoryCatalogVersionDifferenceModel.class);
		differenceModel.setSourceVersion(model.getSourceVersion());
		differenceModel.setTargetVersion(model.getTargetVersion());
		differenceModel.setCronJob(model);
		differenceModel.setMode(getCategoryDifferenceMode());

		differenceModel.setSourceCategory(null);
		differenceModel.setTargetCategory(srcCategory);

		differenceModel.setDifferenceText("Category " + srcCategory.getCode() + " new in version: "
				+ model.getTargetVersion().getVersion());

		return differenceModel;
	}

	@Override
	protected boolean shouldProcess(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		return BooleanUtils.isTrue(cronJobModel.getSearchMissingCategories());
	}
}
