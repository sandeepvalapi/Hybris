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
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEmailModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type CsTicketEvent first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketEventModel extends CommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicketEvent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEvent.startDateTime</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String STARTDATETIME = "startDateTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEvent.endDateTime</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ENDDATETIME = "endDateTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEvent.ticket</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKET = "ticket";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEvent.emails</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EMAILS = "emails";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEvent.entries</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ENTRIES = "entries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketEventModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketEventModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CsTicketEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CsTicketEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEvent.emails</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the emails
	 */
	@Accessor(qualifier = "emails", type = Accessor.Type.GETTER)
	public List<CsTicketEmailModel> getEmails()
	{
		return getPersistenceContext().getPropertyValue(EMAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEvent.endDateTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the endDateTime
	 */
	@Accessor(qualifier = "endDateTime", type = Accessor.Type.GETTER)
	public Date getEndDateTime()
	{
		return getPersistenceContext().getPropertyValue(ENDDATETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEvent.entries</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
	public Set<CsTicketChangeEventEntryModel> getEntries()
	{
		return getPersistenceContext().getPropertyValue(ENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEvent.startDateTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the startDateTime
	 */
	@Accessor(qualifier = "startDateTime", type = Accessor.Type.GETTER)
	public Date getStartDateTime()
	{
		return getPersistenceContext().getPropertyValue(STARTDATETIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEvent.ticket</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the ticket
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "ticket", type = Accessor.Type.GETTER)
	public CsTicketModel getTicket()
	{
		return getPersistenceContext().getPropertyValue(TICKET);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEvent.emails</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the emails
	 */
	@Accessor(qualifier = "emails", type = Accessor.Type.SETTER)
	public void setEmails(final List<CsTicketEmailModel> value)
	{
		getPersistenceContext().setPropertyValue(EMAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEvent.endDateTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the endDateTime
	 */
	@Accessor(qualifier = "endDateTime", type = Accessor.Type.SETTER)
	public void setEndDateTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATETIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEvent.entries</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
	public void setEntries(final Set<CsTicketChangeEventEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEvent.startDateTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the startDateTime
	 */
	@Accessor(qualifier = "startDateTime", type = Accessor.Type.SETTER)
	public void setStartDateTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATETIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEvent.ticket</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the ticket
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "ticket", type = Accessor.Type.SETTER)
	public void setTicket(final CsTicketModel value)
	{
		getPersistenceContext().setPropertyValue(TICKET, value);
	}
	
}
