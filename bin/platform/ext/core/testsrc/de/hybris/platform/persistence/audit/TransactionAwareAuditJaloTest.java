/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.persistence.audit;

import de.hybris.bootstrap.annotations.IntegrationTest;

import org.junit.Before;


@IntegrationTest
public class TransactionAwareAuditJaloTest extends TransactionAwareAuditTest
{

	@Before
	public void prepare()
	{
		persistanceLegacyModeSwitcher.switchToValue("true");
		allTypesEnabledSwitcher.switchToValue("true");
		assumeAuditEnabled();
		warmUp();
	}

}
