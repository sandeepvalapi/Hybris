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
package de.hybris.platform.core;

import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 *
 */
public class TestSlaveTenantStub extends SlaveTenant
{

	/**
	 *
	 */
	public TestSlaveTenantStub(final String systemName)
	{
		super(systemName, new Properties());
		// YTODO Auto-generated constructor stub
	}

	@Override
	public int getClusterID()
	{
		// have to skip call to Registry.getClusterID() to avoid whole platform startup
		return 0;
	}


	@Override
	List<TenantListener> getTenantListeners()
	{

		return Collections.EMPTY_LIST;
	}

}
