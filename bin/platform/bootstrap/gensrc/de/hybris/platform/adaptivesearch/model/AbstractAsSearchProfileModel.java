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
import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractAsSearchProfile first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AbstractAsSearchProfileModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractAsSearchProfile";
	
	/**<i>Generated relation code constant for relation <code>AsSearchProfileActivationSet2SearchProfile</code> defining source attribute <code>activationSet</code> in extension <code>adaptivesearch</code>.</i>*/
	public static final String _ASSEARCHPROFILEACTIVATIONSET2SEARCHPROFILE = "AsSearchProfileActivationSet2SearchProfile";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.code</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.name</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.indexType</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String INDEXTYPE = "indexType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.activationSetPOS</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String ACTIVATIONSETPOS = "activationSetPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsSearchProfile.activationSet</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String ACTIVATIONSET = "activationSet";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractAsSearchProfileModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractAsSearchProfileModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractAsSearchProfile</code> at extension <code>adaptivesearch</code>
	 * @param _indexType initial attribute declared by type <code>AbstractAsSearchProfile</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AbstractAsSearchProfileModel(final String _code, final String _indexType)
	{
		super();
		setCode(_code);
		setIndexType(_indexType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsSearchProfile</code> at extension <code>adaptivesearch</code>
	 * @param _code initial attribute declared by type <code>AbstractAsSearchProfile</code> at extension <code>adaptivesearch</code>
	 * @param _indexType initial attribute declared by type <code>AbstractAsSearchProfile</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractAsSearchProfileModel(final CatalogVersionModel _catalogVersion, final String _code, final String _indexType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setIndexType(_indexType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.activationSet</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the activationSet
	 */
	@Accessor(qualifier = "activationSet", type = Accessor.Type.GETTER)
	public AsSearchProfileActivationSetModel getActivationSet()
	{
		return getPersistenceContext().getPropertyValue(ACTIVATIONSET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.code</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.indexType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
	public String getIndexType()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsSearchProfile.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsSearchProfile.activationSet</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the activationSet
	 */
	@Accessor(qualifier = "activationSet", type = Accessor.Type.SETTER)
	public void setActivationSet(final AsSearchProfileActivationSetModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIVATIONSET, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractAsSearchProfile.catalogVersion</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsSearchProfile.code</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractAsSearchProfile.indexType</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
	public void setIndexType(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsSearchProfile.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsSearchProfile.name</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}
