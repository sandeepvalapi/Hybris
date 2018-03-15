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
package de.hybris.platform.processengine;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class WaitNodeTimeoutIntegrationTest extends ServicelayerBaseTest
{
	private static final long WAIT_TIMEOUT = TimeUnit.SECONDS.toMillis(60);
	private static final String CTX = "(" + WaitNodeTimeoutIntegrationTest.class.getName() + ".getContext())";
	private volatile static Context context = null;

	public static Context getContext()
	{
		return context;
	}

	@Resource
	private ModelService modelService;

	@Resource
	private BusinessProcessService businessProcessService;

	@Before
	public void setUp()
	{
		context = new Context();
	}

	@Test
	public void shouldTransitToTimeoutNodeOnTimeout() throws InterruptedException, TimeoutException, IOException
	{
		final String waitEvent = givenUniqueId();
		final BusinessProcessModel process = givenBusinessProcess( //
				"<?xml version='1.0' encoding='utf-8'?>", //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='" + givenUniqueId() + "'>", //
				"	<scriptAction id='action0'>", //
				"		<script type='groovy'>" + CTX + ".processStarted(); return 'itworks';</script>", //
				"		<transition name='itworks' to='waitNode'/>", //
				"	</scriptAction>", //
				"  <wait id='waitNode' then='nothingChoosen' prependProcessCode='false'>", //
				"    <case event='" + waitEvent + "'>", //
				"      <choice id='first_choice' then='firstChoiceChoosen'/>", //
				"      <choice id='second_choice' then='secondChoiceChoosen'/>", //
				"    </case>", //
				"    <timeout delay='PT30S' then='timeout' />", //
				"  </wait>", //
				"  <scriptAction id='nothingChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'nothingChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='firstChoiceChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'firstChoiceChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='secondChoiceChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'secondChoiceChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='timeout'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'timedOut'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>", //
				"</process>");

		businessProcessService.startProcess(process);

		waitFor(process);
		assertThat(process.getState()).isEqualTo(ProcessState.SUCCEEDED);
		assertThat(context.get("choice")).isNotNull().isEqualTo("timedOut");
	}

	@Test
	public void shouldNotTransitToTimeoutNodeWhenNodeTriggeredBeforeTimeout()
			throws InterruptedException, TimeoutException, IOException
	{
		final String waitEvent = givenUniqueId();
		final BusinessProcessModel process = givenBusinessProcess( //
				"<?xml version='1.0' encoding='utf-8'?>", //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='" + givenUniqueId() + "'>", //
				"	<scriptAction id='action0'>", //
				"		<script type='groovy'>" + CTX + ".processStarted(); return 'itworks';</script>", //
				"		<transition name='itworks' to='waitNode'/>", //
				"	</scriptAction>", //
				"  <wait id='waitNode' then='nothingChoosen' prependProcessCode='false'>", //
				"    <case event='" + waitEvent + "'>", //
				"      <choice id='first_choice' then='firstChoiceChoosen'/>", //
				"      <choice id='second_choice' then='secondChoiceChoosen'/>", //
				"    </case>", //
				"    <timeout delay='PT30S' then='timeout' />", //
				"  </wait>", //
				"  <scriptAction id='nothingChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'nothingChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='firstChoiceChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'firstChoiceChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='secondChoiceChoosen'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'secondChoiceChoosen'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"  <scriptAction id='timeout'>", //
				"    <script type='groovy'>" + CTX + ".put('choice', 'timedOut'); return 'ok';</script>", //
				"    <transition name='ok' to='success' />", //
				"  </scriptAction>", //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>", //
				"</process>");

		businessProcessService.triggerEvent(BusinessProcessEvent.newEvent(waitEvent));
		businessProcessService.startProcess(process);

		waitFor(process);
		assertThat(process.getState()).isEqualTo(ProcessState.SUCCEEDED);
		assertThat(context.get("choice")).isNotNull().isEqualTo("nothingChoosen");
	}

	private String givenUniqueId()
	{
		return UUID.randomUUID().toString();
	}

	private BusinessProcessModel givenBusinessProcess(final String... lines)
	{
		final String content = Arrays.asList(lines).stream().sequential().reduce("", (acc, s) -> acc + s + "\n");
		final String code = extractCodeFromContent(content);

		final DynamicProcessDefinitionModel definition = modelService.create(DynamicProcessDefinitionModel.class);
		definition.setContent(content);
		definition.setCode(code);
		modelService.save(definition);

		return businessProcessService.createProcess(UUID.randomUUID().toString(), code);
	}

	private String extractCodeFromContent(final String content)
	{
		try
		{
			final XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new StringReader(content));
			while (reader.hasNext())
			{
				final XMLEvent tag = reader.nextEvent();
				if (tag.isStartElement())
				{
					final StartElement element = tag.asStartElement();
					if ("process".equalsIgnoreCase(element.getName().getLocalPart()))
					{
						final Attribute name = element.getAttributeByName(QName.valueOf("name"));
						if (name != null)
						{
							return name.getValue();
						}
					}
				}
			}
		}
		catch (XMLStreamException | FactoryConfigurationError e)
		{
			throw new RuntimeException(e);
		}
		throw new RuntimeException("BROKEN TEST");
	}

	private void waitFor(final BusinessProcessModel model) throws InterruptedException, TimeoutException
	{
		waitForCondition(r -> {
			modelService.refresh(model);
			return Boolean.valueOf(model.getState() != ProcessState.RUNNING);
		});
	}

	private void waitForCondition(final Function<Long, Boolean> condition) throws TimeoutException, InterruptedException
	{
		final long start = System.currentTimeMillis();
		long round = 1;
		while (!condition.apply(Long.valueOf(round)).booleanValue())
		{
			if (System.currentTimeMillis() - start > WAIT_TIMEOUT)
			{
				throw new TimeoutException();
			}
			round++;
			Thread.sleep(Math.min(100, WAIT_TIMEOUT / 100));
		}
	}

	public static class Context
	{
		private static final long TIMEOUT = 2;
		private final CountDownLatch processStarted = new CountDownLatch(1);
		private final ConcurrentMap<String, String> ctxMap = new ConcurrentHashMap<>();

		public void processStarted()
		{
			processStarted.countDown();
		}

		public void waitForProcessStart() throws InterruptedException
		{
			if (!processStarted.await(TIMEOUT, TimeUnit.SECONDS))
			{
				throw new RuntimeException("Process didn't start in expected time.");
			}
		}

		public void test(final String sender)
		{
			System.err.println("HELLO FROM " + sender);
		}

		public void put(final String key, final String value)
		{
			ctxMap.put(key, value);
		}

		public String get(final String key)
		{
			return ctxMap.get(key);
		}
	}

}
