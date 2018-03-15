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
package de.hybris.platform.impex.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.util.CSVReader;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 */
@PerformanceTest
public class ImpExMultiThreadedPerformanceTest extends AbstractImpExTest
{
	private static final Logger LOG = Logger.getLogger(ImpExMultiThreadedPerformanceTest.class);

	private final static int MAX10000 = 10000;
	private final static int MAX3000 = 3000;
	private final static int MULTITHREAD_FACTOR = 4;

	@SuppressWarnings("deprecation")
	@Test
	public void testUnitImportBig() throws ImpExException
	{
		//creating importscript for importing MAX10000 units
		final StringBuilder unitImportScript = new StringBuilder();

		unitImportScript.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		for (int index = 0; index < MAX10000; index++)
		{
			unitImportScript.append("; unit_" + index + "; unittype; \n");
		}

		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(unitImportScript.toString()));
		final Set<PK> unitPKs = new HashSet<PK>(MAX10000); //checklist if really those items where imported

		LOG.info("Multi-threaded (" + mImp.getMaxThreads() + ") import of " + MAX10000 + " units...");
		final long starttime = System.currentTimeMillis();
		for (Item p = mImp.importNext(); p != null; p = mImp.importNext())
		{
			unitPKs.add(p.getPK());
		}
		final long endtime = System.currentTimeMillis();
		LOG.info("done in " + (endtime - starttime) + " ms after " + mImp.getCurrentPass() + " passes.");
		assertFalse("", mImp.hadError());
		assertFalse("", mImp.isAborted());
		assertFalse("", mImp.isRunning());
		assertTrue("", mImp.isFinished());
		assertEquals("checklist didn't contains " + MAX10000 + " unit pks", MAX10000, unitPKs.size());
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testImport() throws ImpExException
	{
		//##############################
		//create import scripts
		//##############################

		//create import script for single thread
		final StringBuilder productSingleThreadImportScript = new StringBuilder();

		productSingleThreadImportScript.append("INSERT Product; " + ProductModel.CODE + "[unique=true]; ")
				.append(ProductModel.NAME + "[lang=de]; " + ProductModel.NAME + "[lang=en]; ")
				.append(ProductModel.UNIT + "(" + Unit.CODE + "); ").append("catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		for (int i = 0; i < MAX3000; i++)
		{
			productSingleThreadImportScript.append("; product-1-").append(i).append(";name-de-").append(i).append(";name-en-")
					.append(i).append(";foo1").append('\n');
		}

		productSingleThreadImportScript.append("INSERT Product; " + ProductModel.CODE + "[unique=true]; ")
				.append(ProductModel.NAME + "[lang=de]; " + ProductModel.NAME + "[lang=en]; ")
				.append(ProductModel.UNIT + "(" + Unit.CODE + ");variantType(code) ;")
				.append("catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		// put unit at end to cause all products to be dumped
		productSingleThreadImportScript.append("# trallala \n");
		productSingleThreadImportScript.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		productSingleThreadImportScript.append("; foo1; bar \n");

		//create import script for multi thread
		final StringBuilder productMultiThreadImportScript = new StringBuilder();

		productMultiThreadImportScript.append("INSERT Product; " + ProductModel.CODE + "[unique=true]; ")
				.append(ProductModel.NAME + "[lang=de]; " + ProductModel.NAME + "[lang=en]; ")
				.append(ProductModel.UNIT + "(" + Unit.CODE + "); ").append("catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		for (int i = 0; i < MAX3000 * MULTITHREAD_FACTOR; i++)
		{
			productMultiThreadImportScript.append("; product-2-").append(i).append(";name-de-").append(i).append(";name-en-")
					.append(i).append(";foo1").append('\n');
		}

		productMultiThreadImportScript.append("INSERT Product; " + ProductModel.CODE + "[unique=true]; ")
				.append(ProductModel.NAME + "[lang=de]; " + ProductModel.NAME + "[lang=en]; ")
				.append(ProductModel.UNIT + "(" + Unit.CODE + ");variantType(code) ;")
				.append("catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		// put unit at end to cause all products to be dumped
		productMultiThreadImportScript.append("# trallala \n");
		productMultiThreadImportScript.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		productMultiThreadImportScript.append("; foo2; bar \n");


		//##############################
		//test
		//##############################

		final Importer imp = new Importer(new CSVReader(productSingleThreadImportScript.toString()));
		final Set<PK> controlSetSingleThread = new LinkedHashSet<PK>(MAX3000);

		LOG.info("Single-threaded import of " + MAX3000 + " products...");
		final long startTimeSingleThread = System.currentTimeMillis();
		for (Item p = imp.importNext(); p != null; p = imp.importNext())
		{
			if (p instanceof Product)
			{
				controlSetSingleThread.add(p.getPK());
			}
		}
		final long endTimeSingleThread = System.currentTimeMillis();
		LOG.info("done in " + (endTimeSingleThread - startTimeSingleThread) + " ms after " + imp.getCurrentPass()
				+ " passes. Rate is " + ((MAX3000 * 1000) / (endTimeSingleThread - startTimeSingleThread)) + " items/s");

		assertFalse("", imp.hadError());
		assertFalse("", imp.isAborted());
		assertFalse("", imp.isRunning());
		assertTrue("", imp.isFinished());
		assertEquals("", MAX3000, controlSetSingleThread.size());
		assertEquals("", MAX3000, imp.getDumpedLineCountOverall());

		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(productMultiThreadImportScript.toString()));
		mImp.setMaxThreads(Math.max(4, ImpExImportJob.getDefaultMaxThreads(Registry.getCurrentTenant())));
		final Set<PK> controlSetMultiThread = new LinkedHashSet<PK>(MAX3000 * MULTITHREAD_FACTOR);

		LOG.info("Multi-threaded (" + mImp.getMaxThreads() + ") import of " + (MAX3000 * MULTITHREAD_FACTOR) + " products...");
		final long statTimeMultiThread = System.currentTimeMillis();
		for (Item p = mImp.importNext(); p != null; p = mImp.importNext())
		{
			if (p instanceof Product)
			{
				controlSetMultiThread.add(p.getPK());
			}
		}
		final long endTimeMultiThread = System.currentTimeMillis();
		LOG.info("done in " + (endTimeMultiThread - statTimeMultiThread) + " ms after " + mImp.getCurrentPass()
				+ " passes. Rate is " + ((MAX3000 * MULTITHREAD_FACTOR * 1000) / (endTimeMultiThread - statTimeMultiThread))
				+ " items/s");

		assertFalse("", mImp.hadError());
		assertFalse("", mImp.isAborted());
		assertFalse("", mImp.isRunning());
		assertTrue("", mImp.isFinished());
		assertEquals("", MAX3000 * MULTITHREAD_FACTOR, controlSetMultiThread.size());
		assertTrue("", MAX3000 * MULTITHREAD_FACTOR + 1 >= mImp.getDumpedLineCountOverall());

		final ProductManager prodman = ProductManager.getInstance();
		for (int i = 0; i < MAX3000 * MULTITHREAD_FACTOR; i++)
		{
			assertNotNull(prodman.getProductsByCode("product-2-" + i));
		}

		LOG.info("Difference single-multi = "
				+ (((endTimeMultiThread - statTimeMultiThread) * 100) / (MULTITHREAD_FACTOR * (endTimeSingleThread - startTimeSingleThread)))
				+ " % ");
	}

}
