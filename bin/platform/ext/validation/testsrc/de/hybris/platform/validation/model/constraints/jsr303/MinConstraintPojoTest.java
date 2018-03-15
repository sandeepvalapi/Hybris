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

import de.hybris.bootstrap.annotations.IntegrationTest;

import org.junit.Test;


@IntegrationTest
public class MinConstraintPojoTest extends AbstractConstraintTest
{
	@Test
	//interesting, it souldn'T work - hibernate validation does not support double (lower d)
	public void testCreateMinConstraintForDouble()
	{
		final MinConstraintModel minConstraint = modelService.create(MinConstraintModel.class);
		minConstraint.setId("minConstraint");
		minConstraint.setValue(Long.valueOf(3));
		minConstraint.setQualifier("value");
		minConstraint.setTarget(MinConstraintPojo.class);
		modelService.save(minConstraint);
		validationService.reloadValidationEngine();

		final MinConstraintPojo xxx = new MinConstraintPojo();
		assertEquals(0, xxx.getValue(), 0.1);
		assertEquals(1, validationService.validate(xxx).size());

		xxx.setValue(2.9999);
		assertEquals(1, validationService.validate(xxx).size());

		xxx.setValue(2.47);
		assertEquals(1, validationService.validate(xxx).size());

		xxx.setValue(3.01);
		assertEquals(0, validationService.validate(xxx).size());

		xxx.setValue(5.00);
		assertEquals(0, validationService.validate(xxx).size());
	}

	/**
	 * Testobject
	 */
	protected class MinConstraintPojo
	{
		private double value;

		public void setValue(final double value)
		{
			this.value = value;
		}

		public double getValue()
		{
			return value;
		}
	}

}
