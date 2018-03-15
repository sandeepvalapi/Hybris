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
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Generated model class for type PaymentTransactionEntry first defined at extension payment.
 */
@SuppressWarnings("all")
public class PaymentTransactionEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PaymentTransactionEntry";
	
	/**<i>Generated relation code constant for relation <code>PaymentTransaction2PaymentTransactionEntry</code> defining source attribute <code>paymentTransaction</code> in extension <code>payment</code>.</i>*/
	public static final String _PAYMENTTRANSACTION2PAYMENTTRANSACTIONENTRY = "PaymentTransaction2PaymentTransactionEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.type</code> attribute defined at extension <code>payment</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.amount</code> attribute defined at extension <code>payment</code>. */
	public static final String AMOUNT = "amount";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.currency</code> attribute defined at extension <code>payment</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.time</code> attribute defined at extension <code>payment</code>. */
	public static final String TIME = "time";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.transactionStatus</code> attribute defined at extension <code>payment</code>. */
	public static final String TRANSACTIONSTATUS = "transactionStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.transactionStatusDetails</code> attribute defined at extension <code>payment</code>. */
	public static final String TRANSACTIONSTATUSDETAILS = "transactionStatusDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.requestToken</code> attribute defined at extension <code>payment</code>. */
	public static final String REQUESTTOKEN = "requestToken";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.requestId</code> attribute defined at extension <code>payment</code>. */
	public static final String REQUESTID = "requestId";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.subscriptionID</code> attribute defined at extension <code>payment</code>. */
	public static final String SUBSCRIPTIONID = "subscriptionID";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.code</code> attribute defined at extension <code>payment</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.versionID</code> attribute defined at extension <code>payment</code>. */
	public static final String VERSIONID = "versionID";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentTransactionEntry.paymentTransaction</code> attribute defined at extension <code>payment</code>. */
	public static final String PAYMENTTRANSACTION = "paymentTransaction";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PaymentTransactionEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PaymentTransactionEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PaymentTransactionEntry</code> at extension <code>payment</code>
	 */
	@Deprecated
	public PaymentTransactionEntryModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PaymentTransactionEntry</code> at extension <code>payment</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _versionID initial attribute declared by type <code>PaymentTransactionEntry</code> at extension <code>payment</code>
	 */
	@Deprecated
	public PaymentTransactionEntryModel(final String _code, final ItemModel _owner, final String _versionID)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setVersionID(_versionID);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.amount</code> attribute defined at extension <code>payment</code>. 
	 * @return the amount
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
	public BigDecimal getAmount()
	{
		return getPersistenceContext().getPropertyValue(AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.code</code> attribute defined at extension <code>payment</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.currency</code> attribute defined at extension <code>payment</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.paymentTransaction</code> attribute defined at extension <code>payment</code>. 
	 * @return the paymentTransaction
	 */
	@Accessor(qualifier = "paymentTransaction", type = Accessor.Type.GETTER)
	public PaymentTransactionModel getPaymentTransaction()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTTRANSACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.requestId</code> attribute defined at extension <code>payment</code>. 
	 * @return the requestId
	 */
	@Accessor(qualifier = "requestId", type = Accessor.Type.GETTER)
	public String getRequestId()
	{
		return getPersistenceContext().getPropertyValue(REQUESTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.requestToken</code> attribute defined at extension <code>payment</code>. 
	 * @return the requestToken
	 */
	@Accessor(qualifier = "requestToken", type = Accessor.Type.GETTER)
	public String getRequestToken()
	{
		return getPersistenceContext().getPropertyValue(REQUESTTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.subscriptionID</code> attribute defined at extension <code>payment</code>. 
	 * @return the subscriptionID
	 */
	@Accessor(qualifier = "subscriptionID", type = Accessor.Type.GETTER)
	public String getSubscriptionID()
	{
		return getPersistenceContext().getPropertyValue(SUBSCRIPTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.time</code> attribute defined at extension <code>payment</code>. 
	 * @return the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.GETTER)
	public Date getTime()
	{
		return getPersistenceContext().getPropertyValue(TIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.transactionStatus</code> attribute defined at extension <code>payment</code>. 
	 * @return the transactionStatus
	 */
	@Accessor(qualifier = "transactionStatus", type = Accessor.Type.GETTER)
	public String getTransactionStatus()
	{
		return getPersistenceContext().getPropertyValue(TRANSACTIONSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.transactionStatusDetails</code> attribute defined at extension <code>payment</code>. 
	 * @return the transactionStatusDetails
	 */
	@Accessor(qualifier = "transactionStatusDetails", type = Accessor.Type.GETTER)
	public String getTransactionStatusDetails()
	{
		return getPersistenceContext().getPropertyValue(TRANSACTIONSTATUSDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.type</code> attribute defined at extension <code>payment</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public PaymentTransactionType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.versionID</code> attribute defined at extension <code>payment</code>. 
	 * @return the versionID
	 */
	@Accessor(qualifier = "versionID", type = Accessor.Type.GETTER)
	public String getVersionID()
	{
		return getPersistenceContext().getPropertyValue(VERSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.amount</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the amount
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
	public void setAmount(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(AMOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.code</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.currency</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.paymentTransaction</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the paymentTransaction
	 */
	@Accessor(qualifier = "paymentTransaction", type = Accessor.Type.SETTER)
	public void setPaymentTransaction(final PaymentTransactionModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTTRANSACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.requestId</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the requestId
	 */
	@Accessor(qualifier = "requestId", type = Accessor.Type.SETTER)
	public void setRequestId(final String value)
	{
		getPersistenceContext().setPropertyValue(REQUESTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.requestToken</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the requestToken
	 */
	@Accessor(qualifier = "requestToken", type = Accessor.Type.SETTER)
	public void setRequestToken(final String value)
	{
		getPersistenceContext().setPropertyValue(REQUESTTOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.subscriptionID</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the subscriptionID
	 */
	@Accessor(qualifier = "subscriptionID", type = Accessor.Type.SETTER)
	public void setSubscriptionID(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBSCRIPTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.time</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.SETTER)
	public void setTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.transactionStatus</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the transactionStatus
	 */
	@Accessor(qualifier = "transactionStatus", type = Accessor.Type.SETTER)
	public void setTransactionStatus(final String value)
	{
		getPersistenceContext().setPropertyValue(TRANSACTIONSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.transactionStatusDetails</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the transactionStatusDetails
	 */
	@Accessor(qualifier = "transactionStatusDetails", type = Accessor.Type.SETTER)
	public void setTransactionStatusDetails(final String value)
	{
		getPersistenceContext().setPropertyValue(TRANSACTIONSTATUSDETAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentTransactionEntry.type</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final PaymentTransactionType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PaymentTransactionEntry.versionID</code> attribute defined at extension <code>payment</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the versionID
	 */
	@Accessor(qualifier = "versionID", type = Accessor.Type.SETTER)
	public void setVersionID(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSIONID, value);
	}
	
}
