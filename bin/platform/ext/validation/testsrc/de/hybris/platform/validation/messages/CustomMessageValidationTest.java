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
package de.hybris.platform.validation.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.AssertFalseConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertTrueConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.MinConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit Tests for the Validation extension
 */
@IntegrationTest
public class CustomMessageValidationTest extends AbstractConstraintTest
{
	private static final String FIELD_0_IN_TYPE_1_MUST_BE_BETWEEN_THE_SPECIFIED_BOUNDARIES_4_5 = "Field ''{0}'' in type ''{1}'' must be between the specified boundaries ({4}-{5}).";
	private static final String FIELD_0_IN_TYPE_1_CAN_NOT_BE_NULL = "Field ''{0}'' in type ''{1}'' can not be null.";
	private static final String CONSTRAINT_VIOLATIONS_SIZE_MESSAGE = "Wrong number of elements in constraintViolations. Should be: ";
	private static final Logger LOG = Logger.getLogger(CustomMessageValidationTest.class.getName());

	@Before
	public void prepareConstraints()
	{
		final NotNullConstraintModel notNullCategory = modelService.create(NotNullConstraintModel.class);
		notNullCategory.setId("one");
		notNullCategory.setAnnotation(javax.validation.constraints.NotNull.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(CategoryModel.class), CategoryModel.ORDER);
		notNullCategory.setDescriptor(descrModel);

		final NotNullConstraintModel notNullUnit = modelService.create(NotNullConstraintModel.class);
		notNullUnit.setId("two");
		notNullUnit.setAnnotation(javax.validation.constraints.NotNull.class);
		final AttributeDescriptorModel descrModelUnit = typeService.getAttributeDescriptor(
				typeService.getComposedType(UnitModel.class), UnitModel.CODE);
		notNullUnit.setDescriptor(descrModelUnit);

		final NotNullConstraintModel notNullProduct = modelService.create(NotNullConstraintModel.class);
		notNullProduct.setId("three");
		notNullProduct.setAnnotation(javax.validation.constraints.NotNull.class);
		final AttributeDescriptorModel descrModelProduct = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);
		notNullProduct.setMessage(FIELD_0_IN_TYPE_1_CAN_NOT_BE_NULL);
		notNullProduct.setDescriptor(descrModelProduct);

