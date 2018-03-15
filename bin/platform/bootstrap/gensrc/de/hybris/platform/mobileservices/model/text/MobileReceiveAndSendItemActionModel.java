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
package de.hybris.platform.mobileservices.model.text;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.mobileservices.model.text.MobileReceiveGenericActionModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MobileReceiveAndSendItemAction first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileReceiveAndSendItemActionModel extends MobileReceiveGenericActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileReceiveAndSendItemAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSendItemAction.content</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSendItemAction.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEEPLINKURL = "deeplinkUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSendItemAction.subject</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSendItemAction.isLink</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ISLINK = "isLink";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileReceiveAndSendItemActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileReceiveAndSendItemActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 * @param _content initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _deeplinkUrl initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileReceiveAndSendItemActionModel(final String _code, final ItemModel _content, final DeeplinkUrlModel _deeplinkUrl, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setContent(_content);
		setDeeplinkUrl(_deeplinkUrl);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 * @param _content initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _deeplinkUrl initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>MobileReceiveAndSendItemAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileReceiveAndSendItemActionModel(final String _code, final ItemModel _content, final DeeplinkUrlModel _deeplinkUrl, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setContent(_content);
		setDeeplinkUrl(_deeplinkUrl);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSendItemAction.content</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public ItemModel getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSendItemAction.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.GETTER)
	public DeeplinkUrlModel getDeeplinkUrl()
	{
		return getPersistenceContext().getPropertyValue(DEEPLINKURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSendItemAction.isLink</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the isLink
	 */
	@Accessor(qualifier = "isLink", type = Accessor.Type.GETTER)
	public Boolean getIsLink()
	{
		return getPersistenceContext().getPropertyValue(ISLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSendItemAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSendItemAction.content</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSendItemAction.deeplinkUrl</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.SETTER)
	public void setDeeplinkUrl(final DeeplinkUrlModel value)
	{
		getPersistenceContext().setPropertyValue(DEEPLINKURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSendItemAction.isLink</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the isLink
	 */
	@Accessor(qualifier = "isLink", type = Accessor.Type.SETTER)
	public void setIsLink(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ISLINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSendItemAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
}
