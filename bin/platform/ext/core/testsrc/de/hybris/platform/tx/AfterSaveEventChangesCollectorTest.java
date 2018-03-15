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
package de.hybris.platform.tx;

import static de.hybris.platform.tx.AfterSaveEventUtils.createEventsFromChanges;
import static de.hybris.platform.tx.AfterSaveEventUtils.decodeChangeTypes;
import static de.hybris.platform.tx.AfterSaveEventUtils.decodePK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests the TransactionEntityChangesCollector
 */
@UnitTest
public class AfterSaveEventChangesCollectorTest
{

	private AfterSaveEventChangesCollector transactionEntityChangesCollector;

	private PK pk1;
	private PK pk2;
	private PK pk3;

	@Before
	public void setUp()
	{
		transactionEntityChangesCollector = new AfterSaveEventChangesCollector();

		pk1 = PK.createFixedUUIDPK(10, 1000l);
		pk2 = PK.createFixedUUIDPK(20, 2000l);
		pk3 = PK.createFixedUUIDPK(30, 3000l);
	}

	//CREATE
	@Test
	public void testCreate()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.CREATE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.CREATE);
	}

	private void testEventType(final AfterSaveEventChangesCollector collector, final int type)
	{
		final byte[][] changes = collector.getEncodedChanges();
		assertNotNull(changes);
		assertEquals(1, changes.length);
		assertEquals(pk1, decodePK(changes[0]));
		assertEquals(type, decodeChangeTypes(changes[0]));
	}

	//CREATE + UPDATE(1..*) --> CREATE
	@Test
	public void testCreateUpdate()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.CREATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.CREATE);
	}

	//UPDATE(1..*) + CREATE --> CREATE
	@Test
	public void testUpdateCreateWrongOrder()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.CREATE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.CREATE);
	}

	//CREATE + UPDATE(1..*) + REMOVE --> NULL
	@Test
	public void testCreateUpdateRemove()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.CREATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.REMOVE);
		assertNull(transactionEntityChangesCollector.getEncodedChanges());
	}

	//UPDATE(1..*)
	@Test
	public void testUpdate()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.UPDATE);
	}

	//UPDATE(1..*) + REMOVE --> REMOVE
	@Test
	public void testUpdateRemove()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.REMOVE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.REMOVE);
	}

	//REMOVE
	@Test
	public void testRemove()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.REMOVE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.REMOVE);
	}

	//REMOVE + UPDATE(1..*) --> REMOVE
	@Test
	public void testRemoveUpdateWrongOrder()
	{
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.REMOVE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		testEventType(transactionEntityChangesCollector, AfterSaveEvent.REMOVE);
	}

	//mixed situations
	@Test
	public void testMultiEvents()
	{
		//CREATE 3 items
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.CREATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk1, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk2, AfterSaveEvent.CREATE);
		transactionEntityChangesCollector.collect(pk3, AfterSaveEvent.CREATE);
		assertEquals(new HashSet(Arrays.asList(//
				new AfterSaveEvent(pk1, AfterSaveEvent.CREATE), //
				new AfterSaveEvent(pk2, AfterSaveEvent.CREATE), //
				new AfterSaveEvent(pk3, AfterSaveEvent.CREATE))),// 
				new HashSet(createEventsFromChanges(transactionEntityChangesCollector.getEncodedChanges(), false)));

		//UPDATE item2 and REMOVE item3
		transactionEntityChangesCollector.collect(pk2, AfterSaveEvent.UPDATE);
		transactionEntityChangesCollector.collect(pk3, AfterSaveEvent.REMOVE);
		assertEquals(new HashSet(Arrays.asList(//
				new AfterSaveEvent(pk1, AfterSaveEvent.CREATE), //
				new AfterSaveEvent(pk2, AfterSaveEvent.CREATE))),// 
				new HashSet(createEventsFromChanges(transactionEntityChangesCollector.getEncodedChanges(), false)));

		//clears all saved events
		transactionEntityChangesCollector.clear();
		assertNull(transactionEntityChangesCollector.getEncodedChanges());
	}


	//wrong parameter for AfterSaveEvent
	@Test
	public void testInvalidAfterSaveEvent()
	{
		try
		{
			new AfterSaveEvent(pk1, -1);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//expected
		}
	}
}
