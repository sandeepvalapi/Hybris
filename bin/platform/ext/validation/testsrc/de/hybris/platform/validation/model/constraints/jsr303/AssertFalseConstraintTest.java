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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing the AssertFalseConstraint.
 */
@IntegrationTest
public class AssertFalseConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: Language.active must be false
	 */
	private void createConstraint()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);

		final AssertFalseConstraintModel assertFalse = modelService.create(AssertFalseConstraintModel.class);
		assertFalse.setId("assertFalse");
		assertFalse.setSeverity(Severity.ERROR);
		assertFalse.setDescriptor(attrDesc);
		modelService.save(assertFalse);
		Assert.assertEquals(getDefaultMessage(Constraint.ASSERT_FALSE.msgKey), assertFalse.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * create first the model with false data, then the constraint - then validate
	 */
	@Test
	public void testTrue1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);
		modelService.save(lang);

		createConstraint();

		final Set<HybrisConstraintViolation> result = validationService.validate(lang);
		assertEquals(1, result.size());
	}

	/**
	 * Like {@link #testTrue1()} but the constraint is created first.
	 */
	@Test
	public void testTrue2()
	{
		createConstraint();

		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("xxx");
		lang.setActive(Boolean.TRUE);

		try
		{
			modelService.save(lang);
			fail("AssertFalse constraint should be violated");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.ASSERT_FALSE.msgKey, "active");
		}
	}

	/**
	 * thest the false values - a violation must happend
	 */
	@Test
	public void testFalse1()
	{
		final LanguageModel lang = modelService.create(LanguageModel.class);
		lang.setIsocode("xxx");
		lang.setActive(Boolean.FALSE);
		modelService.save(lang);

		createConstraint();

		assertTrue(validationService.validate(lang).isEmpty());
	}

	/**
	 * same as {@link #testFalse2()}
	 */
	@Test
	public void testFalse2()
	{
		createConstraint();

		final LanguageModel lang = modelService.create(LanguageModel.class);
		lang.setIsocode("xxx");
		lang.setActive(Boolean.FALSE);

		modelService.save(lang);
	}

	@Test
	public void testDefaultActiveValue1()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.BASE);

		final AssertFalseConstraintModel assertFalse1 = modelService.create(AssertFalseConstraintModel.class);
		assertFalse1.setId("assertFalse");
		assertFalse1.setSeverity(Severity.ERROR);
		assertFalse1.setDescriptor(attrDesc);
		modelService.save(assertFalse1);

		assertTrue(assertFalse1.isActive());
	}

	@Test
	public void testDefaultActiveValue2()
	{
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.BASE);

		final AssertFalseConstraintModel af1 = new AssertFalseConstraintModel();
		af1.setId("assertFalse");
		af1.setSeverity(Severity.ERROR);
		af1.setDescriptor(attrDesc);

		assertFalse(af1.isActive());
		modelService.initDefaults(af1);
		assertTrue(af1.isActive());
		modelService.save(af1);
		assertTrue(af1.isActive());
	}
}
