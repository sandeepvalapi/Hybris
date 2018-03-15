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
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class NewSyncExclusiveTest extends HybrisJUnit4Test
{
	private CatalogVersion src, tgt;
	private Product product1, product2, product3;
	private SessionContext deCtx, enCtx;
	private Unit unit;

	private CatalogManager catalogManager;

	@Before
	public void setUp()
	{
		catalogManager = CatalogManager.getInstance();

		final Catalog catalog = catalogManager.createCatalog("foo");
		src = catalogManager.createCatalogVersion(catalog, "bar", null);
		src.setLanguages(Arrays.asList(getOrCreateLanguage("de"), getOrCreateLanguage("en")));
		tgt = catalogManager.createCatalogVersion(catalog, "ba2r", null);
		tgt.setLanguages(Arrays.asList(getOrCreateLanguage("de"), getOrCreateLanguage("en")));

		deCtx = jaloSession.createSessionContext();
		deCtx.setLanguage(getOrCreateLanguage("de"));

		enCtx = jaloSession.createSessionContext();
		enCtx.setLanguage(getOrCreateLanguage("en"));

		final ProductManager productManager = ProductManager.getInstance();
		unit = productManager.createUnit("foo", "bar");

		product1 = productManager.createProduct("product1");
		catalogManager.setCatalogVersion(product1, src);
		product1.setName(deCtx, "product1 name de");
		product1.setName(enCtx, "product1 name en");
		product1.setUnit(unit);

		product2 = productManager.createProduct("product2");
		catalogManager.setCatalogVersion(product2, src);
		product2.setName(deCtx, "product2 name de");
		product2.setName(enCtx, "product2 name en");
		product2.setUnit(unit);

		product3 = productManager.createProduct("product3");
		catalogManager.setCatalogVersion(product3, src);
		product3.setName(deCtx, "product3 name de");
		product3.setName(enCtx, "product3 name en");
		product3.setUnit(unit);

	}

	@Test
	public void testExclusiveSync()
	{
		assertNull(catalogManager.getCounterpartItem(product1, tgt));
		assertNull(catalogManager.getCounterpartItem(product2, tgt));
		assertNull(catalogManager.getCounterpartItem(product3, tgt));

		final ComposedType pType = TypeManager.getInstance().getComposedType(Product.class);
		final AttributeDescriptor nameAd = pType.getAttributeDescriptorIncludingPrivate(Product.NAME);
		final AttributeDescriptor unitAd = pType.getAttributeDescriptorIncludingPrivate(Product.UNIT);

		final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;

		final CatalogVersionSyncJob exclJob1 = createSyncJob("excl1", src, tgt, threads, true);
		exclJob1.setCreateNewItems(false); // don't create new items in full sync
		SyncAttributeDescriptorConfig nameCfg = exclJob1.getConfigFor(nameAd, true);
		nameCfg.setIncludedInSync(true); // +name
		SyncAttributeDescriptorConfig unitCfg = exclJob1.getConfigFor(unitAd, true);
		unitCfg.setIncludedInSync(false); // -unit

		final CatalogVersionSyncJob exclJob2 = createSyncJob("excl2", src, tgt, threads, true);
		exclJob2.setCreateNewItems(true); // don't create new items in full sync
		nameCfg = exclJob2.getConfigFor(nameAd, true);
		nameCfg.setIncludedInSync(false); // -name
		unitCfg = exclJob2.getConfigFor(unitAd, true);
		unitCfg.setIncludedInSync(true); // +unit

		// 1. run job1 -> no item should be copied

		final CatalogVersionSyncCronJob run1 = (CatalogVersionSyncCronJob) exclJob1.newExecution();
		exclJob1.configureFullVersionSync(run1);
		exclJob1.perform(run1, true);
		assertEquals(run1.getResult(), run1.getSuccessResult());

		assertNull(catalogManager.getCounterpartItem(product1, tgt));
		assertNull(catalogManager.getCounterpartItem(product2, tgt));
		assertNull(catalogManager.getCounterpartItem(product3, tgt));
		assertNull(catalogManager.getSynchronizedCopy(product1, exclJob1));
		assertNull(catalogManager.getSynchronizedCopy(product2, exclJob1));
		assertNull(catalogManager.getSynchronizedCopy(product3, exclJob1));

		// 2. run job2 -> now all products should be copied -> without name 

		final CatalogVersionSyncCronJob run2 = (CatalogVersionSyncCronJob) exclJob2.newExecution();
		exclJob2.configureFullVersionSync(run2);
		exclJob2.perform(run2, true);
		assertEquals(run2.getResult(), run2.getSuccessResult());

		Product p1tgt = (Product) catalogManager.getSynchronizedCopy(product1, exclJob2);
		Product p2tgt = (Product) catalogManager.getSynchronizedCopy(product2, exclJob2);
		Product p3tgt = (Product) catalogManager.getSynchronizedCopy(product3, exclJob2);

		assertNotNull(p1tgt);
		assertNotNull(p2tgt);
		assertNotNull(p3tgt);
		assertNotSame(product1, p1tgt);
		assertNotSame(product2, p2tgt);
		assertNotSame(product3, p3tgt);

		assertEquals(product1.getCode(), p1tgt.getCode());
		assertEquals(product1.getUnit(), p1tgt.getUnit());
		assertNull(p1tgt.getName(deCtx));
		assertNull(p1tgt.getName(enCtx));

		assertEquals(product2.getCode(), p2tgt.getCode());
		assertEquals(product2.getUnit(), p2tgt.getUnit());
		assertNull(p2tgt.getName(deCtx));
		assertNull(p2tgt.getName(enCtx));

		assertEquals(product3.getCode(), p3tgt.getCode());
		assertEquals(product3.getUnit(), p3tgt.getUnit());
		assertNull(p3tgt.getName(deCtx));
		assertNull(p3tgt.getName(enCtx));

		List<ItemSyncTimestamp> p1ts = catalogManager.getSynchronizedCopies(product1);
		List<ItemSyncTimestamp> p2ts = catalogManager.getSynchronizedCopies(product2);
		List<ItemSyncTimestamp> p3ts = catalogManager.getSynchronizedCopies(product3);

		assertEquals(1, p1ts.size());
		assertEquals(1, p2ts.size());
		assertEquals(1, p3ts.size());

		final ItemSyncTimestamp itemSyncTimestamp1 = p1ts.get(0);
		final ItemSyncTimestamp itemSyncTimestamp2 = p2ts.get(0);
		final ItemSyncTimestamp itemSyncTimestamp3 = p3ts.get(0);

		assertEquals(src, itemSyncTimestamp1.getSourceVersion());
		assertEquals(tgt, itemSyncTimestamp1.getTargetVersion());
		assertEquals(product1, itemSyncTimestamp1.getSourceItem());
		assertEquals(p1tgt, itemSyncTimestamp1.getTargetItem());
		assertEquals(product1.getModificationTime(), itemSyncTimestamp1.getLastSyncSourceModifiedTime());
		assertEquals(false, itemSyncTimestamp1.isOutdatedAsPrimitive());
		assertEquals(exclJob2, itemSyncTimestamp1.getSyncJob());

		assertEquals(src, itemSyncTimestamp2.getSourceVersion());
		assertEquals(tgt, itemSyncTimestamp2.getTargetVersion());
		assertEquals(product2, itemSyncTimestamp2.getSourceItem());
		assertEquals(p2tgt, itemSyncTimestamp2.getTargetItem());
		assertEquals(product2.getModificationTime(), itemSyncTimestamp2.getLastSyncSourceModifiedTime());
		assertEquals(false, itemSyncTimestamp2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, itemSyncTimestamp2.getSyncJob());

		assertEquals(src, itemSyncTimestamp3.getSourceVersion());
		assertEquals(tgt, itemSyncTimestamp3.getTargetVersion());
		assertEquals(product3, itemSyncTimestamp3.getSourceItem());
		assertEquals(p3tgt, itemSyncTimestamp3.getTargetItem());
		assertEquals(product3.getModificationTime(), itemSyncTimestamp3.getLastSyncSourceModifiedTime());
		assertEquals(false, itemSyncTimestamp3.isOutdatedAsPrimitive());
		assertEquals(exclJob2, itemSyncTimestamp3.getSyncJob());

		// 3. run job1 again -> now names should be copied as well

		final CatalogVersionSyncCronJob run3 = (CatalogVersionSyncCronJob) exclJob1.newExecution();
		exclJob1.configureFullVersionSync(run3);
		exclJob1.perform(run3, true);
		assertEquals(run3.getResult(), run3.getSuccessResult());

		p1tgt = (Product) catalogManager.getSynchronizedCopy(product1, exclJob1);
		p2tgt = (Product) catalogManager.getSynchronizedCopy(product2, exclJob1);
		p3tgt = (Product) catalogManager.getSynchronizedCopy(product3, exclJob1);

		assertEquals(product1.getCode(), p1tgt.getCode());
		assertEquals(product1.getUnit(), p1tgt.getUnit());
		assertEquals(product1.getName(deCtx), p1tgt.getName(deCtx));
		assertEquals(product1.getName(enCtx), p1tgt.getName(enCtx));

		assertEquals(product2.getCode(), p2tgt.getCode());
		assertEquals(product2.getUnit(), p2tgt.getUnit());
		assertEquals(product2.getName(deCtx), p2tgt.getName(deCtx));
		assertEquals(product2.getName(enCtx), p2tgt.getName(enCtx));

		assertEquals(product3.getCode(), p3tgt.getCode());
		assertEquals(product3.getUnit(), p3tgt.getUnit());
		assertEquals(product3.getName(deCtx), p3tgt.getName(deCtx));
		assertEquals(product3.getName(enCtx), p3tgt.getName(enCtx));

		p1ts = catalogManager.getSynchronizedCopies(product1);
		p2ts = catalogManager.getSynchronizedCopies(product2);
		p3ts = catalogManager.getSynchronizedCopies(product3);

		assertEquals(2, p1ts.size());
		assertEquals(2, p2ts.size());
		assertEquals(2, p3ts.size());

		final ItemSyncTimestamp t1j1 = getTS(p1ts, exclJob1);
		final ItemSyncTimestamp t1j2 = getTS(p1ts, exclJob2);

		ItemSyncTimestamp t2j1 = getTS(p2ts, exclJob1);
		ItemSyncTimestamp t2j2 = getTS(p2ts, exclJob2);

		final ItemSyncTimestamp t3j1 = getTS(p3ts, exclJob1);
		final ItemSyncTimestamp t3j2 = getTS(p3ts, exclJob2);

		assertNotNull(t1j1);
		assertNotNull(t1j2);
		assertNotSame(t1j1, t1j2);

		assertNotNull(t2j1);
		assertNotNull(t2j2);
		assertNotSame(t2j1, t2j2);

		assertNotNull(t3j1);
		assertNotNull(t3j2);
		assertNotSame(t3j1, t3j2);

		assertEquals(src, t1j1.getSourceVersion());
		assertEquals(tgt, t1j1.getTargetVersion());
		assertEquals(product1, t1j1.getSourceItem());
		assertEquals(p1tgt, t1j1.getTargetItem());
		assertEquals(product1.getModificationTime(), t1j1.getLastSyncSourceModifiedTime());
		assertEquals(false, t1j1.isOutdatedAsPrimitive());
		assertEquals(exclJob1, t1j1.getSyncJob());

		assertEquals(src, t1j2.getSourceVersion());
		assertEquals(tgt, t1j2.getTargetVersion());
		assertEquals(product1, t1j2.getSourceItem());
		assertEquals(p1tgt, t1j2.getTargetItem());
		assertEquals(product1.getModificationTime(), t1j2.getLastSyncSourceModifiedTime());
		assertEquals(false, t1j2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, t1j2.getSyncJob());

		assertEquals(src, t2j1.getSourceVersion());
		assertEquals(tgt, t2j1.getTargetVersion());
		assertEquals(product2, t2j1.getSourceItem());
		assertEquals(p2tgt, t2j1.getTargetItem());
		assertEquals(product2.getModificationTime(), t2j1.getLastSyncSourceModifiedTime());
		assertEquals(false, t2j1.isOutdatedAsPrimitive());
		assertEquals(exclJob1, t2j1.getSyncJob());

		assertEquals(src, t2j2.getSourceVersion());
		assertEquals(tgt, t2j2.getTargetVersion());
		assertEquals(product2, t2j2.getSourceItem());
		assertEquals(p2tgt, t2j2.getTargetItem());
		assertEquals(product2.getModificationTime(), t2j2.getLastSyncSourceModifiedTime());
		assertEquals(false, t2j2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, t2j2.getSyncJob());

		assertEquals(src, t3j1.getSourceVersion());
		assertEquals(tgt, t3j1.getTargetVersion());
		assertEquals(product3, t3j1.getSourceItem());
		assertEquals(p3tgt, t3j1.getTargetItem());
		assertEquals(product3.getModificationTime(), t3j1.getLastSyncSourceModifiedTime());
		assertEquals(false, t3j1.isOutdatedAsPrimitive());
		assertEquals(exclJob1, t3j1.getSyncJob());

		assertEquals(src, t3j2.getSourceVersion());
		assertEquals(tgt, t3j2.getTargetVersion());
		assertEquals(product3, t3j2.getSourceItem());
		assertEquals(p3tgt, t3j2.getTargetItem());
		assertEquals(product3.getModificationTime(), t3j2.getLastSyncSourceModifiedTime());
		assertEquals(false, t3j2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, t3j2.getSyncJob());

		// sleep at least one seconds to avoid item having the same modification timestamp as before!!!
		try
		{
			Thread.sleep(1100);
		}
		catch (final InterruptedException e)
		{
			// ok
		}

		// now change name of one product
		product2.setName(enCtx, "foo");

		// 4. run job1 again -> should update name 

		final CatalogVersionSyncCronJob run4 = (CatalogVersionSyncCronJob) exclJob1.newExecution();
		exclJob1.configureFullVersionSync(run4);
		exclJob1.perform(run4, true);
		assertEquals(run4.getResult(), run4.getSuccessResult());

		p2tgt = (Product) catalogManager.getSynchronizedCopy(product2, exclJob1);

		assertEquals(product2.getCode(), p2tgt.getCode());
		assertEquals(product2.getUnit(), p2tgt.getUnit());
		assertEquals(product2.getName(deCtx), p2tgt.getName(deCtx));
		assertEquals(product2.getName(enCtx), p2tgt.getName(enCtx));

		p2ts = catalogManager.getSynchronizedCopies(product2);

		assertEquals(2, p2ts.size());

		t2j1 = getTS(p2ts, exclJob1);
		t2j2 = getTS(p2ts, exclJob2);

		assertNotNull(t2j1);
		assertNotNull(t2j2);
		assertNotSame(t2j1, t2j2);

		assertEquals(src, t2j1.getSourceVersion());
		assertEquals(tgt, t2j1.getTargetVersion());
		assertEquals(product2, t2j1.getSourceItem());
		assertEquals(product2.getModificationTime(), t2j1.getLastSyncSourceModifiedTime());
		assertEquals(p2tgt, t2j1.getTargetItem());
		assertEquals(false, t2j1.isOutdatedAsPrimitive());
		assertEquals(exclJob1, t2j1.getSyncJob());

		assertEquals(src, t2j2.getSourceVersion());
		assertEquals(tgt, t2j2.getTargetVersion());
		assertEquals(product2, t2j2.getSourceItem());
		assertEquals(p2tgt, t2j2.getTargetItem());
		assertTrue(product2.getModificationTime().after(t2j2.getLastSyncSourceModifiedTime()));
		assertEquals(true, t2j2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, t2j2.getSyncJob());

		// 5. run job2 again -> should update timestamp but change nothing else

		final CatalogVersionSyncCronJob run5 = (CatalogVersionSyncCronJob) exclJob2.newExecution();
		exclJob2.configureFullVersionSync(run5);
		exclJob2.perform(run5, true);
		assertEquals(run5.getResult(), run5.getSuccessResult());

		p2tgt = (Product) catalogManager.getSynchronizedCopy(product2, exclJob2);

		assertEquals(product2.getCode(), p2tgt.getCode());
		assertEquals(product2.getUnit(), p2tgt.getUnit());
		assertEquals(product2.getName(deCtx), p2tgt.getName(deCtx));
		assertEquals(product2.getName(enCtx), p2tgt.getName(enCtx));

		p2ts = catalogManager.getSynchronizedCopies(product2);

		assertEquals(2, p2ts.size());

		t2j1 = getTS(p2ts, exclJob1);
		t2j2 = getTS(p2ts, exclJob2);

		assertNotNull(t2j1);
		assertNotNull(t2j2);
		assertNotSame(t2j1, t2j2);

		assertEquals(src, t2j1.getSourceVersion());
		assertEquals(tgt, t2j1.getTargetVersion());
		assertEquals(product2, t2j1.getSourceItem());
		assertEquals(p2tgt, t2j1.getTargetItem());
		assertEquals(product2.getModificationTime(), t2j1.getLastSyncSourceModifiedTime());
		assertEquals(false, t2j1.isOutdatedAsPrimitive());
		assertEquals(exclJob1, t2j1.getSyncJob());

		assertEquals(src, t2j2.getSourceVersion());
		assertEquals(tgt, t2j2.getTargetVersion());
		assertEquals(product2, t2j2.getSourceItem());
		assertEquals(p2tgt, t2j2.getTargetItem());
		assertEquals(product2.getModificationTime(), t2j2.getLastSyncSourceModifiedTime());
		assertEquals(false, t2j2.isOutdatedAsPrimitive());
		assertEquals(exclJob2, t2j2.getSyncJob());
	}

	protected ItemSyncTimestamp getTS(final List<ItemSyncTimestamp> list, final CatalogVersionSyncJob job)
	{
		for (final ItemSyncTimestamp ts : list)
		{
			if (job.equals(ts.getSyncJob()))
			{
				return ts;
			}
		}
		return null;
	}

	protected CatalogVersionSyncJob createSyncJob(final String code, final CatalogVersion src, final CatalogVersion tgt,
			final int maxThreads, final boolean exclusive)
	{
		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, code);
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(maxThreads));
		args.put(CatalogVersionSyncJob.EXCLUSIVEMODE, Boolean.valueOf(exclusive));

		return catalogManager.createCatalogVersionSyncJob(args);
	}
}
