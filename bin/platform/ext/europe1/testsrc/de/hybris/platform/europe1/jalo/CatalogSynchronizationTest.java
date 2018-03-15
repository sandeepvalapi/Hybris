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
package de.hybris.platform.europe1.jalo;

import static de.hybris.platform.testframework.Assert.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.cronjob.jalo.JobLog;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for catalog synchronization.
 */
@IntegrationTest
public class CatalogSynchronizationTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(CatalogSynchronizationTest.class);

	private Catalog srcCatalog;
	private CatalogVersion srcCatalogVersion;
	private CatalogVersion tgtCatalogVersion;

	private SyncItemJob src2tgtJob;

	private Category rootCat, dvdCat, cdCat, bargainsCat, bargainDvdCat, bargainCdCat, externCat;
	private Product dvd1, dvd2, cd1, cd2;

	private Unit pieces;

	private Keyword keyword1, keyword2, keyword3, keyword4;
	private Language langDe;
	private Currency currEur;

	private User syncUser;

	private PriceFactory sessionPriceFactoryBefore = null;

	/**
	 * lang:de curr:EUR srcCatalog +-srcCatalogVersion +-rootCat +-dvdCat <-keyword3 +->dvd1(pieces,dvdPPG,-1) <-keyword1
	 * +->dvd2(pieces,dvdPPG,24.99) +->dvdNotInCatalogVersion(pieces,-1) +-cdCat <-keyword4 +->cd1(pieces,cdPPG,-1)
	 * <-keyword2 +->cd2(pieces,cdPPG,9.99) +->cdNotInCatalogVersion(pieces,-1) +-bargainsCat +-bargainDvdCat +->dvd2
	 * +-bargainCdCat +->cd2 outside /externCat +->dvd1 +->dvd2 +->cd1 +->cd2 +->dvdNotInCatalogVersion
	 * +->cdNotInCatalogVersion
	 */
	protected Object registerForRemoval(final Object object)
	{
		assertNotNull(object);
		return object;
	}

	protected void assertNoSessionPriceFactoryActive(final JaloSession jaloSession)
	{
		sessionPriceFactoryBefore = jaloSession.getPriceFactory();
		jaloSession.setPriceFactory(null);
		assertNull(jaloSession.getPriceFactory());
	}

	@Before
	public void setUp() throws Exception
	{
		assertNoSessionPriceFactoryActive(jaloSession);

		Transaction.current().enableDelayedStore(true);

		langDe = getOrCreateLanguage("de");

		jaloSession.getSessionContext().setLanguage(langDe);
		try
		{
			currEur = C2LManager.getInstance().getCurrencyByIsoCode("EUR");
		}
		catch (final JaloItemNotFoundException e)
		{
			registerForRemoval(currEur = C2LManager.getInstance().createCurrency("EUR"));
		}

		final Collection coll = ProductManager.getInstance().getUnitsByCode("pieces");
		if (coll != null && !coll.isEmpty())
		{
			pieces = (Unit) coll.iterator().next();
		}
		else
		{
			registerForRemoval(pieces = ProductManager.getInstance().createUnit("pieces", "pieces"));
		}

		registerForRemoval(syncUser = UserManager.getInstance().createEmployee("syncUser"));
		registerForRemoval(srcCatalog = getCatalogManager().createCatalog("srcCatalog"));

		createSourceCatalogVersion();
		createTargetCatalogVersion();
	}

	@After
	public void tearDown() throws Exception
	{
		jaloSession.setPriceFactory(sessionPriceFactoryBefore);
	}


	private void createSourceCatalogVersion() throws Exception
	{
		final EnumerationManager enumerationManager = EnumerationManager.getInstance();
		//global price for all dvds
		EnumerationValue dvdProductPriceGroup;
		registerForRemoval(dvdProductPriceGroup = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP), "dvdProductPriceGroup"));
		createPrice(null, dvdProductPriceGroup, pieces, 29.99);
		//		
		//global price for all cds
		EnumerationValue cdProductPriceGroup;
		registerForRemoval(cdProductPriceGroup = enumerationManager.createEnumerationValue(
				enumerationManager.getEnumerationType(Europe1Constants.TYPES.PRICE_PRODUCT_GROUP), "cdProductPriceGroup"));
		createPrice(null, cdProductPriceGroup, pieces, 12.99);

		registerForRemoval(srcCatalogVersion = getCatalogManager().createCatalogVersion(srcCatalog, "srcCatalogVersion", langDe));

		rootCat = createCategory("rootCat", srcCatalogVersion, null);
		dvdCat = createCategory("dvdCat", srcCatalogVersion, rootCat);
		cdCat = createCategory("cdCat", srcCatalogVersion, rootCat);
		bargainsCat = createCategory("bargainsCat", srcCatalogVersion, rootCat);
		bargainDvdCat = createCategory("bargainDvdCat", srcCatalogVersion, bargainsCat);
		bargainCdCat = createCategory("bargainCdCat", srcCatalogVersion, bargainsCat);

		externCat = createCategory("externCat", null, rootCat);

		dvd1 = createProduct("dvd1", srcCatalogVersion, list(dvdCat, externCat), pieces, -1);
		dvd1.setProperty(Europe1Constants.PARAMS.PPG, dvdProductPriceGroup);

		dvd2 = createProduct("dvd2", srcCatalogVersion, list(dvdCat, bargainDvdCat, externCat), pieces, 24.99);
		dvd2.setProperty(Europe1Constants.PARAMS.PPG, dvdProductPriceGroup);

		cd1 = createProduct("cd1", srcCatalogVersion, list(cdCat, externCat), pieces, -1);
		cd1.setProperty(Europe1Constants.PARAMS.PPG, cdProductPriceGroup);

		cd2 = createProduct("cd2", srcCatalogVersion, list(cdCat, bargainCdCat, externCat), pieces, 9.99);
		cd2.setProperty(Europe1Constants.PARAMS.PPG, cdProductPriceGroup);

		createProduct("dvdNotInCatalogVersion", null, list(dvdCat, externCat), pieces, -1);
		createProduct("cdNotInCatalogVersion", null, list(cdCat, externCat), pieces, -1);

		keyword1 = createKeyword(srcCatalogVersion, "keyword1", langDe);
		getCatalogManager().addToKeywords(dvd1, langDe, keyword1);

		keyword2 = createKeyword(srcCatalogVersion, "keyword2", langDe);
		getCatalogManager().addToKeywords(cd1, langDe, keyword2);

		keyword3 = createKeyword(srcCatalogVersion, "keyword3", langDe);
		getCatalogManager().addToKeywords(dvdCat, langDe, keyword3);

		keyword4 = createKeyword(srcCatalogVersion, "keyword4", langDe);
		getCatalogManager().addToKeywords(cdCat, langDe, keyword4);
	}

	private void checkSynchronizedCatalogVersion(final CatalogVersion ver) throws JaloInvalidParameterException,
			JaloSecurityException
	{
		Transaction.current().flushDelayedStore();

		assertEquals(1, ver.getRootCategoriesCount());
		assertEquals(6, ver.getAllCategoryCount());
		assertEquals(4, ver.getAllKeywordCount());
		assertEquals(4, ver.getAllProductCount());
		final Set checked = new HashSet();
		checkSynchronizedItem(rootCat, ver.getRootCategories().iterator().next(), checked);

		assertEquals(
				new HashSet(Arrays.asList(new Item[]
				{ rootCat, cdCat, dvdCat, bargainsCat, bargainCdCat, bargainDvdCat, cd1, cd2, dvd1, dvd2, keyword1, keyword2,
						keyword3, keyword4 })), checked);
	}

	private void checkSynchronizedItem(final Item src, final Item tgt, final Set checked) throws JaloInvalidParameterException,
			JaloSecurityException
	{
		Transaction.current().flushDelayedStore();

		if (checked.contains(src))
		{
			return;
		}
		else
		{
			checked.add(src);
		}

		final CatalogVersion srcVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), src);
		final CatalogVersion tgtVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), tgt);

		assertFalse(src.equals(tgt));
		assertFalse(srcVer.equals(tgtVer));
		assertEquals(src.getClass(), tgt.getClass());
		assertEquals(src.getComposedType(), tgt.getComposedType());
		final Collection synchronizedCopies = CatalogManager.getInstance().getSynchronizedCopies(src);
		assertNotNull(synchronizedCopies);
		assertEquals(1, synchronizedCopies.size());
		assertEquals(src, ((ItemSyncTimestamp) synchronizedCopies.iterator().next()).getSourceItem());
		assertEquals(tgt, ((ItemSyncTimestamp) synchronizedCopies.iterator().next()).getTargetItem());

		if (src instanceof Category)
		{
			assertEquals(((Category) src).getCode(), ((Category) tgt).getCode());
			// check subcategories
			final List srcSub = new ArrayList(((Category) src).getSubcategories());
			final List tgtSub = new ArrayList(((Category) tgt).getSubcategories());
			assertEquals(((Category) src).getCode() + " got " + tgtSub + " instead of " + srcSub, srcSub.size(), tgtSub.size());
			for (int i = 0; i < srcSub.size(); i++)
			{
				final Category srcSubCat = (Category) srcSub.get(i);
				final Category tgtSubCat = (Category) tgtSub.get(i);
				final CatalogVersion srcSubCatVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(),
						srcSubCat);
				final CatalogVersion tgtSubCatVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(),
						tgtSubCat);
				// src subcat is from src version ? -> tgt subcat must have been synchronized
				if (srcSubCatVer != null)
				{
					assertEquals(srcVer, srcSubCatVer);
					assertEquals(tgtVer, tgtSubCatVer);
					checkSynchronizedItem(srcSubCat, tgtSubCat, checked);
				}
				// from outside ? -> tgt subcat must be the same
				else
				{
					assertEquals(srcSubCat, tgtSubCat);
					assertNull(srcSubCatVer);
					assertNull(tgtSubCatVer);
				}
			}
			// check contained products
			final List srcProd = new ArrayList(((Category) src).getProducts());
			final List tgtProd = new ArrayList(((Category) tgt).getProducts());
			assertEquals(srcProd.size(), tgtProd.size());
			for (int i = 0; i < srcProd.size(); i++)
			{
				final Product srcCatProd = (Product) srcProd.get(i);
				final Product tgtCatProd = (Product) tgtProd.get(i);
				final CatalogVersion srcSubCatVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(),
						srcCatProd);
				final CatalogVersion tgtSubCatVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(),
						tgtCatProd);
				// src prod is from src version ? -> tgt prod must have been synchronized
				if (srcSubCatVer != null)
				{
					assertEquals(srcVer, srcSubCatVer);
					assertEquals(tgtVer, tgtSubCatVer);
					checkSynchronizedItem(srcCatProd, tgtCatProd, checked);
				}
				// from outside ? -> tgt prod must be the same
				else
				{
					assertEquals(srcCatProd, tgtCatProd);
					assertNull(srcSubCatVer);
					assertNull(tgtSubCatVer);
				}
			}
			// check keywords
			final List srcKw = new ArrayList(CatalogManager.getInstance().getKeywords((Category) src));
			final List tgtKw = new ArrayList(CatalogManager.getInstance().getKeywords((Category) tgt));
			assertEquals(srcKw.size(), tgtKw.size());
			for (int i = 0; i < srcKw.size(); i++)
			{
				final Keyword srcCatKw = (Keyword) srcKw.get(i);
				final Keyword tgtCatKw = (Keyword) tgtKw.get(i);
				assertEquals(srcVer, CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), srcCatKw));
				assertEquals(tgtVer, CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), tgtCatKw));
				checkSynchronizedItem(srcCatKw, tgtCatKw, checked);
			}
		}
		else if (src instanceof Keyword)
		{
			assertEquals(((Keyword) src).getKeyword(), ((Keyword) tgt).getKeyword());
		}
		else if (src instanceof Product)
		{
			assertEquals(((Product) src).getCode(), ((Product) tgt).getCode());
			assertEquals(((Product) src).getUnit(), ((Product) tgt).getUnit());
			assertEquals(((Product) src).getAttribute(Europe1Constants.PARAMS.PPG),
					((Product) tgt).getAttribute(Europe1Constants.PARAMS.PPG));

			// check enclosing categories
			final Set srcCategories = new HashSet(CategoryManager.getInstance().getSupercategories((Product) src));
			final Set tgtCategories = new HashSet(CategoryManager.getInstance().getSupercategories((Product) tgt));
			assertEquals(srcCategories.size(), tgtCategories.size());
			for (final Iterator it = srcCategories.iterator(); it.hasNext();)
			{
				final Category srcSubCat = (Category) it.next();
				final CatalogVersion srcSubCatVer = CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(),
						srcSubCat);
				// src subcat is from src version ? -> tgt subcat must have been synchronized
				if (srcSubCatVer != null)
				{
					final Collection coll = CatalogManager.getInstance().getSynchronizedCopies(srcSubCat);
					assertNotNull(coll);
					assertEquals(1, coll.size());
					final Category tgtSubCat = (Category) ((ItemSyncTimestamp) coll.iterator().next()).getTargetItem();
					assertTrue(tgtCategories.contains(tgtSubCat));
					final CatalogVersion tgtSubCatVer = CatalogManager.getInstance().getCatalogVersion(
							jaloSession.getSessionContext(), tgtSubCat);
					assertEquals(srcVer, srcSubCatVer);
					assertEquals(tgtVer, tgtSubCatVer);
					checkSynchronizedItem(srcSubCat, tgtSubCat, checked);
				}
				// from outside ? -> tgt subcat must be the same
				else
				{
					assertNull(srcSubCatVer);
					assertTrue(tgtCategories.contains(srcSubCat));
				}
			}
			// check keywords
			final Set srcKw = new HashSet(CatalogManager.getInstance().getKeywords((Product) src));
			final Set tgtKw = new HashSet(CatalogManager.getInstance().getKeywords((Product) tgt));
			assertEquals(srcKw.size(), tgtKw.size());
			for (final Iterator it = srcKw.iterator(); it.hasNext();)
			{
				final Keyword srcCatKw = (Keyword) it.next();
				final Collection coll = CatalogManager.getInstance().getSynchronizedCopies(srcCatKw);
				assertNotNull(coll);
				assertEquals(1, coll.size());
				final Keyword tgtCatKw = (Keyword) ((ItemSyncTimestamp) coll.iterator().next()).getTargetItem();
				assertTrue(tgtKw.contains(tgtCatKw));
				assertEquals(srcVer, CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), srcCatKw));
				assertEquals(tgtVer, CatalogManager.getInstance().getCatalogVersion(jaloSession.getSessionContext(), tgtCatKw));
				checkSynchronizedItem(srcCatKw, tgtCatKw, checked);
			}
		}
		else if (src instanceof Media)
		{
			assertEquals(((Media) src).getCode(), ((Media) tgt).getCode());
			assertEquals(((Media) src).getMime(), ((Media) tgt).getMime());
			assertTrue(((Media) src).hasData() == ((Media) tgt).hasData());
			assertEquals(((Media) src).getRealFileName(), ((Media) tgt).getRealFileName());
		}
	}

	private void createTargetCatalogVersion()
	{
		registerForRemoval(tgtCatalogVersion = getCatalogManager().createCatalogVersion(srcCatalog, "tgtCatalogVersion", langDe));

		final Map params = new HashMap();
		params.put(SyncItemJob.SOURCEVERSION, srcCatalogVersion);
		params.put(SyncItemJob.TARGETVERSION, tgtCatalogVersion);
		registerForRemoval(src2tgtJob = getCatalogManager().createSyncItemJob(params));
	}


	private Category createCategory(final String code, final CatalogVersion version, final Category superCategory)
	{
		final Category cat = CategoryManager.getInstance().createCategory(code);
		registerForRemoval(cat);
		if (version != null)
		{
			getCatalogManager().setCatalogVersion(cat, version);
		}
		if (superCategory != null)
		{
			cat.setSupercategories(Collections.singletonList(superCategory));
		}
		return cat;
	}

	private Product createProduct(final String code, final CatalogVersion version, final Collection categories, final Unit unit,
			final double price) throws Exception
	{
		final Product product = ProductManager.getInstance().createProduct(code);
		registerForRemoval(product);
		if (version != null)
		{
			getCatalogManager().setCatalogVersion(product, version);
		}
		if (categories != null)
		{
			for (final Iterator it = categories.iterator(); it.hasNext();)
			{
				final Category category = (Category) it.next();
				category.addProduct(product);
			}
		}
		product.setUnit(unit);

		if (price > -1)
		{
			createPrice(product, null, unit, price);
		}

		return product;
	}

	private void createPrice(final Product product, final EnumerationValue productPriceGroup, final Unit unit, final double price)
			throws Exception
	{
		final Europe1PriceFactory europe1PriceFactory = (Europe1PriceFactory) jaloSession.getOrderManager().getPriceFactory();
		final User user = null;
		final EnumerationValue userPriceGroup = null;
		final int minQuantity = 1;
		final Currency currency = currEur;
		final int unitFactor = 1;
		final boolean net = true;
		final DateRange dateRange = null;
		europe1PriceFactory.createPriceRow(product, productPriceGroup, user, userPriceGroup, minQuantity, currency, unit,
				unitFactor, net, dateRange, price);
	}

	private Keyword createKeyword(final CatalogVersion version, final String keyword, final Language lang)
	{
		final Keyword keywordItem = getCatalogManager().createKeyword(version, keyword, lang);
		registerForRemoval(keywordItem);
		return keywordItem;
	}

	private CatalogManager getCatalogManager()
	{
		return CatalogManager.getInstance();
	}

	@Test
	public void testDefaultRootTypes()
	{
		final TypeManager typeManager = TypeManager.getInstance();
		final ComposedType product = typeManager.getComposedType("Product");
		final ComposedType category = typeManager.getComposedType("Category");
		final ComposedType keyword = typeManager.getComposedType("Keyword");
		final ComposedType media = typeManager.getComposedType("Media");
		final ComposedType mediaContainer = typeManager.getComposedType("MediaContainer");
		final ComposedType taxRow = typeManager.getComposedType("TaxRow");
		final ComposedType priceRow = typeManager.getComposedType("PriceRow");
		final ComposedType discountRow = typeManager.getComposedType("DiscountRow");
		final ComposedType variantProduct = typeManager.getComposedType("VariantProduct");
		final List<ComposedType> rootTypes = new ArrayList();
		rootTypes.add(product);
		rootTypes.add(category);
		rootTypes.add(keyword);
		rootTypes.add(media);
		rootTypes.add(mediaContainer);
		rootTypes.add(priceRow);
		rootTypes.add(taxRow);
		rootTypes.add(discountRow);
		assertNotNull("SyncItemJob.getDefaultRootTypes() should not return null", SyncItemJob.getDefaultRootTypes());
		assertFalse("SyncItemJob.getDefaultRootTypes() should not return empty list", SyncItemJob.getDefaultRootTypes().isEmpty());
		assertTrue(
				"SyncItemJob.getDefaultRootTypes() should contain all of following: Product, Category, Keyword, Media, MediaContainer, TaxRow, PriceRow, DiscountRow",
				SyncItemJob.getDefaultRootTypes().containsAll(rootTypes));
		assertFalse("SyncItemJob.getDefaultRootTypes() should not contain VariantProduct", SyncItemJob.getDefaultRootTypes()
				.contains(variantProduct));
	}

	@Test
	public void testBugCATALOG400() throws Exception
	{
		final ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);
		final ComposedType mediaType = TypeManager.getInstance().getComposedType(Media.class);

		AttributeDescriptor attributeDescriptor = null;
		registerForRemoval(attributeDescriptor = productType.createAttributeDescriptor("pseudoPartOfTest", mediaType,
				AttributeDescriptor.READ_FLAG + AttributeDescriptor.INITIAL_FLAG + AttributeDescriptor.REMOVE_FLAG));
		assertFalse(attributeDescriptor.isOptional());
		assertFalse(attributeDescriptor.isWritable());
		assertFalse(attributeDescriptor.isPartOf());
		assertTrue(attributeDescriptor.isInitial());
		assertTrue(attributeDescriptor.isReadable());

		final Media media;
		registerForRemoval(media = MediaManager.getInstance().createMedia("pseudoPartOfTestMedia"));
		CatalogManager.getInstance().setCatalogVersion(media, srcCatalogVersion);

		final Map attributes = new HashMap();
		attributes.put(Product.CODE, "pseudoPartOfTestProd");
		attributes.put(Product.UNIT, pieces);
		attributes.put(CatalogConstants.Attributes.Product.CATALOGVERSION, srcCatalogVersion);
		attributes.put(attributeDescriptor.getQualifier(), media);
		final Product product;
		registerForRemoval(product = (Product) productType.newInstance(attributes));

		assertEquals(media, product.getAttribute(attributeDescriptor.getQualifier()));

		SyncItemCronJob cronJob = null;
		try
		{
			assertEquals(srcCatalogVersion, src2tgtJob.getSourceVersion());
			assertEquals(tgtCatalogVersion, src2tgtJob.getTargetVersion());

			cronJob = src2tgtJob.newExecution();
			registerForRemoval(cronJob);
			// log setting
			cronJob.setLogToDatabase(false);
			cronJob.setLogToFile(false);

			cronJob.setErrorMode(EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
					CronJobConstants.Enumerations.ErrorMode.FAIL));
			assertTrue(src2tgtJob.isCreateNewItemsAsPrimitive());
			assertFalse(src2tgtJob.isRemoveMissingItemsAsPrimitive());
			assertFalse(cronJob.isLogToDatabaseAsPrimitive());
			assertFalse(cronJob.isLogToFileAsPrimitive());
			assertTrue(cronJob.isActiveAsPrimitive());
			assertFalse((cronJob.isRunning() || cronJob.isRunningRestart()));
			assertTrue(src2tgtJob.isPerformable(cronJob));

			assertEquals(Collections.EMPTY_LIST, cronJob.getPendingItems());

			src2tgtJob.addCatalogItemsToSync(cronJob, Collections.singletonList(product));
			assertEquals(Collections.singletonList(product), cronJob.getPendingItems());

			// now sync right here / we're got to register copied items for removal too
			final SyncItemJob proxy = new SyncItemJob()
			{
				@Override
				protected void registerSynchronizedItem(final SyncItemCronJob cronjob, final Item source, final Item copy,
						final String message)
				{
					super.registerSynchronizedItem(cronjob, source, copy, message);
					if (copy != null && copy.isAlive())
					{
						registerForRemoval(copy);
					}
				}
			};
			proxy.setImplementation(src2tgtJob.getImplementation());
			proxy.setTenant(Registry.getCurrentTenant());
			proxy.perform(cronJob, true);

			Transaction.current().flushDelayedStore();

			assertFalse((cronJob.isRunning() || cronJob.isRunningRestart()));
			assertTrue(cronJob.isActiveAsPrimitive());
			assertEquals(cronJob.getSuccessResult(), cronJob.getResult());
			assertEquals(cronJob.getFinishedStatus(), cronJob.getStatus());
			assertEquals(Collections.EMPTY_LIST, cronJob.getPendingItems());

			Collection syncTimestamps = CatalogManager.getInstance().getSynchronizedCopies(product);
			assertEquals(1, syncTimestamps.size());
			ItemSyncTimestamp timestamp = (ItemSyncTimestamp) syncTimestamps.iterator().next();
			assertEquals(product, timestamp.getSourceItem());

			final Product copy = (Product) timestamp.getTargetItem();
			final Set checked = new HashSet();
			checkSynchronizedItem(product, copy, checked);

			assertEquals(media, product.getAttribute(attributeDescriptor.getQualifier()));

			syncTimestamps = CatalogManager.getInstance().getSynchronizedCopies(media);
			assertEquals(1, syncTimestamps.size());
			timestamp = (ItemSyncTimestamp) syncTimestamps.iterator().next();
			assertEquals(media, timestamp.getSourceItem());

			final Media mediaCopy = (Media) timestamp.getTargetItem();
			assertEquals(mediaCopy, copy.getAttribute(attributeDescriptor.getQualifier()));

			checkSynchronizedItem(media, mediaCopy, checked);
		}
		catch (final Exception e)
		{
			LOG.error("synchronization test failed: dumping log ...");
			if (cronJob != null && cronJob.isAlive())
			{
				for (final Iterator iter = cronJob.getLogs().iterator(); iter.hasNext();)
				{
					final JobLog log = (JobLog) iter.next();
					LOG.error(log.getMessage());
				}
			}
			throw e;
		}
	}

	@Test
	public void testFullSnyc() throws Exception
	{

		SyncItemCronJob cronJob = null;
		try
		{
			assertEquals(srcCatalogVersion, src2tgtJob.getSourceVersion());
			assertEquals(tgtCatalogVersion, src2tgtJob.getTargetVersion());

			cronJob = src2tgtJob.newExecution();
			registerForRemoval(cronJob);
			// log setting
			cronJob.setLogToDatabase(false);
			cronJob.setLogToFile(false);
			/*
			 * cronJob.setLogLevelDatabase( EnumerationManager.getInstance().getEnumerationValue(
			 * CronJobConstants.TC.JOBLOGLEVEL, CronJobConstants.Enumerations.JobLogLevel.DEBUG ) );
			 */
			cronJob.setErrorMode(EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
					CronJobConstants.Enumerations.ErrorMode.FAIL));

			Transaction.current().flushDelayedStore();

			assertTrue(src2tgtJob.isCreateNewItemsAsPrimitive());
			assertFalse(src2tgtJob.isRemoveMissingItemsAsPrimitive());
			assertFalse(cronJob.isLogToDatabaseAsPrimitive());
			assertFalse(cronJob.isLogToFileAsPrimitive());
			assertTrue(cronJob.isActiveAsPrimitive());
			assertFalse((cronJob.isRunning() || cronJob.isRunningRestart()));
			assertTrue(src2tgtJob.isPerformable(cronJob));

			assertEquals(Collections.EMPTY_LIST, cronJob.getPendingItems());

			src2tgtJob.configureFullVersionSync(cronJob);

			Transaction.current().flushDelayedStore();


			assertEquals(
					new HashSet(Arrays.asList(new Item[]
					{ dvd1, dvd2, cd1, cd2, rootCat, dvdCat, cdCat, bargainsCat, bargainDvdCat, bargainCdCat, keyword1, keyword2,
							keyword3, keyword4 })), new HashSet(cronJob.getPendingItems()));

			// now sync right here / we're got to register copied items for removal too
			final SyncItemJob proxy = new SyncItemJob()
			{
				@Override
				protected void registerSynchronizedItem(final SyncItemCronJob cronjob, final Item source, final Item copy,
						final String message)
				{
					super.registerSynchronizedItem(cronjob, source, copy, message);
					if (copy != null && copy.isAlive())
					{
						registerForRemoval(copy);
					}
				}
			};
			proxy.setImplementation(src2tgtJob.getImplementation());
			proxy.setTenant(Registry.getCurrentTenant());
			proxy.perform(cronJob, true);

			Transaction.current().flushDelayedStore();

			assertFalse((cronJob.isRunning() || cronJob.isRunningRestart()));
			assertTrue(cronJob.isActiveAsPrimitive());
			assertEquals(cronJob.getSuccessResult(), cronJob.getResult());
			assertEquals(cronJob.getFinishedStatus(), cronJob.getStatus());
			assertEquals(Collections.EMPTY_LIST, cronJob.getPendingItems());

			checkSynchronizedCatalogVersion(tgtCatalogVersion);
		}
		catch (final Exception e)
		{
			LOG.error("synchronization test failed: dumping log ...");
			if (cronJob != null && cronJob.isAlive())
			{
				for (final Iterator iter = cronJob.getLogs().iterator(); iter.hasNext();)
				{
					final JobLog log = (JobLog) iter.next();
					LOG.error(log.getMessage());
				}
			}
			throw e;
		}
	}

	@Test
	public void testFullSnycPLA8490() throws Exception
	{

		SyncItemCronJob cronJob = null;
		try
		{
			final Collection<Language> allLang = srcCatalogVersion.getLanguages();
			//remove all languages from both source and target catalog version

			srcCatalogVersion.setLanguages(Collections.EMPTY_LIST);
			tgtCatalogVersion.setLanguages(Collections.EMPTY_LIST);

			//change a localized (and non localized) attribute at a single item
			//dvd1.setCode(jaloSession.getSessionContext(), "changedCode4Dvd1");
			dvd1.setName(jaloSession.getSessionContext(), "changedLocalizedName4Dvd1");
			//dvd2.setCode(jaloSession.getSessionContext(), "changedCode4Dvd2");
			dvd2.setName(jaloSession.getSessionContext(), "changedLocalizedName4Dvd2");
			//cd1.setCode(jaloSession.getSessionContext(), "changedCode4Cd1");
			cd1.setName(jaloSession.getSessionContext(), "changedLocalizedName4Cd1");
			//cd2.setCode(jaloSession.getSessionContext(), "changedCode4Cd2");
			cd2.setName(jaloSession.getSessionContext(), "changedLocalizedName4Cd2");
			//completely synchronize the two catalog versions -> all items are up to date but the localized values are missing of course
			cronJob = performSync(false);

			//now add all languages to source and target version again
			srcCatalogVersion.setLanguages(allLang);
			tgtCatalogVersion.setLanguages(allLang);

			//try full sync without 'forceupdate' -> it wont schedule any item (correctly)
			cronJob = performSync(false);
			assertNull(tgtCatalogVersion.getProduct(dvd1.getCode()).getName());
			assertNull(tgtCatalogVersion.getProduct(dvd2.getCode()).getName());
			assertNull(tgtCatalogVersion.getProduct(cd1.getCode()).getName());
			assertNull(tgtCatalogVersion.getProduct(cd2.getCode()).getName());
			//try full sync using forceupdate=true			
			cronJob = performSync(true);
			assertProductSynchronized(dvd1);
			assertProductSynchronized(dvd2);
			assertProductSynchronized(cd1);
			assertProductSynchronized(cd2);

		}
		catch (final Exception e)
		{
			LOG.error("synchronization test failed: dumping log ...");
			if (cronJob != null && cronJob.isAlive())
			{
				for (final Iterator iter = cronJob.getLogs().iterator(); iter.hasNext();)
				{
					final JobLog log = (JobLog) iter.next();
					LOG.error(log.getMessage());
				}
			}
			throw e;
		}
	}

	/**
	 * tests if the localized (name) and non localized (code) attributes really had been changed
	 */
	private void assertProductSynchronized(final Product product)
	{
		Assert.assertNotNull(" Target catalog should contain product.code :" + product.getCode(),
				tgtCatalogVersion.getProduct(product.getCode()));
		Assert.assertEquals(" Target and source catalog products code should be equal ", product.getCode(), tgtCatalogVersion
				.getProduct(product.getCode()).getCode());
		Assert.assertEquals(" Target and source catalog products name should be equal ", product.getName(), tgtCatalogVersion
				.getProduct(product.getCode()).getName());
	}

	private SyncItemCronJob performSync(final boolean forceUpdate/* , final Item... expectedPendingItems */)
			throws JaloSecurityException
	{
		SyncItemCronJob cronJob;
		cronJob = src2tgtJob.newExecution();
		cronJob.setForceUpdate(forceUpdate);
		registerForRemoval(cronJob);
		// log setting
		cronJob.setLogToDatabase(false);
		cronJob.setLogToFile(false);
		cronJob.setErrorMode(EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
				CronJobConstants.Enumerations.ErrorMode.FAIL));

		Transaction.current().flushDelayedStore();

		src2tgtJob.configureFullVersionSync(cronJob);

		Transaction.current().flushDelayedStore();

		// now sync right here / we're got to register copied items for removal too
		final SyncItemJob proxy = new SyncItemJob()
		{
			@Override
			protected void registerSynchronizedItem(final SyncItemCronJob cronjob, final Item source, final Item copy,
					final String message)
			{
				super.registerSynchronizedItem(cronjob, source, copy, message);
				if (copy != null && copy.isAlive())
				{
					registerForRemoval(copy);
				}
			}
		};
		proxy.setImplementation(src2tgtJob.getImplementation());
		proxy.setTenant(Registry.getCurrentTenant());
		proxy.perform(cronJob, true);

		Transaction.current().flushDelayedStore();
		assertFalse((cronJob.isRunning() || cronJob.isRunningRestart()));
		assertTrue(cronJob.isActiveAsPrimitive());
		assertEquals(cronJob.getSuccessResult(), cronJob.getResult());
		assertEquals(cronJob.getFinishedStatus(), cronJob.getStatus());
		assertEquals(Collections.EMPTY_LIST, cronJob.getPendingItems());
		//checkSynchronizedCatalogVersion(tgtCatalogVersion);
		return cronJob;
	}

	@Test
	public void testSynchroniationPermissions()
	{
		final SyncItemJob proxy1 = new SyncItemJob()
		{
			@Override
			public List<Principal> getSyncPrincipals(final SessionContext ctx)
			{
				return Arrays.asList((Principal) syncUser);
			}

			@Override
			public CatalogVersion getTargetVersion(final SessionContext ctx)
			{
				return tgtCatalogVersion;
			}

			@Override
			public CatalogVersion getSourceVersion(final SessionContext ctx)
			{
				return srcCatalogVersion;
			}

			@Override
			public Boolean isSyncPrincipalsOnly(final SessionContext ctx)
			{
				return Boolean.FALSE;
			}
		};

		final SyncItemJob proxy2 = new SyncItemJob()
		{
			@Override
			public List<Principal> getSyncPrincipals(final SessionContext ctx)
			{
				return Collections.EMPTY_LIST;
			}

			@Override
			public CatalogVersion getTargetVersion(final SessionContext ctx)
			{
				return tgtCatalogVersion;
			}

			@Override
			public CatalogVersion getSourceVersion(final SessionContext ctx)
			{
				return srcCatalogVersion;
			}

			@Override
			public Boolean isSyncPrincipalsOnly(final SessionContext ctx)
			{
				return Boolean.FALSE;
			}
		};

		final SyncItemJob proxy3 = new SyncItemJob()
		{
			@Override
			public List<Principal> getSyncPrincipals(final SessionContext ctx)
			{
				return Collections.EMPTY_LIST;
			}

			@Override
			public CatalogVersion getTargetVersion(final SessionContext ctx)
			{
				return tgtCatalogVersion;
			}

			@Override
			public CatalogVersion getSourceVersion(final SessionContext ctx)
			{
				return srcCatalogVersion;
			}

			@Override
			public Boolean isSyncPrincipalsOnly(final SessionContext ctx)
			{
				return Boolean.TRUE;
			}
		};

		assertTrue("User should have granted sync. permissions because of his memebrship in the sync-principals group!",
				CatalogManager.getInstance().canSync(JaloSession.getCurrentSession().getSessionContext(), syncUser, proxy1));

		assertFalse("User shouldn't have granted sync. permissions!",
				CatalogManager.getInstance().canSync(JaloSession.getCurrentSession().getSessionContext(), syncUser, proxy2));

		assertFalse("shouldn't have granted sync. permissions!",
				CatalogManager.getInstance().canSync(JaloSession.getCurrentSession().getSessionContext(), syncUser, proxy3));

		getCatalogManager().addToWritableCatalogVersions(syncUser, tgtCatalogVersion);

		assertTrue("User have granted sync. permissions, because of his catalog write-permissions!", CatalogManager.getInstance()
				.canSync(JaloSession.getCurrentSession().getSessionContext(), syncUser, proxy3));
	}

	// 	 PLA-8375
	@Test
	public void testDuplicatedTaxRowEntries() throws JaloInvalidParameterException, ConsistencyCheckException,
			JaloPriceFactoryException
	{

		final Language langDe = getOrCreateLanguage("de");

		jaloSession.getSessionContext().setLanguage(langDe);

		final Catalog srcCatalog = CatalogManager.getInstance().createCatalog("srcDuplicatedCatalog");

		final Category srcCategory = CategoryManager.getInstance().createCategory("srcDuplicatedCategory");


		final CatalogVersion srcCatalogVersion = CatalogManager.getInstance().createCatalogVersion(srcCatalog,
				"srcDuplicatedCatalogVersion", langDe);

		srcCatalog.addToCatalogVersions(srcCatalogVersion);

		final CatalogVersion tgtCatalogVersion = CatalogManager.getInstance().createCatalogVersion(srcCatalog,
				"tgtDuplicatedCatalogVersion", langDe);

		final Map params = new HashMap();
		params.put(SyncItemJob.SOURCEVERSION, srcCatalogVersion);
		params.put(SyncItemJob.TARGETVERSION, tgtCatalogVersion);
		final SyncItemJob src2tgtJob = CatalogManager.getInstance().createSyncItemJob(params);

		final Europe1PriceFactory europe1PriceFactory = Europe1PriceFactory.getInstance();

		final Tax tax1 = OrderManager.getInstance().createTax("FULL");

		final EnumerationValue full = EnumerationManager.getInstance().createEnumerationValue(Europe1Constants.TC.PRODUCTTAXGROUP,
				"test_full");

		final EnumerationValue userGrp1 = EnumerationManager.getInstance().createEnumerationValue(
				Europe1Constants.TC.PRODUCTTAXGROUP, "userGrp1");


		final User jaloUser = jaloSession.getUser();

		final Product fooProduct = ProductManager.getInstance().createProduct("foo");

		srcCategory.addProduct(fooProduct);

		europe1PriceFactory.setEurope1PriceFactory_PTG(fooProduct, full);
		europe1PriceFactory.setEurope1PriceFactory_UTG(jaloUser, userGrp1);

		final DateRange _1_12_1986_30_11_2008 = new StandardDateRange(new Date(1986, 11, 1), new Date(2008, 10, 30));
		final DateRange _1_12_2008_31_12_2009 = new StandardDateRange(new Date(2008, 11, 1), new Date(2009, 11, 31));
		final DateRange _1_1_2010_31_12_2099 = new StandardDateRange(new Date(2010, 0, 1), new Date(2099, 11, 31));
		//add taxrow entries
		final TaxRow tr1 = europe1PriceFactory.createTaxRow(null, full, null, userGrp1, tax1, _1_12_1986_30_11_2008,
				Double.valueOf(17.5));
		tr1.setProperty("catalogversion", srcCatalogVersion);
		final TaxRow tr2 = europe1PriceFactory.createTaxRow(null, full, null, userGrp1, tax1, _1_12_2008_31_12_2009,
				Double.valueOf(15));
		tr2.setProperty("catalogversion", srcCatalogVersion);
		final TaxRow tr3 = europe1PriceFactory.createTaxRow(null, full, null, userGrp1, tax1, _1_1_2010_31_12_2099,
				Double.valueOf(17.5));
		tr3.setProperty("catalogversion", srcCatalogVersion);

		SyncItemCronJob cronJob = null;

		cronJob = src2tgtJob.newExecution();
		// log setting
		cronJob.setLogToDatabase(false);
		cronJob.setLogToFile(false);

		cronJob.setErrorMode(EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
				CronJobConstants.Enumerations.ErrorMode.FAIL));

		src2tgtJob.addCatalogItemsToSync(cronJob, Collections.singletonList(fooProduct));

		src2tgtJob.perform(cronJob, true);

		Assert.assertEquals("Cronjob shouldn't fail ", cronJob.getResult(), cronJob.getSuccessResult());

	}

}
