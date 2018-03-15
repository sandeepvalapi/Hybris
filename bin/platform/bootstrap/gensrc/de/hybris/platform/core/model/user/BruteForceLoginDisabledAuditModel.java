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
 * Generated model class for type BruteForceLoginDisabledAudit first defined at extension core.
 */
@SuppressWarnings("all")
public class BruteForceLoginDisabledAuditModel extends AbstractUserAuditModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BruteForceLoginDisabledAudit";
	
	/** <i>Generated constant</i> - Attribute key of <code>BruteForceLoginDisabledAudit.failedLogins</code> attribute defined at extension <code>core</code>. */
	public static final String FAILEDLOGINS = "failedLogins";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BruteForceLoginDisabledAuditModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BruteForceLoginDisabledAuditModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _failedLogins initial attribute declared by type <code>BruteForceLoginDisabledAudit</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceLoginDisabledAuditModel(final Integer _failedLogins, final String _uid, final Long _userPK)
	{
		super();
		setFailedLogins(_failedLogins);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _failedLogins initial attribute declared by type <code>BruteForceLoginDisabledAudit</code> at extension <code>core</code>
	 * @param _ipAddress initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceLoginDisabledAuditModel(final Integer _failedLogins, final String _ipAddress, final ItemModel _owner, final String _uid, final Long _userPK)
	{
		super();
		setFailedLogins(_failedLogins);
		setIpAddress(_ipAddress);
		setOwner(_owner);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BruteForceLoginDisabledAudit.failedLogins</code> attribute defined at extension <code>core</code>. 
	 * @return the failedLogins - Number of failed logins
	 */
	@Accessor(qualifier = "failedLogins", type = Accessor.Type.GETTER)
	public Integer getFailedLogins()
	{
		return getPersistenceContext().getPropertyValue(FAILEDLOGINS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BruteForceLoginDisabledAudit.failedLogins</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the failedLogins - Number of failed logins
	 */
	@Accessor(qualifier = "failedLogins", type = Accessor.Type.SETTER)
	public void setFailedLogins(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FAILEDLOGINS, value);
	}
	
}
