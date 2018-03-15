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
package de.hybris.platform.ordercancel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type OrderCancelRecordEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderCancelRecordEntryModel extends OrderModificationRecordEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderCancelRecordEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelRecordEntry.refusedMessage</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String REFUSEDMESSAGE = "refusedMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelRecordEntry.cancelResult</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELRESULT = "cancelResult";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELREASON = "cancelReason";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderCancelRecordEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderCancelRecordEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecord initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _originalVersion initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _status initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderCancelRecordEntryModel(final String _code, final OrderModificationRecordModel _modificationRecord, final OrderHistoryEntryModel _originalVersion, final OrderModificationEntryStatus _status, final Date _timestamp)
	{
		super();
		setCode(_code);
		setModificationRecord(_modificationRecord);
		setOriginalVersion(_originalVersion);
		setStatus(_status);
		setTimestamp(_timestamp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecord initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _originalVersion initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _status initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderCancelRecordEntryModel(final String _code, final OrderModificationRecordModel _modificationRecord, final OrderHistoryEntryModel _originalVersion, final ItemModel _owner, final OrderModificationEntryStatus _status, final Date _timestamp)
	{
		super();
		setCode(_code);
		setModificationRecord(_modificationRecord);
		setOriginalVersion(_originalVersion);
		setOwner(_owner);
		setStatus(_status);
		setTimestamp(_timestamp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelReason
	 */
	@Accessor(qualifier = "cancelReason", type = Accessor.Type.GETTER)
	public CancelReason getCancelReason()
	{
		return getPersistenceContext().getPropertyValue(CANCELREASON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelRecordEntry.cancelResult</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelResult
	 */
	@Accessor(qualifier = "cancelResult", type = Accessor.Type.GETTER)
	public OrderCancelEntryStatus getCancelResult()
	{
		return getPersistenceContext().getPropertyValue(CANCELRESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelRecordEntry.refusedMessage</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the refusedMessage
	 */
	@Accessor(qualifier = "refusedMessage", type = Accessor.Type.GETTER)
	public String getRefusedMessage()
	{
		return getPersistenceContext().getPropertyValue(REFUSEDMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelReason
	 */
	@Accessor(qualifier = "cancelReason", type = Accessor.Type.SETTER)
	public void setCancelReason(final CancelReason value)
	{
		getPersistenceContext().setPropertyValue(CANCELREASON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelRecordEntry.cancelResult</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelResult
	 */
	@Accessor(qualifier = "cancelResult", type = Accessor.Type.SETTER)
	public void setCancelResult(final OrderCancelEntryStatus value)
	{
		getPersistenceContext().setPropertyValue(CANCELRESULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelRecordEntry.refusedMessage</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the refusedMessage
	 */
	@Accessor(qualifier = "refusedMessage", type = Accessor.Type.SETTER)
	public void setRefusedMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(REFUSEDMESSAGE, value);
	}
	
}
