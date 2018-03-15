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
package de.hybris.platform.ticketsystem.events;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.ticket.enums.EventType;
import java.util.Date;
import java.util.List;

public  class SessionEvent extends AbstractEvent 
{

 

	/** <i>Generated property</i> for <code>SessionEvent.customer</code> property defined at extension <code>ticketsystem</code>. */
		
	private UserModel customer;

	/** <i>Generated property</i> for <code>SessionEvent.agent</code> property defined at extension <code>ticketsystem</code>. */
		
	private UserModel agent;

	/** <i>Generated property</i> for <code>SessionEvent.agentGroups</code> property defined at extension <code>ticketsystem</code>. */
		
	private List<PrincipalGroupModel> agentGroups;

	/** <i>Generated property</i> for <code>SessionEvent.site</code> property defined at extension <code>ticketsystem</code>. */
		
	private BaseSiteModel site;

	/** <i>Generated property</i> for <code>SessionEvent.sessionID</code> property defined at extension <code>ticketsystem</code>. */
		
	private String sessionID;

	/** <i>Generated property</i> for <code>SessionEvent.createdAt</code> property defined at extension <code>ticketsystem</code>. */
		
	private Date createdAt;

	/** <i>Generated property</i> for <code>SessionEvent.eventType</code> property defined at extension <code>ticketsystem</code>. */
		
	private EventType eventType;
	
	public SessionEvent()
	{
		// default constructor
	}
	
		
	
	public void setCustomer(final UserModel customer)
	{
		this.customer = customer;
	}

		
	
	public UserModel getCustomer() 
	{
		return customer;
	}
	
		
	
	public void setAgent(final UserModel agent)
	{
		this.agent = agent;
	}

		
	
	public UserModel getAgent() 
	{
		return agent;
	}
	
		
	
	public void setAgentGroups(final List<PrincipalGroupModel> agentGroups)
	{
		this.agentGroups = agentGroups;
	}

		
	
	public List<PrincipalGroupModel> getAgentGroups() 
	{
		return agentGroups;
	}
	
		
	
	public void setSite(final BaseSiteModel site)
	{
		this.site = site;
	}

		
	
	public BaseSiteModel getSite() 
	{
		return site;
	}
	
		
	
	public void setSessionID(final String sessionID)
	{
		this.sessionID = sessionID;
	}

		
	
	public String getSessionID() 
	{
		return sessionID;
	}
	
		
	
	public void setCreatedAt(final Date createdAt)
	{
		this.createdAt = createdAt;
	}

		
	
	public Date getCreatedAt() 
	{
		return createdAt;
	}
	
		
	
	public void setEventType(final EventType eventType)
	{
		this.eventType = eventType;
	}

		
	
	public EventType getEventType() 
	{
		return eventType;
	}
	


}
