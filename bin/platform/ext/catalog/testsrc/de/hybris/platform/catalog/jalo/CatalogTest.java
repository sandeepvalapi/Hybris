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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for extension Catalog
 */
@IntegrationTest
public class CatalogTest extends HybrisJUnit4TransactionalTest
{
	private CatalogManager cManager;
	private CategoryManager categorymanager;
	private UserManager usermanager;
	private Catalog catalog;
	private CatalogVersion version;
	private Company supplier, buyer;
	private Address supplierAddr, buyerAddr;
	private Agreement agreement;
	private Keyword keyword1, keyword2, keyword3;
	private Category root, cat1, cat2, cat21, cat22;
	private Language langDE, langEN;
	private Country countryDE, countryAU, countryCH;
	//	private Currency euro;
	private Media media1, media2, media3;
	private Product product1, product2, product3, product4, product5, product6;

	protected static final String DE = "DE"; //NOPMD
	protected static final String AU = "AU"; //NOPMD
	protected static final String CH = "CH"; //NOPMD

	//catalog
	private static final String CATALOG_ID = "hybris";
	private static final String CATALOG_NAME = "Test Catalog";
	//catalog - supplier
	private static final String CATALOG_SUPPLIER_ID = "SUPPLIER_ID";
	//	private static final String CATALOG_SUPPLIER_ID_DUNS = "SUPPLIER_DUNS";
	//	private static final String CATALOG_SUPPLIER_ID_ILN = "SUPPLIER_ILN";
	//	private static final String CATALOG_SUPPLIER_ID_BUYERSPECIFIC = "SUPPLIER_BUYERSPECIFIC";
	//	private static final String CATALOG_SUPPLIER_ID_SUPPLIERSPECIFIC = "SUPPLIER_SUPPLIERSPECIFIC";
	//	private static final String CATALOG_SUPPLIER_NAME = "SUPPLIER_NAME";
	private static final String CATALOG_SUPPLIER_ADDRESS_NAME = "SUPPLIER_ADDRESS_NAME";
	private static final String CATALOG_SUPPLIER_ADDRESS_CONTACT = "SUPPLIER_ADDRESS_CONTACT";
	private static final String CATALOG_SUPPLIER_ADDRESS_STREET = "SUPPLIER_ADDRESS_STREET";
	private static final String CATALOG_SUPPLIER_ADDRESS_ZIP = "SUPPLIER_ADDRESS_ZIP";
	private static final String CATALOG_SUPPLIER_ADDRESS_CITY = "SUPPLIER_ADDRESS_CITY";
	private static final String CATALOG_SUPPLIER_ADDRESS_COUNTRY = DE;

	//catalogversion
	private static final String CATALOGVERSION_VERSION_1 = "1.0";
	//	private static final String CATALOGVERSION_VERSION_2 = "2.0";
	private static final String CATALOGVERSION_GENERATORINFO = "hybris EBP";
	private static final Date CATALOGVERSION_GENERATIONDATE = new Date(System.currentTimeMillis());
	protected static final String CATALOGVERSION_LANGUAGE_DE = "de";
	protected static final String CATALOGVERSION_LANGUAGE_EN = "en";
	private static final Collection CATALOGVERSION_TERRITORIES = Arrays.asList(new String[]
	{ DE, AU, CH });
	//	private static final String CATALOGVERSION_CURRENCY = "EUR";
	private static final Boolean CATALOGVERSION_INCLFREIGHT = Boolean.TRUE;
	private static final Boolean CATALOGVERSION_INCLASSURANCE = Boolean.FALSE;
	private static final Boolean CATALOGVERSION_INCLDUTY = Boolean.TRUE;
	private static final Boolean CATALOGVERSION_INCLPACKING = Boolean.TRUE;
	private static final String CATALOG_BUYER_ID = "BUYER_NAME";
	private static final String CATALOG_BUYER_NAME = "BUYER_NAME";
	private static final String CATALOG_BUYER_ADDRESS_NAME = "BUYER_ADDRESS_NAME";
	private static final String CATALOG_BUYER_ADDRESS_CONTACT = "BUYER_ADDRESS_CONTACT";
	private static final String CATALOG_BUYER_ADDRESS_STREET = "BUYER_ADDRESS_STREET";
	private static final String CATALOG_BUYER_ADDRESS_ZIP = "BUYER_ADDRESS_ZIP";
	private static final String CATALOG_BUYER_ADDRESS_CITY = "BUYER_ADDRESS_CITY";
	private static final String CATALOG_BUYER_ADDRESS_COUNTRY = DE;
	private static final String CATALOGVERSION_GROUPSYSTEM_NAME = "GROUP_SYSTEM_NAME_";
	private static final String CATALOGVERSION_GROUPSYSTEM_DESCRIPTION = "GROUP_SYSTEM_DESCRIPTION_";
	private static final String CATALOGVERSION_GROUPSYSTEM_ID = "GROUP_SYSTEM_ID";

	// agreements
	private static final String AGREEMENT = "AGREEMENT";
	private static final long OFFSET = 2678400000l;
	private static final long START = System.currentTimeMillis();
	private static final long END = START + OFFSET;
	private static final Date AGREEMENT_STARTDATE = new Date(START);
	private static final Date AGREEMENT_ENDDATE = new Date(END);

