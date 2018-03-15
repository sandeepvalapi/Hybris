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
package de.hybris.platform.servicelayer.media.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit class for {@link DefaultMediaPermissionService}
 */
@UnitTest
public class DefaultMediaPermissionServiceTest
{

	private DefaultMediaPermissionService mediaPermissionService;

	@Mock
	private PermissionCheckingService mockPermissionCheckingService;
	@Mock
	private PermissionManagementService mockPermissionManagementService;

	private MediaModel testMediaItem;
	private PrincipalModel testPrincipal;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		mediaPermissionService = new DefaultMediaPermissionService();
		mediaPermissionService.setPermissionCheckingService(mockPermissionCheckingService);
		mediaPermissionService.setPermissionManagementService(mockPermissionManagementService);

		testMediaItem = new MediaModel();
		testPrincipal = new PrincipalModel();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#isReadAccessGranted(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testIsReadAccessGrantedTrue()
	{
		final PermissionCheckResult expectedResult = new PermissionCheckResult()
		{
			@Override
			public boolean isGranted()
			{
				return true;
			}

			@Override
			public boolean isDenied()
			{
				return false;
			}

			@Override
			public PermissionCheckValue getCheckValue()
			{
				return PermissionCheckValue.ALLOWED;
			}
		};
		given(mockPermissionCheckingService.checkItemPermission(testMediaItem, testPrincipal, PermissionsConstants.READ))
				.willReturn(expectedResult);

		final boolean result = mediaPermissionService.isReadAccessGranted(testMediaItem, testPrincipal);
		Assert.assertTrue(result);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#isReadAccessGranted(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testIsReadAccessGrantedFalse()
	{
		final PermissionCheckResult expectedResult = new PermissionCheckResult()
		{
			@Override
			public boolean isGranted()
			{
				return false;
			}

			@Override
			public boolean isDenied()
			{
				return true;
			}

			@Override
			public PermissionCheckValue getCheckValue()
			{
				return PermissionCheckValue.DENIED;
			}
		};
		given(mockPermissionCheckingService.checkItemPermission(testMediaItem, testPrincipal, PermissionsConstants.READ))
				.willReturn(expectedResult);

		final boolean result = mediaPermissionService.isReadAccessGranted(testMediaItem, testPrincipal);
		Assert.assertFalse(result);
	}

}
