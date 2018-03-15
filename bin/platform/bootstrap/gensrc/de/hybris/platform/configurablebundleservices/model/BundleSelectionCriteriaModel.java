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
package de.hybris.platform.configurablebundleservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type BundleSelectionCriteria first defined at extension configurablebundleservices.
 */
@SuppressWarnings("all")
public class BundleSelectionCriteriaModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BundleSelectionCriteria";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleSelectionCriteria.id</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleSelectionCriteria.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleSelectionCriteria.starter</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String STARTER = "starter";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BundleSelectionCriteriaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BundleSelectionCriteriaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public BundleSelectionCriteriaModel(final CatalogVersionModel _catalogVersion)
	{
		super();
		setCatalogVersion(_catalogVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>BundleSelectionCriteria</code> at extension <code>configurablebundleservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public BundleSelectionCriteriaModel(final CatalogVersionModel _catalogVersion, final String _id, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.id</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleSelectionCriteria.starter</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the starter - Determines starter component for the product.
	 */
	@Accessor(qualifier = "starter", type = Accessor.Type.GETTER)
	public boolean isStarter()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(STARTER));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleSelectionCriteria.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleSelectionCriteria.id</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleSelectionCriteria.starter</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the starter - Determines starter component for the product.
	 */
	@Accessor(qualifier = "starter", type = Accessor.Type.SETTER)
	public void setStarter(final boolean value)
	{
		getPersistenceContext().setPropertyValue(STARTER, toObject(value));
	}
	
}
