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
 * Generated enum RefundReason declared at extension basecommerce.
 */
@SuppressWarnings("PMD")
public class RefundReason implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RefundReason";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "RefundReason";
	private static final ConcurrentMap<String, RefundReason> cache = new ConcurrentHashMap<String, RefundReason>();
	/**
	* Generated enum value for RefundReason.DamagedInTransit declared at extension basecommerce.
	*/
	public static final RefundReason DAMAGEDINTRANSIT = valueOf("DamagedInTransit");
	
	/**
	* Generated enum value for RefundReason.LateDelivery declared at extension basecommerce.
	*/
	public static final RefundReason LATEDELIVERY = valueOf("LateDelivery");
	
	/**
	* Generated enum value for RefundReason.PriceMatch declared at extension basecommerce.
	*/
	public static final RefundReason PRICEMATCH = valueOf("PriceMatch");
	
	/**
	* Generated enum value for RefundReason.LostInTransit declared at extension basecommerce.
	*/
	public static final RefundReason LOSTINTRANSIT = valueOf("LostInTransit");
	
	/**
	* Generated enum value for RefundReason.ManufacturingFault declared at extension basecommerce.
	*/
	public static final RefundReason MANUFACTURINGFAULT = valueOf("ManufacturingFault");
	
	/**
	* Generated enum value for RefundReason.WrongDescription declared at extension basecommerce.
	*/
	public static final RefundReason WRONGDESCRIPTION = valueOf("WrongDescription");
	
	/**
	* Generated enum value for RefundReason.MissedLinkDeal declared at extension basecommerce.
	*/
	public static final RefundReason MISSEDLINKDEAL = valueOf("MissedLinkDeal");
	
	/**
	* Generated enum value for RefundReason.MispickWrongItemDelivered declared at extension basecommerce.
	*/
	public static final RefundReason MISPICKWRONGITEMDELIVERED = valueOf("MispickWrongItemDelivered");
	
	/**
	* Generated enum value for RefundReason.MispickItemMissing declared at extension basecommerce.
	*/
	public static final RefundReason MISPICKITEMMISSING = valueOf("MispickItemMissing");
	
	/**
	* Generated enum value for RefundReason.SiteError declared at extension basecommerce.
	*/
	public static final RefundReason SITEERROR = valueOf("SiteError");
	
	/**
	* Generated enum value for RefundReason.GoodWill declared at extension basecommerce.
	*/
	public static final RefundReason GOODWILL = valueOf("GoodWill");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private RefundReason(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>RefundReason
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
	 * Returns a hash code for this <code>RefundReason</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>RefundReason</code> object.
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
	 * Returns a <tt>RefundReason</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>RefundReason</tt> instance representing <tt>value</tt>. 
	 */
	public static RefundReason valueOf(final String code)
	{
		final String key = code.toLowerCase();
		RefundReason result = cache.get(key);
		if (result == null)
		{
			RefundReason newValue = new RefundReason(code);
			RefundReason previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
