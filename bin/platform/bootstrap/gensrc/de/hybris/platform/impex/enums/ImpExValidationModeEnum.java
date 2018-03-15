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
package de.hybris.platform.impex.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ImpExValidationModeEnum declared at extension impex.
 */
@SuppressWarnings("PMD")
public enum ImpExValidationModeEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for ImpExValidationModeEnum.import_strict declared at extension impex.
	 */
	IMPORT_STRICT("import_strict"),
	/**
	 * Generated enum value for ImpExValidationModeEnum.import_relaxed declared at extension impex.
	 */
	IMPORT_RELAXED("import_relaxed"),
	/**
	 * Generated enum value for ImpExValidationModeEnum.export_only declared at extension impex.
	 */
	EXPORT_ONLY("export_only"),
	/**
	 * Generated enum value for ImpExValidationModeEnum.export_reimport_strict declared at extension impex.
	 */
	EXPORT_REIMPORT_STRICT("export_reimport_strict"),
	/**
	 * Generated enum value for ImpExValidationModeEnum.export_reimport_relaxed declared at extension impex.
	 */
	EXPORT_REIMPORT_RELAXED("export_reimport_relaxed");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ImpExValidationModeEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ImpExValidationModeEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ImpExValidationModeEnum(final String code)
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
