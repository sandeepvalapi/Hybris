/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.security;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider;
import de.hybris.platform.acceleratorstorefrontcommons.security.BruteForceAttackCounter;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


@UnitTest
public class AcceleratorAuthenticationProviderTest
{
	private AcceleratorAuthenticationProvider acceleratorAuthenticationProvider;

	@Mock
	private AbstractAcceleratorAuthenticationProvider abstractAcceleratorAuthenticationProvider;

	private Authentication authentication;

	@Mock
	private BruteForceAttackCounter bruteForceAttackCounter;

	@Mock
	private UserModel userModel;

	@Mock
	UserService userService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		acceleratorAuthenticationProvider = new AcceleratorAuthenticationProvider();
		acceleratorAuthenticationProvider.setBruteForceAttackCounter(bruteForceAttackCounter);
		acceleratorAuthenticationProvider.setUserService(userService);
		authentication = new UsernamePasswordAuthenticationToken("username", "password");
	}

	@Test(expected = BadCredentialsException.class)
	public void testLoginForUserNotPartofCustomerGroup()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 2);
		userModel.setDeactivationDate(calendar.getTime());
		final UserGroupModel userGroupModel = mock(UserGroupModel.class);
		given(userService.getUserForUID(Mockito.anyString())).willReturn(userModel);
		given(userService.getUserGroupForUID(Constants.USER.CUSTOMER_USERGROUP)).willReturn(userGroupModel);
		given(Boolean.valueOf(userService.isMemberOfGroup(userModel, userGroupModel))).willReturn(Boolean.FALSE);
		acceleratorAuthenticationProvider.authenticate(authentication);
	}
}
