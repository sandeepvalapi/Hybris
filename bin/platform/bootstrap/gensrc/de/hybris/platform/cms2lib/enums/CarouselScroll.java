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
package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CarouselScroll declared at extension cms2lib.
 */
@SuppressWarnings("PMD")
public enum CarouselScroll implements HybrisEnumValue
{
	/**
	 * Generated enum value for CarouselScroll.one declared at extension cms2lib.
	 */
	ONE("one"),
	/**
	 * Generated enum value for CarouselScroll.allVisible declared at extension cms2lib.
	 */
	ALLVISIBLE("allVisible");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CarouselScroll";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CarouselScroll";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CarouselScroll(final String code)
	{
		this.code = code.intern();
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
	
}
