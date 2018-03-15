/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
 */
package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrPatternCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;

public abstract  class AbstractRuleIrAttributeCondition extends AbstractRuleIrPatternCondition 
{

 

	/** <i>Generated property</i> for <code>AbstractRuleIrAttributeCondition.attribute</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String attribute;

	/** <i>Generated property</i> for <code>AbstractRuleIrAttributeCondition.operator</code> property defined at extension <code>ruleengineservices</code>. */
		
	private RuleIrAttributeOperator operator;
	
	public AbstractRuleIrAttributeCondition()
	{
		// default constructor
	}
	
		
	
	public void setAttribute(final String attribute)
	{
		this.attribute = attribute;
	}

		
	
	public String getAttribute() 
	{
		return attribute;
	}
	
		
	
	public void setOperator(final RuleIrAttributeOperator operator)
	{
		this.operator = operator;
	}

		
	
	public RuleIrAttributeOperator getOperator() 
	{
		return operator;
	}
	


}
