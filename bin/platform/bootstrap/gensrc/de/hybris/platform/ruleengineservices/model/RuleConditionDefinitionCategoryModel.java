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
package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type RuleConditionDefinitionCategory first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleConditionDefinitionCategoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleConditionDefinitionCategory";
	
	/**<i>Generated relation code constant for relation <code>RuleConditionDefinition2CategoryRelation</code> defining source attribute <code>definitions</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _RULECONDITIONDEFINITION2CATEGORYRELATION = "RuleConditionDefinition2CategoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionCategory.id</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionCategory.name</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionCategory.priority</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionCategory.icon</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ICON = "icon";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionCategory.definitions</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEFINITIONS = "definitions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleConditionDefinitionCategoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleConditionDefinitionCategoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleConditionDefinitionCategoryModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.definitions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the definitions
	 */
	@Accessor(qualifier = "definitions", type = Accessor.Type.GETTER)
	public List<RuleConditionDefinitionModel> getDefinitions()
	{
		return getPersistenceContext().getPropertyValue(DEFINITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.icon</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
	public CatalogUnawareMediaModel getIcon()
	{
		return getPersistenceContext().getPropertyValue(ICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionCategory.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.definitions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the definitions
	 */
	@Accessor(qualifier = "definitions", type = Accessor.Type.SETTER)
	public void setDefinitions(final List<RuleConditionDefinitionModel> value)
	{
		getPersistenceContext().setPropertyValue(DEFINITIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.icon</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
	public void setIcon(final CatalogUnawareMediaModel value)
	{
		getPersistenceContext().setPropertyValue(ICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionCategory.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
}
