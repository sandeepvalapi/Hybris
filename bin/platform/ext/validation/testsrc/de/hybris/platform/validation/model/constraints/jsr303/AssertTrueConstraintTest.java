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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing the AssertTrueConstraint.
 */
@IntegrationTest
public class AssertTrueConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: Language.active must be true
	 */
	private void createConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);

		final AssertTrueConstraintModel assertTrue = modelService.create(AssertTrueConstraintModel.class);
		assertTrue.setId("assertFalse");
		assertTrue.setSeverity(Severity.ERROR);
		assertTrue.setDescriptor(attrDesc);
		modelService.save(assertTrue);
		Assert.assertEquals(getDefaultMessage(Constraint.ASSERT_TRUE.msgKey), assertTrue.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * first model with false data, then constraint - then validation
	 */
	@Test
	public void testFalse1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.FALSE);
		modelService.save(lang);

		createConstraint();

		final Set<HybrisConstraintViolation> result = validationService.validate(lang);
		assertEquals(1, result.size());
	}

	/**
	 * first constraint, then model - then model save
	 */
	@Test
	public void testFalse2()
	{
		createConstraint();

		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.FALSE);

		try
		{
			modelService.save(lang);
			fail("AssertTrue constraint should be violated");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.ASSERT_TRUE.msgKey, "active");
		}
	}

	/**
	 * test positive example
	 */
	@Test
	public void testTrue1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);
		modelService.save(lang);

		createConstraint();

		assertTrue(validationService.validate(lang).isEmpty());
	}

	/**
	 * test positive example
	 */
	@Test
	public void testTrue2()
	{
		createConstraint();

		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);

		modelService.save(lang);
	}
}
