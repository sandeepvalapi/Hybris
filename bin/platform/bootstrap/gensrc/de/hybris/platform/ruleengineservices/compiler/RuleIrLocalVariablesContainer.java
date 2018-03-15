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

import java.io.Serializable;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import java.util.Map;

public  class RuleIrLocalVariablesContainer  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RuleIrLocalVariablesContainer.variables</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,RuleIrVariable> variables;
	
	public RuleIrLocalVariablesContainer()
	{
		// default constructor
	}
	
		
	
	public void setVariables(final Map<String,RuleIrVariable> variables)
	{
		this.variables = variables;
	}

		
	
	public Map<String,RuleIrVariable> getVariables() 
	{
		return variables;
	}
	


}
