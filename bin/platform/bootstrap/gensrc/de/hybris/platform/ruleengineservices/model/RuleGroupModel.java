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
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type RuleGroup first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleGroup.code</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleGroup.description</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleGroup.rules</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULES = "rules";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleGroup.ruleTemplates</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULETEMPLATES = "ruleTemplates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RuleGroup</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public RuleGroupModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RuleGroup</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleGroupModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleGroup.code</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the code - the code of the rule group.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleGroup.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the description - the description of this rule group.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleGroup.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @param loc the value localization key 
	 * @return the description - the description of this rule group.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleGroup.rules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rules
	 */
	@Accessor(qualifier = "rules", type = Accessor.Type.GETTER)
	public Set<AbstractRuleModel> getRules()
	{
		return getPersistenceContext().getPropertyValue(RULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleGroup.ruleTemplates</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ruleTemplates
	 */
	@Accessor(qualifier = "ruleTemplates", type = Accessor.Type.GETTER)
	public Set<AbstractRuleTemplateModel> getRuleTemplates()
	{
		return getPersistenceContext().getPropertyValue(RULETEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleGroup.code</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the code - the code of the rule group.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleGroup.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description - the description of this rule group.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>RuleGroup.description</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the description - the description of this rule group.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleGroup.rules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the rules
	 */
	@Accessor(qualifier = "rules", type = Accessor.Type.SETTER)
	public void setRules(final Set<AbstractRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(RULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleGroup.ruleTemplates</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleTemplates
	 */
	@Accessor(qualifier = "ruleTemplates", type = Accessor.Type.SETTER)
	public void setRuleTemplates(final Set<AbstractRuleTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(RULETEMPLATES, value);
	}
	
}
