/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import java.util.LinkedHashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AbstractActionedRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>AbstractActionedRAO.actions</code> property defined at extension <code>ruleengineservices</code>. */
	private LinkedHashSet<AbstractRuleActionRAO> actions;
		
	public AbstractActionedRAO()
	{
		// default constructor
	}
	
		
	public void setActions(final LinkedHashSet<AbstractRuleActionRAO> actions)
	{
		this.actions = actions;
	}
	
		
	public LinkedHashSet<AbstractRuleActionRAO> getActions() 
	{
		return actions;
	}
		
		@Override
	public boolean equals(Object that)
	{
		return EqualsBuilder.reflectionEquals(this, that);
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(this);
	}
}