	// categories
	private static final String CATEGORY_ROOT_ID = "0";
	private static final String CATEGORY_ROOT_NAME = "ROOT";
	private static final String CATEGORY_ROOT_DESCRIPTION = "ROOT_DESCRIPTION";
	private static final Integer CATEGORY_ROOT_ORDER = Integer.valueOf(1);
	private static final String CATEGORY_1_ID = "1";
	private static final String CATEGORY_1_NAME = "CATEGORY_1";
	private static final String CATEGORY_1_DESCRIPTION = "CATEGORY_1_DESCRIPTION";
	private static final Integer CATEGORY_1_ORDER = Integer.valueOf(1);
	private static final String CATEGORY_2_ID = "2";
	private static final String CATEGORY_2_NAME = "CATEGORY_2";
	private static final String CATEGORY_2_DESCRIPTION = "CATEGORY_2_DESCRIPTION";
	private static final Integer CATEGORY_2_ORDER = Integer.valueOf(2);
	private static final String CATEGORY_2_1_ID = "2_1";
	private static final String CATEGORY_2_1_NAME = "CATEGORY_2_1";
	private static final String CATEGORY_2_1_DESCRIPTION = "CATEGORY_2_1_DESCRIPTION";
	private static final Integer CATEGORY_2_1_ORDER = Integer.valueOf(1);
	private static final String CATEGORY_2_2_ID = "2_2";
	private static final String CATEGORY_2_2_NAME = "CATEGORY_2_2";
	private static final String CATEGORY_2_2_DESCRIPTION = "CATEGORY_2_2_DESCRIPTION";
	private static final Integer CATEGORY_2_2_ORDER = Integer.valueOf(2);
	// keywords
	private static final String KEYWORD_1 = "KEYWORD_1";
	private static final String KEYWORD_2 = "KEYWORD_2";
	private static final String KEYWORD_3 = "KEYWORD_3";
	private static final Collection ALL_KEYWORDS = Arrays.asList(new String[]
	{ KEYWORD_1, KEYWORD_2, KEYWORD_3 });

	private static final String PRODUCT_01_ID = "ARTICLE_01_ID";
	private static final String PRODUCT_01_NAME = "ARTICLE_01_NAME";
	private static final String PRODUCT_01_DESCRIPTION = "ARTICLE_01_DESCRIPTION";

	private static final String PRODUCT_02_ID = "ARTICLE_02_ID";
	private static final String PRODUCT_02_NAME = "ARTICLE_02_NAME";
	private static final String PRODUCT_02_DESCRIPTION = "ARTICLE_02_DESCRIPTION";

	private static final String PRODUCT_03_ID = "ARTICLE_03_ID";
	private static final String PRODUCT_03_NAME = "ARTICLE_03_NAME";
	private static final String PRODUCT_03_DESCRIPTION = "ARTICLE_03_DESCRIPTION";

	private static final String PRODUCT_04_ID = "ARTICLE_04_ID";
	private static final String PRODUCT_04_NAME = "ARTICLE_04_NAME";
	private static final String PRODUCT_04_DESCRIPTION = "ARTICLE_04_DESCRIPTION";

	private static final String PRODUCT_05_ID = "ARTICLE_05_ID";
	private static final String PRODUCT_05_NAME = "ARTICLE_05_NAME";
	private static final String PRODUCT_05_DESCRIPTION = "ARTICLE_05_DESCRIPTION";

	private static final String PRODUCT_06_ID = "ARTICLE_06_ID";
	private static final String PRODUCT_06_NAME = "ARTICLE_06_NAME";
	private static final String PRODUCT_06_DESCRIPTION = "ARTICLE_06_DESCRIPTION";

	private static final String MEDIA_01_ID = "MEDIA_01_ID";
	private static final String MEDIA_01_URL = "MEDIA_01_URL";

	private static final String MEDIA_02_ID = "MEDIA_02_ID";
	private static final String MEDIA_02_URL = "MEDIA_02_URL";

	private static final String MEDIA_03_ID = "MEDIA_03_ID";
	private static final String MEDIA_03_URL = "MEDIA_03_URL";

	//	private static final Collection ALL_CATEGORIES = Arrays.asList( new String[]
	//	{ CATEGORY_ROOT_ID, CATEGORY_1_ID, CATEGORY_2_ID, CATEGORY_2_1_ID, CATEGORY_2_2_ID } );
	private static final Collection CATEGORY_2_PRODUCTS = Arrays.asList(new String[]
	{ PRODUCT_02_ID, PRODUCT_03_ID, PRODUCT_04_ID });
	private static final Collection CATEGORY_2_1_PRODUCTS = Arrays.asList(new String[]
	{ PRODUCT_03_ID, PRODUCT_04_ID });
	private static final Collection CATEGORY_2_2_PRODUCTS = Arrays.asList(new String[]
	{ PRODUCT_05_ID, PRODUCT_06_ID });
	private static final Collection ALL_PRODUCTS = Arrays.asList(new String[]
	{ PRODUCT_01_ID, PRODUCT_02_ID, PRODUCT_03_ID, PRODUCT_04_ID, PRODUCT_05_ID, PRODUCT_06_ID });
	private static final Collection ALL_MEDIAS = Arrays.asList(new String[]
	{ MEDIA_01_ID, MEDIA_02_ID, MEDIA_03_ID });

