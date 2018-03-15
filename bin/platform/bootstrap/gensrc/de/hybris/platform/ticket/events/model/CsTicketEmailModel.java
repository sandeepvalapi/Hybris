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
package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;

/**
 * Generated model class for type CsTicketEmail first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketEmailModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicketEmail";
	
	/**<i>Generated relation code constant for relation <code>CsTicketEvent2CsTicketEmail</code> defining source attribute <code>ticket</code> in extension <code>ticketsystem</code>.</i>*/
	public static final String _CSTICKETEVENT2CSTICKETEMAIL = "CsTicketEvent2CsTicketEmail";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.messageId</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String MESSAGEID = "messageId";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.from</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String FROM = "from";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.to</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TO = "to";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.subject</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.body</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String BODY = "body";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.ticketPOS</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKETPOS = "ticketPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEmail.ticket</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKET = "ticket";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketEmailModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketEmailModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CsTicketEmailModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.body</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the body
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.GETTER)
	public String getBody()
	{
		return getPersistenceContext().getPropertyValue(BODY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.from</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the from
	 */
	@Accessor(qualifier = "from", type = Accessor.Type.GETTER)
	public String getFrom()
	{
		return getPersistenceContext().getPropertyValue(FROM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.messageId</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the messageId
	 */
	@Accessor(qualifier = "messageId", type = Accessor.Type.GETTER)
	public String getMessageId()
	{
		return getPersistenceContext().getPropertyValue(MESSAGEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.subject</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.ticket</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the ticket
	 */
	@Accessor(qualifier = "ticket", type = Accessor.Type.GETTER)
	public CsTicketEventModel getTicket()
	{
		return getPersistenceContext().getPropertyValue(TICKET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEmail.to</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the to
	 */
	@Accessor(qualifier = "to", type = Accessor.Type.GETTER)
	public String getTo()
	{
		return getPersistenceContext().getPropertyValue(TO);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.body</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the body
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.SETTER)
	public void setBody(final String value)
	{
		getPersistenceContext().setPropertyValue(BODY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.from</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the from
	 */
	@Accessor(qualifier = "from", type = Accessor.Type.SETTER)
	public void setFrom(final String value)
	{
		getPersistenceContext().setPropertyValue(FROM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.messageId</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the messageId
	 */
	@Accessor(qualifier = "messageId", type = Accessor.Type.SETTER)
	public void setMessageId(final String value)
	{
		getPersistenceContext().setPropertyValue(MESSAGEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.subject</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.ticket</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the ticket
	 */
	@Accessor(qualifier = "ticket", type = Accessor.Type.SETTER)
	public void setTicket(final CsTicketEventModel value)
	{
		getPersistenceContext().setPropertyValue(TICKET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEmail.to</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the to
	 */
	@Accessor(qualifier = "to", type = Accessor.Type.SETTER)
	public void setTo(final String value)
	{
		getPersistenceContext().setPropertyValue(TO, value);
	}
	
}
