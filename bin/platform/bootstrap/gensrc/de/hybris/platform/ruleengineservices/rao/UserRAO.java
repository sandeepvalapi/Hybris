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
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import de.hybris.platform.personalizationpromotions.rao.CxPromotionActionResultRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import java.util.List;
import java.util.Set;

public  class UserRAO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UserRAO.id</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>UserRAO.pk</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String pk;

	/** <i>Generated property</i> for <code>UserRAO.orders</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Set<AbstractOrderRAO> orders;

	/** <i>Generated property</i> for <code>UserRAO.groups</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Set<UserGroupRAO> groups;

	/** <i>Generated property</i> for <code>UserRAO.cxPromotionActionResults</code> property defined at extension <code>personalizationpromotions</code>. */
		
	private List<CxPromotionActionResultRAO> cxPromotionActionResults;
	
	public UserRAO()
	{
		// default constructor
	}
	
		
	
	public void setId(final String id)
	{
		this.id = id;
	}

		
	
	public String getId() 
	{
		return id;
	}
	
		
	
	public void setPk(final String pk)
	{
		this.pk = pk;
	}

		
	
	public String getPk() 
	{
		return pk;
	}
	
		
	
	public void setOrders(final Set<AbstractOrderRAO> orders)
	{
		this.orders = orders;
	}

		
	
	public Set<AbstractOrderRAO> getOrders() 
	{
		return orders;
	}
	
		
	
	public void setGroups(final Set<UserGroupRAO> groups)
	{
		this.groups = groups;
	}

		
	
	public Set<UserGroupRAO> getGroups() 
	{
		return groups;
	}
	
		
	
	public void setCxPromotionActionResults(final List<CxPromotionActionResultRAO> cxPromotionActionResults)
	{
		this.cxPromotionActionResults = cxPromotionActionResults;
	}

		
	
	public List<CxPromotionActionResultRAO> getCxPromotionActionResults() 
	{
		return cxPromotionActionResults;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final UserRAO other = (UserRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getId(), other.getId()) 
			.append(getPk(), other.getPk()) 
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getId()) 
		.append(getPk()) 
		.toHashCode();
	}

}
