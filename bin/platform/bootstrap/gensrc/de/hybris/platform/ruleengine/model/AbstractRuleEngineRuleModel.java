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
package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractRuleEngineRule first defined at extension ruleengine.
 * <p>
 * AbstractRuleEngineRule is an abstraction for a rule being executed in the rule engine.
 */
@SuppressWarnings("all")
public class AbstractRuleEngineRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractRuleEngineRule";
	
	/**<i>Generated relation code constant for relation <code>SourceRule2DroolsRule</code> defining source attribute <code>sourceRule</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _SOURCERULE2DROOLSRULE = "SourceRule2DroolsRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.uuid</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String UUID = "uuid";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.code</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.active</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.ruleContent</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULECONTENT = "ruleContent";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.ruleType</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULETYPE = "ruleType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.checksum</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CHECKSUM = "checksum";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.currentVersion</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CURRENTVERSION = "currentVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.version</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.ruleParameters</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULEPARAMETERS = "ruleParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.ruleGroupCode</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULEGROUPCODE = "ruleGroupCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.sourceRule</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String SOURCERULE = "sourceRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleEngineRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PROMOTION = "promotion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractRuleEngineRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractRuleEngineRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _ruleType initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _uuid initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _version initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public AbstractRuleEngineRuleModel(final String _code, final RuleType _ruleType, final String _uuid, final Long _version)
	{
		super();
		setCode(_code);
		setRuleType(_ruleType);
		setUuid(_uuid);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _ruleType initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _uuid initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 * @param _version initial attribute declared by type <code>AbstractRuleEngineRule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public AbstractRuleEngineRuleModel(final String _code, final ItemModel _owner, final RuleType _ruleType, final String _uuid, final Long _version)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setRuleType(_ruleType);
		setUuid(_uuid);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.active</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the active - Is rule active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.checksum</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.GETTER)
	public String getChecksum()
	{
		return getPersistenceContext().getPropertyValue(CHECKSUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.code</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the code - unique rule code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.currentVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the currentVersion - Is this rule current?
	 */
	@Accessor(qualifier = "currentVersion", type = Accessor.Type.GETTER)
	public Boolean getCurrentVersion()
	{
		return getPersistenceContext().getPropertyValue(CURRENTVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.GETTER)
	public Integer getMaxAllowedRuns()
	{
		return getPersistenceContext().getPropertyValue(MAXALLOWEDRUNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the promotion - Promotion created for this rule.
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
	public RuleBasedPromotionModel getPromotion()
	{
		return getPersistenceContext().getPropertyValue(PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.ruleContent</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the ruleContent - contains rule syntax
	 */
	@Accessor(qualifier = "ruleContent", type = Accessor.Type.GETTER)
	public String getRuleContent()
	{
		return getPersistenceContext().getPropertyValue(RULECONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.ruleGroupCode</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the ruleGroupCode - The code of the rule group that this rule is part of (derived from the AbstractRule).
	 */
	@Accessor(qualifier = "ruleGroupCode", type = Accessor.Type.GETTER)
	public String getRuleGroupCode()
	{
		return getPersistenceContext().getPropertyValue(RULEGROUPCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.ruleParameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the ruleParameters
	 */
	@Accessor(qualifier = "ruleParameters", type = Accessor.Type.GETTER)
	public String getRuleParameters()
	{
		return getPersistenceContext().getPropertyValue(RULEPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.ruleType</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the ruleType - Rule type
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
	public RuleType getRuleType()
	{
		return getPersistenceContext().getPropertyValue(RULETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.sourceRule</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the sourceRule
	 */
	@Accessor(qualifier = "sourceRule", type = Accessor.Type.GETTER)
	public AbstractRuleModel getSourceRule()
	{
		return getPersistenceContext().getPropertyValue(SOURCERULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.uuid</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the uuid
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
	public String getUuid()
	{
		return getPersistenceContext().getPropertyValue(UUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleEngineRule.version</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the version - unique rule version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Long getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.active</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the active - Is rule active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.checksum</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.SETTER)
	public void setChecksum(final String value)
	{
		getPersistenceContext().setPropertyValue(CHECKSUM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractRuleEngineRule.code</code> attribute defined at extension <code>ruleengine</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique rule code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.currentVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the currentVersion - Is this rule current?
	 */
	@Accessor(qualifier = "currentVersion", type = Accessor.Type.SETTER)
	public void setCurrentVersion(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CURRENTVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.maxAllowedRuns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the maxAllowedRuns - Number of times rule can be applied
	 */
	@Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.SETTER)
	public void setMaxAllowedRuns(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXALLOWEDRUNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the messageFired - Message for fired rule.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.messageFired</code> attribute defined at extension <code>ruleengineservices</code>. 
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
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the promotion - Promotion created for this rule.
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
	public void setPromotion(final RuleBasedPromotionModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.ruleContent</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the ruleContent - contains rule syntax
	 */
	@Accessor(qualifier = "ruleContent", type = Accessor.Type.SETTER)
	public void setRuleContent(final String value)
	{
		getPersistenceContext().setPropertyValue(RULECONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.ruleGroupCode</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleGroupCode - The code of the rule group that this rule is part of (derived from the AbstractRule).
	 */
	@Accessor(qualifier = "ruleGroupCode", type = Accessor.Type.SETTER)
	public void setRuleGroupCode(final String value)
	{
		getPersistenceContext().setPropertyValue(RULEGROUPCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.ruleParameters</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleParameters
	 */
	@Accessor(qualifier = "ruleParameters", type = Accessor.Type.SETTER)
	public void setRuleParameters(final String value)
	{
		getPersistenceContext().setPropertyValue(RULEPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.ruleType</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the ruleType - Rule type
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
	public void setRuleType(final RuleType value)
	{
		getPersistenceContext().setPropertyValue(RULETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.sourceRule</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the sourceRule
	 */
	@Accessor(qualifier = "sourceRule", type = Accessor.Type.SETTER)
	public void setSourceRule(final AbstractRuleModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCERULE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.uuid</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the uuid
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
	public void setUuid(final String value)
	{
		getPersistenceContext().setPropertyValue(UUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleEngineRule.version</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the version - unique rule version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Long value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
