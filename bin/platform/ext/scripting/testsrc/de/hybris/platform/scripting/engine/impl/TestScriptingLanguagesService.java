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
package de.hybris.platform.scripting.engine.impl;

import org.springframework.context.ApplicationContext;


public class TestScriptingLanguagesService extends DefaultScriptingLanguagesService
{
	private ApplicationContext applicationContext;

	@Override
	ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}
}
