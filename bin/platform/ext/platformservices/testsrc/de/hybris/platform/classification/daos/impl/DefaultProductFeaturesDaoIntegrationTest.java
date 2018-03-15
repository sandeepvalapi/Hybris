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
package de.hybris.platform.classification.daos.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.daos.ProductFeaturesDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultProductFeaturesDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ProductFeaturesDao productFeaturesDao;
	@Resource
	private ProductService productService;
	@Resource
	private ClassificationClassesResolverStrategy classificationClassesResolverStrategy; //NOPMD

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.daos.impl.DefaultProductFeaturesDao#findProductFeaturesByProductAndAssignment(de.hybris.platform.core.model.product.ProductModel, java.util.List)}
	 * .
	 */
	@Test
	public void shouldFindFeaturesForProduct()
	{
		// given
		final ProductModel productModel = productService.getProductForCode("HW2300-2356");
		final Set<ClassificationClassModel> classificationClasses = classificationClassesResolverStrategy.resolve(productModel);
		final List<ClassAttributeAssignmentModel> assignments = classificationClassesResolverStrategy
				.getDeclaredClassAttributeAssignments(classificationClasses);

		// when
		final List<List<ItemModel>> features = productFeaturesDao.findProductFeaturesByProductAndAssignment(productModel,
				assignments);

		// then
		assertThat(features).isNotNull();
		assertThat(features).isNotEmpty();
		assertThat(features).hasSize(11);
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.daos.impl.DefaultProductFeaturesDao#findProductFeaturesByProductAndAssignment(ProductModel, List, List)}
	 * .
	 */
	@Test
	public void shouldFindFeaturesForProductWithExcludes()
	{
		// given
		final ProductModel productModel = productService.getProductForCode("HW2300-2356");
		final Set<ClassificationClassModel> classificationClasses = classificationClassesResolverStrategy.resolve(productModel);
		final List<ClassAttributeAssignmentModel> assignments = classificationClassesResolverStrategy
				.getDeclaredClassAttributeAssignments(classificationClasses);
		final List<ProductFeatureModel> excludes = getExcludes(productModel, assignments);

		// when
		final List<List<ItemModel>> features = productFeaturesDao.findProductFeaturesByProductAndAssignment(productModel,
				assignments, excludes);

		// then
		assertThat(features).isNotNull();
		assertThat(features).isNotEmpty();
		assertThat(features).hasSize(8);
	}


	private List<ProductFeatureModel> getExcludes(final ProductModel productModel,
			final List<ClassAttributeAssignmentModel> assignments)
	{
		final List<List<ItemModel>> result = productFeaturesDao
				.findProductFeaturesByProductAndAssignment(productModel, assignments);

		return Lists.newArrayList((ProductFeatureModel) result.get(0).get(0), (ProductFeatureModel) result.get(1).get(0),
				(ProductFeatureModel) result.get(2).get(0));
	}

}
