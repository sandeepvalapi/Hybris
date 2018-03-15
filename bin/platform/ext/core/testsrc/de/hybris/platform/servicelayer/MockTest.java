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

import org.junit.Ignore;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


/**
 * This test marks the ApplicationContext as dirty (see MockTestCleanupListener).
 */
@Ignore
@TestExecutionListeners(
{ MockTestCleanupListener.class })
public abstract class MockTest extends AbstractJUnit4SpringContextTests
{
	//nothing
}
