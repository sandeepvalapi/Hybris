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
package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CreditCardPaymentInfo first defined at extension core.
 */
@SuppressWarnings("all")
public class CreditCardPaymentInfoModel extends PaymentInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CreditCardPaymentInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. */
	public static final String CCOWNER = "ccOwner";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. */
	public static final String NUMBER = "number";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDTOMONTH = "validToMonth";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDTOYEAR = "validToYear";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDFROMMONTH = "validFromMonth";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDFROMYEAR = "validFromYear";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.subscriptionId</code> attribute defined at extension <code>payment</code>. */
	public static final String SUBSCRIPTIONID = "subscriptionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.issueNumber</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ISSUENUMBER = "issueNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.subscriptionValidated</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SUBSCRIPTIONVALIDATED = "subscriptionValidated";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CreditCardPaymentInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CreditCardPaymentInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _ccOwner initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _number initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _validToMonth initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _validToYear initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public CreditCardPaymentInfoModel(final String _ccOwner, final String _code, final String _number, final CreditCardType _type, final UserModel _user, final String _validToMonth, final String _validToYear)
	{
		super();
		setCcOwner(_ccOwner);
		setCode(_code);
		setNumber(_number);
		setType(_type);
		setUser(_user);
		setValidToMonth(_validToMonth);
		setValidToYear(_validToYear);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _ccOwner initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _number initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _original initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _validToMonth initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _validToYear initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public CreditCardPaymentInfoModel(final String _ccOwner, final String _code, final String _number, final ItemModel _original, final ItemModel _owner, final CreditCardType _type, final UserModel _user, final String _validToMonth, final String _validToYear)
	{
		super();
		setCcOwner(_ccOwner);
		setCode(_code);
		setNumber(_number);
		setOriginal(_original);
		setOwner(_owner);
		setType(_type);
		setUser(_user);
		setValidToMonth(_validToMonth);
		setValidToYear(_validToYear);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. 
	 * @return the ccOwner
	 */
	@Accessor(qualifier = "ccOwner", type = Accessor.Type.GETTER)
	public String getCcOwner()
	{
		return getPersistenceContext().getPropertyValue(CCOWNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.issueNumber</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	@Accessor(qualifier = "issueNumber", type = Accessor.Type.GETTER)
	public Integer getIssueNumber()
	{
		return getPersistenceContext().getPropertyValue(ISSUENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. 
	 * @return the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.GETTER)
	public String getNumber()
	{
		return getPersistenceContext().getPropertyValue(NUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.subscriptionId</code> attribute defined at extension <code>payment</code>. 
	 * @return the subscriptionId - Subscription ID is the reference information for the credit cart data stored in the external payment provider.
	 */
	@Accessor(qualifier = "subscriptionId", type = Accessor.Type.GETTER)
	public String getSubscriptionId()
	{
		return getPersistenceContext().getPropertyValue(SUBSCRIPTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public CreditCardType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. 
	 * @return the validFromMonth
	 */
	@Accessor(qualifier = "validFromMonth", type = Accessor.Type.GETTER)
	public String getValidFromMonth()
	{
		return getPersistenceContext().getPropertyValue(VALIDFROMMONTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. 
	 * @return the validFromYear
	 */
	@Accessor(qualifier = "validFromYear", type = Accessor.Type.GETTER)
	public String getValidFromYear()
	{
		return getPersistenceContext().getPropertyValue(VALIDFROMYEAR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. 
	 * @return the validToMonth
	 */
	@Accessor(qualifier = "validToMonth", type = Accessor.Type.GETTER)
	public String getValidToMonth()
	{
		return getPersistenceContext().getPropertyValue(VALIDTOMONTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. 
	 * @return the validToYear
	 */
	@Accessor(qualifier = "validToYear", type = Accessor.Type.GETTER)
	public String getValidToYear()
	{
		return getPersistenceContext().getPropertyValue(VALIDTOYEAR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.subscriptionValidated</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the subscriptionValidated - Determines whether the subscription ID has been validated.
	 */
	@Accessor(qualifier = "subscriptionValidated", type = Accessor.Type.GETTER)
	public boolean isSubscriptionValidated()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SUBSCRIPTIONVALIDATED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the ccOwner
	 */
	@Accessor(qualifier = "ccOwner", type = Accessor.Type.SETTER)
	public void setCcOwner(final String value)
	{
		getPersistenceContext().setPropertyValue(CCOWNER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.issueNumber</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the issueNumber - Issue number is the reference information for the credit cart data.
	 */
	@Accessor(qualifier = "issueNumber", type = Accessor.Type.SETTER)
	public void setIssueNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ISSUENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.SETTER)
	public void setNumber(final String value)
	{
		getPersistenceContext().setPropertyValue(NUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.subscriptionId</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the subscriptionId - Subscription ID is the reference information for the credit cart data stored in the external payment provider.
	 */
	@Accessor(qualifier = "subscriptionId", type = Accessor.Type.SETTER)
	public void setSubscriptionId(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBSCRIPTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.subscriptionValidated</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the subscriptionValidated - Determines whether the subscription ID has been validated.
	 */
	@Accessor(qualifier = "subscriptionValidated", type = Accessor.Type.SETTER)
	public void setSubscriptionValidated(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SUBSCRIPTIONVALIDATED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final CreditCardType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validFromMonth
	 */
	@Accessor(qualifier = "validFromMonth", type = Accessor.Type.SETTER)
	public void setValidFromMonth(final String value)
	{
		getPersistenceContext().setPropertyValue(VALIDFROMMONTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validFromYear
	 */
	@Accessor(qualifier = "validFromYear", type = Accessor.Type.SETTER)
	public void setValidFromYear(final String value)
	{
		getPersistenceContext().setPropertyValue(VALIDFROMYEAR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validToMonth
	 */
	@Accessor(qualifier = "validToMonth", type = Accessor.Type.SETTER)
	public void setValidToMonth(final String value)
	{
		getPersistenceContext().setPropertyValue(VALIDTOMONTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validToYear
	 */
	@Accessor(qualifier = "validToYear", type = Accessor.Type.SETTER)
	public void setValidToYear(final String value)
	{
		getPersistenceContext().setPropertyValue(VALIDTOYEAR, value);
	}
	
}
