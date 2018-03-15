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
package de.hybris.platform.payment.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Generated model class for type PaymentTransaction first defined at extension payment.
 */
@SuppressWarnings("all")
public class PaymentTransactionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PaymentTransaction";
	
	/**<i>Generated relation code constant for relation <code>Order2PaymentTransaction</code> defining source attribute <code>order</code> in extension <code>payment</code>.</i>*/
	public static final String _ORDER2PAYMENTTRANSACTION = "Order2PaymentTransaction";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.code</code> attribute defined at extension <code>payment</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.requestId</code> attribute defined at extension <code>payment</code>. */
	public static final String REQUESTID = "requestId";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.requestToken</code> attribute defined at extension <code>payment</code>. */
	public static final String REQUESTTOKEN = "requestToken";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.paymentProvider</code> attribute defined at extension <code>payment</code>. */
	public static final String PAYMENTPROVIDER = "paymentProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.plannedAmount</code> attribute defined at extension <code>payment</code>. */
	public static final String PLANNEDAMOUNT = "plannedAmount";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.currency</code> attribute defined at extension <code>payment</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.info</code> attribute defined at extension <code>payment</code>. */
	public static final String INFO = "info";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.versionID</code> attribute defined at extension <code>payment</code>. */
	public static final String VERSIONID = "versionID";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.entries</code> attribute defined at extension <code>payment</code>. */
	public static final String ENTRIES = "entries";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransaction.order</code> attribute defined at extension <code>payment</code>. */
	public static final String ORDER = "order";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PaymentTransactionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PaymentTransactionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PaymentTransaction</code> at extension <code>payment</code>
	 */
	@Deprecated
	public PaymentTransactionModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PaymentTransaction</code> at extension <code>payment</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _versionID initial attribute declared by type <code>PaymentTransaction</code> at extension <code>payment</code>
	 */
	@Deprecated
	public PaymentTransactionModel(final String _code, final ItemModel _owner, final String _versionID)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setVersionID(_versionID);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.code</code> attribute defined at extension <code>payment</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.currency</code> attribute defined at extension <code>payment</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.entries</code> attribute defined at extension <code>payment</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
	public List<PaymentTransactionEntryModel> getEntries()
	{
		return getPersistenceContext().getPropertyValue(ENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.info</code> attribute defined at extension <code>payment</code>. 
	 * @return the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.GETTER)
	public PaymentInfoModel getInfo()
	{
		return getPersistenceContext().getPropertyValue(INFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.order</code> attribute defined at extension <code>payment</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public AbstractOrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.paymentProvider</code> attribute defined at extension <code>payment</code>. 
	 * @return the paymentProvider
	 */
	@Accessor(qualifier = "paymentProvider", type = Accessor.Type.GETTER)
	public String getPaymentProvider()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.plannedAmount</code> attribute defined at extension <code>payment</code>. 
	 * @return the plannedAmount
	 */
	@Accessor(qualifier = "plannedAmount", type = Accessor.Type.GETTER)
	public BigDecimal getPlannedAmount()
	{
		return getPersistenceContext().getPropertyValue(PLANNEDAMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.requestId</code> attribute defined at extension <code>payment</code>. 
	 * @return the requestId
	 */
	@Accessor(qualifier = "requestId", type = Accessor.Type.GETTER)
	public String getRequestId()
	{
		return getPersistenceContext().getPropertyValue(REQUESTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.requestToken</code> attribute defined at extension <code>payment</code>. 
	 * @return the requestToken
	 */
	@Accessor(qualifier = "requestToken", type = Accessor.Type.GETTER)
	public String getRequestToken()
	{
		return getPersistenceContext().getPropertyValue(REQUESTTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransaction.versionID</code> attribute defined at extension <code>payment</code>. 
	 * @return the versionID
	 */
	@Accessor(qualifier = "versionID", type = Accessor.Type.GETTER)
	public String getVersionID()
	{
		return getPersistenceContext().getPropertyValue(VERSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.code</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.currency</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.entries</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
	public void setEntries(final List<PaymentTransactionEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.info</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.SETTER)
	public void setInfo(final PaymentInfoModel value)
	{
		getPersistenceContext().setPropertyValue(INFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.order</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.paymentProvider</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the paymentProvider
	 */
	@Accessor(qualifier = "paymentProvider", type = Accessor.Type.SETTER)
	public void setPaymentProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.plannedAmount</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the plannedAmount
	 */
	@Accessor(qualifier = "plannedAmount", type = Accessor.Type.SETTER)
	public void setPlannedAmount(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(PLANNEDAMOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.requestId</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the requestId
	 */
	@Accessor(qualifier = "requestId", type = Accessor.Type.SETTER)
	public void setRequestId(final String value)
	{
		getPersistenceContext().setPropertyValue(REQUESTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransaction.requestToken</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the requestToken
	 */
	@Accessor(qualifier = "requestToken", type = Accessor.Type.SETTER)
	public void setRequestToken(final String value)
	{
		getPersistenceContext().setPropertyValue(REQUESTTOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PaymentTransaction.versionID</code> attribute defined at extension <code>payment</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the versionID
	 */
	@Accessor(qualifier = "versionID", type = Accessor.Type.SETTER)
	public void setVersionID(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSIONID, value);
	}
	
}
