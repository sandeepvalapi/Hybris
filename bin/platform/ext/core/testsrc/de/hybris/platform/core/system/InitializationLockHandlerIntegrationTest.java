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
package de.hybris.platform.core.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test describing a update checking mechanism for a HOR-1329
 */
@IntegrationTest
public class InitializationLockHandlerIntegrationTest extends AbstractLockHandlerIntegrationTest
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitializationLockHandlerIntegrationTest.class.getName());


	private InitializationLockHandler handler;

	@Before
	public void setUp()
	{
		// test requires active master tenant to get master data source
		Registry.activateMasterTenant();
		Utilities.setJUnitTenant();

		this.handler = prepareHandler("LockIntegrationTest");
	}

	@After
	public void tearDown()
	{
		clearHandler(handler);
	}

	@Test
	public void testCheckLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertFalse("Initially if there is no lock check table this should return FALSE ", handler.isLocked());
	}

	@Test
	public void testSetInitLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertTrue("This should put initial lock (for init)", handler.lock(Registry.getCurrentTenant(), "message"));
	}

	@Test
	public void testSetUpdateLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertTrue("This should put initial lock (for update)", handler.lock(Registry.getCurrentTenant(), "message"));
	}

	@Test
	public void testUnLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));

		try
		{
			handler.unlock(Registry.getCurrentTenant());
			fail("This should throw an IllegalStateException");
		}
		catch (final IllegalStateException ile)
		{
			assertEquals("no lock exists", ile.getMessage());
		}
	}

	@Test
	public void testUnLockWithOtherClusterIdSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertTrue("This should put initial lock (for update)", handler.lock(Registry.getCurrentTenant(), "message"));
		final Integer currentTenantClusterId = Integer.valueOf(((AbstractTenant) Registry.getCurrentTenant()).getClusterID());
		final AbstractTenant tenantMock = createOtherClusterIdTenant();
		try
		{
			handler.unlock(tenantMock);
			fail("This should throw an IllegalStateException");
		}
		catch (final IllegalStateException ile)
		{
			assertEquals(
					"lock is owned by different cluster node " + currentTenantClusterId.intValue() + " than "
							+ tenantMock.getClusterID() + " from tenant " + tenantMock, ile.getMessage());
		}
	}


	@Test
	public void testUnLockFewTimes()
	{
		assertFalse(checkTestTableExists(handler));
		assertTrue("This should put initial lock (for update)", handler.lock(Registry.getCurrentTenant(), "message"));
		handler.unlock(Registry.getCurrentTenant());
		assertFalse("Lock check table, this should return FALSE ", handler.isLocked());
		assertNotNull("Lockinfo should  exitst ", handler.getLockInfo());
		//LOG.info("uid "+handler.getLockInfo().getInstanceIdentifier());//
		assertEquals("uid should indicate a no lock also ", 0, handler.getLockInfo().getInstanceIdentifier());
		//unlock again should not harm
		handler.unlock(Registry.getCurrentTenant());
		assertFalse("Lock check table, this should return FALSE ", handler.isLocked());
		assertNotNull("Lockinfo should  exitst ", handler.getLockInfo());
		assertEquals("uid should indicate a no lock also ", 0, handler.getLockInfo().getInstanceIdentifier());
	}


	@Test
	public void testUnlockThenLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertFalse("Initially if there is no lock check table this should return FALSE ", handler.isLocked());
		try
		{

			handler.unlock(Registry.getCurrentTenant());
			fail("Unlocking unlocked system should throw an exception ");
		}
		catch (final IllegalStateException ile)
		{
			assertEquals("no lock exists", ile.getMessage());
		}
		assertFalse("Lock check table, this should return FALSE ", handler.isLocked());
		handler.lock(Registry.getCurrentTenant(), "some message");
		assertTrue("Lock check table, this should return TRUE ", handler.isLocked());
		assertTrue("Lock check table, this should return some message ",
				"some message".equals(handler.getLockInfo().getProcessName()));

	}

	@Test
	public void testLockThenUnLockForClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertFalse("Initially if there is no update check table this should return FALSE ", handler.isLocked());
		handler.lock(Registry.getCurrentTenant(), "funny message here");
		assertTrue("Lock check table, this should return TRUE ", handler.isLocked());
		assertTrue("funny message here".equals(handler.getLockInfo().getProcessName()));
		handler.unlock(Registry.getCurrentTenant());
		assertFalse("Lock check table, this should return FALSE ", handler.isLocked());
	}

	@Test
	public void testLockForUpdateThenLockForInitializationClearSystem()
	{
		assertFalse(checkTestTableExists(handler));
		assertFalse("Initially if there is no update check table this should return FALSE ", handler.isLocked());
		assertTrue("This should  success", handler.lock(Registry.getCurrentTenant(), "message"));
		assertTrue("Lock check table, this should return TRUE ", handler.isLocked());
		assertTrue("message".equals(handler.getLockInfo().getProcessName()));
		assertFalse("This should not success", handler.lock(Registry.getCurrentTenant(), "other message"));
		assertTrue("Lock check table, this should return TRUE ", handler.isLocked());
		assertTrue("message".equals(handler.getLockInfo().getProcessName()));
		handler.unlock(Registry.getCurrentTenant());
	}



	@Test
	public void testLockExistsAfterDaoSerialized() throws IOException, ClassNotFoundException
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();
		//
		final InitializationLockHandler beforeSerializeHandler = new TestInitializationLockHandler(tenant, "FooBar");

		assertFalse(checkTestTableExists(tenant.getDataSource(), "FooBar"));

		assertFalse("Initially if there is no update check table this should return FALSE ", handler.isLocked());
		assertTrue("This should  success", beforeSerializeHandler.lock(Registry.getCurrentTenant(), "message"));

		assertTrue("Lock check table, this should return TRUE ", beforeSerializeHandler.isLocked());
		assertTrue("message".equals(beforeSerializeHandler.getLockInfo().getProcessName()));
		assertTrue("Uid for lock and instance id should be equal ",
				beforeSerializeHandler.getLockInfo().getInstanceIdentifier() == handler.initializationDao
						.getUniqueInstanceIdentifier());

		//serialize
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(beforeSerializeHandler);
		oos.close();
		//deserialize
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		final InitializationLockHandler deserializedHandler = (InitializationLockHandler) ois.readObject();

		//deserializedHandler.getLockInfo()
		assertTrue("Lock could be verified using deserialized handler ", deserializedHandler.isLocked());
		assertFalse("Uid for lock and instance id should not be equal!!! ", deserializedHandler.getLockInfo()
				.getInstanceIdentifier() != deserializedHandler.initializationDao.getUniqueInstanceIdentifier());

		deserializedHandler.unlock(Registry.getCurrentTenant());
		assertFalse("Lock could be now removed ", deserializedHandler.isLocked());
	}


}
