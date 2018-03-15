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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class InvalidationTest extends HybrisJUnit4TransactionalTest
{
	private InvalidationTopic topicSome, topicSomeTopic;
	private InvalidationListener listenerMock;

	@Before
	public void setUp() throws Exception
	{
		final InvalidationManager invalidationManager = new InvalidationManager(Registry.getCurrentTenant().getCache());
		topicSome = invalidationManager.getOrCreateInvalidationTopic(new String[]
		{ "some" });
		topicSomeTopic = invalidationManager.getOrCreateInvalidationTopic(new String[]
		{ "some", "topic" });

		//control = EasyMock.controlFor( InvalidationListener.class );
		listenerMock = createMock(InvalidationListener.class);
		topicSome.addInvalidationListener(listenerMock);
	}

	@Test
	public void testSimpleNotification()//NOPMD
	{
		final Object[] firstkey = new Object[]
		{ "first", "key" };
		listenerMock.keyInvalidated(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(),
				null);
		expectLastCall();
		//control.activate();
		replay(listenerMock);
		topicSome.invalidate(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		verify(listenerMock);
	}

	@Test
	public void testSuperTopicNotification()//NOPMD
	{
		final Object[] firstkey = new Object[]
		{ "first", "key" };
		listenerMock.keyInvalidated(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(),
				null);
		expectLastCall();
		//control.activate();
		replay(listenerMock);
		topicSomeTopic.invalidate(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		verify(listenerMock);
	}

	@Test
	public void testNotification() //NOPMD
	{
		final Object[] firstkey = new Object[]
		{ "first", "key" };
		final Object[] secondkey = new Object[]
		{ "second", "key" };
		listenerMock.keyInvalidated(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(),
				null);
		expectLastCall();
		listenerMock.keyInvalidated(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(),
				null);
		expectLastCall();
		listenerMock.keyInvalidated(secondkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(),
				null);
		expectLastCall();
		//control.activate();
		replay(listenerMock);
		topicSomeTopic.addInvalidationListener(listenerMock);
		topicSomeTopic.invalidate(firstkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		topicSome.invalidate(secondkey, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		verify(listenerMock);
	}

	@Test
	public void testMulticast() //NOPMD
	{
		final Object[] key = new Object[]
		{ "some", "key" };
		listenerMock.keyInvalidated(key, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED, Registry.getCurrentTenant().getCache(), null);
		expectLastCall();
		//control.activate();
		replay(listenerMock);
		topicSome.invalidate(key, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);
		verify(listenerMock);
	}

}
