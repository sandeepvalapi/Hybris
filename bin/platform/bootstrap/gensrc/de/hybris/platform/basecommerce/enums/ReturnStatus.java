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
package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum ReturnStatus declared at extension basecommerce.
 */
@SuppressWarnings("PMD")
public class ReturnStatus implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ReturnStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ReturnStatus";
	private static final ConcurrentMap<String, ReturnStatus> cache = new ConcurrentHashMap<String, ReturnStatus>();
	/**
	* Generated enum value for ReturnStatus.CANCELED declared at extension basecommerce.
	*/
	public static final ReturnStatus CANCELED = valueOf("CANCELED");
	
	/**
	* Generated enum value for ReturnStatus.WAIT declared at extension basecommerce.
	*/
	public static final ReturnStatus WAIT = valueOf("WAIT");
	
	/**
	* Generated enum value for ReturnStatus.RECEIVED declared at extension basecommerce.
	*/
	public static final ReturnStatus RECEIVED = valueOf("RECEIVED");
	
	/**
	* Generated enum value for ReturnStatus.APPROVAL_PENDING declared at extension basecommerce.
	*/
	public static final ReturnStatus APPROVAL_PENDING = valueOf("APPROVAL_PENDING");
	
	/**
	* Generated enum value for ReturnStatus.APPROVING declared at extension basecommerce.
	*/
	public static final ReturnStatus APPROVING = valueOf("APPROVING");
	
	/**
	* Generated enum value for ReturnStatus.RECEIVING declared at extension basecommerce.
	*/
	public static final ReturnStatus RECEIVING = valueOf("RECEIVING");
	
	/**
	* Generated enum value for ReturnStatus.CANCELLING declared at extension basecommerce.
	*/
	public static final ReturnStatus CANCELLING = valueOf("CANCELLING");
	
	/**
	* Generated enum value for ReturnStatus.PAYMENT_REVERSED declared at extension basecommerce.
	*/
	public static final ReturnStatus PAYMENT_REVERSED = valueOf("PAYMENT_REVERSED");
	
	/**
	* Generated enum value for ReturnStatus.PAYMENT_REVERSAL_FAILED declared at extension basecommerce.
	*/
	public static final ReturnStatus PAYMENT_REVERSAL_FAILED = valueOf("PAYMENT_REVERSAL_FAILED");
	
	/**
	* Generated enum value for ReturnStatus.TAX_REVERSED declared at extension basecommerce.
	*/
	public static final ReturnStatus TAX_REVERSED = valueOf("TAX_REVERSED");
	
	/**
	* Generated enum value for ReturnStatus.TAX_REVERSAL_FAILED declared at extension basecommerce.
	*/
	public static final ReturnStatus TAX_REVERSAL_FAILED = valueOf("TAX_REVERSAL_FAILED");
	
	/**
	* Generated enum value for ReturnStatus.COMPLETED declared at extension basecommerce.
	*/
	public static final ReturnStatus COMPLETED = valueOf("COMPLETED");
	
	/**
	* Generated enum value for ReturnStatus.REVERSING_PAYMENT declared at extension basecommerce.
	*/
	public static final ReturnStatus REVERSING_PAYMENT = valueOf("REVERSING_PAYMENT");
	
	/**
	* Generated enum value for ReturnStatus.REVERSING_TAX declared at extension basecommerce.
	*/
	public static final ReturnStatus REVERSING_TAX = valueOf("REVERSING_TAX");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ReturnStatus(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>ReturnStatus
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
	 * Returns a hash code for this <code>ReturnStatus</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>ReturnStatus</code> object.
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
	 * Returns a <tt>ReturnStatus</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>ReturnStatus</tt> instance representing <tt>value</tt>. 
	 */
	public static ReturnStatus valueOf(final String code)
	{
		final String key = code.toLowerCase();
		ReturnStatus result = cache.get(key);
		if (result == null)
		{
			ReturnStatus newValue = new ReturnStatus(code);
			ReturnStatus previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
