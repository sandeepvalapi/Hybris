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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;

/**
 * Generated model class for type SolrIndex first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrIndexModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndex";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndex.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSEARCHCONFIG = "facetSearchConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndex.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndex.qualifier</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndex.active</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndex.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXOPERATIONS = "indexOperations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 * @param _indexedType initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 * @param _qualifier initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexModel(final SolrFacetSearchConfigModel _facetSearchConfig, final SolrIndexedTypeModel _indexedType, final String _qualifier)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setIndexedType(_indexedType);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _facetSearchConfig initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 * @param _indexedType initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>SolrIndex</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexModel(final SolrFacetSearchConfigModel _facetSearchConfig, final SolrIndexedTypeModel _indexedType, final ItemModel _owner, final String _qualifier)
	{
		super();
		setFacetSearchConfig(_facetSearchConfig);
		setIndexedType(_indexedType);
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndex.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
	public SolrFacetSearchConfigModel getFacetSearchConfig()
	{
		return getPersistenceContext().getPropertyValue(FACETSEARCHCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndex.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndex.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the indexOperations
	 */
	@Accessor(qualifier = "indexOperations", type = Accessor.Type.GETTER)
	public Collection<SolrIndexOperationModel> getIndexOperations()
	{
		return getPersistenceContext().getPropertyValue(INDEXOPERATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndex.qualifier</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndex.active</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public boolean isActive()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ACTIVE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndex.active</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndex.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
	public void setFacetSearchConfig(final SolrFacetSearchConfigModel value)
	{
		getPersistenceContext().setPropertyValue(FACETSEARCHCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndex.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndex.indexOperations</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexOperations
	 */
	@Accessor(qualifier = "indexOperations", type = Accessor.Type.SETTER)
	public void setIndexOperations(final Collection<SolrIndexOperationModel> value)
	{
		getPersistenceContext().setPropertyValue(INDEXOPERATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndex.qualifier</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
