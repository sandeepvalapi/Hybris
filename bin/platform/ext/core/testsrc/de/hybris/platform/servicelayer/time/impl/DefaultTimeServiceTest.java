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
package de.hybris.platform.servicelayer.time.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTimeServiceTest extends ServicelayerBaseTest
{
	final long offset = 12 * 60 * 60 * 1000;

	@Resource
	private TimeService timeService;

	//	Some notes on testing time offset on bamboo:
	//	In that environment the JVM may block *any time* for *seconds if not
	// minutes* which is why we must pretend that between each instruction may
	// be a time-relevant delay. 
	// In consequence the following code is not safe:
	//    Date futureDate = new Date( System.currentTimeMillis() + 100 );
	//    timeService.setCurrentTime( futureDate );
	//    assertEquals( 100, timeService.getTimeOffset() );
	// Simply because between assembling the 100-offset Date and making
	// the time service store the offset may be some delay so that the actual
	// offset might be 100, 90, 50 or at worst -200 !
	//
	// So what to do:
	// 1. choose big offsets --> choosing 12h now and hoping that our test JVMs don't freeze that long  
	// 2. compare with <= & >=
	// 3. accept that the test may still fail from time to time !!!

	@Test
	public void testSetCurrentTime()
	{
		assertEquals(0, timeService.getTimeOffset());

		{
			final Date nowPlusOffset = new Date(System.currentTimeMillis() + offset);
			// since we're setting the offset indirectly via Date it may not be exact!
			timeService.setCurrentTime(nowPlusOffset);
			assertTrue("expect current offset to be > 0 when setting future time", timeService.getTimeOffset() > 0);
			// since we're setting the offset indirectly via Date it may not be exact!
			assertTrue("expecting current offset to be <= offset after setting future time", timeService.getTimeOffset() <= offset);
			assertTrue("expecting current offset to be > offset/2 after setting future time",
					timeService.getTimeOffset() > offset / 2);
		}

		{
			final long maxAcceptableVariation = offset * 10 / 100; // allow 10% difference to honor CI env
			final long now = System.currentTimeMillis();
			final long realCurrentOffset = timeService.getCurrentTime().getTime() - now;
			assertTrue("expect real current offset to be > 0 after setting future time", realCurrentOffset > 0);
			assertTrue("expecting real current offset to be +- 10% of offset after setting future time",
					Math.abs(realCurrentOffset - offset) < maxAcceptableVariation);
			assertTrue("expecting real current offset to be > 50% offset after setting future time", realCurrentOffset > offset / 2);
		}

		{
			final long nowBeforeSettingTime = System.currentTimeMillis();
			timeService.setCurrentTime(new Date(nowBeforeSettingTime));
			// since we're setting the offset indirectly via Date it may not be exactly 0 seconds!
			assertTrue("expect offset <= 0 after setting current time", timeService.getTimeOffset() <= 0);
			final long nowAfterSettingTime = System.currentTimeMillis();
			final long currentTimeFromService = timeService.getCurrentTime().getTime();
			final long maxAcceptableVariation = 2 * 1000; // accept 2 seconds give or take to honor CI env
			assertTrue("expect current time from service ~ current time from jvm after setting current time",
					Math.abs(currentTimeFromService - nowAfterSettingTime) < maxAcceptableVariation);
		}

		{
			timeService.setCurrentTime(null);
			assertEquals("expect zero offset after resetting time", 0, timeService.getTimeOffset());
			final long now = System.currentTimeMillis();
			final long nowFromService = timeService.getCurrentTime().getTime();
			final long maxAcceptableVariation = 2 * 1000; // accept 2 seconds give or take to honor CI env
			assertTrue("expect current time from service ~ current time from jvm after resetting time",
					Math.abs(nowFromService - now) < maxAcceptableVariation);
		}

		{
			final long negativeOffset = -1 * offset;
			final long nowMinusOffest = System.currentTimeMillis() + negativeOffset;
			timeService.setCurrentTime(new Date(nowMinusOffest));
			// since we're setting the offset indirectly via Date it may not be exactly 0 seconds!
			assertTrue("expect offset < 0 after setting past time", timeService.getTimeOffset() < 0);
			assertTrue("expect offset < negative offset / 2 after setting past time",
					timeService.getTimeOffset() < negativeOffset / 2);
			// since we're having a negative offset (towards the past) and the JVM clock can proceed the actual offset may be 
			// indicating a time shift even *further* towards the past --> actual negative offset <= negative offset 
			assertTrue("expect offset >= negative offset after setting past time", timeService.getTimeOffset() <= negativeOffset);

			final long now = System.currentTimeMillis();
			final long nowFromService = timeService.getCurrentTime().getTime();
			final long maxAcceptableVariation = 2 * 1000; // accept 2 seconds give or take to honor CI env
			final long diff2 = nowFromService - now;
			assertTrue("expecting real offset < 0 after setting past time", diff2 < 0);
			assertTrue("expecting real offset < negative offset / 2 after setting past time", diff2 < negativeOffset / 2);
			assertTrue("expecting real offset ~ negative offset after setting past time",
					Math.abs(negativeOffset - diff2) < maxAcceptableVariation);
		}
	}

	@Test
	public void testSetTimeOffset()
	{
		timeService.setTimeOffset(offset);
		assertEquals(offset, timeService.getTimeOffset());
		// since we're setting the offset indirectly via Date it may not be exactly 60 seconds!
		long now = System.currentTimeMillis();
		final long diff = timeService.getCurrentTime().getTime() - now;
		assertTrue("having offset in place I expect service time to be different", diff != 0);
		assertTrue("having offset in place I expect service time diff to be >= offset ", diff >= offset);
		assertTrue("having offset in place I expect service time diff to be less than 2 * offset", diff < 2 * offset);

		timeService.setTimeOffset(0);
		assertEquals(0, timeService.getTimeOffset());
		now = System.currentTimeMillis();
		assertTrue(0 <= timeService.getCurrentTime().getTime() - now);

		final long negativeOffset = -1 * offset;
		timeService.setTimeOffset(negativeOffset);
		assertEquals(negativeOffset, timeService.getTimeOffset());
		// since we're setting the offset indirectly via Date it may not be exactly 0 seconds!
		now = System.currentTimeMillis();
		final long diff2 = timeService.getCurrentTime().getTime() - now;
		assertTrue("having negative offest in place I expect system time diff to be negative as well", diff2 < 0);
		assertTrue("having negative offest in place I expect system time diff to be >= negative offset", diff2 >= negativeOffset);
		assertTrue("having negative offest in place I expect system time diff to be less than negative offset / 2",
				diff2 < (negativeOffset / 2));
	}

	@Test
	public void testSetCurrentTimeSystem()
	{
		timeService.setTimeOffset(12345);
		assertEquals(12345, timeService.getTimeOffset());

		timeService.resetTimeOffset();
		assertEquals(0, timeService.getTimeOffset());
		final long nowFromTimeService = timeService.getCurrentTime().getTime(); // make sure to call this *before* the test
		assertTrue(System.currentTimeMillis() >= nowFromTimeService);
	}

}
