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
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Tests {@link LazyLoadItemList}.
 */
@IntegrationTest
public class LazyLoadItemListTest extends HybrisJUnit4Test
{


	@Test
	public void testLazyLoadOnRemovedItem() throws ConsistencyCheckException
	{
		final Country myCountry = C2LManager.getInstance().createCountry("thalerland");
		final LazyLoadItemList<Country> list = new LazyLoadItemList<Country>(null, Collections.singletonList(myCountry.getPK()), 2);
		assertEquals("List contains not exactly one element", 1, list.size());
		myCountry.remove();
		assertEquals("List contains not exactly one element", 1, list.size());
		assertNull("Item was removed before access to list, so List element must be null by contract but isn't", list.iterator()
				.next());
	}

	@Test
	public void testLazyList() throws IOException, ClassNotFoundException
	{

		new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)),
				LazyLoadItemList.DEFAULT_PREFETCH_SIZE).get(0);

		new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)), 0).get(0);

		new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)), -1000).get(0);

		final LazyLoadItemList l3 = new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)), 0);
		l3.get(0);

		final LazyLoadItemList l4 = new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)), -1);
		l4.get(0);

		final LazyLoadItemList l5 = new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)), -100);
		l5.get(0);

		final String current = Config.getParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY);
		try
		{
			Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, Integer.toString(0));

			new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)),
					LazyLoadItemList.DEFAULT_PREFETCH_SIZE).get(0);

			Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, Integer.toString(-1));

			new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)),
					LazyLoadItemList.DEFAULT_PREFETCH_SIZE).get(0);

			Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, Integer.toString(-2000));

			new LazyLoadItemList(null, Collections.singletonList(PK.createFixedUUIDPK(1, 123456789l)),
					LazyLoadItemList.DEFAULT_PREFETCH_SIZE).get(0);
		}
		finally
		{
			Config.setParameter(LazyLoadItemList.PREFETCH_SIZE_PROPERTY, current);

		}
	}


	/**
	 * Checks if before and after serialization <code>LazyLoadItemList.getPageBuffer</code> is empty or not.
	 */
	@Test
	public void testLazyListDeserialize() throws IOException, ClassNotFoundException
	{
		final LazyLoadItemList originalWithNullLanguage = new LazyLoadItemList(null, Collections.singletonList(PK
				.createFixedUUIDPK(1, 123456789l)), 100);

		final User user = jaloSession.getSessionContext().getUser();

		final MonitoredLazyLoadItemList originalWithNullLangMonitored = new MonitoredLazyLoadItemList(null,
				Collections.singletonList(user.getPK()), 100);

		final LazyLoadItemList originalWithNullLanguageNotMonitored = new LazyLoadItemList(null, Collections.singletonList(user
				.getPK()), 100);

		assertEquals("", user, originalWithNullLangMonitored.get(0)); // loads page buffer

		Assert.assertTrue("After get(i) page buffer should be filled  ", originalWithNullLangMonitored.isNotEmptyPageBuffer());

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);

		//write lists
		oos.writeObject(originalWithNullLanguage);
		oos.writeObject(originalWithNullLangMonitored);
		oos.writeObject(originalWithNullLanguageNotMonitored);

		oos.close();

		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		//read them again
		final LazyLoadItemList deserializedWithNullLanguage = (LazyLoadItemList) ois.readObject();
		final MonitoredLazyLoadItemList deserializedWithNullLanguageMonitored = (MonitoredLazyLoadItemList) ois.readObject();
		final LazyLoadItemList deserializedWithNullLanguageNotMonitored = (LazyLoadItemList) ois.readObject();

		ois.close();

		assertNull("", originalWithNullLanguage.get(0));
		assertNull("", deserializedWithNullLanguage.get(0));

		deserializedWithNullLanguageNotMonitored.set(0, null);

		Assert.assertFalse("After set(i,v) page buffer should not be filled  ",
				deserializedWithNullLanguageMonitored.isNotEmptyPageBuffer());
		assertEquals("", user, deserializedWithNullLanguageMonitored.get(0)); // loads page buffer again

		Assert.assertTrue("After get(i) page buffer should  be filled  ",
				deserializedWithNullLanguageMonitored.isNotEmptyPageBuffer());
	}
}
