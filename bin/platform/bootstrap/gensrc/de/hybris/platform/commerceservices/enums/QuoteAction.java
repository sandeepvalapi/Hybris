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
package de.hybris.platform.commerceservices.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum QuoteAction declared at extension commerceservices.
 */
@SuppressWarnings("PMD")
public class QuoteAction implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "QuoteAction";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "QuoteAction";
	private static final ConcurrentMap<String, QuoteAction> cache = new ConcurrentHashMap<String, QuoteAction>();
	/**
	* Generated enum value for QuoteAction.CREATE declared at extension commerceservices.
	*/
	public static final QuoteAction CREATE = valueOf("CREATE");
	
	/**
	* Generated enum value for QuoteAction.VIEW declared at extension commerceservices.
	*/
	public static final QuoteAction VIEW = valueOf("VIEW");
	
	/**
	* Generated enum value for QuoteAction.SUBMIT declared at extension commerceservices.
	*/
	public static final QuoteAction SUBMIT = valueOf("SUBMIT");
	
	/**
	* Generated enum value for QuoteAction.SAVE declared at extension commerceservices.
	*/
	public static final QuoteAction SAVE = valueOf("SAVE");
	
	/**
	* Generated enum value for QuoteAction.EDIT declared at extension commerceservices.
	*/
	public static final QuoteAction EDIT = valueOf("EDIT");
	
	/**
	* Generated enum value for QuoteAction.DISCOUNT declared at extension commerceservices.
	*/
	public static final QuoteAction DISCOUNT = valueOf("DISCOUNT");
	
	/**
	* Generated enum value for QuoteAction.CANCEL declared at extension commerceservices.
	*/
	public static final QuoteAction CANCEL = valueOf("CANCEL");
	
	/**
	* Generated enum value for QuoteAction.CHECKOUT declared at extension commerceservices.
	*/
	public static final QuoteAction CHECKOUT = valueOf("CHECKOUT");
	
	/**
	* Generated enum value for QuoteAction.ORDER declared at extension commerceservices.
	*/
	public static final QuoteAction ORDER = valueOf("ORDER");
	
	/**
	* Generated enum value for QuoteAction.APPROVE declared at extension commerceservices.
	*/
	public static final QuoteAction APPROVE = valueOf("APPROVE");
	
	/**
	* Generated enum value for QuoteAction.REJECT declared at extension commerceservices.
	*/
	public static final QuoteAction REJECT = valueOf("REJECT");
	
	/**
	* Generated enum value for QuoteAction.EXPIRED declared at extension commerceservices.
	*/
	public static final QuoteAction EXPIRED = valueOf("EXPIRED");
	
	/**
	* Generated enum value for QuoteAction.REQUOTE declared at extension commerceservices.
	*/
	public static final QuoteAction REQUOTE = valueOf("REQUOTE");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private QuoteAction(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>QuoteAction
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
	 * Returns a hash code for this <code>QuoteAction</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>QuoteAction</code> object.
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
	 * Returns a <tt>QuoteAction</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>QuoteAction</tt> instance representing <tt>value</tt>. 
	 */
	public static QuoteAction valueOf(final String code)
	{
		final String key = code.toLowerCase();
		QuoteAction result = cache.get(key);
		if (result == null)
		{
			QuoteAction newValue = new QuoteAction(code);
			QuoteAction previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
