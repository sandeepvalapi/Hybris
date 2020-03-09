/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.textfieldconfiguratortemplateaddon.forms;

import de.hybris.platform.catalog.enums.ConfiguratorType;

import java.util.Map;

public class TextFieldConfigurationForm
{
	private Long quantity;
	private Map<ConfiguratorType, Map<String, String>> configurationsKeyValueMap;

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Map<ConfiguratorType, Map<String, String>> getConfigurationsKeyValueMap()
	{
		return configurationsKeyValueMap;
	}

	public void setConfigurationsKeyValueMap(final Map<ConfiguratorType, Map<String, String>> configurationsKeyValueMap)
	{
		this.configurationsKeyValueMap = configurationsKeyValueMap;
	}
}
