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
package de.hybris.platform.core;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.Assert;

import org.junit.Test;


/**
 * Unit tests for {@link GenericSearchOrderBy}.
 */
@UnitTest
public class GenericSearchOrderByTest
{

	private static final GenericSearchField ANY_SEARCH_FIELD = new GenericSearchField("TEST");
	private static final GenericSearchField OTHER_SEARCH_FIELD = new GenericSearchField("TEST_OTHER");

	@Test
	public void sameSearchFieldShouldBeEqual()
	{
		final GenericSearchOrderBy order1 = new GenericSearchOrderBy(ANY_SEARCH_FIELD, true);
		final GenericSearchOrderBy order2 = new GenericSearchOrderBy(ANY_SEARCH_FIELD, true);

		Assert.assertEquals(order1, order2);
	}

	@Test
	public void differentSearchFieldsShouldNotBeEqual()
	{
		final GenericSearchOrderBy order1 = new GenericSearchOrderBy(ANY_SEARCH_FIELD, true);
		final GenericSearchOrderBy order2 = new GenericSearchOrderBy(OTHER_SEARCH_FIELD, true);

		Assert.assertNotEquals(order1, order2);
	}

}
