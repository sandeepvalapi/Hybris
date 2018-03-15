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
package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import java.util.Collection;
import java.util.Map;

/**
 * Generated model class for type SolrIndexerDistributedProcess first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrIndexerDistributedProcessModel extends SimpleDistributedProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexerDistributedProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.sessionUser</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SESSIONUSER = "sessionUser";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.sessionLanguage</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SESSIONLANGUAGE = "sessionLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.sessionCurrency</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SESSIONCURRENCY = "sessionCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.indexOperationId</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXOPERATIONID = "indexOperationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.indexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXOPERATION = "indexOperation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.externalIndexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String EXTERNALINDEXOPERATION = "externalIndexOperation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSEARCHCONFIG = "facetSearchConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.indexedProperties</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDPROPERTIES = "indexedProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.index</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEX = "index";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexerDistributedProcess.indexerHints</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXERHINTS = "indexerHints";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexerDistributedProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexerDistributedProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SolrIndexerDistributedProcessModel(final String _code, final String _currentExecutionId, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setState(_state);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _batchSize initial attribute declared by type <code>SimpleDistributedProcess</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _handlerBeanId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _nodeGroup initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SolrIndexerDistributedProcessModel(final int _batchSize, final String _code, final String _currentExecutionId, final String _handlerBeanId, final String _nodeGroup, final ItemModel _owner, final DistributedProcessState _state)
	{
		super();
		setBatchSize(_batchSize);
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setHandlerBeanId(_handlerBeanId);
		setNodeGroup(_nodeGroup);
		setOwner(_owner);
		setState(_state);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
	public String getFacetSearchConfig()
	{
		return getPersistenceContext().getPropertyValue(FACETSEARCHCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.index</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.GETTER)
	public String getIndex()
	{
		return getPersistenceContext().getPropertyValue(INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.indexedProperties</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexedProperties
	 */
	@Accessor(qualifier = "indexedProperties", type = Accessor.Type.GETTER)
	public Collection<String> getIndexedProperties()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public String getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.indexerHints</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexerHints
	 */
	@Accessor(qualifier = "indexerHints", type = Accessor.Type.GETTER)
	public Map<String,String> getIndexerHints()
	{
		return getPersistenceContext().getPropertyValue(INDEXERHINTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.indexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexOperation
	 */
	@Accessor(qualifier = "indexOperation", type = Accessor.Type.GETTER)
	public IndexerOperationValues getIndexOperation()
	{
		return getPersistenceContext().getPropertyValue(INDEXOPERATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.indexOperationId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexOperationId
	 */
	@Accessor(qualifier = "indexOperationId", type = Accessor.Type.GETTER)
	public long getIndexOperationId()
	{
		return toPrimitive((Long)getPersistenceContext().getPropertyValue(INDEXOPERATIONID));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.sessionCurrency</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
	public String getSessionCurrency()
	{
		return getPersistenceContext().getPropertyValue(SESSIONCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.sessionLanguage</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
	public String getSessionLanguage()
	{
		return getPersistenceContext().getPropertyValue(SESSIONLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.sessionUser</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the sessionUser
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.GETTER)
	public String getSessionUser()
	{
		return getPersistenceContext().getPropertyValue(SESSIONUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexerDistributedProcess.externalIndexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the externalIndexOperation
	 */
	@Accessor(qualifier = "externalIndexOperation", type = Accessor.Type.GETTER)
	public boolean isExternalIndexOperation()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(EXTERNALINDEXOPERATION));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.externalIndexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the externalIndexOperation
	 */
	@Accessor(qualifier = "externalIndexOperation", type = Accessor.Type.SETTER)
	public void setExternalIndexOperation(final boolean value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALINDEXOPERATION, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
	public void setFacetSearchConfig(final String value)
	{
		getPersistenceContext().setPropertyValue(FACETSEARCHCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.index</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.SETTER)
	public void setIndex(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.indexedProperties</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexedProperties
	 */
	@Accessor(qualifier = "indexedProperties", type = Accessor.Type.SETTER)
	public void setIndexedProperties(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.indexerHints</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexerHints
	 */
	@Accessor(qualifier = "indexerHints", type = Accessor.Type.SETTER)
	public void setIndexerHints(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(INDEXERHINTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.indexOperation</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexOperation
	 */
	@Accessor(qualifier = "indexOperation", type = Accessor.Type.SETTER)
	public void setIndexOperation(final IndexerOperationValues value)
	{
		getPersistenceContext().setPropertyValue(INDEXOPERATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.indexOperationId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexOperationId
	 */
	@Accessor(qualifier = "indexOperationId", type = Accessor.Type.SETTER)
	public void setIndexOperationId(final long value)
	{
		getPersistenceContext().setPropertyValue(INDEXOPERATIONID, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.sessionCurrency</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
	public void setSessionCurrency(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.sessionLanguage</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
	public void setSessionLanguage(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexerDistributedProcess.sessionUser</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the sessionUser
	 */
	@Accessor(qualifier = "sessionUser", type = Accessor.Type.SETTER)
	public void setSessionUser(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONUSER, value);
	}
	
}
