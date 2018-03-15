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
package de.hybris.platform.cache.udp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.cluster.udp.DefaultUnicastBroadcastConfiguration;
import de.hybris.platform.cluster.udp.UnicastBroadcastMethod;
import de.hybris.platform.cluster.udp.UnicastBroadcastMethodConfiguration;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.FastHashMapConfig;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Test;


/**
 *
 */
public class UDPUnicastTest extends HybrisJUnit4Test
{
	@Test
	public void testConfigUpdate()
	{
		final byte[] localhost =
		{ 127, 0, 0, 1 };
		final String initialNodesConfig = "127.0.0.1 ;127.0.0.1:12346,127.0.0.1:12347";
		final int port = 12345;
		final List<InetSocketAddress> initialNodes = DefaultUnicastBroadcastConfiguration.parseClusterNodes(initialNodesConfig,
				port);

		final Map<String, String> params = new HashMap<String, String>();
		params.put(DefaultUnicastBroadcastConfiguration.CFG_SERVERADDRESS, "127.0.0.1"); //NOPMD
		params.put(DefaultUnicastBroadcastConfiguration.CFG_PORT, Integer.toString(port));
		params.put(DefaultUnicastBroadcastConfiguration.CFG_SYNC_INTERVAL, "-1");
		params.put(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, initialNodesConfig);

		final ConfigIntf cfg = new FastHashMapConfig(params);

		final UnicastBroadcastMethod method = new UnicastBroadcastMethod()
		{
			@Override
			protected ConfigIntf getConfig()
			{
				return cfg;
			}
		};

		try
		{
			method.init(null);

			Collection<InetSocketAddress> currentNodes = method.getClusterNodes();
			assertEquals(3, currentNodes.size());
			for (final InetSocketAddress initial : initialNodes)
			{
				assertNodeExists(currentNodes, localhost, initial.getPort());
			}

			cfg.setParameter(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, "   127.0.0.1 : 3333  ");
			currentNodes = method.getClusterNodes();
			assertEquals(2, currentNodes.size());
			assertNodeExists(currentNodes, localhost, 3333);
			assertNodeExists(currentNodes, localhost, port);

			cfg.setParameter(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, "127.0.0.1:4444  , 127.0.0.1:9999  ");
			currentNodes = method.getClusterNodes();
			assertEquals(3, currentNodes.size());
			assertNodeExists(currentNodes, localhost, 4444);
			assertNodeExists(currentNodes, localhost, 9999);
			assertNodeExists(currentNodes, localhost, port);

			cfg.setParameter(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, null);
			currentNodes = method.getClusterNodes();
			assertEquals(1, currentNodes.size());
			assertNodeExists(currentNodes, localhost, port);
		}
		finally
		{
			method.shutdown();
		}

	}

	void assertNodeExists(final Collection<InetSocketAddress> nodes, final byte[] ipAddress, final int port)
	{
		for (final InetSocketAddress a : nodes)
		{
			if (Arrays.equals(ipAddress, a.getAddress().getAddress()) && port == a.getPort())
			{
				return;
			}
		}
		fail("nodes " + nodes + " did not contain " + Arrays.toString(ipAddress) + ":" + port);
	}

	@Test
	public void testClusterOfThree()
	{
		final UnicastBroadcastMethodConfiguration defaultCfg = new DefaultUnicastBroadcastConfiguration(Registry.getMasterTenant()
				.getConfig());

		final InetSocketAddress[] allNodes =
		{ InetSocketAddress.createUnresolved("127.0.0.1", defaultCfg.getServerAddress().getPort() + 1), //NOPMD
				InetSocketAddress.createUnresolved("127.0.0.1", defaultCfg.getServerAddress().getPort() + 2), //NOPMD
				InetSocketAddress.createUnresolved("127.0.0.1", defaultCfg.getServerAddress().getPort() + 3) }; //NOPMD

		final List<RawMessage> receivedMessages1 = new CopyOnWriteArrayList<RawMessage>();
		final List<RawMessage> receivedMessages2 = new CopyOnWriteArrayList<RawMessage>();
		final List<RawMessage> receivedMessages3 = new CopyOnWriteArrayList<RawMessage>();


		final TestUnicastMethod node1Method = createNewNode(receivedMessages1, allNodes[0], allNodes);
		final TestUnicastMethod node2Method = createNewNode(receivedMessages2, allNodes[1], allNodes);
		final TestUnicastMethod node3Method = createNewNode(receivedMessages3, allNodes[2], allNodes);
		try
		{
			node1Method.init(null);
			node2Method.init(null);
			node3Method.init(null);

			final String node1ToAll = "node1toall";
			sendMessage(node1Method, node1ToAll);
			assertMessageReceived(node1ToAll, 5, receivedMessages1, receivedMessages2, receivedMessages3);

			final String node2ToAll = "node2toall";
			sendMessage(node2Method, node2ToAll);
			assertMessageReceived(node2ToAll, 5, receivedMessages1, receivedMessages2, receivedMessages3);

			final String node3ToAll = "node3toall";
			sendMessage(node3Method, node3ToAll);
			assertMessageReceived(node3ToAll, 5, receivedMessages1, receivedMessages2, receivedMessages3);
		}
		finally
		{
			node1Method.shutdown();
			node2Method.shutdown();
			node3Method.shutdown();
		}
	}

	void assertMessageReceived(final String payload, final int maxWaitSeconds, final List<RawMessage> receivedMessages1,
			final List<RawMessage> receivedMessages2, final List<RawMessage> receivedMessages3)
	{
		final long maxWait = System.currentTimeMillis() + (maxWaitSeconds * 1000);
		do
		{
			if (containsMessage(payload, receivedMessages1) && containsMessage(payload, receivedMessages2)
					&& containsMessage(payload, receivedMessages3))
			{
				break;
			}
		}
		while (System.currentTimeMillis() < maxWait);
	}

	boolean containsMessage(final String payload, final List<RawMessage> messages)
	{
		for (final RawMessage m : messages)
		{
			if (payload.equals(new String(m.getData())))
			{
				return true;
			}
		}
		return false;
	}

	void sendMessage(final TestUnicastMethod method, final String text)
	{
		method.send(new RawMessage(77, text.getBytes()));
	}

	TestUnicastMethod createNewNode(final List<RawMessage> receivedMessages, final InetSocketAddress address,
			final InetSocketAddress... nodes)
	{
		final TestUnicastMethod ret = new TestUnicastMethod(receivedMessages);

		final DefaultUnicastBroadcastConfiguration configuration = new DefaultUnicastBroadcastConfiguration();
		configuration.setServerAddress(address);
		configuration.setClusterNodes(Arrays.asList(nodes));
		configuration.setSyncNodesIntervalSeconds(-1);

		ret.setConfiguration(configuration);

		return ret;
	}

	static class TestUnicastMethod extends UnicastBroadcastMethod
	{
		final List<RawMessage> receivedMessages;

		TestUnicastMethod(final List<RawMessage> receivedMessages)
		{
			this.receivedMessages = receivedMessages;
		}

		@Override
		protected void notifyMessgageReceived(final RawMessage message)
		{
			receivedMessages.add(message);

			super.notifyMessgageReceived(message);
		}
	}
}
