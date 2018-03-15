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
package de.hybris.platform.directpersistence.read;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultSLDRelationDAOTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource(name = "sldRelationDAO")
	private SLDRelationDAO relationDAO;
	private ProductModel p1, p2, p3, p4, p5;
	private CategoryModel ctg1, ctg2;

	@Before
	public void setUp() throws Exception
	{
		final TestDataCreator testDataCreator = new TestDataCreator(modelService);

		final CatalogModel catalog = testDataCreator.createCatalog();
		final CatalogVersionModel catalogVersion = testDataCreator.createCatalogVersion("testVersion", catalog);

		p1 = testDataCreator.createProduct(catalogVersion);
		p2 = testDataCreator.createProduct(catalogVersion);
		p3 = testDataCreator.createProduct(catalogVersion);
		p4 = testDataCreator.createProduct(catalogVersion);
		p5 = testDataCreator.createProduct(catalogVersion);

		ctg1 = testDataCreator.createCategory(catalogVersion, p1, p2, p3, p4, p5);
		ctg2 = testDataCreator.createCategory(catalogVersion, p1, p2, p3, p4, p5);
	}

	@Test
	public void shouldReturnAllRelatedTargetsWhenLookingFromTheSourceEnd() throws Exception
	{
		// given
		final PK categoryPk = ctg1.getPk();
		final String relationName = "CategoryProductRelation";
		final RelationInformation relationInfo = RelationInformation.builder(categoryPk, relationName, true).build();

		// when
		final Collection<ProductModel> linkedItems = relationDAO.getRelatedModels(relationInfo);

		// then
		assertThat(linkedItems).hasSize(5).containsOnly(p1, p2, p3, p4, p5);
	}

	@Test
	public void shouldReturnallRelatedSourcesWhenLookingFromTheTargetEnd() throws Exception
	{
		// given
		final PK productPk = p1.getPk();
		final String relationName = "CategoryProductRelation";
		final RelationInformation relationInfo = RelationInformation.builder(productPk, relationName, false).build();

		// when
		final Collection<CategoryModel> linkedItems = relationDAO.getRelatedModels(relationInfo);

		// then
		assertThat(linkedItems).hasSize(2).containsOnly(ctg1, ctg2);
	}


}
