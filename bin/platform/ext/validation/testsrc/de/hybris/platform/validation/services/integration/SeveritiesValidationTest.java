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
package de.hybris.platform.validation.services.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for checking {@link AbstractConstraintModel#SEVERITY} feature.
 */
@IntegrationTest
public class SeveritiesValidationTest extends CommonIntegrationValidationTest
{
	private NullConstraintModel nullProduct;
	private SizeConstraintModel sizeTitle;
	private PatternConstraintModel patternConstraint;
	private TitleModel title;

	@Override
	@Before
	public void setUp()
	{
		nullProduct = modelService.create(NullConstraintModel.class);
		nullProduct.setId("one info");

		final AttributeDescriptorModel descrModelTitle = typeService.getAttributeDescriptor(
				typeService.getComposedType(TitleModel.class), TitleModel.CODE);
		nullProduct.setSeverity(Severity.INFO);
		nullProduct.setDescriptor(descrModelTitle);

		sizeTitle = modelService.create(SizeConstraintModel.class);
		sizeTitle.setId("two error");

		sizeTitle.setMin(Long.valueOf(1));
		sizeTitle.setMax(Long.valueOf(10));
		sizeTitle.setDescriptor(descrModelTitle);
		sizeTitle.setSeverity(Severity.ERROR);


		patternConstraint = modelService.create(PatternConstraintModel.class);
		patternConstraint.setId("three warn");

		patternConstraint.setDescriptor(descrModelTitle);
		patternConstraint.setFlags(Collections.singleton(de.hybris.platform.validation.enums.RegexpFlag.DOTALL));
		patternConstraint.setRegexp("code.+ong");
		patternConstraint.setSeverity(Severity.WARN);

		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);

		title = modelService.create(TitleModel.class);
		title.setCode("codeVeryButButVeryLongWithMe");
	}

	@Test
	public void testViolationsSeverities()
	{
		validationService.reloadValidationEngine(); //reload

		try
		{
			modelService.save(title); // 
			Assert.fail("Size validation should violate it has error severity");
		}
		catch (final ModelSavingException mse)
		{

			final List<ViolationComposite> expected = Arrays.asList(new ViolationComposite[]
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10, Severity.ERROR),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.WARN),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_BE_NULL, Severity.INFO), });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsSeverities2()
	{
		nullProduct.setSeverity(Severity.ERROR);
		sizeTitle.setSeverity(Severity.WARN);
		patternConstraint.setSeverity(Severity.INFO);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title); //
			Assert.fail("Null validation should violate it has error severity");
		}
		catch (final ModelSavingException mse)
		{
			//Assert.assertEquals("'TitleModel.code' must be null\n", mse.getMessage());
			final List<ViolationComposite> expected = Arrays.asList(new ViolationComposite[]
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10, Severity.WARN),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.INFO),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_BE_NULL, Severity.ERROR), });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsSeverities3()
	{
		nullProduct.setSeverity(Severity.INFO);
		sizeTitle.setSeverity(Severity.WARN);
		patternConstraint.setSeverity(Severity.ERROR);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title);
			Assert.fail("Pattern  validation should  violate,  it has error severity");
		}
		catch (final ModelSavingException mse)
		{
			final List<ViolationComposite> expected = Arrays.asList(new ViolationComposite[]
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10, Severity.WARN),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.ERROR),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_BE_NULL, Severity.INFO), });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsSeverities4()
	{
		nullProduct.setSeverity(Severity.ERROR);
		sizeTitle.setSeverity(Severity.ERROR);
		patternConstraint.setSeverity(Severity.ERROR);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title);
		}
		catch (final ModelSavingException mse)
		{
			final List<ViolationComposite> expected = Arrays.asList(new ViolationComposite[]
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10, Severity.ERROR),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.ERROR),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_BE_NULL, Severity.ERROR), });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsSeveritiesWithoutErrors()
	{
		nullProduct.setSeverity(Severity.INFO);
		sizeTitle.setSeverity(Severity.INFO);
		patternConstraint.setSeverity(Severity.INFO);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		modelService.save(title);
	}
}
