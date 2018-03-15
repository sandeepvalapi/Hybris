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
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

/**
 * Generated model class for type DroolsRule first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class DroolsRuleModel extends AbstractRuleEngineRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DroolsRule";
	
	/**<i>Generated relation code constant for relation <code>DroolsKIEBase2Rule</code> defining source attribute <code>kieBase</code> in extension <code>ruleengine</code>.</i>*/
	public static final String _DROOLSKIEBASE2RULE = "DroolsKIEBase2Rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsRule.rulePackage</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULEPACKAGE = "rulePackage";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsRule.globals</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String GLOBALS = "globals";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsRule.kieBase</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIEBASE = "kieBase";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DroolsRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DroolsRuleModel(final ItemModelContext ctx)
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
	public DroolsRuleModel(final String _code, final RuleType _ruleType, final String _uuid, final Long _version)
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
	public DroolsRuleModel(final String _code, final ItemModel _owner, final RuleType _ruleType, final String _uuid, final Long _version)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setRuleType(_ruleType);
		setUuid(_uuid);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsRule.globals</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the globals - Contains a map of all globals and their respective spring bean id as needed by this rule. 
	 * 					The key is the name under which the global will be registered, the value is id/alias of 
	 * 					the spring bean to use as a global.
	 */
	@Accessor(qualifier = "globals", type = Accessor.Type.GETTER)
	public Map<String,String> getGlobals()
	{
		return getPersistenceContext().getPropertyValue(GLOBALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsRule.kieBase</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the kieBase
	 */
	@Accessor(qualifier = "kieBase", type = Accessor.Type.GETTER)
	public DroolsKIEBaseModel getKieBase()
	{
		return getPersistenceContext().getPropertyValue(KIEBASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsRule.rulePackage</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the rulePackage - The package name of the rule as defined in the drl file content.
	 */
	@Accessor(qualifier = "rulePackage", type = Accessor.Type.GETTER)
	public String getRulePackage()
	{
		return getPersistenceContext().getPropertyValue(RULEPACKAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsRule.globals</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the globals - Contains a map of all globals and their respective spring bean id as needed by this rule. 
	 * 					The key is the name under which the global will be registered, the value is id/alias of 
	 * 					the spring bean to use as a global.
	 */
	@Accessor(qualifier = "globals", type = Accessor.Type.SETTER)
	public void setGlobals(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(GLOBALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsRule.kieBase</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the kieBase
	 */
	@Accessor(qualifier = "kieBase", type = Accessor.Type.SETTER)
	public void setKieBase(final DroolsKIEBaseModel value)
	{
		getPersistenceContext().setPropertyValue(KIEBASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsRule.rulePackage</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the rulePackage - The package name of the rule as defined in the drl file content.
	 */
	@Accessor(qualifier = "rulePackage", type = Accessor.Type.SETTER)
	public void setRulePackage(final String value)
	{
		getPersistenceContext().setPropertyValue(RULEPACKAGE, value);
	}
	
}
