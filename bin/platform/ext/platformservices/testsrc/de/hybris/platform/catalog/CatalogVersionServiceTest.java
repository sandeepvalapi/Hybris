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
package de.hybris.platform.catalog;

import static com.google.common.collect.ImmutableList.of;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link CatalogVersionService}
 */
@IntegrationTest
public class CatalogVersionServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;

	@Resource
	private UnitDao unitDao;

	@Resource
	private CurrencyDao currencyDao;

	private final static String TEST_CATALOG_1 = "testCatalog1";
	private final static String TEST_CATALOG_2 = "testCatalog2";
	private final static String WINTER_VERSION = "Winter";
	private final static String SPRING_VERSION = "Spring";


	@Before
	public void setup() throws Exception
	{
		createCoreData();
		importCsv("/platformservices/test/catalog/testdata_catalogVersion.csv", "UTF-8");
		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_SET);
	}


	@Test
	public void testgetAllCatalogVersionsOfType()
	{
		final Collection<CatalogVersionModel> testcoll = catalogVersionService
				.getAllCatalogVersionsOfType(CatalogVersionModel.class);
		assertEquals(4, testcoll.size());
	}


	@Test
	public void testUserCanReadAndWriteWithNullValues()
	{
		//case 1
		try
		{
			catalogVersionService.canRead(new CatalogVersionModel(), null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 2
		try
		{
			catalogVersionService.canRead(null, new UserModel());
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 3
		try
		{
			catalogVersionService.canWrite(null, new UserModel());
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 4
		try
		{
			catalogVersionService.canWrite(new CatalogVersionModel(), null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 5
		try
		{
			catalogVersionService.getSessionCatalogVersionForCatalog(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 6
		try
		{
			catalogVersionService.addSessionCatalogVersion(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 7
		try
		{
			catalogVersionService.setSessionCatalogVersions(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 8
		try
		{
			catalogVersionService.setSessionCatalogVersion(null, SPRING_VERSION);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 9
		try
		{
			catalogVersionService.setSessionCatalogVersion(TEST_CATALOG_1, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 10
		try
		{
			catalogVersionService.getAllReadableCatalogVersions(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 11
		try
		{
			catalogVersionService.getAllWritableCatalogVersions(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

		//case 11
		try
		{
			catalogVersionService.getAllCatalogVersionsOfType(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

	}


	/**
	 * tests {@link CatalogVersionService#canRead(CatalogVersionModel, UserModel)} method using applied test data.
	 */
	@Test
	public void testUserCanReadCatalogVersion()
	{
		final UserModel user1 = userService.getUserForUID("testUser1");
		final UserModel user2 = userService.getUserForUID("testUser2");

		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);
		final CatalogVersionModel test2Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, WINTER_VERSION);


		//test canRead
		assertTrue("User should be allowed to read catalog version", catalogVersionService.canRead(test1Winter, user1));
		assertTrue("User should be allowed to read catalog version", catalogVersionService.canRead(test1Spring, user1));
		assertTrue("User should be allowed to read catalog version", catalogVersionService.canRead(test2Spring, user1));
		assertFalse("User should not be allowed to read catalog version", catalogVersionService.canRead(test2Winter, user1));
		assertTrue("Admin should be allowed to read catalog version",
				catalogVersionService.canRead(test2Spring, userService.getAdminUser()));

		//test canWrite
		assertTrue("User should be allowed to write to catalog version", catalogVersionService.canWrite(test2Winter, user2));
		assertTrue("User should be allowed to write to catalog version", catalogVersionService.canWrite(test1Spring, user2));
		assertFalse("User should not be allowed to write to catalog version", catalogVersionService.canWrite(test1Winter, user2));
		assertFalse("User should not be allowed to write to catalog version", catalogVersionService.canWrite(test2Spring, user2));
		assertTrue("Admin should be allowed to write to catalog version",
				catalogVersionService.canWrite(test1Spring, userService.getAdminUser()));
	}


	@Test
	public void testGetSessionCatalogVersionWithEmptyCatalog()
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("xxx");
		modelService.save(cat);
		assertNull(catalogVersionService.getSessionCatalogVersionForCatalog(cat.getId()));
	}

	/**
	 * tests {@link CatalogVersionService#addSessionCatalogVersion(CatalogVersionModel)}.
	 */
	@Test
	public void testAddSessionCatalogVersion()
	{
		//initially empty 
		assertEmptySessionCatalogVersions();

		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);
		final CatalogVersionModel test2Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, WINTER_VERSION);

		catalogVersionService.addSessionCatalogVersion(test1Spring);
		Collection<CatalogVersionModel> sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();
		assertEquals(1, sessionCatalogVersions.size());
		assertTrue(sessionCatalogVersions.contains(test1Spring));

		//take only session catalog version for catalog1
		assertEquals("Unexpected session catalog1 version", test1Spring,
				catalogVersionService.getSessionCatalogVersionForCatalog(TEST_CATALOG_1));

		//take only session catalog versions (!) for catalog1
		Collection<CatalogVersionModel> sessionCatalog1Versions = catalogVersionService
				.getSessionCatalogVersionsForCatalog(TEST_CATALOG_1);
		assertEquals(1, sessionCatalog1Versions.size());
		assertTrue(sessionCatalog1Versions.contains(test1Spring));
		assertUnmodifyableCollection(sessionCatalog1Versions);

		//now we add second catalog version
		catalogVersionService.addSessionCatalogVersion(test2Spring);
		sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();

		assertEquals(2, sessionCatalogVersions.size());
		assertTrue(sessionCatalogVersions.contains(test1Spring));
		assertTrue(sessionCatalogVersions.contains(test2Spring));
		assertUnmodifyableCollection(sessionCatalogVersions);

		//check for particular catalogs separately:
		sessionCatalog1Versions = catalogVersionService.getSessionCatalogVersionsForCatalog(TEST_CATALOG_1);
		assertEquals(1, sessionCatalog1Versions.size());
		assertTrue(sessionCatalog1Versions.contains(test1Spring));

		Collection<CatalogVersionModel> sessionCatalog2Versions = catalogVersionService
				.getSessionCatalogVersionsForCatalog(TEST_CATALOG_2);
		assertEquals(1, sessionCatalog2Versions.size());
		assertTrue(sessionCatalog2Versions.contains(test2Spring));
		assertUnmodifyableCollection(sessionCatalog2Versions);

		//now adding winter catalogs:
		catalogVersionService.addSessionCatalogVersion(test1Winter);
		catalogVersionService.addSessionCatalogVersion(test2Winter);

		//check all
		sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();
		//now we should have 4 of them:
		assertEquals(4, sessionCatalogVersions.size());
		assertTrue(sessionCatalogVersions.contains(test1Spring));
		assertTrue(sessionCatalogVersions.contains(test1Winter));
		assertTrue(sessionCatalogVersions.contains(test2Spring));
		assertTrue(sessionCatalogVersions.contains(test2Winter));

		//check separately per catalog:
		sessionCatalog1Versions = catalogVersionService.getSessionCatalogVersionsForCatalog(TEST_CATALOG_1);
		assertEquals(2, sessionCatalog1Versions.size());
		assertTrue(sessionCatalog1Versions.contains(test1Spring));
		assertTrue(sessionCatalog1Versions.contains(test1Winter));

		sessionCatalog2Versions = catalogVersionService.getSessionCatalogVersionsForCatalog(TEST_CATALOG_2);
		assertEquals(2, sessionCatalog2Versions.size());
		assertTrue(sessionCatalog2Versions.contains(test2Spring));
		assertTrue(sessionCatalog2Versions.contains(test2Winter));

		//check with the method that expects single result
		assertSingularCatalogVersion(TEST_CATALOG_1);
		assertSingularCatalogVersion(TEST_CATALOG_2);

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_LIST);
		assertEmptySessionCatalogVersions();
	}

	/**
	 * 
	 * tests {@link CatalogVersionService#setSessionCatalogVersions(Collection)}.
	 */
	@Test
	public void testSetSessionCatalogVersions()
	{
		assertEmptySessionCatalogVersions();

		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_SET);
		assertEmptySessionCatalogVersions();

		catalogVersionService.setSessionCatalogVersions(Collections.singleton(test1Winter));
		Collection<CatalogVersionModel> currentCV = catalogVersionService.getSessionCatalogVersions();
		assertUnmodifyableCollection(currentCV);

		assertTrue("unexpected session catalog versions content", currentCV.contains(test1Winter));
		assertEquals("unexpected session catalog versions size", 1, currentCV.size());

		catalogVersionService.setSessionCatalogVersions(Arrays.asList(test1Spring, test2Spring));

		currentCV = catalogVersionService.getSessionCatalogVersions();
		assertUnmodifyableCollection(currentCV);
		assertTrue("unexpected session catalog versions content", currentCV.contains(test1Spring));
		assertTrue("unexpected session catalog versions content", currentCV.contains(test2Spring));
		assertEquals("unexpected session catalog versions size", 2, currentCV.size());

		//two versions of one catalog
		catalogVersionService.setSessionCatalogVersions(Arrays.asList(test1Spring, test1Winter));
		assertSingularCatalogVersion(TEST_CATALOG_1);

		final Collection<CatalogVersionModel> sessionCatalogVersions = catalogVersionService.getSessionCatalogVersions();
		assertEquals(2, sessionCatalogVersions.size());
		assertTrue(sessionCatalogVersions.contains(test1Spring));
		assertTrue(sessionCatalogVersions.contains(test1Winter));

		try
		{
			catalogVersionService.setSessionCatalogVersions(null);
			fail("IllegalArgumentExceptionExcpected when setting null set as session catalog versions");
		}
		catch (final IllegalArgumentException e)
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("IllegalArgumentExceptionExcpected when setting null set as session catalog versions");
		}
		catalogVersionService.setSessionCatalogVersions(new HashSet<CatalogVersionModel>());
		assertEmptySessionCatalogVersions();

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_SET);
		assertEmptySessionCatalogVersions();
	}

	/**
	 * tests {@link CatalogVersionService#setSessionCatalogVersion(String, String)}.
	 */
	@Test
	public void testSetSessionCatalogVersion()
	{
		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);

		assertEmptySessionCatalogVersions();
		Collection<CatalogVersionModel> currentCV = catalogVersionService.getSessionCatalogVersions();
		assertTrue(currentCV.isEmpty());
		catalogVersionService.setSessionCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);

		currentCV = catalogVersionService.getSessionCatalogVersions();
		assertUnmodifyableCollection(currentCV);
		assertTrue("unexpected session catalog versions content", currentCV.contains(test1Winter));
		assertEquals("unexpected session catalog versions size", 1, currentCV.size());

		catalogVersionService.setSessionCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);

		currentCV = catalogVersionService.getSessionCatalogVersions();
		assertUnmodifyableCollection(currentCV);
		assertTrue("unexpected session catalog versions content", currentCV.contains(test2Spring));
		assertEquals("unexpected session catalog versions size", 1, currentCV.size());

	}

	/**
	 * Tests {@link CatalogVersionService#getAllCatalogVersions()}
	 */
	@Test
	public void testGetAllCatalogVersions()
	{
		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);
		final CatalogVersionModel test2Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, WINTER_VERSION);

		final Collection<CatalogVersionModel> allCatalogVersions = catalogVersionService.getAllCatalogVersions();
		assertEquals("Unexpected allCatalogVersions collection size", 4, allCatalogVersions.size());
		assertTrue("Unexpected allCatalogVersions content", allCatalogVersions.contains(test1Spring));
		assertTrue("Unexpected allCatalogVersions content", allCatalogVersions.contains(test1Winter));
		assertTrue("Unexpected allCatalogVersions content", allCatalogVersions.contains(test2Spring));
		assertTrue("Unexpected allCatalogVersions content", allCatalogVersions.contains(test2Winter));
	}

	@Test
	public void testGetReadableCatalogVersions()
	{
		final UserModel user2 = userService.getUserForUID("testUser2");
		final UserModel user3 = userService.getUserForUID("testUser3");

		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, WINTER_VERSION);

		assertTrue("readable catalog version should be empty",
				catalogVersionService.getAllReadableCatalogVersions(user3).isEmpty());
		final Collection<CatalogVersionModel> user2ReadableCV = catalogVersionService.getAllReadableCatalogVersions(user2);
		assertEquals("unexpected readable catalog version collection size", 2, user2ReadableCV.size());
		assertTrue("unexpected readable catalog version collection content", user2ReadableCV.contains(test2Winter));
		assertTrue("unexpected readable catalog version collection content", user2ReadableCV.contains(test1Spring));
	}

	@Test
	public void testGetWritableCatalogVersions()
	{
		final UserModel user1 = userService.getUserForUID("testUser1");
		final UserModel user3 = userService.getUserForUID("testUser3");

		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final CatalogVersionModel test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		final CatalogVersionModel test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);

		assertTrue("readable catalog version should be empty",
				catalogVersionService.getAllWritableCatalogVersions(user3).isEmpty());

		final Collection<CatalogVersionModel> user1WritableCV = catalogVersionService.getAllWritableCatalogVersions(user1);
		assertEquals("unexpected readable catalog version collection size", 3, user1WritableCV.size());
		assertTrue("unexpected readable catalog version collection content", user1WritableCV.contains(test1Winter));
		assertTrue("unexpected readable catalog version collection content", user1WritableCV.contains(test1Spring));
		assertTrue("unexpected readable catalog version collection content", user1WritableCV.contains(test2Spring));
	}


	@Test
	public void testSetAndAddSessionCVs()
	{
		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_LIST);
		catalogVersionService.addSessionCatalogVersion(test1Winter);
		final Collection<CatalogVersionModel> cvs = catalogVersionService.getSessionCatalogVersions();
		assertEquals(1, cvs.size());
		assertTrue(cvs.contains(test1Winter));
	}

	private PriceRowModel createPriceRow(final ProductModel product, final CatalogVersionModel cvm, final UserModel user)
	{
		final UnitModel unit = unitDao.findAllUnits().iterator().next();
		final CurrencyModel currency = currencyDao.findBaseCurrencies().iterator().next();

		final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setUnit(unit);
		priceRow.setCurrency(currency);
		priceRow.setCatalogVersion(cvm);
		priceRow.setProduct(product);
		priceRow.setUser(user);
		priceRow.setPrice(5.0);
		priceRow.setUnitFactor(1);

		return priceRow;
	}

	@Test
	public void combinedUniqueKeysShouldBeReportedAsDuplicates()
	{
		// given
		final ProductModel prod1 = modelService.create(ProductModel.class);
		prod1.setCode("P-001");
		final CatalogVersionModel test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("user1");
		modelService.saveAll();

		final PriceRowModel priceRow1 = createPriceRow(prod1, test1Winter, user);
		modelService.save(priceRow1);
		final PriceRowModel priceRow2 = createPriceRow(prod1, test1Winter, user);
		modelService.save(priceRow2);

		// when
		final Collection<DuplicatedItemIdentifier> duplicatedItems = catalogVersionService.findDuplicatedIds(test1Winter);

		// then
		assertThat(duplicatedItems).hasSize(1);
		final DuplicatedItemIdentifier duplicateInfo = duplicatedItems.iterator().next();
		assertThat(duplicateInfo.getCount()).isEqualTo(2);
	}

	@Test
	public void shouldSilentlyRemoveDeletedCatalogVersionsFromSession()
	{
		// given
		final CatalogModel catalog = createCatalog();
		final CatalogVersionModel catalogVersion1 = createCatalogVersion(catalog);
		final CatalogVersionModel catalogVersion2 = createCatalogVersion(catalog);
		modelService.saveAll(catalog, catalogVersion1, catalogVersion2);

		// when
		catalogVersionService.setSessionCatalogVersions(of(catalogVersion1, catalogVersion2));

		// then
		assertThat(catalogVersionService.getSessionCatalogVersions()).hasSize(2);

		// and when
		modelService.remove(catalogVersion2);

		try
		{
			// then
			assertThat(catalogVersionService.getSessionCatalogVersions()).hasSize(1);

			final Collection catalogVersions = (Collection) sessionService.getAllAttributes().get("catalogversions");
			assertThat(catalogVersions).hasSize(1);
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			fail("Deleted catalog version should be silently removed from session");
		}
	}

	private CatalogModel createCatalog()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());
		return catalog;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		catalogVersion.setCatalog(catalog);
		return catalogVersion;
	}


	private void assertUnmodifyableCollection(final Collection collection)
	{
		try
		{
			collection.add(new Object());
			fail("collection should be unmodifyable");
		}
		catch (final UnsupportedOperationException e)
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("collection should be unmodifyable");
		}
	}

	private void assertEmptySessionCatalogVersions()
	{
		//the service should return an empty collection
		assertTrue(catalogVersionService.getSessionCatalogVersions().isEmpty());

		//however there should be a dummy object in the session's set
		final Object actualSessionObject = sessionService.getAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS);
		assertNotNull(actualSessionObject);
		assertTrue(actualSessionObject instanceof Collection);
		final Collection<CatalogVersionModel> sessionCollection = (Collection<CatalogVersionModel>) actualSessionObject;
		assertFalse(sessionCollection.isEmpty());
		assertEquals(CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY, sessionCollection.iterator().next());
	}

	private void assertSingularCatalogVersion(final String catalogId)
	{
		try
		{
			catalogVersionService.getSessionCatalogVersionForCatalog(catalogId);
			fail("AmbiguousIdentifierException was expected");
		}
		catch (final AmbiguousIdentifierException e)
		{
			//ok
		}
		catch (final Exception e)
		{
			fail("AmbiguousIdentifierException was expected");
		}
	}
}
