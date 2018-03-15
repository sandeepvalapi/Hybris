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
 * Testing Past Constraint
 */
@IntegrationTest
public class PastConstraintTest extends AbstractConstraintTest
{
	/**
	 * Testsample: own POJO object with a Date field - must be in the past as the current time/date
	 */
	private void createPastConstraint()
	{
		final PastConstraintModel pastConstraint = new PastConstraintModel();
		pastConstraint.setId("pastConstraint");
		pastConstraint.setQualifier("date");
		pastConstraint.setTarget(POJO.class);
		modelService.initDefaults(pastConstraint);
		modelService.saveAll(pastConstraint);
		Assert.assertEquals(getDefaultMessage(Constraint.PAST.msgKey), pastConstraint.getDefaultMessage());
		validationService.reloadValidationEngine();
	}

	@Test
	public void testPastOK1() throws InterruptedException
	{
		final POJO xxx = new POJO();
		xxx.setDate(new Date(System.currentTimeMillis() - (10 * 1000L)));
		createPastConstraint();
		//!!!! Beware to do not place there any time-consuming logic it is highly time dependent !!!
		assertTrue(validationService.validate(xxx).isEmpty());
	}


	@Test
	public void testPastFalse1() throws InterruptedException
	{
		createPastConstraint();
		final POJO xxx = new POJO();
		xxx.setDate(new Date(System.currentTimeMillis() + (10 * 1000L)));
		//!!! Beware to do not place there any time-consuming logic it is highly time dependent !!!!
		assertFalse(validationService.validate(xxx).isEmpty());
	}

	@Test
	public void testPastNull() throws InterruptedException
	{
		createPastConstraint();
		final POJO xxx = new POJO();
		assertTrue(validationService.validate(xxx).isEmpty());
	}

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
