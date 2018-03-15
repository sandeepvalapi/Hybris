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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


public class DefaultSLDItemDAOTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource(name = "sldItemDAO")
	private DefaultSLDItemDAO dao;

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

        final Product productItem = modelService.getSource(p1);
        productItem.setProperty("fooo", "baaar");

        ctg1 = testDataCreator.createCategory(catalogVersion, p1, p2, p3, p4, p5);
		ctg2 = testDataCreator.createCategory(catalogVersion, p1, p2, p3, p4, p5);
	}

	@Test
	public void shouldLoadContainerForAnExistingItem() throws Exception
	{
		// given
		final PK productPk = p1.getPk();

		// when
		final SLDDataContainer result = dao.load(productPk);

		// then
		SLDDataContainerAssert.assertThat(result).containsAttribute(ProductModel.CODE).withValueEqualTo(p1.getCode());
		SLDDataContainerAssert.assertThat(result).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p1.getName());
	}

	@Test
	public void shouldBulkLoadContainersForAnExistingItems() throws Exception
	{
        // given
        final ArrayList<PK> pks = Lists.newArrayList(p1.getPk(), p2.getPk(), p3.getPk());

        // when
        final Collection<SLDDataContainer> result = dao.load(pks);

        // then
        assertThat(result).hasSize(3);
        final SLDDataContainer first = getContainerWithPk(result, p1.getPk());
        SLDDataContainerAssert.assertThat(first).containsAttribute(ProductModel.CODE).withValueEqualTo(p1.getCode());
        SLDDataContainerAssert.assertThat(first).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p1.getName());

        final SLDDataContainer second = getContainerWithPk(result, p2.getPk());
        SLDDataContainerAssert.assertThat(second).containsAttribute(ProductModel.CODE).withValueEqualTo(p2.getCode());
        SLDDataContainerAssert.assertThat(second).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p2.getName());

        final SLDDataContainer third = getContainerWithPk(result, p3.getPk());
        SLDDataContainerAssert.assertThat(third).containsAttribute(ProductModel.CODE).withValueEqualTo(p3.getCode());
        SLDDataContainerAssert.assertThat(third).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p3.getName());
    }

	@Test
	public void shouldReturnEmptyResultWhenAskingForAlreadyRemovedItems() throws Exception
	{
        // given
        final ArrayList<PK> pks = Lists.newArrayList(p1.getPk(), p2.getPk(), p3.getPk());
        modelService.removeAll(p1, p2, p3);

        // when
        final List<SLDDataContainer> result = dao.load(pks);

        // then
        assertThat(result).isEmpty();
    }

    private SLDDataContainer getContainerWithPk(final Collection<SLDDataContainer> containers, final PK pk)
    {
        final Optional<SLDDataContainer> possibleContainer = containers.stream().filter(c -> c.getPk().equals(pk)).findFirst();
        assertThat(possibleContainer.isPresent()).isTrue();
        return possibleContainer.get();
    }

}
