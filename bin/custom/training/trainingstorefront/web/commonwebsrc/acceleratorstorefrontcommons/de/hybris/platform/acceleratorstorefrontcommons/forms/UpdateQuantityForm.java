/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * Form for validating update field on cart page.
 * 
 */
public class UpdateQuantityForm
{
	@NotNull(message = "{basket.error.quantity.notNull}")
	@Min(value = 0, message = "{basket.error.quantity.invalid}")
	@Digits(fraction = 0, integer = 10, message = "{basket.error.quantity.invalid}")
	private Long quantity;

	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Long getQuantity()
	{
		return quantity;
	}
}
