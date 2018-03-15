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
 * Generated enum QuoteState declared at extension core.
 */
@SuppressWarnings("PMD")
public class QuoteState implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "QuoteState";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "QuoteState";
	private static final ConcurrentMap<String, QuoteState> cache = new ConcurrentHashMap<String, QuoteState>();
	/**
	* Generated enum value for QuoteState.BUYER_APPROVED declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_APPROVED = valueOf("BUYER_APPROVED");
	
	/**
	* Generated enum value for QuoteState.CREATED declared at extension core.
	*/
	public static final QuoteState CREATED = valueOf("CREATED");
	
	/**
	* Generated enum value for QuoteState.BUYER_DRAFT declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_DRAFT = valueOf("BUYER_DRAFT");
	
	/**
	* Generated enum value for QuoteState.DRAFT declared at extension core.
	*/
	public static final QuoteState DRAFT = valueOf("DRAFT");
	
	/**
	* Generated enum value for QuoteState.BUYER_SUBMITTED declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_SUBMITTED = valueOf("BUYER_SUBMITTED");
	
	/**
	* Generated enum value for QuoteState.SUBMITTED declared at extension core.
	*/
	public static final QuoteState SUBMITTED = valueOf("SUBMITTED");
	
	/**
	* Generated enum value for QuoteState.BUYER_OFFER declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_OFFER = valueOf("BUYER_OFFER");
	
	/**
	* Generated enum value for QuoteState.OFFER declared at extension core.
	*/
	public static final QuoteState OFFER = valueOf("OFFER");
	
	/**
	* Generated enum value for QuoteState.BUYER_ACCEPTED declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_ACCEPTED = valueOf("BUYER_ACCEPTED");
	
	/**
	* Generated enum value for QuoteState.ORDERED declared at extension core.
	*/
	public static final QuoteState ORDERED = valueOf("ORDERED");
	
	/**
	* Generated enum value for QuoteState.BUYER_REJECTED declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_REJECTED = valueOf("BUYER_REJECTED");
	
	/**
	* Generated enum value for QuoteState.CANCELLED declared at extension core.
	*/
	public static final QuoteState CANCELLED = valueOf("CANCELLED");
	
	/**
	* Generated enum value for QuoteState.BUYER_ORDERED declared at extension commerceservices.
	*/
	public static final QuoteState BUYER_ORDERED = valueOf("BUYER_ORDERED");
	
	/**
	* Generated enum value for QuoteState.EXPIRED declared at extension core.
	*/
	public static final QuoteState EXPIRED = valueOf("EXPIRED");
	
	/**
	* Generated enum value for QuoteState.SELLER_REQUEST declared at extension commerceservices.
	*/
	public static final QuoteState SELLER_REQUEST = valueOf("SELLER_REQUEST");
	
	/**
	* Generated enum value for QuoteState.SELLER_DRAFT declared at extension commerceservices.
	*/
	public static final QuoteState SELLER_DRAFT = valueOf("SELLER_DRAFT");
	
	/**
	* Generated enum value for QuoteState.SELLER_SUBMITTED declared at extension commerceservices.
	*/
	public static final QuoteState SELLER_SUBMITTED = valueOf("SELLER_SUBMITTED");
	
	/**
	* Generated enum value for QuoteState.SELLERAPPROVER_DRAFT declared at extension commerceservices.
	*/
	public static final QuoteState SELLERAPPROVER_DRAFT = valueOf("SELLERAPPROVER_DRAFT");
	
	/**
	* Generated enum value for QuoteState.SELLERAPPROVER_PENDING declared at extension commerceservices.
	*/
	public static final QuoteState SELLERAPPROVER_PENDING = valueOf("SELLERAPPROVER_PENDING");
	
	/**
	* Generated enum value for QuoteState.SELLERAPPROVER_REJECTED declared at extension commerceservices.
	*/
	public static final QuoteState SELLERAPPROVER_REJECTED = valueOf("SELLERAPPROVER_REJECTED");
	
	/**
	* Generated enum value for QuoteState.SELLERAPPROVER_APPROVED declared at extension commerceservices.
	*/
	public static final QuoteState SELLERAPPROVER_APPROVED = valueOf("SELLERAPPROVER_APPROVED");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private QuoteState(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>QuoteState
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
	 * Returns a hash code for this <code>QuoteState</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>QuoteState</code> object.
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
	 * Returns a <tt>QuoteState</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>QuoteState</tt> instance representing <tt>value</tt>. 
	 */
	public static QuoteState valueOf(final String code)
	{
		final String key = code.toLowerCase();
		QuoteState result = cache.get(key);
		if (result == null)
		{
			QuoteState newValue = new QuoteState(code);
			QuoteState previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