	@Before
	public void setUp() throws Exception
	{
		cManager = (CatalogManager) jaloSession.getExtensionManager().getExtension(CatalogConstants.EXTENSIONNAME);
		categorymanager = CategoryManager.getInstance();
		usermanager = UserManager.getInstance();

		langDE = getOrCreateLanguage(CATALOGVERSION_LANGUAGE_DE);
		langEN = getOrCreateLanguage(CATALOGVERSION_LANGUAGE_EN);

		countryDE = jaloSession.getC2LManager().createCountry(DE);
		assertNotNull(countryDE);

		countryAU = jaloSession.getC2LManager().createCountry(AU);
		assertNotNull(countryAU);

		countryCH = jaloSession.getC2LManager().createCountry(CH);
		assertNotNull(countryCH);
		createCatalog();
	}

	protected void createCatalog() throws Exception
	{
		//assertTrue( cManager.isTestOK() );
		//catalog
		supplier = cManager.createCompany(CATALOG_SUPPLIER_ID);
		assertNotNull(supplier);
		supplierAddr = jaloSession.getUserManager().createAddress(supplier);
		assertNotNull(supplierAddr);
		supplierAddr.setAttribute(Address.LASTNAME, CATALOG_SUPPLIER_ADDRESS_CONTACT);
		supplierAddr.setAttribute(Address.COMPANY, CATALOG_SUPPLIER_ADDRESS_NAME);
		supplierAddr.setAttribute(Address.STREETNAME, CATALOG_SUPPLIER_ADDRESS_STREET);
		supplierAddr.setAttribute(Address.POSTALCODE, CATALOG_SUPPLIER_ADDRESS_ZIP);
		supplierAddr.setAttribute(Address.TOWN, CATALOG_SUPPLIER_ADDRESS_CITY);
		supplierAddr.setCountry(countryDE);
		supplier.setAddresses(Collections.singletonList(supplierAddr));
		catalog = cManager.createCatalog(CATALOG_ID, CATALOG_NAME, supplier);

		buyer = cManager.createCompany(CATALOG_BUYER_ID);
		assertNotNull(buyer);
		buyerAddr = jaloSession.getUserManager().createAddress(buyer);
		assertNotNull(buyerAddr);
		buyerAddr.setAttribute(Address.LASTNAME, CATALOG_BUYER_ADDRESS_CONTACT);
		buyerAddr.setAttribute(Address.COMPANY, CATALOG_BUYER_ADDRESS_NAME);
		buyerAddr.setAttribute(Address.STREETNAME, CATALOG_BUYER_ADDRESS_STREET);
		buyerAddr.setAttribute(Address.POSTALCODE, CATALOG_BUYER_ADDRESS_ZIP);
		buyerAddr.setAttribute(Address.TOWN, CATALOG_BUYER_ADDRESS_CITY);
		buyerAddr.setCountry(countryDE);
		buyer.setAddresses(Collections.singletonList(buyerAddr));
		catalog.setBuyer(buyer);

		assertNotNull(catalog);

		// catalogversion
		version = cManager.createCatalogVersion(catalog, CATALOGVERSION_VERSION_1, langDE);
		assertNotNull(version);
		version.setCategorySystemID(CATALOGVERSION_GROUPSYSTEM_ID);
		version.setCategorySystemName(CATALOGVERSION_GROUPSYSTEM_NAME);
		version.setCategorySystemDescription(CATALOGVERSION_GROUPSYSTEM_DESCRIPTION);
		// version.setDefaultCurrency(euro);
		version.setGenerationDate(CATALOGVERSION_GENERATIONDATE);
		version.setGeneratorInfo(CATALOGVERSION_GENERATORINFO);
		version.setInclAssurance(CATALOGVERSION_INCLASSURANCE);
		version.setInclDuty(CATALOGVERSION_INCLDUTY);
		version.setInclFreight(CATALOGVERSION_INCLFREIGHT);
		version.setInclPacking(CATALOGVERSION_INCLPACKING);
		version.setTerritories(Arrays.asList(new Country[]
		{ countryDE, countryAU, countryCH }));

		// agreement
		agreement = cManager.createAgreement(version, AGREEMENT, AGREEMENT_STARTDATE, AGREEMENT_ENDDATE);
		assertNotNull(agreement);
		// keywords
		keyword1 = cManager.createKeyword(version, KEYWORD_1, langDE);
		assertNotNull(keyword1);
		keyword2 = cManager.createKeyword(version, KEYWORD_2, langDE);
		assertNotNull(keyword2);
		keyword3 = cManager.createKeyword(version, KEYWORD_3, langDE);
		assertNotNull(keyword3);
		// categories
		root = cManager.createCatalogCategory(version, CATEGORY_ROOT_ID, CATEGORY_ROOT_NAME, CATEGORY_ROOT_DESCRIPTION,
				CATEGORY_ROOT_ORDER, Collections.singletonList(keyword1), null);
		assertNotNull(root);
		cat1 = cManager.createCatalogCategory(version, CATEGORY_1_ID, CATEGORY_1_NAME, CATEGORY_1_DESCRIPTION, CATEGORY_1_ORDER,
				Collections.singletonList(keyword2), root);
		assertNotNull(cat1);
		cat2 = cManager.createCatalogCategory(version, CATEGORY_2_ID, CATEGORY_2_NAME, CATEGORY_2_DESCRIPTION, CATEGORY_2_ORDER,
				Collections.singletonList(keyword3), root);
		assertNotNull(cat2);
		cat21 = cManager.createCatalogCategory(version, CATEGORY_2_1_ID, CATEGORY_2_1_NAME, CATEGORY_2_1_DESCRIPTION,
				CATEGORY_2_1_ORDER, Collections.singletonList(keyword3), cat2);
		assertNotNull(cat21);
		cat22 = cManager.createCatalogCategory(version, CATEGORY_2_2_ID, CATEGORY_2_2_NAME, CATEGORY_2_2_DESCRIPTION,
				CATEGORY_2_2_ORDER, Collections.singletonList(keyword3), cat2);
		assertNotNull(cat22);
		// products		
		product1 = createProduct(PRODUCT_01_ID, PRODUCT_01_NAME, PRODUCT_01_DESCRIPTION, version);
		assertNotNull(product1);
		product2 = createProduct(PRODUCT_02_ID, PRODUCT_02_NAME, PRODUCT_02_DESCRIPTION, version);
		assertNotNull(product2);
		product3 = createProduct(PRODUCT_03_ID, PRODUCT_03_NAME, PRODUCT_03_DESCRIPTION, version);
		assertNotNull(product3);
		product4 = createProduct(PRODUCT_04_ID, PRODUCT_04_NAME, PRODUCT_04_DESCRIPTION, version);
		assertNotNull(product4);
		product5 = createProduct(PRODUCT_05_ID, PRODUCT_05_NAME, PRODUCT_05_DESCRIPTION, version);
		assertNotNull(product5);
		product6 = createProduct(PRODUCT_06_ID, PRODUCT_06_NAME, PRODUCT_06_DESCRIPTION, version);
		assertNotNull(product6);
		cat1.setProducts(Collections.singletonList(product1));
		cat2.setProducts(Arrays.asList(new Product[]
		{ product2, product3, product4 }));
		cat21.setProducts(Arrays.asList(new Product[]
		{ product3, product4 }));
		cat22.setProducts(Arrays.asList(new Product[]
		{ product5, product6 }));

		media1 = createMedia(MEDIA_01_ID, MEDIA_01_URL, version);
		assertNotNull(media1);
		media2 = createMedia(MEDIA_02_ID, MEDIA_02_URL, version);
		assertNotNull(media2);
		media3 = createMedia(MEDIA_03_ID, MEDIA_03_URL, version);
		assertNotNull(media3);
		jaloSession.getLinkManager().createLink("media_1", product1, media1, 0, 0);
		jaloSession.getLinkManager().createLink("media_2", product2, media2, 0, 0);
		cat1.setMedias(Collections.singletonList(media3));
	}

