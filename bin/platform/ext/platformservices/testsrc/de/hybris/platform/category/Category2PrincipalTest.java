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
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests the behaviour of setting allowed principals inside the categories
 */
@IntegrationTest
public class Category2PrincipalTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Resource
	private CategoryService categoryService;

	private CatalogModel testCatalog;

	private CatalogVersionModel catVerA1;

	private CategoryModel categoryTop, categoryMiddle1, categoryMiddle2, categoryBottom1_1, categoryBottom1_2, categoryBottom2_1,
			categoryBottom2_2;

	@Before
	public void setUp()
	{
		testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("testCatalog");
		catVerA1 = modelService.create(CatalogVersionModel.class);
		catVerA1.setCatalog(testCatalog);
		catVerA1.setVersion("catVerA1");
		modelService.saveAll();

		categoryTop = modelService.create(CategoryModel.class);
		categoryTop.setCatalogVersion(catVerA1);
		categoryTop.setCode("testCategoryA");

		categoryMiddle1 = modelService.create(CategoryModel.class);
		categoryMiddle1.setCatalogVersion(catVerA1);
		categoryMiddle1.setCode("testCategoryA1");
		categoryMiddle1.setSupercategories(Collections.singletonList(categoryTop));
		categoryMiddle2 = modelService.create(CategoryModel.class);
		categoryMiddle2.setCatalogVersion(catVerA1);
		categoryMiddle2.setCode("testCategoryA2");
		categoryMiddle2.setSupercategories(Collections.singletonList(categoryTop));

		categoryBottom1_1 = modelService.create(CategoryModel.class);
		categoryBottom1_1.setCatalogVersion(catVerA1);
		categoryBottom1_1.setCode("testCategoryA1_1");
		categoryBottom1_1.setSupercategories(Collections.singletonList(categoryMiddle1));
		categoryBottom1_2 = modelService.create(CategoryModel.class);
		categoryBottom1_2.setCatalogVersion(catVerA1);
		categoryBottom1_2.setCode("testCategoryA1_2");
		categoryBottom1_2.setSupercategories(Collections.singletonList(categoryMiddle1));

		categoryBottom2_1 = modelService.create(CategoryModel.class);
		categoryBottom2_1.setCatalogVersion(catVerA1);
		categoryBottom2_1.setCode("testCategoryA2_1");
		categoryBottom2_1.setSupercategories(Collections.singletonList(categoryMiddle2));
		categoryBottom2_2 = modelService.create(CategoryModel.class);
		categoryBottom2_2.setCatalogVersion(catVerA1);
		categoryBottom2_2.setCode("testCategoryA2_2");
		categoryBottom2_2.setSupercategories(Collections.singletonList(categoryMiddle2));

		modelService.saveAll();

		verifyCategoryStructure();
	}

	private void verifyCategoryStructure()
	{
		assertThat(categoryTop.getAllSupercategories()).isEmpty(); //root
		assertThat(categoryTop.getAllSubcategories()).containsOnly(categoryMiddle1, categoryMiddle2, categoryBottom1_1,
				categoryBottom1_2, categoryBottom2_1, categoryBottom2_2);

		assertThat(categoryMiddle1.getAllSupercategories()).containsOnly(categoryTop); //middle
		assertThat(categoryMiddle1.getAllSubcategories()).containsOnly(categoryBottom1_1, categoryBottom1_2);

		assertThat(categoryMiddle2.getAllSupercategories()).containsOnly(categoryTop); //middle
		assertThat(categoryMiddle2.getAllSubcategories()).containsOnly(categoryBottom2_1, categoryBottom2_2);

		assertThat(categoryBottom1_1.getAllSupercategories()).containsOnly(categoryMiddle1, categoryTop);
		assertThat(categoryBottom1_2.getAllSubcategories()).isEmpty();//bottom

		assertThat(categoryBottom2_1.getAllSupercategories()).containsOnly(categoryMiddle2, categoryTop);
		assertThat(categoryBottom2_2.getAllSubcategories()).isEmpty();//bottom
	}

	@Test
	public void shouldSetPrincipalsFromMiddleCategoryToTopAndBottomCategories()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		final CustomerModel customerJan = createCustomer("Jan C.", "Jan");
		//sessionService.setAttribute(CategoryConstants.DISABLE_SETALLOWEDPRINCIPAL_RECURSIVELY, Boolean.FALSE); //default
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryMiddle1.setAllowedPrincipals(Arrays.asList(principalAdmin, customerJan));
		modelService.save(categoryMiddle1);
		//modelService.saveAll(categoryA, categoryA1, categoryA1_1);

		//then
		refreshCategoryStructure();

		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);

		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();

	}

	@Test
	public void shouldSetAndRemovePrincipalsFromMiddleCategoriesInTopAndBottomCategories()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryMiddle1.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryMiddle1);

		//then
		refreshCategoryStructure();

		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin);

		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();

		final CustomerModel customerJan = createCustomer("Jan C.", "Jan");

		//now - process removing from middle1 and set in middle2
		categoryMiddle1.setAllowedPrincipals(null);
		categoryMiddle2.setAllowedPrincipals(Collections.singletonList(customerJan));
		modelService.saveAll(categoryMiddle1, categoryMiddle2);
		refreshCategoryStructure();

		assertThat(categoryMiddle1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan); //cannot remove principal from supercategory  
		assertThat(categoryBottom1_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom1_2.getAllowedPrincipals()).isEmpty();

		assertThat(categoryMiddle2.getAllowedPrincipals()).containsOnly(customerJan);
		assertThat(categoryBottom2_1.getAllowedPrincipals()).containsOnly(customerJan);
		assertThat(categoryBottom2_2.getAllowedPrincipals()).containsOnly(customerJan);
	}

	@Test
	public void shouldSetAndRemovePrincipalsFromBottomCategoryInclRelatedCategories()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		final CustomerModel customerJan = createCustomer("Jan C.", "Jan");
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryBottom1_1.setAllowedPrincipals(Arrays.asList(principalAdmin, customerJan));
		modelService.save(categoryBottom1_1);

		//then
		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();

		//now - remove customerJan from the list for bottom category
		categoryBottom1_1.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryBottom1_1);

		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();
	}

	@Test
	public void shouldSetPrincipalsFromBottomAndTopCategoryInclRelatedCategories()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		final CustomerModel customerJan = createCustomer("Jan C.", "Jan");
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryTop.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		categoryBottom1_1.setAllowedPrincipals(Collections.singletonList(customerJan));
		//modelService.saveAll(categoryBottom1_1, categoryTop);
		modelService.saveAll();

		//then
		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(customerJan);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_2.getAllowedPrincipals()).containsOnly(principalAdmin);

	}

	@Test
	public void shouldSetPrincipalFromTopCategoryToAllSubcategories()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryTop.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryTop);

		//then
		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_2.getAllowedPrincipals()).containsOnly(principalAdmin);
	}

	@Test
	public void shouldSetPrincipalForMiddleCategoryWhileRemovingFromTopCategory()
	{
		//given
		final PrincipalModel principalAdmin = userService.getAdminUser();
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryTop.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryTop);

		//then
		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_2.getAllowedPrincipals()).containsOnly(principalAdmin);

		final CustomerModel customerJan = createCustomer("Jan C.", "Jan");

		categoryMiddle1.setAllowedPrincipals(Arrays.asList(principalAdmin, customerJan));
		categoryTop.setAllowedPrincipals(Collections.emptyList());
		modelService.saveAll(); //the order should NOT matter - top or middle first - always the same results
		refreshCategoryStructure();

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin, customerJan);
		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();
	}

	@Test
	public void testDiamondCaseSetPrincipalsFromMiddleCategory()
	{
		final CategoryModel categoryBottomShared = modelService.create(CategoryModel.class);
		categoryBottomShared.setCatalogVersion(catVerA1);
		categoryBottomShared.setCode("testCategoryBottomShared");
		categoryBottomShared.setSupercategories(Arrays.asList(categoryMiddle1, categoryMiddle2));
		modelService.saveAll();
		modelService.refresh(categoryBottomShared);
		refreshCategoryStructure();

		final PrincipalModel principalAdmin = userService.getAdminUser();
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryMiddle1.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryMiddle1);
		refreshCategoryStructure();
		modelService.refresh(categoryBottomShared);

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_2.getAllowedPrincipals()).containsOnly(principalAdmin);

		assertThat(categoryBottomShared.getAllowedPrincipals()).containsOnly(principalAdmin);

		assertThat(categoryMiddle2.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();
	}

	@Test
	public void testDiamondCaseSetPrincipalsFromBottomSharedCategory()
	{
		final CategoryModel categoryBottomShared = modelService.create(CategoryModel.class);
		categoryBottomShared.setCatalogVersion(catVerA1);
		categoryBottomShared.setCode("testCategoryBottomShared");
		categoryBottomShared.setSupercategories(Arrays.asList(categoryMiddle1, categoryMiddle2));
		modelService.saveAll();
		modelService.refresh(categoryBottomShared);
		refreshCategoryStructure();

		final PrincipalModel principalAdmin = userService.getAdminUser();
		assertThat(categoryService.isSetAllowedPrincipalsRecursivelyDisabled()).isFalse();

		//when
		categoryBottomShared.setAllowedPrincipals(Collections.singletonList(principalAdmin));
		modelService.save(categoryBottomShared);
		refreshCategoryStructure();
		modelService.refresh(categoryBottomShared);

		assertThat(categoryTop.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryMiddle1.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom1_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom1_2.getAllowedPrincipals()).isEmpty();

		assertThat(categoryBottomShared.getAllowedPrincipals()).containsOnly(principalAdmin);

		assertThat(categoryMiddle2.getAllowedPrincipals()).containsOnly(principalAdmin);
		assertThat(categoryBottom2_1.getAllowedPrincipals()).isEmpty();
		assertThat(categoryBottom2_2.getAllowedPrincipals()).isEmpty();
	}


	private void refreshCategoryStructure()
	{
		modelService.refresh(categoryTop);
		modelService.refresh(categoryMiddle1);
		modelService.refresh(categoryMiddle2);
		modelService.refresh(categoryBottom1_1);
		modelService.refresh(categoryBottom1_2);
		modelService.refresh(categoryBottom2_1);
		modelService.refresh(categoryBottom2_2);
	}

	private CustomerModel createCustomer(final String id, final String name)
	{
		final CustomerModel result = modelService.create(CustomerModel.class);
		result.setName(name);
		result.setUid(id);
		modelService.save(result);
		return result;
	}

}
