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
package de.hybris.platform.servicelayer.tx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import junit.framework.Assert;


@IntegrationTest
public class ModelAwareTransactionTemplateTest extends ServicelayerBaseTest
{
	@Resource
	private PlatformTransactionManager txManager;

	@Resource
	private ModelService modelService;

	private TransactionTemplate template;

	@Before
	public void setUp()
	{
		template = new TransactionTemplate(txManager);
	}

	@Test
	public void testInTXModification1() throws Exception
	{
		final CountryModel c = modelService.create(CountryModel.class);
		c.setIsocode("before");
		modelService.save(c);
		assertNotNull(c);
		template.execute(new ModelAwareTransactionCallbackWithoutResult<CountryModel>()
		{
			@Override
			protected ModelService getModelService()
			{
				return modelService;
			}


			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				try
				{
					//save as after
					c.setIsocode("after");
					modelService.save(c);
					//look for a before
					findCountry("before");
					fail("Model notfound should occur!");
				}
				catch (final de.hybris.platform.servicelayer.exceptions.ModelNotFoundException e)
				{
					//OK
				}
				catch (final Exception e)
				{
					fail(e.getMessage());
				}

				try
				{
					//look for a after
					findCountry("after");
					//and not! the new
				}
				catch (final de.hybris.platform.servicelayer.exceptions.ModelNotFoundException e)
				{
					fail("Model notfound should not occur!");
				}
			}
		});
	}

	@Ignore("HOR-2141")
	@Test
	public void testInTXModification2() throws Exception
	{
		final Boolean bb = (Boolean) jaloSession.getSessionContext().getAttribute(SessionContext.TRANSACTION_4_ALLATTRIBUTES);
		try
		{
			jaloSession.getSessionContext().setAttribute(SessionContext.TRANSACTION_4_ALLATTRIBUTES, Boolean.FALSE);
			final CountryModel c = modelService.create(CountryModel.class);
			c.setIsocode("before");
			modelService.save(c);

			assertNotNull(c);
			template.execute(new ModelAwareTransactionCallbackWithoutResult<CountryModel>()
			{

				@Override
				protected ModelService getModelService()
				{
					return modelService;
				}

				@Override
				protected boolean isEnableDelayedStore()
				{
					// enabled delayed store
					return true;
				}

				@Override
				protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
				{
					try
					{
						c.setIsocode("after");
						modelService.save(c);

						findCountry("after");
						fail("Model NotFound should occur!");
					}
					catch (final ModelNotFoundException e)
					{
						//OK
					}
					catch (final Exception e)
					{
						Assert.fail();
					}
					try
					{
						findCountry("before");
					}
					catch (final ModelNotFoundException e)
					{
						fail("Model notfound should not occur!");
					}
					Transaction.current().flushDelayedStore();
					try
					{
						findCountry("after");
					}
					catch (final ModelNotFoundException e)
					{
						fail("Model notfound should not occur!");
					}
				}
			});

		} finally
		{
			jaloSession.getSessionContext().setAttribute(SessionContext.TRANSACTION_4_ALLATTRIBUTES, bb);
		}
	}


	@Test
	public void testInTXRemoval() throws Exception
	{

		final String iso = PK.createUUIDPK(0).getHex();
		final CountryModel c = modelService.create(CountryModel.class);
		c.setIsocode(iso);
		modelService.save(c);

		template.execute(new ModelAwareTransactionCallbackWithoutResult<CountryModel>()
		{
			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				try
				{
					modelService.remove(c);
				}
				catch (final Exception e)
				{
					Assert.fail();
				}
				try
				{
					findCountry(iso);
					fail("Model notfound should occur!");
				}
				catch (final ModelNotFoundException e)
				{
					//ok, good
				}
			}

			@Override
			protected ModelService getModelService()
			{
				return modelService;
			}

		});

	}

	@Test
	public void testJaloCacheInvalidation() throws Exception
	{
		final CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode("code11");
		modelService.save(country);

		assertNotNull("Country could not be a null", country);
		assertEquals("Countries is code should not be null", "code11", country.getIsocode());

		template.execute(new ModelAwareTransactionCallbackWithoutResult<CountryModel>()
		{
			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				assertEquals("code11", country.getIsocode());
				try
				{
					country.setIsocode("code2");
					modelService.save(country);
				}
				catch (final ModelSavingException e)
				{
					Assert.fail();
				}
				assertEquals("code2", country.getIsocode());
			}

			@Override
			protected ModelService getModelService()
			{

				return modelService;
			}

		});
		assertFalse("found transaction still running", Transaction.current().isRunning());
		assertEquals("code2", country.getIsocode());

		template.execute(new ModelAwareTransactionCallbackWithoutResult<CountryModel>()
		{
			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				status.setRollbackOnly();
				assertEquals("code2", country.getIsocode());
				try
				{
					country.setIsocode("code3");
					modelService.save(country);
				}
				catch (final ModelSavingException e)
				{
					Assert.fail();
				}
				assertEquals("code3", country.getIsocode());
			}

			@Override
			protected ModelService getModelService()
			{
				return modelService;
			}
		});
		assertFalse("found transaction still running", Transaction.current().isRunning());
		try
		{
			findCountry("code2");

		}
		catch (final ModelNotFoundException e)
		{
			fail("Model notfound should not  occur!");
		}

		try
		{
			findCountry("code3");
			fail("Model notfound should occur!");
		}
		catch (final ModelNotFoundException e)//NOPMD
		{
			//ok, good
		}
	}

	@Test
	public void testNestedTransactionRollback() throws ConsistencyCheckException
	{
		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());


		template.execute(new ModelAwareTransactionCallbackWithoutResult()
		{
			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				assertTrue(Transaction.current().isRunning());
				assertEquals(1, Transaction.current().getOpenTransactionCount());

				CountryModel countryLocal = null;
				try
				{
					countryLocal = modelService.create(CountryModel.class);
					countryLocal.setIsocode("DummyLand");
					modelService.save(countryLocal);
				}
				catch (final ModelSavingException e)
				{
					Assert.fail("Should save model");
				}
				assertNotNull(countryLocal);
				assertEquals(countryLocal, findCountry("DummyLand"));

				template.execute(new ModelAwareTransactionCallbackWithoutResult()
				{
					@Override
					protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
					{

						assertTrue(Transaction.current().isRunning());
						// in propagation mode REQUIRED the inner callback is simply executed within the enclosing TA !!!
						assertEquals(1, Transaction.current().getOpenTransactionCount());
					}

					@Override
					protected ModelService getModelService()
					{
						return modelService;
					}

				});
				status.setRollbackOnly();

				assertEquals(1, Transaction.current().getOpenTransactionCount());
				assertTrue(Transaction.current().isRunning());
				assertNotNull(countryLocal);
				assertEquals(countryLocal, findCountry("DummyLand"));
			}


			@Override
			protected ModelService getModelService()
			{
				return modelService;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}
		});

		try
		{
			findCountry("DummyLand");
			fail("Model NotFoundException expected");
		}
		catch (final ModelNotFoundException e)
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

		template.execute(new ModelAwareTransactionCallbackWithoutResult()
		{
			@Override
			protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
			{
				assertEquals(1, Transaction.current().getOpenTransactionCount());
				assertTrue(Transaction.current().isRunning());

				template.execute(new ModelAwareTransactionCallbackWithoutResult()
				{
					@Override
					protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
					{
						// in propagation mode REQUIRED the inner callback is simply executed within the enclosing TA !!!
						assertEquals(1, Transaction.current().getOpenTransactionCount());
						assertTrue(Transaction.current().isRunning());
					}

					@Override
					protected ModelService getModelService()
					{
						return modelService;
					}

				});
			}

			@Override
			protected ModelService getModelService()
			{
				return modelService;
			}

			@Override
			protected boolean isEnableDelayedStore()
			{
				return false;
			}
		});

		assertEquals(0, Transaction.current().getOpenTransactionCount());
		assertFalse(Transaction.current().isRunning());
	}

	@Test
	public void testNestedTransactionWithCreate() throws Exception
	{
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("test");
		final CatalogVersionModel version = new CatalogVersionModel();
		version.setVersion("test");
		version.setCatalog(catalog);
		version.setActive(Boolean.TRUE);
		modelService.initDefaults(catalog);
		modelService.initDefaults(version);
		modelService.saveAll(catalog, version);

		for (int i = 0; i < 10; i++)
		{
			try
			{
				template.execute(new ModelAwareTransactionCallbackWithoutResult()
				{

					@Override
					protected void doInModelAwareTransactionWithoutResult(final TransactionStatus status)
					{
						status.setRollbackOnly();

						final MediaFormatModel formatModel = modelService.create(MediaFormatModel.class);
						formatModel.setQualifier("testFormat3_tx6");

						final MediaModel mediaModel = modelService.create(MediaModel.class);
						mediaModel.setMediaFormat(formatModel);
						mediaModel.setCode("testMedia1WithFormat3_tx");
						mediaModel.setCatalogVersion(version);
						modelService.save(mediaModel);
					}

					@Override
					protected ModelService getModelService()
					{
						return modelService;
					}

				});
			}
			catch (final Exception mse)
			{
				mse.printStackTrace();
				Assert.fail("Any rollbacked logic in tx should not throw a exception");

			}
		}
	}

	@SuppressWarnings("deprecation")
	private CountryModel findCountry(final String code)
	{
		final CountryModel template = new CountryModel();
		template.setIsocode(code);
		return modelService.getByExample(template);
	}

}