	protected Product createProduct(final String id, final String name, final String desc, final CatalogVersion catVersion)
			throws Exception
	{
		final ComposedType type = jaloSession.getTypeManager().getComposedType(Product.class);
		final Map values = new HashMap();
		values.put(Product.CODE, id);
		values.put(Product.NAME, name);
		values.put(Product.DESCRIPTION, desc);
		values.put(CatalogConstants.Attributes.Product.CATALOGVERSION, catVersion);
		return (Product) type.newInstance(values);
	}

	protected Media createMedia(final String id, final String file, final CatalogVersion catVersion) throws Exception
	{
		final ComposedType type = jaloSession.getTypeManager().getComposedType(Media.class);
		final Map values = new HashMap();
		values.put(Media.CODE, id);
		values.put(Media.URL, file);
		values.put(CatalogConstants.Attributes.Media.CATALOGVERSION, catVersion);
		return (Media) type.newInstance(values);
	}

	protected void checkCatalogVersion(final CatalogVersion catalogVersion, final String versionStr)
	{
		assertNotNull("CatalogVersion is null!", catalogVersion);
		assertEquals(catalog, catalogVersion.getCatalog());
		assertEquals(versionStr, catalogVersion.getVersion());
		assertTrue("Not one import language!", catalogVersion.getLanguages().size() == 1);
		assertEquals(Collections.singletonList(langDE), catalogVersion.getLanguages());
		assertEquals(Collections.singletonList(agreement), catalogVersion.getAgreements());

		//-territories
		final Collection territories = catalogVersion.getTerritories();
		assertTrue("Three territories!", territories.size() == 3);
		for (final Iterator iter = territories.iterator(); iter.hasNext();)
		{
			final Country country = (Country) iter.next();
			final String iso = country.getIsoCode();
			assertTrue("Territory " + iso + " exists!", CATALOGVERSION_TERRITORIES.contains(iso));
		}

		//-priceflags
		assertEquals(CATALOGVERSION_INCLASSURANCE, catalogVersion.isInclAssurance());
		assertEquals(CATALOGVERSION_INCLDUTY, catalogVersion.isInclDuty());
		assertEquals(CATALOGVERSION_INCLFREIGHT, catalogVersion.isInclFreight());
		assertEquals(CATALOGVERSION_INCLPACKING, catalogVersion.isInclPacking());

		//-catalog group system
		assertEquals(CATALOGVERSION_GROUPSYSTEM_ID, catalogVersion.getCategorySystemID());
		assertEquals(CATALOGVERSION_GROUPSYSTEM_NAME, catalogVersion.getCategorySystemName());
		assertEquals(CATALOGVERSION_GROUPSYSTEM_DESCRIPTION, catalogVersion.getCategorySystemDescription());

		// NOTE: returns all root categories
		assertEquals(1, version.getRootCategoriesCount());
		final Category rootCat = version.getRootCategories().iterator().next();
		// subcategories
		assertEquals(2, rootCat.getCategories().size());
		for (final Iterator iter = rootCat.getCategories().iterator(); iter.hasNext();)
		{
			final Category cat = (Category) iter.next();
			if (cat.getCode().equals(CATEGORY_1_ID))
			{
				assertEquals(0, cat.getSubcategories().size());
				assertTrue("One product?", cat.getProducts().size() == 1);
				final Product product01 = cat.getProducts().iterator().next();
				assertEquals(product1, product01);
				assertEquals(PRODUCT_01_ID, product01.getCode());
			}
			else if (cat.getCode().equals(CATEGORY_2_ID))
			{
				assertTrue("Three products?", cat.getProducts().size() == 3);
				for (final Iterator it = cat.getProducts().iterator(); it.hasNext();)
				{
					final String code = ((Product) it.next()).getCode();
					assertTrue("Product " + code + " exists!", CATEGORY_2_PRODUCTS.contains(code));
				}
				assertEquals(2, cat.getSubcategories().size());
				for (final Iterator it = cat.getSubcategories().iterator(); it.hasNext();)
				{
					final Category subCat = (Category) it.next();
					if (subCat.getCode().equals(CATEGORY_2_1_ID))
					{
						assertTrue("Two products?", subCat.getProducts().size() == 2);
						for (final Iterator it2 = subCat.getProducts().iterator(); it2.hasNext();)
						{
							final String code = ((Product) it2.next()).getCode();
							assertTrue("Product " + code + " exists!", CATEGORY_2_1_PRODUCTS.contains(code));
						}
					}
					else if (subCat.getCode().equals(CATEGORY_2_2_ID))
					{
						assertTrue("Two products?", subCat.getProducts().size() == 2);
						for (final Iterator it2 = subCat.getProducts().iterator(); it2.hasNext();)
						{
							final String code = ((Product) it2.next()).getCode();
							assertTrue("Product " + code + " exists!", CATEGORY_2_2_PRODUCTS.contains(code));
						}
					}
				}
			}
		}
	}

