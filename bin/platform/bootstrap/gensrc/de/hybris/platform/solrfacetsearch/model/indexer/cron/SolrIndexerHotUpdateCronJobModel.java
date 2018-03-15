/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.solrfacetsearch.model.indexer.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;
import java.util.Collection;

/**
 * Generated model class for type SolrIndexerHotUpdateCronJob first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrIndexerHotUpdateCronJobModel extends SolrIndexerCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexerHotUpdateCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerHotUpdateCronJob.indexTypeName</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXTYPENAME = "indexTypeName";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerHotUpdateCronJob.items</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ITEMS = "items";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexerHotUpdateCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexerHotUpdateCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndexerCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _indexTypeName initial attribute declared by type <code>SolrIndexerHotUpdateCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SolrIndexerHotUpdateCronJobModel(final SolrFacetSearchConfigModel _facetSearchConfig, final String _indexTypeName, final JobModel _job)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setIndexTypeName(_indexTypeName);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndexerCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _indexTypeName initial attribute declared by type <code>SolrIndexerHotUpdateCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SolrIndexerHotUpdateCronJobModel(final SolrFacetSearchConfigModel _facetSearchConfig, final String _indexTypeName, final JobModel _job, final ItemModel _owner)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setIndexTypeName(_indexTypeName);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerHotUpdateCronJob.indexTypeName</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexTypeName
	 */
	@Accessor(qualifier = "indexTypeName", type = Accessor.Type.GETTER)
	public String getIndexTypeName()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerHotUpdateCronJob.items</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the items
	 */
	@Accessor(qualifier = "items", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getItems()
	{
		return getPersistenceContext().getPropertyValue(ITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerHotUpdateCronJob.indexTypeName</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexTypeName
	 */
	@Accessor(qualifier = "indexTypeName", type = Accessor.Type.SETTER)
	public void setIndexTypeName(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerHotUpdateCronJob.items</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the items
	 */
	@Accessor(qualifier = "items", type = Accessor.Type.SETTER)
	public void setItems(final Collection<ItemModel> value)
	{
		getPersistenceContext().setPropertyValue(ITEMS, value);
	}
	
}
