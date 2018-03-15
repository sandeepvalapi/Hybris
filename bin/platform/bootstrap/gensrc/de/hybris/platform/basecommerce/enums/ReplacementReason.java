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
 * Generated enum ReplacementReason declared at extension basecommerce.
 */
@SuppressWarnings("PMD")
public class ReplacementReason implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ReplacementReason";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ReplacementReason";
	private static final ConcurrentMap<String, ReplacementReason> cache = new ConcurrentHashMap<String, ReplacementReason>();
	/**
	* Generated enum value for ReplacementReason.ReturnInTime declared at extension basecommerce.
	*/
	public static final ReplacementReason RETURNINTIME = valueOf("ReturnInTime");
	
	/**
	* Generated enum value for ReplacementReason.DamagedInTransit declared at extension basecommerce.
	*/
	public static final ReplacementReason DAMAGEDINTRANSIT = valueOf("DamagedInTransit");
	
	/**
	* Generated enum value for ReplacementReason.LateDelivery declared at extension basecommerce.
	*/
	public static final ReplacementReason LATEDELIVERY = valueOf("LateDelivery");
	
	/**
	* Generated enum value for ReplacementReason.ManufacturingFault declared at extension basecommerce.
	*/
	public static final ReplacementReason MANUFACTURINGFAULT = valueOf("ManufacturingFault");
	
	/**
	* Generated enum value for ReplacementReason.WrongDescription declared at extension basecommerce.
	*/
	public static final ReplacementReason WRONGDESCRIPTION = valueOf("WrongDescription");
	
	/**
	* Generated enum value for ReplacementReason.LostInTransit declared at extension basecommerce.
	*/
	public static final ReplacementReason LOSTINTRANSIT = valueOf("LostInTransit");
	
	/**
	* Generated enum value for ReplacementReason.MispickWrongItemDelivered declared at extension basecommerce.
	*/
	public static final ReplacementReason MISPICKWRONGITEMDELIVERED = valueOf("MispickWrongItemDelivered");
	
	/**
	* Generated enum value for ReplacementReason.MispickItemMissing declared at extension basecommerce.
	*/
	public static final ReplacementReason MISPICKITEMMISSING = valueOf("MispickItemMissing");
	
	/**
	* Generated enum value for ReplacementReason.Refused declared at extension basecommerce.
	*/
	public static final ReplacementReason REFUSED = valueOf("Refused");
	
	/**
	* Generated enum value for ReplacementReason.GoodWill declared at extension basecommerce.
	*/
	public static final ReplacementReason GOODWILL = valueOf("GoodWill");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ReplacementReason(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>ReplacementReason
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
	 * Returns a hash code for this <code>ReplacementReason</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>ReplacementReason</code> object.
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
	 * Returns a <tt>ReplacementReason</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>ReplacementReason</tt> instance representing <tt>value</tt>. 
	 */
	public static ReplacementReason valueOf(final String code)
	{
		final String key = code.toLowerCase();
		ReplacementReason result = cache.get(key);
		if (result == null)
		{
			ReplacementReason newValue = new ReplacementReason(code);
			ReplacementReason previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
