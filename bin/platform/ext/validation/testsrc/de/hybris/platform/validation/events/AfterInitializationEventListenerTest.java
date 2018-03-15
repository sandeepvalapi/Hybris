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
package de.hybris.platform.validation.events;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.validation.services.ValidationService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


@UnitTest
public class AfterInitializationEventListenerTest
{
	@Mock
	private ValidationService validationService;

	@Spy
	private final AfterInitializationEndEventListener listener = new AfterInitializationEndEventListener();

	private final Throwable someExpectedExcpetion = new ExpectedUnsupportedOperationException();

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.doReturn(validationService).when(listener).getValidationService();
		Mockito.doThrow(someExpectedExcpetion).when(validationService).reloadValidationEngine();
	}

	@Test
	public void testMessageSend()
	{
		final AfterInitializationEndEvent event = new AfterInitializationEndEvent();

		try
		{
			listener.onEvent(event);
			Assert.fail("Should be called a validationService#reloadValidationEngine");
		}
		catch (final ExpectedUnsupportedOperationException e)
		{
			//ok here
		}

	}

	class ExpectedUnsupportedOperationException extends UnsupportedOperationException
	{
		//
	}
}
