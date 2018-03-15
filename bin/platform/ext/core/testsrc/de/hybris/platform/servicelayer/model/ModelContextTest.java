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
package de.hybris.platform.servicelayer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Separate class for testing model context behaviour since using parallel threads was impossible on SQL Server due to
 * (supposedly) obtained locks while creating data in setUp(). (See PLA-8626 for details)
 */
@IntegrationTest
public class ModelContextTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;

	@Test
	public void testModelContext() throws InterruptedException
	{
		final User user = jaloSession.getUser();
		final UserModel um0 = modelService.get(user);
		final Map<Thread, UserModel> models = Collections.synchronizedMap(new HashMap<Thread, UserModel>());
		final Tenant tenant = jaloSession.getTenant();

		final Thread thread1 = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				Registry.setCurrentTenant(tenant);
				final UserModel userModel = (UserModel) modelService.get(user);
				models.put(this, userModel);
			}
		};
		final Thread thread2 = new RegistrableThread()
		{
			@Override
			public void internalRun()
			{
				Registry.setCurrentTenant(tenant);
				models.put(this, (UserModel) modelService.get(user));
			}
		};

		thread1.start();
		thread2.start();

		final long start = System.currentTimeMillis();
		final long waitUntil = start + (30 * 1000);
		do
		{
			Thread.sleep(1000);
		}
		while ((thread1.isAlive() || thread2.isAlive()) && System.currentTimeMillis() < waitUntil);

		assertFalse("Thread will not end", thread1.isAlive());
		assertFalse("Thread will not end", thread2.isAlive());

		assertEquals(2, models.size());
		assertNotNull(models.get(thread1));
		assertNotNull(models.get(thread2));
		final UserModel um1 = models.get(thread1);
		final UserModel um2 = models.get(thread2);

		assertNotSame(um0, um1);
		assertNotSame(um0, um2);
		assertNotSame(um1, um2);

		assertEquals(user.getPK(), um0.getPk());
		assertEquals(user.getPK(), um1.getPk());
		assertEquals(user.getPK(), um2.getPk());
	}
}
