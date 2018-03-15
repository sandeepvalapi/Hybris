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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class InvalidationTopicTest extends HybrisJUnit4TransactionalTest
{
	InvalidationManager invalidationManager;
	InvalidationTopic topicSome, topicSomeTopic;

	@Before
	public void setUp() throws Exception
	{
		invalidationManager = new InvalidationManager(Registry.getCurrentTenant().getCache());
		topicSome = invalidationManager.getOrCreateInvalidationTopic(new String[]
		{ "some" });
		topicSomeTopic = invalidationManager.getOrCreateInvalidationTopic(new String[]
		{ "some", "topic" });
	}

	@Test
	public void testName()
	{
		assertEquals("some", topicSome.getName());
		assertEquals("topic", topicSomeTopic.getName());
	}

	@Test
	public void testSuperTopic()
	{
		assertEquals(topicSome, topicSomeTopic.getSuperTopic());
		assertNotNull(topicSome.getSuperTopic());
		assertEquals("root", topicSome.getSuperTopic().getName());
		assertNull(topicSome.getSuperTopic().getSuperTopic());
	}

	@Test
	public void testNoTopic()
	{
		assertEquals(null, invalidationManager.getInvalidationTopic(new String[]
		{ "bla", "tralalala" }));
		assertEquals(null, invalidationManager.getInvalidationTopic(new String[]
		{ "root" }));
	}

	@Test
	public void testCreate()
	{
		try
		{
			invalidationManager.getOrCreateInvalidationTopic(new String[]
			{ "parent", "doesnt", "exist" });

			//should fail here in Platform <=2.20, but in 3.0 the parent topic is implicitly created
			//fail( "created topic without parent" );
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}
	}

	@Test
	public void testTopic()
	{
		assertSame(topicSome, invalidationManager.getInvalidationTopic(new String[]
		{ "some" }));
		assertSame(topicSomeTopic, invalidationManager.getInvalidationTopic(new String[]
		{ "some", "topic" }));
		assertNotNull(invalidationManager.getInvalidationTopic(new String[0]));
		assertEquals("root", invalidationManager.getInvalidationTopic(new String[0]).getName());
		assertEquals(null, InvalidationManager.getInstance().getInvalidationTopic(new String[]
		{ "some" }));
	}

}
