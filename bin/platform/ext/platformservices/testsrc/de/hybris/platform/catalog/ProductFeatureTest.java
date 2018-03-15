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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * created for HOR-262
 */
@IntegrationTest
public class ProductFeatureTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	private ProductModel productModel = null;

	private ClassAttributeAssignmentModel caam = null;

	@Before
	public void prepare() throws ConsistencyCheckException
	{
		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");
		modelService.save(cm1);

		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);

		productModel = modelService.create(ProductModel.class);
		productModel.setCatalogVersion(cmv1);
		productModel.setCode("someFooCode");
		modelService.save(productModel);

		ClassificationSystemModel csmDef = null;

		ClassificationSystemVersionModel catalogDef = null;

		csmDef = modelService.create(ClassificationSystemModel.class);
		csmDef.setId("classFoo_a");

		catalogDef = modelService.create(ClassificationSystemVersionModel.class);
		catalogDef.setCatalog(csmDef);
		catalogDef.setVersion("ver def");
		csmDef.setDefaultCatalog(Boolean.TRUE);

		modelService.save(catalogDef);

		final ClassificationSystemModel csm = modelService.create(ClassificationSystemModel.class);
		csm.setId("modelSystemFoo_a");

		final ClassificationSystemVersionModel cvm = modelService.create(ClassificationSystemVersionModel.class);
		cvm.setCatalog(csm);
		cvm.setVersion("ver1.0");

		final ClassificationClassModel ccm = modelService.create(ClassificationClassModel.class);
		ccm.setCatalogVersion(cvm);
		ccm.setCode("ver1.0");

		modelService.save(ccm);

		final ClassificationAttributeModel cam = modelService.create(ClassificationAttributeModel.class);
		cam.setCode("attrModelFoo_a");
		cam.setSystemVersion(cvm);
		modelService.save(cam);

		caam = modelService.create(ClassAttributeAssignmentModel.class);
		caam.setClassificationAttribute(cam);
		caam.setClassificationClass(ccm);
		modelService.save(caam);
	}

	@Test
	public void shouldNotCreateProductFeatureIfProductNotSet()
	{
		final ProductFeatureModel modelItem = modelService.create(ProductFeatureModel.class);
		modelItem.setValue("someFooValue");
		modelItem.setClassificationAttributeAssignment(caam);

		try
		{
			modelService.save(modelItem);
			fail("Exception was expected (due to missing Product attribute) but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(ModelSavingException.class);
		}
	}

	@Test
	public void shouldNotCreateProductFeatureIfValueNotSet()
	{
		final ProductFeatureModel modelItem = modelService.create(ProductFeatureModel.class);
		modelItem.setProduct(productModel);
		modelItem.setClassificationAttributeAssignment(caam);

		try
		{
			modelService.save(modelItem);
			fail("Exception was expected (due to missing Value attribute) but not thrown");
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(ModelSavingException.class);
		}
	}

	@Test
	public void shouldCreateProductFeatureWithClassAttributeAssignment()
			throws JaloGenericCreationException, JaloAbstractTypeException, ConsistencyCheckException
	{
		final Map attrs = new HashMap();
		attrs.put(ProductFeature.PRODUCT, modelService.getSource(productModel));
		attrs.put(ProductFeature.VALUE, "someJaloValue");
		attrs.put(ProductFeature.CLASSIFICATIONATTRIBUTEASSIGNMENT, modelService.getSource(caam));
		attrs.put(ProductFeature.VALUEPOSITION, Integer.valueOf(0));

		final ComposedType type = JaloSession.getCurrentSession().getTenant().getJaloConnection().getTypeManager()
				.getComposedType(ProductFeature.class);
		final ProductFeature jaloFeature = (ProductFeature) type.newInstance(jaloSession.getSessionContext(), attrs);

		final ProductFeatureModel modelItem = modelService.create(ProductFeatureModel.class);
		modelItem.setProduct(productModel);
		modelItem.setValue("someFooValue");
		modelItem.setClassificationAttributeAssignment(caam);
		modelItem.setValuePosition(Integer.valueOf(1));

		modelService.save(modelItem);

		Assert.assertEquals(jaloFeature.getQualifier(), modelItem.getQualifier());
	}

	@Test
	public void shouldCreateProductFeatureWithQualifierExplicitelySet()
	{
		final ProductFeatureModel modelItem = modelService.create(ProductFeatureModel.class);
		modelItem.setProduct(productModel);
		modelItem.setValue("someFooValue");
		modelItem.setClassificationAttributeAssignment(caam);
		modelItem.setQualifier("someBarQual");

		modelService.save(modelItem);

		Assert.assertEquals("someBarQual", modelItem.getQualifier());
	}
}
