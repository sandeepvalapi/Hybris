/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
