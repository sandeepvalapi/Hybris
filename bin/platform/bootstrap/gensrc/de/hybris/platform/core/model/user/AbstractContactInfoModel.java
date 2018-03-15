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
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractContactInfo first defined at extension core.
 * <p>
 * Abstract contact info.
 */
@SuppressWarnings("all")
public class AbstractContactInfoModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractContactInfo";
	
	/**<i>Generated relation code constant for relation <code>User2ContactInfos</code> defining source attribute <code>user</code> in extension <code>core</code>.</i>*/
	public static final String _USER2CONTACTINFOS = "User2ContactInfos";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractContactInfo.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractContactInfo.userPOS</code> attribute defined at extension <code>core</code>. */
	public static final String USERPOS = "userPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractContactInfo.user</code> attribute defined at extension <code>core</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractContactInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractContactInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractContactInfoModel(final String _code, final UserModel _user)
	{
		super();
		setCode(_code);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractContactInfoModel(final String _code, final ItemModel _owner, final UserModel _user)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractContactInfo.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code - Specify code for that contact info.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractContactInfo.user</code> attribute defined at extension <code>core</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractContactInfo.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code - Specify code for that contact info.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractContactInfo.user</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
