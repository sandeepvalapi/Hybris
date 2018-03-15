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
package de.hybris.platform.mobileservices.model.text.job;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Map;

/**
 * Generated model class for type MobileSendMessageCronJob first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileSendMessageCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileSendMessageCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.link</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LINK = "link";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.subject</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.template</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEEPLINKURL = "deeplinkUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.variables</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String VARIABLES = "variables";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.item</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.language</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.recipientPhoneNumberLists</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String RECIPIENTPHONENUMBERLISTS = "recipientPhoneNumberLists";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.recipients</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String RECIPIENTS = "recipients";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendMessageCronJob.phoneNumbers</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONENUMBERS = "phoneNumbers";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileSendMessageCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileSendMessageCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MobileSendMessageCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileSendMessageCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.GETTER)
	public DeeplinkUrlModel getDeeplinkUrl()
	{
		return getPersistenceContext().getPropertyValue(DEEPLINKURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.item</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		return getPersistenceContext().getPropertyValue(ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.language</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.link</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the link
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.GETTER)
	public Boolean getLink()
	{
		return getPersistenceContext().getPropertyValue(LINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.phoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the phoneNumbers
	 */
	@Accessor(qualifier = "phoneNumbers", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberModel> getPhoneNumbers()
	{
		return getPersistenceContext().getPropertyValue(PHONENUMBERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.recipientPhoneNumberLists</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the recipientPhoneNumberLists
	 */
	@Accessor(qualifier = "recipientPhoneNumberLists", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getRecipientPhoneNumberLists()
	{
		return getPersistenceContext().getPropertyValue(RECIPIENTPHONENUMBERLISTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.recipients</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the recipients
	 */
	@Accessor(qualifier = "recipients", type = Accessor.Type.GETTER)
	public Collection<UserModel> getRecipients()
	{
		return getPersistenceContext().getPropertyValue(RECIPIENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.template</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public String getTemplate()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendMessageCronJob.variables</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the variables
	 */
	@Accessor(qualifier = "variables", type = Accessor.Type.GETTER)
	public Map<String,String> getVariables()
	{
		return getPersistenceContext().getPropertyValue(VARIABLES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.SETTER)
	public void setDeeplinkUrl(final DeeplinkUrlModel value)
	{
		getPersistenceContext().setPropertyValue(DEEPLINKURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.item</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.language</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.link</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the link
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.SETTER)
	public void setLink(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.phoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneNumbers
	 */
	@Accessor(qualifier = "phoneNumbers", type = Accessor.Type.SETTER)
	public void setPhoneNumbers(final Collection<PhoneNumberModel> value)
	{
		getPersistenceContext().setPropertyValue(PHONENUMBERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.recipientPhoneNumberLists</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the recipientPhoneNumberLists
	 */
	@Accessor(qualifier = "recipientPhoneNumberLists", type = Accessor.Type.SETTER)
	public void setRecipientPhoneNumberLists(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(RECIPIENTPHONENUMBERLISTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.recipients</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the recipients
	 */
	@Accessor(qualifier = "recipients", type = Accessor.Type.SETTER)
	public void setRecipients(final Collection<UserModel> value)
	{
		getPersistenceContext().setPropertyValue(RECIPIENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.template</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final String value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendMessageCronJob.variables</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the variables
	 */
	@Accessor(qualifier = "variables", type = Accessor.Type.SETTER)
	public void setVariables(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(VARIABLES, value);
	}
	
}
