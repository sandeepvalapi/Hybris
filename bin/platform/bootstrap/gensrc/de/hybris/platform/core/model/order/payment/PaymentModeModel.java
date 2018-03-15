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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type PaymentMode first defined at extension core.
 */
@SuppressWarnings("all")
public class PaymentModeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PaymentMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTINFOTYPE = "paymentInfoType";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. */
	public static final String SUPPORTEDDELIVERYMODES = "supportedDeliveryModes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PaymentModeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PaymentModeModel(final ItemModelContext ctx)
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
	public PaymentModeModel(final Boolean _active, final String _code, final ComposedTypeModel _paymentInfoType)
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
	public PaymentModeModel(final Boolean _active, final String _code, final ItemModel _owner, final ComposedTypeModel _paymentInfoType)
	{
		super();
		setActive(_active);
		setCode(_code);
		setOwner(_owner);
		setPaymentInfoType(_paymentInfoType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentInfoType
	 * @deprecated since ages - use { @link #getPaymentInfoType()} instead
	 */
	@Deprecated
	public ComposedTypeModel getPaymentinfotype()
	{
		return this.getPaymentInfoType();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentInfoType
	 */
	@Accessor(qualifier = "paymentInfoType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getPaymentInfoType()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTINFOTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supportedDeliveryModes
	 * @deprecated since ages - use { @link #getSupportedDeliveryModes()} instead
	 */
	@Deprecated
	public Collection<DeliveryModeModel> getSupporteddeliverymodes()
	{
		return this.getSupportedDeliveryModes();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supportedDeliveryModes
	 */
	@Accessor(qualifier = "supportedDeliveryModes", type = Accessor.Type.GETTER)
	public Collection<DeliveryModeModel> getSupportedDeliveryModes()
	{
		return getPersistenceContext().getPropertyValue(SUPPORTEDDELIVERYMODES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfoType
	 * @deprecated since ages - use { @link #setPaymentInfoType(de.hybris.platform.core.model.type.ComposedTypeModel)} instead
	 */
	@Deprecated
	public void setPaymentinfotype(final ComposedTypeModel value)
	{
		this.setPaymentInfoType(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfoType
	 */
	@Accessor(qualifier = "paymentInfoType", type = Accessor.Type.SETTER)
	public void setPaymentInfoType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTINFOTYPE, value);
	}
	
}
