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
package de.hybris.platform.catalog.job.util.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.job.sort.Sorter;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Dao like finder for getting {@link ComposedTypeModel}s , {@link CatalogVersionModel} related {@link ItemModel}
 * instances.
 */
public class DefaultCatalogVersionJobDao implements CatalogVersionJobDao
{

	private FlexibleSearchService flexibleSearchService;

	private Sorter<ComposedTypeModel> composedTypeSorter;

	private ModelService modelService;

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setComposedTypeSorter(final Sorter<ComposedTypeModel> composedTypeSorter)
	{
		this.composedTypeSorter = composedTypeSorter;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public int getItemInstanceCount(final CatalogModel catalogModel, final Collection<ComposedTypeModel> composedTypes)
	{
		return getItemInstanceCount(catalogModel.getCatalogVersions(), composedTypes);
	}

	@Override
	public int getItemInstanceCount(final Collection<CatalogVersionModel> catalogVersions,
			final Collection<ComposedTypeModel> composedTypes)
	{
		int itemcount = 0;
		for (final CatalogVersionModel catver : catalogVersions)
		{
			itemcount += getItemInstanceCount(catver, composedTypes);
		}
		return itemcount;
	}

	@Override
	public int getItemInstanceCount(final CatalogVersionModel catalogVersion, final Collection<ComposedTypeModel> composedTypes)
	{
		int itemcount = 0;
		for (final ComposedTypeModel comptyp : composedTypes)
		{
			itemcount += getItemInstanceCount(catalogVersion, comptyp);
		}
		return itemcount;
	}

	@Override
	public int getItemInstanceCount(final CatalogVersionModel catalogVersion, final ComposedTypeModel composedType)
	{
		if (composedType.getCatalogVersionAttribute() == null)
		{
			throw new IllegalArgumentException("Could not find the AttributeDescriptor for a ComposedType " + composedType);
		}
		final Map<String, Object> values = new HashMap<String, Object>();

		final String queryString = "SELECT count({" + ItemModel.PK + "}) FROM {" + composedType.getCode() + "!} WHERE {"
				+ composedType.getCatalogVersionAttribute().getQualifier() + "}  = ?version";
		values.put("version", catalogVersion);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, values);
		query.setResultClassList(Arrays.asList(Integer.class));
		query.setFailOnUnknownFields(false);
		final SearchResult<Integer> result = flexibleSearchService.search(query);

		if (result.getCount() == 1)
		{
			return result.getResult().get(0).intValue();
		}
		return 0;
	}

	@Override
	public List<ComposedTypeModel> getOrderedComposedTypes()
	{
		final Collection<ComposedTypeModel> modelResult = getUnorderedNonAbstractComposedTypes();

		return composedTypeSorter.sort(modelResult);
	}

	/**
	 * JALO method use service layer instead
	 */
	protected Collection<ComposedTypeModel> getUnorderedNonAbstractComposedTypes()
	{
		final Collection<ComposedType> resultJalo = CatalogManager.getInstance().getAllCatalogItemTypes();
		final Collection<ComposedTypeModel> modelResult = new ArrayList<ComposedTypeModel>(resultJalo.size());
		for (final ComposedType ct : resultJalo)
		{
			if (!ct.isAbstract())
			{
				modelResult.add((ComposedTypeModel) modelService.get(ct));
			}
		}
		return modelResult;
	}

	@Override
	public List<PK> getPKList(final ComposedTypeModel composedTypeModel, final CatalogVersionModel version)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("version", version);

		final String queryString = "SELECT {" + ItemModel.PK + "} FROM {" + composedTypeModel.getCode() + "!} WHERE {"
				+ composedTypeModel.getCatalogVersionAttribute().getQualifier() + "}  = ?version ORDER BY {PK} DESC";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, values);
		query.setResultClassList(Arrays.asList(PK.class));

		final SearchResult<PK> result = flexibleSearchService.search(query);

		return result.getResult();
	}


}
