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
package de.hybris.platform.servicelayer.internal.model.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.tx.DefaultAfterSaveListenerRegistry;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


@Ignore("HORST-798")
@IntegrationTest
public class DefaultModelServiceAfterSaveEventTest extends ServicelayerBaseTest
{
	public static final int TIMEOUT = 3;
	final Logger LOG = Logger.getLogger(DefaultModelServiceAfterSaveEventTest.class.getName());

	@Resource
	private ModelService modelService;

	@Resource
	private DefaultAfterSaveListenerRegistry defaultAfterSaveListenerRegistry;

	@Resource
	private UserService userService;

	@Resource
	private CommonI18NService commonI18NService;

	private final AtomicReference<PK> pkFromTest = new AtomicReference<PK>();
	private final AtomicReference<String> codeFromListener = new AtomicReference<>();
	private CyclicBarrier eventListenerJoinGate;
	private AfterSaveListener eventListener;

	private CatalogModel catalog;
	private CatalogVersionModel catalogVersion;
	private CurrencyModel currency;
	private UnitModel kg;
	private UnitModel g;
	private OrderModel order;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");

		catalog = new CatalogModel();
		catalog.setId("Default");
		catalog.setName("Default", Locale.ENGLISH);
		modelService.save(catalog);

		catalogVersion = new CatalogVersionModel();
		catalogVersion.setActive(true);
		catalogVersion.setVersion("Staged");
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		currency = new CurrencyModel();
		currency.setActive(true);
		currency.setSymbol("EUR");
		currency.setConversion(1.0);
		currency.setIsocode("EUR");
		modelService.save(currency);

		kg = new UnitModel();
		kg.setCode("kg");
		kg.setName("kilogram", Locale.ENGLISH);
		kg.setConversion(1000d);
		kg.setUnitType("weight");
		modelService.save(kg);

		g = new UnitModel();
		g.setCode("g");
		g.setName("gram", Locale.ENGLISH);
		g.setConversion(1d);
		g.setUnitType("weight");
		modelService.save(g);

		order = new OrderModel();
		order.setCode("testOrder");
		order.setUser(userService.getAdminUser());
		order.setCurrency(commonI18NService.getCurrency("EUR"));
		order.setDate(new Date());
		modelService.save(order);

		eventListenerJoinGate = new CyclicBarrier(2);
		eventListener = createEventListener(eventListenerJoinGate);
		defaultAfterSaveListenerRegistry.addListener(getEventListener());
	}

	@Test
	public void shouldReturnRefreshedModelInAfterSaveEvent() throws InterruptedException
	{
		final PK pk = order.getPk();
		pkFromTest.set(pk);
		String newCode = "TEST 01";
		final OrderModel orderModel = modelService.get(pk);
		orderModel.setCode(newCode);
		modelService.save(orderModel);
		assertSame(orderModel, modelService.get(pk));

		LOG.info("CODE: " + orderModel.getCode());
		assertEquals("codes should be the same", newCode, orderModel.getCode());

		waitForEventListener();

		assertEquals("code from AfterSaveEvent listener is outdated", newCode, codeFromListener.get());

		// change code
		newCode = "TEST 02";
		assertSame(orderModel, modelService.get(pk));
		orderModel.setCode(newCode);
		modelService.save(orderModel);
		assertSame(orderModel, modelService.get(pk));

		LOG.info("CODE: " + orderModel.getCode());
		assertEquals("codes should be the same", newCode, orderModel.getCode());

		waitForEventListener();

		assertEquals("code from AfterSaveEvent listener is outdated", newCode, codeFromListener.get());
	}

	private void waitForEventListener() throws InterruptedException
	{
		try
		{
			eventListenerJoinGate.await(TIMEOUT, TimeUnit.SECONDS);
		}
		catch (final TimeoutException e)
		{
			fail("Timeout waiting for after save event: " + e.getMessage());
		}
		catch (final BrokenBarrierException e)
		{
			fail("Broken barrier while waiting for after save event: " + e.getMessage());
		}
	}

	private AfterSaveListener getEventListener()
	{
		return eventListener;
	}


	private AfterSaveListener createEventListener(final CyclicBarrier joinGate)
	{
		return new AfterSaveListener()
		{
			@Override
			public void afterSave(final Collection<AfterSaveEvent> events)
			{
				for (final AfterSaveEvent ase : events)
				{
					final PK pk = ase.getPk();
					if (pk.equals(DefaultModelServiceAfterSaveEventTest.this.pkFromTest.get()))
					{
						final OrderModel orderModel = modelService.get(pk);
						codeFromListener.set(orderModel.getCode());
						LOG.info("CODE FROM AFTER SAVE EVENT: " + codeFromListener.get());
						//latch.countDown();
						try
						{
							joinGate.await();
						}
						catch (final InterruptedException e)
						{
							Thread.currentThread().interrupt();
							e.printStackTrace();
						}
						catch (final BrokenBarrierException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		};
	}

	@After
	public void tearDown()
	{
		defaultAfterSaveListenerRegistry.removeListener(getEventListener());
		modelService.remove(order);
		catalogVersion.setActive(false);
		modelService.save(catalogVersion);
		modelService.removeAll(catalog, catalogVersion, kg, g, currency);
	}

}
