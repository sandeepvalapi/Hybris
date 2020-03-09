/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

/**
 * Form for adding entry group to the cart.
 */
public class AddToEntryGroupForm
{
	private String productCode;
	private Integer entryGroupNumber;

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public Integer getEntryGroupNumber()
	{
		return entryGroupNumber;
	}

	public void setEntryGroupNumber(Integer entryGroupNumber)
	{
		this.entryGroupNumber = entryGroupNumber;
	}
}
