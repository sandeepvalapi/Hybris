/*
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
package de.hybris.platform.ruleengineservices.rrd;

import java.util.Map;

/**
 * Represents a Rule Group execution during rule evaluation (gets inserted as one fact per rule group, the code being the rule group's code).
		This is used as a control fact to track and check the rules that have been executed for this group. Use the custom (see template) method allowedToExecute(RuleConfigurationRRD config) for checking whether a specific rule is allowed to execute.
 */
public  class RuleGroupExecutionRRD  implements java.io.Serializable 
{


	/** the code of the rule group this execution control fact represents.<br/><br/><i>Generated property</i> for <code>RuleGroupExecutionRRD.code</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String code;

	/** contains the map of rule codes and how often the rule has been executed.<br/><br/><i>Generated property</i> for <code>RuleGroupExecutionRRD.executedRules</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,Integer> executedRules;
	
	public RuleGroupExecutionRRD()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setExecutedRules(final Map<String,Integer> executedRules)
	{
		this.executedRules = executedRules;
	}

		
	
	public Map<String,Integer> getExecutedRules() 
	{
		return executedRules;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null || !this.getClass().equals(o.getClass())) {
          return false;
        } else if (o == this) {
          return true;
        }

		final RuleGroupExecutionRRD other = (RuleGroupExecutionRRD) o;
		return new org.apache.commons.lang.builder.EqualsBuilder()
		.append(getCode(), other.getCode()) 
		.isEquals();
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getCode()) 
		.toHashCode();
	}
	/**
	 * Determines whether the given rule (identified by its config object) is allowed to be executed. When the already triggered rule
	 * gets triggered again, it is allowed to be executed as many times as specified by the rules 'maximum allowed executions' attribute.
	 */
	public boolean allowedToExecute(final RuleConfigurationRRD ruleConfig)
	{
		if (this.executedRules == null)
		{
			// first execution of the group
			return true;
		}
		else
		{
			if (this.executedRules.entrySet().isEmpty())
			{
				// first execution of the group
				return true;
			}
			else
			{
				// unless this rule has been triggered already and has more than 1 executions allowed
				final Integer current = this.executedRules.get(ruleConfig.getRuleCode());
				if (current == null)
				{
					// this rule hasn't been yet tracked
					return true;
				}
				Integer max = ruleConfig.getMaxAllowedRuns();
				if (max == null)
				{
					max = Integer.valueOf(1);
				}
				return current.compareTo(max) < 0;
			}
		}
	}
}
