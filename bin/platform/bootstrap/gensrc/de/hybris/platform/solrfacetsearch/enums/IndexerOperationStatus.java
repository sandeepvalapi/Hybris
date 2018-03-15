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

/**
 * Generated enum IndexerOperationStatus declared at extension solrfacetsearch.
 */
@SuppressWarnings("PMD")
public enum IndexerOperationStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for IndexerOperationStatus.RUNNING declared at extension solrfacetsearch.
	 */
	RUNNING("RUNNING"),
	/**
	 * Generated enum value for IndexerOperationStatus.ABORTED declared at extension solrfacetsearch.
	 */
	ABORTED("ABORTED"),
	/**
	 * Generated enum value for IndexerOperationStatus.SUCCESS declared at extension solrfacetsearch.
	 */
	SUCCESS("SUCCESS"),
	/**
	 * Generated enum value for IndexerOperationStatus.FAILED declared at extension solrfacetsearch.
	 */
	FAILED("FAILED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "IndexerOperationStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "IndexerOperationStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private IndexerOperationStatus(final String code)
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
