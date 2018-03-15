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
package de.hybris.platform.acceleratorwebservicesaddon.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PaymentSubscriptionResult first defined at extension acceleratorwebservicesaddon.
 * <p>
 * Used for storing subscription result.
 */
@SuppressWarnings("all")
public class PaymentSubscriptionResultModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PaymentSubscriptionResult";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentSubscriptionResult.cartId</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. */
	public static final String CARTID = "cartId";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentSubscriptionResult.success</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. */
	public static final String SUCCESS = "success";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentSubscriptionResult.result</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. */
	public static final String RESULT = "result";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PaymentSubscriptionResultModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PaymentSubscriptionResultModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cartId initial attribute declared by type <code>PaymentSubscriptionResult</code> at extension <code>acceleratorwebservicesaddon</code>
	 */
	@Deprecated
	public PaymentSubscriptionResultModel(final String _cartId)
	{
		super();
		setCartId(_cartId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cartId initial attribute declared by type <code>PaymentSubscriptionResult</code> at extension <code>acceleratorwebservicesaddon</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PaymentSubscriptionResultModel(final String _cartId, final ItemModel _owner)
	{
		super();
		setCartId(_cartId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.cartId</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 * @return the cartId
	 */
	@Accessor(qualifier = "cartId", type = Accessor.Type.GETTER)
	public String getCartId()
	{
		return getPersistenceContext().getPropertyValue(CARTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.result</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 * @return the result - Serialized result object
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.GETTER)
	public Object getResult()
	{
		return getPersistenceContext().getPropertyValue(RESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentSubscriptionResult.success</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 * @return the success - Define if subscription was successful.
	 */
	@Accessor(qualifier = "success", type = Accessor.Type.GETTER)
	public boolean isSuccess()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SUCCESS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentSubscriptionResult.cartId</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 *  
	 * @param value the cartId
	 */
	@Accessor(qualifier = "cartId", type = Accessor.Type.SETTER)
	public void setCartId(final String value)
	{
		getPersistenceContext().setPropertyValue(CARTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentSubscriptionResult.result</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 *  
	 * @param value the result - Serialized result object
	 */
	@Accessor(qualifier = "result", type = Accessor.Type.SETTER)
	public void setResult(final Object value)
	{
		getPersistenceContext().setPropertyValue(RESULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentSubscriptionResult.success</code> attribute defined at extension <code>acceleratorwebservicesaddon</code>. 
	 *  
	 * @param value the success - Define if subscription was successful.
	 */
	@Accessor(qualifier = "success", type = Accessor.Type.SETTER)
	public void setSuccess(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SUCCESS, toObject(value));
	}
	
}
