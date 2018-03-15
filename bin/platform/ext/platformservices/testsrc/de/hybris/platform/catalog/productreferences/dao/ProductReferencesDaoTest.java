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
package de.hybris.platform.catalog.productreferences.dao;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.references.daos.ProductReferencesDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * tests {@link ProductReferencesDao} interface implementation.
 */
@IntegrationTest
public class ProductReferencesDaoTest extends ServicelayerTest
{
	@Resource
	private ProductReferencesDao productReferencesDao;

	@Resource
	private ModelService modelService;


	private CatalogVersionModel catalogVesion;

	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;
	private ProductModel product4;
	private ProductModel product5;


	private ProductReferenceModel productReference1;
	private ProductReferenceModel productReference2;
	private ProductReferenceModel productReference3;
	private ProductReferenceModel productReference4;
	private ProductReferenceModel productReference5;
	private ProductReferenceModel productReference6;


	@Before
	public void setupTestData()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("testCatalog");
		modelService.save(catalog);

		catalogVesion = modelService.create(CatalogVersionModel.class);
		catalogVesion.setCatalog(catalog);
		catalogVesion.setVersion("testCatalogVersion");
		modelService.save(catalogVesion);


		product1 = prepareProduct("product1");
		product2 = prepareProduct("product2");
		product3 = prepareProduct("product3");
		product4 = prepareProduct("product4");
		product5 = prepareProduct("product5");

		productReference1 = prepareProductReference(product1, product2, ProductReferenceTypeEnum.CROSSELLING, "testReference1-2",
				true);
		productReference2 = prepareProductReference(product2, product3, ProductReferenceTypeEnum.CROSSELLING, "testReference2-3",
				true);
		productReference3 = prepareProductReference(product5, product5, ProductReferenceTypeEnum.UPSELLING, "testReference5-5",
				true);
		productReference4 = prepareProductReference(product3, product5, ProductReferenceTypeEnum.CROSSELLING, "testReference3-5",
				false);

		productReference5 = prepareProductReference(product2, product1, ProductReferenceTypeEnum.ACCESSORIES, "testReference2-1",
				false);
		productReference6 = prepareProductReference(product2, product1, ProductReferenceTypeEnum.FOLLOWUP, "testReference2-1a",
				true);

	}

	@Test
	public void testFindAllReferences()
	{
		assertThat(productReferencesDao.findAllReferences(product1)).contains(productReference5, productReference6).hasSize(2);
		assertThat(productReferencesDao.findAllReferences(product2)).contains(productReference1).hasSize(1);
		assertThat(productReferencesDao.findAllReferences(product3)).contains(productReference2).hasSize(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindAllProductReferences()
	{
		//try to find them all
		productReferencesDao.findProductReferences(null, null, null, null, Boolean.TRUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindAllWithNullValue()
	{
		productReferencesDao.findAllReferences(null);
	}

	@Test
	public void testFindProductReferences()
	{
		//all active crosselling references
		assertThat(productReferencesDao.findProductReferences(null, null, null, ProductReferenceTypeEnum.CROSSELLING, Boolean.TRUE))
				.hasSize(2).contains(productReference1, productReference2);

		//all crosselling references
		assertThat(productReferencesDao.findProductReferences(null, null, null, ProductReferenceTypeEnum.CROSSELLING, null))
				.hasSize(3).contains(productReference1, productReference2, productReference4);

		//all active with source = product2
		assertThat(productReferencesDao.findProductReferences(null, product2, null, null, Boolean.TRUE)).hasSize(2).contains(
				productReference2, productReference6);

		//all with source = product2
		assertThat(productReferencesDao.findProductReferences(null, product2, null, null, null)).hasSize(3).contains(
				productReference2, productReference5, productReference6);

		//not matching cases
		assertThat(
				productReferencesDao.findProductReferences(null, product2, null, ProductReferenceTypeEnum.MANDATORY, Boolean.TRUE))
				.isEmpty();
		assertThat(
				productReferencesDao
						.findProductReferences("test", product2, null, ProductReferenceTypeEnum.CROSSELLING, Boolean.TRUE)).isEmpty();
		assertThat(
				productReferencesDao.findProductReferences("testReference2-3", product2, product5,
						ProductReferenceTypeEnum.CROSSELLING, Boolean.TRUE)).isEmpty();

		//exact matching
		assertThat(
				productReferencesDao.findProductReferences("testReference2-3", product2, product3,
						ProductReferenceTypeEnum.CROSSELLING, Boolean.TRUE)).hasSize(1).contains(productReference2);

		//all with target = product5
		assertThat(productReferencesDao.findProductReferences(null, null, product5, null, null)).hasSize(2).contains(
				productReference3, productReference4);

		//all active with target = product5
		assertThat(productReferencesDao.findProductReferences(null, null, product5, null, Boolean.TRUE)).hasSize(1).contains(
				productReference3);

		//all with source = product2, target = product1
		assertThat(productReferencesDao.findProductReferences(null, product2, product1, null, null)).hasSize(2).contains(
				productReference5, productReference6);

		//all active with source = product2, target = product1
		assertThat(productReferencesDao.findProductReferences(null, product2, product1, null, Boolean.TRUE)).hasSize(1).contains(
				productReference6);

		//all active accessories with source = product2, target = product1
		assertThat(
				productReferencesDao.findProductReferences(null, product2, product1, ProductReferenceTypeEnum.ACCESSORIES,
						Boolean.TRUE)).isEmpty();

		//all accessories with source = product2, target = product1
		assertThat(productReferencesDao.findProductReferences(null, product2, product1, ProductReferenceTypeEnum.ACCESSORIES, null))
				.hasSize(1).contains(productReference5);

		//all follow-ups with source = product2, target = product1
		assertThat(productReferencesDao.findProductReferences(null, product2, product1, ProductReferenceTypeEnum.FOLLOWUP, null))
				.hasSize(1).contains(productReference6);
	}

	@Test
	public void findAllReferencesNoReferencesTest()
	{
		assertThat(productReferencesDao.findAllReferences(product4)).isEmpty();
	}

	@Test
	public void findAllReferencesCircularReferencesTest()
	{
		assertThat(productReferencesDao.findAllReferences(product5)).contains(productReference3, productReference4).hasSize(2);
	}



	@Test
	public void findAllReferencesTestCornerCases()
	{
		try
		{
			productReferencesDao.findAllReferences(new ProductModel());
			fail();
		}
		catch (final IllegalStateException e) //NOPMD
		{
			//ok nothing to do
		}
	}


	private ProductModel prepareProduct(final String code)
	{
		final ProductModel res = modelService.create(ProductModel.class);
		res.setCode(code);
		res.setCatalogVersion(catalogVesion);

		modelService.save(res);

		return res;
	}

	private ProductReferenceModel prepareProductReference(final ProductModel source, final ProductModel target,
			final ProductReferenceTypeEnum type, final String qualifier, final boolean active)
	{
		final ProductReferenceModel res = modelService.create(ProductReferenceModel.class);

		res.setTarget(target);
		res.setSource(source);
		res.setActive(Boolean.valueOf(active));
		res.setPreselected(Boolean.TRUE);
		res.setReferenceType(type);
		res.setQualifier(qualifier);

		modelService.save(res);

		return res;
	}
}
