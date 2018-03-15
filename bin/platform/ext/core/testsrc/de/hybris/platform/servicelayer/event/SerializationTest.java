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
package de.hybris.platform.servicelayer.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.MockTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.StopWatch;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@UnitTest
@ContextConfiguration(locations =
{ "classpath:/servicelayer/test/servicelayer-mock-base-test.xml" })
public class SerializationTest extends MockTest
{
	private static final Logger LOG = Logger.getLogger(SerializationTest.class);
	private static final int CNT = 20000;

	@Resource(name = "serializationService")
	private SerializationService serializationService;


	@Test
	public void testSize()
	{
		LOG.info("=== DefaultSerializationService ===");
		test(serializationService, true);
	}

	@Test
	public void testPerformance()
	{

		StopWatch stopwatch = new StopWatch("defaultSerialization");
		for (int i = 0; i < CNT; i++)
		{
			test(serializationService, false);
		}
		stopwatch.stop();

		stopwatch = new StopWatch("customEventWithJDKSerialization");
		for (int i = 0; i < CNT; i++)
		{
			serializationService.deserialize(serializationService.serialize(new CustomTxEvent(true, "testid")));
		}
		final long time = stopwatch.stop();
		LOG.info("serializations/second=" + CNT / ((double) time / 1000 / 1000 / 1000));
	}

	@Test
	public void testBase64Size()
	{
		final byte[] byt = serializationService.serialize(new CustomTxEvent(true, "testid"));
		LOG.info("serialized without base64: " + byt.length + " bytes");
		String str = Base64.encodeBytes(byt, Base64.DONT_BREAK_LINES);
		LOG.info("serialized with base64: " + str.length() + " chars," + str.getBytes().length + " bytes");
		str = Base64.encodeBytes(byt, Base64.DONT_BREAK_LINES | Base64.GZIP);
		LOG.info("serialized with base64 GZIP: " + str.length() + " chars," + str.getBytes().length + " bytes");

	}

	@Test
	public void testBase64Perf()
	{
		final StopWatch stopwatch = new StopWatch("customEventWithJDKSerializationAndBase64");
		for (int i = 0; i < CNT; i++)
		{
			byte[] byt = serializationService.serialize(new CustomTxEvent(true, "testid"));
			final String str = Base64.encodeBytes(byt, Base64.DONT_BREAK_LINES);
			byt = Base64.decode(str);
			serializationService.deserialize(byt);
		}
		final long time = stopwatch.stop();
		LOG.info("serializations/second=" + CNT / ((double) time / 1000 / 1000 / 1000));
	}

	private void test(final SerializationService service, final boolean output)
	{
		final String test = "1234567890";
		byte[] byt = service.serialize(test);
		final String test2 = (String) service.deserialize(byt);
		assertEquals("", test, test2);
		if (output)
		{
			LOG.info("String (10chars)=" + byt.length + " bytes");
		}

		Object obj = new SmallObject();
		byt = service.serialize(obj);
		Object obj2 = service.deserialize(byt);
		assertNotSame("cannot be equal", obj, obj2);
		if (output)
		{
			LOG.info("new Object()=" + byt.length + " bytes");
		}


		obj = new CustomTxEvent(true, "testid");
		byt = service.serialize(obj);
		if (output)
		{
			LOG.info("new CustomTxEvent(true,\"testid\")=" + byt.length + " bytes");
		}

		obj = new CustomTxEvent(true, "a longer teststring here");
		byt = service.serialize(obj);
		obj2 = service.deserialize(byt);
		assertEquals("", obj, obj2);
		if (output)
		{
			LOG.info("new CustomTxEvent(true,\"a longer teststring here\")=" + byt.length + " bytes");
		}

	}

	static class CustomTxEvent extends AbstractEvent implements TransactionAwareEvent
	{
		private final String id;
		private final boolean onCommit;
		@SuppressWarnings("unused")
		private static final String TENANTID = "master";
		@SuppressWarnings("unused")
		private static final int CLUSTERID = 15;

		public CustomTxEvent(final boolean publishOnCommit, final String id)
		{
			super();
			this.id = id;
			this.onCommit = publishOnCommit;
		}

		@Override
		public Object getId()
		{
			return id;
		}

		@Override
		public boolean publishOnCommitOnly()
		{
			return onCommit;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == null)
			{
				return false;
			}

			return ((CustomTxEvent) obj).id.equals(id);
		}

	}

	static class SmallObject implements Serializable
	{
		//nothing here
	}
}
