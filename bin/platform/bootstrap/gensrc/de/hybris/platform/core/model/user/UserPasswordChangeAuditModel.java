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
import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type UserPasswordChangeAudit first defined at extension core.
 */
@SuppressWarnings("all")
public class UserPasswordChangeAuditModel extends AbstractUserAuditModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "UserPasswordChangeAudit";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPasswordChangeAudit.encodedPassword</code> attribute defined at extension <code>core</code>. */
	public static final String ENCODEDPASSWORD = "encodedPassword";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserPasswordChangeAudit.passwordEncoding</code> attribute defined at extension <code>core</code>. */
	public static final String PASSWORDENCODING = "passwordEncoding";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserPasswordChangeAuditModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserPasswordChangeAuditModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _encodedPassword initial attribute declared by type <code>UserPasswordChangeAudit</code> at extension <code>core</code>
	 * @param _passwordEncoding initial attribute declared by type <code>UserPasswordChangeAudit</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserPasswordChangeAuditModel(final String _encodedPassword, final String _passwordEncoding, final String _uid, final Long _userPK)
	{
		super();
		setEncodedPassword(_encodedPassword);
		setPasswordEncoding(_passwordEncoding);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _encodedPassword initial attribute declared by type <code>UserPasswordChangeAudit</code> at extension <code>core</code>
	 * @param _ipAddress initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _passwordEncoding initial attribute declared by type <code>UserPasswordChangeAudit</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserPasswordChangeAuditModel(final String _encodedPassword, final String _ipAddress, final ItemModel _owner, final String _passwordEncoding, final String _uid, final Long _userPK)
	{
		super();
		setEncodedPassword(_encodedPassword);
		setIpAddress(_ipAddress);
		setOwner(_owner);
		setPasswordEncoding(_passwordEncoding);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPasswordChangeAudit.encodedPassword</code> attribute defined at extension <code>core</code>. 
	 * @return the encodedPassword
	 */
	@Accessor(qualifier = "encodedPassword", type = Accessor.Type.GETTER)
	public String getEncodedPassword()
	{
		return getPersistenceContext().getPropertyValue(ENCODEDPASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserPasswordChangeAudit.passwordEncoding</code> attribute defined at extension <code>core</code>. 
	 * @return the passwordEncoding
	 */
	@Accessor(qualifier = "passwordEncoding", type = Accessor.Type.GETTER)
	public String getPasswordEncoding()
	{
		return getPersistenceContext().getPropertyValue(PASSWORDENCODING);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>UserPasswordChangeAudit.encodedPassword</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the encodedPassword
	 */
	@Accessor(qualifier = "encodedPassword", type = Accessor.Type.SETTER)
	public void setEncodedPassword(final String value)
	{
		getPersistenceContext().setPropertyValue(ENCODEDPASSWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserPasswordChangeAudit.passwordEncoding</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the passwordEncoding
	 */
	@Accessor(qualifier = "passwordEncoding", type = Accessor.Type.SETTER)
	public void setPasswordEncoding(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORDENCODING, value);
	}
	
}
