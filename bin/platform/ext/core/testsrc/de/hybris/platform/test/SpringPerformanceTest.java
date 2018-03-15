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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class SpringPerformanceTest extends HybrisJUnit4Test
{
	private static final Logger log = Logger.getLogger(SpringPerformanceTest.class);

	@Test
	public void testModificationTime() throws Exception
	{
		final SavedQuery query = FlexibleSearch.getInstance().createSavedQuery("$$",
				TypeManager.getInstance().getComposedType(Product.class), "SELECT {PK} FROM {Product}", new HashMap());
		query.getModificationTime();

		final long timeNanos = System.nanoTime();
		for (int i = 0; i < 1000000; i++)
		{
			query.getModificationTime();
		}
		final long timeMillis = (System.nanoTime() - timeNanos) / 1000 / 1000;
		log.info("Calling 10.000.000 times getModificationTime(): " + timeMillis + "ms");
	}
}
