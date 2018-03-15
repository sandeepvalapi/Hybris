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

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Testing size Constraint
 */
@IntegrationTest
public class SizeConstraintTest extends AbstractConstraintTest
{
	LanguageModel fbl1 = null;
	LanguageModel fbl2 = null;

	/**
	 * need additional sample data for the collection test
	 */
	@Before
	public void createFallbackLangs()
	{
		fbl1 = new LanguageModel();
		fbl1.setActive(Boolean.TRUE);
		fbl1.setIsocode("fbl1");

		fbl2 = new LanguageModel();
		fbl2.setActive(Boolean.TRUE);
		fbl2.setIsocode("fbl2");

		modelService.saveAll(fbl1, fbl2);
	}

	/**
	 * Testsample: LanguageModel.ISOCODE (size between and including 5-8) and LanguageModel.FALLBACKLANGUAGES (between
	 * and including 1-1)
	 */
	private void createSizeConstraint()
	{
		final AttributeDescriptorModel attrDesc1 = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ISOCODE);
		final SizeConstraintModel sizeConstraint1 = modelService.create(SizeConstraintModel.class);
		sizeConstraint1.setId("sizeConstraint1");
		sizeConstraint1.setMin(Long.valueOf(5));
		sizeConstraint1.setMax(Long.valueOf(8));
		sizeConstraint1.setDescriptor(attrDesc1);

		final AttributeDescriptorModel attrDesc2 = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.FALLBACKLANGUAGES);
		final SizeConstraintModel sizeConstraint2 = modelService.create(SizeConstraintModel.class);
		sizeConstraint2.setId("sizeConstraint2");
		sizeConstraint2.setMin(Long.valueOf(1));
		sizeConstraint2.setMax(Long.valueOf(1));
		sizeConstraint2.setDescriptor(attrDesc2);

		modelService.saveAll(sizeConstraint1, sizeConstraint2);
		Assert.assertEquals(getDefaultMessage(Constraint.SIZE.msgKey), sizeConstraint1.getDefaultMessage());
		Assert.assertEquals(getDefaultMessage(Constraint.SIZE.msgKey), sizeConstraint2.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	@Test
	public void testSizeOk1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setActive(Boolean.TRUE);
		lang.setIsocode("123456");
		lang.setFallbackLanguages(Collections.singletonList(fbl1));
		modelService.save(lang);

		createSizeConstraint();
		assertTrue(validationService.validate(lang).isEmpty());
	}

	@Test
	public void testSizeOk2()
	{
		createSizeConstraint();

		final LanguageModel lang = new LanguageModel();
		lang.setActive(Boolean.TRUE);
		lang.setIsocode("123456");
		lang.setFallbackLanguages(Collections.singletonList(fbl1));
		modelService.save(lang);

		assertTrue(validationService.validate(lang).isEmpty());
	}

	@Test
	public void testSizeFailIsoCode1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setActive(Boolean.TRUE);
		lang.setIsocode("1234");
		lang.setFallbackLanguages(Collections.singletonList(fbl1));
		modelService.save(lang);

		createSizeConstraint();
		assertEquals(1, validationService.validate(lang).size());

		lang.setIsocode("123456789");
		assertEquals(1, validationService.validate(lang).size());
	}

	@Test
	public void testSizeFailIsoCode2()
	{
		createSizeConstraint();
		final LanguageModel lang = new LanguageModel();
		lang.setActive(Boolean.TRUE);
		lang.setFallbackLanguages(Collections.singletonList(fbl1));

		try
		{
			lang.setIsocode("1234");
			modelService.save(lang);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.SIZE.msgKey, "isocode");
		}
		///////////////////////////////
		try
		{
			lang.setIsocode("123456789");
			modelService.save(lang);
			fail("test should fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.SIZE.msgKey, "isocode");
		}

	}

	@Test
	public void testSizeFailFallBackLanguagesAndIsoCode1()
	{
		final LanguageModel lang = new LanguageModel();
		lang.setActive(Boolean.TRUE);
		lang.setIsocode("1234");
		lang.setFallbackLanguages(Collections.EMPTY_LIST);
		modelService.save(lang);

		createSizeConstraint();
		assertEquals(2, validationService.validate(lang).size());

		lang.setIsocode("1234567890");
		lang.setFallbackLanguages(Arrays.asList(fbl1, fbl2));
		assertEquals(2, validationService.validate(lang).size());
	}

}
