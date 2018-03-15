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
package de.hybris.platform.audit;

import de.hybris.platform.util.Config;
import org.junit.Assume;


/**
 * Every Audit related tests, which requires the auditing to be enabled should use this interface.
 */
public interface AuditableTest
{
	/**
	 * Assumes the audting is enabled globally. If not - AssumptionViolatedException is thrown, which junit interprets as
	 * test to be ignored.
	 */
	default void assumeAuditEnabled()
	{
		Assume.assumeTrue("Auditing is disabled globally - test will be ignored", Config.getBoolean("auditing.enabled", false));
	}
}
