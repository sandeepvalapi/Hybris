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
package de.hybris.platform.acceleratorservices.model.email;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type EmailAttachment first defined at extension acceleratorservices.
 * <p>
 * Item that represents email attachment. It extends Media type without adding any new attribute.
 */
@SuppressWarnings("all")
public class EmailAttachmentModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EmailAttachment";
	
	/**<i>Generated relation code constant for relation <code>EmailMessage2EmailAttachmentsRel</code> defining source attribute <code>message</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EMAILMESSAGE2EMAILATTACHMENTSREL = "EmailMessage2EmailAttachmentsRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAttachment.message</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String MESSAGE = "message";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EmailAttachmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EmailAttachmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public EmailAttachmentModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public EmailAttachmentModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAttachment.message</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public EmailMessageModel getMessage()
	{
		return getPersistenceContext().getPropertyValue(MESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailAttachment.message</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final EmailMessageModel value)
	{
		getPersistenceContext().setPropertyValue(MESSAGE, value);
	}
	
}
