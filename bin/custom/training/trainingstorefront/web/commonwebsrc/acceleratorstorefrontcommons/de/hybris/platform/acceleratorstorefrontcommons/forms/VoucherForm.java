/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class VoucherForm implements Serializable
{
	private static final long serialVersionUID = 3734178553292263688L;

	@NotNull(message = "{text.voucher.apply.invalid.error}")
	@Size(min = 1, max = 255, message = "{text.voucher.apply.invalid.error}")
	private String voucherCode;

	public String getVoucherCode()
	{
		return voucherCode;
	}

	public void setVoucherCode(final String voucherCode)
	{
		this.voucherCode = XSSFilterUtil.filter(voucherCode);
	}
}
