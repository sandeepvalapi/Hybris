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
package de.hybris.platform.test;

import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;

import java.util.List;
import java.util.Set;


/**
 * Test class with monitor for not empty page buffer.
 */
public class MonitoredLazyLoadItemList<E> extends LazyLoadItemList<E>
{
	private transient boolean notemptyPageBuffer = false;

	public MonitoredLazyLoadItemList(final Set<PK> prefetchLanguages, final List<PK> pks, final int prefetchSize)
	{
		super(prefetchLanguages, pks, prefetchSize);
	}

	@Override
	protected E getBuffered(final int listPos)
	{
		final E result = super.getBuffered(listPos);
		final BufferedPage<E> page = getBufferedPageIfLoaded(listPos);
		notemptyPageBuffer = page != null && !page.isEmpty();
		return result;
	}

	public boolean isNotEmptyPageBuffer()
	{
		return notemptyPageBuffer;
	}

}
