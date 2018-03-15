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
package de.hybris.platform.paymentstandard.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.paymentstandard.model.StandardPaymentModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type StandardPaymentModeValue first defined at extension paymentstandard.
 */
@SuppressWarnings("all")
public class StandardPaymentModeValueModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StandardPaymentModeValue";
	
	/**<i>Generated relation code constant for relation <code>StdModeValuesRelation</code> defining source attribute <code>paymentMode</code> in extension <code>paymentstandard</code>.</i>*/
	public static final String _STDMODEVALUESRELATION = "StdModeValuesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>StandardPaymentModeValue.currency</code> attribute defined at extension <code>paymentstandard</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>StandardPaymentModeValue.value</code> attribute defined at extension <code>paymentstandard</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>StandardPaymentModeValue.paymentMode</code> attribute defined at extension <code>paymentstandard</code>. */
	public static final String PAYMENTMODE = "paymentMode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StandardPaymentModeValueModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StandardPaymentModeValueModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 * @param _paymentMode initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 * @param _value initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 */
	@Deprecated
	public StandardPaymentModeValueModel(final CurrencyModel _currency, final StandardPaymentModeModel _paymentMode, final Double _value)
	{
		super();
		setCurrency(_currency);
		setPaymentMode(_paymentMode);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _paymentMode initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 * @param _value initial attribute declared by type <code>StandardPaymentModeValue</code> at extension <code>paymentstandard</code>
	 */
	@Deprecated
	public StandardPaymentModeValueModel(final CurrencyModel _currency, final ItemModel _owner, final StandardPaymentModeModel _paymentMode, final Double _value)
	{
		super();
		setCurrency(_currency);
		setOwner(_owner);
		setPaymentMode(_paymentMode);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StandardPaymentModeValue.currency</code> attribute defined at extension <code>paymentstandard</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StandardPaymentModeValue.paymentMode</code> attribute defined at extension <code>paymentstandard</code>. 
	 * @return the paymentMode
	 */
	@Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
	public StandardPaymentModeModel getPaymentMode()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StandardPaymentModeValue.value</code> attribute defined at extension <code>paymentstandard</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Double getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StandardPaymentModeValue.currency</code> attribute defined at extension <code>paymentstandard</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StandardPaymentModeValue.paymentMode</code> attribute defined at extension <code>paymentstandard</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the paymentMode
	 */
	@Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
	public void setPaymentMode(final StandardPaymentModeModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StandardPaymentModeValue.value</code> attribute defined at extension <code>paymentstandard</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Double value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
