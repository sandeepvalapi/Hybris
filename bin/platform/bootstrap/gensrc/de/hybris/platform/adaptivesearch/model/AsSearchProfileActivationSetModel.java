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
package de.hybris.platform.adaptivesearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type AsSearchProfileActivationSet first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AsSearchProfileActivationSetModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AsSearchProfileActivationSet";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsSearchProfileActivationSet.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsSearchProfileActivationSet.indexType</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String INDEXTYPE = "indexType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsSearchProfileActivationSet.priority</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsSearchProfileActivationSet.searchProfiles</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHPROFILES = "searchProfiles";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AsSearchProfileActivationSetModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AsSearchProfileActivationSetModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexType initial attribute declared by type <code>AsSearchProfileActivationSet</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsSearchProfileActivationSetModel(final String _indexType)
	{
		super();
		setIndexType(_indexType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AsSearchProfileActivationSet</code> at extension <code>adaptivesearch</code>
	 * @param _indexType initial attribute declared by type <code>AsSearchProfileActivationSet</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AsSearchProfileActivationSetModel(final CatalogVersionModel _catalogVersion, final String _indexType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setIndexType(_indexType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsSearchProfileActivationSet.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsSearchProfileActivationSet.indexType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
	public String getIndexType()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsSearchProfileActivationSet.priority</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the priority - priority (higher priority gets applied first)
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsSearchProfileActivationSet.searchProfiles</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchProfiles
	 */
	@Accessor(qualifier = "searchProfiles", type = Accessor.Type.GETTER)
	public List<AbstractAsSearchProfileModel> getSearchProfiles()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPROFILES);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsSearchProfileActivationSet.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsSearchProfileActivationSet.indexType</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
	public void setIndexType(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsSearchProfileActivationSet.priority</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the priority - priority (higher priority gets applied first)
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsSearchProfileActivationSet.searchProfiles</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the searchProfiles
	 */
	@Accessor(qualifier = "searchProfiles", type = Accessor.Type.SETTER)
	public void setSearchProfiles(final List<AbstractAsSearchProfileModel> value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPROFILES, value);
	}
	
}
