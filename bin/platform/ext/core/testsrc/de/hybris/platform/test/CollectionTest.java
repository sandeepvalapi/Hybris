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
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CollectionTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(CollectionTest.class.getName());

	private Product product;
	private Media media;

	@Before
	public void setUp() throws Exception
	{
		product = jaloSession.getProductManager().createProduct("hfldshf");
		media = jaloSession.getMediaManager().createMedia("gdkjfm.,");
	}

	@After
	public void tearDown() throws Exception
	{
		final Iterator iter = jaloSession.getMediaManager().getMediaByCode("media_%").iterator();
		while (iter.hasNext())
		{
			final Media next = (Media) iter.next();
			next.remove();
		}
		product.remove();
		media.remove();
	}

	@Test
	public void testHashSet() throws ConsistencyCheckException
	{
		final int checkUntil100 = 1;
		final int checkFrom100 = 100;
		final Collection expected = new HashSet();
		for (int i = 1; i < 100; i++)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("trying " + i);
			}
			final Link next = jaloSession.getLinkManager().createLink("media_" + i, product, media, 0, 0);
			expected.add(next);
			if ((i < 100 && i % checkUntil100 == 0) || (i >= 100 && i % checkFrom100 == 0))
			{
				///*conv-log*/ log.debug("z 75 ...");
				final Collection getBack = jaloSession.getLinkManager().getLinks("%", product, media);
				///*conv-log*/ log.debug("z 78 ...");
				assertEquals("Collection size is not equal", i, getBack.size());
				///*conv-log*/ log.debug("z 80 ...");
				assertTrue("Collection don't contain all elements", expected.containsAll(getBack));
				//assertTrue(expected.containsAll(jaloSession.getLinkManager().getLinks("media_0", product, media, Link.UNI)));
				///*conv-log*/ log.debug("z 83 ...");
				assertTrue("", getBack.containsAll(expected));
				//assertTrue(getBack.containsAll(jaloSession.getLinkManager().getLinks("media_0", product, media, Link.UNI)));
				///*conv-log*/ log.debug("z 86 ...");

				final Iterator iter = getBack.iterator();

				while (iter.hasNext())
				{
					///*conv-log*/ log.debug("hash: "+iter.next().hashCode());
					iter.next().hashCode();
				}

			}
		}
	}



}
