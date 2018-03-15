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

/**
 * Generated enum TypeOfCollectionEnum declared at extension core.
 */
@SuppressWarnings("PMD")
public enum TypeOfCollectionEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for TypeOfCollectionEnum.collection declared at extension core.
	 */
	COLLECTION("collection"),
	/**
	 * Generated enum value for TypeOfCollectionEnum.set declared at extension core.
	 */
	SET("set"),
	/**
	 * Generated enum value for TypeOfCollectionEnum.list declared at extension core.
	 */
	LIST("list");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "TypeOfCollectionEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "TypeOfCollectionEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private TypeOfCollectionEnum(final String code)
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
