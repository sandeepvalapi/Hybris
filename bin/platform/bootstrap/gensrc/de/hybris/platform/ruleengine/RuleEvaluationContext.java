/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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
package de.hybris.platform.ruleengine;

import java.io.Serializable;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.util.Map;
import java.util.Set;

public  class RuleEvaluationContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** defines the context of the rule engine (e.g. which module to evaluate etc)<br/><br/><i>Generated property</i> for <code>RuleEvaluationContext.ruleEngineContext</code> property defined at extension <code>ruleengine</code>. */
		
	private AbstractRuleEngineContextModel ruleEngineContext;

	/** <i>Generated property</i> for <code>RuleEvaluationContext.facts</code> property defined at extension <code>ruleengine</code>. */
		
	private Set<Object> facts;

	/** for Drools this map contains entries for global identifier and the reference to the global.<br/><br/><i>Generated property</i> for <code>RuleEvaluationContext.globals</code> property defined at extension <code>ruleengine</code>. */
		
	private Map<String,Object> globals;

	/** for Drools this optional attribute can contain an instance of an AgendaFilter.<br/><br/><i>Generated property</i> for <code>RuleEvaluationContext.filter</code> property defined at extension <code>ruleengine</code>. */
		
	private Object filter;

	/** contains a set of event listeners (e.g. for Drools this can be AgendaEventListener, RuleRuntimeEventListener or ProcessEventListener)<br/><br/><i>Generated property</i> for <code>RuleEvaluationContext.eventListeners</code> property defined at extension <code>ruleengine</code>. */
		
	private Set<Object> eventListeners;
	
	public RuleEvaluationContext()
	{
		// default constructor
	}
	
		
	
	public void setRuleEngineContext(final AbstractRuleEngineContextModel ruleEngineContext)
	{
		this.ruleEngineContext = ruleEngineContext;
	}

		
	
	public AbstractRuleEngineContextModel getRuleEngineContext() 
	{
		return ruleEngineContext;
	}
	
		
	
	public void setFacts(final Set<Object> facts)
	{
		this.facts = facts;
	}

		
	
	public Set<Object> getFacts() 
	{
		return facts;
	}
	
		
	
	public void setGlobals(final Map<String,Object> globals)
	{
		this.globals = globals;
	}

		
	
	public Map<String,Object> getGlobals() 
	{
		return globals;
	}
	
		
	
	public void setFilter(final Object filter)
	{
		this.filter = filter;
	}

		
	
	public Object getFilter() 
	{
		return filter;
	}
	
		
	
	public void setEventListeners(final Set<Object> eventListeners)
	{
		this.eventListeners = eventListeners;
	}

		
	
	public Set<Object> getEventListeners() 
	{
		return eventListeners;
	}
	


}
