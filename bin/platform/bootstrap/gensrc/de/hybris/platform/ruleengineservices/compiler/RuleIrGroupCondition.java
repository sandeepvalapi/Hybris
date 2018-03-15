/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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

import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import java.util.List;

public  class RuleIrGroupCondition extends RuleIrCondition 
{

 

	/** <i>Generated property</i> for <code>RuleIrGroupCondition.operator</code> property defined at extension <code>ruleengineservices</code>. */
		
	private RuleIrGroupOperator operator;

	/** <i>Generated property</i> for <code>RuleIrGroupCondition.children</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<RuleIrCondition> children;
	
	public RuleIrGroupCondition()
	{
		// default constructor
	}
	
		
	
	public void setOperator(final RuleIrGroupOperator operator)
	{
		this.operator = operator;
	}

		
	
	public RuleIrGroupOperator getOperator() 
	{
		return operator;
	}
	
		
	
	public void setChildren(final List<RuleIrCondition> children)
	{
		this.children = children;
	}

		
	
	public List<RuleIrCondition> getChildren() 
	{
		return children;
	}
	


}