	@Test
	public void testOrderOverride()
	{

		final String SRC = "relation.CategoryCategoryRelation.source.ordered";
		final String TGT = "relation.CategoryCategoryRelation.target.ordered";
		final String MARK_MODIFIED = "relation.CategoryCategoryRelation.markmodified";

		final String srcBefore = Config.getParameter(SRC);
		final String tgtBefore = Config.getParameter(TGT);
		final String markBefore = Config.getParameter(MARK_MODIFIED);

		try
		{
			Utilities.clearRelationOrderingOverrideCache();
			Config.setParameter(SRC, "true");
			Config.setParameter(TGT, "true");

			boolean cat2cat_src = Config.getBoolean(SRC, true);
			assertTrue("Default should be true!", cat2cat_src);
			boolean cat2cat_tgt = Config.getBoolean(TGT, true);
			assertTrue("Default should be true!", cat2cat_tgt);

			boolean cat2cat_src_result = Utilities.getRelationOrderingOverride(SRC, true);

			assertEquals(cat2cat_src, cat2cat_src_result);

			boolean cat2cat_tgt_result = Utilities.getRelationOrderingOverride(SRC, true);
			assertEquals(cat2cat_tgt, cat2cat_tgt_result);

			Utilities.clearRelationOrderingOverrideCache();


			Config.setParameter(SRC, "false");
			cat2cat_src = Config.getBoolean(SRC, true);
			Assert.assertFalse("Should now be false!", cat2cat_src);

			Config.setParameter(TGT, "false");
			cat2cat_tgt = Config.getBoolean(TGT, true);
			Assert.assertFalse("Should now be false!", cat2cat_tgt);

			cat2cat_src_result = Utilities.getRelationOrderingOverride(SRC, true);
			assertEquals(cat2cat_src, cat2cat_src_result);

			cat2cat_tgt_result = Utilities.getRelationOrderingOverride(SRC, true);
			assertEquals(cat2cat_tgt, cat2cat_tgt_result);

			Utilities.clearMarkModifiedOverrideCache();
			Config.setParameter(MARK_MODIFIED, "true");
			boolean markModified = Config.getBoolean(MARK_MODIFIED, true);
			assertTrue("Default is true", markModified);

			boolean markModified_result = Utilities.getMarkModifiedOverride(MARK_MODIFIED);
			assertTrue("Should also be true", markModified_result);

			Config.setParameter(MARK_MODIFIED, "false");
			markModified = Config.getBoolean(MARK_MODIFIED, true);
			Assert.assertFalse("Now set to false", markModified);

			markModified_result = Utilities.getMarkModifiedOverride(MARK_MODIFIED);
			assertEquals(markModified, !markModified_result); //as it is still cached
			Utilities.clearMarkModifiedOverrideCache();

			markModified_result = Utilities.getMarkModifiedOverride(MARK_MODIFIED);
			assertEquals(markModified, markModified_result); //as cache got cleared
		}
		finally
		{
			revertConfigIfNeeded(SRC, srcBefore);
			revertConfigIfNeeded(TGT, tgtBefore);
			revertConfigIfNeeded(MARK_MODIFIED, markBefore);

			Utilities.clearRelationOrderingOverrideCache();
			Utilities.clearMarkModifiedOverrideCache();
		}

	}

