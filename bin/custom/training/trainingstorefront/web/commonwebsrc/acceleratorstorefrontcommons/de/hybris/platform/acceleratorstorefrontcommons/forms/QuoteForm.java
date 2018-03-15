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

import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.QuoteExpirationTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Form for handling quote fields.
 */
public class QuoteForm
{
	@NotNull(message = "{quote.name.invalid}", groups = {Buyer.class, Name.class})
	@Size(min = 1, max = 255, message = "{quote.name.invalid}", groups = {Buyer.class, Name.class})
	private String name;
	@Size(min = 0, max = 255, message = "{quote.description.invalid}", groups = {Buyer.class, Description.class})
	private String description;
	@QuoteExpirationTime(message = "{text.quote.expiration.time.invalid}", groups = {Seller.class})
	private String expirationTime;

	public String getExpirationTime()
	{
		return expirationTime;
	}

	public void setExpirationTime(final String expirationTime)
	{
		this.expirationTime = expirationTime;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name != null ? name.trim() : null;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public interface Buyer
	{
		//empty
	}

	public interface Seller
	{
		//empty
	}

	public interface Name
	{
		//empty
	}

	public interface Description
	{
		//empty
	}
}
