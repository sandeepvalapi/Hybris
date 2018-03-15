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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class NewSyncTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(NewSyncTest.class.getName());

	private static final int level1Count = 10;
	private static final int level2Count = 50;
	private static final int level3Count = 100;
	private static final int productsCount = 100;

	private CatalogVersion src;
	private CatalogVersion tgt;
	private List<Category> level1;
	private List<Category> level2;
	private List<Category> level3;
	private List<Product> products;

	private Catalog cat = null;
	private Product product1, product2;

	final Product product3 = null;
	private final Category category = null;
	private final ProductReference reference = null;

	private CatalogVersionSyncJob job = null;
	private CatalogVersionSyncCronJob cron = null;


	private void tearDown(final Item... items)
	{
		for (final Item item : items)
		{
			try
			{
				if (item != null)
				{
					item.remove();
				}
			}
			catch (final Exception e)
			{
				LOG.info(e.getMessage());
			}
		}
	}

	@After
	public void tearDown()
	{
		tearDown(cat, product1, product2, product3, cron, category, reference, src, tgt);
	}

	protected void fillCatalog() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final CategoryManager categoryManager = CategoryManager.getInstance();

		final Catalog catalog = CatalogManager.getInstance().createCatalog("foo");
		src = CatalogManager.getInstance().createCatalogVersion(catalog, "bar", null);
		src.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
		tgt = CatalogManager.getInstance().createCatalogVersion(catalog, "ba2r", null);
		tgt.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		level1 = new ArrayList<Category>(level1Count);
		level2 = new ArrayList<Category>(level2Count);
		level3 = new ArrayList<Category>(level3Count);

		final Map args = new HashMap();
		args.put(CatalogConstants.Attributes.Category.CATALOGVERSION, src);
		for (int i = 0; i < level1Count + level2Count + level3Count; i++)
		{
			// alternate between 3, 2 and 1 to generate a more shuffled schedule
			final int level;
			if (level1.size() < level1Count)
			{
				level = 3 - (i % 3);
			}
			else if (level2.size() < level2Count)
			{
				level = 3 - (i % 2);
			}
			else
			{
				level = 3;
			}
			switch (level)
			{
				case 1:
					args.put(Category.CODE, "Level1-" + (level1.size() + 1));
					level1.add(categoryManager.createCategory(args));
					break;
				case 2:
					args.put(Category.CODE, "Level2-" + (level2.size() + 1));
					level2.add(categoryManager.createCategory(args));
					break;
				case 3:
					args.put(Category.CODE, "Level3-" + (level3.size() + 1));
					level3.add(categoryManager.createCategory(args));
					break;
			}
		}

		for (int i = level1Count - 1; i >= 0; i--)
		{
			final Category level1Cat = level1.get(i);
			level1Cat.setSubcategories(level2);
		}

		for (int i = level2Count - 1; i >= 0; i--)
		{
			final Category level2Cat = level2.get(i);
			level2Cat.setSubcategories(level3);
		}

		products = new ArrayList<Product>(productsCount);

		final ComposedType composedType = TypeManager.getInstance().getComposedType(Product.class);
		Product prod;
		args.clear();
		for (int i = 0; i < productsCount; i++)
		{
			args.put(Product.CODE, "Product-" + i);
			args.put(CatalogConstants.Attributes.Product.CATALOGVERSION, src);
			prod = (Product) composedType.newInstance(args);
			products.add(prod);
		}

		for (int i = level3Count - 1; i >= 0; i--)
		{
			final Category level3Cat = level3.get(i);
			level3Cat.setProducts(products);
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPLA9466() throws Exception
	{
		cat = CatalogManager.getInstance().createCatalog("pla9467-cat");
		src = CatalogManager.getInstance().createCatalogVersion(cat, "pla9467-src", null);
		src.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
		tgt = CatalogManager.getInstance().createCatalogVersion(cat, "pla9467-tgt", null);
		tgt.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		final ComposedType pType = TypeManager.getInstance().getComposedType(Product.class);
		final AttributeDescriptor attributeDescriptor = pType.createAttributeDescriptor("testProductRef", pType,
				AttributeDescriptor.WRITE_FLAG + AttributeDescriptor.INITIAL_FLAG + AttributeDescriptor.PROPERTY_FLAG);

		product1 = ProductManager.getInstance().createProduct("pla9467-1");
		CatalogManager.getInstance().setCatalogVersion(product1, src);

		product2 = ProductManager.getInstance().createProduct("pla9467-2");
		CatalogManager.getInstance().setCatalogVersion(product2, src);
		product2.setProperty(attributeDescriptor.getQualifier(), product1);

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "pla9467-job");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(1));
		job = CatalogManager.getInstance().createCatalogVersionSyncJob(args);

		cron = (CatalogVersionSyncCronJob) job.newExecution();

		cron.addPendingItem(product2, null);
		cron.addPendingItem(product1, null);

		job.perform(cron, true);

		assertEquals(cron.getSuccessResult(), cron.getResult());

		assertNotNull(CatalogManager.getInstance().getCounterpartItem(product2, tgt));
		assertNotNull(CatalogManager.getInstance().getCounterpartItem(product1, tgt));
	}

	@Test
	public void testPendingItems()
	{
		final CatalogManager catalogManager = CatalogManager.getInstance();

		final Catalog cat = CatalogManager.getInstance().createCatalog("foo2");
		final CatalogVersion src = CatalogManager.getInstance().createCatalogVersion(cat, "bar2", null);
		src.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
		final CatalogVersion tgt = CatalogManager.getInstance().createCatalogVersion(cat, "ba2r2", null);
		tgt.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		final Product product = ProductManager.getInstance().createProduct("ppp");
		CatalogManager.getInstance().setCatalogVersion(product, src);

		final Category category = CategoryManager.getInstance().createCategory("ccc");
		CatalogManager.getInstance().setCatalogVersion(category, src);

		category.setProducts(Collections.singletonList(product));

		assertEquals(Collections.singletonList(category), CategoryManager.getInstance().getSupercategories(product));

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "foo2");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		final CatalogVersionSyncJob job = catalogManager.createCatalogVersionSyncJob(args);
		CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob) job.newExecution();

		// just schedule p -> should be still pending after sync due to missing reference to c
		cronJob.addPendingItems(Collections.singletonList(new PK[]
		{ product.getPK(), null, null }));

		job.perform(cronJob, true);

		final EnumerationValue fail = cronJob.getFailureResult();
		Assert.assertEquals(fail, cronJob.getResult());

		final Product product2 = (Product) catalogManager.getCounterpartItem(product, tgt);

		assertNotNull(product2);
		assertEquals(product.getCode(), product2.getCode());
		assertEquals(tgt, catalogManager.getCatalogVersion(product2));
		assertEquals(Collections.EMPTY_LIST, CategoryManager.getInstance().getSupercategories(product2));
		assertNull(catalogManager.getCounterpartItem(category, tgt));

		final Map<PK, ItemSyncTimestamp> timestamps = catalogManager.getSyncTimestampMap(product, job);
		assertEquals(1, timestamps.size());
		final ItemSyncTimestamp itemSyncTimestamp = timestamps.entrySet().iterator().next().getValue();
		assertTrue(itemSyncTimestamp.isOutdatedAsPrimitive());

		cronJob = (CatalogVersionSyncCronJob) job.newExecution();
		job.configureFullVersionSync(cronJob);

		final SyncScheduleReader scheduleReader = cronJob.createSyncScheduleReader();
		boolean foundP = false;
		final PK srcPK = product.getPK();
		final PK tgtPK = product2.getPK();
		final PK tsPK = itemSyncTimestamp.getPK();
		while (scheduleReader.readNextLine())
		{
			final SyncSchedule schedule = scheduleReader.getScheduleFromLine();
			if (srcPK.equals(schedule.getSrcPK()))
			{
				assertEquals(tgtPK, schedule.getTgtPK());
				assertEquals(tsPK, schedule.getTimestampPK());
				foundP = true;
			}
		}
			assertTrue(foundP);
		}
}
