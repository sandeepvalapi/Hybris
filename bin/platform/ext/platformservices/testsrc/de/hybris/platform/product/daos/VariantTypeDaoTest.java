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
package de.hybris.platform.product.daos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for VariantTypeDao.
 */
@IntegrationTest
public class VariantTypeDaoTest extends ServicelayerTransactionalTest
{
	private static final String TEST_VARIANT_PRODUCT = "testVariantProduct0";
	private static final String TEST_PRODUCT = "testProduct0";

	@Resource
	private VariantTypeDao variantTypeDao;

	@Resource
	private ProductDao productDao;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
	}

	/**
	 * Test method for {@link de.hybris.platform.product.daos.impl.DefaultVariantTypeDao#findAllVariantTypes()}.
	 */
	@Test
	public void testFindAllVariantTypes()
	{
		//when
		final List<VariantTypeModel> result = variantTypeDao.findAllVariantTypes();

		//then
		assertNotNull(result);
		final Set<PK> resultPKs = new java.util.HashSet<PK>(result.size());
		for (final VariantTypeModel type : result)
		{
			assertNotNull(type.getCode());
			assertTrue("Variant type " + type + " is already in the result ", resultPKs.add(type.getPk()));
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.daos.impl.DefaultVariantTypeDao#getBaseProductCount(VariantTypeModel)}.
	 */
	@Test
	public void testGetBaseProductCount()
	{
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode(TEST_VARIANT_PRODUCT);
		variantTypeModel.setSuperType(typeService.getComposedTypeForCode(VariantProductModel._TYPECODE));
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(variantTypeModel);
		//when
		int result = variantTypeDao.getBaseProductCount(variantTypeModel);
		assertTrue(result == 0);

		final ProductModel base = productDao.findProductsByCode(TEST_PRODUCT).get(0);
		base.setVariantType(variantTypeModel);
		modelService.save(base);
		//when
		result = variantTypeDao.getBaseProductCount(variantTypeModel);

		//then
		assertTrue(result == 1);

	}


}
