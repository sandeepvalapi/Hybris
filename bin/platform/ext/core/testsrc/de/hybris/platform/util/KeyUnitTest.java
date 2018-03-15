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
package de.hybris.platform.util;

import de.hybris.bootstrap.annotations.UnitTest;

import junit.framework.Assert;

import org.junit.Test;


@UnitTest
public class KeyUnitTest
{
	@Test
	public void testEqualForNull()
	{
		final Key key1 = Key.create(null, null);
		final Key key2 = Key.create(null, null);
		Assert.assertEquals(key1, key2);
	}

	@Test
	public void testNotEqualForEmpty()
	{
		final Key key1 = Key.create(" ", null);
		final Key key2 = Key.create(null, null);
		Assert.assertFalse(key1.equals(key2));

		final Key key3 = Key.create("", null);
		final Key key4 = Key.create(null, null);
		Assert.assertFalse(key3.equals(key4));
	}

	@Test
	public void testEqual()
	{
		final Key key1 = Key.create(" ", null);
		final Key key2 = Key.create(" ", null);
		Assert.assertTrue(key1.equals(key2));

		final Key key3 = Key.create("", null);
		final Key key4 = Key.create("", null);
		Assert.assertTrue(key3.equals(key4));

		final Key key5 = Key.create("koka", null);
		final Key key6 = Key.create("koka", null);
		Assert.assertTrue(key5.equals(key6));
	}

	@Test
	public void testNotEqualForBlank()
	{
		final Key key1 = Key.create(" ", null);
		final Key key2 = Key.create("", null);
		Assert.assertFalse(key1.equals(key2));

		final Key key3 = Key.create("", null);
		final Key key4 = Key.create("	", null);
		Assert.assertFalse(key3.equals(key4));
	}

	@Test
	public void testNotEqual()
	{
		final Key key1 = Key.create("ala", null);
		final Key key2 = Key.create("kola", null);
		Assert.assertFalse(key1.equals(key2));
	}


	@Test
	public void testNotEqualWithBlankDefault()
	{
		final Key key1 = Key.create("", " ");
		final Key key2 = Key.create("", null);
		Assert.assertFalse(key1.equals(key2));

		final Key key3 = Key.create("	", null);
		final Key key4 = Key.create("	", "");
		Assert.assertFalse(key3.equals(key4));
	}

	@Test
	public void testNotEqualBlankWithDefault()
	{
		final Key key1 = Key.create("ala", " ");
		final Key key2 = Key.create("ala", null);
		Assert.assertFalse(key1.equals(key2));

		final Key key3 = Key.create("ala", null);
		final Key key4 = Key.create("ala", "ala");
		Assert.assertFalse(key3.equals(key4));

		final Key key5 = Key.create("ala", "");
		final Key key6 = Key.create("ala", "	");
		Assert.assertFalse(key5.equals(key6));
	}


	@Test
	public void testEqualWitHDefault()
	{

		final Key key5 = Key.create("koka", "juka");
		final Key key6 = Key.create("koka", "juka");
		Assert.assertTrue(key5.equals(key6));
	}

}
