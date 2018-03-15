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
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.HybrisConfig;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class HybrisConfigTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(HybrisConfigTest.class);

	@Test
	public void testAdvancedProperties()
	{
		if (Registry.isStandaloneMode())
		{
			assertEquals("ok", Registry.getMasterTenant().getConfig().getParameter("junit.marker"));
		}
		else
		{
			LOG.info("running in server mode - skipped test");
		}
	}

	@Test
	public void testConfig()
	{
		final Properties data = new Properties();

		// base 
		data.put("path.key", "path.key");
		data.put("path2.key2", "path2.key2");
		data.put("key4", "key4");

		// standalone plain
		data.put("standalone.path.key", "standalone.path.key");
		data.put("standalone.onlystandalone", "standalone.onlystandalone");

		// cluster plain
		data.put("cluster.1.path2.key2", "cluster.1.path2.key2");
		data.put("cluster.2.path2.key2", "cluster.2.path2.key2");
		data.put("cluster.2.onlycluster", "cluster.2.onlycluster");
		// wrong cluster key
		data.put("cluster.wrong.foo", "cluster.wrong.foo");

		// combined
		data.put("standalone.cluster.1.path2.key2", "standalone.cluster.1.path2.key2");
		data.put("standalone.cluster.2.onlycluster", "standalone.cluster.2.onlycluster");
		data.put("standalone.cluster.2.ttt", "standalone.cluster.2.ttt");


		// 1. clusterId = 0 , no standalone
		data.put(Config.Params.CLUSTER_ID, "0");
		ConfigIntf cfg = new HybrisConfig(data, false, 0);

		assertEquals("path.key", cfg.getParameter("path.key"));
		assertEquals("key4", cfg.getParameter("key4"));
		assertNull(cfg.getParameter("doesnotexist"));
		assertEquals("path2.key2", cfg.getParameter("path2.key2"));
		assertNull(cfg.getParameter("onlycluster"));
		assertNull(cfg.getParameter("onlystandalone"));
		assertEquals("cluster.wrong.foo", cfg.getParameter("cluster.wrong.foo"));

		// 2. clusterId = 0 + standalone
		//		data.put("standalone.path.key", "standalone.path.key");
		//		data.put("standalone.onlystandalone", "standalone.onlystandalone");
		cfg = new HybrisConfig(data, true, 0);

		assertNull(cfg.getParameter("doesnotexist"));
		assertEquals("key4", cfg.getParameter("key4"));
		assertEquals("path2.key2", cfg.getParameter("path2.key2"));
		assertEquals("standalone.path.key", cfg.getParameter("path.key"));
		assertEquals("standalone.onlystandalone", cfg.getParameter("onlystandalone"));
		assertNull(cfg.getParameter("onlycluster"));
		assertEquals("cluster.wrong.foo", cfg.getParameter("cluster.wrong.foo"));

		// 2. clusterId = 1 - standalone
		//		data.put("cluster.1.path2.key2", "cluster.1.path2.key2");
		data.put(Config.Params.CLUSTER_ID, "1");
		cfg = new HybrisConfig(data, false, 1);

		assertEquals("key4", cfg.getParameter("key4"));
		assertEquals("path.key", cfg.getParameter("path.key"));
		assertNull(cfg.getParameter("doesnotexist"));
		assertEquals("cluster.1.path2.key2", cfg.getParameter("path2.key2"));
		assertNull(cfg.getParameter("onlycluster"));
		assertNull(cfg.getParameter("onlystandalone"));
		assertEquals("cluster.wrong.foo", cfg.getParameter("cluster.wrong.foo"));

		// 2. clusterId = 2 - standalone
		//		data.put("cluster.2.path2.key2", "cluster.2.path2.key2");
		//		data.put("cluster.2.onlycluster", "cluster.2.onlycluster");
		data.put(Config.Params.CLUSTER_ID, "2");
		cfg = new HybrisConfig(data, false, 2);

		assertEquals("key4", cfg.getParameter("key4"));
		assertEquals("path.key", cfg.getParameter("path.key"));
		assertNull(cfg.getParameter("doesnotexist"));
		assertNull(cfg.getParameter("onlystandalone"));
		assertEquals("cluster.2.path2.key2", cfg.getParameter("path2.key2"));
		assertEquals("cluster.2.onlycluster", cfg.getParameter("onlycluster"));
		assertEquals("cluster.wrong.foo", cfg.getParameter("cluster.wrong.foo"));

		// 2. clusterId = 2 + standalone
		//		data.put("standalone.cluster.1.path2.key2", "standalone.cluster.1.path2.key2");
		//		data.put("standalone.cluster.2.onlycluster", "standalone.cluster.2.onlycluster");
		//		data.put("standalone.cluster.2.ttt", "standalone.cluster.2.ttt");
		data.put(Config.Params.CLUSTER_ID, "2");
		cfg = new HybrisConfig(data, true, 2);

		assertEquals("key4", cfg.getParameter("key4"));
		assertEquals("standalone.path.key", cfg.getParameter("path.key"));
		assertNull(cfg.getParameter("doesnotexist"));
		assertEquals("standalone.onlystandalone", cfg.getParameter("onlystandalone"));
		assertEquals("cluster.2.path2.key2", cfg.getParameter("path2.key2"));
		assertEquals("standalone.cluster.2.onlycluster", cfg.getParameter("onlycluster"));
		assertEquals("standalone.cluster.2.ttt", cfg.getParameter("ttt"));
		assertEquals("cluster.wrong.foo", cfg.getParameter("cluster.wrong.foo"));

	}
}
