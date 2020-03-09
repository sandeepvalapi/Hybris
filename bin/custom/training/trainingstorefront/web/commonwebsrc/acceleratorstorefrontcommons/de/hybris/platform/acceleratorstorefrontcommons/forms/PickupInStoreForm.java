/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Form for validating product quantity on pickup in store page.
 */
public class PickupInStoreForm
{
	private long hiddenPickupQty;

	@NotNull(message = "{basket.error.quantity.notNull}")
	@Min(value = 0, message = "{basket.error.quantity.invalid}")
	@Digits(fraction = 0, integer = 10, message = "{basket.error.quantity.invalid}")
	public long getHiddenPickupQty()
	{
		return hiddenPickupQty;
	}

	public void setHiddenPickupQty(final long hiddenPickupQty)
	{
		this.hiddenPickupQty = hiddenPickupQty;
	}
}
