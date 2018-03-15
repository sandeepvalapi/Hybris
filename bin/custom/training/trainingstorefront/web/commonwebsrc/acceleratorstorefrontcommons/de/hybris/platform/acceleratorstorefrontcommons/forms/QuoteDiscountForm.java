/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import javax.validation.constraints.NotNull;


/**
 * Form for handling discount related fields for quote.
 */
public class QuoteDiscountForm
{
	@NotNull(message = "{text.quote.discount.rate.null.error}")
	private Double discountRate;
	@NotNull(message = "{text.quote.discount.type.null.error}")
	private String discountType;

	public String getDiscountType()
	{
		return discountType;
	}

	public void setDiscountType(final String discountType)
	{
		this.discountType = discountType;
	}

	public Double getDiscountRate()
	{
		return discountRate;
	}

	public void setDiscountRate(final Double discountRate)
	{
		this.discountRate = discountRate;
	}
}
