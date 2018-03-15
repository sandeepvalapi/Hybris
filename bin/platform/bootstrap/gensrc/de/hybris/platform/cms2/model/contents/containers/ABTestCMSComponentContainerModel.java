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
import de.hybris.platform.cms2.enums.ABTestScopes;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ABTestCMSComponentContainer first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ABTestCMSComponentContainerModel extends AbstractCMSComponentContainerModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ABTestCMSComponentContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>ABTestCMSComponentContainer.scope</code> attribute defined at extension <code>cms2</code>. */
	public static final String SCOPE = "scope";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ABTestCMSComponentContainerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ABTestCMSComponentContainerModel(final ItemModelContext ctx)
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
	public ABTestCMSComponentContainerModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public ABTestCMSComponentContainerModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ABTestCMSComponentContainer.scope</code> attribute defined at extension <code>cms2</code>. 
	 * @return the scope
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
	public ABTestScopes getScope()
	{
		return getPersistenceContext().getPropertyValue(SCOPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ABTestCMSComponentContainer.scope</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the scope
	 */
	@Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
	public void setScope(final ABTestScopes value)
	{
		getPersistenceContext().setPropertyValue(SCOPE, value);
	}
	
}
