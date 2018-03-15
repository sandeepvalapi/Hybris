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
package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.CronExpression;


/**
 * Test presenting the {@link CronExpression} base mechanism for calculating next trigger execution time for given cron
 * expression .
 */
@UnitTest
public class TriggerValidateInterceptorTest
{
	private TriggerValidateInterceptor validator;

	private final static Logger LOG = Logger.getLogger(TriggerValidateInterceptorTest.class.getName());

	private final Date currentDate = new Date(System.currentTimeMillis());

	@Before
	public void setUp()
	{
		validator = new TriggerValidateInterceptor()
		{
			@Override
			protected Date getCurrentTime()
			{
				return currentDate;
			}
		};
	}


	@Test
	public void testFillInActivationTimeIfWasNull()
	{
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);

		final TriggerModel model = new TriggerModel();
		model.setCronExpression("10 0/5 * * * ?");

		try
		{
			validator.onValidate(model, ctx);
			Assert.assertEquals(model.getActivationTime(), new CronExpression("10 0/5 * * * ?").getNextValidTimeAfter(currentDate));
		}
		catch (final InterceptorException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
		catch (final ParseException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
	}

	@Test
	public void testDoNotFillInActivationIfInTheFutureAndNotModified()
	{
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);

		final TriggerModel model = new TriggerModel();
		model.setCronExpression("10 0/5 * * * ?");
		model.setActivationTime(new Date(Long.MAX_VALUE));

		Mockito.when(Boolean.valueOf(ctx.isModified(model, TriggerModel.CRONEXPRESSION))).thenReturn(Boolean.FALSE);

		try
		{
			validator.onValidate(model, ctx);
			Assert.assertEquals(model.getActivationTime(), new Date(Long.MAX_VALUE));
		}
		catch (final InterceptorException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
	}


	@Test
	public void testFillInIfExpressionChanged()
	{
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);

		final TriggerModel model = new TriggerModel();
		model.setCronExpression("10 0/5 * * * ?");

		Mockito.when(Boolean.valueOf(ctx.isModified(model, TriggerModel.CRONEXPRESSION))).thenReturn(Boolean.TRUE);

		try
		{
			validator.onValidate(model, ctx);
			Assert.assertEquals(model.getActivationTime(), new CronExpression("10 0/5 * * * ?").getNextValidTimeAfter(currentDate));
		}
		catch (final InterceptorException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
		catch (final ParseException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
	}

	@Test
	public void testFillInActivationTimeIfWasInThePast()
	{
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);

		final TriggerModel model = new TriggerModel();
		model.setCronExpression("10 0/5 * * * ?");
		model.setActivationTime(new Date(0));
		try
		{
			validator.onValidate(model, ctx);
			Assert.assertEquals(model.getActivationTime(), new CronExpression("10 0/5 * * * ?").getNextValidTimeAfter(currentDate));
		}
		catch (final InterceptorException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}
		catch (final ParseException e)
		{
			Assert.fail(e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
		}

	}



	@Test
	public void testWithInvalidExpressin()
	{

		final TriggerModel model = new TriggerModel();
		model.setCronExpression("weird exopression");

		try
		{
			validator.onValidate(model, null);
			Assert.fail("The expression is invalid");
		}
		catch (final InterceptorException e)
		{
			// ok here
		}

	}



}
