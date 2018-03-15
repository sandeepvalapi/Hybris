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
package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.DefaultExistingItemResolver;
import de.hybris.platform.impex.jalo.imp.ValueLine;


public class TestExistingItemResolver extends DefaultExistingItemResolver
{
    private final TestImportProcessor tip;

	public TestExistingItemResolver(final TestImportProcessor tip)
	{
		super();
        this.tip = tip;
	}

	@Override
	protected void createLookupTableEntry(final ValueLine valueLine, final AbstractColumnDescriptor acd)
	{
		if (tip.isSecondPass())
		{
            super.createLookupTableEntry(valueLine, acd);
		}
		else
		{
            throw new NullPointerException("always fail in the first pass");
		}
	}
}
