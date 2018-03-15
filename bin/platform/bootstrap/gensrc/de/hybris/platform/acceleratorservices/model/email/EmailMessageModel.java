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
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type EmailMessage first defined at extension acceleratorservices.
 * <p>
 * Extending EmailMessage type with additional attributes.
 */
@SuppressWarnings("all")
public class EmailMessageModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EmailMessage";
	
	/**<i>Generated relation code constant for relation <code>BusinessProcess2EmailMessageRel</code> defining source attribute <code>process</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _BUSINESSPROCESS2EMAILMESSAGEREL = "BusinessProcess2EmailMessageRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.sent</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SENT = "sent";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.replyToAddress</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String REPLYTOADDRESS = "replyToAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.subject</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.body</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String BODY = "body";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.bodyMedia</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String BODYMEDIA = "bodyMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.sentDate</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SENTDATE = "sentDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.sentMessageID</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SENTMESSAGEID = "sentMessageID";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.toAddresses</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String TOADDRESSES = "toAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.ccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CCADDRESSES = "ccAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.bccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String BCCADDRESSES = "bccAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.fromAddress</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String FROMADDRESS = "fromAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.attachments</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String ATTACHMENTS = "attachments";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailMessage.process</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String PROCESS = "process";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EmailMessageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EmailMessageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _replyToAddress initial attribute declared by type <code>EmailMessage</code> at extension <code>acceleratorservices</code>
	 * @param _subject initial attribute declared by type <code>EmailMessage</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public EmailMessageModel(final String _replyToAddress, final String _subject)
	{
		super();
		setReplyToAddress(_replyToAddress);
		setSubject(_subject);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _replyToAddress initial attribute declared by type <code>EmailMessage</code> at extension <code>acceleratorservices</code>
	 * @param _subject initial attribute declared by type <code>EmailMessage</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public EmailMessageModel(final ItemModel _owner, final String _replyToAddress, final String _subject)
	{
		super();
		setOwner(_owner);
		setReplyToAddress(_replyToAddress);
		setSubject(_subject);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.attachments</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachments
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
	public List<EmailAttachmentModel> getAttachments()
	{
		return getPersistenceContext().getPropertyValue(ATTACHMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.bccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the bccAddresses
	 */
	@Accessor(qualifier = "bccAddresses", type = Accessor.Type.GETTER)
	public List<EmailAddressModel> getBccAddresses()
	{
		return getPersistenceContext().getPropertyValue(BCCADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.body</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the body - Body of the email message.
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.GETTER)
	public String getBody()
	{
		return getPersistenceContext().getPropertyValue(BODY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.bodyMedia</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the bodyMedia - Body of the email message which is too big to put in 'body' attribute
	 */
	@Accessor(qualifier = "bodyMedia", type = Accessor.Type.GETTER)
	public MediaModel getBodyMedia()
	{
		return getPersistenceContext().getPropertyValue(BODYMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.ccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ccAddresses
	 */
	@Accessor(qualifier = "ccAddresses", type = Accessor.Type.GETTER)
	public List<EmailAddressModel> getCcAddresses()
	{
		return getPersistenceContext().getPropertyValue(CCADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.fromAddress</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the fromAddress
	 */
	@Accessor(qualifier = "fromAddress", type = Accessor.Type.GETTER)
	public EmailAddressModel getFromAddress()
	{
		return getPersistenceContext().getPropertyValue(FROMADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.process</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.GETTER)
	public BusinessProcessModel getProcess()
	{
		return getPersistenceContext().getPropertyValue(PROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.replyToAddress</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the replyToAddress - It's a string value with email address that message has to be replied to.
	 */
	@Accessor(qualifier = "replyToAddress", type = Accessor.Type.GETTER)
	public String getReplyToAddress()
	{
		return getPersistenceContext().getPropertyValue(REPLYTOADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.sentDate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the sentDate - Date of sending the email message.
	 */
	@Accessor(qualifier = "sentDate", type = Accessor.Type.GETTER)
	public Date getSentDate()
	{
		return getPersistenceContext().getPropertyValue(SENTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.sentMessageID</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the sentMessageID - Identifier of the email message.
	 */
	@Accessor(qualifier = "sentMessageID", type = Accessor.Type.GETTER)
	public String getSentMessageID()
	{
		return getPersistenceContext().getPropertyValue(SENTMESSAGEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.subject</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the subject - Subject of the email message.
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.toAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the toAddresses
	 */
	@Accessor(qualifier = "toAddresses", type = Accessor.Type.GETTER)
	public List<EmailAddressModel> getToAddresses()
	{
		return getPersistenceContext().getPropertyValue(TOADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailMessage.sent</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the sent - Is set when the email message has been already sent.
	 */
	@Accessor(qualifier = "sent", type = Accessor.Type.GETTER)
	public boolean isSent()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SENT));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.attachments</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the attachments
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
	public void setAttachments(final List<EmailAttachmentModel> value)
	{
		getPersistenceContext().setPropertyValue(ATTACHMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.bccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the bccAddresses
	 */
	@Accessor(qualifier = "bccAddresses", type = Accessor.Type.SETTER)
	public void setBccAddresses(final List<EmailAddressModel> value)
	{
		getPersistenceContext().setPropertyValue(BCCADDRESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.body</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the body - Body of the email message.
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.SETTER)
	public void setBody(final String value)
	{
		getPersistenceContext().setPropertyValue(BODY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.bodyMedia</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the bodyMedia - Body of the email message which is too big to put in 'body' attribute
	 */
	@Accessor(qualifier = "bodyMedia", type = Accessor.Type.SETTER)
	public void setBodyMedia(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(BODYMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.ccAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the ccAddresses
	 */
	@Accessor(qualifier = "ccAddresses", type = Accessor.Type.SETTER)
	public void setCcAddresses(final List<EmailAddressModel> value)
	{
		getPersistenceContext().setPropertyValue(CCADDRESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.fromAddress</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the fromAddress
	 */
	@Accessor(qualifier = "fromAddress", type = Accessor.Type.SETTER)
	public void setFromAddress(final EmailAddressModel value)
	{
		getPersistenceContext().setPropertyValue(FROMADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.process</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final BusinessProcessModel value)
	{
		getPersistenceContext().setPropertyValue(PROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EmailMessage.replyToAddress</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the replyToAddress - It's a string value with email address that message has to be replied to.
	 */
	@Accessor(qualifier = "replyToAddress", type = Accessor.Type.SETTER)
	public void setReplyToAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(REPLYTOADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.sent</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the sent - Is set when the email message has been already sent.
	 */
	@Accessor(qualifier = "sent", type = Accessor.Type.SETTER)
	public void setSent(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SENT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.sentDate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the sentDate - Date of sending the email message.
	 */
	@Accessor(qualifier = "sentDate", type = Accessor.Type.SETTER)
	public void setSentDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(SENTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.sentMessageID</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the sentMessageID - Identifier of the email message.
	 */
	@Accessor(qualifier = "sentMessageID", type = Accessor.Type.SETTER)
	public void setSentMessageID(final String value)
	{
		getPersistenceContext().setPropertyValue(SENTMESSAGEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EmailMessage.subject</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the subject - Subject of the email message.
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailMessage.toAddresses</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the toAddresses
	 */
	@Accessor(qualifier = "toAddresses", type = Accessor.Type.SETTER)
	public void setToAddresses(final List<EmailAddressModel> value)
	{
		getPersistenceContext().setPropertyValue(TOADDRESSES, value);
	}
	
}
