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
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Integration for checking an PLA-10905. Fixed by introducing a separate caches for catalog and non-catalog aware
 * attributes.
 */
@IntegrationTest
public class CatalogVersionSyncCopyContextTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(CatalogVersionSyncCopyContextTest.class);

	private CatalogVersionSyncCopyContext ctx;

	private CatalogVersion srcCatalogVersion;

	private CatalogVersion tgtCatalogVersion;

	private PriceRow priceRowSpy;


	@Before
	public void setUp() throws Exception
	{
		final CatalogManager catalogManager = CatalogManager.getInstance();

		final Catalog catalog = catalogManager.createCatalog("PartOfTest");
		srcCatalogVersion = catalogManager.createCatalogVersion(catalog, "ver1", null);
		srcCatalogVersion.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));
		tgtCatalogVersion = catalogManager.createCatalogVersion(catalog, "ver2", null);
		tgtCatalogVersion.setLanguages(Collections.singletonList(getOrCreateLanguage("de")));

		LOG.info("Creating  product");
		final ComposedType composedType = TypeManager.getInstance().getComposedType(Product.class);

		final Product productOne = createProduct("Product-One", composedType);
		final Product productTwo = createProduct("Product-Two", composedType);

		LOG.info("Creating  product reference ");

		catalogManager.createProductReference("foo", productOne, productTwo, Integer.valueOf(1));

		LOG.info("Done catalog creation.");


		final Europe1PriceFactory europe1 = Europe1PriceFactory.getInstance();
		final Currency currency = C2LManager.getInstance().createCurrency("europe1/dr");
		final Unit unit = ProductManager.getInstance().createUnit(null, "europe1/u", "typ");
		final EnumerationValue enumValue = EnumerationManager.getInstance().createEnumerationValue(
				Europe1Constants.TYPES.DISCOUNT_USER_GROUP, "test");

		priceRowSpy = Mockito.spy(europe1.createPriceRow(productTwo, null, null, enumValue, 0, currency, unit, 1, true, null, 0));
	}

	private Product createProduct(final String code, final ComposedType composedType) throws JaloGenericCreationException,
			JaloAbstractTypeException
	{
		final Map args = new HashMap();
		args.put(Product.CODE, code);
		args.put(CatalogConstants.Attributes.Product.CATALOGVERSION, srcCatalogVersion);
		return (Product) composedType.newInstance(args);
	}

	/**
	 * Test check if Overwriting of unique key attribute cache is working
	 */
	@Test
	public void testOverwriteUniqueKeyAttributeCache()
	{

		final ComposedType priceRowComposedTypeSpy = Mockito.spy(TypeManager.getInstance().getComposedType(PriceRow.class));

		final CatalogManager catalogManager = CatalogManager.getInstance();

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "foo");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, srcCatalogVersion);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgtCatalogVersion);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(1));
		final CatalogVersionSyncJob job = catalogManager.createCatalogVersionSyncJob(args);

		final CatalogVersionSyncCronJob cjob = (CatalogVersionSyncCronJob) job.newExecution();

		ctx = Mockito.spy(new CatalogVersionSyncCopyContext(job, cjob, null));

		Mockito.when(priceRowComposedTypeSpy.getAttributeDescriptorsIncludingPrivate()).thenReturn(Collections.EMPTY_SET);
		Mockito.when(priceRowSpy.getComposedType()).thenReturn(priceRowComposedTypeSpy);

		ctx.queryNonCatalogItemCopy(priceRowSpy);
		ctx.queryCatalogItemCopy(priceRowSpy);
	}

}
