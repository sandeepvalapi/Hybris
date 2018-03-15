/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import java.util.List;

public  class MappingData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>MappingData.segments</code> property defined at extension <code>personalizationintegration</code>. */
		
	private List<SegmentMappingData> segments;
	
	public MappingData()
	{
		// default constructor
	}
	
		
	
	public void setSegments(final List<SegmentMappingData> segments)
	{
		this.segments = segments;
	}

		
	
	public List<SegmentMappingData> getSegments() 
	{
		return segments;
	}
	


}
