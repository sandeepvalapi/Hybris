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
package de.hybris.platform.catalog.job.util;

import de.hybris.platform.catalog.job.sort.impl.ComposedTypeSorter;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Collection;
import java.util.List;



/**
 * Dao like finder for getting {@link ComposedTypeModel}s , {@link CatalogVersionModel} related {@link ItemModel}
 * instances.
 * 
 * @since 4.3
 * @spring.bean removeItemCounter
 */
public interface CatalogVersionJobDao
{


	/**
	 * Returns the item count for the given catalog. All {@link ComposedTypeModel}s with a <code>catalogversion</code>
	 * attribute in all {@link CatalogVersionModel}s of the catalog are counted
	 */
	int getItemInstanceCount(final CatalogModel catalogModel, final Collection<ComposedTypeModel> composedTypes);

	/**
	 * Returns the item count for the given collection of {@link CatalogVersionModel}s. All {@link ComposedTypeModel}s
	 * with a <code>catalogversion</code> attribute are counted.
	 */
	int getItemInstanceCount(final Collection<CatalogVersionModel> catalogVersions,
			final Collection<ComposedTypeModel> composedTypes);

	/**
	 * Return the item count which match the given {@link CatalogVersionModel}. All {@link ComposedTypeModel}s with a
	 * <code>catalogversion</code> attribute are counted.
	 */
	int getItemInstanceCount(final CatalogVersionModel catalogVersion, final Collection<ComposedTypeModel> composedTypes);

	/**
	 * Returns the item count which match the given {@link ComposedTypeModel} and {@link CatalogVersionModel}.
	 */
	int getItemInstanceCount(final CatalogVersionModel catalogVersion, final ComposedTypeModel composedType);

	/**
	 * Returns sorted {@link ComposedTypeModel} list according to {@link ComposedTypeSorter} strategy.
	 */
	List<ComposedTypeModel> getOrderedComposedTypes();

	/**
	 * Returns a list of {@link PK}s for the given {@link ComposedTypeModel} and {@link CatalogVersionModel}.
	 */
	List<PK> getPKList(final ComposedTypeModel composedTypeModel, final CatalogVersionModel version);

}
