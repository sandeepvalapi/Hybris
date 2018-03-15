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
package de.hybris.platform.acceleratorservices.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum SiteMapChangeFrequencyEnum declared at extension acceleratorservices.
 */
@SuppressWarnings("PMD")
public class SiteMapChangeFrequencyEnum implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SiteMapChangeFrequencyEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SiteMapChangeFrequencyEnum";
	private static final ConcurrentMap<String, SiteMapChangeFrequencyEnum> cache = new ConcurrentHashMap<String, SiteMapChangeFrequencyEnum>();
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.always declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum ALWAYS = valueOf("always");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.hourly declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum HOURLY = valueOf("hourly");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.daily declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum DAILY = valueOf("daily");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.weekly declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum WEEKLY = valueOf("weekly");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.monthly declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum MONTHLY = valueOf("monthly");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.yearly declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum YEARLY = valueOf("yearly");
	
	/**
	* Generated enum value for SiteMapChangeFrequencyEnum.never declared at extension acceleratorservices.
	*/
	public static final SiteMapChangeFrequencyEnum NEVER = valueOf("never");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SiteMapChangeFrequencyEnum(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>SiteMapChangeFrequencyEnum
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
	 * Returns a hash code for this <code>SiteMapChangeFrequencyEnum</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>SiteMapChangeFrequencyEnum</code> object.
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
	 * Returns a <tt>SiteMapChangeFrequencyEnum</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>SiteMapChangeFrequencyEnum</tt> instance representing <tt>value</tt>. 
	 */
	public static SiteMapChangeFrequencyEnum valueOf(final String code)
	{
		final String key = code.toLowerCase();
		SiteMapChangeFrequencyEnum result = cache.get(key);
		if (result == null)
		{
			SiteMapChangeFrequencyEnum newValue = new SiteMapChangeFrequencyEnum(code);
			SiteMapChangeFrequencyEnum previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
