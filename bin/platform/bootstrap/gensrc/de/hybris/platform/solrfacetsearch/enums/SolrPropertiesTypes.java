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
package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum SolrPropertiesTypes declared at extension solrfacetsearch.
 */
@SuppressWarnings("PMD")
public class SolrPropertiesTypes implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrPropertiesTypes";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrPropertiesTypes";
	private static final ConcurrentMap<String, SolrPropertiesTypes> cache = new ConcurrentHashMap<String, SolrPropertiesTypes>();
	/**
	* Generated enum value for SolrPropertiesTypes.boolean declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes BOOLEAN = valueOf("boolean");
	
	/**
	* Generated enum value for SolrPropertiesTypes.int declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes INT = valueOf("int");
	
	/**
	* Generated enum value for SolrPropertiesTypes.string declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes STRING = valueOf("string");
	
	/**
	* Generated enum value for SolrPropertiesTypes.sortabletext declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes SORTABLETEXT = valueOf("sortabletext");
	
	/**
	* Generated enum value for SolrPropertiesTypes.text declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes TEXT = valueOf("text");
	
	/**
	* Generated enum value for SolrPropertiesTypes.float declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes FLOAT = valueOf("float");
	
	/**
	* Generated enum value for SolrPropertiesTypes.double declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes DOUBLE = valueOf("double");
	
	/**
	* Generated enum value for SolrPropertiesTypes.date declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes DATE = valueOf("date");
	
	/**
	* Generated enum value for SolrPropertiesTypes.long declared at extension solrfacetsearch.
	*/
	public static final SolrPropertiesTypes LONG = valueOf("long");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrPropertiesTypes(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>SolrPropertiesTypes
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
	 * Returns a hash code for this <code>SolrPropertiesTypes</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>SolrPropertiesTypes</code> object.
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
	 * Returns a <tt>SolrPropertiesTypes</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>SolrPropertiesTypes</tt> instance representing <tt>value</tt>. 
	 */
	public static SolrPropertiesTypes valueOf(final String code)
	{
		final String key = code.toLowerCase();
		SolrPropertiesTypes result = cache.get(key);
		if (result == null)
		{
			SolrPropertiesTypes newValue = new SolrPropertiesTypes(code);
			SolrPropertiesTypes previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
