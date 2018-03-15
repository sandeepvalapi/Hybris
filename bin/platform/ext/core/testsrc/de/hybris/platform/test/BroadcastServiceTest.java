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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cluster.BroadcastMessageListener;
import de.hybris.platform.cluster.BroadcastMethod;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.BroadcastServiceConfiguration;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.DefaultBroadcastServiceConfiguration;
import de.hybris.platform.cluster.InvalidationBroadcastHandler;
import de.hybris.platform.cluster.LoopBackBroadcastMethod;
import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.cluster.legacy.LegacyBroadcastHandler;
import de.hybris.platform.cluster.udp.UDPBroadcastMethod;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Test {@link UDPBroadcastMethod} by causing a item modification and listening to the message by creating a second
 * broadcast method instance.
 */
@IntegrationTest
public class BroadcastServiceTest extends HybrisJUnit4Test
{
	private static final Logger log = Logger.getLogger(BroadcastServiceTest.class);

	private final double timeFactor = Config.getDouble("platform.test.timefactor", 1.0);

	@Test
	public void testLoopBackMethod() throws UnknownHostException, InterruptedException
	{
		DefaultBroadcastService loopBackModeService = null;
		try
		{
			final DefaultBroadcastServiceConfiguration cfg = new DefaultBroadcastServiceConfiguration();
			cfg.setDynamicNodeID(1234l);
			cfg.setClusterMode(false);
			loopBackModeService = new DefaultBroadcastService(cfg);
			loopBackModeService.initMethods(); // we must init methods manually since tenant listener won't fire
			assertEquals(Collections.singleton("<loopback>"), loopBackModeService.getBroadcastMethodNames());

			assertEquals(1234l, loopBackModeService.getDynamicClusterNodeID());

			final BroadcastMethod method = loopBackModeService.getBroadcastMethod("<loopback>");

			assertTrue(method instanceof LoopBackBroadcastMethod);

			final List<RawMessage> received = new ArrayList<RawMessage>();

			final BroadcastMessageListener l;

			loopBackModeService.registerBroadcastListener(l = new BroadcastMessageListener()
			{
				@Override
				public boolean processMessage(final RawMessage message)
				{
					received.add(message);
					return true;
				}
			}, false); // get local messages too

			loopBackModeService.send(new RawMessage(LegacyBroadcastHandler.KIND_CUSTOMEVENT, "foo".getBytes()));
			loopBackModeService.send(new RawMessage(LegacyBroadcastHandler.KIND_CUSTOMEVENT, "bar".getBytes()));

			Thread.sleep((long) (timeFactor * 2000));

			loopBackModeService.unregisterBroadcastListener(l);

			loopBackModeService.send(new RawMessage(LegacyBroadcastHandler.KIND_CUSTOMEVENT, "after".getBytes()));

			Thread.sleep((long) (timeFactor * 2000));

			final InetAddress local = InetAddress.getLocalHost();

			assertEquals(2, received.size());
			final RawMessage m1 = received.get(0);
			assertNotNull(m1);
			assertEquals("foo", new String(m1.getData()));
			assertEquals(local, m1.getRemoteAddress());
			assertEquals(loopBackModeService.getDynamicClusterNodeID(), m1.getDynamicNodeID());

			final RawMessage m2 = received.get(1);
			assertNotNull(m2);
			assertEquals("bar", new String(m2.getData()));
			assertEquals(local, m2.getRemoteAddress());
			assertEquals(loopBackModeService.getDynamicClusterNodeID(), m2.getDynamicNodeID());
		}
		finally
		{
			if (loopBackModeService != null)
			{
				loopBackModeService.destroy();
			}
		}

	}

