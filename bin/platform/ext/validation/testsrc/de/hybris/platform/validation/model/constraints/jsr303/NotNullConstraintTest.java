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
 * Testing the NotNull Constraint
 */
@IntegrationTest
public class NotNullConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: Language.active must not be null
	 */
	private void createNullConstraintForBoolean()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);

		final NotNullConstraintModel notNullConstraint = modelService.create(NotNullConstraintModel.class);
		notNullConstraint.setId("notNullConstraint");
		notNullConstraint.setDescriptor(attrDesc);
		modelService.save(notNullConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.NOT_NULL.msgKey), notNullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * Testsample: Customer.description must not be null
	 */
	private void createConstraintForString()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CustomerModel.class), CustomerModel.DESCRIPTION);
		final NotNullConstraintModel notNullConstraint = modelService.create(NotNullConstraintModel.class);
		notNullConstraint.setId("nullConstraint");
		notNullConstraint.setDescriptor(attrDesc);
		modelService.save(notNullConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.NOT_NULL.msgKey), notNullConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	@Test
	public void testBooleanNotNullok()
	{
		final LanguageModel lang = modelService.create(LanguageModel.class);
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);
		assertEquals(Boolean.TRUE, lang.getActive());
		modelService.save(lang);
		createNullConstraintForBoolean();
		assertTrue(validationService.validate(lang).isEmpty());
	}

	@Test
	public void testStringNullfails()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription(null);
		assertNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		final Set<HybrisConstraintViolation> result = validationService.validate(cust);
		assertEquals(1, result.size());
	}

	@Test
	public void testStringNullok()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription("moo");
		assertNotNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		assertTrue(validationService.validate(cust).isEmpty());
	}

	@Test
	public void testEmptyStringNullok()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("xxx");
		cust.setDescription("");
		assertNotNull(cust.getDescription());
		modelService.save(cust);
		createConstraintForString();
		assertTrue(validationService.validate(cust).isEmpty());
	}
}
