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
package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type SimpleCMSComponent first defined at extension cms2.
 */
@SuppressWarnings("all")
public class SimpleCMSComponentModel extends AbstractCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SimpleCMSComponent";
	
	/**<i>Generated relation code constant for relation <code>ElementsForContainer</code> defining source attribute <code>containers</code> in extension <code>cms2</code>.</i>*/
	public static final String _ELEMENTSFORCONTAINER = "ElementsForContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleCMSComponent.containers</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTAINERS = "containers";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SimpleCMSComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SimpleCMSComponentModel(final ItemModelContext ctx)
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
	public SimpleCMSComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public SimpleCMSComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleCMSComponent.containers</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the containers
	 */
	@Accessor(qualifier = "containers", type = Accessor.Type.GETTER)
	public Collection<AbstractCMSComponentContainerModel> getContainers()
	{
		return getPersistenceContext().getPropertyValue(CONTAINERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleCMSComponent.containers</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the containers
	 */
	@Accessor(qualifier = "containers", type = Accessor.Type.SETTER)
	public void setContainers(final Collection<AbstractCMSComponentContainerModel> value)
	{
		getPersistenceContext().setPropertyValue(CONTAINERS, value);
	}
	
}
