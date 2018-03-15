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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogVersionDaoTest extends AbstractCatalogTest
{

	@Resource
	private CatalogVersionDao catalogVersionDao;

	@Resource
	private UserService userService;


	private final static String TEST_USER_1 = "testUser1";
	private final static String TEST_USER_2 = "testUser2";

	private final static String TEST_USER_GROUP_PARENT = "testUserGroupParent";
	private final static String TEST_USER_GROUP_CHILD = "testUserGroupChild";


	private UserModel user1 = null;
	private UserModel user2 = null;

	private UserGroupModel groupParent = null;
	private UserGroupModel groupChild = null;

	private CatalogVersionModel cv1Spring, cv1Winter, cv2Spring, cv2Winter;

	private ProductModel product1, product2;

	private MediaModel mediaModel1, mediaModel2, mediaModel3;

	private KeywordModel keywordModel1, keywordModel2, keywordModel3;

	/**
	 * 
	 */
	@Before
	public void beforeTest() throws Exception
	{
		user1 = userService.getUserForUID(TEST_USER_1);
		user2 = userService.getUserForUID(TEST_USER_2);
		groupChild = userService.getUserGroupForUID(TEST_USER_GROUP_CHILD);
		groupParent = userService.getUserGroupForUID(TEST_USER_GROUP_PARENT);

		Assert.assertNotNull(user1);
		Assert.assertNotNull(user2);
		Assert.assertNotNull(groupChild);
		Assert.assertNotNull(groupParent);

		final CatalogModel catalogTemplate = new CatalogModel();
		catalogTemplate.setId(TEST_CATALOG_1);
		final CatalogModel testCatalog1 = flexibleSearchService.getModelByExample(catalogTemplate);
		catalogTemplate.setId(TEST_CATALOG_2);
		final CatalogModel testCatalog2 = flexibleSearchService.getModelByExample(catalogTemplate);

		final CatalogVersionModel template = new CatalogVersionModel();
		template.setCatalog(testCatalog1);
		template.setVersion(SPRING_VERSION);
		cv1Spring = flexibleSearchService.getModelByExample(template);

		template.setVersion(WINTER_VERSION);
		cv1Winter = flexibleSearchService.getModelByExample(template);

		template.setCatalog(testCatalog2);
		template.setVersion(SPRING_VERSION);
		cv2Spring = flexibleSearchService.getModelByExample(template);

		template.setVersion(WINTER_VERSION);
		cv2Winter = flexibleSearchService.getModelByExample(template);

		Assert.assertNotNull(cv1Spring);
		Assert.assertNotNull(cv1Winter);
		Assert.assertNotNull(cv2Spring);
		Assert.assertNotNull(cv2Winter);

		product1 = modelService.create(ProductModel.class);
		product1.setCatalogVersion(cv1Spring);
		product1.setCode("product1");
		modelService.save(product1);

		product2 = modelService.create(ProductModel.class);
		product2.setCatalogVersion(cv1Spring);
		product2.setCode("product2");
		modelService.save(product2);

		mediaModel1 = modelService.create(MediaModel.class);
		mediaModel1.setCode("mediaModel1");
		mediaModel1.setCatalogVersion(cv1Spring);
		modelService.save(mediaModel1);

		mediaModel2 = modelService.create(MediaModel.class);
		mediaModel2.setCode("mediaModel2");
		mediaModel2.setCatalogVersion(cv1Spring);
		modelService.save(mediaModel2);

		mediaModel3 = modelService.create(MediaModel.class);
		mediaModel3.setCode("mediaModel3");
		mediaModel3.setCatalogVersion(cv1Spring);
		modelService.save(mediaModel3);

		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode("code");
		modelService.save(language);

		keywordModel1 = new KeywordModel();
		keywordModel1.setKeyword("keywordModel1");
		keywordModel1.setLanguage(language);
		keywordModel1.setCatalogVersion(cv1Spring);
		modelService.save(keywordModel1);

		keywordModel2 = new KeywordModel();
		keywordModel2.setKeyword("keywordModel2");
		keywordModel2.setLanguage(language);
		keywordModel2.setCatalogVersion(cv1Spring);
		modelService.save(keywordModel2);

		keywordModel3 = new KeywordModel();
		keywordModel3.setKeyword("keywordModel3");
		keywordModel3.setLanguage(language);
		keywordModel3.setCatalogVersion(cv1Spring);
		modelService.save(keywordModel3);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.daos.CatalogVersionDao#findCatalogVersions(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFindCatalogVersions()
	{
		Assert.assertEquals("Wrong catalogversion returned", cv1Spring,
				catalogVersionDao.findCatalogVersions(TEST_CATALOG_1, SPRING_VERSION).iterator().next());
		Assert.assertEquals("Wrong catalogversion returned", cv1Winter,
				catalogVersionDao.findCatalogVersions(TEST_CATALOG_1, WINTER_VERSION).iterator().next());

		Assert.assertEquals("Wrong catalogversion returned", cv2Spring,
				catalogVersionDao.findCatalogVersions(TEST_CATALOG_2, SPRING_VERSION).iterator().next());
		Assert.assertEquals("Wrong catalogversion returned", cv2Winter,
				catalogVersionDao.findCatalogVersions(TEST_CATALOG_2, WINTER_VERSION).iterator().next());



	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindCatalogVersionsNullCatalog()
	{
		catalogVersionDao.findCatalogVersions(null, WINTER_VERSION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindCatalogVersionsNullVersion()
	{
		catalogVersionDao.findCatalogVersions(TEST_CATALOG_1, null);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.daos.CatalogVersionDao#findWritableCatalogVersions(de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetWriteableCatalogVersions() throws Exception
	{
		final Collection<CatalogVersionModel> user1writableCV = catalogVersionDao.findWritableCatalogVersions(user1);
		for (final CatalogVersionModel cv : user1writableCV)
		{
			Logger.getLogger(this.getClass()).info(cv.getPk());
		}
		Assert.assertEquals("Unexpected writable catalog versions size", 3, user1writableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", user1writableCV.contains(cv1Winter));
		Assert.assertTrue("Collection should contain writable catalog versions", user1writableCV.contains(cv2Spring));
		Assert.assertTrue("Collection should contain writable catalog versions", user1writableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> user2writableCV = catalogVersionDao.findWritableCatalogVersions(user2);
		Assert.assertEquals("Unexpected writable catalog versions size", 2, user2writableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", user2writableCV.contains(cv2Winter));
		Assert.assertTrue("Collection should contain writable catalog versions", user2writableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> childGroupwritableCV = catalogVersionDao.findWritableCatalogVersions(groupChild);
		Assert.assertEquals("Unexpected writable catalog versions size", 2, childGroupwritableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", childGroupwritableCV.contains(cv2Spring));
		Assert.assertTrue("Collection should contain writable catalog versions", childGroupwritableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> parentGroupwritableCV = catalogVersionDao.findWritableCatalogVersions(groupParent);
		Assert.assertEquals("Unexpected writable catalog versions size", 1, parentGroupwritableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", parentGroupwritableCV.contains(cv1Spring));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.daos.CatalogVersionDao#findReadableCatalogVersions(de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testGetReadableCatalogVersions()
	{
		final Collection<CatalogVersionModel> user1readableCV = catalogVersionDao.findReadableCatalogVersions(user1);
		Assert.assertEquals("Unexpected writable catalog versions size", 3, user1readableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", user1readableCV.contains(cv1Winter));
		Assert.assertTrue("Collection should contain writable catalog versions", user1readableCV.contains(cv2Spring));
		Assert.assertTrue("Collection should contain writable catalog versions", user1readableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> user2readableCV = catalogVersionDao.findReadableCatalogVersions(user2);
		Assert.assertEquals("Unexpected writable catalog versions size", 2, user2readableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", user2readableCV.contains(cv2Winter));
		Assert.assertTrue("Collection should contain writable catalog versions", user2readableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> childGroupreadableCV = catalogVersionDao.findReadableCatalogVersions(groupChild);
		Assert.assertEquals("Unexpected writable catalog versions size", 2, childGroupreadableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", childGroupreadableCV.contains(cv2Spring));
		Assert.assertTrue("Collection should contain writable catalog versions", childGroupreadableCV.contains(cv1Spring));

		final Collection<CatalogVersionModel> parentGroupreadableCV = catalogVersionDao.findReadableCatalogVersions(groupParent);
		Assert.assertEquals("Unexpected writable catalog versions size", 1, parentGroupreadableCV.size());
		Assert.assertTrue("Collection should contain writable catalog versions", parentGroupreadableCV.contains(cv1Spring));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetReadableCatalogVersionsNullPrincipal()
	{
		catalogVersionDao.findReadableCatalogVersions(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWriatbleCatalogVersionsNullPrincipal()
	{
		catalogVersionDao.findWritableCatalogVersions(null);
	}

	@Test
	public void testFindAllProductCount()
	{
		assertThat(catalogVersionDao.findAllProductsCount(cv1Spring)).isEqualTo(Integer.valueOf(3));
		assertThat(catalogVersionDao.findAllProductsCount(cv2Spring)).isEqualTo(Integer.valueOf(0));
	}

	@Test
	public void testFindAllMediaCount()
	{
		assertThat(catalogVersionDao.findAllMediasCount(cv1Spring)).isEqualTo(Integer.valueOf(3));
		assertThat(catalogVersionDao.findAllMediasCount(cv2Spring)).isEqualTo(Integer.valueOf(0));
	}

	@Test
	public void testFindAllKeywordCount()
	{
		assertThat(catalogVersionDao.findAllKeywordsCount(cv1Spring)).isEqualTo(Integer.valueOf(3));
		assertThat(catalogVersionDao.findAllKeywordsCount(cv2Spring)).isEqualTo(Integer.valueOf(0));
	}

	@Test
	public void testFindAllCategoryCount()
	{
		assertThat(catalogVersionDao.findAllCategoriesCount(cv1Spring)).isEqualTo(Integer.valueOf(1));
		assertThat(catalogVersionDao.findAllCategoriesCount(cv2Spring)).isEqualTo(Integer.valueOf(0));
	}


	@Test
	public void testFindAllCatalogVersions()
	{
		assertThat(catalogVersionDao.findAllCatalogVersions()).contains(cv1Spring, cv1Winter, cv2Spring, cv2Winter);


	}


}
