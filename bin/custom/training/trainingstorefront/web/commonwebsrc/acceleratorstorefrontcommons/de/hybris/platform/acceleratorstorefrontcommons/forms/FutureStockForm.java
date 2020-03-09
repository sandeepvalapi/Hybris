/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import java.util.List;


public class FutureStockForm
{

	private List<String> skus;
	private String productCode;

	public List<String> getSkus()
	{
		return skus;
	}

	public void setSkus(final List<String> skus)
	{
		this.skus = skus;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}


}
