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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Test modification time behaviour when linking items to each other.
 */
@IntegrationTest
public class LinkMarkModifiedTest extends HybrisJUnit4Test
{

	Item src1;
	Item tgt1;
	Item tgt2;
	Item tgt3;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		src1 = UserManager.getInstance().createTitle("src1");
		tgt1 = UserManager.getInstance().createTitle("tgt1");
		tgt2 = UserManager.getInstance().createTitle("tgt2");
		tgt3 = UserManager.getInstance().createTitle("tgt3");
	}

	@Test
	public void testMarkModifiedRemovingSingleElements() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2, tgt3]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt1,  tgt3] ( remove tgt2 ) ---> expect modification mark for src and tgt2, NOT the others! 
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt3));

		tracker.assertSame(tgt1, tgt3);
		tracker.assertModified(src1, tgt2);

		// sanity check
		assertEquals(Arrays.asList(tgt1, tgt3), src1.getLinkedItems(true, "dummyRel", null));
	}

	@Test
	public void testMarkModifiedRemovingSingleElementsRemovalOnly() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2, tgt3]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt1,  tgt3] ( remove tgt2 ) ---> expect modification mark for src and tgt2, NOT the others! 
		src1.removeLinkedItems(true, "dummyRel", null, Arrays.asList(tgt2));

		tracker.assertSame(tgt1, tgt3);
		tracker.assertModified(src1, tgt2);

		// sanity check
		assertEquals(Arrays.asList(tgt1, tgt3), src1.getLinkedItems(true, "dummyRel", null));
	}

	@Test
	public void testMarkModifiedJustReorderingElements() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2, tgt3]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt3, tgt2, tgt1] ( shuffle tgt1 and tgt3 ) ---> expect modification mark for src ONLY! 
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt3, tgt2, tgt1));
		tracker.assertSame(tgt1, tgt2, tgt3);
		tracker.assertModified(src1);

		// sanity check
		assertEquals(Arrays.asList(tgt3, tgt2, tgt1), src1.getLinkedItems(true, "dummyRel", null));
	}

	@Test
	public void testMarkModifiedAddingElements() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2));
		assertEquals(Arrays.asList(tgt1, tgt2), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt1, tgt2, tgt3] ( add tgt3) ---> expect modification mark for src and tgt3 
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		tracker.assertSame(tgt1, tgt2);
		tracker.assertModified(src1, tgt3);

		// sanity check
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));
	}

	@Test
	public void testMarkModifiedAddingElementsAddOnly() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2));
		assertEquals(Arrays.asList(tgt1, tgt2), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt1, tgt2, tgt3] ( add tgt3) ---> expect modification mark for src and tgt3 
		src1.addLinkedItems(true, "dummyRel", null, Arrays.asList(tgt3));
		tracker.assertSame(tgt1, tgt2);
		tracker.assertModified(src1, tgt3);

		// sanity check
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));
	}

	@Test
	public void testMarkModifiedUnchanged() throws ConsistencyCheckException, InterruptedException
	{
		// src ->[ tgt1, tgt2, tgt3]
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));

		final ItemModificationTracker tracker = new ItemModificationTracker();
		tracker.snapShot(src1, tgt1, tgt2, tgt3);

		Thread.sleep(1100); // wait at least one second in order to notice change in Date as MySQL isn't storing mills !!!

		// src ->[ tgt1, tgt2, tgt3] ( nothing changes ) ---> no modification marks 
		src1.setLinkedItems(true, "dummyRel", null, Arrays.asList(tgt1, tgt2, tgt3));
		tracker.assertSame(src1, tgt1, tgt2, tgt3);

		// sanity check
		assertEquals(Arrays.asList(tgt1, tgt2, tgt3), src1.getLinkedItems(true, "dummyRel", null));
	}


	static class ItemModificationTracker
	{
		final List<Map<Item, Date>> lastModifiedTime = new ArrayList<>();

		int snapShot(final Item... items)
		{
			final Map<Item, Date> capture = new HashMap<>(items.length * 2);
			for (final Item item : items)
			{
				capture.put(item, item.getModificationTime());
			}
			lastModifiedTime.add(capture);
			return lastModifiedTime.size() - 1;
		}

		void assertSame(final Item... items)
		{
			assertSame(lastModifiedTime.size() - 1, items);
		}

		void assertSame(final int snapShot, final Item... items)
		{
			for (final Item item : items)
			{
				assertSame(item, snapShot);
			}
		}

		void assertSame(final Item item)
		{
			assertSame(item, lastModifiedTime.size() - 1);
		}


		void assertSame(final Item item, final int snapShot)
		{
			final Date lastTime = lastModifiedTime.get(snapShot).get(item);
			assertNotNull("no modification time captured in snapshot " + snapShot + " for item " + item, lastTime);
			assertEquals("item " + item + " was modified but shouldn't", lastTime.getTime(), item.getModificationTime().getTime());
		}

		void assertModified(final Item... items)
		{
			assertModified(lastModifiedTime.size() - 1, items);
		}

		void assertModified(final int snapShot, final Item... items)
		{
			for (final Item item : items)
			{
				assertModified(item, snapShot);
			}
		}

		void assertModified(final Item item)
		{
			assertModified(item, lastModifiedTime.size() - 1);
		}

		void assertModified(final Item item, final int snapShot)
		{
			final Date lastTime = lastModifiedTime.get(snapShot).get(item);
			assertNotNull("no modification time captured in snapshot " + snapShot + " for item " + item, lastTime);
			assertTrue("item " + item + " wasn't modified but should", lastTime.getTime() < item.getModificationTime().getTime());
		}

	}



}
