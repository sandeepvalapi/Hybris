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

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.WrapperFactory;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;


/**
 * Junit Test for EJBCollections (EJBMap/EJBSet/EJBList)
 */
@IntegrationTest
public class WrapperTest
{

	@Test
	public void testBug1025() throws Exception
	{
		final Set s = new TreeSet();
		s.add("test");
		s.add("string");
		final Set safter = (Set) WrapperFactory.wrap(s);
		if (!(safter instanceof SortedSet))
		{
			fail("SortedSet is not wrapped correctly");
		}



	}

}
