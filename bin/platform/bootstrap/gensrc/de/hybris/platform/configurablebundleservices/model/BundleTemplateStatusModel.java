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
import de.hybris.platform.configurablebundleservices.enums.BundleTemplateStatusEnum;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type BundleTemplateStatus first defined at extension configurablebundleservices.
 */
@SuppressWarnings("all")
public class BundleTemplateStatusModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BundleTemplateStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplateStatus.id</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplateStatus.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplateStatus.status</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplateStatus.bundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String BUNDLETEMPLATES = "bundleTemplates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BundleTemplateStatusModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BundleTemplateStatusModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleTemplateStatus</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public BundleTemplateStatusModel(final CatalogVersionModel _catalogVersion)
	{
		super();
		setCatalogVersion(_catalogVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleTemplateStatus</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>BundleTemplateStatus</code> at extension <code>configurablebundleservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public BundleTemplateStatusModel(final CatalogVersionModel _catalogVersion, final String _id, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.bundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the bundleTemplates
	 */
	@Accessor(qualifier = "bundleTemplates", type = Accessor.Type.GETTER)
	public Collection<BundleTemplateModel> getBundleTemplates()
	{
		return getPersistenceContext().getPropertyValue(BUNDLETEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.id</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplateStatus.status</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public BundleTemplateStatusEnum getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplateStatus.bundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the bundleTemplates
	 */
	@Accessor(qualifier = "bundleTemplates", type = Accessor.Type.SETTER)
	public void setBundleTemplates(final Collection<BundleTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(BUNDLETEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleTemplateStatus.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleTemplateStatus.id</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplateStatus.status</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final BundleTemplateStatusEnum value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
}
