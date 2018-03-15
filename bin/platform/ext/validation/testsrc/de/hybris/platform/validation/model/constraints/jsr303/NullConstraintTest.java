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
package de.hybris.platform.validation.model.constraints.jsr303;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing Null constraint
 */
@IntegrationTest
public class NullConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: Language.active must be null
	 */
	private void createNullConstraintForBoolean()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);

		final NullConstraintModel nullConstraint = modelService.create(NullConstraintModel.class);
		modelService.initDefaults(nullConstraint);
		nullConstraint.setId("nullConstraint");
		nullConstraint.setDescriptor(attrDesc);
		modelService.save(nullConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.NULL.msgKey), nullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * Testsample: Customer.description must be null
	 */
	private void createConstraintForString()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CustomerModel.class), CustomerModel.DESCRIPTION);
		final NullConstraintModel nullConstraint = new NullConstraintModel();
		modelService.initDefaults(nullConstraint);
		nullConstraint.setId("nullConstraint");
		nullConstraint.setDescriptor(attrDesc);
		modelService.save(nullConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.NULL.msgKey), nullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	@Test
	public void testBooleanNullfails()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);
		assertEquals(Boolean.TRUE, lang.getActive());
		modelService.save(lang);
		createNullConstraintForBoolean();
		final Set<HybrisConstraintViolation> result = validationService.validate(lang);
		assertEquals(1, result.size());
	}

	@Test
	public void testBooleanNullok()
	{
		final POJO pojo = new POJO();
		final NullConstraintModel nullConstraint1 = new NullConstraintModel();
		modelService.initDefaults(nullConstraint1);
		nullConstraint1.setId("nullConstraint");
		nullConstraint1.setTarget(POJO.class);
		nullConstraint1.setQualifier("bool");
		modelService.save(nullConstraint1);
		validationService.reloadValidationEngine();
		assertNull(pojo.getBool());
		assertTrue(validationService.validate(pojo).isEmpty());
	}

	@Test
	public void testStringNullok()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription(null);
		assertNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		assertTrue(validationService.validate(cust).isEmpty());
	}

	@Test
	public void testStringNullfails()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription("moo");
		assertNotNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		final Set<HybrisConstraintViolation> result = validationService.validate(cust);
		assertEquals(1, result.size());
	}

	@Test
	public void testEmptyStringNullfails()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription("");
		assertNotNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		final Set<HybrisConstraintViolation> result = validationService.validate(cust);
		assertEquals(1, result.size());
	}

	protected class POJO
	{
		private Boolean bool;

		public void setBool(final Boolean bool)
		{
			this.bool = bool;
		}

		public Boolean getBool()
		{
			return bool;
		}
	}
}
