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
package de.hybris.platform.test.tx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveEventUtils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests the Utility helper EntityChangesEncodingUtils.
 */
@UnitTest
public class AfterSaveEventUtilsTest
{

	private PK pk1;
	private PK pk2;
	private PK pk3;

	@Before
	public void setUp()
	{
		pk1 = PK.createFixedUUIDPK(10, 1000l);
		pk2 = PK.createFixedUUIDPK(20, 2000l);
		pk3 = PK.createFixedUUIDPK(30, 3000l);
	}

	@Test
	public void testDecodeChangeTypes()
	{
		final byte[] encodedChanges = new byte[9];
		encodedChanges[8] = (byte) (AfterSaveEvent.CREATE & 0xFF);
		final int types = AfterSaveEventUtils.decodeChangeTypes(encodedChanges);
		assertEquals(encodedChanges[8], types);
	}

	@Test
	public void testDecodePK()
	{
		//a valid pk
		final long testPK = 8796095381534l;
		final byte[] encodedChanges = new byte[8];
		for (int i = 7; i >= 0; i--)
		{
			encodedChanges[7 - i] = (byte) (testPK >> (i * 8) & 0xFF);
		}
		final PK pk = AfterSaveEventUtils.decodePK(encodedChanges);
		assertEquals(testPK, pk.getLongValue());
	}

