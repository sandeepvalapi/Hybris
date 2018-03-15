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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@IntegrationTest
public class TransactionTemplateTest extends HybrisJUnit4Test
{
	private TransactionTemplate template;

	@Before
	public void setUp()
	{
		template = new TransactionTemplate((PlatformTransactionManager) Registry.getApplicationContext().getBean("txManager"));
	}

	@Test
	public void testInTXModification1() throws Exception
	{
		final Country country = C2LManager.getInstance().createCountry("before");
		assertNotNull(country);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				Transaction.current().enableDelayedStore(false);
				try
				{
					country.setIsoCode("after");
					C2LManager.getInstance().getCountryByIsoCode("before"); //should find the old
					fail("Jaloitemnotfound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					//OK
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("after"); //and not! the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
			}
		});
	}

	@Test
	public void testInTXModification2() throws Exception
	{
		final Country country = C2LManager.getInstance().createCountry("before");
		assertNotNull(country);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				Transaction.current().enableDelayedStore(true);
				try
				{
					country.setIsoCode("after");
					C2LManager.getInstance().getCountryByIsoCode("after"); //should find the old
					fail("JaloItemNotFound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					//OK
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("before"); //and not! the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
				Transaction.current().flushDelayedStore();
				try
				{
					C2LManager.getInstance().getCountryByIsoCode("after"); //should find the new
				}
				catch (final JaloItemNotFoundException e)
				{
					fail("Jaloitemnotfound should not occur!");
				}
			}
		});
	}

	@Test
	public void testInTXRemoval() throws Exception
	{

		final String iso = PK.createUUIDPK(0).getHex();
		final Country country = C2LManager.getInstance().createCountry(iso);

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				Transaction.current().enableDelayedStore(false);
				try
				{
					country.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				try
				{
					C2LManager.getInstance().getCountryByIsoCode(iso);
					fail("Jaloitemnotfound should occur!");
				}
				catch (final JaloItemNotFoundException e)
				{
					//ok, good
				}
			}
		});

	}

	@Test
	public void testJaloCacheInvalidation() throws Exception
	{
		final Country country = jaloSession.getC2LManager().createCountry("code11");
		assertNotNull(country);
		assertEquals("code11", country.getIsoCode());

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				assertEquals("code11", country.getIsoCode());
				try
				{
					country.setIsoCode("code2");
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				assertEquals("code2", country.getIsoCode());
			}
		});
		assertFalse("found transaction still running", Transaction.current().isRunning());
		assertEquals("code2", country.getIsoCode());

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				status.setRollbackOnly();
				assertEquals("code2", country.getIsoCode());
				try
				{
					country.setIsoCode("code3");
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				assertEquals("code3", country.getIsoCode());
			}
		});
		assertFalse("found transaction still running", Transaction.current().isRunning());
		assertEquals("code2", country.getIsoCode());
	}

	@Test
	public void testNestedTransactionRollback() throws ConsistencyCheckException
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				assertTrue(Transaction.current().isRunning());
				assertEquals(1, Transaction.current().getOpenTransactionCount());

				Country country = null;
				try
				{
					country = C2LManager.getInstance().createCountry("Thalerland");
				}
				catch (final ConsistencyCheckException e)
				{
					Assert.fail();
				}
				assertNotNull(country);
				assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

				template.execute(new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(final TransactionStatus status)
					{

						assertTrue(Transaction.current().isRunning());
						//assertEquals(2, Transaction.current().getOpenTransactionCount());

						// in propagation mode REQUIRED the inner callback is simply executed within the enclosing TA !!!  
						assertEquals(1, Transaction.current().getOpenTransactionCount());
					}
				});
				status.setRollbackOnly();

				assertEquals(1, Transaction.current().getOpenTransactionCount());
				assertTrue(Transaction.current().isRunning());
				assertEquals(country, C2LManager.getInstance().getCountryByIsoCode("Thalerland"));

				assertNotNull(country);

			}
		});

		try
		{
			C2LManager.getInstance().getCountryByIsoCode("Thalerland");
			fail("JaloItemNotFoundException expected");
		}
		catch (final JaloItemNotFoundException e)
		{
			// OK
		}
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
	}

	@Test
	public void testNestedTransactionExecute() throws Exception
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				assertEquals(1, Transaction.current().getOpenTransactionCount());
				assertTrue(Transaction.current().isRunning());

				template.execute(new TransactionCallbackWithoutResult()
				{
					@Override
					protected void doInTransactionWithoutResult(final TransactionStatus status)
					{
						// in propagation mode REQUIRED the inner callback is simply executed within the enclosing TA !!!  
						//assertEquals(2, Transaction.current().getOpenTransactionCount());
						assertEquals(1, Transaction.current().getOpenTransactionCount());

						assertTrue(Transaction.current().isRunning());
					}
				});
			}
		});

		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
	}

	@Test
	public void testNestedTransactionWithCreate() throws Exception
	{
		for (int i = 0; i < 10; i++)
		{
			template.execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus status)
				{
					status.setRollbackOnly();

					final MediaFormat format3 = MediaManager.getInstance().createMediaFormat("testFormat3_tx6");
					MediaManager.getInstance().createMedia("testMedia1WithFormat3_tx", format3);
				}
			});
		}
	}
}
