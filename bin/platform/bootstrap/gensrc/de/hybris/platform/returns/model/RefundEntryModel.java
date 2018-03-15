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

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Generated model class for type RefundEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class RefundEntryModel extends ReturnEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RefundEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>RefundEntry.reason</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String REASON = "reason";
	
	/** <i>Generated constant</i> - Attribute key of <code>RefundEntry.amount</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String AMOUNT = "amount";
	
	/** <i>Generated constant</i> - Attribute key of <code>RefundEntry.currency</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>RefundEntry.refundedDate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String REFUNDEDDATE = "refundedDate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RefundEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RefundEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _reason initial attribute declared by type <code>RefundEntry</code> at extension <code>basecommerce</code>
	 * @param _status initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public RefundEntryModel(final ReturnAction _action, final AbstractOrderEntryModel _orderEntry, final RefundReason _reason, final ReturnStatus _status)
	{
		super();
		setAction(_action);
		setOrderEntry(_orderEntry);
		setReason(_reason);
		setStatus(_status);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _reason initial attribute declared by type <code>RefundEntry</code> at extension <code>basecommerce</code>
	 * @param _status initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public RefundEntryModel(final ReturnAction _action, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner, final RefundReason _reason, final ReturnStatus _status)
	{
		super();
		setAction(_action);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
		setReason(_reason);
		setStatus(_status);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RefundEntry.amount</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the amount
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
	public BigDecimal getAmount()
	{
		return getPersistenceContext().getPropertyValue(AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RefundEntry.currency</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RefundEntry.reason</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the reason
	 */
	@Accessor(qualifier = "reason", type = Accessor.Type.GETTER)
	public RefundReason getReason()
	{
		return getPersistenceContext().getPropertyValue(REASON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RefundEntry.refundedDate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the refundedDate
	 */
	@Accessor(qualifier = "refundedDate", type = Accessor.Type.GETTER)
	public Date getRefundedDate()
	{
		return getPersistenceContext().getPropertyValue(REFUNDEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RefundEntry.amount</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the amount
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
	public void setAmount(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(AMOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RefundEntry.reason</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the reason
	 */
	@Accessor(qualifier = "reason", type = Accessor.Type.SETTER)
	public void setReason(final RefundReason value)
	{
		getPersistenceContext().setPropertyValue(REASON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RefundEntry.refundedDate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the refundedDate
	 */
	@Accessor(qualifier = "refundedDate", type = Accessor.Type.SETTER)
	public void setRefundedDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(REFUNDEDDATE, value);
	}
	
}