	private void revertConfigIfNeeded(final String key, final String before)
	{
		if (StringUtils.isNotEmpty(before))
		{
			Config.setParameter(key, before);
		}
	}

	@Test
	public void testCatalog() throws JaloSecurityException
	{
		//assertTrue( cManager.isTestOK() );
		assertNotNull("Catalog is null!", catalog);
		assertEquals(CATALOG_ID, catalog.getId());
		assertEquals(CATALOG_NAME, catalog.getName());

		//supplier
		Company company = catalog.getSupplier();
		assertNotNull("Supplier is null!", company);
		assertEquals(supplier, company);
		assertEquals(CATALOG_SUPPLIER_ID, company.getUID());
		Address address = company.getAddresses().iterator().next();
		assertNotNull(address);
		assertEquals(CATALOG_SUPPLIER_ADDRESS_CITY, address.getAttribute(Address.TOWN));
		assertEquals(CATALOG_SUPPLIER_ADDRESS_CONTACT, address.getAttribute(Address.LASTNAME));
		assertEquals(CATALOG_SUPPLIER_ADDRESS_NAME, address.getAttribute(Address.COMPANY));
		assertEquals(CATALOG_SUPPLIER_ADDRESS_STREET, address.getAttribute(Address.STREETNAME));
		assertEquals(CATALOG_SUPPLIER_ADDRESS_ZIP, address.getAttribute(Address.POSTALCODE));
		assertNotNull("Country is null!", address.getCountry());
		assertEquals(CATALOG_SUPPLIER_ADDRESS_COUNTRY, address.getCountry().getIsoCode());

		//buyer
		company = catalog.getBuyer();
		assertNotNull("Buyer is null!", company);
		assertEquals(buyer, company);
		assertEquals(CATALOG_BUYER_NAME, company.getUID());
		address = company.getAddresses().iterator().next();
		assertNotNull("Buyer address is null!", address);
		assertEquals(CATALOG_BUYER_ADDRESS_CITY, address.getAttribute(Address.TOWN));
		assertEquals(CATALOG_BUYER_ADDRESS_CONTACT, address.getAttribute(Address.LASTNAME));
		assertEquals(CATALOG_BUYER_ADDRESS_NAME, address.getAttribute(Address.COMPANY));
		assertEquals(CATALOG_BUYER_ADDRESS_STREET, address.getAttribute(Address.STREETNAME));
		assertEquals(CATALOG_BUYER_ADDRESS_ZIP, address.getAttribute(Address.POSTALCODE));
		assertNotNull("Country is null!", address.getCountry());
		assertEquals(CATALOG_BUYER_ADDRESS_COUNTRY, address.getCountry().getIsoCode());

		// version
		final Collection versions = catalog.getCatalogVersions();
		assertTrue("One catalog version?", versions.size() == 1);
		assertNotNull("CatalogVersion is null!", catalog.getCatalogVersion(CATALOGVERSION_VERSION_1));
		assertEquals(version, catalog.getCatalogVersion(CATALOGVERSION_VERSION_1));

		checkCatalogVersion(version, CATALOGVERSION_VERSION_1);
	}

	@Test
	public void testGetterMethods()
	{
		assertNotNull("keywords is null!", version.getAllKeywords());
		assertEquals(3, version.getAllKeywords().size());
		for (final Iterator iter = version.getAllKeywords().iterator(); iter.hasNext();)
		{
			final String keyword = ((Keyword) iter.next()).getKeyword();
			assertTrue("Keyword " + keyword + " exists!", ALL_KEYWORDS.contains(keyword));
		}
		assertNotNull("products is null!", version.getAllProducts());
		assertEquals(6, version.getAllProducts().size());
		for (final Iterator iter = version.getAllProducts().iterator(); iter.hasNext();)
		{
			final String code = ((Product) iter.next()).getCode();
			assertTrue("Product " + code + " exists!", ALL_PRODUCTS.contains(code));
		}
		assertNotNull("products is null!", version.getProducts(PRODUCT_01_ID));
		assertEquals(1, version.getProducts(PRODUCT_01_ID).size());
		assertEquals(product1, version.getProducts(PRODUCT_01_ID).iterator().next());
		// NOTE: get root categories
		assertNotNull("root is null!", version.getRootCategories());
		assertEquals(1, version.getRootCategories().size());
		assertEquals(root, version.getRootCategories().iterator().next());
		assertNotNull("category is null!", version.getCategories(CATEGORY_1_ID));
		assertEquals(1, version.getCategories(CATEGORY_1_ID).size());
		assertEquals(cat1, version.getCategories(CATEGORY_1_ID).iterator().next());

		assertNotNull("medias is null!", version.getAllMedias());
		assertEquals(3, version.getAllMedias().size());
		for (final Iterator iter = version.getAllMedias().iterator(); iter.hasNext();)
		{
			final String code = ((Media) iter.next()).getCode();
			assertTrue("Media " + code + " exists!", ALL_MEDIAS.contains(code));
		}
		assertNotNull("medias is null!", version.getMedias(MEDIA_01_ID));
		assertEquals(1, version.getMedias(MEDIA_01_ID).size());
		assertEquals(media1, version.getMedias(MEDIA_01_ID).iterator().next());
		assertNotNull("medias is null!", version.getMedias(MEDIA_03_ID));
		assertEquals(1, version.getMedias(MEDIA_03_ID).size());
		assertEquals(media3, version.getMedias(MEDIA_03_ID).iterator().next());

		assertNotNull("catalog is null!", supplier.getCatalogsAsSupplier());
		assertEquals(1, supplier.getCatalogsAsSupplier().size());
		assertEquals(catalog, supplier.getCatalogsAsSupplier().iterator().next());
		assertNotNull("catalog is null!", buyer.getCatalogsAsBuyer());
		assertEquals(1, buyer.getCatalogsAsBuyer().size());
		assertEquals(catalog, buyer.getCatalogsAsBuyer().iterator().next());
	}