	@Test
	public void testInvalidationViaUDP()
	{
		// test is only valid if clustermode=true
		if (Registry.getMasterTenant().isClusteringEnabled())
		{
			UDPBroadcastMethod otherMethod = null;
			try
			{
				final BroadcastService dbs = DefaultBroadcastService.getInstance();
				final UDPBroadcastMethod udpMethod = (UDPBroadcastMethod) dbs.getBroadcastMethod("udp");
				assertNotNull("missing upd broadcast method", udpMethod);

				final int[] packetsCounter = new int[1];

				otherMethod = new UDPBroadcastMethod()
				{
					@Override
					protected void processDatagramPacket(final java.net.DatagramPacket packet)
					{
						packetsCounter[0]++;
						super.processDatagramPacket(packet);
					}
				};
				otherMethod.init(dbs);

				final List<RawMessage> messages = new ArrayList<RawMessage>();

				final BroadcastMessageListener listener = new BroadcastMessageListener()
				{
					@Override
					public boolean processMessage(final RawMessage message)
					{
						messages.add(message);
						return false;
					}
				};

				otherMethod.registerProcessor(listener);

				// cause invalidation
				jaloSession.getUser().setName(Long.toString(System.currentTimeMillis()));

				Thread.sleep((long) (timeFactor * 2000));

				otherMethod.unregisterProcessor(listener);

				assertTrue(packetsCounter[0] > 0);

				final long dynamicNodeID = DefaultBroadcastService.getInstance().getDynamicClusterNodeID();
				final long clusterIsland = MasterTenant.getInstance().getClusterIslandPK();

				final Map<RawMessage, String> expectedMessages = new LinkedHashMap<RawMessage, String>();
				final String pkStr = jaloSession.getUser().getPK().toString();

				for (final RawMessage msg : messages)
				{
					assertNotNull(msg);
					if (msg.getClusterIslandPK() != clusterIsland)
					{
						if (log.isDebugEnabled())
						{
							log.debug("discarding message " + msg + " from other cluster island (" + clusterIsland + "<>"
									+ msg.getClusterIslandPK());
						}
					}
					else if (msg.getDynamicNodeID() != dynamicNodeID)
					{
						if (log.isDebugEnabled())
						{
							log.debug("discarding message " + msg + " from other dynamic node (" + dynamicNodeID + "<>"
									+ msg.getDynamicNodeID());
						}
					}
					else if (InvalidationBroadcastHandler.KIND_INVALIDATION != msg.getKind())
					{
						if (log.isDebugEnabled())
						{
							log.debug("discarding message " + msg + " due to unexpected kind ("
									+ InvalidationBroadcastHandler.KIND_INVALIDATION + "<>" + msg.getKind());
						}
					}
					else
					{
						final String s = new String(msg.getData());
						if (s.contains(pkStr))
						{
							expectedMessages.put(msg, s);
						}
						else
						{
							if (log.isDebugEnabled())
							{
								log.debug("discarding message " + msg + " since it does not contain " + pkStr + " text:'" + s + "'");
							}
						}
					}
				}

				assertFalse(//
						"did not find expected invalidation message (received:" + messages + ")",//
						expectedMessages.isEmpty());
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				fail("error:" + e.getMessage());
			}
			finally
			{
				if (otherMethod != null)
				{
					try
					{
						otherMethod.shutdown();
					}
					catch (final Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			log.info("skipping BroadcastServiceTest.testInvalidationViaUDP due to clustermode=false");
		}
	}

	/**
	 * Tests PLA-8550: whenever the message sending queue was filled up the calling thread did not block for the
	 * specified time but got a error immediately.
	 * <p>
	 * So the tests tries to send considerably more messages than queue size could allow and tries to get that error
	 * again. If the test finished the error must be fixed.
	 */
	@Test
	public void testMessageSendingQueue()
	{
		DefaultBroadcastService bs = null;
		try
		{
			final boolean[] gotError = new boolean[1];
			final BroadcastServiceConfiguration cfg = new DefaultBroadcastServiceConfiguration();
			bs = new DefaultBroadcastService(cfg)
			{
				@Override
				protected void sendAsnychronously(final MethodWrapper m, final RawMessage message,
						final de.hybris.platform.core.Tenant currentTenant, final java.util.concurrent.ExecutorService es)
						throws java.util.concurrent.RejectedExecutionException
				{
					try
					{
						super.sendAsnychronously(m, message, currentTenant, es);
					}
					catch (final RejectedExecutionException e)
					{
						gotError[0] = true;
						throw e;
					}
				}
			};
			bs.initMethods();

			final int queueSize = cfg.getMessageSendingQueueSize();

			final int queueSizeExceeded = (int) (1.5d * queueSize);

			assertTrue(queueSize < queueSizeExceeded);

			for (int i = 0; i < queueSizeExceeded; i++)
			{
				bs.send(new RawMessage(123));
			}

			assertFalse(gotError[0]);

			// no error by now -> done
		}
		finally
		{
			if (bs != null)
			{
				bs.destroy();
			}
		}
	}

	@Test
	public void testOneLargeUDPPacket() throws InterruptedException
	{
		// test is only valid if clustermode=true
		if (Registry.getMasterTenant().isClusteringEnabled())
		{

			final RawMessage longmsg = new RawMessage(10,
					("this schould a string which is longer than 1000 bytes so this string should "
							+ "be splitted (not splatted or splutted) ................................"
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "....................................................................."
							+ "........xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx......xxxA").getBytes());
			assertTrue("too short!", longmsg.getData().length > 1000);

			launchMessages(Collections.singletonList(longmsg));
		}
		else
		{
			log.info("skipping BroadcastServiceTest.testOneLargeUDPPacket due to clustermode=false");
		}
	}

	@Test
	public void testRandomPacketSizesAndPacketAmounts() throws InterruptedException
	{
		// test is only valid if clustermode=true
		if (Registry.getMasterTenant().isClusteringEnabled())
		{
			log.info("testing normal packets: 50 packets, maxsize 500byte, minsize 0 byte");
			launchPackets(50, 500, 0);

			log.info("testing oversized packets 50 packets, maxsize 5000byte, minsize 1000 byte");
			launchPackets(50, 5000, 1000);
		}
		else
		{
			log.info("skipping BroadcastServiceTest.testRandomPacketSizesAndPacketAmounts due to clustermode=false");
		}
	}



	private void launchPackets(final int packetcount, final int maxpacketsize, final int minpacketsize)
			throws InterruptedException
	{
		final List<RawMessage> toSend = new ArrayList<RawMessage>();
		final Random randomGenerator = new Random();

		for (int index = 0; index < packetcount; index++)
		{
			final byte[] data = new byte[randomGenerator.nextInt(maxpacketsize) + minpacketsize]; //min size: 1000bytes, max size: 999900bytes
			randomGenerator.nextBytes(data);
			toSend.add(new RawMessage(11, data));
		}
		launchMessages(toSend);
	}

	private void launchMessages(final List<RawMessage> toSend) throws InterruptedException
	{
		final BroadcastService dbs = DefaultBroadcastService.getInstance();
		final UDPBroadcastMethod udpMethod = (UDPBroadcastMethod) dbs.getBroadcastMethod("udp");
		assertNotNull("missing upd broadcast method", udpMethod);


		final ArrayList<RawMessage> recievedMessages = new ArrayList<RawMessage>();

		final BroadcastMessageListener listener = new BroadcastMessageListener()
		{
			@Override
			public boolean processMessage(final RawMessage message)
			{
				recievedMessages.add(message);
				return false;
			}
		};

		dbs.registerBroadcastListener(listener, false);

		for (final RawMessage sentNow : toSend)
		{
			dbs.send(sentNow);
		}

		Thread.sleep((long) (timeFactor * 2000));

		dbs.unregisterBroadcastListener(listener);


		for (final RawMessage toCheck : toSend)
		{
			RawMessage compareMsg = null;
			for (final RawMessage msg : recievedMessages)
			{
				if (toCheck.getMessageKey().equals(msg.getMessageKey()))
				{
					compareMsg = msg;
					break;
				}
			}
			compareRawMessages(toCheck, compareMsg);
		}

	}


	private void compareRawMessages(final RawMessage expected, final RawMessage current)
	{
		assertNotNull(expected);
		assertNotNull("no recieved message found", current);
		assertArrayEquals("rawmessage.data is not equal", expected.getData(), current.getData());
		assertEquals("rawmessage.clusterislandpk is not equal", expected.getClusterIslandPK(), current.getClusterIslandPK());
		assertEquals("rawmessage.currentpacketnumber is not equal", expected.getCurrentPacketNumber(),
				current.getCurrentPacketNumber());
		assertEquals("rawmessage.dynamicnodeid is not equal", expected.getDynamicNodeID(), current.getDynamicNodeID());
		assertEquals("rawmessage.kind is not equal", expected.getKind(), current.getKind());
		assertEquals("rawmessage.messagekey is not equal", expected.getMessageKey(), current.getMessageKey());
		assertEquals("rawmessage.messagenumber is not equal", expected.getMessageNumber(), current.getMessageNumber());
		assertEquals("rawmessage.numberofpackets is not equal", expected.getNumberOfPackets(), current.getNumberOfPackets());
		assertEquals("rawmessage.version is not equal", expected.getVersion(), current.getVersion());

	}

}
