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
package de.hybris.platform.processing.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum DistributedProcessState declared at extension processing.
 */
@SuppressWarnings("PMD")
public enum DistributedProcessState implements HybrisEnumValue
{
	/**
	 * Generated enum value for DistributedProcessState.CREATED declared at extension processing.
	 */
	CREATED("CREATED"),
	/**
	 * Generated enum value for DistributedProcessState.INITIALIZING declared at extension processing.
	 */
	INITIALIZING("INITIALIZING"),
	/**
	 * Generated enum value for DistributedProcessState.SCHEDULING_EXECUTION declared at extension processing.
	 */
	SCHEDULING_EXECUTION("SCHEDULING_EXECUTION"),
	/**
	 * Generated enum value for DistributedProcessState.WAITING_FOR_EXECUTION declared at extension processing.
	 */
	WAITING_FOR_EXECUTION("WAITING_FOR_EXECUTION"),
	/**
	 * Generated enum value for DistributedProcessState.SUCCEEDED declared at extension processing.
	 */
	SUCCEEDED("SUCCEEDED"),
	/**
	 * Generated enum value for DistributedProcessState.FAILED declared at extension processing.
	 */
	FAILED("FAILED"),
	/**
	 * Generated enum value for DistributedProcessState.STOPPED declared at extension processing.
	 */
	STOPPED("STOPPED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DistributedProcessState";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "DistributedProcessState";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private DistributedProcessState(final String code)
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
