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
package de.hybris.platform.personalizationcms.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxCmsComponentContainer first defined at extension personalizationcms.
 */
@SuppressWarnings("all")
public class CxCmsComponentContainerModel extends AbstractCMSComponentContainerModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxCmsComponentContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCmsComponentContainer.defaultCmsComponent</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String DEFAULTCMSCOMPONENT = "defaultCmsComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCmsComponentContainer.sourceId</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String SOURCEID = "sourceId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxCmsComponentContainerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxCmsComponentContainerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CxCmsComponentContainerModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CxCmsComponentContainerModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCmsComponentContainer.defaultCmsComponent</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the defaultCmsComponent - Default component displayed if no custom variation is changing the container
	 */
	@Accessor(qualifier = "defaultCmsComponent", type = Accessor.Type.GETTER)
	public SimpleCMSComponentModel getDefaultCmsComponent()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTCMSCOMPONENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCmsComponentContainer.sourceId</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the sourceId - Id of container used by cms actions. Does not need to be unique.
	 */
	@Accessor(qualifier = "sourceId", type = Accessor.Type.GETTER)
	public String getSourceId()
	{
		return getPersistenceContext().getPropertyValue(SOURCEID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCmsComponentContainer.defaultCmsComponent</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the defaultCmsComponent - Default component displayed if no custom variation is changing the container
	 */
	@Accessor(qualifier = "defaultCmsComponent", type = Accessor.Type.SETTER)
	public void setDefaultCmsComponent(final SimpleCMSComponentModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTCMSCOMPONENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCmsComponentContainer.sourceId</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the sourceId - Id of container used by cms actions. Does not need to be unique.
	 */
	@Accessor(qualifier = "sourceId", type = Accessor.Type.SETTER)
	public void setSourceId(final String value)
	{
		getPersistenceContext().setPropertyValue(SOURCEID, value);
	}
	
}
