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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AccountBookmarkComponent first defined at extension acceleratorcms.
 * <p>
 * It represents account bookmark component containing one link to the previous page usually.
 */
@SuppressWarnings("all")
public class AccountBookmarkComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AccountBookmarkComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>AccountBookmarkComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String STYLECLASS = "styleClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>AccountBookmarkComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String LINK = "link";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AccountBookmarkComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AccountBookmarkComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _link initial attribute declared by type <code>AccountBookmarkComponent</code> at extension <code>acceleratorcms</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AccountBookmarkComponentModel(final CatalogVersionModel _catalogVersion, final CMSLinkComponentModel _link, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setLink(_link);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _link initial attribute declared by type <code>AccountBookmarkComponent</code> at extension <code>acceleratorcms</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AccountBookmarkComponentModel(final CatalogVersionModel _catalogVersion, final CMSLinkComponentModel _link, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setLink(_link);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountBookmarkComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the link - The cms navigation node of this navigation component.
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.GETTER)
	public CMSLinkComponentModel getLink()
	{
		return getPersistenceContext().getPropertyValue(LINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountBookmarkComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the styleClass - CSS style class of this bar component.
	 */
	@Accessor(qualifier = "styleClass", type = Accessor.Type.GETTER)
	public String getStyleClass()
	{
		return getPersistenceContext().getPropertyValue(STYLECLASS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AccountBookmarkComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the link - The cms navigation node of this navigation component.
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.SETTER)
	public void setLink(final CMSLinkComponentModel value)
	{
		getPersistenceContext().setPropertyValue(LINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AccountBookmarkComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the styleClass - CSS style class of this bar component.
	 */
	@Accessor(qualifier = "styleClass", type = Accessor.Type.SETTER)
	public void setStyleClass(final String value)
	{
		getPersistenceContext().setPropertyValue(STYLECLASS, value);
	}
	
}
