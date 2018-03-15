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
package de.hybris.platform.category;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Direct persistence tests for Category related scenarios
 */
@IntegrationTest
public class CategorySldTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private SessionService sessionService;

	private CatalogModel testCatalog;

	private CatalogVersionModel catVerA1;

	private static final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void setUp()
	{
		persistenceLegacyMode.switchToValue("false");
		testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("testCatalog");
		catVerA1 = modelService.create(CatalogVersionModel.class);
		catVerA1.setCatalog(testCatalog);
		catVerA1.setVersion("A1");
		modelService.saveAll();
		categoryService.enableSubcategoryRemovalCheck();
	}

	@After
	public void tearDown()
	{
		persistenceLegacyMode.switchBackToDefault();
		sessionService.removeAttribute(CategoryConstants.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY);
	}

	@Test
	public void shouldCreateCategoryWithBasicAttributes()
	{
		final CategoryModel category = modelService.create(CategoryModel.class);
		category.setCatalogVersion(catVerA1);
		category.setCode("testCategory");

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, category);

		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(category);

		modelService.refresh(category);
		assertThat(category.getCatalogVersion()).isEqualTo(catVerA1);
		final CatalogModel catalogFieldInCategory = category.getProperty(CategoryModel.CATALOG);
		assertThat(catalogFieldInCategory).isEqualTo(testCatalog);
		assertThat(category.getAllowedPrincipals()).isEmpty();
		assertThat(category.getAllSupercategories()).isEmpty();
		assertThat(category.getAllSubcategories()).isEmpty();
	}

	@Test
	public void shouldCreateCategoriesWithTheSameSupercategory()
	{
		final CategoryModel categoryA = modelService.create(CategoryModel.class);
		categoryA.setCatalogVersion(catVerA1);
		categoryA.setCode("testCategoryA");
		modelService.save(categoryA); //supercategory - given

		final CategoryModel categoryA1 = modelService.create(CategoryModel.class);
		categoryA1.setCatalogVersion(catVerA1);
		categoryA1.setCode("testCategoryA1");
		categoryA1.setSupercategories(Collections.singletonList(categoryA));

		final CategoryModel categoryA2 = modelService.create(CategoryModel.class);
		categoryA2.setCatalogVersion(catVerA1);
		categoryA2.setCode("testCategoryA2");
		categoryA2.setSupercategories(Collections.singletonList(categoryA));

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, categoryA1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(categoryA1);
		modelService.refresh(categoryA1);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, categoryA2);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(categoryA2);
		modelService.refresh(categoryA2);
		modelService.refresh(categoryA1);
		modelService.refresh(categoryA);

		assertThat(categoryA1.getAllSupercategories()).containsOnly(categoryA);
		assertThat(categoryA1.getAllSubcategories()).isEmpty();

		assertThat(categoryA2.getAllSupercategories()).containsOnly(categoryA);
		assertThat(categoryA2.getAllSubcategories()).isEmpty();

		assertThat(categoryA.getAllSupercategories()).isEmpty();
		assertThat(categoryA.getAllSubcategories()).containsOnly(categoryA1, categoryA2);

		assertThat(categoryA1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryA.getAllowedPrincipals()).isEmpty();
		assertThat(categoryA2.getAllowedPrincipals()).isEmpty();

	}

	@Test
	public void shouldCreateCategoryWitAllowedPrincipalsOnlyForGivenCategory()
	{
		//given
		final CategoryModel categoryA = modelService.create(CategoryModel.class);
		categoryA.setCatalogVersion(catVerA1);
		categoryA.setCode("testCategoryA");
		final CategoryModel categoryA1 = modelService.create(CategoryModel.class);
		categoryA1.setCatalogVersion(catVerA1);
		categoryA1.setCode("testCategoryA1");
		categoryA1.setSupercategories(Collections.singletonList(categoryA));
		modelService.saveAll(categoryA, categoryA1); //sub and supercategory - given

		final PrincipalModel principalAdmin = userService.getAdminUser();
		sessionService.setAttribute(CategoryConstants.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY, Boolean.TRUE);
		//when

		categoryA1.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, categoryA1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(categoryA1);
		modelService.refresh(categoryA1);
		modelService.refresh(categoryA);

		assertThat(categoryA1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryA.getAllowedPrincipals()).isEmpty();
	}

	@Test
	public void shouldCreateCategoryWitAllowedPrincipalsForAllRelatedCategories()
	{
		//given
		final CategoryModel categoryA = modelService.create(CategoryModel.class);
		categoryA.setCatalogVersion(catVerA1);
		categoryA.setCode("testCategoryA");
		final CategoryModel categoryA1 = modelService.create(CategoryModel.class);
		categoryA1.setCatalogVersion(catVerA1);
		categoryA1.setCode("testCategoryA1");
		categoryA1.setSupercategories(Collections.singletonList(categoryA));
		final CategoryModel categoryA1_1 = modelService.create(CategoryModel.class);
		categoryA1_1.setCatalogVersion(catVerA1);
		categoryA1_1.setCode("testCategoryA1_1");
		categoryA1_1.setSupercategories(Collections.singletonList(categoryA1));

		modelService.saveAll(categoryA, categoryA1, categoryA1_1); //sub and supercategory - given

		final PrincipalModel principalAdmin = userService.getAdminUser();
		//sessionService.setAttribute(CategoryConstants.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY, Boolean.FALSE); //default
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();
		//when

		categoryA1.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, categoryA1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(categoryA1);

		modelService.refresh(categoryA1);
		modelService.refresh(categoryA);
		modelService.refresh(categoryA1_1);

		assertThat(categoryA1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryA.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryA1_1.getAllowedPrincipals()).containsOnly(principalAdmin);

		//try the same with removing allowed principals
		categoryA1.setAllowedPrincipals(null);
		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, categoryA1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(categoryA1);
		modelService.refresh(categoryA1);
		modelService.refresh(categoryA);
		modelService.refresh(categoryA1_1);

		assertThat(categoryA1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryA.getAllowedPrincipals()).containsOnly(principalAdmin); //can not be removed from supercategory
		assertThat(categoryA1_1.getAllowedPrincipals()).isEmpty();

	}

}
