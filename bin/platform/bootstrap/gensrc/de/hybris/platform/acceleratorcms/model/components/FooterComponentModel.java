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
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type FooterComponent first defined at extension acceleratorcms.
 * <p>
 * Deprecated since 6.2, please use NavigationComponent instead. Represents footer component that contains configurable list of navigation nodes.
 */
@SuppressWarnings("all")
public class FooterComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FooterComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterComponent.navigationNodes</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String NAVIGATIONNODES = "navigationNodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String WRAPAFTER = "wrapAfter";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String SHOWLANGUAGECURRENCY = "showLanguageCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String NOTICE = "notice";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FooterComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FooterComponentModel(final ItemModelContext ctx)
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
	public FooterComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public FooterComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterComponent.navigationNodes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the navigationNodes - List of navigation nodes that are rendered in this footer component.
	 */
	@Accessor(qualifier = "navigationNodes", type = Accessor.Type.GETTER)
	public List<CMSNavigationNodeModel> getNavigationNodes()
	{
		return getPersistenceContext().getPropertyValue(NAVIGATIONNODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
	public String getNotice()
	{
		return getNotice(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
	public String getNotice(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NOTICE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the wrapAfter - Determines the number of navigation nodes when the following elements will be wrapped.
	 */
	@Accessor(qualifier = "wrapAfter", type = Accessor.Type.GETTER)
	public Integer getWrapAfter()
	{
		return getPersistenceContext().getPropertyValue(WRAPAFTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	@Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.GETTER)
	public boolean isShowLanguageCurrency()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SHOWLANGUAGECURRENCY));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterComponent.navigationNodes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the navigationNodes - List of navigation nodes that are rendered in this footer component.
	 */
	@Accessor(qualifier = "navigationNodes", type = Accessor.Type.SETTER)
	public void setNavigationNodes(final List<CMSNavigationNodeModel> value)
	{
		getPersistenceContext().setPropertyValue(NAVIGATIONNODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
	public void setNotice(final String value)
	{
		setNotice(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>FooterComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
	public void setNotice(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NOTICE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	@Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.SETTER)
	public void setShowLanguageCurrency(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SHOWLANGUAGECURRENCY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterComponent.wrapAfter</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the wrapAfter - Determines the number of navigation nodes when the following elements will be wrapped.
	 */
	@Accessor(qualifier = "wrapAfter", type = Accessor.Type.SETTER)
	public void setWrapAfter(final Integer value)
	{
		getPersistenceContext().setPropertyValue(WRAPAFTER, value);
	}
	
}
