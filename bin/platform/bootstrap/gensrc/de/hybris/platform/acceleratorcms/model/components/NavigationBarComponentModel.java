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
import de.hybris.platform.acceleratorcms.enums.NavigationBarMenuLayout;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type NavigationBarComponent first defined at extension acceleratorcms.
 * <p>
 * Deprecated since 6.2, please use NavigationComponent instead. It represents navigation bar component that contains cms navigation node. Nodes structure can be built in a hierarchical way.
 */
@SuppressWarnings("all")
public class NavigationBarComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "NavigationBarComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarComponent.dropDownLayout</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String DROPDOWNLAYOUT = "dropDownLayout";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarComponent.navigationNode</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String NAVIGATIONNODE = "navigationNode";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String WRAPAFTER = "wrapAfter";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String LINK = "link";
	
	/** <i>Generated constant</i> - Attribute key of <code>NavigationBarComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String STYLECLASS = "styleClass";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public NavigationBarComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public NavigationBarComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _link initial attribute declared by type <code>NavigationBarComponent</code> at extension <code>acceleratorcms</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public NavigationBarComponentModel(final CatalogVersionModel _catalogVersion, final CMSLinkComponentModel _link, final String _uid)
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
	 * @param _link initial attribute declared by type <code>NavigationBarComponent</code> at extension <code>acceleratorcms</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public NavigationBarComponentModel(final CatalogVersionModel _catalogVersion, final CMSLinkComponentModel _link, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setLink(_link);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarComponent.dropDownLayout</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the dropDownLayout - Determines the way navigation drop down is positioned.
	 */
	@Accessor(qualifier = "dropDownLayout", type = Accessor.Type.GETTER)
	public NavigationBarMenuLayout getDropDownLayout()
	{
		return getPersistenceContext().getPropertyValue(DROPDOWNLAYOUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the link - The cms link component that is attached to this bar component.
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.GETTER)
	public CMSLinkComponentModel getLink()
	{
		return getPersistenceContext().getPropertyValue(LINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarComponent.navigationNode</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the navigationNode - The cms navigation node of this navigation bar component.
	 */
	@Accessor(qualifier = "navigationNode", type = Accessor.Type.GETTER)
	public CMSNavigationNodeModel getNavigationNode()
	{
		return getPersistenceContext().getPropertyValue(NAVIGATIONNODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the styleClass - CSS style class of this bar component.
	 */
	@Accessor(qualifier = "styleClass", type = Accessor.Type.GETTER)
	public String getStyleClass()
	{
		return getPersistenceContext().getPropertyValue(STYLECLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>NavigationBarComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the wrapAfter - Determines the number of navigation nodes when the following elements will be wrapped.
	 */
	@Accessor(qualifier = "wrapAfter", type = Accessor.Type.GETTER)
	public Integer getWrapAfter()
	{
		return getPersistenceContext().getPropertyValue(WRAPAFTER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarComponent.dropDownLayout</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the dropDownLayout - Determines the way navigation drop down is positioned.
	 */
	@Accessor(qualifier = "dropDownLayout", type = Accessor.Type.SETTER)
	public void setDropDownLayout(final NavigationBarMenuLayout value)
	{
		getPersistenceContext().setPropertyValue(DROPDOWNLAYOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarComponent.link</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the link - The cms link component that is attached to this bar component.
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.SETTER)
	public void setLink(final CMSLinkComponentModel value)
	{
		getPersistenceContext().setPropertyValue(LINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarComponent.navigationNode</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the navigationNode - The cms navigation node of this navigation bar component.
	 */
	@Accessor(qualifier = "navigationNode", type = Accessor.Type.SETTER)
	public void setNavigationNode(final CMSNavigationNodeModel value)
	{
		getPersistenceContext().setPropertyValue(NAVIGATIONNODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarComponent.styleClass</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the styleClass - CSS style class of this bar component.
	 */
	@Accessor(qualifier = "styleClass", type = Accessor.Type.SETTER)
	public void setStyleClass(final String value)
	{
		getPersistenceContext().setPropertyValue(STYLECLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>NavigationBarComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the wrapAfter - Determines the number of navigation nodes when the following elements will be wrapped.
	 */
	@Accessor(qualifier = "wrapAfter", type = Accessor.Type.SETTER)
	public void setWrapAfter(final Integer value)
	{
		getPersistenceContext().setPropertyValue(WRAPAFTER, value);
	}
	
}
