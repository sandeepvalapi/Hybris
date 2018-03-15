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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.EJBTools;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;


@IntegrationTest
public class EJBToolsTest extends HybrisJUnit4TransactionalTest
{

	PK pk1 = PK.createFixedUUIDPK(0, 1);
	PK pk2 = PK.createFixedUUIDPK(0, 2);
	PK pk3 = PK.createFixedUUIDPK(0, 3);
	PK pk4 = PK.createFixedUUIDPK(0, 4);
	PK pk5 = PK.createFixedUUIDPK(0, 5);
	PK pk6 = PK.createFixedUUIDPK(0, 6);
	String pk1s = pk1.getLongValueAsString();
	String pk2s = pk2.getLongValueAsString();
	String pk3s = pk3.getLongValueAsString();
	String pk4s = pk4.getLongValueAsString();
	String pk5s = pk5.getLongValueAsString();
	String pk6s = pk6.getLongValueAsString();
	String C = ",";
	String NULL = "null";

	String getPksString(final PK[] coll)
	{
		final ArrayList x = new ArrayList();
		for (int i = 0; i < coll.length; i++)
		{
			x.add(new ItemDummy(coll[i]));
		}
		return EJBTools.getPKCollectionString(x);
	}

	@Test
	public void testPkStringCollection() throws Exception
	{
		assertNull(EJBTools.getPKCollectionString(null));
		assertNotNull(EJBTools.getPKCollectionString(Collections.EMPTY_SET));
		assertFalse("".equals(EJBTools.getPKCollectionString(Collections.EMPTY_SET)));
		assertEquals(C + pk1s + C, getPksString(new PK[]
		{ pk1 }));
		assertEquals(C + pk1s + C + pk2s + C, getPksString(new PK[]
		{ pk1, pk2 }));
		assertEquals(C + pk1s + C + pk2s + C + pk3s + C, getPksString(new PK[]
		{ pk1, pk2, pk3 }));
		//assertEquals(C+pk1+C+NULL+C+pk3+C, getPksString(new PK[]{pk1,null,pk3}));

		assertEquals(C + pk1s + C, EJBTools.addPKToPKCollectionString(null, EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals(C + pk1s + C, EJBTools.addPKToPKCollectionString("", EJBTools.getPK(new ItemDummy(pk1))));
		//assertEquals(null, EJBTools.addItemToPKCollectionString("", new ItemDummy(null)));
		assertEquals(C + pk1s + C + pk2s + C, EJBTools.addPKToPKCollectionString(C + pk1s + C, EJBTools.getPK(new ItemDummy(pk2))));
		assertEquals(C + pk1s + C + pk1s + C, EJBTools.addPKToPKCollectionString(C + pk1s + C, EJBTools.getPK(new ItemDummy(pk1))));
		//assertEquals(",pk1,null,", EJBTools.addItemToPKCollectionString(",pk1,", new ItemDummy(null)));

		assertEquals(null, removePKFromPKCollectionString(null, EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals(null, removePKFromPKCollectionString("", EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals("", removePKFromPKCollectionString(C + pk1s + C, EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals(C + pk2s + C, removePKFromPKCollectionString(C + pk1s + C + pk2s + C, EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals(C + pk1s + C, removePKFromPKCollectionString(C + pk1s + C + pk2s + C, EJBTools.getPK(new ItemDummy(pk2))));
		assertEquals(null, removePKFromPKCollectionString(C + pk1s + C + pk2s + C, EJBTools.getPK(new ItemDummy(pk3))));
		assertEquals(
				C + pk2s + C + pk3s + C + pk4s + C + pk5s + C,
				removePKFromPKCollectionString(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C,
						EJBTools.getPK(new ItemDummy(pk1))));
		assertEquals(
				C + pk1s + C + pk2s + C + pk4s + C + pk5s + C,
				removePKFromPKCollectionString(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C,
						EJBTools.getPK(new ItemDummy(pk3))));
		assertEquals(C + pk1s + C + pk2s + C + pk4s + C,
				removePKFromPKCollectionString(C + pk1s + C + pk2s + C + pk4s + C + pk5s + C, EJBTools.getPK(new ItemDummy(pk5))));
		assertEquals(
				null,
				removePKFromPKCollectionString(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C,
						EJBTools.getPK(new ItemDummy(pk6))));

		assertFalse(containsItem(null, new ItemDummy(pk1)));
		assertFalse(containsItem("", new ItemDummy(pk1)));
		assertTrue(containsItem(C + pk1s + C, new ItemDummy(pk1)));
		assertTrue(containsItem(C + pk1s + C + pk2s + C, new ItemDummy(pk1)));
		assertTrue(containsItem(C + pk1s + C + pk2s + C, new ItemDummy(pk2)));
		assertFalse(containsItem(C + pk1s + C + pk2s + C, new ItemDummy(pk3)));
		assertTrue(containsItem(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C, new ItemDummy(pk1)));
		assertTrue(containsItem(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C, new ItemDummy(pk3)));
		assertFalse(containsItem(C + pk1s + C + pk2s + C + pk3s + C + pk4s + C + pk5s + C, new ItemDummy(pk6)));
	}

	@Test
	public void testConvertToFromDatabase()
	{
		final String[] testStrings = new String[]
		{ null, "", "_", "__", "hallo" };
		for (int i = 0; i < testStrings.length; i++)
		{
			final String realString = testStrings[i];
			final String databaseString = EJBTools.convertToDatabase(realString);
			assertEquals(realString, EJBTools.convertFromDatabase(databaseString));
		}
	}


	private static String removePKFromPKCollectionString(String pkCollectionString, final PK itemPk)
	{
		if (pkCollectionString == null || pkCollectionString.length() == 0)
		{
			return null;
		}

		final int start = pkCollectionString.indexOf(itemPk.toString());
		if (start < 0)
		{
			return null;
		}
		pkCollectionString = pkCollectionString.substring(0, start)
				+ pkCollectionString.substring(start + itemPk.toString().length() + 1);
		return pkCollectionString.length() > 1 ? pkCollectionString : "";
	}

	private static boolean containsItem(final String pkCollectionString, final ItemRemote item)
	{
		final PK itemPk = EJBTools.getPK(item);
		return (pkCollectionString != null && pkCollectionString.indexOf(itemPk.toString() + ',') >= 0);
	}


}
