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
package de.hybris.platform.cms2.model.relations;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ContentSlotForTemplate first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ContentSlotForTemplateModel extends CMSRelationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ContentSlotForTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotForTemplate.allowOverwrite</code> attribute defined at extension <code>cms2</code>. */
	public static final String ALLOWOVERWRITE = "allowOverwrite";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotForTemplate.position</code> attribute defined at extension <code>cms2</code>. */
	public static final String POSITION = "position";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotForTemplate.pageTemplate</code> attribute defined at extension <code>cms2</code>. */
	public static final String PAGETEMPLATE = "pageTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotForTemplate.contentSlot</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTSLOT = "contentSlot";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ContentSlotForTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ContentSlotForTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSRelation</code> at extension <code>cms2</code>
	 * @param _contentSlot initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _pageTemplate initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _position initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSRelation</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotForTemplateModel(final CatalogVersionModel _catalogVersion, final ContentSlotModel _contentSlot, final PageTemplateModel _pageTemplate, final String _position, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setContentSlot(_contentSlot);
		setPageTemplate(_pageTemplate);
		setPosition(_position);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSRelation</code> at extension <code>cms2</code>
	 * @param _contentSlot initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pageTemplate initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _position initial attribute declared by type <code>ContentSlotForTemplate</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSRelation</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotForTemplateModel(final CatalogVersionModel _catalogVersion, final ContentSlotModel _contentSlot, final ItemModel _owner, final PageTemplateModel _pageTemplate, final String _position, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setContentSlot(_contentSlot);
		setOwner(_owner);
		setPageTemplate(_pageTemplate);
		setPosition(_position);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotForTemplate.allowOverwrite</code> attribute defined at extension <code>cms2</code>. 
	 * @return the allowOverwrite
	 */
	@Accessor(qualifier = "allowOverwrite", type = Accessor.Type.GETTER)
	public Boolean getAllowOverwrite()
	{
		return getPersistenceContext().getPropertyValue(ALLOWOVERWRITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotForTemplate.contentSlot</code> attribute defined at extension <code>cms2</code>. 
	 * @return the contentSlot
	 */
	@Accessor(qualifier = "contentSlot", type = Accessor.Type.GETTER)
	public ContentSlotModel getContentSlot()
	{
		return getPersistenceContext().getPropertyValue(CONTENTSLOT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotForTemplate.pageTemplate</code> attribute defined at extension <code>cms2</code>. 
	 * @return the pageTemplate
	 */
	@Accessor(qualifier = "pageTemplate", type = Accessor.Type.GETTER)
	public PageTemplateModel getPageTemplate()
	{
		return getPersistenceContext().getPropertyValue(PAGETEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotForTemplate.position</code> attribute defined at extension <code>cms2</code>. 
	 * @return the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.GETTER)
	public String getPosition()
	{
		return getPersistenceContext().getPropertyValue(POSITION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotForTemplate.allowOverwrite</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the allowOverwrite
	 */
	@Accessor(qualifier = "allowOverwrite", type = Accessor.Type.SETTER)
	public void setAllowOverwrite(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ALLOWOVERWRITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotForTemplate.contentSlot</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the contentSlot
	 */
	@Accessor(qualifier = "contentSlot", type = Accessor.Type.SETTER)
	public void setContentSlot(final ContentSlotModel value)
	{
		getPersistenceContext().setPropertyValue(CONTENTSLOT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotForTemplate.pageTemplate</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the pageTemplate
	 */
	@Accessor(qualifier = "pageTemplate", type = Accessor.Type.SETTER)
	public void setPageTemplate(final PageTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(PAGETEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotForTemplate.position</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.SETTER)
	public void setPosition(final String value)
	{
		getPersistenceContext().setPropertyValue(POSITION, value);
	}
	
}
