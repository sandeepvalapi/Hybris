/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
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
 *  
 */
package de.hybris.platform.ticket.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.events.model.CsTicketResolutionEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type CsTicket first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicket";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.ticketID</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKETID = "ticketID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.customer</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.order</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.headline</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String HEADLINE = "headline";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.category</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String CATEGORY = "category";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.priority</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.state</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.assignedAgent</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ASSIGNEDAGENT = "assignedAgent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.assignedGroup</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ASSIGNEDGROUP = "assignedGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.resolution</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String RESOLUTION = "resolution";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.baseSite</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.events</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EVENTS = "events";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicket.retentionDate</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String RETENTIONDATE = "retentionDate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _ticketID initial attribute declared by type <code>CsTicket</code> at extension <code>ticketsystem</code>
	 */
	@Deprecated
	public CsTicketModel(final ItemModel _owner, final String _ticketID)
	{
		super();
		setOwner(_owner);
		setTicketID(_ticketID);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.assignedAgent</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the assignedAgent
	 */
	@Accessor(qualifier = "assignedAgent", type = Accessor.Type.GETTER)
	public EmployeeModel getAssignedAgent()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNEDAGENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.assignedGroup</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the assignedGroup
	 */
	@Accessor(qualifier = "assignedGroup", type = Accessor.Type.GETTER)
	public CsAgentGroupModel getAssignedGroup()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNEDGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.baseSite</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.category</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.GETTER)
	public CsTicketCategory getCategory()
	{
		return getPersistenceContext().getPropertyValue(CATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.customer</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public UserModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.events</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the events
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "events", type = Accessor.Type.GETTER)
	public List<CsTicketEventModel> getEvents()
	{
		return getPersistenceContext().getPropertyValue(EVENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.headline</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the headline
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
	public String getHeadline()
	{
		return getPersistenceContext().getPropertyValue(HEADLINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.order</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public AbstractOrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.priority</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public CsTicketPriority getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.resolution</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the resolution
	 */
	@Accessor(qualifier = "resolution", type = Accessor.Type.GETTER)
	public CsTicketResolutionEventModel getResolution()
	{
		return getPersistenceContext().getPropertyValue(RESOLUTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.retentionDate</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the retentionDate - Date, when ticket was closed and retention period was started
	 */
	@Accessor(qualifier = "retentionDate", type = Accessor.Type.GETTER)
	public Date getRetentionDate()
	{
		return getPersistenceContext().getPropertyValue(RETENTIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.state</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public CsTicketState getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicket.ticketID</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the ticketID
	 */
	@Accessor(qualifier = "ticketID", type = Accessor.Type.GETTER)
	public String getTicketID()
	{
		return getPersistenceContext().getPropertyValue(TICKETID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.assignedAgent</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the assignedAgent
	 */
	@Accessor(qualifier = "assignedAgent", type = Accessor.Type.SETTER)
	public void setAssignedAgent(final EmployeeModel value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNEDAGENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.assignedGroup</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the assignedGroup
	 */
	@Accessor(qualifier = "assignedGroup", type = Accessor.Type.SETTER)
	public void setAssignedGroup(final CsAgentGroupModel value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNEDGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.baseSite</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.category</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.SETTER)
	public void setCategory(final CsTicketCategory value)
	{
		getPersistenceContext().setPropertyValue(CATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.customer</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.headline</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the headline
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
	public void setHeadline(final String value)
	{
		getPersistenceContext().setPropertyValue(HEADLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.order</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.priority</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final CsTicketPriority value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.resolution</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the resolution
	 */
	@Accessor(qualifier = "resolution", type = Accessor.Type.SETTER)
	public void setResolution(final CsTicketResolutionEventModel value)
	{
		getPersistenceContext().setPropertyValue(RESOLUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.retentionDate</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the retentionDate - Date, when ticket was closed and retention period was started
	 */
	@Accessor(qualifier = "retentionDate", type = Accessor.Type.SETTER)
	public void setRetentionDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(RETENTIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicket.state</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final CsTicketState value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CsTicket.ticketID</code> attribute defined at extension <code>ticketsystem</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the ticketID
	 */
	@Accessor(qualifier = "ticketID", type = Accessor.Type.SETTER)
	public void setTicketID(final String value)
	{
		getPersistenceContext().setPropertyValue(TICKETID, value);
	}
	
}
