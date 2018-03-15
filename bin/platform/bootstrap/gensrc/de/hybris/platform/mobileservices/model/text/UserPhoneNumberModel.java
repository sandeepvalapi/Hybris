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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.mobileservices.enums.PhoneType;
import de.hybris.platform.mobileservices.model.text.PhoneNumberModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type UserPhoneNumber first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class UserPhoneNumberModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "UserPhoneNumber";
	
	/**<i>Generated relation code constant for relation <code>UserPhoneNumberRelation</code> defining source attribute <code>user</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _USERPHONENUMBERRELATION = "UserPhoneNumberRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPhoneNumber.type</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPhoneNumber.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONENUMBER = "phoneNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPhoneNumber.default</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEFAULT = "default";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPhoneNumber.user</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserPhoneNumberModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserPhoneNumberModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _phoneNumber initial attribute declared by type <code>UserPhoneNumber</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public UserPhoneNumberModel(final PhoneNumberModel _phoneNumber)
	{
		super();
		setPhoneNumber(_phoneNumber);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _phoneNumber initial attribute declared by type <code>UserPhoneNumber</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public UserPhoneNumberModel(final ItemModel _owner, final PhoneNumberModel _phoneNumber)
	{
		super();
		setOwner(_owner);
		setPhoneNumber(_phoneNumber);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPhoneNumber.default</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.GETTER)
	public Boolean getDefault()
	{
		return getPersistenceContext().getPropertyValue(DEFAULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPhoneNumber.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.GETTER)
	public PhoneNumberModel getPhoneNumber()
	{
		return getPersistenceContext().getPropertyValue(PHONENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPhoneNumber.type</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public PhoneType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPhoneNumber.user</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserPhoneNumber.default</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the default
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.SETTER)
	public void setDefault(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserPhoneNumber.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.SETTER)
	public void setPhoneNumber(final PhoneNumberModel value)
	{
		getPersistenceContext().setPropertyValue(PHONENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserPhoneNumber.type</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final PhoneType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserPhoneNumber.user</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
