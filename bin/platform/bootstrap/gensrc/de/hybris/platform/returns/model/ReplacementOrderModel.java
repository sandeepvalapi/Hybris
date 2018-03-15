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
package de.hybris.platform.returns.model;

import de.hybris.platform.basecommerce.enums.ReturnFulfillmentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.returns.model.ReturnOrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ReplacementOrder first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ReplacementOrderModel extends ReturnOrderModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ReplacementOrder";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReplacementOrderModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReplacementOrderModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _fulfilmentStatus initial attribute declared by type <code>ReturnOrder</code> at extension <code>basecommerce</code>
	 * @param _user initial attribute declared by type <code>Order</code> at extension <code>core</code>
	 */
	@Deprecated
	public ReplacementOrderModel(final CurrencyModel _currency, final Date _date, final ReturnFulfillmentStatus _fulfilmentStatus, final UserModel _user)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setFulfilmentStatus(_fulfilmentStatus);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _fulfilmentStatus initial attribute declared by type <code>ReturnOrder</code> at extension <code>basecommerce</code>
	 * @param _originalVersion initial attribute declared by type <code>Order</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>Order</code> at extension <code>core</code>
	 * @param _versionID initial attribute declared by type <code>Order</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ReplacementOrderModel(final CurrencyModel _currency, final Date _date, final ReturnFulfillmentStatus _fulfilmentStatus, final OrderModel _originalVersion, final ItemModel _owner, final UserModel _user, final String _versionID)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setFulfilmentStatus(_fulfilmentStatus);
		setOriginalVersion(_originalVersion);
		setOwner(_owner);
		setUser(_user);
		setVersionID(_versionID);
	}
	
	
}
