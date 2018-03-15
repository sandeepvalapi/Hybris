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
package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type NavigationBarCollectionComponent first defined at extension acceleratorcms.
 * <p>
 * Deprecated since 6.2, please use NavigationComponent instead. It represents account navigation bar component that contains cms navigation node.
 */
@SuppressWarnings("all")
public class NavigationBarCollectionComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "NavigationBarCollectionComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarCollectionComponent.components</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String COMPONENTS = "components";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public NavigationBarCollectionComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public NavigationBarCollectionComponentModel(final ItemModelContext ctx)
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
	public NavigationBarCollectionComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public NavigationBarCollectionComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarCollectionComponent.components</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the components - A collection of navigation bar components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.GETTER)
	public List<NavigationBarComponentModel> getComponents()
	{
		return getPersistenceContext().getPropertyValue(COMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarCollectionComponent.components</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the components - A collection of navigation bar components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.SETTER)
	public void setComponents(final List<NavigationBarComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(COMPONENTS, value);
	}
	
}
