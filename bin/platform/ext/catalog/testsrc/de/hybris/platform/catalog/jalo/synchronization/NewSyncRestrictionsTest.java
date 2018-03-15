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
package de.hybris.platform.catalog.jalo.synchronization;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * The test was created to verify that the restriction/sessionContext defect(PLA-8677) within the custom
 * CatalogVersionSyncJob was fixed. The test creates three products within srcCatalogVersion but only one will be
 * synchronized.
 */
@IntegrationTest
public class NewSyncRestrictionsTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(NewSyncRestrictionsTest.class);

	private CatalogManager catalogManager;
	private CatalogVersion src, tgt;
	private Product prod1, prod2, prod3;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		SessionContext deCtx, enCtx = null;
		Unit unit = null;
		catalogManager = CatalogManager.getInstance();

		final Catalog catalog = catalogManager.createCatalog("foo");
		src = catalogManager.createCatalogVersion(catalog, "bar", null);
		src.setLanguages(Arrays.asList(getOrCreateLanguage("de"), getOrCreateLanguage("en")));
		tgt = catalogManager.createCatalogVersion(catalog, "bar2", null);
		tgt.setLanguages(Arrays.asList(getOrCreateLanguage("de"), getOrCreateLanguage("ed")));

		deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(getOrCreateLanguage("de"));
		enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(getOrCreateLanguage("en"));

		final ProductManager productManager = ProductManager.getInstance();
		unit = productManager.createUnit("foo", "bar");

		prod1 = productManager.createProduct("prod1");
		catalogManager.setCatalogVersion(prod1, src);
		prod1.setName(deCtx, "prod1 name de");
		prod1.setName(enCtx, "prod1 name en");
		prod1.setUnit(unit);

		prod2 = productManager.createProduct("prod2");
		catalogManager.setCatalogVersion(prod2, src);
		prod2.setName(deCtx, "prod2 name de");
		prod2.setName(enCtx, "prod2 name en");
		prod2.setUnit(unit);

		prod3 = productManager.createProduct("prod3");
		catalogManager.setCatalogVersion(prod3, src);
		prod3.setName(deCtx, "prod3 name de");
		prod3.setName(enCtx, "prod3 name en");
		prod3.setUnit(unit);

		final User userSync = UserManager.getInstance().createEmployee("syncUser");
		TypeManager.getInstance().createRestriction("testResttiction", userSync,
				TypeManager.getInstance().getComposedType(Product.class), "{item.code} = 'prod1'");
	}

	@Test
	public void testNewSyncWithRestrictions() throws Exception
	{
		final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
		LOG.info("TestCustomCatalogVersionSyncJob: threads = " + threads);

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "fooNewSync");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(threads));

		ComposedType type = null;
		final TypeManager manager = TypeManager.getInstance();
		type = manager.createComposedType(manager.getComposedType(CatalogVersionSyncJob.class), "TestJob");
		type.setJaloClass(TestCustomCatalogVersionSyncJob.class);
		final CatalogVersionSyncJob testJob = (CatalogVersionSyncJob) type.newInstance(args);
		final CatalogVersionSyncCronJob testCronJob = (CatalogVersionSyncCronJob) testJob.newExcecution();

		testJob.configureFullVersionSync(testCronJob);
		testJob.perform(testCronJob, true);

		assertEquals("Wrong CronJob result! Should be set to 'Success'!", testCronJob.getResult(), testCronJob.getSuccessResult());

		final Product p1tgt = (Product) catalogManager.getSynchronizedCopy(prod1, testJob);
		final Product p2tgt = (Product) catalogManager.getSynchronizedCopy(prod2, testJob);
		final Product p3tgt = (Product) catalogManager.getSynchronizedCopy(prod3, testJob);

		assertNotNull("Product with code: '" + prod1.getCode() + "' have to be synchronized!", p1tgt);
		assertNull("Product with code: '" + prod2.getCode()
				+ "' doesn't have to be synchronized! Search restrictions does not seem to be applied!", p2tgt);
		assertNull("Product with code: '" + prod3.getCode()
				+ "' doesn't have to be synchronized! Search restrictions does not seem to be applied!", p3tgt);
	}

	/**
	 * The sample custom CatalogVersionSyncJob which adds some restriction to the SyncSessionContext(PLA-8677).
	 */
	static public class TestCustomCatalogVersionSyncJob extends CatalogVersionSyncJob
	{
		@Override
		protected SessionContext createSyncSessionContext(final SyncItemCronJob cronJob)
		{
			final SessionContext ctx = super.createSyncSessionContext(cronJob);
			ctx.setUser(UserManager.getInstance().getEmployeeByLogin("syncUser"));
			ctx.removeAttribute(FlexibleSearch.DISABLE_RESTRICTIONS);
			ctx.removeAttribute(FlexibleSearch.DISABLE_RESTRICTION_GROUP_INHERITANCE);
			ctx.removeAttribute(FlexibleSearch.DISABLE_SESSION_ATTRIBUTES);
			return ctx;
		}
	}
}
