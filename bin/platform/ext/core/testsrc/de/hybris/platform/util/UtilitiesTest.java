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
package de.hybris.platform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class UtilitiesTest
{
	@Test
	public void shouldReturnNullContextWhenNullRequestUriIsProvided()
	{
		final String nullRequestUri = null;
		final String result = Utilities.getContextFromRequestUri(nullRequestUri);
		assertNull(result);
	}

	@Test
	public void shouldReturnEmptyContextWhenEmptyRequestUriIsProvided()
	{
		final String emptyRequestUri = "";
		final String result = Utilities.getContextFromRequestUri(emptyRequestUri);
		assertEquals("", result);
	}

	@Test
	public void shouldReturnEmptyContextWhenOnlyWhitespacesAsRequestUriAreProvided()
	{
		final String whitespacesRequestUri = " \t  ";
		final String result = Utilities.getContextFromRequestUri(whitespacesRequestUri);
		assertEquals("", result);
	}

	@Test
	public void shouldReturnContextWhenContextIsPassedAsRequestUri()
	{
		final String context1 = "/";
		final String context2 = "/ctx";
		assertEquals(context1, Utilities.getContextFromRequestUri(context1));
		assertEquals(context2, Utilities.getContextFromRequestUri(context2));
	}

	@Test
	public void shouldReturnProperContextForComplexRequestUri()
	{
		final String context1 = "/ctx1/";
		final String context2 = "/ctx2/sub1";
		final String context3 = "/ctx3/sub1/";
		final String context4 = "/ctx4/sub1/sub2";
		assertEquals("/ctx1", Utilities.getContextFromRequestUri(context1));
		assertEquals("/ctx2", Utilities.getContextFromRequestUri(context2));
		assertEquals("/ctx3", Utilities.getContextFromRequestUri(context3));
		assertEquals("/ctx4", Utilities.getContextFromRequestUri(context4));
	}

}
