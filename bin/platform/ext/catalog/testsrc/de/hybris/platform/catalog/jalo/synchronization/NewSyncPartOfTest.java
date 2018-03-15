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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.*;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;

import java.util.*;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
@IntegrationTest
public class NewSyncPartOfTest extends HybrisJUnit4Test
{
    private static final Logger LOG = Logger.getLogger(NewSyncPartOfTest.class);
    private static final int productCount = 500;
    private static final int refCount = 10;

    CatalogVersion src;
    CatalogVersion tgt;

    private List<Product> products;
    private List<ProductReference> references;

    @Before
    public void setUp() throws Exception
    {
        final CatalogManager catalogManager = CatalogManager.getInstance();

        final Catalog catalog = catalogManager.createCatalog("PartOfTest");
        src = catalogManager.createCatalogVersion(catalog, "ver1", null);
        src.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
        tgt = catalogManager.createCatalogVersion(catalog, "ver2", null);
        tgt.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

        LOG.info("Creating " + productCount + " products...");
        products = new ArrayList<Product>(productCount);
        final ComposedType composedType = TypeManager.getInstance().getComposedType(Product.class);
        final Map args = new HashMap();
        for (int i = 0; i < productCount; i++)
        {
            args.put(Product.CODE, "Product-" + i);
            args.put(CatalogConstants.Attributes.Product.CATALOGVERSION, src);
            products.add((Product) composedType.newInstance(args));
        }

        LOG.info("Creating " + (productCount * refCount) + " product references...");

        references = new ArrayList<>(productCount * refCount);
        for (int i = 0; i < productCount; i++)
        {
            final Product product = products.get(i);
            for (int j = 0; j < refCount; j++)
            {
                final Product tgt = products.get((i + j + 1) % productCount);
                final ProductReference prodRef = catalogManager.createProductReference("foo", product, tgt, Integer.valueOf(1));
                prodRef.setActive(Boolean.FALSE);
                prodRef.setPreselected(Boolean.FALSE);
                references.add(prodRef);
            }
        }
        LOG.info("Done catalog creation.");
    }

    @Test
    public void testFullSync() throws InterruptedException
    {
        final CatalogManager catalogManager = CatalogManager.getInstance();

        final Map args = new HashMap();
        args.put(CatalogVersionSyncJob.CODE, "foo");
        args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
        args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
        final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
        LOG.info("NewSyncPartOfTest: threads = " + threads);
        args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(threads));
        final CatalogVersionSyncJob job = catalogManager.createCatalogVersionSyncJob(args);

