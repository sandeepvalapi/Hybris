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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionCategoryModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionParameterModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionRuleTypeMappingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type RuleActionDefinition first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleActionDefinitionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleActionDefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.id</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.name</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.priority</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.breadcrumb</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String BREADCRUMB = "breadcrumb";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.translatorId</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String TRANSLATORID = "translatorId";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.translatorParameters</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String TRANSLATORPARAMETERS = "translatorParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.categories</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CATEGORIES = "categories";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.parameters</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String PARAMETERS = "parameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionDefinition.ruleTypes</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULETYPES = "ruleTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleActionDefinitionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleActionDefinitionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleActionDefinitionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.breadcrumb</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the breadcrumb
	 */
	@Accessor(qualifier = "breadcrumb", type = Accessor.Type.GETTER)
	public String getBreadcrumb()
	{
		return getBreadcrumb(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.breadcrumb</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the breadcrumb
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "breadcrumb", type = Accessor.Type.GETTER)
	public String getBreadcrumb(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(BREADCRUMB, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.categories</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public List<RuleActionDefinitionCategoryModel> getCategories()
	{
		return getPersistenceContext().getPropertyValue(CATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.parameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.GETTER)
	public List<RuleActionDefinitionParameterModel> getParameters()
	{
		return getPersistenceContext().getPropertyValue(PARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.ruleTypes</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ruleTypes
	 */
	@Accessor(qualifier = "ruleTypes", type = Accessor.Type.GETTER)
	public List<RuleActionDefinitionRuleTypeMappingModel> getRuleTypes()
	{
		return getPersistenceContext().getPropertyValue(RULETYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.translatorId</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the translatorId
	 */
	@Accessor(qualifier = "translatorId", type = Accessor.Type.GETTER)
	public String getTranslatorId()
	{
		return getPersistenceContext().getPropertyValue(TRANSLATORID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionDefinition.translatorParameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the translatorParameters
	 */
	@Accessor(qualifier = "translatorParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getTranslatorParameters()
	{
		return getPersistenceContext().getPropertyValue(TRANSLATORPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.breadcrumb</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the breadcrumb
	 */
	@Accessor(qualifier = "breadcrumb", type = Accessor.Type.SETTER)
	public void setBreadcrumb(final String value)
	{
		setBreadcrumb(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.breadcrumb</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the breadcrumb
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "breadcrumb", type = Accessor.Type.SETTER)
	public void setBreadcrumb(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(BREADCRUMB, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.categories</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final List<RuleActionDefinitionCategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.parameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.SETTER)
	public void setParameters(final List<RuleActionDefinitionParameterModel> value)
	{
		getPersistenceContext().setPropertyValue(PARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.ruleTypes</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleTypes
	 */
	@Accessor(qualifier = "ruleTypes", type = Accessor.Type.SETTER)
	public void setRuleTypes(final List<RuleActionDefinitionRuleTypeMappingModel> value)
	{
		getPersistenceContext().setPropertyValue(RULETYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.translatorId</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the translatorId
	 */
	@Accessor(qualifier = "translatorId", type = Accessor.Type.SETTER)
	public void setTranslatorId(final String value)
	{
		getPersistenceContext().setPropertyValue(TRANSLATORID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionDefinition.translatorParameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the translatorParameters
	 */
	@Accessor(qualifier = "translatorParameters", type = Accessor.Type.SETTER)
	public void setTranslatorParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(TRANSLATORPARAMETERS, value);
	}
	
}
