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
import de.hybris.platform.catalog.SynchronizationPersistenceAdapter;
import de.hybris.platform.catalog.SynchronizationPersistenceException;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ArticleStatus;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


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
public class ItemCopyCreatorTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(ItemCopyCreatorTest.class);

	public static final String CATALOG_ID = "bar";
	public static final String PRODUCT_FAILING_ON_CREATE = "foobarSource";
	public static final String PRODUCT_FAILING_ON_UPDATE = "foobarSourceCannotUpdate";
	// public static final String PRODUCT_TARGET = "foobarTarget";
	public static final String FROM_CV = "from";
	public static final String TO_CV = "to";
	public static final int SIZE = 2;


	private CatalogManager catalogManager;
	private CatalogVersionSyncJob syncJob;
	private CatalogVersionSyncCronJob syncCronJob;
	private CatalogVersionSyncWorker worker;

	CatalogVersion src, tgt;

	@Resource
	ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		this.catalogManager = CatalogManager.getInstance();
		this.syncJob = createSyncJob(createCatalog(CATALOG_ID, true), FROM_CV, TO_CV, true);
		this.syncCronJob = (CatalogVersionSyncCronJob) syncJob.newExecution();

		this.worker = createSingleWorker(syncJob, syncCronJob);

	}

	@Test
	public void testExceptionDuringCreation() throws InterruptedException
	{

		final CatalogVersionSyncCopyContext ctx = new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker)
		{
			@Override
			SynchronizationPersistenceAdapter getPersistenceAdapter()
			{
				return new LegacySynchronizationPersistenceAdapter(this)
				{
					@Override
					public Item create(final ComposedType expectedType, final Map<String, Object> attributes)
							throws SynchronizationPersistenceException
					{
						throw new IllegalStateException("expected exception during creation ");
					}
				};
			}

		};

		final Product source = ProductManager.getInstance().createProduct(PRODUCT_FAILING_ON_CREATE);
		final Product target = null;

		final Collection<String> whiteList = Arrays.asList(Product.CODE/* , Product.NAME, Product.TYPE, Product.PK */);
		final Collection<String> blackList = Arrays.asList(Product.MODIFIED_TIME, Product.CREATION_TIME);


		final Map<String, Object> presets = Collections.EMPTY_MAP;
		final ItemCopyCreator copyCreator = new ItemCopyCreator(ctx, null, source, target, blackList, whiteList, presets);

		Assert.assertNull(copyCreator.copy());

	}


	@Test
	public void testExceptionDuringCreationOtherAttributes() throws JaloBusinessException
	{

		final Set<Language> languages = jaloSession.getC2LManager().getAllLanguages();
		//determine other attribute
		final CatalogVersionSyncCopyContext ctx = new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker)
		{
			@Override
			SynchronizationPersistenceAdapter getPersistenceAdapter()
			{
				return new LegacySynchronizationPersistenceAdapter(this)
				{
					@Override
					public void update(final Item entity, final Map.Entry<String, Object> attribute)
							throws SynchronizationPersistenceException
					{
						//if other attribute
						if ("articleStatus".equalsIgnoreCase(attribute.getKey()))
						{
							throw new IllegalStateException("expected exception during setting other attributes  ...");
						}
						super.update(entity, attribute);
					}
				};
			}


			@Override
			protected Set<Language> getTargetLanguages()
			{
				return languages;
			}

		};

		final Product source = ProductManager.getInstance().createProduct(PRODUCT_FAILING_ON_UPDATE);

		final EnumerationValue articleStatus = EnumerationManager.getInstance().getEnumerationValue(ArticleStatus._TYPECODE,
				GeneratedCatalogConstants.Enumerations.ArticleStatus.OTHERS);
		CatalogManager.getInstance().setArticleStatus(source, ImmutableMap.of(articleStatus, "foo"));

		final Product target = null;

		final Collection<String> whiteList = Arrays.asList(Product.CODE, ArticleStatus._TYPECODE/*
																															  * , Product.NAME,
																															  * Product.TYPE,
																															  * Product.PK
																															  */);
		final Collection<String> blackList = Arrays.asList(Product.MODIFIED_TIME, Product.CREATION_TIME);


		// need to make sure that product code is unique due to database index constraints!
		final ItemCopyCreator copyCreator = new ItemCopyCreator(ctx, null, source, target, blackList, whiteList, Collections.singletonMap(Product.CODE, UUID.randomUUID().toString()));

		final Product result = copyCreator.copy();

		Assert.assertNotNull("Result should be not null ", result);//TODO when tx feature activated any reference fail should prevent from creating the item
		Assert.assertTrue("The article status shouldn't be set ",
				MapUtils.isEmpty(CatalogManager.getInstance().getArticleStatus(result)));
	}

	@Test
	public void testUpdatingNullValues() throws JaloBusinessException
	{
		final UnitModel u = modelService.create(UnitModel.class);
		u.setCode("unit-" + System.nanoTime());
		u.setUnitType("type");
		u.setConversion(Double.valueOf(1.0));
		modelService.save(u);

		final ProductModel p = modelService.create(ProductModel.class);
		p.setCatalogVersion((CatalogVersionModel) modelService.get(src));
		p.setCode("foo-" + System.nanoTime());
		p.setMinOrderQuantity(12);
		p.setUnit(u);
		p.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		modelService.save(p);

		final Product pJalo = modelService.getSource(p);

		// 1. create copy -> values are non-null
		final Product pCopyJalo = (Product) new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker)
		{
			@Override
			protected Set<Language> getTargetLanguages()
			{
				return jaloSession.getC2LManager().getAllLanguages();
			}

			@Override
			protected SyncSchedule getCurrentSchedule()
			{
				return new SyncSchedule(pJalo.getPK(), null, null, Collections.EMPTY_SET, Collections.EMPTY_MAP);
			}
		}.copy(pJalo);

		Assert.assertNotNull("Product not copied", pCopyJalo);

		final ProductModel pCopy = modelService.get(pCopyJalo);
		Assert.assertEquals(p.getCode(), pCopy.getCode());
		Assert.assertEquals(p.getMinOrderQuantity(), pCopy.getMinOrderQuantity());
		Assert.assertEquals(p.getApprovalStatus(), pCopy.getApprovalStatus());
		Assert.assertEquals(p.getUnit(), pCopy.getUnit());

		// 2. change original -> some values are null now
		p.setUnit(null);
		p.setMinOrderQuantity(null);
		modelService.save(p);
		Assert.assertNull(p.getMinOrderQuantity());
		Assert.assertNull(p.getUnit());

		// 3. copy again -> copy should have null values too
		final Product pCopyJalo2 = (Product) new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker)
		{
			@Override
			protected Set<Language> getTargetLanguages()
			{
				return jaloSession.getC2LManager().getAllLanguages();
			}

			@Override
			protected SyncSchedule getCurrentSchedule()
			{
				return new SyncSchedule(pJalo.getPK(), pCopyJalo.getPK(), null, Collections.EMPTY_SET, Collections.EMPTY_MAP);
			}
		}.copy(pJalo);

		Assert.assertNotNull("Product not copied", pCopyJalo2);

		final ProductModel pCopy2 = modelService.get(pCopyJalo2);
