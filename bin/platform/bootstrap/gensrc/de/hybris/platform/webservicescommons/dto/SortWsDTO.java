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
package de.hybris.platform.webservicescommons.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Sort option
 */
@ApiModel(value="sort", description="Sort option")
public  class SortWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SortWsDTO.code</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="code") 	
	private String code;

	/** <i>Generated property</i> for <code>SortWsDTO.asc</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="asc") 	
	private boolean asc;
	
	public SortWsDTO()
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
	
		
	
	public void setAsc(final boolean asc)
	{
		this.asc = asc;
	}

		
	
	public boolean isAsc() 
	{
		return asc;
	}
	


}
