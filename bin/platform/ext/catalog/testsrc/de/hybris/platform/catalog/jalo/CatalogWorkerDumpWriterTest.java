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
package de.hybris.platform.catalog.jalo;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncScheduleMedia;
import de.hybris.platform.catalog.jalo.synchronization.SyncSchedule;
import de.hybris.platform.catalog.jalo.synchronization.SyncScheduleReader;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogWorkerDumpWriterTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(CatalogWorkerDumpWriterTest.class.getName());

	private CatalogVersion source;
	private CatalogVersion target;
	private Catalog catalog = null;
	private Product product1, product2, product3, product4;
	private Media media1, media2, media3, media4;

	private Category category1, category2;

	private CatalogVersionSyncJob job = null;

	@Before
	public void setUp()
	{
		createCatalog();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testWorkerDump() throws Exception
	{
		// define the pk's to look for
		final List<PK> pks = new ArrayList<PK>();
		pks.add(product1.getPK());
		pks.add(product2.getPK());
		pks.add(product3.getPK());
		pks.add(product4.getPK());
		pks.add(media1.getPK());
		pks.add(media2.getPK());
		pks.add(media3.getPK());
		pks.add(media4.getPK());
		pks.add(category1.getPK());
		pks.add(category2.getPK());

		assertEquals(pks.size(), 10);

		final String categoryTC = category1.getPK().getTypeCodeAsString();
		final String productTC = product1.getPK().getTypeCodeAsString();
		final String mediaTC = media1.getPK().getTypeCodeAsString();

		// define the 'type' order
		final List<String> typeCodes = new ArrayList<String>();
		for (int i = 0; i < 2; i++)
		{
			typeCodes.add(categoryTC);
		}
		for (int i = 0; i < 4; i++)
		{
			typeCodes.add(productTC);
		}
		for (int i = 0; i < 4; i++)
		{
			typeCodes.add(mediaTC);
		}

		assertEquals(typeCodes.size(), 10);

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "test-job_" + new Date());
		args.put(CatalogVersionSyncJob.SOURCEVERSION, source);
		args.put(CatalogVersionSyncJob.TARGETVERSION, target);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(2));
		job = CatalogManager.getInstance().createCatalogVersionSyncJob(args);
		final CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob) job.newExecution();
		job.configureFullVersionSync(cronJob);

		for (final CatalogVersionSyncScheduleMedia syncMedia : cronJob.getScheduleMedias())
		{
			final File file = (File) syncMedia.getFiles().iterator().next();
			final SyncScheduleReader reader = new SyncScheduleReader(new FileReader(file), -1);
			int index = 0;
			while (reader.readNextLine())
			{
				final SyncSchedule line = reader.getScheduleFromLine();
				final PK syncpk = line.getSrcPK();
				assertEquals(typeCodes.get(index++), syncpk.getTypeCodeAsString()); // checking of correct 'type' order
				pks.remove(syncpk); // found, so let's remove id from the 'created instances' list
			}
		}
		assertEquals(pks.size(), 0); // nothing left -> so everything was 'found' before
	}


	@After
	public void tearDown()
	{
		try
		{
			if (catalog != null)
			{
				catalog.remove();
			}
			if (product1 != null)
			{
				product1.remove();
			}
			if (product2 != null)
			{
				product2.remove();
			}
			if (product3 != null)
			{
				product3.remove();
			}
			if (product4 != null)
			{
				product4.remove();
			}
			if (job != null)
			{
				job.remove();
			}
			if (media1 != null)
			{
				media1.remove();
			}
			if (media2 != null)
			{
				media2.remove();
			}
			if (media3 != null)
			{
				media3.remove();
			}
			if (media4 != null)
			{
				media4.remove();
			}
			if (category1 != null)
			{
				category1.remove();
			}
			if (category2 != null)
			{
				category2.remove();
			}
			if (source != null)
			{
				source.remove();
			}
			if (target != null)
			{
				target.remove();
			}
		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage());
		}
	}

	private void createCatalog()
	{
		catalog = CatalogManager.getInstance().createCatalog("test-catalog");

		source = CatalogManager.getInstance().createCatalogVersion(catalog, "test-source", null);
		source.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		target = CatalogManager.getInstance().createCatalogVersion(catalog, "test-target", null);
		target.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		category1 = CategoryManager.getInstance().createCategory("test-category1");
		CatalogManager.getInstance().setCatalogVersion(category1, source);

		category2 = CategoryManager.getInstance().createCategory("test-category2");
		CatalogManager.getInstance().setCatalogVersion(category2, source);

		product1 = ProductManager.getInstance().createProduct("test-product1");
		CatalogManager.getInstance().setCatalogVersion(product1, source);
		category1.addProduct(product1);

		product2 = ProductManager.getInstance().createProduct("test-product-2");
		CatalogManager.getInstance().setCatalogVersion(product2, source);
		category1.addProduct(product2);

		product3 = ProductManager.getInstance().createProduct("test-product-3");
		CatalogManager.getInstance().setCatalogVersion(product3, source);
		category2.addProduct(product3);

		product4 = ProductManager.getInstance().createProduct("test-product-4");
		CatalogManager.getInstance().setCatalogVersion(product4, source);
		category2.addProduct(product4);

		media1 = MediaManager.getInstance().createMedia("media1");
		CatalogManager.getInstance().setCatalogVersion(media1, source);
		product1.setPicture(media1);

		media2 = MediaManager.getInstance().createMedia("media2");
		CatalogManager.getInstance().setCatalogVersion(media2, source);
		product2.setPicture(media2);

		media3 = MediaManager.getInstance().createMedia("media3");
		CatalogManager.getInstance().setCatalogVersion(media3, source);
		product3.setPicture(media3);

		media4 = MediaManager.getInstance().createMedia("media4");
		CatalogManager.getInstance().setCatalogVersion(media4, source);
		product4.setPicture(media4);
	}
}
