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

import static junit.framework.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.LazyLoadMultiColumnList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


@UnitTest
public class LazyLoadCollectionsTest
{

	// see PLA-8051
	@Test
	public void testMultiColumnList()
	{
		final List originalRows = Arrays.asList( //
				Arrays.asList("0_0", "0_1", "0_2"),// 
				Arrays.asList("1_0", "1_1", "1_2"),// 
				Arrays.asList("2_0", "2_1", "2_2")// 
				);
		final LazyLoadMultiColumnList list = new LazyLoadMultiColumnList(//
				originalRows, //
				(List) Arrays.asList(Serializable.class, Serializable.class, Serializable.class), //
				null,//
				100,//
				true//
		)
		{

			@Override
			protected Object wrapObject(final Object original)
			{
				return original;
			}
		};

		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				assertEquals(row + "_" + col, list.get(row).get(col));
			}
		}
	}
}
