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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Testing the Future Constraint.
 */
@IntegrationTest
public class FutureConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: own POJO object with a Date field - must be in the future as the current time/date
	 */
	private void createFutureConstraint()
	{
		final FutureConstraintModel futureConstraint = modelService.create(FutureConstraintModel.class);
		futureConstraint.setId("futureConstraint");
		futureConstraint.setQualifier("date");
		futureConstraint.setTarget(POJO.class);

		modelService.saveAll(futureConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.FUTURE.msgKey), futureConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	/**
	 * Testing a date witch is 1sec+something old
	 */
	@Test
	public void testFutureFalse1()
	{
		final POJO xxx = new POJO();
		xxx.setDate(new Date(System.currentTimeMillis() - (10 * 1000L)));
		createFutureConstraint();
		//!!! Beware to do not place there any time-consuming logic it is highly time dependent!!!
		assertFalse(validationService.validate(xxx).isEmpty());
	}

	/**
	 * Testing a date in the future
	 */
	@Test
	public void testFutureOK1()
	{
		createFutureConstraint();
		final POJO xxx = new POJO();
		xxx.setDate(new Date(System.currentTimeMillis() + (10 * 1000L)));
		//!!! Beware to do not place there any time-consuming logic it is highly time dependent!!!
		assertTrue(validationService.validate(xxx).isEmpty());
	}

	/**
	 * Null is valid therefore ok
	 */
	@Test
	public void testFutureNull()
	{
		createFutureConstraint();
		final POJO xxx = new POJO();
		assertTrue(validationService.validate(xxx).isEmpty());
	}

	/**
	 * Testobject
	 */
	protected class POJO
	{
		private Date date;

		public void setDate(final Date date)
		{
			this.date = date;
		}

		public Date getDate()
		{
			return date;
		}
	}
}
