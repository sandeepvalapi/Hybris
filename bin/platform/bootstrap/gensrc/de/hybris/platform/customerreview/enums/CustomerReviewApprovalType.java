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
package de.hybris.platform.customerreview.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CustomerReviewApprovalType declared at extension customerreview.
 * <p/>
 * It represents approval status of the customer review. When it is set to approved, the review is visible to visitors.
 */
@SuppressWarnings("PMD")
public enum CustomerReviewApprovalType implements HybrisEnumValue
{
	/**
	 * Generated enum value for CustomerReviewApprovalType.approved declared at extension customerreview.
	 */
	APPROVED("approved"),
	/**
	 * Generated enum value for CustomerReviewApprovalType.pending declared at extension customerreview.
	 */
	PENDING("pending"),
	/**
	 * Generated enum value for CustomerReviewApprovalType.rejected declared at extension customerreview.
	 */
	REJECTED("rejected");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CustomerReviewApprovalType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CustomerReviewApprovalType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CustomerReviewApprovalType(final String code)
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
