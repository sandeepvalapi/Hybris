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
import de.hybris.platform.catalog.job.diff.CatalogVersionDifferenceFinder;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CategoryCatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Required;


/**
 * General difference finder for a different {@link CategoryModel}s between {@link CompareCatalogVersionsCronJobModel}s
 * source and target {@link CatalogVersionModel}.
 */
abstract public class AbstractCategoryCatalogVersionDiffFinder implements
		CatalogVersionDifferenceFinder<CategoryModel, CategoryCatalogVersionDifferenceModel>
{
	protected ModelService modelService;

	private static final int DEFAULT_BLOCK_SIZE = 100;

	private int blockSize = DEFAULT_BLOCK_SIZE;

	protected EnumerationService enumerationService;

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public void setBlockSize(final int blockSize)
	{
		this.blockSize = blockSize;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * Creates a difference object of type <code>CategoryCatalogVersionDifferenceModel</code> between two
	 * <code>CategoryModel</code>s.
	 */
	abstract protected CategoryCatalogVersionDifferenceModel populateDifferenceModel(final CategoryModel srcCategory,
			final CategoryModel targetCategory, final CompareCatalogVersionsCronJobModel model);

	@Override
	public int processDifferences(final CompareCatalogVersionsCronJobModel model)
	{
		int processedStepsCounter = 0;
		if (shouldProcess(model))
		{
			final int categoriesCount = getAllCategoryCount(getSourceCatalogVersion(model));
			// find  categories
			for (int i = 0; i <= categoriesCount; i = i + blockSize)
			{
				final Collection<CategoryModel> categories = getAllCategories(getSourceCatalogVersion(model), i, blockSize);
				for (final Iterator<CategoryModel> it = categories.iterator(); it.hasNext();)
				{
					final CategoryModel categoryModel = it.next();
					final Collection<CategoryModel> sameCategories = getSameCategories(getTargetCatalogVersion(model), categoryModel);
					if (sameCategories == null || sameCategories.isEmpty())
					{

						final CategoryCatalogVersionDifferenceModel differenceModel = populateDifferenceModel(categoryModel, null,
								model);

						modelService.save(differenceModel);
						processedStepsCounter++; // see CompareCatalogVersionWizard#pollStatus
					}
				}
			}
		}
		return processedStepsCounter;

	}

	/**
	 * Method decides if to process differences or not depending on the model's flag
	 * {@link CompareCatalogVersionsCronJobModel#getSearchMissingCategories()},
	 * {@link CompareCatalogVersionsCronJobModel#getSearchNewCategories()}
	 */
	abstract protected boolean shouldProcess(CompareCatalogVersionsCronJobModel model);

	/**
	 * gets source catalog version for the cronjob model ( compare source->target , target->source) while checking
	 * new/removed categories
	 */
	abstract protected CatalogVersionModel getSourceCatalogVersion(CompareCatalogVersionsCronJobModel model);

	/**
	 * gets source catalog version for the cronjob model ( compare source->target , target->source) while checking
	 * new/removed categories
	 */
	abstract protected CatalogVersionModel getTargetCatalogVersion(CompareCatalogVersionsCronJobModel model);

	/**
	 * Provides a {@link CategoryDifferenceMode} instance to be specialized in any subtype.
	 */
	abstract protected CategoryDifferenceMode getCategoryDifferenceMode();

	/**
	 * TODO JALO logic
	 */
	protected Collection<CategoryModel> getSameCategories(final CatalogVersionModel cmodel, final CategoryModel catModel)
	{
		final de.hybris.platform.catalog.jalo.CatalogVersion versionJalo = modelService.getSource(cmodel);
		final Collection result = versionJalo.getSameCategories((de.hybris.platform.category.jalo.Category) modelService
				.getSource(catModel));
		final Collection<CategoryModel> modelResult = new ArrayList<CategoryModel>(result.size());
		modelService.getAll(result, modelResult);
		return modelResult;
	}

	/**
	 * TODO JALO logic
	 */
	protected Collection<CategoryModel> getAllCategories(final CatalogVersionModel cmodel, final int start, final int count)
	{
		final de.hybris.platform.catalog.jalo.CatalogVersion versionJalo = modelService.getSource(cmodel);
		final Collection result = versionJalo.getAllCategories(start, count);
		final Collection<CategoryModel> modelResult = new ArrayList<CategoryModel>(result.size());
		modelService.getAll(result, modelResult);
		return modelResult;
	}

	/**
	 * TODO
	 */
	protected int getAllCategoryCount(final CatalogVersionModel model)
	{
		final de.hybris.platform.catalog.jalo.CatalogVersion versionJalo = modelService.getSource(model);
		return versionJalo.getAllCategoryCount();
	}



}
