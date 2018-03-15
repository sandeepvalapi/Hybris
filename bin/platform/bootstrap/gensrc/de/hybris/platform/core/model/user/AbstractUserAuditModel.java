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
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractUserAudit first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractUserAuditModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractUserAudit";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractUserAudit.uid</code> attribute defined at extension <code>core</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractUserAudit.userPK</code> attribute defined at extension <code>core</code>. */
	public static final String USERPK = "userPK";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractUserAudit.changingUser</code> attribute defined at extension <code>core</code>. */
	public static final String CHANGINGUSER = "changingUser";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractUserAudit.changingApplication</code> attribute defined at extension <code>core</code>. */
	public static final String CHANGINGAPPLICATION = "changingApplication";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractUserAudit.ipAddress</code> attribute defined at extension <code>core</code>. */
	public static final String IPADDRESS = "ipAddress";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractUserAuditModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractUserAuditModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractUserAuditModel(final String _uid, final Long _userPK)
	{
		super();
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _ipAddress initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractUserAuditModel(final String _ipAddress, final ItemModel _owner, final String _uid, final Long _userPK)
	{
		super();
		setIpAddress(_ipAddress);
		setOwner(_owner);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractUserAudit.changingApplication</code> attribute defined at extension <code>core</code>. 
	 * @return the changingApplication
	 */
	@Accessor(qualifier = "changingApplication", type = Accessor.Type.GETTER)
	public String getChangingApplication()
	{
		return getPersistenceContext().getPropertyValue(CHANGINGAPPLICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractUserAudit.changingUser</code> attribute defined at extension <code>core</code>. 
	 * @return the changingUser
	 */
	@Accessor(qualifier = "changingUser", type = Accessor.Type.GETTER)
	public String getChangingUser()
	{
		return getPersistenceContext().getPropertyValue(CHANGINGUSER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractUserAudit.ipAddress</code> attribute defined at extension <code>core</code>. 
	 * @return the ipAddress
	 */
	@Accessor(qualifier = "ipAddress", type = Accessor.Type.GETTER)
	public String getIpAddress()
	{
		return getPersistenceContext().getPropertyValue(IPADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractUserAudit.uid</code> attribute defined at extension <code>core</code>. 
	 * @return the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		return getPersistenceContext().getPropertyValue(UID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractUserAudit.userPK</code> attribute defined at extension <code>core</code>. 
	 * @return the userPK
	 */
	@Accessor(qualifier = "userPK", type = Accessor.Type.GETTER)
	public Long getUserPK()
	{
		return getPersistenceContext().getPropertyValue(USERPK);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractUserAudit.changingApplication</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the changingApplication
	 */
	@Accessor(qualifier = "changingApplication", type = Accessor.Type.SETTER)
	public void setChangingApplication(final String value)
	{
		getPersistenceContext().setPropertyValue(CHANGINGAPPLICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractUserAudit.changingUser</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the changingUser
	 */
	@Accessor(qualifier = "changingUser", type = Accessor.Type.SETTER)
	public void setChangingUser(final String value)
	{
		getPersistenceContext().setPropertyValue(CHANGINGUSER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractUserAudit.ipAddress</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the ipAddress
	 */
	@Accessor(qualifier = "ipAddress", type = Accessor.Type.SETTER)
	public void setIpAddress(final String value)
	{
		getPersistenceContext().setPropertyValue(IPADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractUserAudit.uid</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		getPersistenceContext().setPropertyValue(UID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractUserAudit.userPK</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the userPK
	 */
	@Accessor(qualifier = "userPK", type = Accessor.Type.SETTER)
	public void setUserPK(final Long value)
	{
		getPersistenceContext().setPropertyValue(USERPK, value);
	}
	
}
