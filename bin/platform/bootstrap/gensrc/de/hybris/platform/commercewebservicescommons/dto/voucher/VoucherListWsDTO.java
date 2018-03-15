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
package de.hybris.platform.commercewebservicescommons.dto.voucher;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherWsDTO;
import java.util.List;

public  class VoucherListWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>VoucherListWsDTO.vouchers</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<VoucherWsDTO> vouchers;
	
	public VoucherListWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setVouchers(final List<VoucherWsDTO> vouchers)
	{
		this.vouchers = vouchers;
	}

		
	
	public List<VoucherWsDTO> getVouchers() 
	{
		return vouchers;
	}
	


}
