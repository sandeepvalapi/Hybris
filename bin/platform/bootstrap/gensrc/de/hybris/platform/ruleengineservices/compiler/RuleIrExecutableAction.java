/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
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

import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import java.util.Map;

public  class RuleIrExecutableAction extends RuleIrAction 
{

 

	/** <i>Generated property</i> for <code>RuleIrExecutableAction.actionId</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String actionId;

	/** <i>Generated property</i> for <code>RuleIrExecutableAction.actionParameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,Object> actionParameters;
	
	public RuleIrExecutableAction()
	{
		// default constructor
	}
	
		
	
	public void setActionId(final String actionId)
	{
		this.actionId = actionId;
	}

		
	
	public String getActionId() 
	{
		return actionId;
	}
	
		
	
	public void setActionParameters(final Map<String,Object> actionParameters)
	{
		this.actionParameters = actionParameters;
	}

		
	
	public Map<String,Object> getActionParameters() 
	{
		return actionParameters;
	}
	


}
