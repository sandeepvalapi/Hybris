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
package de.hybris.platform.processengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ProcessState declared at extension processing.
 */
@SuppressWarnings("PMD")
public enum ProcessState implements HybrisEnumValue
{
	/**
	 * Generated enum value for ProcessState.CREATED declared at extension processing.
	 */
	CREATED("CREATED"),
	/**
	 * Generated enum value for ProcessState.RUNNING declared at extension processing.
	 */
	RUNNING("RUNNING"),
	/**
	 * Generated enum value for ProcessState.WAITING declared at extension processing.
	 */
	WAITING("WAITING"),
	/**
	 * Generated enum value for ProcessState.SUCCEEDED declared at extension processing.
	 */
	SUCCEEDED("SUCCEEDED"),
	/**
	 * Generated enum value for ProcessState.FAILED declared at extension processing.
	 */
	FAILED("FAILED"),
	/**
	 * Generated enum value for ProcessState.ERROR declared at extension processing.
	 */
	ERROR("ERROR");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProcessState";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ProcessState";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ProcessState(final String code)
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
