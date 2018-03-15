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
package de.hybris.platform.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class LegacyPKTest extends HybrisJUnit4Test
{

	//3.1 style pk(UUID), bit 43 is 1
	private final String LEGACY_31_USER_PK1 = "1135001077316720";
	//3.1 style pk(UUID), bit 43 is 0
	private final String LEGACY_31_USER_PK2 = "1130603030805627";

	//4.x style pk(counter based), bit 43 is 1
	private final String RELEASE_4_USER_PK = "10106335985668";
	//5.x style pk(counter based), bit 43 is 1
	private final String RELEASE_5_USER_PK = "8796093054980";

	private PK userPK1;
	private PK userPK2;

	private PK userPK4;
	private PK userPK5;

	@Before
	public void createUserPks()
	{
		userPK1 = PK.parse(LEGACY_31_USER_PK1);
		userPK2 = PK.parse(LEGACY_31_USER_PK2);

		userPK4 = PK.parse(RELEASE_4_USER_PK);
		userPK5 = PK.parse(RELEASE_5_USER_PK);
	}

	@Test
	public void testLegacyPKWithoutDetection()
	{
		assertTrue(userPK1.isCounterBased(false));
		assertFalse(userPK2.isCounterBased(false));
		assertTrue(userPK4.isCounterBased(false));
		assertTrue(userPK5.isCounterBased(false));
	}

	@Test
	public void testLegacyPKWithDetection()
	{
		assertFalse(userPK1.isCounterBased(true));
		//		assertEquals(4, userPK1.getTypeCode());

		assertFalse(userPK2.isCounterBased(true));
		//		assertEquals(4, userPK2.getTypeCode());

		assertTrue(userPK4.isCounterBased(true));
		assertEquals(4, userPK4.getTypeCode());

		assertTrue(userPK5.isCounterBased(true));
		assertEquals(4, userPK5.getTypeCode());
	}

}
