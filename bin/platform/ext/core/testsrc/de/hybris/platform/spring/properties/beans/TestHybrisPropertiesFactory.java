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
package de.hybris.platform.spring.properties.beans;

import de.hybris.platform.servicelayer.config.impl.HybrisPropertiesFactory;

import java.util.Map;
import java.util.Properties;


/**
 *
 */
public class TestHybrisPropertiesFactory extends HybrisPropertiesFactory
{
	private Map<String, String> allProps;


	public void setAllProps(final Map<String, String> allProps)
	{
		this.allProps = allProps;
	}


	@Override
	public Properties getObject()
	{
		final Properties props = new Properties();
		props.putAll(allProps);
		return props;
	}

}
