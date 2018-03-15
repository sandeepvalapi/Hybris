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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type EmailAddress first defined at extension acceleratorservices.
 * <p>
 * Extending EmailAddress type with additional attributes.
 */
@SuppressWarnings("all")
public class EmailAddressModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EmailAddress";
	
	/**<i>Generated relation code constant for relation <code>EmailMessage2ToAddressesRel</code> defining source attribute <code>toMessages</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EMAILMESSAGE2TOADDRESSESREL = "EmailMessage2ToAddressesRel";
	
	/**<i>Generated relation code constant for relation <code>EmailMessage2CcAddressesRel</code> defining source attribute <code>ccMessages</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EMAILMESSAGE2CCADDRESSESREL = "EmailMessage2CcAddressesRel";
	
	/**<i>Generated relation code constant for relation <code>EmailMessage2BccAddressesRel</code> defining source attribute <code>bccMessages</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EMAILMESSAGE2BCCADDRESSESREL = "EmailMessage2BccAddressesRel";
	
	/**<i>Generated relation code constant for relation <code>EmailMessage2FromAddressRel</code> defining source attribute <code>messagesSent</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EMAILMESSAGE2FROMADDRESSREL = "EmailMessage2FromAddressRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.emailAddress</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String EMAILADDRESS = "emailAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.displayName</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String DISPLAYNAME = "displayName";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.toMessages</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String TOMESSAGES = "toMessages";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.ccMessages</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CCMESSAGES = "ccMessages";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.bccMessages</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String BCCMESSAGES = "bccMessages";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailAddress.messagesSent</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String MESSAGESSENT = "messagesSent";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EmailAddressModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EmailAddressModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _displayName initial attribute declared by type <code>EmailAddress</code> at extension <code>acceleratorservices</code>
	 * @param _emailAddress initial attribute declared by type <code>EmailAddress</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public EmailAddressModel(final String _displayName, final String _emailAddress)
	{
		super();
		setDisplayName(_displayName);
		setEmailAddress(_emailAddress);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _displayName initial attribute declared by type <code>EmailAddress</code> at extension <code>acceleratorservices</code>
	 * @param _emailAddress initial attribute declared by type <code>EmailAddress</code> at extension <code>acceleratorservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public EmailAddressModel(final String _displayName, final String _emailAddress, final ItemModel _owner)
	{
		super();
		setDisplayName(_displayName);
		setEmailAddress(_emailAddress);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.bccMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the bccMessages
	 */
	@Accessor(qualifier = "bccMessages", type = Accessor.Type.GETTER)
	public List<EmailMessageModel> getBccMessages()
	{
		return getPersistenceContext().getPropertyValue(BCCMESSAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.ccMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ccMessages
	 */
	@Accessor(qualifier = "ccMessages", type = Accessor.Type.GETTER)
	public List<EmailMessageModel> getCcMessages()
	{
		return getPersistenceContext().getPropertyValue(CCMESSAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.displayName</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the displayName - Display name displayed for given item.
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName()
	{
		return getPersistenceContext().getPropertyValue(DISPLAYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.emailAddress</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the emailAddress - Email address of the given item.
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
	public String getEmailAddress()
	{
		return getPersistenceContext().getPropertyValue(EMAILADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.messagesSent</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the messagesSent
	 */
	@Accessor(qualifier = "messagesSent", type = Accessor.Type.GETTER)
	public List<EmailMessageModel> getMessagesSent()
	{
		return getPersistenceContext().getPropertyValue(MESSAGESSENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAddress.toMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the toMessages
	 */
	@Accessor(qualifier = "toMessages", type = Accessor.Type.GETTER)
	public List<EmailMessageModel> getToMessages()
	{
		return getPersistenceContext().getPropertyValue(TOMESSAGES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailAddress.bccMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the bccMessages
	 */
	@Accessor(qualifier = "bccMessages", type = Accessor.Type.SETTER)
	public void setBccMessages(final List<EmailMessageModel> value)
	{
		getPersistenceContext().setPropertyValue(BCCMESSAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailAddress.ccMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the ccMessages
	 */
	@Accessor(qualifier = "ccMessages", type = Accessor.Type.SETTER)
	public void setCcMessages(final List<EmailMessageModel> value)
	{
		getPersistenceContext().setPropertyValue(CCMESSAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EmailAddress.displayName</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the displayName - Display name displayed for given item.
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
	public void setDisplayName(final String value)
	{
		getPersistenceContext().setPropertyValue(DISPLAYNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EmailAddress.emailAddress</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the emailAddress - Email address of the given item.
	 */
	@Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
	public void setEmailAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAILADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailAddress.messagesSent</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the messagesSent
	 */
	@Accessor(qualifier = "messagesSent", type = Accessor.Type.SETTER)
	public void setMessagesSent(final List<EmailMessageModel> value)
	{
		getPersistenceContext().setPropertyValue(MESSAGESSENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailAddress.toMessages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the toMessages
	 */
	@Accessor(qualifier = "toMessages", type = Accessor.Type.SETTER)
	public void setToMessages(final List<EmailMessageModel> value)
	{
		getPersistenceContext().setPropertyValue(TOMESSAGES, value);
	}
	
}
