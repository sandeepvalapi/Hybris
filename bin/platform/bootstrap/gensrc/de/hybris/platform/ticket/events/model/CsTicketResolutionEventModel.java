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
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;

/**
 * Generated model class for type CsTicketResolutionEvent first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketResolutionEventModel extends CsCustomerEventModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicketResolutionEvent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketResolutionEvent.resolutionType</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String RESOLUTIONTYPE = "resolutionType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketResolutionEventModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketResolutionEventModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _resolutionType initial attribute declared by type <code>CsTicketResolutionEvent</code> at extension <code>ticketsystem</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CsTicketResolutionEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final CsResolutionType _resolutionType, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setResolutionType(_resolutionType);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _resolutionType initial attribute declared by type <code>CsTicketResolutionEvent</code> at extension <code>ticketsystem</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CsTicketResolutionEventModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final CsResolutionType _resolutionType, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setResolutionType(_resolutionType);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketResolutionEvent.resolutionType</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the resolutionType
	 */
	@Accessor(qualifier = "resolutionType", type = Accessor.Type.GETTER)
	public CsResolutionType getResolutionType()
	{
		return getPersistenceContext().getPropertyValue(RESOLUTIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CsTicketResolutionEvent.resolutionType</code> attribute defined at extension <code>ticketsystem</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the resolutionType
	 */
	@Accessor(qualifier = "resolutionType", type = Accessor.Type.SETTER)
	public void setResolutionType(final CsResolutionType value)
	{
		getPersistenceContext().setPropertyValue(RESOLUTIONTYPE, value);
	}
	
}
