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
package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PaymentInfoSldTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;


	private static final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");


	@Before
	public void setUp()
	{
		persistenceLegacyMode.switchToValue("false");
	}

	@After
	public void tearDown()
	{
		persistenceLegacyMode.switchBackToDefault();
	}

	@Test
	public void shouldCreatePaymentInfoThroughSld()
	{
		final PaymentInfoModel paymentInfo = modelService.create(PaymentInfoModel.class);


		paymentInfo.setCode("someCode");
		paymentInfo.setUser(userService.getCurrentUser());

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, paymentInfo);


		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(paymentInfo);
	}
}
