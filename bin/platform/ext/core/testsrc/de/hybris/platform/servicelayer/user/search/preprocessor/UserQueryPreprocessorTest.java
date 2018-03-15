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
package de.hybris.platform.servicelayer.user.search.preprocessor;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class UserQueryPreprocessorTest
{
	@InjectMocks
	private final QueryPreprocessor preprocessor = new UserQueryPreprocessor();
	@Mock
	private UserService userService;
	@Mock
	private FlexibleSearchQuery query;
	@Mock
	private UserModel user;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotProcessWhenUserInQueryObjectIsNull()
	{
		// given
		given(query.getUser()).willReturn(null);

		// when
		preprocessor.process(query);

		// then
		verify(userService, times(0)).setCurrentUser((UserModel) anyObject());
	}

	@Test
	public void shouldProcessWhenThereIsUserInQueryObject()
	{
		// given
		given(query.getUser()).willReturn(user);

		// when
		preprocessor.process(query);

		// then
		verify(userService, times(1)).setCurrentUser(user);
	}

}
