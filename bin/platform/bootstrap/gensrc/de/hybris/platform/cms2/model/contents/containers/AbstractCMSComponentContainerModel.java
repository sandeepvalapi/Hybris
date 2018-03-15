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
package de.hybris.platform.cms2.model.contents.containers;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type AbstractCMSComponentContainer first defined at extension cms2.
 */
@SuppressWarnings("all")
public class AbstractCMSComponentContainerModel extends AbstractCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractCMSComponentContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCMSComponentContainer.currentCMSComponents</code> attribute defined at extension <code>cms2</code>. */
	public static final String CURRENTCMSCOMPONENTS = "currentCMSComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCMSComponentContainer.simpleCMSComponents</code> attribute defined at extension <code>cms2</code>. */
	public static final String SIMPLECMSCOMPONENTS = "simpleCMSComponents";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractCMSComponentContainerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractCMSComponentContainerModel(final ItemModelContext ctx)
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
	public AbstractCMSComponentContainerModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public AbstractCMSComponentContainerModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponentContainer.currentCMSComponents</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the currentCMSComponents
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "currentCMSComponents", type = Accessor.Type.GETTER)
	public List<SimpleCMSComponentModel> getCurrentCMSComponents()
	{
		return getPersistenceContext().getPropertyValue(CURRENTCMSCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponentContainer.simpleCMSComponents</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the simpleCMSComponents
	 */
	@Accessor(qualifier = "simpleCMSComponents", type = Accessor.Type.GETTER)
	public List<SimpleCMSComponentModel> getSimpleCMSComponents()
	{
		return getPersistenceContext().getPropertyValue(SIMPLECMSCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCMSComponentContainer.simpleCMSComponents</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the simpleCMSComponents
	 */
	@Accessor(qualifier = "simpleCMSComponents", type = Accessor.Type.SETTER)
	public void setSimpleCMSComponents(final List<SimpleCMSComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(SIMPLECMSCOMPONENTS, value);
	}
	
}
