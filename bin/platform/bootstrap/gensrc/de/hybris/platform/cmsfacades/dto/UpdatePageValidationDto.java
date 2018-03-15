/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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
package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;
import de.hybris.platform.cmsfacades.data.AbstractPageData;

/**
 * @deprecated Deprecated since 6.6
 */
@Deprecated
public  class UpdatePageValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UpdatePageValidationDto.originalUid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String originalUid;

	/** <i>Generated property</i> for <code>UpdatePageValidationDto.page</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractPageData page;
	
	public UpdatePageValidationDto()
	{
		// default constructor
	}
	
		
	
	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}

		
	
	public String getOriginalUid() 
	{
		return originalUid;
	}
	
		
	
	public void setPage(final AbstractPageData page)
	{
		this.page = page;
	}

		
	
	public AbstractPageData getPage() 
	{
		return page;
	}
	


}
