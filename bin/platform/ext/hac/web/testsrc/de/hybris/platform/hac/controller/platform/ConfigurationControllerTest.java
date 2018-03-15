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
package de.hybris.platform.hac.controller.platform;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import de.hybris.platform.hac.controller.ControllerConstants;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ConfigurationControllerTest
{
	@Spy
	private final ConfigurationController configurationController = new ConfigurationController();
	@Mock
	private SessionService sessionService;
	@Mock
	private Session session;

	private static final String VALID_KEY = "valid.key";
	private static final String INVALID_KEY = "invalidKey <script/>";
	private static final String VALUE = "value";

	@Before
	public void setUp()
	{
		doReturn(VALUE).when(configurationController).getParameter(VALID_KEY);
		configurationController.setSessionService(sessionService);
		when(sessionService.getCurrentSession()).thenReturn(session);
	}

	@Test
	public void valueChangedShouldReturnValidationErrorForInvalidKey()
	{
		//when
		final Map result = configurationController.valuechanged(INVALID_KEY, VALUE);

		//then
		assertThat(result).includes(entry(ControllerConstants.VALIDATION_ERROR, Boolean.TRUE));
	}

	@Test
	public void valueChangedShouldReturnValidResultForNotEditedValue()
	{
		//when
		final Map result = configurationController.valuechanged(VALID_KEY, VALUE);

		//then
		assertThat(result).includes(entry(ControllerConstants.VALIDATION_ERROR, Boolean.FALSE), //
				entry(ConfigurationController.CHANGED, Boolean.FALSE), //
				entry(ConfigurationController.HAS_EDITED, Boolean.FALSE));
	}

	@Test
	public void valueChangedShouldReturnValidResultForNewKey()
	{
		//given
		doReturn(null).when(configurationController).getParameter(VALID_KEY);

		//when
		final Map result = configurationController.valuechanged(VALID_KEY, VALUE);

		//then
		assertThat(result).includes(entry(ControllerConstants.VALIDATION_ERROR, Boolean.FALSE), //
				entry(ConfigurationController.IS_NEW, Boolean.TRUE), //
				entry(ConfigurationController.HAS_EDITED, Boolean.FALSE));
	}
}
