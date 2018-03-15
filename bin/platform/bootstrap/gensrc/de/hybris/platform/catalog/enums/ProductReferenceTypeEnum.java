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
package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ProductReferenceTypeEnum declared at extension catalog.
 */
@SuppressWarnings("PMD")
public enum ProductReferenceTypeEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for ProductReferenceTypeEnum.ACCESSORIES declared at extension catalog.
	 */
	ACCESSORIES("ACCESSORIES"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.BASE_PRODUCT declared at extension catalog.
	 */
	BASE_PRODUCT("BASE_PRODUCT"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.CONSISTS_OF declared at extension catalog.
	 */
	CONSISTS_OF("CONSISTS_OF"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.DIFF_ORDERUNIT declared at extension catalog.
	 */
	DIFF_ORDERUNIT("DIFF_ORDERUNIT"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.FOLLOWUP declared at extension catalog.
	 */
	FOLLOWUP("FOLLOWUP"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.MANDATORY declared at extension catalog.
	 */
	MANDATORY("MANDATORY"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.SIMILAR declared at extension catalog.
	 */
	SIMILAR("SIMILAR"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.SELECT declared at extension catalog.
	 */
	SELECT("SELECT"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.SPAREPART declared at extension catalog.
	 */
	SPAREPART("SPAREPART"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.OTHERS declared at extension catalog.
	 */
	OTHERS("OTHERS"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.UPSELLING declared at extension catalog.
	 */
	UPSELLING("UPSELLING"),
	/**
	 * Generated enum value for ProductReferenceTypeEnum.CROSSELLING declared at extension catalog.
	 */
	CROSSELLING("CROSSELLING");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProductReferenceTypeEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ProductReferenceTypeEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ProductReferenceTypeEnum(final String code)
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