        final CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob) job.newExecution();

        job.configureFullVersionSync(cronJob);

        final int refTs = TypeManager.getInstance().getComposedType(ProductReference.class).getItemTypeCode();

        final Set<PK> refPKset = Collections.synchronizedSet(new HashSet<PK>((productCount-refCount) * refCount));
        final PersistencePool.PersistenceListener persistenceListener = new PersistencePool.PersistenceListener()
        {
            @Override
            public void entityCreated(final PK pk)
            {
                if (pk.getTypeCode() == refTs)
                {
                    refPKset.add(pk);

					Transaction.current().executeOnRollback(new RemoveFromCollectionOnRollback(refPKset, pk));
                }
            }
        };

        try
        {
            Registry.getCurrentTenant().getPersistencePool().registerPersistenceListener(persistenceListener);

            LOG.info("Started ....");
            final long start  = System.currentTimeMillis();
            job.perform(cronJob, true);
            LOG.info("Finished  : "+(System.currentTimeMillis() - start));
        }
        finally
        {
            Registry.getCurrentTenant().getPersistencePool().unregisterPersistenceListener(persistenceListener);
        }

        Assert.assertFalse("cronjob is still running", cronJob.isRunning());

        final EnumerationValue success = cronJob.getSuccessResult();
        if( !success.equals(cronJob.getResult()))
        {
        
           final SyncScheduleReader reader = cronJob.createSyncScheduleReader();
           try
           {
         	  while( reader.readNextLine())
         	  {
         		  final SyncSchedule schedule = reader.getScheduleFromLine();
					System.err.println(
							"remaining item to sync: src="+schedule.getSrcPK()+"/" + pkToString(schedule.getSrcPK()) + " tgt="+ schedule.getTgtPK()+"/" + pkToString(schedule.getTgtPK())
									+ " ts="+ schedule.getTimestampPK() +"/" + pkToString(schedule.getTimestampPK()) + " pending=" + schedule.getPendingAttributes());
         	  }
           }
           finally
           {
         	  reader.closeQuietly();
           }
        }
        
        Assert.assertEquals(success, cronJob.getResult());

        Assert.assertEquals(products.size(), tgt.getAllProductCount());

        LOG.info("Comparing Products ...");
        // test level3
        final List<Product> productTgt = new ArrayList<Product>(productCount);
        for (int i = 0; i < productCount; i++)
        {
            final Product srcProd = products.get(i);
            final Product tgtProd = tgt.getProduct(srcProd.getCode());
            final List<ItemSyncTimestamp> itemSyncTimestampList = catalogManager.getSynchronizedCopies(srcProd);
            Assert.assertNotNull(srcProd);
            Assert.assertNotNull(tgtProd);
            Assert.assertEquals(tgtProd.getCode(), tgtProd.getCode());
            Assert.assertEquals(1, itemSyncTimestampList.size());
            final ItemSyncTimestamp itemSyncTimestamp = itemSyncTimestampList.get(0);
            Assert.assertFalse(itemSyncTimestamp.isOutdatedAsPrimitive());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributeQualifiers());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributesOwnerJob());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributesScheduledTurn());
            Assert.assertEquals(srcProd.getModificationTime(), itemSyncTimestamp.getLastSyncSourceModifiedTime());

            productTgt.add(tgtProd);
        }

        LOG.info("Comparing references...");

        final List<ProductReference> refTgt = new ArrayList<ProductReference>(productCount * refCount);
        for (int i = 0; i < references.size(); i++)
        {
            final ProductReference srcRef = references.get(i);
            final List<ItemSyncTimestamp> itemSyncTimestampList = catalogManager.getSynchronizedCopies(srcRef);
            Assert.assertNotNull(srcRef);
            Assert.assertEquals(1, itemSyncTimestampList.size());
            final ItemSyncTimestamp itemSyncTimestamp = itemSyncTimestampList.get(0);
            Assert.assertFalse(itemSyncTimestamp.isOutdatedAsPrimitive());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributeQualifiers());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributesOwnerJob());
            Assert.assertNull(itemSyncTimestamp.getPendingAttributesScheduledTurn());
            Assert.assertEquals(srcRef.getModificationTime(), itemSyncTimestamp.getLastSyncSourceModifiedTime());

            final ProductReference tgtRef = (ProductReference) itemSyncTimestamp.getTargetItem();
            Assert.assertNotNull(tgtRef);
            final Product srcSrcProd = srcRef.getSource();
            final Product tgtSrcProd = tgtRef.getSource();
            Assert.assertEquals(srcSrcProd.getCode(), tgtSrcProd.getCode());
            Assert.assertEquals(products.indexOf(srcSrcProd), productTgt.indexOf(tgtSrcProd));
            final Product srcTgtProd = srcRef.getTarget();
            final Product tgtTgtProd = tgtRef.getTarget();
            Assert.assertEquals(srcTgtProd.getCode(), tgtTgtProd.getCode());
            Assert.assertEquals(products.indexOf(srcTgtProd), productTgt.indexOf(tgtTgtProd));

            refTgt.add(tgtRef);
            refPKset.remove(tgtRef.getPK());
        }

		Assert.assertTrue(refPKset.isEmpty());

        LOG.info("Comparing reference assignment...");
        for (int i = 0; i < productCount; i++)
        {
            final Product srcProd = products.get(i);
            final Product tgtProd = productTgt.get(i);
            final Collection<ProductReference> srcRefs = catalogManager.getProductReferences(srcProd);
            final Collection<ProductReference> tgtRefs = catalogManager.getProductReferences(tgtProd);
            Assert.assertEquals(srcRefs.size(), tgtRefs.size());
            for (final Iterator<ProductReference> srcIt = srcRefs.iterator(), tgtIt = tgtRefs.iterator(); srcIt.hasNext()
                    && tgtIt.hasNext();)
            {
                final ProductReference srcR = srcIt.next();
                final ProductReference tgtR = tgtIt.next();

                Assert.assertNotNull(srcR);
                Assert.assertNotNull(tgtR);
                Assert.assertEquals(references.indexOf(srcR), refTgt.indexOf(tgtR));

                Assert.assertTrue(srcIt.hasNext() == tgtIt.hasNext());
            }
        }

    }
    
    String pkToString(final PK pk )
    {
   	 if( pk != null )
   	 {
   		 final Item item = jaloSession.getItem(pk);
   		 if( item.isAlive())
   		 {
   			 try
   			 {
   				 return item.toString();
   			 }
   			 catch( final Exception e)
   			 {
   				 return "<error:"+e.getMessage()+">";
   			 }
   		 }
   	 }
   	 return "NONE";
    }
    

    private void waitingAfterFinish(final long millis , final CatalogVersionSyncCronJob cronJob) throws InterruptedException
    {
        //Thread.sleep(5000);
        while (cronJob.isRunning())
        {
            Thread.sleep(millis);
        }
        //Thread.sleep(5000);
    }

	private static class RemoveFromCollectionOnRollback extends Transaction.TransactionAwareExecution
	{
		private final Set<PK> refPKset;
		private final PK pk;

		public RemoveFromCollectionOnRollback(final Set<PK> refPKset, final PK pk)
		{
			this.refPKset = refPKset;
			this.pk = pk;
		}

		@Override
		public void execute(final Transaction tx) throws Exception
		{
			refPKset.remove(pk);
		}

		@Override
		public Object getId()
		{
			return pk;
		}
	}
}
