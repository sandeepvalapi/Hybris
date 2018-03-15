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
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.paymentstandard.model.StandardPaymentModeValueModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type StandardPaymentMode first defined at extension paymentstandard.
 */
@SuppressWarnings("all")
public class StandardPaymentModeModel extends PaymentModeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StandardPaymentMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>StandardPaymentMode.net</code> attribute defined at extension <code>paymentstandard</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>StandardPaymentMode.paymentModeValues</code> attribute defined at extension <code>paymentstandard</code>. */
	public static final String PAYMENTMODEVALUES = "paymentModeValues";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StandardPaymentModeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StandardPaymentModeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _paymentInfoType initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 */
	@Deprecated
	public StandardPaymentModeModel(final Boolean _active, final String _code, final ComposedTypeModel _paymentInfoType)
	{
		super();
		setActive(_active);
		setCode(_code);
		setPaymentInfoType(_paymentInfoType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _paymentInfoType initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 */
	@Deprecated
	public StandardPaymentModeModel(final Boolean _active, final String _code, final ItemModel _owner, final ComposedTypeModel _paymentInfoType)
	{
		super();
		setActive(_active);
		setCode(_code);
		setOwner(_owner);
		setPaymentInfoType(_paymentInfoType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StandardPaymentMode.net</code> attribute defined at extension <code>paymentstandard</code>. 
	 * @return the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StandardPaymentMode.paymentModeValues</code> attribute defined at extension <code>paymentstandard</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the paymentModeValues
	 */
	@Accessor(qualifier = "paymentModeValues", type = Accessor.Type.GETTER)
	public Collection<StandardPaymentModeValueModel> getPaymentModeValues()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTMODEVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StandardPaymentMode.net</code> attribute defined at extension <code>paymentstandard</code>. 
	 *  
	 * @param value the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StandardPaymentMode.paymentModeValues</code> attribute defined at extension <code>paymentstandard</code>. 
	 *  
	 * @param value the paymentModeValues
	 */
	@Accessor(qualifier = "paymentModeValues", type = Accessor.Type.SETTER)
	public void setPaymentModeValues(final Collection<StandardPaymentModeValueModel> value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTMODEVALUES, value);
	}
	
}