		final SizeConstraintModel sizeProduct = modelService.create(SizeConstraintModel.class);
		sizeProduct.setId("four");
		sizeProduct.setAnnotation(javax.validation.constraints.Size.class);
		sizeProduct.setMin(Long.valueOf(1));
		sizeProduct.setMax(Long.valueOf(Integer.MAX_VALUE));
		final AttributeDescriptorModel sizedescrModelProduct = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.NAME);
		sizeProduct.setDescriptor(sizedescrModelProduct);
		sizeProduct.setMessage(FIELD_0_IN_TYPE_1_MUST_BE_BETWEEN_THE_SPECIFIED_BOUNDARIES_4_5);

		final SizeConstraintModel sizeUnit = modelService.create(SizeConstraintModel.class);
		sizeUnit.setId("five");
		sizeUnit.setAnnotation(javax.validation.constraints.Size.class);
		sizeUnit.setMin(Long.valueOf(1));
		sizeUnit.setMax(Long.valueOf(Integer.MAX_VALUE));
		final AttributeDescriptorModel sizedescrModelUnit = typeService.getAttributeDescriptor(
				typeService.getComposedType(UnitModel.class), UnitModel.NAME);
		sizeUnit.setDescriptor(sizedescrModelUnit);

		modelService.saveAll(notNullCategory, notNullUnit, notNullProduct, sizeProduct, sizeUnit);
	}

	/**
	 * Tests both constraint for {@link ProductModel} not null for the {@link ProductModel#CODE} ,size constrain for a
	 * {@link ProductModel#NAME} ( warn name must be not null )
	 */
	@Test
	public void validateProductModel()
	{
		validationService.reloadValidationEngine();

		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setName("");//name length 0
		Set<HybrisConstraintViolation> constraintViolations = validationService.validate(productModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 2, 2, constraintViolations.size());
		checkViolations(FIELD_0_IN_TYPE_1_CAN_NOT_BE_NULL, constraintViolations);
		checkViolations(FIELD_0_IN_TYPE_1_MUST_BE_BETWEEN_THE_SPECIFIED_BOUNDARIES_4_5, constraintViolations);

		productModel.setCode("code");
		constraintViolations = validationService.validate(productModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 1, 1, constraintViolations.size());
		checkViolations(FIELD_0_IN_TYPE_1_MUST_BE_BETWEEN_THE_SPECIFIED_BOUNDARIES_4_5, constraintViolations);
	}

	/**
	 * Tests both constraint for {@link UnitModel} not null for the {@link UnitModel#CODE} ,size constrain for a
	 * {@link UnitModel#NAME} ( warn name must be not null )
	 */
	@Test
	public void validateUnitModel()
	{
		validationService.reloadValidationEngine();

		final UnitModel unitModel = modelService.create(UnitModel.class);
		unitModel.setName("");//name length 0

		Set<HybrisConstraintViolation> constraintViolations = validationService.validate(unitModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 2, 2, constraintViolations.size());

		unitModel.setCode("code");
		unitModel.setName("foo unit");
		constraintViolations = validationService.validate(unitModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 0, 0, constraintViolations.size());
	}



	@Test
	public void validateCategoryModel()
	{
		validationService.reloadValidationEngine();

		final CategoryModel categoryModel = modelService.create(CategoryModel.class);

		Set<HybrisConstraintViolation> constraintViolations = validationService.validate(categoryModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 1, 1, constraintViolations.size());

		categoryModel.setOrder(Integer.valueOf(1));
		constraintViolations = validationService.validate(categoryModel);
		assertEquals(CONSTRAINT_VIOLATIONS_SIZE_MESSAGE + 0, 0, constraintViolations.size());
	}

	//explaining HOR-837
	@Test
	public void testNonPrimitivePojo()
	{
		final AssertTrueConstraintModel constraint1 = modelService.create(AssertTrueConstraintModel.class);
		constraint1.setId("onePrmitiveTrue");
		constraint1.setQualifier("nonPrimitiveBoolean");
		constraint1.setTarget(PojoNonPrimitive.class);

		modelService.save(constraint1);

		final AssertFalseConstraintModel constraint2 = modelService.create(AssertFalseConstraintModel.class);
		constraint2.setId("onePrmitiveFalse");
		constraint2.setQualifier("nonPrimitiveBoolean");
		constraint2.setTarget(PojoNonPrimitive.class);

		modelService.save(constraint2);

		final MinConstraintModel constraint3 = modelService.create(MinConstraintModel.class);
		constraint3.setId("onePrmitiveMin");
		constraint3.setQualifier("nonPrimitiveInt");
		constraint3.setTarget(PojoNonPrimitive.class);
		constraint3.setValue(Long.valueOf(10));

		modelService.save(constraint3);

		validationService.reloadValidationEngine();

		final PojoNonPrimitive pojo = new PojoNonPrimitive();

		final Set<HybrisConstraintViolation> violations = validationService.validate(pojo);

		Assert.assertEquals("Because of initial values for a non primitives we will have 0 vilations", 0, violations.size());
	}

	//explaining HOR-837
	@Test
	public void testPrimitivePojo()
	{
		final AssertTrueConstraintModel constraint1 = modelService.create(AssertTrueConstraintModel.class);
		constraint1.setId("onePrmitiveTrue");
		constraint1.setQualifier("primitiveBoolean");
		constraint1.setTarget(PojoPrimitive.class);

		modelService.save(constraint1);

		final AssertFalseConstraintModel constraint2 = modelService.create(AssertFalseConstraintModel.class);
		constraint2.setId("onePrmitiveFalse");
		constraint2.setQualifier("primitiveBoolean");
		constraint2.setTarget(PojoPrimitive.class);

		modelService.save(constraint2);

		final MinConstraintModel constraint3 = modelService.create(MinConstraintModel.class);
		constraint3.setId("onePrmitiveMin");
		constraint3.setQualifier("primitiveInt");
		constraint3.setTarget(PojoPrimitive.class);
		constraint3.setValue(Long.valueOf(10));

		modelService.save(constraint3);

		validationService.reloadValidationEngine();

		final PojoPrimitive pojo = new PojoPrimitive();

		final Set<HybrisConstraintViolation> violations = validationService.validate(pojo);

		Assert.assertEquals("Because of initial values for a primitives we will have 2 violations", 2, violations.size());
	}


	protected class PojoNonPrimitive
	{
		Boolean nonPrimitiveBoolean;

		public void setNonPrimitiveBoolean(final Boolean primitive)
		{
			this.nonPrimitiveBoolean = primitive;
		}

		public Boolean getNonPrimitiveBoolean()
		{
			return this.nonPrimitiveBoolean;
		}

		Integer nonPrimitiveInt;

		public void setNonPrimitiveInt(final Integer primitive)
		{
			this.nonPrimitiveInt = primitive;
		}

		public Integer getNonPrimitiveInt()
		{
			return this.nonPrimitiveInt;
		}
	}


	protected class PojoPrimitive
	{
		boolean primitiveBoolean;

		public void setPrimitiveBoolean(final boolean primitive)
		{
			this.primitiveBoolean = primitive;
		}

		public boolean getPrimitiveBoolean() //NOPMD
		{
			return this.primitiveBoolean;
		}

		int primitiveInt;

		public void setPrimitiveInt(final int primitive)
		{
			this.primitiveInt = primitive;
		}

		public int getPrimitiveInt()
		{
			return this.primitiveInt;
		}
	}

	protected <T> void checkViolations(final String expectedMessage, final Set<HybrisConstraintViolation> constraintViolations)
	{
		assertTrue(expectedMessage, doesMessageExist(expectedMessage, constraintViolations));
	}

	protected <T> boolean doesMessageExist(final String expectedMessage, final Set<HybrisConstraintViolation> constraintViolations)
	{
		for (final HybrisConstraintViolation constraintViolation : constraintViolations)
		{
			if (expectedMessage.equals(constraintViolation.getLocalizedMessage()))
			{
				return true;
			}
		}
		LOG.info("---------------Messages from current violation----------------");
		for (final HybrisConstraintViolation constraintViolation : constraintViolations)
		{
			LOG.info(constraintViolation.getLocalizedMessage());
		}

		return false;
	}

}
