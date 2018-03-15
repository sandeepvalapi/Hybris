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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.migration.MigrationUtilities;
import de.hybris.platform.util.migration.OldPK;
import de.hybris.platform.util.migration.PKMapper;
import de.hybris.platform.util.migration.TypecodeMapper;

import junit.framework.Assert;

import org.junit.Test;


@UnitTest
public class MigrationUtilitiesTest
{
	@Test
	public void testPKUniqueness()
	{
		// works only for PK where creation time is different or random part differs in last 4 bits

		final PK pk1 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-91");
		final PK pk2 = MigrationUtilities.convertOldPK("SThsPK6mTexUfxAMPYx068-91");

		Assert.assertFalse(pk1.equals(pk2));
	}

	@Test
	public void testTypecodeMapper()
	{
		final PK pk1_1 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-91");
		final PK pk2_1 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-92");

		final TypecodeMapper mapper = new TypecodeMapper()
		{
			@Override
			public int mapTypecode(final int typecode)
			{
				if (typecode == 91)
				{
					return 123;
				}
				else
				{
					return typecode;
				}
			}
		};
		MigrationUtilities.registerTypecodeMapper(mapper);

		final PK pk1_2 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-91");
		final PK pk2_2 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-92");

		Assert.assertTrue(MigrationUtilities.unregisterTypecodeMapper(mapper));

		final PK pk1_3 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-91");
		final PK pk2_3 = MigrationUtilities.convertOldPK("gS0HZK6mTexUfxAMPYx068-92");

		Assert.assertTrue(pk1_1.equals(pk1_3));
		Assert.assertTrue(pk1_1.getTypeCode() == 91);
		Assert.assertTrue(pk1_3.getTypeCode() == 91);

		Assert.assertFalse(pk1_1.equals(pk1_2));
		Assert.assertFalse(pk1_2.equals(pk1_3));
		Assert.assertTrue(pk1_2.getTypeCode() == 123);

		Assert.assertTrue(pk2_1.equals(pk2_3));
		Assert.assertTrue(pk2_1.getTypeCode() == 92);
		Assert.assertTrue(pk2_3.getTypeCode() == 92);

		Assert.assertTrue(pk2_1.equals(pk2_2));
		Assert.assertTrue(pk2_2.equals(pk2_3));
		Assert.assertTrue(pk2_2.getTypeCode() == 92);
	}

	@Test
	public void testPKMapper()
	{
		// 	random part is same in last 8 bits, so they mapped to same pk :-(
		final PK pk1_1 = MigrationUtilities.convertOldPK("BkclWkNLvhHrnkQfYAlBEd-334");
		final PK pk2_1 = MigrationUtilities.convertOldPK("xmO1hlNLvhHrnkQfYAlBEd-334");

		Assert.assertTrue(pk1_1.equals(pk2_1));

		final PK mappedPK = PK.createUUIDPK(334);

		// so register a pk mapper for one of them
		final PKMapper mapper = new PKMapper()
		{
			@Override
			public PK mapPK(final OldPK oldPK)
			{
				if (oldPK.toString().equals("xmO1hlNLvhHrnkQfYAlBEd-334"))
				{
					return mappedPK;
				}
				else
				{
					return null;
				}
			}
		};
		MigrationUtilities.registerPKMapper(mapper);

		// same again
		final PK pk1_2 = MigrationUtilities.convertOldPK("BkclWkNLvhHrnkQfYAlBEd-334");
		final PK pk2_2 = MigrationUtilities.convertOldPK("xmO1hlNLvhHrnkQfYAlBEd-334");

		Assert.assertTrue(pk1_1.equals(pk1_2));
		Assert.assertFalse(pk2_1.equals(pk2_2));
		Assert.assertTrue(pk2_2.equals(mappedPK));

		Assert.assertTrue(MigrationUtilities.unregisterPKMapper(mapper));
	}

	@Test
	public void testPKStability()
	{
		Assert.assertTrue(MigrationUtilities.isOldPK("admin-4"));
		Assert.assertEquals(PK.parse("1125899906842640"), MigrationUtilities.convertOldPK("admin-4"));

		Assert.assertTrue(MigrationUtilities.isOldPK("P0buIyC2LQkpATPjDHiNia-1"));
		Assert.assertEquals(PK.parse("393280453967199"), MigrationUtilities.convertOldPK("P0buIyC2LQkpATPjDHiNia-1"));
		Assert.assertEquals(PK.parse("393280453967199"), MigrationUtilities.convertOldPK("P0buIyC2LQkpATPjDHiNia-1"));
	}

	@Test
	public void testWrongPK()
	{
		Assert.assertFalse(MigrationUtilities.isOldPK("P0buIyC2LQkpATPjDHiNia-"));
		try
		{
			MigrationUtilities.convertOldPK("P0buIyC2LQkpATPjDHiNia-");
			Assert.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//OK
		}

		Assert.assertFalse(MigrationUtilities.isOldPK("-1"));
		try
		{
			MigrationUtilities.convertOldPK("-1");
			Assert.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//OK
		}

		Assert.assertFalse(MigrationUtilities.isOldPK("P0buIyC2LQkpATPjDHiNia"));
		try
		{
			MigrationUtilities.convertOldPK("P0buIyC2LQkpATPjDHiNia");
			Assert.fail();
		}
		catch (final IllegalArgumentException e)
		{
			//OK
		}
	}
}
