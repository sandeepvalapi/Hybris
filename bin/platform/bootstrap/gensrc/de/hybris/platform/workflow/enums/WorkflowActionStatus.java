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
package de.hybris.platform.workflow.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum WorkflowActionStatus declared at extension workflow.
 */
@SuppressWarnings("PMD")
public enum WorkflowActionStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for WorkflowActionStatus.pending declared at extension workflow.
	 */
	PENDING("pending"),
	/**
	 * Generated enum value for WorkflowActionStatus.in_progress declared at extension workflow.
	 */
	IN_PROGRESS("in_progress"),
	/**
	 * Generated enum value for WorkflowActionStatus.paused declared at extension workflow.
	 */
	PAUSED("paused"),
	/**
	 * Generated enum value for WorkflowActionStatus.completed declared at extension workflow.
	 */
	COMPLETED("completed"),
	/**
	 * Generated enum value for WorkflowActionStatus.disabled declared at extension workflow.
	 */
	DISABLED("disabled"),
	/**
	 * Generated enum value for WorkflowActionStatus.ended_through_end_of_workflow declared at extension workflow.
	 */
	ENDED_THROUGH_END_OF_WORKFLOW("ended_through_end_of_workflow"),
	/**
	 * Generated enum value for WorkflowActionStatus.terminated declared at extension workflow.
	 */
	TERMINATED("terminated");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowActionStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "WorkflowActionStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private WorkflowActionStatus(final String code)
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
