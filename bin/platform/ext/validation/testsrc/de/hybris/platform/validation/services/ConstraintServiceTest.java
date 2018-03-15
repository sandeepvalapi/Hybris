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
package de.hybris.platform.validation.services;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.Null;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class ConstraintServiceTest extends AbstractConstraintTest
{
	@Resource
	protected ConstraintService constraintService;

	@Test
	public void testFetchConstraints()
	{
		final AttributeConstraintModel attributeOne = modelService.create(AttributeConstraintModel.class);
		attributeOne.setId("one");
		attributeOne.setAnnotation(javax.validation.constraints.Null.class);

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);
		attributeOne.setDescriptor(descrModel);

		modelService.save(attributeOne);

		final TypeConstraintModel typeOne = modelService.create(TypeConstraintModel.class);
		typeOne.setId("two");
		typeOne.setAnnotation(javax.validation.constraints.Null.class);
		final ComposedTypeModel catalogCompsedTypeModel = typeService.getComposedType(CatalogModel.class);
		typeOne.setType(catalogCompsedTypeModel);
		modelService.save(typeOne);
		final List<ComposedTypeModel> composedTypes = constraintService.getConstraintedComposedTypes();
		Assert.assertEquals(2, composedTypes.size());

		for (final ComposedTypeModel ctModel : composedTypes)
		{
			Assert.assertTrue(!ctModel.getConstraints().isEmpty());
		}
	}

	@Test
	public void testFetchPojos()
	{
		final AttributeConstraintModel attributeOne = modelService.create(AttributeConstraintModel.class);
		attributeOne.setId("one");
		attributeOne.setAnnotation(javax.validation.constraints.Null.class);
		attributeOne.setTarget(PojoTwo.class);
		attributeOne.setQualifier("oneAttribute");

		modelService.save(attributeOne);

		final TypeConstraintModel typeOne = modelService.create(TypeConstraintModel.class);
		typeOne.setId("two");
		typeOne.setAnnotation(javax.validation.constraints.Null.class);
		typeOne.setTarget(PojoOne.class);

		modelService.save(typeOne);

		final List<AbstractConstraintModel> constraint = constraintService.getPojoRelatedConstraints();

		Assert.assertEquals(2, constraint.size());

		for (final AbstractConstraintModel ctModel : constraint)
		{
			Assert.assertTrue(ctModel.getAnnotation().equals(Null.class));
		}
	}

	@SuppressWarnings("unused")
	private class PojoOne
	{
		private Date oneAttribute;

		public void setOneAttribute(final Date oneAttribute)
		{
			this.oneAttribute = oneAttribute;
		}

		public Date getOneAttribute()
		{
			return oneAttribute;
		}
	}

	@SuppressWarnings("unused")
	private class PojoTwo extends PojoOne
	{
		private Date twoAttribute;

		public void setTwoAttribute(final Date twoAttribute)
		{
			this.twoAttribute = twoAttribute;
		}

		public Date getTwoAttribute()
		{
			return twoAttribute;
		}
	}
}
