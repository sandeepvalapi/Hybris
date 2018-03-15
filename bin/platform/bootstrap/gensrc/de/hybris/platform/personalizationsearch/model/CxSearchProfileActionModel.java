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
package de.hybris.platform.personalizationsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxSearchProfileAction first defined at extension personalizationsearch.
 */
@SuppressWarnings("all")
public class CxSearchProfileActionModel extends CxAbstractActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxSearchProfileAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxSearchProfileAction.searchProfileCode</code> attribute defined at extension <code>personalizationsearch</code>. */
	public static final String SEARCHPROFILECODE = "searchProfileCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxSearchProfileAction.searchProfileCatalog</code> attribute defined at extension <code>personalizationsearch</code>. */
	public static final String SEARCHPROFILECATALOG = "searchProfileCatalog";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxSearchProfileActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxSearchProfileActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _searchProfileCode initial attribute declared by type <code>CxSearchProfileAction</code> at extension <code>personalizationsearch</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxSearchProfileActionModel(final CatalogVersionModel _catalogVersion, final String _code, final String _searchProfileCode, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setSearchProfileCode(_searchProfileCode);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchProfileCode initial attribute declared by type <code>CxSearchProfileAction</code> at extension <code>personalizationsearch</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxSearchProfileActionModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final String _searchProfileCode, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setSearchProfileCode(_searchProfileCode);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxSearchProfileAction.searchProfileCatalog</code> attribute defined at extension <code>personalizationsearch</code>. 
	 * @return the searchProfileCatalog - Targeted search profile catalog
	 */
	@Accessor(qualifier = "searchProfileCatalog", type = Accessor.Type.GETTER)
	public String getSearchProfileCatalog()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPROFILECATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxSearchProfileAction.searchProfileCode</code> attribute defined at extension <code>personalizationsearch</code>. 
	 * @return the searchProfileCode - Targeted search profile code
	 */
	@Accessor(qualifier = "searchProfileCode", type = Accessor.Type.GETTER)
	public String getSearchProfileCode()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPROFILECODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxSearchProfileAction.searchProfileCatalog</code> attribute defined at extension <code>personalizationsearch</code>. 
	 *  
	 * @param value the searchProfileCatalog - Targeted search profile catalog
	 */
	@Accessor(qualifier = "searchProfileCatalog", type = Accessor.Type.SETTER)
	public void setSearchProfileCatalog(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPROFILECATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxSearchProfileAction.searchProfileCode</code> attribute defined at extension <code>personalizationsearch</code>. 
	 *  
	 * @param value the searchProfileCode - Targeted search profile code
	 */
	@Accessor(qualifier = "searchProfileCode", type = Accessor.Type.SETTER)
	public void setSearchProfileCode(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPROFILECODE, value);
	}
	
}
