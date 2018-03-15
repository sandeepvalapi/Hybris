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
package de.hybris.platform.spring.ctx;

public class TestSubBean
{

	private String simpleProperty;

	/**
	 * @param simpleProperty
	 *           the simpleProperty to set
	 */
	public void setSimpleProperty(final String simpleProperty)
	{
		this.simpleProperty = simpleProperty;
	}


	/**
	 * @return the simpleProperty
	 */
	public String getSimpleProperty()
	{
		return simpleProperty;
	}

}
