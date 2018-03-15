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

import static de.hybris.platform.cluster.RawMessage.HEADER_SIZE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cluster.RawMessage;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests {@link RawMessage} binary encoding and decoding.
 */
@UnitTest
public class RawMessageTest
{
	byte[] test1 = new byte[]
	{ 0, 0, 0, 4, // version
			14, 14, 14, 14, 14, 14, 14, 14,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 0, // message number
			0, 0, 0, 1,// kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1,//number of packets
			66, 66, 66 // dummy
	};

	byte[] test2 = new byte[]
	{ 0, 0, 0, 4, // version
			14, 14, 14, 14, 14, 14, 14, 14,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 1, // message number
			0, 0, 0, 2,// kind (different)
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1,//number of packets
			66, 66 }; // dummy

	byte[] test3 = new byte[]
	{ 0, 0, 0, 4,// version
			14, 14, 14, 14, 14, 14, 14, 15,//different clusterid
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 2, // message number
			0, 0, 0, 2, // kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1 //number of packets
	};

	byte[] test4 = new byte[]
	{ 0, 0, 0, 8, ////different version
			14, 14, 14, 14, 14, 14, 14, 14, // cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 3, // message number
			0, 0, 0, 2, // kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1 //number of packets
	};

	byte[] test5 = new byte[]
	{ 22, 22, // dummy
			0, 0, 0, 4,// version
			14, 14, 14, 14, 14, 14, 14, 14,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 0, // message number
			0, 0, 0, 1, //the first, but shifted
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1,//number of packets
			66, 66, 66 // dummy
	};

	byte[] test6 = new byte[]
	{ 0, 0, 0, 4, // version
			-77, -77, -77, -77, -77, -77, -77, -77,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 5, // message number
			0, 0, 0, 1, // kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1,//number of packets
			66, 66, 66 // dummy
	};

	byte[] test7 = new byte[]
	{ 0, 0, 0, 4, // version
			-77, -77, -77, -77, -77, -77, -77, -77,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 5, // message number
			0, 0, 0, 1, // kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 2,//number of packets
			88, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 77 // dummy 30+3 long
	};

	byte[] test8 = new byte[]
	{ 0, 0, 0, 4, // version
			-77, -77, -77, -77, -77, -77, -77, -77,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 5, // message number
			0, 0, 0, 1, // kind
			0, 0, 0, 2,//current packet number
			0, 0, 0, 2,//number of packets
			11, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 22 // dummy
	};

	byte[] dataarray = new byte[]
	{ 88, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 77, //data part of test7 30 elements 
			11, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 66, 22, //datapart of test8
	};

	byte[] header = new byte[]
	{ 0, 0, 0, 4, // version
			-77, -77, -77, -77, -77, -77, -77, -77,// cluster island
			10, 10, 10, 10, 10, 10, 10, 10,// dynamic node ID
			0, 0, 0, 5, // message number
			0, 0, 0, 1, // kind
			0, 0, 0, 1,//current packet number
			0, 0, 0, 1 //number of packets 
	};

	@Test
	public void testUDPPackets()
	{
		RawMessage packet = new RawMessage(test1, 0, HEADER_SIZE);
		assertEquals(packet.getVersion(), 4);
		assertEquals(packet.getKind(), 1);
		assertEquals(packet.getCurrentPacketNumber(), 1);
		assertEquals(packet.getNumberOfPackets(), 1);

		//and shifts
		packet = new RawMessage(test5, 2, HEADER_SIZE);
		assertEquals(packet.getVersion(), 4);
		assertEquals(packet.getKind(), 1);
		assertEquals(packet.getCurrentPacketNumber(), 1);
		assertEquals(packet.getNumberOfPackets(), 1);

		//assertMatches
		assertTrue(packet.matches(new RawMessage(test1, 0)));
		assertTrue(packet.matches(new RawMessage(test2, 0)));
		assertFalse(packet.matches(new RawMessage(test3, 0)));
		assertFalse(packet.matches(new RawMessage(test4, 0)));
		assertFalse(packet.matches(new RawMessage(test5, 0)));
		assertTrue(packet.matches(new RawMessage(test5, 2)));

		//assertSame
		assertArrayEquals(test1, new RawMessage(test1, 0, test1.length).toRawByteArray());
		assertArrayEquals(test5, new RawMessage(test5, 0, test5.length).toRawByteArray());
		assertArrayEquals(test1, new RawMessage(test5, 2, test1.length).toRawByteArray());

		assertArrayEquals(test6, new RawMessage(test6, 0, test6.length).toRawByteArray());
	}

	@Test
	public void testLargeClusterIsland()
	{
		final RawMessage packet = new RawMessage(1, null);
		packet.setSenderTransportData(4, 15752260825806512L, 123456L, 12);
		Assert.assertTrue(packet.getClusterIslandPK() == 15752260825806512L);
		final byte[] packetByteArray = packet.toRawByteArray();
		assertTrue(packet.getClusterIslandPK() == new RawMessage(packetByteArray, 0, packetByteArray.length).getClusterIslandPK());
	}

	@Test
	public void testJoinUDPPackets()
	{
		final RawMessage part_one = new RawMessage(test7, 0, test7.length);
		final RawMessage part_two = new RawMessage(test8, 0);
		assertEquals(part_one.getCurrentPacketNumber(), 1);
		assertEquals(part_one.getNumberOfPackets(), 2);
		assertEquals(part_two.getCurrentPacketNumber(), 2);
		assertEquals(part_two.getNumberOfPackets(), 2);

		final List<RawMessage> splittedList = new ArrayList<RawMessage>();
		splittedList.add(part_two); //add reversed
		splittedList.add(part_one);

		final RawMessage packet = part_one.join(splittedList);
		assertEquals(packet.getKind(), part_one.getKind());
		assertEquals(packet.getCurrentPacketNumber(), 1);
		assertEquals(packet.getNumberOfPackets(), 1);
		assertEquals(packet.getMessageNumber(), part_one.getMessageNumber());
		assertEquals(packet.getMessageNumber(), part_two.getMessageNumber());
		assertArrayEquals(packet.getData(), dataarray);
	}

	@Test
	public void testSplitUDPPackets1() //2*30byte parts
	{
		final byte[] data = new byte[header.length + dataarray.length];
		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(dataarray, 0, data, header.length, dataarray.length);

		final RawMessage bigone = new RawMessage(data, 0);
		final List<RawMessage> messages = bigone.split(66);
		if (messages == null || messages.isEmpty())
		{
			fail("should be splitted");
		}
		for (final RawMessage msg : messages)
		{
			assertEquals(bigone.getKind(), msg.getKind());
			assertEquals(bigone.getMessageNumber(), msg.getMessageNumber());
			assertEquals(msg.getNumberOfPackets(), 2);
			if (msg.getCurrentPacketNumber() == 1)
			{
				assertArrayEquals(msg.toRawByteArray(), test7);
			}
			if (msg.getCurrentPacketNumber() == 2)
			{
				assertArrayEquals(msg.toRawByteArray(), test8);
			}
		}
	}

	@Test
	public void testSplitUDPPackets2()
	{
		final byte[] data = new byte[header.length + dataarray.length];
		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(dataarray, 0, data, header.length, dataarray.length);

		final RawMessage bigone = new RawMessage(data, 0);
		final List<RawMessage> messages = bigone.split(60); //2*24 + 1*12 parts
		assertEquals(messages.size(), 3);
		assertEquals(messages.get(0).getData().length, 24);
		assertEquals(messages.get(2).getData().length, 12);
		assertEquals(messages.get(2).getData()[11], 22);
	}

}
