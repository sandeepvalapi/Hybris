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
package de.hybris.platform.commerceservices.organization.services;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import java.util.Set;

public  class OrgUnitMemberParameter<T>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrgUnitMemberParameter<T>.uid</code> property defined at extension <code>commerceservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>OrgUnitMemberParameter<T>.members</code> property defined at extension <code>commerceservices</code>. */
		
	private Set<T> members;

	/** <i>Generated property</i> for <code>OrgUnitMemberParameter<T>.type</code> property defined at extension <code>commerceservices</code>. */
		
	private Class<T> type;

	/** <i>Generated property</i> for <code>OrgUnitMemberParameter<T>.pageableData</code> property defined at extension <code>commerceservices</code>. */
		
	private PageableData pageableData;
	
	public OrgUnitMemberParameter()
	{
		// default constructor
	}
	
		
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

		
	
	public String getUid() 
	{
		return uid;
	}
	
		
	
	public void setMembers(final Set<T> members)
	{
		this.members = members;
	}

		
	
	public Set<T> getMembers() 
	{
		return members;
	}
	
		
	
	public void setType(final Class<T> type)
	{
		this.type = type;
	}

		
	
	public Class<T> getType() 
	{
		return type;
	}
	
		
	
	public void setPageableData(final PageableData pageableData)
	{
		this.pageableData = pageableData;
	}

		
	
	public PageableData getPageableData() 
	{
		return pageableData;
	}
	


}
