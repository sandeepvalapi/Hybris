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

package de.hybris.platform.audit.demo;

import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.HashMap;
import java.util.Map;


public class AuditTestConfigManager
{

	private final Map<String, PropertyConfigSwitcher> auditedTypes = new HashMap<>();
	private final AuditEnablementService auditEnablementService;

	/**
	 * @deprecated since 6.6 in favor of {@link AuditTestConfigManager#AuditTestConfigManager(AuditEnablementService)}.
	 */
	@Deprecated
	public AuditTestConfigManager(
			final de.hybris.platform.directpersistence.audit.internal.AuditEnablementService auditEnablementService)
	{
		this(auditEnablementService.getDelegate());
	}

	public AuditTestConfigManager(final AuditEnablementService auditEnablementService)
	{
		this.auditEnablementService = auditEnablementService;
	}

	public void enableAuditingForTypes(final String... types)
	{
		for (final String type : types)
		{
			final PropertyConfigSwitcher switcher = getPropertyConfigSwitcher(type);
			switcher.switchToValue("true");
		}

		auditEnablementService.refreshConfiguredAuditTypes();
	}

	public void disableAuditingForTypes(final String... types)
	{
		for (final String type : types)
		{
			final PropertyConfigSwitcher switcher = getPropertyConfigSwitcher(type);
			switcher.switchToValue("false");
		}

		auditEnablementService.refreshConfiguredAuditTypes();
	}

	private PropertyConfigSwitcher getPropertyConfigSwitcher(String type)
	{
		final PropertyConfigSwitcher switcher;
		if (auditedTypes.containsKey(type))
		{
			switcher = auditedTypes.get(type);
		}
		else
		{
			switcher = new PropertyConfigSwitcher("audit." + type + ".enabled");
			auditedTypes.put(type, switcher);
		}
		return switcher;
	}

	public void resetAuditConfiguration()
	{
		for (final Map.Entry<String, PropertyConfigSwitcher> entry : auditedTypes.entrySet())
		{
			entry.getValue().switchBackToDefault();
		}

		auditEnablementService.refreshConfiguredAuditTypes();
	}
}