	@Test
	public void testRemoveDefaultCatalog()
	{
		Catalog defaultCatalog = null;
		Catalog anotherDefaultCatalog = null;
		try
		{
			defaultCatalog = cManager.createCatalog("default");
			defaultCatalog.setDefaultCatalog(true);
			assertTrue(defaultCatalog.isDefaultCatalogAsPrimitive());
			assertEquals(cManager.getDefaultCatalog(), defaultCatalog);
			try
			{
				//try to remove default catalog!
				defaultCatalog.remove();
				fail("default catalog could be removed!!");
			}
			catch (final ConsistencyCheckException e)
			{
				//well done
			}
			assertNotNull(defaultCatalog);
			anotherDefaultCatalog = cManager.createCatalog("secondDefault");
			assertNotNull(anotherDefaultCatalog);
			try
			{
				anotherDefaultCatalog.setDefaultCatalog(true);
				//try to remove ex-default catalog
				defaultCatalog.remove();
				defaultCatalog = null;
			}
			catch (final ConsistencyCheckException e)
			{
				e.printStackTrace();
				fail("Couldn't remove ex-default catalog");
			}
		}
		finally
		{
			if (defaultCatalog != null)
			{
				defaultCatalog.setDefaultCatalog(false);
			}
			if (anotherDefaultCatalog != null)
			{
				anotherDefaultCatalog.setDefaultCatalog(false);
			}
		}
	}

	@Test
	public void testRemoveActiveCatalogVersion()
	{
		final Catalog catalog = cManager.createCatalog("testcatalog");
		assertNotNull(catalog);

		final CatalogVersion catalogVersion = cManager.createCatalogVersion(catalog, "cv 1.0", langDE);
		catalog.setActiveCatalogVersion(catalogVersion);
		assertNotNull(catalogVersion);

		try
		{
			//try to remove active catalog version
			catalogVersion.remove();
			fail("active catalogversion could be removed!!");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine here
		}
		finally
		{
			catalogVersion.setActive(Boolean.FALSE); // so cleanUp() can do the job
		}
	}

	@Test
	public void testSetAllName() throws Exception
	{
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(langDE);

		final Map nameMap = new HashMap();
		nameMap.put(langDE, "nameDE");
		nameMap.put(langEN, "nameEN");
		try
		{
			catalog.setAllName(ctx, nameMap);
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}

		assertEquals("nameDE", catalog.getName(ctx));
	}

	@Test
	public void testDefaultVersionOwner() throws ConsistencyCheckException
	{
		final Employee emp1 = UserManager.getInstance().createEmployee("juhu1234");
		final Employee emp2 = UserManager.getInstance().createEmployee("juhuNochmal");

		assertNotNull(emp1);
		assertNotNull(emp2);

		final Catalog cat = cManager.createCatalog("blahfasel");


		assertNotNull(cat);

		final User user = jaloSession.getUser();

		try
		{
			jaloSession.setUser(emp1);

			// test fallback: current user emp1 should become reader/writer of new version
			final CatalogVersion ver1 = cManager.createCatalogVersion(cat, "1.0", null);
			assertNotNull(ver1);

			assertEquals(Collections.singletonList(emp1), ver1.getReadPrincipals());
			assertEquals(Collections.singletonList(emp1), ver1.getWritePrincipals());

			final Map params = new HashMap();
			params.put(CatalogVersion.CATALOG, cat);
			params.put(CatalogVersion.VERSION, "2.0");
			params.put(CatalogVersion.READPRINCIPALS, Collections.singletonList(emp2));
			params.put(CatalogVersion.WRITEPRINCIPALS, Collections.singletonList(emp2));

			// now we dont want fallback since reader/writer are defined explicitely
			final CatalogVersion ver2 = cManager.createCatalogVersion(params);
			assertNotNull(ver2);
			assertEquals(Collections.singletonList(emp2), ver2.getReadPrincipals());
			assertEquals(Collections.singletonList(emp2), ver2.getWritePrincipals());
		}
		finally
		{
			jaloSession.setUser(user);
		}
	}

