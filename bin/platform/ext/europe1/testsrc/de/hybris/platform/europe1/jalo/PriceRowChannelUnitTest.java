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
package de.hybris.platform.europe1.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.europe1.enums.PriceRowChannel;

import org.junit.Test;


@UnitTest
public class PriceRowChannelUnitTest
{
	@Test
	public void testChannelDynamicEnums()
	{
		final PriceRowChannel desktopChannel = PriceRowChannel.DESKTOP;
		final PriceRowChannel desktopChannel1 = PriceRowChannel.DESKTOP;
		final PriceRowChannel mobileChannel = PriceRowChannel.MOBILE;
		assertNotNull(mobileChannel);
		assertEquals(desktopChannel, desktopChannel1);
		assertEquals(PriceRowChannel.valueOf("desktop"), PriceRowChannel.DESKTOP);
		assertEquals(PriceRowChannel.valueOf("mobile"), PriceRowChannel.valueOf("mobile"));
		assertNotSame(PriceRowChannel.valueOf("desktop"), PriceRowChannel.valueOf("mobile"));
	}
}
