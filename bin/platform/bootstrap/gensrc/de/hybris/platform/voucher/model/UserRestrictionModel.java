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
package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Collection;

/**
 * Generated model class for type UserRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class UserRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "UserRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserRestriction.users</code> attribute defined at extension <code>voucher</code>. */
	public static final String USERS = "users";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _users initial attribute declared by type <code>UserRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public UserRestrictionModel(final Collection<PrincipalModel> _users, final VoucherModel _voucher)
	{
		super();
		setUsers(_users);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _users initial attribute declared by type <code>UserRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public UserRestrictionModel(final ItemModel _owner, final Collection<PrincipalModel> _users, final VoucherModel _voucher)
	{
		super();
		setOwner(_owner);
		setUsers(_users);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserRestriction.users</code> attribute defined at extension <code>voucher</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the users - the principals the given voucher is valid for.
	 */
	@Accessor(qualifier = "users", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getUsers()
	{
		return getPersistenceContext().getPropertyValue(USERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserRestriction.users</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the users - the principals the given voucher is valid for.
	 */
	@Accessor(qualifier = "users", type = Accessor.Type.SETTER)
	public void setUsers(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(USERS, value);
	}
	
}
