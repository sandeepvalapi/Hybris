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
package de.hybris.platform.europe1.jalo.synchronization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.jalo.synchronization.GenericCatalogCopyContext;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantType;
import de.hybris.platform.variants.jalo.VariantsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


@Ignore("HORST-51")
@IntegrationTest
public class NewSyncPartOfRemovalTest extends HybrisJUnit4Test
{
    private static final Logger LOG = Logger.getLogger(NewSyncPartOfRemovalTest.class.getName());

    private static final int PRICES_PER_VARIANT = 5;
    private static final int VARIANTS_PER_PRODUCT = 5;
    public static final int REMOVAL_TIMES = 10;
    public static final int THREADS = 10;

    private Currency eur;
    private Unit unit;
    private VariantType variantType;

    private Catalog catalog;

    @Before
    public void setUp() throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        catalog = CatalogManager.getInstance().createCatalog("testCat");


        eur = getOrCreateCurrency("eur");
        unit = ProductManager.getInstance().createUnit("p", "piece");

        variantType = VariantsManager.getInstance().createVariantType("VarType");
    }

    @Test
    public void testPartOfRemovalSyncJaloMode()
            throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final String propertyBefore = Config.getParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG);

        try
        {
            Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, "true");
            testPartOfRemovalSync();
        }
        finally
        {
            Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, propertyBefore);
        }
    }


    @Test
    public void testPartOfRemovalSyncSLMode()
            throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final String propertyBefore = Config.getParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG);

        try
        {
            Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, "false");
            testPartOfRemovalSync();
        }
        finally
        {
            Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, propertyBefore);
        }
    }

    private void testPartOfRemovalSync() throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final CatalogVersion srcVersion = createCatalogVersion("src");

        final List<Product> toCreateList = ImmutableList.of(createBase(srcVersion, "base1", variantType),
                createBase(srcVersion, "base2", variantType), createBase(srcVersion, "base3", variantType)

        );

        for (int i = 0; i < REMOVAL_TIMES; i++)
        {
            final CatalogVersion targetVersion = createCatalogVersion("target-" + i);
            final List<Product> toRemoveList = ImmutableList.of(createBase(targetVersion, "target1", variantType),
                    createBase(targetVersion, "target2", variantType), createBase(targetVersion, "target3", variantType));


            LOG.info("Synchronization  " + srcVersion.getVersion() + "->" + targetVersion.getVersion() + " ..... ");
            final List<Product> clashList = ImmutableList.of(toCreateList.get(0), toRemoveList.get(0), toCreateList.get(1),
                    toRemoveList.get(1), toCreateList.get(2), toRemoveList.get(2));

            final CatalogVersionSyncCronJob sync = createAndConfigureNewSync(srcVersion, targetVersion, clashList);
            sync.getJob().perform(sync, true);
            assertEquals(sync.getSuccessResult(), sync.getResult());

            //check results
            assertProduct("base1", targetVersion);
            assertProduct("base2", targetVersion);
            assertProduct("base3", targetVersion);

            assertNull(CatalogManager.getInstance().getProductByCatalogVersion(srcVersion, "target1"));
            assertNull(CatalogManager.getInstance().getProductByCatalogVersion(srcVersion, "target2"));
            assertNull(CatalogManager.getInstance().getProductByCatalogVersion(srcVersion, "target3"));

        }
    }

    private void assertProduct(final String code, final CatalogVersion targetVersion) throws JaloPriceFactoryException
    {
        final Product single = CatalogManager.getInstance().getProductByCatalogVersion(targetVersion, code);
        assertNotNull("Product with code " + code + " should exists ", single);
        assertEquals("Product should have " + VARIANTS_PER_PRODUCT + " variants ", VARIANTS_PER_PRODUCT, VariantsManager
                .getInstance().getVariants(single).size());
        for (final VariantProduct vp : VariantsManager.getInstance().getVariants(single))
        {
            final Collection priceRows =
                    Europe1PriceFactory.getInstance().getProductPriceRows(jaloSession.getSessionContext(), vp,
                            Europe1PriceFactory.getInstance().getPPG(jaloSession.getSessionContext(), vp));
            assertEquals("Variant should have " + PRICES_PER_VARIANT + " prices ", PRICES_PER_VARIANT, priceRows.size());
        }
    }

    private CatalogVersionSyncCronJob createAndConfigureNewSync(final CatalogVersion sourceVersion,
                                                                final CatalogVersion tgtVersion,
                                                                final Collection<Product> productsToCreateAndRemoveClashList)
            throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final CatalogVersionSyncJob sync = createNewSync(sourceVersion, tgtVersion);

        final CatalogVersionSyncCronJob cronJob = (CatalogVersionSyncCronJob) sync.newExecution();


        int i = 0;
        for (final Product singleToRemoveOrCreate : productsToCreateAndRemoveClashList)
        {
            if (i % 2 == 0)
            {
                //to create
                LOG.info("Configuring product to create " + singleToRemoveOrCreate.getCode());
                addProductToCreate(cronJob, singleToRemoveOrCreate);
            }
            else
            {
                //to remove
                LOG.info("Configuring product to remove " + singleToRemoveOrCreate.getCode());
                addProductToRemove(cronJob, singleToRemoveOrCreate);
            }
            i++;
        }
        return cronJob;

    }

    private void addProductToRemove(final CatalogVersionSyncCronJob cronJob, final Product toRemove)
    {
        cronJob.addPendingItem(null, toRemove.getPK());
        for (final VariantProduct removeVar : VariantsManager.getInstance().getVariants(toRemove))
        {
            cronJob.addPendingItem(null, removeVar.getPK());
        }
    }

    private void addProductToCreate(final CatalogVersionSyncCronJob cronJob, final Product toCreate)
    {
        cronJob.addPendingItem(toCreate.getPK(), null);
        for (final VariantProduct base1Var : VariantsManager.getInstance().getVariants(toCreate))
        {
            cronJob.addPendingItem(base1Var.getPK(), null);
        }
    }

    private CatalogVersionSyncJob createNewSync(final CatalogVersion sourceVersion, final CatalogVersion targetVersion)
    {
        final Map args = new HashMap();
        args.put(CatalogVersionSyncJob.CODE, sourceVersion.getVersion() + "->" + targetVersion.getVersion());
        args.put(CatalogVersionSyncJob.SOURCEVERSION, sourceVersion);
        args.put(CatalogVersionSyncJob.TARGETVERSION, targetVersion);
        args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(THREADS));

        return CatalogManager.getInstance().createCatalogVersionSyncJob(args);
    }

    private CatalogVersion createCatalogVersion(final String version)
    {
        return CatalogManager.getInstance().createCatalogVersion(catalog, version, getOrCreateLanguage("de"));
    }

    private Product createBase(final CatalogVersion catalogVersion, final String code, final VariantType variantType)
            throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final CatalogManager catalogManager = CatalogManager.getInstance();
        final ProductManager productManager = ProductManager.getInstance();

        final Product base = productManager.createProduct(code);
        catalogManager.setCatalogVersion(base, catalogVersion);
        VariantsManager.getInstance().setVariantType(base, variantType);

        for (int i = 0; i < VARIANTS_PER_PRODUCT; i++)
        {
            createVariant(catalogVersion, base, code + "Var" + i, variantType);
        }
        return base;
    }

    private VariantProduct createVariant(final CatalogVersion catalogVersion, final Product base, final String code,
                                         final VariantType variantType)
            throws JaloGenericCreationException, JaloAbstractTypeException, JaloPriceFactoryException
    {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put(Product.CODE, code);
        params.put(VariantProduct.BASEPRODUCT, base);
        params.put(CatalogConstants.Attributes.Product.CATALOGVERSION, catalogVersion);

        final VariantProduct ret = (VariantProduct) variantType.newInstance(params);

        for (int i = 0; i < PRICES_PER_VARIANT; i++)
        {
            createPriceRow(ret, (10.5 * i) + 1);
        }

        return ret;
    }

    private PriceRow createPriceRow(final VariantProduct var, final double price) throws JaloPriceFactoryException
    {
        return Europe1PriceFactory.getInstance().createPriceRow(var, null, null, null, 0, eur, unit, 1, false, null, price);
    }
}
