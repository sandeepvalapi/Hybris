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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.validation.interceptors.TypeConstraintValidator;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import junit.framework.Assert;

import org.junit.Test;


@IntegrationTest
public class TypeConstraintTest extends AbstractConstraintTest
{
	@Test
	public void testTypeAssignmentForModel()
	{
		final TypeConstraintModel constraint = modelService.create(TypeConstraintModel.class);
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setId("Foo constraint");
		constraint.setTarget(ProductModel.class);
		final ComposedTypeModel productModel = typeService.getComposedType(ProductModel.class);
		constraint.setType(productModel);

		modelService.save(constraint);
		Assert.assertEquals(constraint.getType().getJaloclass(), modelService.getModelTypeClass(ProductModel.class)); //??
	}


	@Test
	public void testTypeAssignmentForModelFillInTarget()
	{
		final TypeConstraintModel constraint = modelService.create(TypeConstraintModel.class);
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setId("Foo constraint");
		final ComposedTypeModel productModel = typeService.getComposedType(ProductModel.class);
		constraint.setType(productModel);

		modelService.save(constraint);
		Assert.assertEquals(constraint.getType().getJaloclass(), modelService.getModelTypeClass(ProductModel.class));
		Assert.assertEquals("Target should be filled in with model class for type ", constraint.getTarget(), ProductModel.class);
	}

	@Test
	public void testTypeAssignmentWithInvalidAnnotation()
	{
		final TypeConstraintModel constraint = modelService.create(TypeConstraintModel.class);
		constraint.setAnnotation(String.class);
		constraint.setId("Foo constraint");
		final ComposedTypeModel productModel = typeService.getComposedType(ProductModel.class);
		constraint.setType(productModel);

		try
		{
			modelService.save(constraint);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}

	@Test
	public void testTypeAssignmentForInvalidModel()
	{
		final TypeConstraintModel constraint = modelService.create(TypeConstraintModel.class);
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setId("Foo constraint");
		constraint.setTarget(ProductModel.class);
		final ComposedTypeModel catalogModel = typeService.getComposedType(CatalogModel.class);
		constraint.setType(catalogModel);

		modelService.save(constraint);
		Assert.assertEquals(CatalogModel.class, constraint.getTarget());
	}

	@Test
	public void testTypeAssignmentForPojo()
	{
		final TypeConstraintModel constraint = modelService.create(TypeConstraintModel.class);
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setId("Foo constraint");
		constraint.setTarget(BeanOne.class);

		modelService.save(constraint);
		Assert.assertEquals(null, constraint.getType());
		Assert.assertEquals(BeanOne.class, constraint.getTarget());

		final ComposedTypeModel productModel = typeService.getComposedType(ProductModel.class);
		constraint.setType(productModel);

		modelService.save(constraint);
		Assert.assertEquals(ProductModel.class, constraint.getTarget());
	}

	@SuppressWarnings("unused")
	private class BeanTwo
	{
		private String attributeTwo;

		public void setAttributeTwo(final String attributeTwo)
		{
			this.attributeTwo = attributeTwo;
		}

		public String getAttributeTwo()
		{
			return attributeTwo;
		}
	}

	@SuppressWarnings("unused")
	private class BeanOne
	{
		private String attribute;

		public void setAttribute(final String attribute)
		{
			this.attribute = attribute;
		}

		public String getAttribute()
		{
			return attribute;
		}
	}
}
