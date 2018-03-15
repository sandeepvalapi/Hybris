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
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type ReturnRequest first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ReturnRequestModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ReturnRequest";
	
	/**<i>Generated relation code constant for relation <code>Order2ReturnRequest</code> defining source attribute <code>order</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDER2RETURNREQUEST = "Order2ReturnRequest";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.RMA</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RMA = "RMA";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.replacementOrder</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String REPLACEMENTORDER = "replacementOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.currency</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.status</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.subtotal</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String SUBTOTAL = "subtotal";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.returnLabel</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNLABEL = "returnLabel";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.returnForm</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNFORM = "returnForm";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.returnWarehouse</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNWAREHOUSE = "returnWarehouse";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.orderPOS</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERPOS = "orderPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.order</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.returnEntries</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNENTRIES = "returnEntries";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.returnProcess</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNPROCESS = "returnProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnRequest.refundDeliveryCost</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String REFUNDDELIVERYCOST = "refundDeliveryCost";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReturnRequestModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReturnRequestModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ReturnRequest</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ReturnRequestModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ReturnRequest</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ReturnRequestModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.currency</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.order</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public OrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.refundDeliveryCost</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	@Accessor(qualifier = "refundDeliveryCost", type = Accessor.Type.GETTER)
	public Boolean getRefundDeliveryCost()
	{
		return getPersistenceContext().getPropertyValue(REFUNDDELIVERYCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.replacementOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the replacementOrder
	 */
	@Accessor(qualifier = "replacementOrder", type = Accessor.Type.GETTER)
	public ReplacementOrderModel getReplacementOrder()
	{
		return getPersistenceContext().getPropertyValue(REPLACEMENTORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.returnEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the returnEntries
	 */
	@Accessor(qualifier = "returnEntries", type = Accessor.Type.GETTER)
	public List<ReturnEntryModel> getReturnEntries()
	{
		return getPersistenceContext().getPropertyValue(RETURNENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.returnForm</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnForm - The return form for the ReturnRequest.
	 */
	@Accessor(qualifier = "returnForm", type = Accessor.Type.GETTER)
	public MediaModel getReturnForm()
	{
		return getPersistenceContext().getPropertyValue(RETURNFORM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.returnLabel</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnLabel - The return shipping label for the ReturnRequest.
	 */
	@Accessor(qualifier = "returnLabel", type = Accessor.Type.GETTER)
	public MediaModel getReturnLabel()
	{
		return getPersistenceContext().getPropertyValue(RETURNLABEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.returnProcess</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the returnProcess
	 */
	@Accessor(qualifier = "returnProcess", type = Accessor.Type.GETTER)
	public Collection<ReturnProcessModel> getReturnProcess()
	{
		return getPersistenceContext().getPropertyValue(RETURNPROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.returnWarehouse</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnWarehouse - Determines the warehouse where the products need to be returned by the customer
	 */
	@Accessor(qualifier = "returnWarehouse", type = Accessor.Type.GETTER)
	public WarehouseModel getReturnWarehouse()
	{
		return getPersistenceContext().getPropertyValue(RETURNWAREHOUSE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.RMA</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the RMA
	 */
	@Accessor(qualifier = "RMA", type = Accessor.Type.GETTER)
	public String getRMA()
	{
		return getPersistenceContext().getPropertyValue(RMA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.status</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public ReturnStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.subtotal</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the subtotal
	 */
	@Accessor(qualifier = "subtotal", type = Accessor.Type.GETTER)
	public BigDecimal getSubtotal()
	{
		return getPersistenceContext().getPropertyValue(SUBTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.code</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.currency</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.order</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.refundDeliveryCost</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the refundDeliveryCost - Include Delivery Cost in the Refund Amount
	 */
	@Accessor(qualifier = "refundDeliveryCost", type = Accessor.Type.SETTER)
	public void setRefundDeliveryCost(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REFUNDDELIVERYCOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.replacementOrder</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the replacementOrder
	 */
	@Accessor(qualifier = "replacementOrder", type = Accessor.Type.SETTER)
	public void setReplacementOrder(final ReplacementOrderModel value)
	{
		getPersistenceContext().setPropertyValue(REPLACEMENTORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.returnEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnEntries
	 */
	@Accessor(qualifier = "returnEntries", type = Accessor.Type.SETTER)
	public void setReturnEntries(final List<ReturnEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(RETURNENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.returnForm</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnForm - The return form for the ReturnRequest.
	 */
	@Accessor(qualifier = "returnForm", type = Accessor.Type.SETTER)
	public void setReturnForm(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(RETURNFORM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.returnLabel</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnLabel - The return shipping label for the ReturnRequest.
	 */
	@Accessor(qualifier = "returnLabel", type = Accessor.Type.SETTER)
	public void setReturnLabel(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(RETURNLABEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.returnProcess</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnProcess
	 */
	@Accessor(qualifier = "returnProcess", type = Accessor.Type.SETTER)
	public void setReturnProcess(final Collection<ReturnProcessModel> value)
	{
		getPersistenceContext().setPropertyValue(RETURNPROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.returnWarehouse</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnWarehouse - Determines the warehouse where the products need to be returned by the customer
	 */
	@Accessor(qualifier = "returnWarehouse", type = Accessor.Type.SETTER)
	public void setReturnWarehouse(final WarehouseModel value)
	{
		getPersistenceContext().setPropertyValue(RETURNWAREHOUSE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.RMA</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the RMA
	 */
	@Accessor(qualifier = "RMA", type = Accessor.Type.SETTER)
	public void setRMA(final String value)
	{
		getPersistenceContext().setPropertyValue(RMA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.status</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final ReturnStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnRequest.subtotal</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the subtotal
	 */
	@Accessor(qualifier = "subtotal", type = Accessor.Type.SETTER)
	public void setSubtotal(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(SUBTOTAL, value);
	}
	
}