	@Test
	public void testEncodeChanges()
	{
		final byte[] changes = AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.CREATE);
		final long pk1Long = pk1.getLongValue();
		for (int i = 7; i >= 0; i--)
		{
			final byte byteValue = (byte) (pk1Long >> (i * 8) & 0xFF);
			assertEquals(changes[7 - i], byteValue);
		}
		assertEquals(changes[8], AfterSaveEvent.CREATE & 0xFF);
	}

	//test the different combinations of CREATE, UPDATE, and REMOVE 
	@Test
	public void testCreateEventsFromChanges()
	{
		//only CREATE
		final byte[][] changes1 = createChanges1();
		Set<AfterSaveEvent> events = new HashSet<AfterSaveEvent>(//
				AfterSaveEventUtils.createEventsFromChanges(changes1, true));
		Set<AfterSaveEvent> expectedAfterSaveEvents = createCreateSet();
		assertEquals(3, events.size());
		assertTrue(expectedAfterSaveEvents.containsAll(events));
		expectedAfterSaveEvents.clear();

		//CREATE and UPDATE, while removeUnnecessaryTypes(UPDATE will be removed)
		final byte[][] changes2 = createChanges2();
		events = new HashSet<AfterSaveEvent>(//
				AfterSaveEventUtils.createEventsFromChanges(changes2, true));
		expectedAfterSaveEvents = createCreateSet();
		assertEquals(3, events.size());
		assertTrue(expectedAfterSaveEvents.containsAll(events));
		expectedAfterSaveEvents.clear();

		//UPDATE and REMOVE, while removeUnnecessaryTypes(UPDATE will be removed)
		final byte[][] changes3 = createChanges3();
		events = new HashSet<AfterSaveEvent>(//
				AfterSaveEventUtils.createEventsFromChanges(changes3, true));
		expectedAfterSaveEvents = createRemoveAllSet();
		assertEquals(3, events.size());
		assertTrue(expectedAfterSaveEvents.containsAll(events));
		expectedAfterSaveEvents.clear();

		//CREATE, UPDATE, and REMOVE, while removeUnnecessaryTypes(UPDATE will be removed)
		final byte[][] changes4 = createChanges4();
		events = new HashSet<AfterSaveEvent>(//
				AfterSaveEventUtils.createEventsFromChanges(changes4, true));
		expectedAfterSaveEvents.addAll(createCreateSet());
		expectedAfterSaveEvents.addAll(createRemoveSet());
		assertEquals(5, events.size());
		assertTrue(expectedAfterSaveEvents.containsAll(events));
		expectedAfterSaveEvents.clear();

		//CREATE, UPDATE, and REMOVE, while NOT removeUnnecessaryTypes(UPDATE will remain)
		events = new HashSet<AfterSaveEvent>(//
				AfterSaveEventUtils.createEventsFromChanges(changes4, false));
		expectedAfterSaveEvents.addAll(createCreateSet());
		expectedAfterSaveEvents.addAll(createUpdateSet());
		expectedAfterSaveEvents.addAll(createRemoveSet());
		assertEquals(7, events.size());
		assertTrue(expectedAfterSaveEvents.containsAll(events));
		expectedAfterSaveEvents.clear();
	}

	//3 CREATE
	private byte[][] createChanges1()
	{
		return new byte[][]
		{ AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk3, AfterSaveEvent.CREATE) };
	}

	//3 CREATE, and 2 UPDATE
	private byte[][] createChanges2()
	{
		return new byte[][]
		{ AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk3, AfterSaveEvent.CREATE) };
	}

	//2 UPDATE, and 3 REMOVE
	private byte[][] createChanges3()
	{
		return new byte[][]
		{ AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.REMOVE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.REMOVE),
				AfterSaveEventUtils.encodeChanges(pk3, AfterSaveEvent.REMOVE) };
	}

	//3 CREATE, and 2 UPDATE, and 2 REMOVE
	private byte[][] createChanges4()
	{
		return new byte[][]
		{ AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk2, AfterSaveEvent.UPDATE),
				AfterSaveEventUtils.encodeChanges(pk3, AfterSaveEvent.CREATE),
				AfterSaveEventUtils.encodeChanges(pk1, AfterSaveEvent.REMOVE),
				AfterSaveEventUtils.encodeChanges(pk3, AfterSaveEvent.REMOVE), };
	}

	//3 CREATE
	private Set<AfterSaveEvent> createCreateSet()
	{
		final Set<AfterSaveEvent> expectedAfterSaveEvents = new HashSet<AfterSaveEvent>(3);
		final AfterSaveEvent event1 = new AfterSaveEvent(pk1, AfterSaveEvent.CREATE);
		final AfterSaveEvent event2 = new AfterSaveEvent(pk2, AfterSaveEvent.CREATE);
		final AfterSaveEvent event3 = new AfterSaveEvent(pk3, AfterSaveEvent.CREATE);
		expectedAfterSaveEvents.add(event1);
		expectedAfterSaveEvents.add(event2);
		expectedAfterSaveEvents.add(event3);
		return expectedAfterSaveEvents;
	}

	//2 UPDATE
	private Set<AfterSaveEvent> createUpdateSet()
	{
		final Set<AfterSaveEvent> expectedAfterSaveEvents = new HashSet<AfterSaveEvent>(3);
		final AfterSaveEvent event2 = new AfterSaveEvent(pk1, AfterSaveEvent.UPDATE);
		final AfterSaveEvent event4 = new AfterSaveEvent(pk2, AfterSaveEvent.UPDATE);
		expectedAfterSaveEvents.add(event2);
		expectedAfterSaveEvents.add(event4);
		return expectedAfterSaveEvents;
	}

	//2 REMOVE
	private Set<AfterSaveEvent> createRemoveSet()
	{
		final Set<AfterSaveEvent> expectedAfterSaveEvents = new HashSet<AfterSaveEvent>(3);
		final AfterSaveEvent event2 = new AfterSaveEvent(pk1, AfterSaveEvent.REMOVE);
		final AfterSaveEvent event5 = new AfterSaveEvent(pk3, AfterSaveEvent.REMOVE);
		expectedAfterSaveEvents.add(event2);
		expectedAfterSaveEvents.add(event5);
		return expectedAfterSaveEvents;
	}

	//3 REMOVE
	private Set<AfterSaveEvent> createRemoveAllSet()
	{
		final Set<AfterSaveEvent> expectedAfterSaveEvents = new HashSet<AfterSaveEvent>(3);
		final AfterSaveEvent event2 = new AfterSaveEvent(pk1, AfterSaveEvent.REMOVE);
		final AfterSaveEvent event3 = new AfterSaveEvent(pk2, AfterSaveEvent.REMOVE);
		final AfterSaveEvent event5 = new AfterSaveEvent(pk3, AfterSaveEvent.REMOVE);
		expectedAfterSaveEvents.add(event2);
		expectedAfterSaveEvents.add(event3);
		expectedAfterSaveEvents.add(event5);
		return expectedAfterSaveEvents;
	}

}
