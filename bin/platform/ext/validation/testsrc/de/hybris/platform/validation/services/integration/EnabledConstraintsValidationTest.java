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
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Integration test for checking {@link AbstractConstraintModel#ACTIVE} flag.
 */
@IntegrationTest
public class EnabledConstraintsValidationTest extends CommonIntegrationValidationTest
{
	@Test
	public void testViolationsEnabled()
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
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.WARN),
					new ViolationCompositeImpl(TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10, Severity.ERROR) });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsEnabled2()
	{
		nullProduct.setSeverity(Severity.ERROR);
		sizeTitle.setSeverity(Severity.WARN);
		sizeTitle.setActive(false);
		patternConstraint.setSeverity(Severity.ERROR);

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
			{ new ViolationCompositeImpl(TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG, Severity.ERROR), });

			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();
				checkIfContains(expected, vve.getHybrisConstraintViolations());
			}
		}
	}

	@Test
	public void testViolationsEnabled3()
	{
		nullProduct.setSeverity(Severity.INFO);
		sizeTitle.setSeverity(Severity.WARN);
		patternConstraint.setSeverity(Severity.ERROR);
		patternConstraint.setActive(false);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title);
		}
		catch (final ModelSavingException mse)
		{
			Assert.fail("All constraints are marked not active ");
		}
	}

	@Test
	public void testViolationsEnabled4()
	{
		nullProduct.setSeverity(Severity.ERROR);
		nullProduct.setActive(false);
		sizeTitle.setSeverity(Severity.ERROR);
		sizeTitle.setActive(false);
		patternConstraint.setSeverity(Severity.ERROR);
		patternConstraint.setActive(false);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title);
		}
		catch (final ModelSavingException mse)
		{
			Assert.fail("All constraints are marked not active ");
		}
	}

	@Test
	public void testViolationsEnabled5()
	{
		nullProduct.setSeverity(Severity.INFO);
		sizeTitle.setSeverity(Severity.INFO);
		patternConstraint.setSeverity(Severity.INFO);
		modelService.saveAll(nullProduct, sizeTitle, patternConstraint);
		validationService.reloadValidationEngine(); //reload
		try
		{
			modelService.save(title);
			//Assert.fail("Pattern  validation should  violate,  it has error severity");
		}
		catch (final ModelSavingException mse)
		{
			Assert.fail("All constraints are marked not active ");
		}
	}
}
