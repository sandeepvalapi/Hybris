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
 * Generated model class for type BruteForceLoginAttempts first defined at extension core.
 */
@SuppressWarnings("all")
public class BruteForceLoginAttemptsModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BruteForceLoginAttempts";
	
	/** <i>Generated constant</i> - Attribute key of <code>BruteForceLoginAttempts.uid</code> attribute defined at extension <code>core</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>BruteForceLoginAttempts.attempts</code> attribute defined at extension <code>core</code>. */
	public static final String ATTEMPTS = "attempts";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BruteForceLoginAttemptsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BruteForceLoginAttemptsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attempts initial attribute declared by type <code>BruteForceLoginAttempts</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>BruteForceLoginAttempts</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceLoginAttemptsModel(final Integer _attempts, final String _uid)
	{
		super();
		setAttempts(_attempts);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attempts initial attribute declared by type <code>BruteForceLoginAttempts</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>BruteForceLoginAttempts</code> at extension <code>core</code>
	 */
	@Deprecated
	public BruteForceLoginAttemptsModel(final Integer _attempts, final ItemModel _owner, final String _uid)
	{
		super();
		setAttempts(_attempts);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BruteForceLoginAttempts.attempts</code> attribute defined at extension <code>core</code>. 
	 * @return the attempts
	 */
	@Accessor(qualifier = "attempts", type = Accessor.Type.GETTER)
	public Integer getAttempts()
	{
		return getPersistenceContext().getPropertyValue(ATTEMPTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BruteForceLoginAttempts.uid</code> attribute defined at extension <code>core</code>. 
	 * @return the uid - User identifier
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		return getPersistenceContext().getPropertyValue(UID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BruteForceLoginAttempts.attempts</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the attempts
	 */
	@Accessor(qualifier = "attempts", type = Accessor.Type.SETTER)
	public void setAttempts(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ATTEMPTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BruteForceLoginAttempts.uid</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the uid - User identifier
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		getPersistenceContext().setPropertyValue(UID, value);
	}
	
}