//		Assert.assertSame(pCopy, pCopy2);
		modelService.refresh(pCopy2);
		// unchanged ?
		Assert.assertEquals(p.getCode(), pCopy2.getCode());
		Assert.assertEquals(p.getApprovalStatus(), pCopy2.getApprovalStatus());
		// null'ed ?
		Assert.assertNull(pCopy2.getUnit());
		Assert.assertNull(pCopy2.getMinOrderQuantity());
	}

	@Ignore("can not easily write test case for nested ItemCopyCreators ")
	@Test
	public void testExceptionDuringCreationPartOfAttributes() throws JaloPriceFactoryException
	{
		final Currency eur = getOrCreateCurrency("eur");
		final Unit unit = ProductManager.getInstance().createUnit("p", "piece");
		final Set<Language> languages = jaloSession.getC2LManager().getAllLanguages();
		//determine other attribute
		final CatalogVersionSyncCopyContext ctx = new CatalogVersionSyncCopyContext(syncJob, syncCronJob, worker)
		{
			@Override
			SynchronizationPersistenceAdapter getPersistenceAdapter()
			{
				return new LegacySynchronizationPersistenceAdapter(this)
				{
					@Override
					public void update(final Item entity, final Map.Entry<String, Object> attribute)
							throws SynchronizationPersistenceException
					{
						//if other attribute
						if ("europe1Prices".equalsIgnoreCase(attribute.getKey()))
						{
							throw new IllegalStateException("expected exception during setting partof attributes  ...");
						}
						super.update(entity, attribute);
					}
				};
			}


			@Override
			protected Set<Language> getTargetLanguages()
			{
				return languages;
			}

		};

		final Product source = ProductManager.getInstance().createProduct(PRODUCT_FAILING_ON_CREATE);


		Europe1PriceFactory.getInstance().createPriceRow(source, null, null, null, 0, eur, unit, 1, false, null, 99.99);

		final Product target = null;

		final Collection<String> whiteList = Arrays.asList(Product.CODE, "europe1Prices"/*
																												   * , Product.NAME, Product.TYPE,
																												   * Product.PK
																												   */);
		final Collection<String> blackList = Arrays.asList(Product.MODIFIED_TIME, Product.CREATION_TIME);


		final Map<String, Object> presets = Collections.EMPTY_MAP;
		final ItemCopyCreator copyCreator = new ItemCopyCreator(ctx, null, source, target, blackList, whiteList, presets);


		final Product result = copyCreator.copy();

		Assert.assertEquals(
				0,
				Europe1PriceFactory
						.getInstance()
						.getProductPriceRows(jaloSession.getSessionContext(), result,
								Europe1PriceFactory.getInstance().getPPG(jaloSession.getSessionContext(), result)).size());
		Assert.assertNull(catalogManager.getProductByCatalogVersion(syncJob.getTargetVersion(), PRODUCT_FAILING_ON_CREATE));
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

		org.junit.Assert.assertNotNull(srcCVname);
		org.junit.Assert.assertNotNull(trgCVname);
		org.junit.Assert.assertNotNull(catalog);

		src = catalog.getCatalogVersion(srcCVname);
		if (src == null && createIfNotExists)
		{
			src = catalogManager.createCatalogVersion(catalog, srcCVname, null);
		}
		org.junit.Assert.assertNotNull(src);

		tgt = catalog.getCatalogVersion(trgCVname);
		if (tgt == null && createIfNotExists)
		{
			tgt = catalogManager.createCatalogVersion(catalog, trgCVname, null);
		}
		org.junit.Assert.assertNotNull(tgt);

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, catalog.getId() + ": " + src.getVersion() + "->" + tgt.getVersion());
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		final int threads = CatalogVersionSyncJob.getDefaultMaxThreads(jaloSession.getTenant()) * 2;
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(threads));

		return catalogManager.createCatalogVersionSyncJob(args);
	}
}
