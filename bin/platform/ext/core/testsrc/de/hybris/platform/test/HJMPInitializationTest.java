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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.hjmp.HJMPFindInvalidationListener;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


@IntegrationTest
public class HJMPInitializationTest extends HybrisJUnit4TransactionalTest
{
	@Test
	public void testInitialization()
	{
		final MockCache mockCache = new MockCache();
		final InvalidationManager manager = new InvalidationManager(mockCache);

		final InvalidationTopic topic_hjmp = manager.getInvalidationTopic(new String[]
		{ Cache.CACHEKEY_HJMP });

		//in platform <=2.20, the hjmp topic was created if creating the InvalidationManager,
		//but since 3.0, it is not! automatically created
		assertNull(topic_hjmp);
		//assertNotNull( topic_hjmp );


		final InvalidationTopic topic_hjmp_entity = manager.getInvalidationTopic(new String[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY });

		//in platform <=2.20, the hjmp topic was created if creating the InvalidationManager,
		//but since 3.0, it is not! automatically created
		assertNull(topic_hjmp_entity);
		//assertNotNull( topic_hjmp_entity );

		final InvalidationTopic topic_hjmp_entity_somebean = manager.getOrCreateInvalidationTopic(new String[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, "10" });
		topic_hjmp_entity_somebean.addInvalidationListener(new HJMPFindInvalidationListener("10"));

		final Object[] key = new Object[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, "10", PK.createFixedUUIDPK(0, 1) };

		//this key is NOT expected in Platform 3.0, because the HJMPEntityInvListener
		//is NOT registered when creating a new InvalidationManager as is was before
		//mockCache.expectInvalidate( key );


		mockCache.expectInvalidate(new Object[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_FIND, "10" });

		topic_hjmp_entity_somebean.invalidate(key, de.hybris.platform.cache.AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

		mockCache.verify();
	}

	static class MockCache implements InvalidationTarget
	{
		List expectedInvalidations = new ArrayList();

		void expectInvalidate(final Object[] key)
		{
			expectedInvalidations.add(Arrays.asList(key));
		}

		@Override
		public void invalidate(final Object[] key, final int invalidationType)
		{
			final List keyAsList = Arrays.asList(key);
			if (!expectedInvalidations.remove(keyAsList))
			{
				fail("unexpected invalidation: " + keyAsList);
			}
		}

		void verify()
		{
			if (!expectedInvalidations.isEmpty())
			{
				fail("invalidations missing: " + expectedInvalidations);
			}
		}
	}

}
