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
package de.hybris.platform.servicelayer;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;



public class MockTestCleanupListener extends AbstractTestExecutionListener
{
	@Override
	public void afterTestMethod(final TestContext testContext) throws Exception
	{
		testContext.markApplicationContextDirty(null);

	}
}
