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
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;

/**
 * Generated model class for type CsCustomerEvent first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsCustomerEventModel extends CsTicketEventModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsCustomerEvent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsCustomerEvent.interventionType</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String INTERVENTIONTYPE = "interventionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsCustomerEvent.reason</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String REASON = "reason";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsCustomerEventModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsCustomerEventModel(final ItemModelContext ctx)
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
	public CsCustomerEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final String _text)
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
	public CsCustomerEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsCustomerEvent.interventionType</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the interventionType
	 */
	@Accessor(qualifier = "interventionType", type = Accessor.Type.GETTER)
	public CsInterventionType getInterventionType()
	{
		return getPersistenceContext().getPropertyValue(INTERVENTIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsCustomerEvent.reason</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the reason
	 */
	@Accessor(qualifier = "reason", type = Accessor.Type.GETTER)
	public CsEventReason getReason()
	{
		return getPersistenceContext().getPropertyValue(REASON);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsCustomerEvent.interventionType</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the interventionType
	 */
	@Accessor(qualifier = "interventionType", type = Accessor.Type.SETTER)
	public void setInterventionType(final CsInterventionType value)
	{
		getPersistenceContext().setPropertyValue(INTERVENTIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsCustomerEvent.reason</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the reason
	 */
	@Accessor(qualifier = "reason", type = Accessor.Type.SETTER)
	public void setReason(final CsEventReason value)
	{
		getPersistenceContext().setPropertyValue(REASON, value);
	}
	
}
