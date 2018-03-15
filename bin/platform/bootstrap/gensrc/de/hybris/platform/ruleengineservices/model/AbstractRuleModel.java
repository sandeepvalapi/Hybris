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
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type AbstractRule first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class AbstractRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractRule";
	
	/**<i>Generated relation code constant for relation <code>RuleGroup2AbstractRule</code> defining source attribute <code>ruleGroup</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _RULEGROUP2ABSTRACTRULE = "RuleGroup2AbstractRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.uuid</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String UUID = "uuid";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.code</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.name</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.description</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.startDate</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.endDate</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.priority</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.stackable</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String STACKABLE = "stackable";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.status</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.version</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.deployments</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEPLOYMENTS = "deployments";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULEGROUP = "ruleGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRule.engineRules</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ENGINERULES = "engineRules";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public AbstractRuleModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uuid initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public AbstractRuleModel(final String _code, final ItemModel _owner, final String _uuid)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setUuid(_uuid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.code</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the code - Rule code that defines the rule uniquely, cannot be changed after the rule is created
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.deployments</code> dynamic attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the deployments - List of the current deployments.
	 */
	@Accessor(qualifier = "deployments", type = Accessor.Type.GETTER)
	public List<AbstractRulesModuleModel> getDeployments()
	{
		return getPersistenceContext().getDynamicValue(this,DEPLOYMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the description - Rule description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.description</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.endDate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the endDate - Date on which this promotion stops being available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.engineRules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the engineRules
	 */
	@Accessor(qualifier = "engineRules", type = Accessor.Type.GETTER)
	public Set<AbstractRuleEngineRuleModel> getEngineRules()
	{
		return getPersistenceContext().getPropertyValue(ENGINERULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.GETTER)
	public Integer getMaxAllowedRuns()
	{
		return getPersistenceContext().getPropertyValue(MAXALLOWEDRUNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the name - Rule name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the priority - priority (higher priority gets applied first)
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the ruleGroup
	 */
	@Accessor(qualifier = "ruleGroup", type = Accessor.Type.GETTER)
	public RuleGroupModel getRuleGroup()
	{
		return getPersistenceContext().getPropertyValue(RULEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.stackable</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the stackable - whether the rule is stackable
	 */
	@Accessor(qualifier = "stackable", type = Accessor.Type.GETTER)
	public Boolean getStackable()
	{
		return getPersistenceContext().getPropertyValue(STACKABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.startDate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the startDate - Date on which this promotion becomes available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.status</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the status - Status of a rule
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public RuleStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.uuid</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the uuid - Rule uuid that defines the rule uniquely, cannot be changed after the rule is created
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
	public String getUuid()
	{
		return getPersistenceContext().getPropertyValue(UUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRule.version</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the version - rule version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Long getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractRule.code</code> attribute defined at extension <code>ruleengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Rule code that defines the rule uniquely, cannot be changed after the rule is created
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description - Rule description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.description</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>AbstractRule.endDate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the endDate - Date on which this promotion stops being available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.engineRules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the engineRules
	 */
	@Accessor(qualifier = "engineRules", type = Accessor.Type.SETTER)
	public void setEngineRules(final Set<AbstractRuleEngineRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(ENGINERULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.SETTER)
	public void setMaxAllowedRuns(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXALLOWEDRUNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>AbstractRule.name</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the name - Rule name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.name</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>AbstractRule.priority</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the priority - priority (higher priority gets applied first)
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.ruleGroup</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleGroup
	 */
	@Accessor(qualifier = "ruleGroup", type = Accessor.Type.SETTER)
	public void setRuleGroup(final RuleGroupModel value)
	{
		getPersistenceContext().setPropertyValue(RULEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.stackable</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the stackable - whether the rule is stackable
	 */
	@Accessor(qualifier = "stackable", type = Accessor.Type.SETTER)
	public void setStackable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(STACKABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.startDate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the startDate - Date on which this promotion becomes available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.status</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the status - Status of a rule
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final RuleStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractRule.uuid</code> attribute defined at extension <code>ruleengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the uuid - Rule uuid that defines the rule uniquely, cannot be changed after the rule is created
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
	public void setUuid(final String value)
	{
		getPersistenceContext().setPropertyValue(UUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRule.version</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the version - rule version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Long value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
