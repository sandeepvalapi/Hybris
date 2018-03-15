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
import de.hybris.platform.mobileservices.model.text.MobileReceiveGenericActionModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MobileReceiveAndSubscribeAction first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileReceiveAndSubscribeActionModel extends MobileReceiveGenericActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileReceiveAndSubscribeAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSubscribeAction.remove</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String REMOVE = "remove";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSubscribeAction.list</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LIST = "list";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSubscribeAction.successMessage</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SUCCESSMESSAGE = "successMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileReceiveAndSubscribeAction.defaultErrorMessage</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEFAULTERRORMESSAGE = "defaultErrorMessage";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileReceiveAndSubscribeActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileReceiveAndSubscribeActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 * @param _list initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _successMessage initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileReceiveAndSubscribeActionModel(final String _code, final PhoneNumberListModel _list, final String _successMessage, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setList(_list);
		setSuccessMessage(_successMessage);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 * @param _list initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _successMessage initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>MobileReceiveAndSubscribeAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileReceiveGenericAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileReceiveAndSubscribeActionModel(final String _code, final PhoneNumberListModel _list, final ItemModel _owner, final String _successMessage, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setList(_list);
		setOwner(_owner);
		setSuccessMessage(_successMessage);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSubscribeAction.defaultErrorMessage</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the defaultErrorMessage
	 */
	@Accessor(qualifier = "defaultErrorMessage", type = Accessor.Type.GETTER)
	public String getDefaultErrorMessage()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTERRORMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSubscribeAction.list</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the list
	 */
	@Accessor(qualifier = "list", type = Accessor.Type.GETTER)
	public PhoneNumberListModel getList()
	{
		return getPersistenceContext().getPropertyValue(LIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSubscribeAction.remove</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the remove
	 */
	@Accessor(qualifier = "remove", type = Accessor.Type.GETTER)
	public Boolean getRemove()
	{
		return getPersistenceContext().getPropertyValue(REMOVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileReceiveAndSubscribeAction.successMessage</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the successMessage
	 */
	@Accessor(qualifier = "successMessage", type = Accessor.Type.GETTER)
	public String getSuccessMessage()
	{
		return getPersistenceContext().getPropertyValue(SUCCESSMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSubscribeAction.defaultErrorMessage</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the defaultErrorMessage
	 */
	@Accessor(qualifier = "defaultErrorMessage", type = Accessor.Type.SETTER)
	public void setDefaultErrorMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTERRORMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSubscribeAction.list</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the list
	 */
	@Accessor(qualifier = "list", type = Accessor.Type.SETTER)
	public void setList(final PhoneNumberListModel value)
	{
		getPersistenceContext().setPropertyValue(LIST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSubscribeAction.remove</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the remove
	 */
	@Accessor(qualifier = "remove", type = Accessor.Type.SETTER)
	public void setRemove(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileReceiveAndSubscribeAction.successMessage</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the successMessage
	 */
	@Accessor(qualifier = "successMessage", type = Accessor.Type.SETTER)
	public void setSuccessMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(SUCCESSMESSAGE, value);
	}
	
}
