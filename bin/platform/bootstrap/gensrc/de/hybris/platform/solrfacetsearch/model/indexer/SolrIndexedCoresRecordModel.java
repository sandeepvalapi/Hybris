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
package de.hybris.platform.solrfacetsearch.model.indexer;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationRecordModel;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type SolrIndexedCoresRecord first defined at extension solrfacetsearch.
 * <p>
 * Deprecated since 6.2, please use SolrIndex instead.
 */
@SuppressWarnings("all")
public class SolrIndexedCoresRecordModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexedCoresRecord";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.coreName</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String CORENAME = "coreName";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.indexName</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXNAME = "indexName";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.indexTime</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXTIME = "indexTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.currentIndexDataSubDirectory</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String CURRENTINDEXDATASUBDIRECTORY = "currentIndexDataSubDirectory";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.serverMode</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SERVERMODE = "serverMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexedCoresRecord.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXOPERATIONS = "indexOperations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexedCoresRecordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexedCoresRecordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _coreName initial attribute declared by type <code>SolrIndexedCoresRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _indexName initial attribute declared by type <code>SolrIndexedCoresRecord</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexedCoresRecordModel(final String _coreName, final String _indexName)
	{
		super();
		setCoreName(_coreName);
		setIndexName(_indexName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _coreName initial attribute declared by type <code>SolrIndexedCoresRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _indexName initial attribute declared by type <code>SolrIndexedCoresRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SolrIndexedCoresRecordModel(final String _coreName, final String _indexName, final ItemModel _owner)
	{
		super();
		setCoreName(_coreName);
		setIndexName(_indexName);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.coreName</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the coreName
	 */
	@Accessor(qualifier = "coreName", type = Accessor.Type.GETTER)
	public String getCoreName()
	{
		return getPersistenceContext().getPropertyValue(CORENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.currentIndexDataSubDirectory</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the currentIndexDataSubDirectory
	 */
	@Accessor(qualifier = "currentIndexDataSubDirectory", type = Accessor.Type.GETTER)
	public String getCurrentIndexDataSubDirectory()
	{
		return getPersistenceContext().getPropertyValue(CURRENTINDEXDATASUBDIRECTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.indexName</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexName
	 */
	@Accessor(qualifier = "indexName", type = Accessor.Type.GETTER)
	public String getIndexName()
	{
		return getPersistenceContext().getPropertyValue(INDEXNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexOperations
	 */
	@Accessor(qualifier = "indexOperations", type = Accessor.Type.GETTER)
	public Collection<SolrIndexOperationRecordModel> getIndexOperations()
	{
		return getPersistenceContext().getPropertyValue(INDEXOPERATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.indexTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexTime
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "indexTime", type = Accessor.Type.GETTER)
	public Date getIndexTime()
	{
		return getPersistenceContext().getPropertyValue(INDEXTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedCoresRecord.serverMode</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the serverMode
	 */
	@Accessor(qualifier = "serverMode", type = Accessor.Type.GETTER)
	public SolrServerModes getServerMode()
	{
		return getPersistenceContext().getPropertyValue(SERVERMODE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndexedCoresRecord.coreName</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the coreName
	 */
	@Accessor(qualifier = "coreName", type = Accessor.Type.SETTER)
	public void setCoreName(final String value)
	{
		getPersistenceContext().setPropertyValue(CORENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexedCoresRecord.currentIndexDataSubDirectory</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the currentIndexDataSubDirectory
	 */
	@Accessor(qualifier = "currentIndexDataSubDirectory", type = Accessor.Type.SETTER)
	public void setCurrentIndexDataSubDirectory(final String value)
	{
		getPersistenceContext().setPropertyValue(CURRENTINDEXDATASUBDIRECTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndexedCoresRecord.indexName</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexName
	 */
	@Accessor(qualifier = "indexName", type = Accessor.Type.SETTER)
	public void setIndexName(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexedCoresRecord.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexOperations
	 */
	@Accessor(qualifier = "indexOperations", type = Accessor.Type.SETTER)
	public void setIndexOperations(final Collection<SolrIndexOperationRecordModel> value)
	{
		getPersistenceContext().setPropertyValue(INDEXOPERATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexedCoresRecord.indexTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexTime
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "indexTime", type = Accessor.Type.SETTER)
	public void setIndexTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(INDEXTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexedCoresRecord.serverMode</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the serverMode
	 */
	@Accessor(qualifier = "serverMode", type = Accessor.Type.SETTER)
	public void setServerMode(final SolrServerModes value)
	{
		getPersistenceContext().setPropertyValue(SERVERMODE, value);
	}
	
}
