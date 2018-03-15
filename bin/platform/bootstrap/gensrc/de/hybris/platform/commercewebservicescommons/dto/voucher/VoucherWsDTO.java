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
package de.hybris.platform.commercewebservicescommons.dto.voucher;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;

public  class VoucherWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VoucherWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>VoucherWsDTO.voucherCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String voucherCode;

	/** <i>Generated property</i> for <code>VoucherWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VoucherWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>VoucherWsDTO.value</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double value;

	/** <i>Generated property</i> for <code>VoucherWsDTO.valueFormatted</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String valueFormatted;

	/** <i>Generated property</i> for <code>VoucherWsDTO.valueString</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String valueString;

	/** <i>Generated property</i> for <code>VoucherWsDTO.freeShipping</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean freeShipping;

	/** <i>Generated property</i> for <code>VoucherWsDTO.currency</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private CurrencyWsDTO currency;

	/** <i>Generated property</i> for <code>VoucherWsDTO.appliedValue</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO appliedValue;
	
	public VoucherWsDTO()
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
	
		
	
	public void setVoucherCode(final String voucherCode)
	{
		this.voucherCode = voucherCode;
	}

		
	
	public String getVoucherCode() 
	{
		return voucherCode;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setValue(final Double value)
	{
		this.value = value;
	}

		
	
	public Double getValue() 
	{
		return value;
	}
	
		
	
	public void setValueFormatted(final String valueFormatted)
	{
		this.valueFormatted = valueFormatted;
	}

		
	
	public String getValueFormatted() 
	{
		return valueFormatted;
	}
	
		
	
	public void setValueString(final String valueString)
	{
		this.valueString = valueString;
	}

		
	
	public String getValueString() 
	{
		return valueString;
	}
	
		
	
	public void setFreeShipping(final Boolean freeShipping)
	{
		this.freeShipping = freeShipping;
	}

		
	
	public Boolean getFreeShipping() 
	{
		return freeShipping;
	}
	
		
	
	public void setCurrency(final CurrencyWsDTO currency)
	{
		this.currency = currency;
	}

		
	
	public CurrencyWsDTO getCurrency() 
	{
		return currency;
	}
	
		
	
	public void setAppliedValue(final PriceWsDTO appliedValue)
	{
		this.appliedValue = appliedValue;
	}

		
	
	public PriceWsDTO getAppliedValue() 
	{
		return appliedValue;
	}
	


}
