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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrderCancelConfig first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderCancelConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderCancelConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.orderCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERCANCELALLOWED = "orderCancelAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.cancelAfterWarehouseAllowed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELAFTERWAREHOUSEALLOWED = "cancelAfterWarehouseAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.completeCancelAfterShippingStartedAllowed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String COMPLETECANCELAFTERSHIPPINGSTARTEDALLOWED = "completeCancelAfterShippingStartedAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.partialCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PARTIALCANCELALLOWED = "partialCancelAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.partialOrderEntryCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PARTIALORDERENTRYCANCELALLOWED = "partialOrderEntryCancelAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderCancelConfig.queuedOrderWaitingTime</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String QUEUEDORDERWAITINGTIME = "queuedOrderWaitingTime";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderCancelConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderCancelConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cancelAfterWarehouseAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _completeCancelAfterShippingStartedAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _orderCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _partialCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _partialOrderEntryCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _queuedOrderWaitingTime initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderCancelConfigModel(final boolean _cancelAfterWarehouseAllowed, final boolean _completeCancelAfterShippingStartedAllowed, final boolean _orderCancelAllowed, final boolean _partialCancelAllowed, final boolean _partialOrderEntryCancelAllowed, final int _queuedOrderWaitingTime)
	{
		super();
		setCancelAfterWarehouseAllowed(_cancelAfterWarehouseAllowed);
		setCompleteCancelAfterShippingStartedAllowed(_completeCancelAfterShippingStartedAllowed);
		setOrderCancelAllowed(_orderCancelAllowed);
		setPartialCancelAllowed(_partialCancelAllowed);
		setPartialOrderEntryCancelAllowed(_partialOrderEntryCancelAllowed);
		setQueuedOrderWaitingTime(_queuedOrderWaitingTime);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cancelAfterWarehouseAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _completeCancelAfterShippingStartedAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _orderCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partialCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _partialOrderEntryCancelAllowed initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 * @param _queuedOrderWaitingTime initial attribute declared by type <code>OrderCancelConfig</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderCancelConfigModel(final boolean _cancelAfterWarehouseAllowed, final boolean _completeCancelAfterShippingStartedAllowed, final boolean _orderCancelAllowed, final ItemModel _owner, final boolean _partialCancelAllowed, final boolean _partialOrderEntryCancelAllowed, final int _queuedOrderWaitingTime)
	{
		super();
		setCancelAfterWarehouseAllowed(_cancelAfterWarehouseAllowed);
		setCompleteCancelAfterShippingStartedAllowed(_completeCancelAfterShippingStartedAllowed);
		setOrderCancelAllowed(_orderCancelAllowed);
		setOwner(_owner);
		setPartialCancelAllowed(_partialCancelAllowed);
		setPartialOrderEntryCancelAllowed(_partialOrderEntryCancelAllowed);
		setQueuedOrderWaitingTime(_queuedOrderWaitingTime);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.queuedOrderWaitingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the queuedOrderWaitingTime - Determines how long an order should wait in queue before it's started to be fullfilled
	 */
	@Accessor(qualifier = "queuedOrderWaitingTime", type = Accessor.Type.GETTER)
	public int getQueuedOrderWaitingTime()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(QUEUEDORDERWAITINGTIME));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.cancelAfterWarehouseAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelAfterWarehouseAllowed - Determines if the order cancelling is still possible after sending it to warehouse
	 */
	@Accessor(qualifier = "cancelAfterWarehouseAllowed", type = Accessor.Type.GETTER)
	public boolean isCancelAfterWarehouseAllowed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(CANCELAFTERWAREHOUSEALLOWED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.completeCancelAfterShippingStartedAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the completeCancelAfterShippingStartedAllowed - Determines if a complete cancel request is allowed after shipping has been started. This means: Cancel the part of the order that has not been shipped yet. Evaluated only if cancelAfterWarehouseAllowed is true.
	 */
	@Accessor(qualifier = "completeCancelAfterShippingStartedAllowed", type = Accessor.Type.GETTER)
	public boolean isCompleteCancelAfterShippingStartedAllowed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(COMPLETECANCELAFTERSHIPPINGSTARTEDALLOWED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.orderCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the orderCancelAllowed - Determines if the order cancelling is at all possible
	 */
	@Accessor(qualifier = "orderCancelAllowed", type = Accessor.Type.GETTER)
	public boolean isOrderCancelAllowed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ORDERCANCELALLOWED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.partialCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the partialCancelAllowed - Determines if the partial order cancelling (discarding whole order entries) is possible.
	 */
	@Accessor(qualifier = "partialCancelAllowed", type = Accessor.Type.GETTER)
	public boolean isPartialCancelAllowed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PARTIALCANCELALLOWED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderCancelConfig.partialOrderEntryCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the partialOrderEntryCancelAllowed - Determines if the partial order entry cancelling (discarding parts of order entries) is possible.  This parameter is evaluated only if partialCancelAllowed is set to true.
	 */
	@Accessor(qualifier = "partialOrderEntryCancelAllowed", type = Accessor.Type.GETTER)
	public boolean isPartialOrderEntryCancelAllowed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PARTIALORDERENTRYCANCELALLOWED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.cancelAfterWarehouseAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelAfterWarehouseAllowed - Determines if the order cancelling is still possible after sending it to warehouse
	 */
	@Accessor(qualifier = "cancelAfterWarehouseAllowed", type = Accessor.Type.SETTER)
	public void setCancelAfterWarehouseAllowed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(CANCELAFTERWAREHOUSEALLOWED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.completeCancelAfterShippingStartedAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the completeCancelAfterShippingStartedAllowed - Determines if a complete cancel request is allowed after shipping has been started. This means: Cancel the part of the order that has not been shipped yet. Evaluated only if cancelAfterWarehouseAllowed is true.
	 */
	@Accessor(qualifier = "completeCancelAfterShippingStartedAllowed", type = Accessor.Type.SETTER)
	public void setCompleteCancelAfterShippingStartedAllowed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(COMPLETECANCELAFTERSHIPPINGSTARTEDALLOWED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.orderCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the orderCancelAllowed - Determines if the order cancelling is at all possible
	 */
	@Accessor(qualifier = "orderCancelAllowed", type = Accessor.Type.SETTER)
	public void setOrderCancelAllowed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ORDERCANCELALLOWED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.partialCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the partialCancelAllowed - Determines if the partial order cancelling (discarding whole order entries) is possible.
	 */
	@Accessor(qualifier = "partialCancelAllowed", type = Accessor.Type.SETTER)
	public void setPartialCancelAllowed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PARTIALCANCELALLOWED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.partialOrderEntryCancelAllowed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the partialOrderEntryCancelAllowed - Determines if the partial order entry cancelling (discarding parts of order entries) is possible.  This parameter is evaluated only if partialCancelAllowed is set to true.
	 */
	@Accessor(qualifier = "partialOrderEntryCancelAllowed", type = Accessor.Type.SETTER)
	public void setPartialOrderEntryCancelAllowed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PARTIALORDERENTRYCANCELALLOWED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderCancelConfig.queuedOrderWaitingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the queuedOrderWaitingTime - Determines how long an order should wait in queue before it's started to be fullfilled
	 */
	@Accessor(qualifier = "queuedOrderWaitingTime", type = Accessor.Type.SETTER)
	public void setQueuedOrderWaitingTime(final int value)
	{
		getPersistenceContext().setPropertyValue(QUEUEDORDERWAITINGTIME, toObject(value));
	}
	
}
