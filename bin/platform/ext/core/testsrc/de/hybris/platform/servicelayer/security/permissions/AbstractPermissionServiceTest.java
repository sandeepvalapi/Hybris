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
package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;


/**
 *
 */
@Ignore
public abstract class AbstractPermissionServiceTest extends ServicelayerTransactionalBaseTest
{
	protected static final String TEST_PERMISSION_1 = "TP1";
	protected static final String TEST_PERMISSION_2 = "TP2";
	protected static final String TEST_PERMISSION_3 = "TP3";
	protected static final String TEST_PERMISSION_4 = "TP4";
	protected static final String TEST_PERMISSION_5 = "TP5";

	protected static final String TEST_USER_1_UID = "testUser1";
	protected static final String TEST_USER_2_UID = "testUser2";
	protected static final String TEST_USER_3_UID = "testUser3";
	protected static final String TEST_USER_4_UID = "testUser4";

	@Resource
	protected PermissionManagementService permissionManagementService;

	@Resource
	protected ModelService modelService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	protected PrincipalModel testUser1;
	protected PrincipalModel testUser2;

	@Before
	public void setUp()
	{
		testUser1 = modelService.create(UserModel.class);
		testUser1.setUid(TEST_USER_1_UID);
		modelService.save(testUser1);

		testUser2 = modelService.create(UserModel.class);
		testUser2.setUid(TEST_USER_2_UID);
		modelService.save(testUser2);
	}


	protected Tenant getTenant()
	{
		return Registry.getCurrentTenant();
	}
}
