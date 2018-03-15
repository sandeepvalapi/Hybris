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

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;


/**
 * Common class for integration test.
 */
@Ignore
public abstract class CommonIntegrationValidationTest extends AbstractConstraintTest
{
	protected final String TITLE_MODEL_CODE_MUST_BE_NULL = "{javax.validation.constraints.Null.message}";
	protected final String TITLE_MODEL_CODE_MUST_MATCH_CODE_ONG = "{javax.validation.constraints.Pattern.message}";
	protected final String TITLE_MODEL_CODE_SIZE_MUST_BE_BETWEEN_1_AND_10 = "{javax.validation.constraints.Size.message}";
	protected NullConstraintModel nullProduct;
	protected SizeConstraintModel sizeTitle;
	protected PatternConstraintModel patternConstraint;
	protected TitleModel title;

	@Before
	public void setUp()
	{
		nullProduct = modelService.create(NullConstraintModel.class);
		nullProduct.setId("one info");

		final AttributeDescriptorModel descrModelTitle = typeService.getAttributeDescriptor(
				typeService.getComposedType(TitleModel.class), TitleModel.CODE);
		nullProduct.setSeverity(Severity.INFO);
		nullProduct.setDescriptor(descrModelTitle);
		nullProduct.setActive(false);

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

	protected void checkIfContains(final List<ViolationComposite> expectedViolations,
			final Collection<HybrisConstraintViolation> gotViolations)
	{
		final List<HybrisConstraintViolation> matchedViolations = new ArrayList<HybrisConstraintViolation>();
		final List<HybrisConstraintViolation> unMatchedViolations = new ArrayList<HybrisConstraintViolation>();


		for (final HybrisConstraintViolation gotViol : gotViolations)
		{
			boolean found = false;
			for (final ViolationComposite toCheck : expectedViolations)
			{
				if (gotViol.getMessageTemplate().equals(toCheck.getMessage())
						&& gotViol.getViolationSeverity().equals(toCheck.getSeverity()))
				{
					found = true;
					break;
				}
			}
			if (found)
			{
				matchedViolations.add(gotViol);
			}
			else
			{
				unMatchedViolations.add(gotViol);
			}
		}

		if (!unMatchedViolations.isEmpty())
		{
			final StringBuilder builder = new StringBuilder();
			if (expectedViolations.size() != gotViolations.size())
			{
				builder.append("different size between expectedViolations and gotViolations: " + expectedViolations.size() + " to "
						+ gotViolations.size() + "\n");
			}
			builder.append("matched size: " + matchedViolations.size() + " unmatched size: " + unMatchedViolations.size() + "\n");
			builder.append("Got following violations:\n");

			for (final HybrisConstraintViolation hcv : gotViolations)
			{
				builder.append("\t" + hcv.getViolationSeverity() + ": " + hcv.getMessageTemplate() + "\n");
			}
			builder.append("Expected following violations:\n");
			for (final ViolationComposite vc : expectedViolations)
			{
				builder.append("\t" + vc.getSeverity() + " - " + vc.getMessage() + "\n");
			}
			Assert.fail("Not expected violations found : " + builder.toString());
		}
	}

	protected interface ViolationComposite
	{
		Severity getSeverity();

		String getMessage();
	}

	protected class ViolationCompositeImpl implements ViolationComposite
	{
		private final String message;
		private final Severity svr;

		ViolationCompositeImpl(final String message, final Severity svr)
		{
			this.message = message;
			this.svr = svr;
		}

		@Override
		public String getMessage()
		{
			return message;
		}

		@Override
		public Severity getSeverity()
		{
			return svr;
		}
	}
}
