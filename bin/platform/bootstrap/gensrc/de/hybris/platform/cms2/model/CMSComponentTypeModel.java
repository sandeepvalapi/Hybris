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
import de.hybris.platform.acceleratorcms.model.CMSActionTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type CMSComponentType first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSComponentTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSComponentType";
	
	/**<i>Generated relation code constant for relation <code>ValidComponentTypesForSite</code> defining source attribute <code>cmsSites</code> in extension <code>cms2</code>.</i>*/
	public static final String _VALIDCOMPONENTTYPESFORSITE = "ValidComponentTypesForSite";
	
	/**<i>Generated relation code constant for relation <code>ValidComponentTypesForContentSlots</code> defining source attribute <code>contentSlotNames</code> in extension <code>cms2</code>.</i>*/
	public static final String _VALIDCOMPONENTTYPESFORCONTENTSLOTS = "ValidComponentTypesForContentSlots";
	
	/**<i>Generated relation code constant for relation <code>ComponentTypeGroups2ComponentType</code> defining source attribute <code>componentTypeGroups</code> in extension <code>cms2</code>.</i>*/
	public static final String _COMPONENTTYPEGROUPS2COMPONENTTYPE = "ComponentTypeGroups2ComponentType";
	
	/**<i>Generated relation code constant for relation <code>ApplicableCmsActionsTypeForCmsComponent</code> defining source attribute <code>actionTypes</code> in extension <code>acceleratorcms</code>.</i>*/
	public static final String _APPLICABLECMSACTIONSTYPEFORCMSCOMPONENT = "ApplicableCmsActionsTypeForCmsComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSComponentType.cmsSites</code> attribute defined at extension <code>cms2</code>. */
	public static final String CMSSITES = "cmsSites";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSComponentType.contentSlotNames</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTSLOTNAMES = "contentSlotNames";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSComponentType.componentTypeGroups</code> attribute defined at extension <code>cms2</code>. */
	public static final String COMPONENTTYPEGROUPS = "componentTypeGroups";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSComponentType.actionTypes</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String ACTIONTYPES = "actionTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSComponentTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSComponentTypeModel(final ItemModelContext ctx)
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
	public CMSComponentTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
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
	public CMSComponentTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType)
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
	 * <i>Generated method</i> - Getter of the <code>CMSComponentType.actionTypes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actionTypes
	 */
	@Accessor(qualifier = "actionTypes", type = Accessor.Type.GETTER)
	public Collection<CMSActionTypeModel> getActionTypes()
	{
		return getPersistenceContext().getPropertyValue(ACTIONTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSComponentType.cmsSites</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cmsSites
	 */
	@Accessor(qualifier = "cmsSites", type = Accessor.Type.GETTER)
	public Set<CMSSiteModel> getCmsSites()
	{
		return getPersistenceContext().getPropertyValue(CMSSITES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSComponentType.componentTypeGroups</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the componentTypeGroups
	 */
	@Accessor(qualifier = "componentTypeGroups", type = Accessor.Type.GETTER)
	public Set<ComponentTypeGroupModel> getComponentTypeGroups()
	{
		return getPersistenceContext().getPropertyValue(COMPONENTTYPEGROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSComponentType.contentSlotNames</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contentSlotNames
	 */
	@Accessor(qualifier = "contentSlotNames", type = Accessor.Type.GETTER)
	public Set<ContentSlotNameModel> getContentSlotNames()
	{
		return getPersistenceContext().getPropertyValue(CONTENTSLOTNAMES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSComponentType.actionTypes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the actionTypes
	 */
	@Accessor(qualifier = "actionTypes", type = Accessor.Type.SETTER)
	public void setActionTypes(final Collection<CMSActionTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(ACTIONTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSComponentType.cmsSites</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the cmsSites
	 */
	@Accessor(qualifier = "cmsSites", type = Accessor.Type.SETTER)
	public void setCmsSites(final Set<CMSSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(CMSSITES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSComponentType.componentTypeGroups</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the componentTypeGroups
	 */
	@Accessor(qualifier = "componentTypeGroups", type = Accessor.Type.SETTER)
	public void setComponentTypeGroups(final Set<ComponentTypeGroupModel> value)
	{
		getPersistenceContext().setPropertyValue(COMPONENTTYPEGROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSComponentType.contentSlotNames</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the contentSlotNames
	 */
	@Accessor(qualifier = "contentSlotNames", type = Accessor.Type.SETTER)
	public void setContentSlotNames(final Set<ContentSlotNameModel> value)
	{
		getPersistenceContext().setPropertyValue(CONTENTSLOTNAMES, value);
	}
	
}
