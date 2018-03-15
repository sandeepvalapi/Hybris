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
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RuleConditionDefinitionRuleTypeMapping first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleConditionDefinitionRuleTypeMappingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleConditionDefinitionRuleTypeMapping";
	
	/**<i>Generated relation code constant for relation <code>RuleConditionDefinition2RuleTypeMappingRelation</code> defining source attribute <code>definition</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _RULECONDITIONDEFINITION2RULETYPEMAPPINGRELATION = "RuleConditionDefinition2RuleTypeMappingRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionRuleTypeMapping.ruleType</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String RULETYPE = "ruleType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleConditionDefinitionRuleTypeMapping.definition</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String DEFINITION = "definition";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleConditionDefinitionRuleTypeMappingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleConditionDefinitionRuleTypeMappingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleConditionDefinitionRuleTypeMappingModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionRuleTypeMapping.definition</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the definition
	 */
	@Accessor(qualifier = "definition", type = Accessor.Type.GETTER)
	public RuleConditionDefinitionModel getDefinition()
	{
		return getPersistenceContext().getPropertyValue(DEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleConditionDefinitionRuleTypeMapping.ruleType</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getRuleType()
	{
		return getPersistenceContext().getPropertyValue(RULETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionRuleTypeMapping.definition</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the definition
	 */
	@Accessor(qualifier = "definition", type = Accessor.Type.SETTER)
	public void setDefinition(final RuleConditionDefinitionModel value)
	{
		getPersistenceContext().setPropertyValue(DEFINITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleConditionDefinitionRuleTypeMapping.ruleType</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
	public void setRuleType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(RULETYPE, value);
	}
	
}
