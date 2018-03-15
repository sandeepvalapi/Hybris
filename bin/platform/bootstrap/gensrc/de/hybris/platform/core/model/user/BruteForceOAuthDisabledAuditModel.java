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
 * Generated model class for type BruteForceOAuthDisabledAudit first defined at extension oauth2.
 */
@SuppressWarnings("all")
public class BruteForceOAuthDisabledAuditModel extends AbstractUserAuditModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BruteForceOAuthDisabledAudit";
	
	/** <i>Generated constant</i> - Attribute key of <code>BruteForceOAuthDisabledAudit.failedOAuthAuthorizations</code> attribute defined at extension <code>oauth2</code>. */
	public static final String FAILEDOAUTHAUTHORIZATIONS = "failedOAuthAuthorizations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BruteForceOAuthDisabledAuditModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BruteForceOAuthDisabledAuditModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _failedOAuthAuthorizations initial attribute declared by type <code>BruteForceOAuthDisabledAudit</code> at extension <code>oauth2</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceOAuthDisabledAuditModel(final Integer _failedOAuthAuthorizations, final String _uid, final Long _userPK)
	{
		super();
		setFailedOAuthAuthorizations(_failedOAuthAuthorizations);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _failedOAuthAuthorizations initial attribute declared by type <code>BruteForceOAuthDisabledAudit</code> at extension <code>oauth2</code>
	 * @param _ipAddress initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 * @param _userPK initial attribute declared by type <code>AbstractUserAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceOAuthDisabledAuditModel(final Integer _failedOAuthAuthorizations, final String _ipAddress, final ItemModel _owner, final String _uid, final Long _userPK)
	{
		super();
		setFailedOAuthAuthorizations(_failedOAuthAuthorizations);
		setIpAddress(_ipAddress);
		setOwner(_owner);
		setUid(_uid);
		setUserPK(_userPK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BruteForceOAuthDisabledAudit.failedOAuthAuthorizations</code> attribute defined at extension <code>oauth2</code>. 
	 * @return the failedOAuthAuthorizations - Number of failed logins
	 */
	@Accessor(qualifier = "failedOAuthAuthorizations", type = Accessor.Type.GETTER)
	public Integer getFailedOAuthAuthorizations()
	{
		return getPersistenceContext().getPropertyValue(FAILEDOAUTHAUTHORIZATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BruteForceOAuthDisabledAudit.failedOAuthAuthorizations</code> attribute defined at extension <code>oauth2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the failedOAuthAuthorizations - Number of failed logins
	 */
	@Accessor(qualifier = "failedOAuthAuthorizations", type = Accessor.Type.SETTER)
	public void setFailedOAuthAuthorizations(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FAILEDOAUTHAUTHORIZATIONS, value);
	}
	
}
