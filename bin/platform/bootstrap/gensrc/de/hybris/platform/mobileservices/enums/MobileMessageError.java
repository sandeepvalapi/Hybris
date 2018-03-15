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
package de.hybris.platform.mobileservices.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum MobileMessageError declared at extension mobileservices.
 */
@SuppressWarnings("PMD")
public class MobileMessageError implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MobileMessageError";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "MobileMessageError";
	private static final ConcurrentMap<String, MobileMessageError> cache = new ConcurrentHashMap<String, MobileMessageError>();
	/**
	* Generated enum value for MobileMessageError.INVALID_PHONE_NUMBER declared at extension mobileservices.
	*/
	public static final MobileMessageError INVALID_PHONE_NUMBER = valueOf("INVALID_PHONE_NUMBER");
	
	/**
	* Generated enum value for MobileMessageError.MAX_SIZE_EXCEEDED declared at extension mobileservices.
	*/
	public static final MobileMessageError MAX_SIZE_EXCEEDED = valueOf("MAX_SIZE_EXCEEDED");
	
	/**
	* Generated enum value for MobileMessageError.UNSUBSCRIBED declared at extension mobileservices.
	*/
	public static final MobileMessageError UNSUBSCRIBED = valueOf("UNSUBSCRIBED");
	
	/**
	* Generated enum value for MobileMessageError.NOROUTE declared at extension mobileservices.
	*/
	public static final MobileMessageError NOROUTE = valueOf("NOROUTE");
	
	/**
	* Generated enum value for MobileMessageError.ACTIONMISSING declared at extension mobileservices.
	*/
	public static final MobileMessageError ACTIONMISSING = valueOf("ACTIONMISSING");
	
	/**
	* Generated enum value for MobileMessageError.LINKNOTSUPPORTED declared at extension mobileservices.
	*/
	public static final MobileMessageError LINKNOTSUPPORTED = valueOf("LINKNOTSUPPORTED");
	
	/**
	* Generated enum value for MobileMessageError.FILTERED declared at extension mobileservices.
	*/
	public static final MobileMessageError FILTERED = valueOf("FILTERED");
	
	/**
	* Generated enum value for MobileMessageError.WRONGCONFIG declared at extension mobileservices.
	*/
	public static final MobileMessageError WRONGCONFIG = valueOf("WRONGCONFIG");
	
	/**
	* Generated enum value for MobileMessageError.UNKNOWN declared at extension mobileservices.
	*/
	public static final MobileMessageError UNKNOWN = valueOf("UNKNOWN");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private MobileMessageError(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>MobileMessageError
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
	 * Returns a hash code for this <code>MobileMessageError</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>MobileMessageError</code> object.
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
	 * Returns a <tt>MobileMessageError</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>MobileMessageError</tt> instance representing <tt>value</tt>. 
	 */
	public static MobileMessageError valueOf(final String code)
	{
		final String key = code.toLowerCase();
		MobileMessageError result = cache.get(key);
		if (result == null)
		{
			MobileMessageError newValue = new MobileMessageError(code);
			MobileMessageError previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
