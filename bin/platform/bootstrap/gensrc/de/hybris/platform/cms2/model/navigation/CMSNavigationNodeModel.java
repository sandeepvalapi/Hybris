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
package de.hybris.platform.cms2.model.navigation;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type CMSNavigationNode first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSNavigationNodeModel extends CMSItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSNavigationNode";
	
	/**<i>Generated relation code constant for relation <code>CMSNavigationNodeChildren</code> defining source attribute <code>parent</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSNAVIGATIONNODECHILDREN = "CMSNavigationNodeChildren";
	
	/**<i>Generated relation code constant for relation <code>CMSLinksForNavNodes</code> defining source attribute <code>links</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSLINKSFORNAVNODES = "CMSLinksForNavNodes";
	
	/**<i>Generated relation code constant for relation <code>CMSContentPagesForNavNodes</code> defining source attribute <code>pages</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSCONTENTPAGESFORNAVNODES = "CMSContentPagesForNavNodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.title</code> attribute defined at extension <code>cms2</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.visible</code> attribute defined at extension <code>cms2</code>. */
	public static final String VISIBLE = "visible";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.parentPOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String PARENTPOS = "parentPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.parent</code> attribute defined at extension <code>cms2</code>. */
	public static final String PARENT = "parent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.children</code> attribute defined at extension <code>cms2</code>. */
	public static final String CHILDREN = "children";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.links</code> attribute defined at extension <code>cms2</code>. */
	public static final String LINKS = "links";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.pages</code> attribute defined at extension <code>cms2</code>. */
	public static final String PAGES = "pages";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSNavigationNode.entries</code> attribute defined at extension <code>cms2</code>. */
	public static final String ENTRIES = "entries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSNavigationNodeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSNavigationNodeModel(final ItemModelContext ctx)
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
	public CMSNavigationNodeModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public CMSNavigationNodeModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.children</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the children
	 */
	@Accessor(qualifier = "children", type = Accessor.Type.GETTER)
	public List<CMSNavigationNodeModel> getChildren()
	{
		return getPersistenceContext().getPropertyValue(CHILDREN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.entries</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
	public List<CMSNavigationEntryModel> getEntries()
	{
		return getPersistenceContext().getPropertyValue(ENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.links</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the links
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "links", type = Accessor.Type.GETTER)
	public List<CMSLinkComponentModel> getLinks()
	{
		return getPersistenceContext().getPropertyValue(LINKS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.pages</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pages
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "pages", type = Accessor.Type.GETTER)
	public List<ContentPageModel> getPages()
	{
		return getPersistenceContext().getPropertyValue(PAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.parent</code> attribute defined at extension <code>cms2</code>. 
	 * @return the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
	public CMSNavigationNodeModel getParent()
	{
		return getPersistenceContext().getPropertyValue(PARENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.title</code> attribute defined at extension <code>cms2</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.title</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the title
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSNavigationNode.visible</code> attribute defined at extension <code>cms2</code>. 
	 * @return the visible
	 */
	@Accessor(qualifier = "visible", type = Accessor.Type.GETTER)
	public boolean isVisible()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(VISIBLE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.children</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the children
	 */
	@Accessor(qualifier = "children", type = Accessor.Type.SETTER)
	public void setChildren(final List<CMSNavigationNodeModel> value)
	{
		getPersistenceContext().setPropertyValue(CHILDREN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.entries</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
	public void setEntries(final List<CMSNavigationEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.links</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the links
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "links", type = Accessor.Type.SETTER)
	public void setLinks(final List<CMSLinkComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(LINKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.pages</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the pages
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "pages", type = Accessor.Type.SETTER)
	public void setPages(final List<ContentPageModel> value)
	{
		getPersistenceContext().setPropertyValue(PAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.parent</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
	public void setParent(final CMSNavigationNodeModel value)
	{
		getPersistenceContext().setPropertyValue(PARENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.title</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.title</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the title
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSNavigationNode.visible</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the visible
	 */
	@Accessor(qualifier = "visible", type = Accessor.Type.SETTER)
	public void setVisible(final boolean value)
	{
		getPersistenceContext().setPropertyValue(VISIBLE, toObject(value));
	}
	
}
