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
package de.hybris.platform.catalog.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests current implementation of {@link CatalogDao}.
 */
@IntegrationTest
public class CatalogDaoTest extends AbstractCatalogTest
{

	@Resource
	private CatalogDao catalogDao;

	private CatalogModel testCatalog1Example;
	private CatalogModel testCatalog2Example;

	@Before
	public void before()
	{
		final CatalogModel catalogTemplate = new CatalogModel();
		catalogTemplate.setId(TEST_CATALOG_1);
		testCatalog1Example = flexibleSearchService.getModelByExample(catalogTemplate);
		catalogTemplate.setId(TEST_CATALOG_2);
		testCatalog2Example = flexibleSearchService.getModelByExample(catalogTemplate);
	}

	/**
	 * Test method for {@link de.hybris.platform.catalog.daos.CatalogDao#findCatalogs(java.lang.String)}.
	 */
	@Test
	public void testFindCatalogById()
	{
		final CatalogModel testCatalog1 = catalogDao.findCatalogById(TEST_CATALOG_1);
		Assert.assertEquals("Unexpected catalog id", TEST_CATALOG_1, testCatalog1.getId());
		Assert.assertEquals("Unexpected catalog", testCatalog1Example, testCatalog1);

		final CatalogModel testCatalog2 = catalogDao.findCatalogById(TEST_CATALOG_2);
		Assert.assertEquals("Unexpected catalog id", TEST_CATALOG_2, testCatalog2.getId());
		Assert.assertEquals("Unexpected catalog", testCatalog2Example, testCatalog2);
	}

	/**
	 * Corner case for {@link de.hybris.platform.catalog.daos.CatalogDao#findCatalogs(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindCatalogByIdNullId()
	{
		catalogDao.findCatalogById(null);
	}

	/**
	 * Corner case for {@link de.hybris.platform.catalog.daos.CatalogDao#findCatalogs(java.lang.String)}.
	 */
	@Test(expected = UnknownIdentifierException.class)
	public void testFindCatalogsNonExisting()
	{
		catalogDao.findCatalogById("wrong_ID");
	}

	/**
	 * Test method for {@link de.hybris.platform.catalog.daos.CatalogDao#findAllCatalogs()}.
	 */
	@Test
	public void testFindAllCatalogs()
	{
		final Collection<CatalogModel> allCatalogs = catalogDao.findAllCatalogs();
		Assert.assertTrue("all catalogs should contain testCatalog1", allCatalogs.contains(testCatalog1Example));
		Assert.assertTrue("all catalogs should contain testCatalog2", allCatalogs.contains(testCatalog2Example));
	}

	/**
	 * Test method for {@link de.hybris.platform.catalog.daos.CatalogDao#findDefaultCatalogs()}.
	 */
	@Test
	public void testFindDefaultCatalogs()
	{
		Collection<CatalogModel> defaultCatalogs = catalogDao.findDefaultCatalogs();
		final CatalogModel currentDefault = defaultCatalogs.iterator().next();
		Assert.assertEquals("Unexpected default catalogs size", 1, defaultCatalogs.size());
		Assert.assertTrue("unexpected default catalogs content", currentDefault.getId().equals(TEST_CATALOG_1));

		final CatalogModel newDefaultCatalog = modelService.create(CatalogModel.class);
		newDefaultCatalog.setId("new_Catalog");
		newDefaultCatalog.setName("new catalog");
		newDefaultCatalog.setDefaultCatalog(Boolean.TRUE);
		modelService.save(newDefaultCatalog);
		//now two default catalogs ?.. actually no!, saving new default catalog will reset the current default to non-default.
		defaultCatalogs = catalogDao.findDefaultCatalogs();
		Assert.assertEquals("Unexpected default catalogs size", 1, defaultCatalogs.size());

		final Iterator<CatalogModel> iterator = defaultCatalogs.iterator();
		Assert.assertEquals("Unexpected default catalog", "new_Catalog", iterator.next().getId());

		//tear down new model
		newDefaultCatalog.setDefaultCatalog(Boolean.FALSE);
		modelService.save(newDefaultCatalog);
		modelService.remove(newDefaultCatalog);
	}


}
