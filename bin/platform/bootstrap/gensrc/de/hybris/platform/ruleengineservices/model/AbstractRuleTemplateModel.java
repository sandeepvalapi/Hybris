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
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractRuleTemplate first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class AbstractRuleTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractRuleTemplate";
	
	/**<i>Generated relation code constant for relation <code>RuleGroup2AbstractRuleTemplate</code> defining source attribute <code>ruleGroup</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _RULEGROUP2ABSTRACTRULETEMPLATE = "RuleGroup2AbstractRuleTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.code</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.name</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.description</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleTemplate.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULEGROUP = "ruleGroup";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractRuleTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractRuleTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleTemplate</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public AbstractRuleTemplateModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleTemplate</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractRuleTemplateModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.code</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the code - Rule code that defines the rule uniquely
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the description - Rule description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the description - Rule description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.GETTER)
	public Integer getMaxAllowedRuns()
	{
		return getPersistenceContext().getPropertyValue(MAXALLOWEDRUNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the messageFired - Message for fired rule.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGEFIRED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the name - Rule name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Rule name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleTemplate.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the ruleGroup
	 */
	@Accessor(qualifier = "ruleGroup", type = Accessor.Type.GETTER)
	public RuleGroupModel getRuleGroup()
	{
		return getPersistenceContext().getPropertyValue(RULEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.code</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the code - Rule code that defines the rule uniquely
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description - Rule description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description - Rule description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.SETTER)
	public void setMaxAllowedRuns(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXALLOWEDRUNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the messageFired - Message for fired rule.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGEFIRED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name - Rule name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name - Rule name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleTemplate.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleGroup
	 */
	@Accessor(qualifier = "ruleGroup", type = Accessor.Type.SETTER)
	public void setRuleGroup(final RuleGroupModel value)
	{
		getPersistenceContext().setPropertyValue(RULEGROUP, value);
	}
	
}
