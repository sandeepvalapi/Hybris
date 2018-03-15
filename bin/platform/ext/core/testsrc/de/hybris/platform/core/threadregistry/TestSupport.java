/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core.threadregistry;

import de.hybris.platform.core.threadregistry.OperationInfo.StandardAttributes;

import java.util.Objects;


class TestSupport
{
	private final ThreadRegistry threadRegistry;

	public static TestSupport forRegistry(final ThreadRegistry threadRegistry)
	{
		return new TestSupport(threadRegistry);
	}

	private TestSupport(final ThreadRegistry threadRegistry)
	{
		this.threadRegistry = Objects.requireNonNull(threadRegistry);
	}

	public boolean isCurrentThreadRegistered()
	{
		return getCurrentOperationInfo() != null;
	}

	public Object getAttributeFromCurrentOperation(final StandardAttributes attribute)
	{
		return getCurrentOperationInfo().getAllAttributes().get(attribute);
	}

	public String getAttributeFromCurrentOperation(final String attribute)
	{
		return (String) getCurrentOperationInfo().getAllAttributes().get(attribute);
	}

	public OperationInfo getCurrentOperationInfo()
	{
		return threadRegistry.getAllOperations().get(Long.valueOf(Thread.currentThread().getId()));
	}
}
