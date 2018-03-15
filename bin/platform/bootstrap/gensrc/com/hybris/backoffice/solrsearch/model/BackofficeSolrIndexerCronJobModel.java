/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.backoffice.solrsearch.model;

import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;
import java.util.Collection;

/**
 * Generated model class for type BackofficeSolrIndexerCronJob first defined at extension backofficesolrsearch.
 */
@SuppressWarnings("all")
public class BackofficeSolrIndexerCronJobModel extends SolrIndexerCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BackofficeSolrIndexerCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSolrIndexerCronJob.modifiedItems</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String MODIFIEDITEMS = "modifiedItems";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BackofficeSolrIndexerCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BackofficeSolrIndexerCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndexerCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BackofficeSolrIndexerCronJobModel(final SolrFacetSearchConfigModel _facetSearchConfig, final JobModel _job)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndexerCronJob</code> at extension <code>solrfacetsearch</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public BackofficeSolrIndexerCronJobModel(final SolrFacetSearchConfigModel _facetSearchConfig, final JobModel _job, final ItemModel _owner)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSolrIndexerCronJob.modifiedItems</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the modifiedItems
	 */
	@Accessor(qualifier = "modifiedItems", type = Accessor.Type.GETTER)
	public Collection<SolrModifiedItemModel> getModifiedItems()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSolrIndexerCronJob.modifiedItems</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the modifiedItems
	 */
	@Accessor(qualifier = "modifiedItems", type = Accessor.Type.SETTER)
	public void setModifiedItems(final Collection<SolrModifiedItemModel> value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDITEMS, value);
	}
	
}
