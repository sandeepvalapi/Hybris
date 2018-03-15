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
package de.hybris.platform.europe1.channel.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.junit.Test;


/**
 *
 */
@IntegrationTest
public class RetrieveChannelStrategyTest extends ServicelayerTest
{
	@Resource
	private RetrieveChannelStrategy retrieveChannelStrategy;
	protected static final String DETECTED_UI_EXPERIENCE_LEVEL = "UiExperienceService-Detected-Level";
	protected static final String CHANNEL = "channel";

	@Test
	public void testGetChannelForNullContext()
	{
		final SessionContext ctx = null;
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNull(channel);
	}

	@Test
	public void testGetChannelForNullUIExpLevelContext()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNull(channel);
	}

	public void testGetChannelForValidUIExpLevelContext()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final EnumerationValue desktopEnumUIExpLevel = EnumerationManager.getInstance().getEnumerationValue(
				PriceRowChannel._TYPECODE, "desktop");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, desktopEnumUIExpLevel);
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNotNull(channel);
		assertEquals(desktopEnumUIExpLevel.getCode(), channel.getCode());
	}

	@Test
	public void testGetChannelForNullChannelContext()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final EnumerationValue desktopEnumUIExpLevel = EnumerationManager.getInstance().getEnumerationValue(
				PriceRowChannel._TYPECODE, "desktop");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, desktopEnumUIExpLevel);
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNotNull(channel);
		assertEquals(desktopEnumUIExpLevel.getCode(), channel.getCode());
	}

	@Test
	public void testGetChannelForValidChannelContext()
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final EnumerationValue desktopEnumUIExpLevel = EnumerationManager.getInstance().getEnumerationValue(
				PriceRowChannel._TYPECODE, "desktop");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, desktopEnumUIExpLevel);
		ctx.setAttribute(CHANNEL, PriceRowChannel.DESKTOP);
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNotNull(channel);
		assertEquals(PriceRowChannel.DESKTOP.getCode(), channel.getCode());
	}

	@Test
	public void testGetChannelForInvalidUIExpLevelContext() throws Exception
	{
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		final EnumerationValue iPhoneChannel = EnumerationManager.getInstance().createEnumerationValue(
				Europe1Constants.TYPES.DISCOUNT_PRODUCT_GROUP, "dpg");
		ctx.setAttribute(DETECTED_UI_EXPERIENCE_LEVEL, iPhoneChannel);
		final PriceRowChannel channel = retrieveChannelStrategy.getChannel(ctx);
		assertNull(channel);
	}
}
