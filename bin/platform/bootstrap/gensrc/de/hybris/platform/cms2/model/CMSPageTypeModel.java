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
package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type CMSPageType first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSPageTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSPageType";
	
	/**<i>Generated relation code constant for relation <code>ApplicableRestrictionTypeForPageTypes</code> defining source attribute <code>restrictionTypes</code> in extension <code>cms2</code>.</i>*/
	public static final String _APPLICABLERESTRICTIONTYPEFORPAGETYPES = "ApplicableRestrictionTypeForPageTypes";
	
	/**<i>Generated relation code constant for relation <code>ValidPageTypesForTemplates</code> defining source attribute <code>templates</code> in extension <code>cms2</code>.</i>*/
	public static final String _VALIDPAGETYPESFORTEMPLATES = "ValidPageTypesForTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSPageType.previewDisabled</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWDISABLED = "previewDisabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSPageType.restrictionTypes</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESTRICTIONTYPES = "restrictionTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSPageType.templates</code> attribute defined at extension <code>cms2</code>. */
	public static final String TEMPLATES = "templates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSPageTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSPageTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public CMSPageTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public CMSPageTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSPageType.restrictionTypes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictionTypes
	 */
	@Accessor(qualifier = "restrictionTypes", type = Accessor.Type.GETTER)
	public Collection<RestrictionTypeModel> getRestrictionTypes()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSPageType.templates</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the templates
	 */
	@Accessor(qualifier = "templates", type = Accessor.Type.GETTER)
	public Set<PageTemplateModel> getTemplates()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSPageType.previewDisabled</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewDisabled
	 */
	@Accessor(qualifier = "previewDisabled", type = Accessor.Type.GETTER)
	public boolean isPreviewDisabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PREVIEWDISABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSPageType.previewDisabled</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewDisabled
	 */
	@Accessor(qualifier = "previewDisabled", type = Accessor.Type.SETTER)
	public void setPreviewDisabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWDISABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSPageType.restrictionTypes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the restrictionTypes
	 */
	@Accessor(qualifier = "restrictionTypes", type = Accessor.Type.SETTER)
	public void setRestrictionTypes(final Collection<RestrictionTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSPageType.templates</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the templates
	 */
	@Accessor(qualifier = "templates", type = Accessor.Type.SETTER)
	public void setTemplates(final Set<PageTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATES, value);
	}
	
}
