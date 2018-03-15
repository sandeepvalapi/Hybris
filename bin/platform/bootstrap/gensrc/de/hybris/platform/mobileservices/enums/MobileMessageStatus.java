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
 * Generated enum MobileMessageStatus declared at extension mobileservices.
 */
@SuppressWarnings("PMD")
public class MobileMessageStatus implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MobileMessageStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "MobileMessageStatus";
	private static final ConcurrentMap<String, MobileMessageStatus> cache = new ConcurrentHashMap<String, MobileMessageStatus>();
	/**
	* Generated enum value for MobileMessageStatus.RECEIVED declared at extension mobileservices.
	*/
	public static final MobileMessageStatus RECEIVED = valueOf("RECEIVED");
	
	/**
	* Generated enum value for MobileMessageStatus.PROCESSING declared at extension mobileservices.
	*/
	public static final MobileMessageStatus PROCESSING = valueOf("PROCESSING");
	
	/**
	* Generated enum value for MobileMessageStatus.PROCCESED declared at extension mobileservices.
	*/
	public static final MobileMessageStatus PROCCESED = valueOf("PROCCESED");
	
	/**
	* Generated enum value for MobileMessageStatus.VERIFYING declared at extension mobileservices.
	*/
	public static final MobileMessageStatus VERIFYING = valueOf("VERIFYING");
	
	/**
	* Generated enum value for MobileMessageStatus.SENT declared at extension mobileservices.
	*/
	public static final MobileMessageStatus SENT = valueOf("SENT");
	
	/**
	* Generated enum value for MobileMessageStatus.DONE declared at extension mobileservices.
	*/
	public static final MobileMessageStatus DONE = valueOf("DONE");
	
	/**
	* Generated enum value for MobileMessageStatus.DISCARDED declared at extension mobileservices.
	*/
	public static final MobileMessageStatus DISCARDED = valueOf("DISCARDED");
	
	/**
	* Generated enum value for MobileMessageStatus.SCHEDULED declared at extension mobileservices.
	*/
	public static final MobileMessageStatus SCHEDULED = valueOf("SCHEDULED");
	
	/**
	* Generated enum value for MobileMessageStatus.ERROR declared at extension mobileservices.
	*/
	public static final MobileMessageStatus ERROR = valueOf("ERROR");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private MobileMessageStatus(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>MobileMessageStatus
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
	 * Returns a hash code for this <code>MobileMessageStatus</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>MobileMessageStatus</code> object.
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
	 * Returns a <tt>MobileMessageStatus</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>MobileMessageStatus</tt> instance representing <tt>value</tt>. 
	 */
	public static MobileMessageStatus valueOf(final String code)
	{
		final String key = code.toLowerCase();
		MobileMessageStatus result = cache.get(key);
		if (result == null)
		{
			MobileMessageStatus newValue = new MobileMessageStatus(code);
			MobileMessageStatus previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
