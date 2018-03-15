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
package de.hybris.platform.cms2.model.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type PageTemplate first defined at extension cms2.
 */
@SuppressWarnings("all")
public class PageTemplateModel extends CMSItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PageTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.active</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.velocityTemplate</code> attribute defined at extension <code>cms2</code>. */
	public static final String VELOCITYTEMPLATE = "velocityTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.frontendTemplateName</code> attribute defined at extension <code>cms2</code>. */
	public static final String FRONTENDTEMPLATENAME = "frontendTemplateName";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.contentSlots</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTSLOTS = "contentSlots";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.previewIcon</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWICON = "previewIcon";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.availableContentSlots</code> attribute defined at extension <code>cms2</code>. */
	public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
	
	/** <i>Generated constant</i> - Attribute key of <code>PageTemplate.restrictedPageTypes</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESTRICTEDPAGETYPES = "restrictedPageTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PageTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PageTemplateModel(final ItemModelContext ctx)
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
	public PageTemplateModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public PageTemplateModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.active</code> attribute defined at extension <code>cms2</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.availableContentSlots</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the availableContentSlots
	 */
	@Accessor(qualifier = "availableContentSlots", type = Accessor.Type.GETTER)
	public List<ContentSlotNameModel> getAvailableContentSlots()
	{
		return getPersistenceContext().getPropertyValue(AVAILABLECONTENTSLOTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.contentSlots</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contentSlots
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "contentSlots", type = Accessor.Type.GETTER)
	public List<ContentSlotForTemplateModel> getContentSlots()
	{
		return getPersistenceContext().getPropertyValue(CONTENTSLOTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.frontendTemplateName</code> attribute defined at extension <code>cms2</code>. 
	 * @return the frontendTemplateName
	 */
	@Accessor(qualifier = "frontendTemplateName", type = Accessor.Type.GETTER)
	public String getFrontendTemplateName()
	{
		return getPersistenceContext().getPropertyValue(FRONTENDTEMPLATENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.previewIcon</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewIcon
	 */
	@Accessor(qualifier = "previewIcon", type = Accessor.Type.GETTER)
	public MediaModel getPreviewIcon()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.restrictedPageTypes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictedPageTypes
	 */
	@Accessor(qualifier = "restrictedPageTypes", type = Accessor.Type.GETTER)
	public Set<CMSPageTypeModel> getRestrictedPageTypes()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTEDPAGETYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PageTemplate.velocityTemplate</code> attribute defined at extension <code>cms2</code>. 
	 * @return the velocityTemplate
	 */
	@Accessor(qualifier = "velocityTemplate", type = Accessor.Type.GETTER)
	public String getVelocityTemplate()
	{
		return getPersistenceContext().getPropertyValue(VELOCITYTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.active</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.availableContentSlots</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the availableContentSlots
	 */
	@Accessor(qualifier = "availableContentSlots", type = Accessor.Type.SETTER)
	public void setAvailableContentSlots(final List<ContentSlotNameModel> value)
	{
		getPersistenceContext().setPropertyValue(AVAILABLECONTENTSLOTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.frontendTemplateName</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the frontendTemplateName
	 */
	@Accessor(qualifier = "frontendTemplateName", type = Accessor.Type.SETTER)
	public void setFrontendTemplateName(final String value)
	{
		getPersistenceContext().setPropertyValue(FRONTENDTEMPLATENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.previewIcon</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewIcon
	 */
	@Accessor(qualifier = "previewIcon", type = Accessor.Type.SETTER)
	public void setPreviewIcon(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.restrictedPageTypes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the restrictedPageTypes
	 */
	@Accessor(qualifier = "restrictedPageTypes", type = Accessor.Type.SETTER)
	public void setRestrictedPageTypes(final Set<CMSPageTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTEDPAGETYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PageTemplate.velocityTemplate</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the velocityTemplate
	 */
	@Accessor(qualifier = "velocityTemplate", type = Accessor.Type.SETTER)
	public void setVelocityTemplate(final String value)
	{
		getPersistenceContext().setPropertyValue(VELOCITYTEMPLATE, value);
	}
	
}
