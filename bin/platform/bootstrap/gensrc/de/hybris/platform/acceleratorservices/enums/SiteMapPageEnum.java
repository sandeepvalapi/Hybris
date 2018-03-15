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
 * Generated enum SiteMapPageEnum declared at extension acceleratorservices.
 */
@SuppressWarnings("PMD")
public class SiteMapPageEnum implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SiteMapPageEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SiteMapPageEnum";
	private static final ConcurrentMap<String, SiteMapPageEnum> cache = new ConcurrentHashMap<String, SiteMapPageEnum>();
	/**
	* Generated enum value for SiteMapPageEnum.Homepage declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum HOMEPAGE = valueOf("Homepage");
	
	/**
	* Generated enum value for SiteMapPageEnum.Product declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum PRODUCT = valueOf("Product");
	
	/**
	* Generated enum value for SiteMapPageEnum.Category declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum CATEGORY = valueOf("Category");
	
	/**
	* Generated enum value for SiteMapPageEnum.CategoryLanding declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum CATEGORYLANDING = valueOf("CategoryLanding");
	
	/**
	* Generated enum value for SiteMapPageEnum.Store declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum STORE = valueOf("Store");
	
	/**
	* Generated enum value for SiteMapPageEnum.Content declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum CONTENT = valueOf("Content");
	
	/**
	* Generated enum value for SiteMapPageEnum.Custom declared at extension acceleratorservices.
	*/
	public static final SiteMapPageEnum CUSTOM = valueOf("Custom");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SiteMapPageEnum(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>SiteMapPageEnum
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
	 * Returns a hash code for this <code>SiteMapPageEnum</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>SiteMapPageEnum</code> object.
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
	 * Returns a <tt>SiteMapPageEnum</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>SiteMapPageEnum</tt> instance representing <tt>value</tt>. 
	 */
	public static SiteMapPageEnum valueOf(final String code)
	{
		final String key = code.toLowerCase();
		SiteMapPageEnum result = cache.get(key);
		if (result == null)
		{
			SiteMapPageEnum newValue = new SiteMapPageEnum(code);
			SiteMapPageEnum previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
