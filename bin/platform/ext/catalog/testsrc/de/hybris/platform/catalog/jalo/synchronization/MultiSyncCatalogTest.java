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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * See file '/resources/testcatalogdataForMultisync.csv' which catalogs exist and how the content is!
 */
@IntegrationTest
public class MultiSyncCatalogTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(MultiSyncCatalogTest.class);
	private final CatalogManager catalogmanager = CatalogManager.getInstance();
	private final CategoryManager categorymanager = CategoryManager.getInstance();

	private final FlexibleSearch flexSearch = FlexibleSearch.getInstance();

	@Before
	public void setUp() throws Exception
	{
		new CoreBasicDataCreator().createSupportedEncodings();
		final String csvFile = "/catalog/testcatalogdataForMultisync.csv";
		// importing test csv
		LOG.info("importing resource " + csvFile);
		final InputStream inputStream = HybrisJUnit4Test.class.getResourceAsStream(csvFile);
		final Importer importer = ImpExManager.getInstance().importDataLight(inputStream, "UTF-8", true);
		if (importer.hasUnresolvedLines())
		{
			fail("Import has unresolved lines:\n" + importer.getDumpHandler().getDumpAsString());
		}
		assertFalse(importer.hadError());
	}

	/**
	 * test one single sync job, catalog has NO references to other catalogs
	 */
	@Test
	public void testSimpleSync() throws InterruptedException
	{
		//setup job
		final CatalogVersionSyncJob job = createSyncJob("non_sync_catalog", "cv_nonsync_staged", "cv_nonsync_online", false);
		final CatalogVersionSyncCronJob cronJob = createFullSyncCronJob(job);

		//run a-sync
		job.perform(cronJob, false);
		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cronJob);

		assertEquals(cronJob.getSuccessResult(), cronJob.getResult());

		//check result
		final List<Product> prodlist = flexSearch.search("SELECT {pk} from {product} where {code} = 'P5'", Product.class)
				.getResult();
		assertTrue("There should be exact 2 product after synchronization ", prodlist.size() == 2);
		for (final Product prod : prodlist)
		{
			final Collection<Category> categoryColl = categorymanager.getCategoriesByProduct(prod);
			assertTrue(categoryColl.size() == 1);

			final Category category = categoryColl.iterator().next();
			assertEquals(catalogmanager.getCatalogVersion(prod), catalogmanager.getCatalogVersion(category));
		}
	}

	/**
	 * test one single sync job, catalog HAS references to other catalogs
	 */
	@Test
	public void testSimpleSyncWithExternalReferences() throws InterruptedException
	{
		//setup job 
		final CatalogVersionSyncJob job = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		final CatalogVersionSyncCronJob cronJob = createFullSyncCronJob(job);

		//run synchonosly
		job.perform(cronJob, true);
		assertFalse("cronjob is still running", cronJob.isRunning());
		assertEquals(cronJob.getSuccessResult(), cronJob.getResult());

		//check result
		//products are external, 5 products 
		final Catalog prodcatalog = catalogmanager.getCatalog("productCatalog");
		final Catalog nonsynccatalog = catalogmanager.getCatalog("non_sync_catalog");
		final CatalogVersion prodstaged = prodcatalog.getCatalogVersion("cv_prod_staged");
		final CatalogVersion nonsyncstaged = nonsynccatalog.getCatalogVersion("cv_nonsync_staged");

		final List<Product> prodlist = flexSearch.search("SELECT {pk} from {product}", Product.class).getResult();
		assertTrue(prodlist.size() == 5);
		for (final Product prod : prodlist)
		{
			if (prod.getCode().equals("P5"))
			{
				assertEquals(nonsyncstaged, catalogmanager.getCatalogVersion(prod));
			}
			else
			{
				assertEquals(prodstaged, catalogmanager.getCatalogVersion(prod));
			}
		}
	}

	/**
	 * two catalogs will be synchronized, one catalog has references into the other - result will be additional
	 * references to staged/online at same time
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMultiSyncWithoutJobDependenyButWithCrossReferences() throws InterruptedException
	{
		//setup
		final CatalogVersionSyncJob cat_job = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		final CatalogVersionSyncCronJob cat_cj = createFullSyncCronJob(cat_job);
		final CatalogVersionSyncJob prod_job = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online", false);
		final CatalogVersionSyncCronJob prod_cj = createFullSyncCronJob(prod_job);

		final CatalogVersion cat_cv_staged = cat_job.getSourceVersion();
		final CatalogVersion cat_cv_online = cat_job.getTargetVersion();
		final CatalogVersion prod_cv_online = prod_job.getTargetVersion();
		final CatalogVersion prod_cv_staged = prod_job.getSourceVersion();

		//both jobs run one after one
		cat_job.perform(cat_cj, true);
		prod_job.perform(prod_cj, true);

		assertFalse("cronjob is still running", prod_cj.isRunning() || cat_cj.isRunning());
		assertEquals(cat_cj.getSuccessResult(), cat_cj.getResult());
		assertEquals(prod_cj.getSuccessResult(), prod_cj.getResult());

		//productCatalog was synchronized and categoryCatalog (which has references to priductCatalog) was also synchronized
		//both jobs are not connected to each other -> therefore categoryCatalog has doublereferences/crossreferences
		//here this is only checked
		final List<Category> catlist = flexSearch.search("SELECT {pk} from {category} where {code} = 'HARDWARE'", Category.class)
				.getResult();
		assertTrue(catlist.size() == 2);
		boolean foundCatStaged = false;
		boolean foundCatOnline = false;
		for (final Category cat : catlist)
		{
			if (cat_cv_online.equals(catalogmanager.getCatalogVersion(cat)))
			{
				foundCatOnline = true;
			}
			if (cat_cv_staged.equals(catalogmanager.getCatalogVersion(cat)))
			{
				foundCatStaged = true;
			}
			boolean foundProdStaged = false;
			boolean foundProdOnline = false;
			assertTrue(cat.getProducts().size() == 2);
			for (final Product prod : cat.getProducts())
			{
				if (prod_cv_online.equals(catalogmanager.getCatalogVersion(prod)))
				{
					foundProdOnline = true;
				}
				if (prod_cv_staged.equals(catalogmanager.getCatalogVersion(prod)))
				{
					foundProdStaged = true;
				}
			}

			//each category has online and staged product - because jobs are NOT dependend to each other
			assertTrue(foundProdStaged && foundProdOnline);
		}
		assertTrue(foundCatStaged && foundCatOnline);
	}

	/**
	 * Tests if job correctly aborts if wrong dependent jobs have been configured.
	 */
	@Test
	public void testInvalidDependantJobsSetup() throws InterruptedException
	{
		//setup
		final CatalogVersionSyncJob cat_job1 = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		final CatalogVersionSyncCronJob cat_cj1 = createFullSyncCronJob(cat_job1);
		final CatalogVersionSyncJob cat_job2 = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online_x", true);
		//		final CatalogVersionSyncCronJob cat_cj2 = createFullSyncCronJob(cat_job2);

		cat_job1.setDependentSyncJobs(Collections.singleton(cat_job2));
		cat_job1.perform(cat_cj1, false);
		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cat_cj1);
		assertEquals(cat_cj1.getErrorResult(), cat_cj1.getResult());


		final CatalogVersionSyncJob prod_job1 = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online", false);
		final CatalogVersionSyncCronJob prod_cj1 = createFullSyncCronJob(prod_job1);
		final CatalogVersionSyncJob prod_job2 = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online1", true);
		final CatalogVersionSyncJob prod_job3 = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online2", true);
		final Set<CatalogVersionSyncJob> dependOn = new HashSet<CatalogVersionSyncJob>();
		dependOn.add(prod_job2);
		dependOn.add(prod_job3);
		prod_job1.setDependsOnSyncJobs(dependOn);

		prod_job1.perform(prod_cj1, false);
		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, prod_cj1);
		assertEquals(prod_cj1.getErrorResult(), prod_cj1.getResult());
	}


	/**
	 * multisynctest: create for all catalogs sync jobs and connect them to each other (all 3 belongs together). The
	 * online categories should only include online products now and viseversa with the staged version
	 * 
	 */
	@Test
	public void testBigMultiSyncJobsRunningSameTimeDependenceByCatJob() throws InterruptedException
	{
		//setup
		final CatalogVersionSyncJob cat_job = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		CatalogVersionSyncCronJob cat_cj = createFullSyncCronJob(cat_job);
		final CatalogVersionSyncJob prod_job = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online", false);
		CatalogVersionSyncCronJob prod_cj = createFullSyncCronJob(prod_job);
		final CatalogVersionSyncJob nonsync_job = createSyncJob("non_sync_catalog", "cv_nonsync_staged", "cv_nonsync_online", false);
		CatalogVersionSyncCronJob nonsync_cj = createFullSyncCronJob(nonsync_job);

		//create dependencies
		final Set<CatalogVersionSyncJob> xxx = new HashSet<CatalogVersionSyncJob>();
		xxx.add(prod_job);
		xxx.add(nonsync_job);
		cat_job.setDependentSyncJobs(xxx);

		//perform all independently and together
		cat_job.perform(cat_cj, false);
		prod_job.perform(prod_cj, false);
		nonsync_job.perform(nonsync_cj, false);

		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cat_cj, prod_cj, nonsync_cj);
		assertFalse("cronjob is still running", prod_cj.isRunning() || cat_cj.isRunning() || nonsync_cj.isRunning());


		if (cat_cj.getResult().equals(cat_cj.getFailureResult()) || prod_cj.getResult().equals(prod_cj.getFailureResult())
				|| nonsync_cj.getResult().equals(nonsync_cj.getFailureResult()))
		{
			LOG.info("result cat_cj:" + cat_cj.getResult());
			LOG.info("result prod_cj:" + prod_cj.getResult());
			LOG.info("result nonsync_cj:" + nonsync_cj.getResult());
			LOG.info("create new cronjobs for sync and restart all again");
			cat_cj = createFullSyncCronJob(cat_job);
			prod_cj = createFullSyncCronJob(prod_job);
			nonsync_cj = createFullSyncCronJob(nonsync_job);

			cat_job.perform(cat_cj, false);
			prod_job.perform(prod_cj, false);
			nonsync_job.perform(nonsync_cj, false);

			checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cat_cj, prod_cj, nonsync_cj);
		}

		assertFalse("cronjob is still running", prod_cj.isRunning() || cat_cj.isRunning() || nonsync_cj.isRunning());
		assertEquals("cat_cj was not sucessful! ", cat_cj.getSuccessResult(), cat_cj.getResult());
		assertEquals("prod_cj was not sucessful! ", prod_cj.getSuccessResult(), prod_cj.getResult());
		assertEquals("nonsync_cj was not sucessful! ", nonsync_cj.getSuccessResult(), nonsync_cj.getResult());

		checkCategories(12);
		checkProducts(10);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testBigMultiSyncJobsRunningSameTimeDependenceByNonSyncJob() throws InterruptedException
	{
		//setup
		final CatalogVersionSyncJob cat_job = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		CatalogVersionSyncCronJob cat_cj = createFullSyncCronJob(cat_job);
		final CatalogVersionSyncJob prod_job = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online", false);
		CatalogVersionSyncCronJob prod_cj = createFullSyncCronJob(prod_job);
		final CatalogVersionSyncJob nonsync_job = createSyncJob("non_sync_catalog", "cv_nonsync_staged", "cv_nonsync_online", false);
		CatalogVersionSyncCronJob nonsync_cj = createFullSyncCronJob(nonsync_job);

		//set dependences
		final Set<CatalogVersionSyncJob> xxx = new HashSet<CatalogVersionSyncJob>();
		xxx.add(nonsync_job);
		xxx.add(cat_job);
		prod_job.setDependsOnSyncJobs(xxx);

		//perform all together and in async
		cat_job.perform(cat_cj, false);
		prod_job.perform(prod_cj, false);
		nonsync_job.perform(nonsync_cj, false);
		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cat_cj, prod_cj, nonsync_cj);

		assertFalse(prod_cj.getCode() + " is still running", prod_cj.isRunning());
		assertFalse(cat_cj.getCode() + " is still running", cat_cj.isRunning());
		assertFalse(nonsync_cj.getCode() + " is still running", nonsync_cj.isRunning());

		// run all the failure cronjobs again
		cat_cj = runAgainIfNotSuccessful(cat_job, cat_cj, 30, 3 * 60);
		prod_cj = runAgainIfNotSuccessful(prod_job, prod_cj, 30, 3 * 60);
		nonsync_cj = runAgainIfNotSuccessful(nonsync_job, nonsync_cj, 30, 3 * 60);

		// final check
		assertFalse("cronjob is still running", prod_cj.isRunning() || cat_cj.isRunning() || nonsync_cj.isRunning());
		assertEquals(cat_cj.getSuccessResult(), cat_cj.getResult());
		assertEquals(prod_cj.getSuccessResult(), prod_cj.getResult());
		assertEquals(nonsync_cj.getSuccessResult(), nonsync_cj.getResult());

		checkCategories(12);
		checkProducts(10);
	}

	private CatalogVersionSyncCronJob runAgainIfNotSuccessful(final CatalogVersionSyncJob job,
			final CatalogVersionSyncCronJob prevCj, final int startTimeoutSec, final int waitTimeoutSec) throws InterruptedException
	{
		if (!prevCj.getSuccessResult().equals(prevCj.getResult()))
		{
			LOG.info("running " + job.getCode() + " again since previous job has not been successful but " + prevCj.getResult());
			final CatalogVersionSyncCronJob cronJob = createFullSyncCronJob(job);
			job.perform(cronJob, false);
			checkExecutionOfAsynchronousJobs(startTimeoutSec * 1000, waitTimeoutSec * 1000, cronJob);
			return cronJob;
		}
		else
		{
			return prevCj;
		}
	}

	/**
	 * multisynctest: create for all catalogs sync jobs and connect them to each other (all 3 belongs together). Same as
	 * test above but the jobs are executed step by step
	 * 
	 */
	@Test
	public void testBigMultiSyncJobsRunningJobByJob() throws InterruptedException
	{
		final CatalogVersionSyncJob cat_job = createSyncJob("categoryCatalog", "cv_cat_staged", "cv_cat_online", false);
		final CatalogVersionSyncCronJob cat_cj = createFullSyncCronJob(cat_job);
		final CatalogVersionSyncJob prod_job = createSyncJob("productCatalog", "cv_prod_staged", "cv_prod_online", false);
		final CatalogVersionSyncCronJob prod_cj = createFullSyncCronJob(prod_job);
		final CatalogVersionSyncJob nonsync_job = createSyncJob("non_sync_catalog", "cv_nonsync_staged", "cv_nonsync_online", false);
		final CatalogVersionSyncCronJob nonsync_cj = createFullSyncCronJob(nonsync_job);

		final Set<CatalogVersionSyncJob> xxx = new HashSet<CatalogVersionSyncJob>();
		xxx.add(cat_job);
		xxx.add(nonsync_job);
		prod_job.setDependentSyncJobs(xxx);

		startSingleSync(cat_job, cat_cj, true);
		startSingleSync(prod_job, prod_cj, false);
		startSingleSync(nonsync_job, nonsync_cj, false);


		//sync of category catalog will fail first time, but then not anymore
		startSingleSync(cat_job, cat_cj, false);
		checkExecutionOfAsynchronousJobs(30 * 1000, 3 * 60 * 1000, cat_cj);
		checkCategories(12);
		checkProducts(10);

	}

	private void startSingleSync(final CatalogVersionSyncJob job, final CatalogVersionSyncCronJob cronjob, final boolean willFail)
			throws InterruptedException
	{
		job.perform(cronjob, true);

		assertFalse("cronjob is still running", cronjob.isRunning());
		assertEquals(willFail ? cronjob.getFailureResult() : cronjob.getSuccessResult(), cronjob.getResult());
	}


	private void checkProducts(final int expectedProdSize)
	{
		final FlexibleSearch flex = FlexibleSearch.getInstance();
		final List<Product> prodlist = flex.search("SELECT {pk} from {product}", Product.class).getResult();
		assertTrue(prodlist.size() == expectedProdSize);
		for (final Product prod : prodlist)
		{
			final boolean prodIsFromStaged = catalogmanager.getCatalogVersion(prod).getVersion().contains("staged");
			final boolean prodIsFromOnline = catalogmanager.getCatalogVersion(prod).getVersion().contains("online");
			assertTrue(prodIsFromStaged ^ prodIsFromOnline);

			for (final Category cat : categorymanager.getCategoriesByProduct(prod))
			{
				assertTrue(prodIsFromStaged == catalogmanager.getCatalogVersion(cat).getVersion().contains("staged"));
				assertTrue(prodIsFromOnline == catalogmanager.getCatalogVersion(cat).getVersion().contains("online"));
			}
		}
	}

	private void checkCategories(final int expectedCatSize)
	{
		final FlexibleSearch flex = FlexibleSearch.getInstance();
		final List<Category> catlist = flex.search("SELECT {pk} from {category}", Category.class).getResult();
		assertTrue(catlist.size() == expectedCatSize);
		for (final Category cat : catlist)
		{
			final boolean catIsFromStaged = catalogmanager.getCatalogVersion(cat).getVersion().contains("staged");
			final boolean catIsFromOnline = catalogmanager.getCatalogVersion(cat).getVersion().contains("online");
			assertTrue(catIsFromStaged ^ catIsFromOnline);

			for (final Product prod : cat.getProducts())
			{
				assertTrue(catIsFromStaged == catalogmanager.getCatalogVersion(prod).getVersion().contains("staged"));
				assertTrue(catIsFromOnline == catalogmanager.getCatalogVersion(prod).getVersion().contains("online"));
			}
			for (final Category subcat : cat.getSubcategories())
			{
				assertTrue(catIsFromStaged == catalogmanager.getCatalogVersion(subcat).getVersion().contains("staged"));
				assertTrue(catIsFromOnline == catalogmanager.getCatalogVersion(subcat).getVersion().contains("online"));
			}
		}
	}

	private CatalogVersionSyncJob createSyncJob(final String catalogname, final String srcCVname, final String trgCVname,
			final boolean createIfNotExists)
	{
		Assert.assertNotNull(catalogname);
		Assert.assertNotNull(srcCVname);
		Assert.assertNotNull(trgCVname);

		Catalog catalog = catalogmanager.getCatalog(catalogname);
		if (catalog == null && createIfNotExists)
		{
			catalog = catalogmanager.createCatalog(catalogname);
		}
		Assert.assertNotNull(catalog);

		CatalogVersion src = catalog.getCatalogVersion(srcCVname);
		if (src == null && createIfNotExists)
		{
			src = catalogmanager.createCatalogVersion(catalog, srcCVname, null);
		}
		Assert.assertNotNull(src);

		CatalogVersion trg = catalog.getCatalogVersion(trgCVname);
		if (trg == null && createIfNotExists)
		{
			trg = catalogmanager.createCatalogVersion(catalog, trgCVname, null);
		}
		Assert.assertNotNull(trg);

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, catalog.getId() + ": " + src.getVersion() + "->" + trg.getVersion());
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, trg);
		final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(threads));
		return catalogmanager.createCatalogVersionSyncJob(args);
	}

	private CatalogVersionSyncCronJob createFullSyncCronJob(final CatalogVersionSyncJob job)
	{
		final CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob) job.newExecution();
		job.configureFullVersionSync(cronJob);
		return cronJob;
	}

	private void checkExecutionOfAsynchronousJobs(final long startTimeOutInMilis, final long executionTimeOutInMilis,
			final CatalogVersionSyncCronJob... cronjobs) throws InterruptedException
	{
		assertAllStarted(startTimeOutInMilis, cronjobs);
		assertAllFinished(executionTimeOutInMilis, cronjobs);
	}

	public void assertAllFinished(final long endTimeOutInMilis, final CatalogVersionSyncCronJob... cronjobs)
			throws InterruptedException
	{
		final long timeoutTime = System.currentTimeMillis() + endTimeOutInMilis;
		//check if the cronjob had started
		while (!areAllOfThemFinished(cronjobs) && System.currentTimeMillis() < timeoutTime)
		{
			Thread.sleep(500);
		}
		assertTrue(areAllOfThemFinished(cronjobs));
	}

	private void assertAllStarted(final long startTimeOutInMilis, final CatalogVersionSyncCronJob... cronjobs)
			throws InterruptedException
	{
		final long timeoutTime = System.currentTimeMillis() + startTimeOutInMilis;
		//check if the cronjob had started
		while (!allStartedOrFinished(cronjobs) && System.currentTimeMillis() < timeoutTime)
		{
			Thread.sleep(500);
		}
		assertTrue(allStartedOrFinished(cronjobs));
	}

	private boolean allStartedOrFinished(final CatalogVersionSyncCronJob... cronjobs)
	{
		for (final CatalogVersionSyncCronJob cronjob : cronjobs)
		{
			if (!(cronjob.isRunning() || (cronjob.isFinished() || cronjob.getAbortedStatus().equals(cronjob.getStatus()))))
			{
				return false;
			}
		}
		return true;
	}

	private boolean areAllOfThemFinished(final CatalogVersionSyncCronJob... cronjobs)
	{
		for (final CatalogVersionSyncCronJob cronjob : cronjobs)
		{
			if (!(cronjob.isFinished() || cronjob.getAbortedStatus().equals(cronjob.getStatus())))
			{
				return false;
			}
		}
		return true;
	}


}
