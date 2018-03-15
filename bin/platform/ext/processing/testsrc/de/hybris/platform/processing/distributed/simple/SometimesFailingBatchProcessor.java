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
package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.core.PK;

import java.util.List;

import com.google.common.base.Preconditions;


public class SometimesFailingBatchProcessor extends TestBatchProcessor
{

	@Override
	protected List<PK> asList(final Object ctx)
	{
		Preconditions.checkState(ctx instanceof List, "ctx must be instance of List");
		final List<PK> result = (List<PK>) ctx;

		if (result.size() < 100)
		{
			throw new IllegalStateException("Test exception");
		}

		return result;
	}
}
