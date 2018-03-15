/*
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
package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum OrderStatus declared at extension core.
 */
@SuppressWarnings("PMD")
public class OrderStatus implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "OrderStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "OrderStatus";
	private static final ConcurrentMap<String, OrderStatus> cache = new ConcurrentHashMap<String, OrderStatus>();
	/**
	* Generated enum value for OrderStatus.CANCELLING declared at extension basecommerce.
	*/
	public static final OrderStatus CANCELLING = valueOf("CANCELLING");
	
	/**
	* Generated enum value for OrderStatus.CHECKED_VALID declared at extension commerceservices.
	*/
	public static final OrderStatus CHECKED_VALID = valueOf("CHECKED_VALID");
	
	/**
	* Generated enum value for OrderStatus.CREATED declared at extension core.
	*/
	public static final OrderStatus CREATED = valueOf("CREATED");
	
	/**
	* Generated enum value for OrderStatus.CHECKED_INVALID declared at extension commerceservices.
	*/
	public static final OrderStatus CHECKED_INVALID = valueOf("CHECKED_INVALID");
	
	/**
	* Generated enum value for OrderStatus.ON_VALIDATION declared at extension core.
	*/
	public static final OrderStatus ON_VALIDATION = valueOf("ON_VALIDATION");
	
	/**
	* Generated enum value for OrderStatus.SUSPENDED declared at extension basecommerce.
	*/
	public static final OrderStatus SUSPENDED = valueOf("SUSPENDED");
	
	/**
	* Generated enum value for OrderStatus.COMPLETED declared at extension core.
	*/
	public static final OrderStatus COMPLETED = valueOf("COMPLETED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_AUTHORIZED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_AUTHORIZED = valueOf("PAYMENT_AUTHORIZED");
	
	/**
	* Generated enum value for OrderStatus.CANCELLED declared at extension core.
	*/
	public static final OrderStatus CANCELLED = valueOf("CANCELLED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_NOT_AUTHORIZED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_NOT_AUTHORIZED = valueOf("PAYMENT_NOT_AUTHORIZED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_AMOUNT_RESERVED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_AMOUNT_RESERVED = valueOf("PAYMENT_AMOUNT_RESERVED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_AMOUNT_NOT_RESERVED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_AMOUNT_NOT_RESERVED = valueOf("PAYMENT_AMOUNT_NOT_RESERVED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_CAPTURED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_CAPTURED = valueOf("PAYMENT_CAPTURED");
	
	/**
	* Generated enum value for OrderStatus.PAYMENT_NOT_CAPTURED declared at extension commerceservices.
	*/
	public static final OrderStatus PAYMENT_NOT_CAPTURED = valueOf("PAYMENT_NOT_CAPTURED");
	
	/**
	* Generated enum value for OrderStatus.FRAUD_CHECKED declared at extension commerceservices.
	*/
	public static final OrderStatus FRAUD_CHECKED = valueOf("FRAUD_CHECKED");
	
	/**
	* Generated enum value for OrderStatus.ORDER_SPLIT declared at extension commerceservices.
	*/
	public static final OrderStatus ORDER_SPLIT = valueOf("ORDER_SPLIT");
	
	/**
	* Generated enum value for OrderStatus.PROCESSING_ERROR declared at extension commerceservices.
	*/
	public static final OrderStatus PROCESSING_ERROR = valueOf("PROCESSING_ERROR");
	
	/**
	* Generated enum value for OrderStatus.WAIT_FRAUD_MANUAL_CHECK declared at extension commerceservices.
	*/
	public static final OrderStatus WAIT_FRAUD_MANUAL_CHECK = valueOf("WAIT_FRAUD_MANUAL_CHECK");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private OrderStatus(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>OrderStatus
	 * </code> object that contains the enum value <code>code</code> as this object.
	 *  
	 * @param obj the object to compare with.
	 * @return <code>true</code> if the objects are the same;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		try
		{
			final HybrisEnumValue enum2 = (HybrisEnumValue) obj;
			return this == enum2
			|| (enum2 != null && !this.getClass().isEnum() && !enum2.getClass().isEnum()
			&& this.getType().equalsIgnoreCase(enum2.getType()) && this.getCode().equalsIgnoreCase(enum2.getCode()));
		}
		catch (final ClassCastException e)
		{
			return false;
		}
	}
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
	/**
	 * Returns a hash code for this <code>OrderStatus</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>OrderStatus</code> object.
	 */
	@Override
	public int hashCode()
	{
		return this.codeLowerCase.hashCode();
	}
	
	/**
	 * Returns the code representing this enum value.
	 *  
	 * @return a string representation of the value of this object.
	 */
	@Override
	public String toString()
	{
		return this.code.toString();
	}
	
	/**
	 * Returns a <tt>OrderStatus</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>OrderStatus</tt> instance representing <tt>value</tt>. 
	 */
	public static OrderStatus valueOf(final String code)
	{
		final String key = code.toLowerCase();
		OrderStatus result = cache.get(key);
		if (result == null)
		{
			OrderStatus newValue = new OrderStatus(code);
			OrderStatus previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
