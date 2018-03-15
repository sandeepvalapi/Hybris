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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class RemoveCatalogAndCatalogVersionTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(RemoveCatalogAndCatalogVersionTest.class);
	private final CatalogManager catman = CatalogManager.getInstance();
	private final CategoryManager categoryman = CategoryManager.getInstance();
	private final ProductManager prodman = ProductManager.getInstance();
	private String impexModeBackup;

	@Before
	public void setUp() throws Exception
	{
		impexModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		new CoreBasicDataCreator().createSupportedEncodings();

		importCsv("/catalog/testdata_catalog.csv", "utf-8");
	}

	@After
	public void restoreOriginalImpexMode()
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, impexModeBackup);
	}

	private void importCsv(final String csvFile, final String encoding) throws ImpExException
	{
		// importing test csv
		LOG.info("importing resource " + csvFile);
		final InputStream inputstream = HybrisJUnit4Test.class.getResourceAsStream(csvFile);
		final Importer importer = ImpExManager.getInstance().importDataLight(inputstream, encoding, true);
		if (importer.hasUnresolvedLines())
		{
			fail("Import has unresolved lines:\n" + importer.getDumpHandler().getDumpAsString());
		}
		assertFalse(importer.hadError());
	}

	private RemoveCatalogVersionCronJob getCronJob() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final ComposedType comptype = JaloSession.getCurrentSession().getTypeManager()
				.getComposedType(RemoveCatalogVersionCronJob.class);
		RemoveCatalogVersionCronJob cronjob = null;
		final HashMap params = new HashMap();
		params.put(CronJob.JOB, CatalogManager.getInstance().getOrCreateDefaultRemoveCatalogVersionJob());
		cronjob = (RemoveCatalogVersionCronJob) comptype.newInstance(params);
		return cronjob;
	}

	@Test
	public void testSetUpSuccessfull()
	{
		assertEquals("testitem not found", 1, categoryman.getCategoriesByCode("CategoryB1-1").size());
		final Catalog catB = catman.getCatalog("CatalogB"); // Catalog item
		assertNotNull("testitem not found", catB);
		final CatalogVersion cvB2 = catB.getActiveCatalogVersion(); // catalogversion
		assertEquals("testitem not found", "CatalogVersionB2", cvB2.getVersion());
		final Keyword keyword = catman.getKeyword("Keyword", cvB2, "B1-keyword2"); // keyword
		assertNotNull("testitem not found", keyword);
		assertEquals("testitem not found", 1, keyword.getProductsCount()); // product
		assertEquals("testitem not found", 1, keyword.getCategoriesCount()); // category
		assertEquals("testitem not found", 1, prodman.getProductsByCode("Product-AB-inC").size());
		final Product prod = (Product) prodman.getProductsByCode("Product-AB-inC").iterator().next();
		assertNotNull("testitem not found", prod);
		assertTrue("testitem not found", prod.isAlive());
	}

	@Test
	public void testRemoveCatalogD() throws Exception
	{
		final Catalog catD = catman.getCatalog("CatalogD");
		assertNotNull("Catalog not found", catD);
		assertTrue("Catalog is not alive", catD.isAlive());

		final RemoveCatalogVersionCronJob cronjob = getCronJob();
		cronjob.setCatalog(catD);
		cronjob.getJob().perform(cronjob, true);

		assertFalse("Catalog D is alive/was not removed!", catD.isAlive());
		assertTrue("Catalog B was removed but shouln't be", catman.getCatalog("CatalogB").isAlive());

	}

	@Test
	public void testRemoveCatalogC() throws Exception
	{
		final Catalog catC = catman.getCatalog("CatalogC");
		assertEquals("Categorycount of Catalog C doesn't fit", 1, catC.getCatalogVersions().size());
		final CatalogVersion catver = catC.getCatalogVersion("CatalogVersionC1");
		assertTrue("CatalogVersion was already deleted", catver.isAlive());

		final RemoveCatalogVersionCronJob cronjob = getCronJob();
		cronjob.setCatalog(catC);
		cronjob.getJob().perform(cronjob, true);

		assertFalse("CatalogVersion in Catalog C was not removed", catver.isAlive());
		assertFalse("Catalog C was not removed", catC.isAlive());
	}

	@Test
	public void testRemoveCatalogVersionC1() throws Exception
	{
		final Catalog catC = catman.getCatalog("CatalogC");
		final CatalogVersion catver = catC.getCatalogVersion("CatalogVersionC1");

		final RemoveCatalogVersionCronJob cronjob = getCronJob();
		cronjob.setCatalog(catC);
		cronjob.setCatalogVersion(catver);
		cronjob.getJob().perform(cronjob, true);

		assertTrue("Product in Catalog C was not deleted", prodman.getProductsByCode("Product-AB-inC").isEmpty());
		assertFalse("CatalogVersion is still alive", catver.isAlive());
		assertTrue("Catalog c was deleted, but only the catalogversion should be", catC.isAlive());
	}

	@Test
	public void testRemoveCatalogB() throws Exception
	{
		final Catalog catB = catman.getCatalog("CatalogB");

		final Product product_A1_1 = (Product) prodman.getProductsByCode("Product-A1-1").iterator().next();
		final Product product_A1_2 = (Product) prodman.getProductsByCode("Product-A1-2").iterator().next();
		final Product product_A1_11 = (Product) prodman.getProductsByCode("Product-A1-11").iterator().next();
		final Product product_B_5555 = (Product) prodman.getProductsByCode("Product-B-5555").iterator().next();
		final Product product_A2_2 = (Product) prodman.getProductsByCode("Product-A2-2").iterator().next();

		final ProductReference pr1 = catman.getProductReferences("pr1", product_A1_1, product_A1_2).iterator().next(); // noch
																																							// da
		final ProductReference pr4 = catman.getProductReferences("pr4", product_A1_11, product_B_5555).iterator().next(); // weg?
		final ProductReference pr10 = catman.getProductReferences("pr10", product_B_5555, product_A2_2).iterator().next(); // weg!
		assertNotNull("ProductReference not found", pr1);
		assertNotNull("ProductReference not found", pr4);
		assertNotNull("ProductReference not found", pr10);

		final RemoveCatalogVersionCronJob cronjob = getCronJob();
		cronjob.setCatalog(catB);
		cronjob.getJob().perform(cronjob, true);

		// categories - 4 deleted
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryB1-1").isEmpty());
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryB1-11").isEmpty());
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryB1-111").isEmpty());
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryB1-1111").isEmpty());
		assertEquals("Category not found!", 1, categoryman.getCategoriesByCode("Category-nowhere-1").size());
		assertEquals("Category not found!", 1, categoryman.getCategoriesByCode("CategoryA-8").size());

		// products 2
		assertTrue("Product was not deleted", prodman.getProductsByCode("Product-B-5555").isEmpty());
		assertTrue("Product was not deleted", prodman.getProductsByCode("Product-AB-inB").isEmpty());
		assertEquals("Product not found!", 1, prodman.getProductsByCode("Product-A1-12").size());
		assertEquals("Product not found!", 1, prodman.getProductsByCode("Product-AB-inC").size());
		assertEquals("Product not found!", 1, prodman.getProductsByCode("Product-A2-2").size());

		// keywords 1 + 1
		assertNull("Keyword was not deleted", catman.getKeyword("Keyword", null, "B1-keyword1"));
		assertNull("Keyword was not deleted", catman.getKeyword("Keyword", null, "B1-keyword2"));

		final Catalog catA = catman.getCatalog("CatalogA");
		assertEquals("catalog id does not match", "CatalogA", catA.getId());
		final CatalogVersion catverA2 = catA.getCatalogVersion("CatalogVersionA2");
		assertFalse("CatalogVersionA2 is active CatalogVersion, shouldn't be", catverA2.isActive().booleanValue());
		assertNotNull("Keyword not found", catman.getKeyword("Keyword", catverA2, "A2-de-prod-a1+cat-a1"));

		// productreferences - not catalog aware but should be removed together the products
		assertTrue("Productreference was removed but shouldn't be", pr1.isAlive());
		assertFalse("Productreference was not deleted", pr4.isAlive());
		assertFalse("Productreference was not deleted", pr10.isAlive());
	}

	@Test
	public void testRemoveCatalogA() throws Exception
	{
		final Catalog catA = catman.getCatalog("CatalogA");

		final RemoveCatalogVersionCronJob cronjob = getCronJob();
		cronjob.setCatalog(catA);
		cronjob.getJob().perform(cronjob, true);

		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryA1-1").isEmpty());
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryA2-12").isEmpty());
		assertTrue("Category was not deleted", categoryman.getCategoriesByCode("CategoryA-81").isEmpty());
		assertEquals("Category not found!", 1, categoryman.getCategoriesByCode("Category-nowhere-1").size());
		assertEquals("Category not found!", 1, categoryman.getCategoriesByCode("CategoryB1-1111").size());

		assertTrue("Product was not deleted", prodman.getProductsByCode("Product-A-11").isEmpty());
		assertTrue("Product was not deleted", prodman.getProductsByCode("Product-AB-inA").isEmpty());
		assertEquals("Product not found!", 1, prodman.getProductsByCode("Product-AB-inB").size());
		assertEquals("Product not found!", 1, prodman.getProductsByCode("Product-AB-nowhere").size());

	}


}
