/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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
package de.hybris.platform.commercewebservicescommons.dto.user;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import java.util.List;

public  class AddressValidationWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AddressValidationWsDTO.errors</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private ErrorListWsDTO errors;

	/** <i>Generated property</i> for <code>AddressValidationWsDTO.decision</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String decision;

	/** <i>Generated property</i> for <code>AddressValidationWsDTO.suggestedAddresses</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<AddressWsDTO> suggestedAddresses;
	
	public AddressValidationWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setErrors(final ErrorListWsDTO errors)
	{
		this.errors = errors;
	}

		
	
	public ErrorListWsDTO getErrors() 
	{
		return errors;
	}
	
		
	
	public void setDecision(final String decision)
	{
		this.decision = decision;
	}

		
	
	public String getDecision() 
	{
		return decision;
	}
	
		
	
	public void setSuggestedAddresses(final List<AddressWsDTO> suggestedAddresses)
	{
		this.suggestedAddresses = suggestedAddresses;
	}

		
	
	public List<AddressWsDTO> getSuggestedAddresses() 
	{
		return suggestedAddresses;
	}
	


}
