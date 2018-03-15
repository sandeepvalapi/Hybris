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
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type RuleConditionDefinitionParameter first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleConditionDefinitionParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleConditionDefinitionParameter";
	
	/**<i>Generated relation code constant for relation <code>RuleConditionDefinition2ParametersRelation</code> defining source attribute <code>definition</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _RULECONDITIONDEFINITION2PARAMETERSRELATION = "RuleConditionDefinition2ParametersRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.id</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.name</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.description</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.priority</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.type</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.value</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.required</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String REQUIRED = "required";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.validators</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String VALIDATORS = "validators";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.filters</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String FILTERS = "filters";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.defaultEditor</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEFAULTEDITOR = "defaultEditor";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.definitionPOS</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEFINITIONPOS = "definitionPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionParameter.definition</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEFINITION = "definition";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleConditionDefinitionParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleConditionDefinitionParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleConditionDefinitionParameterModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.defaultEditor</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the defaultEditor - default editor for the parameter
	 */
	@Accessor(qualifier = "defaultEditor", type = Accessor.Type.GETTER)
	public String getDefaultEditor()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTEDITOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.definition</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the definition
	 */
	@Accessor(qualifier = "definition", type = Accessor.Type.GETTER)
	public RuleConditionDefinitionModel getDefinition()
	{
		return getPersistenceContext().getPropertyValue(DEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.filters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the filters - The map defines narrowing filtering rules. For every entry: key corresponds to the type field name to narrow for, value corresponds to RuleConditionDefinitionParameter.id to narrow by
	 */
	@Accessor(qualifier = "filters", type = Accessor.Type.GETTER)
	public Map<String,String> getFilters()
	{
		return getPersistenceContext().getPropertyValue(FILTERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.required</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the required - whether the parameter is required
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.GETTER)
	public Boolean getRequired()
	{
		return getPersistenceContext().getPropertyValue(REQUIRED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.type</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public String getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.validators</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the validators
	 */
	@Accessor(qualifier = "validators", type = Accessor.Type.GETTER)
	public List<String> getValidators()
	{
		return getPersistenceContext().getPropertyValue(VALIDATORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionParameter.value</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.defaultEditor</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the defaultEditor - default editor for the parameter
	 */
	@Accessor(qualifier = "defaultEditor", type = Accessor.Type.SETTER)
	public void setDefaultEditor(final String value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTEDITOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.definition</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the definition
	 */
	@Accessor(qualifier = "definition", type = Accessor.Type.SETTER)
	public void setDefinition(final RuleConditionDefinitionModel value)
	{
		getPersistenceContext().setPropertyValue(DEFINITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.filters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the filters - The map defines narrowing filtering rules. For every entry: key corresponds to the type field name to narrow for, value corresponds to RuleConditionDefinitionParameter.id to narrow by
	 */
	@Accessor(qualifier = "filters", type = Accessor.Type.SETTER)
	public void setFilters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(FILTERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.id</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.required</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the required - whether the parameter is required
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.SETTER)
	public void setRequired(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUIRED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.type</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final String value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.validators</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the validators
	 */
	@Accessor(qualifier = "validators", type = Accessor.Type.SETTER)
	public void setValidators(final List<String> value)
	{
		getPersistenceContext().setPropertyValue(VALIDATORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionParameter.value</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
