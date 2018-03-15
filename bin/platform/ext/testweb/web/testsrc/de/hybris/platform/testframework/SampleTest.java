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
package de.hybris.platform.testframework;

import java.io.NotSerializableException;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

public class SampleTest
{

	@Ignore
	@Test
	public void testTwoIgnored()
	{
		//
	}

	@Test
	public void testOne() throws InterruptedException
	{
		//
	}

	@Test
	public void testThreeFail()
	{
		junit.framework.Assert.fail("expected");
	}

	@Test
	public void testFourAssumeFails()
	{
		Assume.assumeTrue(false);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFiveThrowExpectedException()
	{
		throw new UnsupportedOperationException();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSixThrowUnExpectedException() throws Exception
	{
		throw new NotSerializableException();
	}
}