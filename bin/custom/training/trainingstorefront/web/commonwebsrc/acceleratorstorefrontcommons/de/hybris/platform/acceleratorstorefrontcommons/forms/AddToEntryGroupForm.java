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
