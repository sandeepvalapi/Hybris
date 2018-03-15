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

import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrBooleanCondition;
import java.util.Map;

public  class RuleIrExecutableCondition extends AbstractRuleIrBooleanCondition 
{

 

	/** <i>Generated property</i> for <code>RuleIrExecutableCondition.conditionId</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String conditionId;

	/** <i>Generated property</i> for <code>RuleIrExecutableCondition.conditionParameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,Object> conditionParameters;
	
	public RuleIrExecutableCondition()
	{
		// default constructor
	}
	
		
	
	public void setConditionId(final String conditionId)
	{
		this.conditionId = conditionId;
	}

		
	
	public String getConditionId() 
	{
		return conditionId;
	}
	
		
	
	public void setConditionParameters(final Map<String,Object> conditionParameters)
	{
		this.conditionParameters = conditionParameters;
	}

		
	
	public Map<String,Object> getConditionParameters() 
	{
		return conditionParameters;
	}
	


}
