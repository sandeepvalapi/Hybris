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
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
@IntegrationTest
public class ItemCopyCreatorServiceLayerTest extends ServicelayerBaseTest
{
    private static final Logger LOG = Logger.getLogger(ItemCopyCreatorServiceLayerTest.class);

    public static final String CATALOG_ID = "bar";
    public static final String PRODUCT_FAILING_ON_CREATE = "foobarSource";

    public static final String FROM_CV = "from";
    public static final String TO_CV = "to";
    public static final int SIZE = 2;

    @Resource
    private ValidationService validationService;

    @Resource
    private ModelService modelService;

    @Resource
    private TypeService typeService;


    private CatalogManager catalogManager;
    private CatalogVersionSyncJob syncJob;
    private CatalogVersionSyncCronJob syncCronJob;
    private CatalogVersionSyncWorker worker;

    private PatternConstraintModel createConstraint;

    private String configLegacyFlag;

    @Before
    public void setUp() throws Exception
    {
        this.catalogManager = CatalogManager.getInstance();
        this.syncJob = createSyncJob(createCatalog(CATALOG_ID, true), FROM_CV, TO_CV, true);
        this.syncCronJob = (CatalogVersionSyncCronJob) syncJob.newExecution();

        this.worker = createSingleWorker(syncJob, syncCronJob);

        configLegacyFlag = Config.getParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG);
        Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, "false"); //activate SL flag;

    }

    private <T extends ItemModel> T createCreateConstraint(final String pattern)
    {
        final PatternConstraintModel constraint = modelService.create(PatternConstraintModel.class);
        constraint.setId("failOnCreate");
        constraint.setDescriptor(typeService.getAttributeDescriptor(ProductModel._TYPECODE, ProductModel.CODE));
        constraint.setType(typeService.getComposedTypeForClass(ProductModel.class));
        constraint.setRegexp(pattern);
        constraint.setFlags(Collections.singleton(RegexpFlag.DOTALL));

        return (T) constraint;
    }


    @After
    public void tearDown() throws Exception
    {
        if (StringUtils.isNotEmpty(configLegacyFlag))
        {
            Config.setParameter(GenericCatalogCopyContext.LEGACY_MODE_FLAG, configLegacyFlag);
        }

        if (createConstraint != null)
        {
            modelService.remove(createConstraint);
        }
        validationService.reloadValidationEngine();
    }

    @Test
    public void testExceptionDuringCreation() throws InterruptedException
    {
        createConstraint = createCreateConstraint(PRODUCT_FAILING_ON_CREATE);
        modelService.save(createConstraint);
        validationService.reloadValidationEngine();


        final CatalogVersionSyncCopyContext ctx = new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker);

        final Product source = ProductManager.getInstance().createProduct(PRODUCT_FAILING_ON_CREATE + " foo ");
        final Product target = null;

        final Collection<String> whiteList = Arrays.asList(Product.CODE/*, Product.NAME, Product.TYPE, Product.PK*/);
        final Collection<String> blackList = Arrays.asList(Product.MODIFIED_TIME, Product.CREATION_TIME);


        final Map<String, Object> presets = Collections.EMPTY_MAP;
        final ItemCopyCreator copyCreator = new ItemCopyCreator(ctx, null, source, target, blackList, whiteList, presets);

        Assert.assertNull(copyCreator.copy());

    }


    private CatalogVersionSyncWorker createSingleWorker(final CatalogVersionSyncJob syncJob,
                                                        final CatalogVersionSyncCronJob syncCronJob) throws InterruptedException
    {
        final CatalogVersionSyncMaster masterSync = new CatalogVersionSyncMaster(syncJob, syncCronJob);

        final CatalogVersionSyncWorker worker = masterSync.createWorker(0);
        return worker;
    }


    private Catalog createCatalog(final String catalogname, final boolean createIfNotExists)
    {
        org.junit.Assert.assertNotNull(catalogname);


        Catalog catalog = catalogManager.getCatalog(catalogname);
        if (catalog == null && createIfNotExists)
        {
            catalog = catalogManager.createCatalog(catalogname);
        }
        org.junit.Assert.assertNotNull(catalog);
        return catalog;
    }


    private CatalogVersionSyncJob createSyncJob(final Catalog catalog, final String srcCVname, final String trgCVname,
                                                final boolean createIfNotExists)
    {

        Assert.assertNotNull(srcCVname);
        Assert.assertNotNull(trgCVname);
        Assert.assertNotNull(catalog);

        CatalogVersion src = catalog.getCatalogVersion(srcCVname);
        if (src == null && createIfNotExists)
        {
            src = catalogManager.createCatalogVersion(catalog, srcCVname, null);
        }
        Assert.assertNotNull(src);

        CatalogVersion trg = catalog.getCatalogVersion(trgCVname);
        if (trg == null && createIfNotExists)
        {
            trg = catalogManager.createCatalogVersion(catalog, trgCVname, null);
        }
        Assert.assertNotNull(trg);

        final Map args = new HashMap();
        args.put(CatalogVersionSyncJob.CODE, catalog.getId() + ": " + src.getVersion() + "->" + trg.getVersion());
        args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
        args.put(CatalogVersionSyncJob.TARGETVERSION, trg);
        final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
        args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(threads));

        return catalogManager.createCatalogVersionSyncJob(args);
    }
}
