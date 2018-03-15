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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.MaxConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.MinConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.junit.Test;


/**
 * Set of mixed constraints test atatched to POJO.
 */
@IntegrationTest
public class GenericConstraintsTest extends AbstractConstraintTest
{
	@Test
	public void testMinMaxConstraintWithPojoObject()
	{
		final MinConstraintModel minConstraint = modelService.create(MinConstraintModel.class);
		minConstraint.setId("minConstraint");
		minConstraint.setQualifier("intValue");
		minConstraint.setValue(Long.valueOf(1));
		minConstraint.setTarget(PojoObject.class);

		final MaxConstraintModel maxConstraint = modelService.create(MaxConstraintModel.class);
		maxConstraint.setId("maxConstraint");
		maxConstraint.setQualifier("intValue");
		maxConstraint.setValue(Long.valueOf(10));
		maxConstraint.setTarget(PojoObject.class);

		modelService.saveAll(minConstraint, maxConstraint);
		validationService.reloadValidationEngine();

		final PojoObject pojo = new PojoObject();
		pojo.setIntValue(-1);
		assertEquals(1, validationService.validate(pojo).size());

		pojo.setIntValue(1);
		assertTrue(validationService.validate(pojo).isEmpty());

		pojo.setIntValue(10);
		assertTrue(validationService.validate(pojo).isEmpty());

		pojo.setIntValue(11);
		assertEquals(1, validationService.validate(pojo).size());
	}

	@Test
	public void testMinMaxConstraintWithPojoObjectAfterDetach()
	{
		final MinConstraintModel minConstraint = modelService.create(MinConstraintModel.class);
		minConstraint.setId("minConstraint");
		minConstraint.setQualifier("intValue");
		minConstraint.setValue(Long.valueOf(1));
		minConstraint.setTarget(PojoObject.class);

		final MaxConstraintModel maxConstraint = modelService.create(MaxConstraintModel.class);
		maxConstraint.setId("maxConstraint");
		maxConstraint.setQualifier("intValue");
		maxConstraint.setValue(Long.valueOf(10));
		maxConstraint.setTarget(PojoObject.class);

		modelService.saveAll(minConstraint, maxConstraint);
		validationService.unloadValidationEngine();

		final PojoObject pojo = new PojoObject();
		pojo.setIntValue(-1);
		assertEquals("should not occur any validation violation after detach", 0, validationService.validate(pojo).size());

		pojo.setIntValue(1);
		assertTrue("should not occur any validation violation after detach", validationService.validate(pojo).isEmpty());

		pojo.setIntValue(10);
		assertTrue("should not occur any validation violation after detach", validationService.validate(pojo).isEmpty());

		pojo.setIntValue(11);
		assertEquals("should not occur any validation violation after detach", 0, validationService.validate(pojo).size());
	}

	@Test
	public void testNestedConstraintAttributeLoad()
	{

		Set<HybrisConstraintViolation> constraintViolations = null;
		final SizeConstraintModel sizeConstraint = modelService.create(SizeConstraintModel.class);
		sizeConstraint.setId("oneNested");
		sizeConstraint.setAnnotation(javax.validation.constraints.Size.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);
		sizeConstraint.setDescriptor(descrModel);
		sizeConstraint.setMin(Long.valueOf(10));
		sizeConstraint.setMax(Long.valueOf(20));

		final PatternConstraintModel patternConstraint = modelService.create(PatternConstraintModel.class);
		patternConstraint.setId("twoNested");
		patternConstraint.setAnnotation(javax.validation.constraints.Pattern.class);
		patternConstraint.setDescriptor(descrModel);
		patternConstraint.setFlags(Collections.singleton(de.hybris.platform.validation.enums.RegexpFlag.DOTALL));
		patternConstraint.setRegexp("code.+ong");

		//check builder from type system
		modelService.saveAll(sizeConstraint, patternConstraint);
		validationService.reloadValidationEngine(); //reload

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCatalogVersion(null);
		product.setCode("code"); //won't fit both constraints 		
		product.setDescription("some description");

		constraintViolations = validationService.validate(product);
		assertEquals(2, constraintViolations.size());
		product.setCode("codeLong"); //won't fit too short
		constraintViolations = validationService.validate(product);
		assertEquals(1, constraintViolations.size());
		product.setCode("codeVeryLong"); //ok
		constraintViolations = validationService.validate(product);
		assertEquals(0, constraintViolations.size());
	}

	@Test
	public void testNestedConstraintAttributeLoadAfterDetach()
	{
		Set<HybrisConstraintViolation> constraintViolations = null;
		final SizeConstraintModel sizeConstraint = modelService.create(SizeConstraintModel.class);
		sizeConstraint.setId("oneNested");
		sizeConstraint.setAnnotation(javax.validation.constraints.Size.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);
		sizeConstraint.setDescriptor(descrModel);
		sizeConstraint.setMin(Long.valueOf(10));
		sizeConstraint.setMax(Long.valueOf(20));

		final PatternConstraintModel patternConstraint = modelService.create(PatternConstraintModel.class);
		patternConstraint.setId("twoNested");
		patternConstraint.setAnnotation(javax.validation.constraints.Pattern.class);
		patternConstraint.setDescriptor(descrModel);
		patternConstraint.setFlags(Collections.singleton(de.hybris.platform.validation.enums.RegexpFlag.DOTALL));
		patternConstraint.setRegexp("code.+ong");

		//check builder from type system
		modelService.saveAll(sizeConstraint, patternConstraint);
		validationService.unloadValidationEngine(); //reload

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCatalogVersion(null);
		product.setCode("code"); //won't fit both constraints 		
		product.setDescription("some description");

		constraintViolations = validationService.validate(product);
		assertEquals("should not occur any validation violation after detach", 0, constraintViolations.size());
		product.setCode("codeLong"); //won't fit too short
		constraintViolations = validationService.validate(product);
		assertEquals("should not occur any validation violation after detach", 0, constraintViolations.size());
		product.setCode("codeVeryLong"); //ok
		constraintViolations = validationService.validate(product);
		assertEquals("should not occur any validation violation after detach", 0, constraintViolations.size());
	}

	@SuppressWarnings("unused")
	private class PojoObject
	{
		private int intValue;
		private Date dateValue;
		private String stringValue;

		public void setIntValue(final int intValue)
		{
			this.intValue = intValue;
		}

		public int getIntValue()
		{
			return intValue;
		}

		public void setDateValue(final Date dateValue)
		{
			this.dateValue = dateValue;
		}

		public Date getDateValue()
		{
			return dateValue;
		}

		public void setStringValue(final String stringValue)
		{
			this.stringValue = stringValue;
		}

		public String getStringValue()
		{
			return stringValue;
		}
	}
}
