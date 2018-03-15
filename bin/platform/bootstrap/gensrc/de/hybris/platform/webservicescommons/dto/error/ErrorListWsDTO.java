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
package de.hybris.platform.webservicescommons.dto.error;

import java.io.Serializable;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * List of errors
 */
@ApiModel(value="errorList", description="List of errors")
public  class ErrorListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ErrorListWsDTO.errors</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="errors") 	
	private List<ErrorWsDTO> errors;
	
	public ErrorListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setErrors(final List<ErrorWsDTO> errors)
	{
		this.errors = errors;
	}

		
	
	public List<ErrorWsDTO> getErrors() 
	{
		return errors;
	}
	


}