	@Test
	public void testPLA7006default() throws ConsistencyCheckException
	{
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.removeAttribute(Category.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY);

		UserGroup custgr = null;
		User customer = null;
		try
		{
			//setup
			custgr = usermanager.createUserGroup("customergroup");
			assertNotNull(custgr);
			customer = usermanager.createUser("customer");
			assertNotNull(customer);
			custgr.addMember(customer);
			assertEquals(1, custgr.getMembers().size());

			final Category category1 = categorymanager.createCategory("c1");
			final Category category2 = categorymanager.createCategory("c2");
			final Category category3 = categorymanager.createCategory("c3");
			final Category category4 = categorymanager.createCategory("c4");
			category1.addSubcategory(category2);
			category1.addSubcategory(category4);
			category2.addSubcategory(category3);
			assertNotNull(category1);
			assertNotNull(category2);
			assertNotNull(category3);
			assertNotNull(category4);
			assertEquals(0, category1.getSupercategoriesCount());
			assertEquals(2, category1.getSubcategoryCount());
			assertEquals(1, category2.getSupercategoriesCount());
			assertEquals(1, category2.getSubcategoryCount());
			assertEquals(1, category3.getSupercategoriesCount());
			assertEquals(0, category3.getSubcategoryCount());
			assertEquals(1, category4.getSupercategoriesCount());
			assertEquals(0, category4.getSubcategoryCount());

			//check PLA7006
			final List<Principal> plist = new ArrayList<Principal>();
			plist.add(custgr);
			cManager.setAllowedPrincipals(localctx, category2, plist);
			assertEquals(1, cManager.getAllowedPrincipals(category1).size());
			assertEquals(1, cManager.getAllowedPrincipals(category3).size());
			assertEquals(0, cManager.getAllowedPrincipals(category4).size());

		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	@Test
	public void testPLA7006withDisabledinSessionContext() throws ConsistencyCheckException
	{
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.setAttribute(Category.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY, Boolean.TRUE);

		UserGroup custgr = null;
		User customer = null;
		try
		{
			//setup
			custgr = usermanager.createUserGroup("customergroup");
			assertNotNull(custgr);
			customer = usermanager.createUser("customer");
			assertNotNull(customer);
			custgr.addMember(customer);
			assertEquals(1, custgr.getMembers().size());


			final Category category1 = categorymanager.createCategory("c1");
			final Category category2 = categorymanager.createCategory("c2");
			final Category category3 = categorymanager.createCategory("c3");
			final Category category4 = categorymanager.createCategory("c4");
			category1.addSubcategory(category2);
			category1.addSubcategory(category4);
			category2.addSubcategory(category3);
			assertNotNull(category1);
			assertNotNull(category2);
			assertNotNull(category3);
			assertEquals(0, category1.getSupercategoriesCount());
			assertEquals(2, category1.getSubcategoryCount());
			assertEquals(1, category2.getSupercategoriesCount());
			assertEquals(1, category2.getSubcategoryCount());
			assertEquals(1, category3.getSupercategoriesCount());
			assertEquals(0, category3.getSubcategoryCount());
			assertEquals(1, category4.getSupercategoriesCount());
			assertEquals(0, category4.getSubcategoryCount());
			//check PLA7006
			final List<Principal> plist = new ArrayList<Principal>();
			plist.add(custgr);
			cManager.setAllowedPrincipals(localctx, category2, plist);
			assertEquals(0, cManager.getAllowedPrincipals(category1).size());
			assertEquals(0, cManager.getAllowedPrincipals(category3).size());
			assertEquals(0, cManager.getAllowedPrincipals(category4).size());
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}

	}

	@Test
	public void testPLA7006withEnabledinSessionContext() throws ConsistencyCheckException
	{
		final SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
		localctx.setAttribute(Category.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY, Boolean.FALSE);

		UserGroup custgr = null;
		User customer = null;
		try
		{
			//setup
			custgr = usermanager.createUserGroup("customergroup");
			assertNotNull(custgr);
			customer = usermanager.createUser("customer");
			assertNotNull(customer);
			custgr.addMember(customer);
			assertEquals(1, custgr.getMembers().size());

			final Category category1 = categorymanager.createCategory("c1");
			final Category category2 = categorymanager.createCategory("c2");
			final Category category3 = categorymanager.createCategory("c3");
			final Category category4 = categorymanager.createCategory("c4");
			category1.addSubcategory(category2);
			category1.addSubcategory(category4);
			category2.addSubcategory(category3);
			assertNotNull(category1);
			assertNotNull(category2);
			assertNotNull(category3);
			assertEquals(0, category1.getSupercategoriesCount());
			assertEquals(2, category1.getSubcategoryCount());
			assertEquals(1, category2.getSupercategoriesCount());
			assertEquals(1, category2.getSubcategoryCount());
			assertEquals(1, category3.getSupercategoriesCount());
			assertEquals(0, category3.getSubcategoryCount());
			assertEquals(1, category4.getSupercategoriesCount());
			assertEquals(0, category4.getSubcategoryCount());
			//check PLA7006
			final List<Principal> plist = new ArrayList<Principal>();
			plist.add(custgr);
			cManager.setAllowedPrincipals(localctx, category2, plist);
			assertEquals(1, cManager.getAllowedPrincipals(category1).size());
			assertEquals(1, cManager.getAllowedPrincipals(category3).size());
			assertEquals(0, cManager.getAllowedPrincipals(category4).size());
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}
}
