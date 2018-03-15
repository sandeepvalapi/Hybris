/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
 * ----------------------------------------------------------------
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
 */
package de.hybris.platform.personalizationintegration.mapping;

import java.io.Serializable;
import java.math.BigDecimal;

public  class SegmentMappingData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SegmentMappingData.code</code> property defined at extension <code>personalizationintegration</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>SegmentMappingData.affinity</code> property defined at extension <code>personalizationintegration</code>. */
		
	private BigDecimal affinity;
	
	public SegmentMappingData()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setAffinity(final BigDecimal affinity)
	{
		this.affinity = affinity;
	}

		
	
	public BigDecimal getAffinity() 
	{
		return affinity;
	}
	


}
